package org.egov.works.estimate.domain.service;

import java.util.*;

import net.minidev.json.JSONArray;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.DetailedEstimateRepository;
import org.egov.works.estimate.domain.validator.EstimateValidator;
import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.egov.works.workflow.service.WorkflowService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DetailedEstimateService {

	public static final String DETAILED_ESTIMATE_WF_TYPE = "DetailedEstimate";

	public static final String DETAILED_ESTIMATE_BUSINESSKEY = "DetailedEstimate";

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private DetailedEstimateRepository detailedEstimateRepository;

	@Autowired
	private EstimateUtils estimateUtils;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private EstimateValidator validator;

	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
		return detailedEstimateRepository.search(detailedEstimateSearchContract);
	}

	public DetailedEstimateResponse create(DetailedEstimateRequest detailedEstimateRequest, Boolean isRevision) {
		validator.validateDetailedEstimates(detailedEstimateRequest, isRevision);
		AuditDetails auditDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), false);
		for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
			detailedEstimate.setId(commonUtils.getUUID());
			detailedEstimate.setAuditDetails(auditDetails);
			detailedEstimate.setTotalIncludingRE(detailedEstimate.getEstimateValue());
			if(detailedEstimate.getSpillOverFlag()) {
			    detailedEstimate.setApprovedDate(detailedEstimate.getEstimateDate());
			}
			AbstractEstimate abstactEstimate = null;
			if (detailedEstimate.getAbstractEstimateDetail() != null || (isRevision != null && isRevision)) {
				abstactEstimate = validator.searchAbstractEstimate(detailedEstimate);
                detailedEstimate.setAbstractEstimateDetail(abstactEstimate.getAbstractEstimateDetails().get(0));
                detailedEstimate.setProjectCode(abstactEstimate.getAbstractEstimateDetails().get(0).getProjectCode());
				if ((abstactEstimate != null && !abstactEstimate.getDetailedEstimateCreated())
						 || (isRevision != null && isRevision)) {
					String estimateNumber = idGenerationRepository.generateDetailedEstimateNumber(
							detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
					detailedEstimate.setEstimateNumber(estimateUtils.getCityCode(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()) + "/" + propertiesManager.getDetailedEstimateNumberPrefix() + '/'
							+ detailedEstimate.getDepartment().getCode() + estimateNumber);
				}
			}

            if(validator.checkAbstractEstimateRequired(detailedEstimate,detailedEstimateRequest.getRequestInfo())) {
                if(abstactEstimate != null) {
                    detailedEstimate.setNameOfWork(abstactEstimate.getNatureOfWork().getCode());
                    detailedEstimate.setWard(abstactEstimate.getWard());
                    detailedEstimate.setLocality(abstactEstimate.getLocality());
                    detailedEstimate.setBillsCreated(abstactEstimate.getBillsCreated());
                    detailedEstimate.setSpillOverFlag(abstactEstimate.getSpillOverFlag());
                    detailedEstimate.setBeneficiary(abstactEstimate.getBeneficiary());
                    detailedEstimate.setFunction(abstactEstimate.getFunction());
                    detailedEstimate.setFund(abstactEstimate.getFund());
                    detailedEstimate.setBudgetGroup(abstactEstimate.getBudgetGroup());
                    detailedEstimate.setDepartment(abstactEstimate.getDepartment());
                    detailedEstimate.setModeOfAllotment(abstactEstimate.getModeOfAllotment());
                    detailedEstimate.setWorksType(abstactEstimate.getTypeOfWork());
                    detailedEstimate.setWorksSubtype(abstactEstimate.getSubTypeOfWork());
                    List<AssetsForEstimate> assetsForEstimates = new ArrayList<>();
                    if(abstactEstimate.getAssetDetails() != null && !abstactEstimate.getAssetDetails().isEmpty()) {
                        AssetsForEstimate assetsForEstimate = null;
                        for(AbstractEstimateAssetDetail assets : abstactEstimate.getAssetDetails()) {
                            assetsForEstimate = new AssetsForEstimate();
                            assetsForEstimate.setLandAsset(assets.getLandAsset());
                            assetsForEstimate.setAsset(assets.getAsset());
                            assetsForEstimate.setAuditDetails(auditDetails);
                            assetsForEstimate.setId(commonUtils.getUUID());
                            assetsForEstimates.add(assetsForEstimate);
                        }
                    }
                    detailedEstimate.setAssets(assetsForEstimates);
                }

            }

			if (isRevision == null || (isRevision != null && !isRevision)) {
                MultiYearEstimate multiYearEstimate = new MultiYearEstimate();
                multiYearEstimate.setId(commonUtils.getUUID());
                multiYearEstimate.setFinancialYear(getCurrentFinancialYear(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()));
                multiYearEstimate.setTenantId(detailedEstimate.getTenantId());
                multiYearEstimate.setPercentage(100d);
                multiYearEstimate.setAuditDetails(auditDetails);
                detailedEstimate.setMultiYearEstimates(Arrays.asList(multiYearEstimate));

				for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
					estimateOverhead.setId(commonUtils.getUUID());
					estimateOverhead.setAuditDetails(auditDetails);
				}

				for (final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate
						.getDetailedEstimateDeductions()) {
					detailedEstimateDeduction.setId(commonUtils.getUUID());
					detailedEstimateDeduction.setAuditDetails(auditDetails);
				}

				if (detailedEstimate.getEstimateTechnicalSanctions() != null) {
					for (final EstimateTechnicalSanction estimateTechnicalSanction : detailedEstimate
							.getEstimateTechnicalSanctions()) {
						estimateTechnicalSanction.setId(commonUtils.getUUID());
						estimateTechnicalSanction.setAuditDetails(auditDetails);
                        estimateTechnicalSanction.setDetailedEstimate(detailedEstimate.getId());
					}
				}
				
				if (detailedEstimate.getDocumentDetails() != null) {
					for (DocumentDetail documentDetail : detailedEstimate.getDocumentDetails()) {
						documentDetail.setObjectId(detailedEstimate.getEstimateNumber());
						documentDetail.setObjectType(CommonConstants.DETAILEDESTIMATE);
						documentDetail.setAuditDetails(auditDetails);
					}
				}
			}

			for (final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
				estimateActivity.setId(commonUtils.getUUID());
				estimateActivity.setAuditDetails(auditDetails);
				if (estimateActivity.getEstimateMeasurementSheets() != null) {
					for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity
							.getEstimateMeasurementSheets()) {
						estimateMeasurementSheet.setId(commonUtils.getUUID());
						estimateMeasurementSheet.setAuditDetails(auditDetails);
                        estimateMeasurementSheet.setEstimateActivity(estimateActivity.getId());
					}
				}
                if(estimateActivity.getNonSor() != null) {
                    estimateActivity.getNonSor().setId(commonUtils.getUUID());
                }
			}

            if(validator.workflowRequired(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()) &&
                 isRevision == null || (isRevision != null && !isRevision)) {
				populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo(), abstactEstimate);
				Map<String, String> workFlowResponse = workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
						detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
				detailedEstimate.setStateId(workFlowResponse.get("id"));
				detailedEstimate.setStatus(DetailedEstimateStatus.valueOf(workFlowResponse.get("status")));
			} else if(detailedEstimate.getSpillOverFlag())
				detailedEstimate.setStatus(DetailedEstimateStatus.TECHNICAL_SANCTIONED);
            else
                detailedEstimate.setStatus(DetailedEstimateStatus.CREATED);
		}
		if (isRevision == null || (isRevision != null && !isRevision))
			kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateAndUpdateTopic(), detailedEstimateRequest);
		else
			kafkaTemplate.send(propertiesManager.getWorksRECreateUpdateTopic(), detailedEstimateRequest);
		final DetailedEstimateResponse response = new DetailedEstimateResponse();
		response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
		response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
		return response;
	}

    private FinancialYear getCurrentFinancialYear(final String tenantId, final RequestInfo requestInfo) {
        JSONArray jsonArray = estimateUtils.getMDMSData(CommonConstants.FINANCIALYEAR_OBJECT, "tenantId", tenantId,
                tenantId, requestInfo, Constants.EGF_MODULE_CODE);
        FinancialYear financialYear = null;
        if(jsonArray != null && !jsonArray.isEmpty()) {
            for(int i= 0 ; i < jsonArray.size(); i++) {
                Map<String, Object> jsonMap = (Map<String, Object>) jsonArray.get(i);
                long fromDate = (long) jsonMap.get("startingDate");
                long toDate = (long) jsonMap.get("endingDate");
                long currentDateTime = new Date().getTime();
                if(fromDate<= currentDateTime && toDate >= currentDateTime) {
                    financialYear = new FinancialYear();
                    financialYear.setId((String) jsonMap.get("id"));
                    financialYear.setFinYearRange((String) jsonMap.get("finYearRange"));
                    break;
                }
            }
        }
        return financialYear;
    }

    public DetailedEstimateResponse update(DetailedEstimateRequest detailedEstimateRequest, Boolean isRevision) {
		validator.validateDetailedEstimates(detailedEstimateRequest, isRevision);
		AuditDetails updateDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), true);
		AuditDetails createDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), false);
		AbstractEstimate abstactEstimate = null;
		for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            abstactEstimate = validator.searchAbstractEstimate(detailedEstimate);

            if(detailedEstimate.getSpillOverFlag()) {
                detailedEstimate.setApprovedDate(detailedEstimate.getEstimateDate());
            }
            detailedEstimate.setAuditDetails(updateDetails);

            if (detailedEstimate.getAssets() != null) {
                for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets()) {
                    if (assetsForEstimate.getId() == null) {
                        assetsForEstimate.setId(commonUtils.getUUID());
                        assetsForEstimate.setAuditDetails(createDetails);
                    }
                    assetsForEstimate.setAuditDetails(updateDetails);
                }
            }

            if (detailedEstimate.getEstimateOverheads() != null) {
                for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
                    if (estimateOverhead.getId() == null) {
                        estimateOverhead.setId(commonUtils.getUUID());
                        estimateOverhead.setAuditDetails(createDetails);
                    }
                    estimateOverhead.setAuditDetails(updateDetails);
                }
            }

            if(detailedEstimate.getDetailedEstimateDeductions() != null) {
                for (final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate
                        .getDetailedEstimateDeductions()) {
                    if (detailedEstimateDeduction.getId() == null) {
                        detailedEstimateDeduction.setId(commonUtils.getUUID());
                        detailedEstimateDeduction.setAuditDetails(createDetails);
                    }
                    detailedEstimateDeduction.setAuditDetails(updateDetails);
                }
            }

            if(detailedEstimate.getEstimateActivities() != null) {
                for (final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
                    if (estimateActivity.getId() == null) {
                        estimateActivity.setId(commonUtils.getUUID());
                        estimateActivity.setAuditDetails(createDetails);
                    }
                    estimateActivity.setAuditDetails(updateDetails);
                    if (estimateActivity.getEstimateMeasurementSheets() != null) {
                        for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity
                                .getEstimateMeasurementSheets()) {
                            if (estimateMeasurementSheet.getId() == null) {
                                estimateMeasurementSheet.setId(commonUtils.getUUID());
                                estimateMeasurementSheet.setAuditDetails(createDetails);
                            }
                            estimateMeasurementSheet.setAuditDetails(updateDetails);
                        }
                    }
                }
            }
            if(validator.workflowRequired(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo())) {
                populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo(), abstactEstimate);
                Map<String, String> workFlowResponse = workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
                        detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
                detailedEstimate.setStateId(workFlowResponse.get("id"));
                detailedEstimate.setStatus(DetailedEstimateStatus.valueOf(workFlowResponse.get("status")));
            }
            
            if(!detailedEstimate.getSpillOverFlag() && detailedEstimate.getStatus().toString().equalsIgnoreCase(DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString())) {
                detailedEstimate.setApprovedDate(new Date().getTime());
            }

		}
		kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateAndUpdateTopic(), detailedEstimateRequest);
		final DetailedEstimateResponse response = new DetailedEstimateResponse();
		response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
		response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
		return response;
	}

	private void populateWorkFlowDetails(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
			AbstractEstimate abstactEstimate) {

		if (null != detailedEstimate && null != detailedEstimate.getWorkFlowDetails()) {

			WorkFlowDetails workFlowDetails = detailedEstimate.getWorkFlowDetails();
			if (abstactEstimate != null && abstactEstimate.getDetailedEstimateCreated()) {
				workFlowDetails.setType(CommonConstants.SPILLOVER_DETAILED_ESTIMATE_WF_TYPE);
				workFlowDetails.setBusinessKey(CommonConstants.SPILLOVER_DETAILED_ESTIMATE_WF_TYPE);
			} else {
				workFlowDetails.setType(DETAILED_ESTIMATE_WF_TYPE);
				workFlowDetails.setBusinessKey(DETAILED_ESTIMATE_BUSINESSKEY);
			}
			workFlowDetails.setStateId(detailedEstimate.getStateId());
			if (detailedEstimate.getStatus() != null)
				workFlowDetails.setStatus(detailedEstimate.getStatus().toString());

			if (null != requestInfo && null != requestInfo.getUserInfo()) {
				workFlowDetails.setSenderName(requestInfo.getUserInfo().getUserName());
			}

			if (detailedEstimate.getStateId() != null) {
				workFlowDetails.setStateId(detailedEstimate.getStateId());
			}
		}
	}
}
