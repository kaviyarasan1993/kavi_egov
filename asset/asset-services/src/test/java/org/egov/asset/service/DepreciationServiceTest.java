package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.contract.FinancialYearContract;
import org.egov.asset.contract.FinancialYearContractResponse;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.repository.DepreciationRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class DepreciationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Mock
    private DepreciationRepository depreciationRepository;

    @Mock
    private CurrentValueService currentValueService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private AssetDepreciator assetDepreciator;

    @Mock
    private SequenceGenService sequenceGenService;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private AssetCommonService assetCommonService;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private DepreciationService depreciationService;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private VoucherService voucherService;

    @SuppressWarnings("unchecked")
    @Test
    public void test_Depreciate_Asset() {
        final DepreciationResponse depreciationResponse = getDepreciationReponse();
        final DepreciationRequest depreciationRequest = DepreciationRequest.builder().requestInfo(new RequestInfo())
                .depreciationCriteria(getDepreciationCriteria()).build();
        final HttpHeaders headers = getHttpHeaders();
        final FinancialYearContractResponse financialYearContractResponse = getFinancialYearContractResponse();
        when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(financialYearContractResponse);
        assertEquals(depreciationResponse.toString(),
                depreciationService.depreciateAsset(depreciationRequest, headers).toString());
    }

    @Test
    public void test_ValidationAndGenerationDepreciationVoucher() {
        final Map<Long, DepreciationDetail> depreciationDetailsMap = getDepreciationDetailsMap();
        final HttpHeaders headers = getHttpHeaders();

        final List<CalculationAssetDetails> calculationAssetDetailList = getCalculationAssetDetailList();
        final Map<Long, List<CalculationAssetDetails>> cadMap = getCalculationAssetDetailMap();

        final RequestInfo requestInfo = getRequestInfo();
        when(assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                "ap.kurnool")).thenReturn(true);
        depreciationService.validationAndGenerationDepreciationVoucher(depreciationDetailsMap, headers, requestInfo,
                "ap.kurnool", calculationAssetDetailList, cadMap);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_SetDefaultsInDepreciationCriteria() {
        final DepreciationCriteria depreciationCriteria = getDepreciationCriteria();
        final RequestInfo requestInfo = getRequestInfo();
        final FinancialYearContractResponse financialYearContractResponse = getFinancialYearContractResponse();
        when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(financialYearContractResponse);
        depreciationService.setDefaultsInDepreciationCriteria(depreciationCriteria, requestInfo);
    }

    private FinancialYearContractResponse getFinancialYearContractResponse() {
        final FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
        final List<FinancialYearContract> financialYearContracts = new ArrayList<FinancialYearContract>();
        final FinancialYearContract financialYearContract = new FinancialYearContract();
        financialYearContract.setFinYearRange("2017-18");
        financialYearContract.setActive(true);
        financialYearContract.setStartingDate(java.sql.Date.valueOf(LocalDate.of(2017, 04, 1)));
        financialYearContract.setEndingDate(java.sql.Date.valueOf(LocalDate.of(2018, 03, 31)));
        financialYearContract.setIsActiveForPosting(true);
        financialYearContracts.add(financialYearContract);
        financialYearContractResponse.setFinancialYears(financialYearContracts);
        financialYearContractResponse.setPage(null);
        financialYearContractResponse.setFinancialYear(null);
        financialYearContractResponse.setResponseInfo(null);
        return financialYearContractResponse;
    }

    private RequestInfo getRequestInfo() {
        final RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAction("asd");
        requestInfo.setApiId("org.egov.pgr");
        requestInfo.setVer("1.0");
        return requestInfo;
    }

    private Map<Long, List<CalculationAssetDetails>> getCalculationAssetDetailMap() {
        final Map<Long, List<CalculationAssetDetails>> calculationAssetDetailMap = new HashMap<Long, List<CalculationAssetDetails>>();
        calculationAssetDetailMap.put(Long.valueOf("552"), getCalculationAssetDetailList());
        return calculationAssetDetailMap;
    }

    private List<CalculationAssetDetails> getCalculationAssetDetailList() {
        final List<CalculationAssetDetails> calculationAssetDetails = new ArrayList<CalculationAssetDetails>();
        final CalculationAssetDetails calculationAssetDetail = new CalculationAssetDetails();
        calculationAssetDetail.setAccumulatedDepreciation(BigDecimal.ZERO);
        calculationAssetDetail.setAccumulatedDepreciationAccount(Long.valueOf("1947"));
        calculationAssetDetail.setAssetCategoryDepreciationRate(null);
        calculationAssetDetail.setAssetCategoryId(Long.valueOf("196"));
        calculationAssetDetail.setAssetCategoryName("Kalyana Mandapam");
        calculationAssetDetail.setAssetDepreciationRate(Double.valueOf("16.53"));
        calculationAssetDetail.setAssetId(Long.valueOf("597"));
        calculationAssetDetail.setAssetReference(null);
        calculationAssetDetail.setDepartmentId(Long.valueOf("5"));
        calculationAssetDetail.setDepreciationExpenseAccount(Long.valueOf("1906"));
        calculationAssetDetail.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        calculationAssetDetail.setEnableYearWiseDepreciation(true);
        calculationAssetDetail.setFinancialyear("2017-18");
        calculationAssetDetail.setGrossValue(new BigDecimal("15000"));
        calculationAssetDetail.setYearwisedepreciationrate(Double.valueOf("15"));
        calculationAssetDetails.add(calculationAssetDetail);
        return calculationAssetDetails;
    }

    private Map<Long, DepreciationDetail> getDepreciationDetailsMap() {
        final Map<Long, DepreciationDetail> depreciationDetailsMap = new HashMap<Long, DepreciationDetail>();
        depreciationDetailsMap.put(Long.valueOf("597"), getDepreciationDetail());
        return depreciationDetailsMap;
    }

    private HttpHeaders getHttpHeaders() {
        final List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.ALL);
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.COOKIE, "SESSIONID=123");
        requestHeaders.setPragma("no-cache");
        requestHeaders.setConnection("keep-alive");
        requestHeaders.setCacheControl("no-cache");
        requestHeaders.setAccept(mediaTypes);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

    private DepreciationResponse getDepreciationReponse() {
        final DepreciationResponse depreciationResponse = new DepreciationResponse();
        final DepreciationCriteria depreciationCriteria = getDepreciationCriteria();
        final Depreciation depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
                .tenantId("ap.kurnool").depreciationDetails(new ArrayList<DepreciationDetail>()).build();
        depreciationResponse.setDepreciation(depreciation);
        depreciationResponse.setResponseInfo(null);
        return depreciationResponse;
    }

    private DepreciationCriteria getDepreciationCriteria() {
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        depreciationCriteria.setAssetIds(null);
        depreciationCriteria.setFinancialYear("2017-18");
        depreciationCriteria.setFromDate(null);
        depreciationCriteria.setToDate(null);
        depreciationCriteria.setTenantId("ap.kurnool");
        return depreciationCriteria;
    }

    private DepreciationDetail getDepreciationDetail() {
        final DepreciationDetail depreciationDetail = DepreciationDetail.builder().assetId(Long.valueOf("552"))
                .depreciationRate(Double.valueOf("20")).depreciationValue(new BigDecimal("3200"))
                .status(DepreciationStatus.SUCCESS).reasonForFailure(null)
                .valueBeforeDepreciation(new BigDecimal("16000")).valueAfterDepreciation(new BigDecimal("12800"))
                .voucherReference("1/GJV/00000219/08/2017-18").build();
        return depreciationDetail;
    }

}
