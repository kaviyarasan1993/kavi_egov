package org.egov.works.estimate.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.domain.validator.EstimateValidator;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.AbstractEstimate;
import org.egov.works.estimate.web.contract.AbstractEstimateAssetDetail;
import org.egov.works.estimate.web.contract.AbstractEstimateDetails;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateResponse;
import org.egov.works.estimate.web.contract.AbstractEstimateSanctionDetail;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.egov.works.estimate.web.contract.AbstractEstimateStatus;
import org.egov.works.estimate.web.contract.DocumentDetail;
import org.egov.works.estimate.web.contract.EstimateAppropriation;
import org.egov.works.estimate.web.contract.EstimateAppropriationRequest;
import org.egov.works.estimate.web.contract.EstimateAppropriationResponse;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeRequest;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.WorkFlowDetails;
import org.egov.works.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class AbstractEstimateService {

    @Autowired
    private AbstractEstimateRepository abstractEstimateRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private IdGenerationRepository idGenerationRepository;

    @Autowired
    private EstimateUtils estimateUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProjectCodeService projectCodeService;

    @Autowired
    private EstimateValidator validator;
    
    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @Transactional
    public AbstractEstimateResponse create(AbstractEstimateRequest abstractEstimateRequest) {
        validator.validateEstimates(abstractEstimateRequest, true);
        Boolean isSpilloverWFReq = false;
        ProjectCode projectCode = new ProjectCode();
        for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
            estimate.setId(commonUtils.getUUID());
            estimate.setAuditDetails(estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
            for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails()) {
                details.setId(commonUtils.getUUID());
                details.setAuditDetails(estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
            }
            if (estimate.getSanctionDetails() != null)
                for (final AbstractEstimateSanctionDetail sanctionDetail : estimate.getSanctionDetails())
                    sanctionDetail.setId(commonUtils.getUUID());
            if (estimate.getAssetDetails() != null) {
                for (final AbstractEstimateAssetDetail assetDetail : estimate.getAssetDetails()) {
                    assetDetail.setId(commonUtils.getUUID());
                    assetDetail.setAuditDetails(
                            estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
                }
            }

            Boolean isBudgetCheckReq = isBudgetCheckRequired(CommonConstants.BUDGET_CHECK_REQUIRED,
                    abstractEstimateRequest.getRequestInfo(), estimate.getTenantId());

            if (!estimate.getSpillOverFlag()) {
                String abstractEstimateNumber = idGenerationRepository.generateAbstractEstimateNumber(
                        estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
                // TODO: check idgen to accept values to generate
                estimate.setAbstractEstimateNumber(
                        estimateUtils.getCityCode(estimate.getTenantId(), abstractEstimateRequest.getRequestInfo()) + "/"
                                + propertiesManager.getEstimateNumberPrefix() + "/"
                                + estimate.getDepartment().getCode() + abstractEstimateNumber);
            }
            if (estimate.getDocumentDetails() != null) {
                for (final DocumentDetail documentDetail : estimate.getDocumentDetails()) {
                    documentDetail.setId(commonUtils.getUUID());
                    documentDetail.setObjectId(estimate.getAbstractEstimateNumber());
                    documentDetail.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
                    documentDetail.setAuditDetails(
                            estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
                }
            }
            if (estimate.getSpillOverFlag())
                isSpilloverWFReq = isConfigRequired(CommonConstants.SPILLOVER_WORKFLOW_MANDATORY,
                        abstractEstimateRequest.getRequestInfo(), estimate.getTenantId());
            if (!isSpilloverWFReq && estimate.getSpillOverFlag()) {
                estimate.setStatus(AbstractEstimateStatus.ADMIN_SANCTIONED);
                for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
                    projectCode.setCode(setProjectCode(abstractEstimateDetails, estimate.getSpillOverFlag(),
                            abstractEstimateRequest.getRequestInfo(), estimate, Boolean.FALSE));
                    abstractEstimateDetails.setProjectCode(projectCode);
                    if (isBudgetCheckReq)
                        estimateAppropriationService.setEstimateAppropriation(abstractEstimateDetails, abstractEstimateRequest.getRequestInfo(), Boolean.TRUE);
                }
            } else {
                populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
                Map<String, String> workFlowResponse = workflowService.enrichWorkflow(estimate.getWorkFlowDetails(),
                        estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
                estimate.setStateId(workFlowResponse.get("id"));
                estimate.setStatus(AbstractEstimateStatus.valueOf(workFlowResponse.get("status")));
            }

        }
        kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateCreateAndUpdateTopic(), abstractEstimateRequest);
        final AbstractEstimateResponse response = new AbstractEstimateResponse();
        response.setAbstractEstimates(abstractEstimateRequest.getAbstractEstimates());
        response.setResponseInfo(estimateUtils.getResponseInfo(abstractEstimateRequest.getRequestInfo()));
        return response;
    }

    private Boolean isConfigRequired(String keyName, RequestInfo requestInfo, final String tenantId) {
        Boolean isSpilloverWFReq = false;
        JSONArray responseJSONArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                keyName, tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                isSpilloverWFReq = true;
        }
        return isSpilloverWFReq;
    }

    private Boolean isBudgetCheckRequired(String keyName, RequestInfo requestInfo, final String tenantId) {
        Boolean isBudgetCheckReq = false;
        JSONArray responseJSONArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                keyName, tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                isBudgetCheckReq = true;
        }
        return isBudgetCheckReq;
    }

    public AbstractEstimateResponse update(AbstractEstimateRequest abstractEstimateRequest) {
        validator.validateEstimates(abstractEstimateRequest, false);
        ProjectCode projectCode = new ProjectCode();
        for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
            if(estimate.getDocumentDetails() != null)
                for (final DocumentDetail documentDetail : estimate.getDocumentDetails()) {
                    if (documentDetail.getId() == null)
                        documentDetail.setId(commonUtils.getUUID());
                    documentDetail.setObjectId(estimate.getAbstractEstimateNumber());
                    documentDetail.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
                    documentDetail
                            .setAuditDetails(estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), true));
                }
            populateAuditDetails(abstractEstimateRequest.getRequestInfo(), estimate);
            populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
            Map<String, String> workFlowResponse = workflowService.enrichWorkflow(estimate.getWorkFlowDetails(),
                    estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
            estimate.setStateId(workFlowResponse.get("id"));
            estimate.setStatus(AbstractEstimateStatus.valueOf(workFlowResponse.get("status")));

            Boolean isFinIntReq = isConfigRequired(CommonConstants.FINANCIAL_INTEGRATION_KEY,
                    abstractEstimateRequest.getRequestInfo(), estimate.getTenantId());

            Boolean isBudgetCheckReq = isBudgetCheckRequired(CommonConstants.BUDGET_CHECK_REQUIRED,
                    abstractEstimateRequest.getRequestInfo(), estimate.getTenantId());

            
            Boolean isSpilloverWFReq = isConfigRequired(CommonConstants.SPILLOVER_WORKFLOW_MANDATORY,
                    abstractEstimateRequest.getRequestInfo(), estimate.getTenantId());
            
            if(isSpilloverWFReq && estimate.getStatus().toString()
                    .equalsIgnoreCase(AbstractEstimateStatus.ADMIN_SANCTIONED.toString())) {
                for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
                    projectCode.setCode(setProjectCode(abstractEstimateDetails, estimate.getSpillOverFlag(),
                            abstractEstimateRequest.getRequestInfo(), estimate, Boolean.FALSE));
                    abstractEstimateDetails.setProjectCode(projectCode);
                    if (isFinIntReq && isBudgetCheckReq) {
                        estimateAppropriationService.setEstimateAppropriation(abstractEstimateDetails, abstractEstimateRequest.getRequestInfo(), Boolean.TRUE);
                    }
                        
                }
            }
            if (estimate.getStatus().toString()
                    .equalsIgnoreCase(AbstractEstimateStatus.FINANCIAL_SANCTIONED.toString())) {
                for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
                    projectCode.setCode(setProjectCode(abstractEstimateDetails, estimate.getSpillOverFlag(),
                            abstractEstimateRequest.getRequestInfo(), estimate, Boolean.FALSE));
                    abstractEstimateDetails.setProjectCode(projectCode);
                    if (isFinIntReq && isBudgetCheckReq)
                        estimateAppropriationService.setEstimateAppropriation(abstractEstimateDetails, abstractEstimateRequest.getRequestInfo(), Boolean.TRUE);
                }
            }

            if (estimate.getStatus().toString()
                    .equalsIgnoreCase(AbstractEstimateStatus.CANCELLED.toString())) {
                for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
                    projectCode.setCode(setProjectCode(abstractEstimateDetails, estimate.getSpillOverFlag(),
                            abstractEstimateRequest.getRequestInfo(), estimate, Boolean.TRUE));
                    abstractEstimateDetails.setProjectCode(projectCode);
                }
            }

        }
        kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateCreateAndUpdateTopic(), abstractEstimateRequest);
        final AbstractEstimateResponse response = new AbstractEstimateResponse();
        response.setAbstractEstimates(abstractEstimateRequest.getAbstractEstimates());
        response.setResponseInfo(estimateUtils.getResponseInfo(abstractEstimateRequest.getRequestInfo()));
        return response;
    }

    private void populateAuditDetails(final RequestInfo requestInfo, final AbstractEstimate estimate) {
        for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails())
            details.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
        for (final AbstractEstimateAssetDetail assetDetail : estimate.getAssetDetails())
            assetDetail.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
        for (final DocumentDetail documentDetail : estimate.getDocumentDetails())
            documentDetail.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
        estimate.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
    }

    private void populateWorkFlowDetails(AbstractEstimate abstractEstimate, RequestInfo requestInfo) {

        if (null != abstractEstimate && null != abstractEstimate.getWorkFlowDetails()) {

            WorkFlowDetails workFlowDetails = abstractEstimate.getWorkFlowDetails();

            if (abstractEstimate.getSpillOverFlag()) {
                workFlowDetails.setType(CommonConstants.SPILLOVER_ABSTRACT_ESTIMATE_WF_TYPE);
                workFlowDetails.setBusinessKey(CommonConstants.SPILLOVER_ABSTRACT_ESTIMATE_BUSINESSKEY);
            } else {
                workFlowDetails.setType(CommonConstants.ABSTRACT_ESTIMATE_WF_TYPE);
                workFlowDetails.setBusinessKey(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
            }
            workFlowDetails.setStateId(abstractEstimate.getStateId());
            if (abstractEstimate.getStatus() != null)
                workFlowDetails.setStatus(abstractEstimate.getStatus().toString());

            if (null != requestInfo && null != requestInfo.getUserInfo()) {
                workFlowDetails.setSenderName(requestInfo.getUserInfo().getUserName());
            }

            if (abstractEstimate.getStateId() != null) {
                workFlowDetails.setStateId(abstractEstimate.getStateId());
            }
        }
    }

    public AbstractEstimateResponse search(AbstractEstimateSearchContract abstractEstimateSearchContract,
            RequestInfo requestInfo) {
        List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
        final AbstractEstimateResponse response = new AbstractEstimateResponse();
        response.setAbstractEstimates(abstractEstimates);
        response.setResponseInfo(estimateUtils.getResponseInfo(requestInfo));
        return response;
    }

    public String setProjectCode(final AbstractEstimateDetails abstractEstimateDetails, boolean spillOverFlag,
            final RequestInfo requestInfo, final AbstractEstimate abstractEstimate, Boolean isCancelled) {
        Map<String, String> messages = new HashMap<>();
        ProjectCode projectCode = new ProjectCode();
        StringBuilder winCode = new StringBuilder();
        if (spillOverFlag) {
            if (abstractEstimateDetails.getProjectCode() != null
                    && abstractEstimateDetails.getProjectCode().getCode() != null)
                projectCode.setCode(abstractEstimateDetails.getProjectCode().getCode());
            else
                messages.put(Constants.KEY_UNIQUE_WORKIDENTIFICATIONNUMBER,
                        Constants.MESSAGE_UNIQUE_WORKIDENTIFICATIONNUMBER);
        } else {
            String workIdentificationNumber = idGenerationRepository
                    .generateWorkIdentificationNumber(abstractEstimate.getTenantId(), requestInfo);
            // TODO: check idgen to accept values to generate

            final String objectCode = estimateUtils.getBudgetGroup(abstractEstimate.getBudgetGroup(), abstractEstimate.getTenantId(), requestInfo);
            winCode.append(estimateUtils.getCityCode(abstractEstimate.getTenantId(), requestInfo)).append("/")
                    .append(propertiesManager.getWorkIdentificationNumberPrefix()).append("/")
                    .append(abstractEstimate.getFunction().getCode()).append("/")
                    .append(objectCode).append("/").append(workIdentificationNumber);
            projectCode.setCode(winCode.toString());
        }

        if (!messages.isEmpty())
            throw new CustomException(messages);

        projectCode.setName(abstractEstimateDetails.getNameOfWork());
        projectCode.setDescription(abstractEstimateDetails.getNameOfWork());
        projectCode.setTenantId(abstractEstimateDetails.getTenantId());
        projectCode.setActive(!isCancelled);
        ProjectCodeRequest projectCodeRequest = new ProjectCodeRequest();
        projectCodeRequest.setRequestInfo(requestInfo);
        List<ProjectCode> projectCodes = new ArrayList<>();
        projectCodes.add(projectCode);
        projectCodeRequest.setProjectCodes(projectCodes);
        List<ProjectCode> savedCodes = new ArrayList<>();
        if (isCancelled)
            savedCodes = projectCodeService.update(projectCodeRequest);
        else
            savedCodes = projectCodeService.create(projectCodeRequest);
        return savedCodes.get(0).getCode();
    }

}
