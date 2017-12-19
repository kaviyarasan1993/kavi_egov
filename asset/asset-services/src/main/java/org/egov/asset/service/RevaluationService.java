package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.KafkaTopicName;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.repository.RevaluationRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RevaluationService {

    @Autowired
    private RevaluationRepository revaluationRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private AssetService assetService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private CurrentValueService currentValueService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    public RevaluationResponse createAsync(final RevaluationRequest revaluationRequest, final HttpHeaders headers) {
        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final RequestInfo requestInfo = revaluationRequest.getRequestInfo();
        log.debug("RevaluationService createAsync revaluationRequest:" + revaluationRequest);

        revaluation.setId(assetCommonService.getNextId(Sequence.REVALUATIONSEQUENCE));

        if (revaluation.getAuditDetails() == null)
            revaluation.setAuditDetails(assetCommonService.getAuditDetails(requestInfo));

        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                revaluation.getTenantId()))
            try {
                log.info("Commencing Voucher Generation for Asset Revaluation");
                final String voucherNumber = createVoucherForRevaluation(revaluationRequest, headers);

                if (StringUtils.isNotBlank(voucherNumber))
                    revaluation.setVoucherReference(voucherNumber);
            } catch (final Exception e) {
                throw new RuntimeException("Voucher Generation is failed due to :" + e.getMessage());
            }

        logAwareKafkaTemplate.send(applicationProperties.getCreateAssetRevaluationTopicName(),
                KafkaTopicName.SAVEREVALUATION.toString(), revaluationRequest);

        final List<Revaluation> revaluations = new ArrayList<Revaluation>();
        revaluations.add(revaluation);
        return getRevaluationResponse(revaluations, requestInfo);
    }

    public void create(final RevaluationRequest revaluationRequest) {
        revaluationRepository.create(revaluationRequest);
        saveRevaluationAmountToCurrentAmount(revaluationRequest);
    }

    public void saveRevaluationAmountToCurrentAmount(final RevaluationRequest revaluationRequest) {

        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<AssetCurrentValue>();
        final AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
        assetCurrentValue.setAssetId(revaluation.getAssetId());
        assetCurrentValue.setAssetTranType(TransactionType.REVALUATION);
        assetCurrentValue.setCurrentAmount(revaluation.getValueAfterRevaluation());
        assetCurrentValue.setTenantId(revaluation.getTenantId());
        assetCurrentValues.add(assetCurrentValue);
        final AssetCurrentValueRequest assetCurrentValueRequest = new AssetCurrentValueRequest();
        assetCurrentValueRequest.setRequestInfo(revaluationRequest.getRequestInfo());
        assetCurrentValueRequest.setAssetCurrentValues(assetCurrentValues);
        currentValueService.createCurrentValueAsync(assetCurrentValueRequest);
    }

    public RevaluationResponse search(final RevaluationCriteria revaluationCriteria, final RequestInfo requestInfo) {
        List<Revaluation> revaluations = new ArrayList<Revaluation>();
        try {
            revaluations = revaluationRepository.search(revaluationCriteria);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return getRevaluationResponse(revaluations, requestInfo);
    }

    public String createVoucherForRevaluation(final RevaluationRequest revaluationRequest, final HttpHeaders headers) {
        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final RequestInfo requestInfo = revaluationRequest.getRequestInfo();
        final String tenantId = revaluation.getTenantId();
        final Asset asset = assetService.getAsset(tenantId, revaluation.getAssetId(), requestInfo);
        log.debug("asset for revaluation :: " + asset);

        final AssetCategory assetCategory = asset.getAssetCategory();

        if (revaluation.getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
            log.info("subledger details check for Type of change INCREASED");

            final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, assetCategory.getAssetAccount());
            final List<ChartOfAccountDetailContract> subledgerDetailsForRevaluationReserverAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, assetCategory.getRevaluationReserveAccount());

            voucherService.validateSubLedgerDetails(subledgerDetailsForAssetAccount,
                    subledgerDetailsForRevaluationReserverAccount);

        } else if (revaluation.getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
            log.info("subledger details check for Type of change DECREASED");

            final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, assetCategory.getAssetAccount());
            final List<ChartOfAccountDetailContract> subledgerDetailsForFixedAssetWrittenOffAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, revaluation.getFixedAssetsWrittenOffAccount());

            voucherService.validateSubLedgerDetails(subledgerDetailsForAssetAccount,
                    subledgerDetailsForFixedAssetWrittenOffAccount);

        }

        final List<VoucherAccountCodeDetails> accountCodeDetails = getAccountDetails(revaluation, assetCategory,
                requestInfo);

        log.debug("Voucher Create Account Code Details :: " + accountCodeDetails);
        final VoucherRequest voucherRequest = voucherService.createRevaluationVoucherRequest(revaluation,
                accountCodeDetails, asset.getId(), asset.getDepartment().getId(), headers);
        log.debug("Voucher Request for Revaluation :: " + voucherRequest);

        return voucherService.createVoucher(voucherRequest, tenantId, headers);

    }

    public List<VoucherAccountCodeDetails> getAccountDetails(final Revaluation revaluation,
            final AssetCategory assetCategory, final RequestInfo requestInfo) {
        final List<VoucherAccountCodeDetails> accountCodeDetails = new ArrayList<VoucherAccountCodeDetails>();
        final String tenantId = revaluation.getTenantId();

        final BigDecimal amount = revaluation.getRevaluationAmount();
        if (assetCategory != null && revaluation.getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, assetCategory.getAssetAccount(),
                    amount, false, true));
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId,
                    assetCategory.getRevaluationReserveAccount(), amount, true, false));
        } else if (assetCategory != null && revaluation.getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId,
                    revaluation.getFixedAssetsWrittenOffAccount(), amount, false, true));
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, assetCategory.getAssetAccount(),
                    amount, true, false));

        }
        return accountCodeDetails;
    }

    private RevaluationResponse getRevaluationResponse(final List<Revaluation> revaluations,
            final RequestInfo requestInfo) {
        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        revaluationResponse.setRevaluations(revaluations);
        revaluationResponse.setResposneInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
        return revaluationResponse;
    }

}
