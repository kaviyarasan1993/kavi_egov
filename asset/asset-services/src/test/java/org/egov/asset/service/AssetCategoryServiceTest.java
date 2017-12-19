package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.repository.AssetCategoryRepository;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AssetCategoryServiceTest {

    @Mock
    private AssetCategoryRepository assetCategoryRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private AssetCategoryService assetCategoryService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private AssetCommonService assetCommonService;

    @Test
    public void testSearch() {
        final List<AssetCategory> assetCategories = new ArrayList<>();
        assetCategories.add(getAssetCategory());

        when(assetCategoryRepository.search(any(AssetCategoryCriteria.class))).thenReturn(assetCategories);

        assertTrue(assetCategories.equals(assetCategoryService.search(any(AssetCategoryCriteria.class))));
    }

    @Test
    public void testCreate() {
        AssetCategoryResponse assetCategoryResponse = null;
        try {
            assetCategoryResponse = getAssetCategoryResponse("assetcategorycreateresponse.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
        final AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
        assetCategoryRequest.setRequestInfo(null);
        assetCategoryRequest.setAssetCategory(getAssetCategory());

        when(assetCategoryRepository.create(any(AssetCategoryRequest.class))).thenReturn(getAssetCategory());

        assetCategoryService.create(Matchers.any(AssetCategoryRequest.class));

        assertEquals(assetCategoryResponse.getAssetCategory().get(0).toString(),
                assetCategoryRequest.getAssetCategory().toString());
    }

    @Test
    public void testCreateAsync() {

        final AssetCategory assetCategory = getAssetCategory();
        final AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
        assetCategoryRequest.setAssetCategory(assetCategory);

        final List<AssetCategory> assetCategories = new ArrayList<>();
        assetCategories.add(assetCategory);
        final AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
        assetCategoryResponse.setResponseInfo(
                responseInfoFactory.createResponseInfoFromRequestHeaders(assetCategoryRequest.getRequestInfo()));
        assetCategoryResponse.setAssetCategory(assetCategories);

        assertTrue(assetCategoryResponse.equals(assetCategoryService.createAsync(assetCategoryRequest)));
    }

    @Test
    public void testUpdate() {
        AssetCategoryResponse assetCategoryResponse = null;
        try {
            assetCategoryResponse = getAssetCategoryResponse("assetcategoryupdateresponseservice.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
        final AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
        assetCategoryRequest.setRequestInfo(null);
        assetCategoryRequest.setAssetCategory(getAssetCategory());

        when(assetCategoryRepository.update(any(AssetCategoryRequest.class))).thenReturn(getAssetCategory());

        assetCategoryService.update(Matchers.any(AssetCategoryRequest.class));

        assertEquals(assetCategoryResponse.getAssetCategory().get(0).toString(),
                assetCategoryRequest.getAssetCategory().toString());
    }

    @Test
    public void testUpdateAsync() {

        final AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
        assetCategoryRequest.setAssetCategory(getAssetCategoryForUpdateAsync());

        final List<AssetCategoryRequest> insertedAssetCategoryRequest = new ArrayList<>();
        insertedAssetCategoryRequest.add(assetCategoryRequest);

        AssetCategoryResponse assetCategoryResponse = null;
        try {
            assetCategoryResponse = getAssetCategoryResponse("assetcategoryservice.assetcategory1.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
        when(applicationProperties.getUpdateAssetCategoryTopicName()).thenReturn("kafka.topics.update.disposal");
        when(assetCommonService.getCode(any(String.class), any(Sequence.class))).thenReturn("15");
        assertTrue(assetCategoryResponse.getAssetCategory().get(0).getId().equals(Long.valueOf("15")));

        assetCategoryService.updateAsync(assetCategoryRequest);

        assertEquals(assetCategoryResponse.getAssetCategory().get(0).toString(),
                assetCategoryRequest.getAssetCategory().toString());
    }

    private AssetCategory getAssetCategory() {

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setTenantId("ap.kurnool");
        assetCategory.setId(null);
        assetCategory.setName("asset3");
        assetCategory.setCode(null);
        assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
        assetCategory.setParent(Long.valueOf("2"));
        assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        assetCategory.setIsAssetAllow(true);
        assetCategory.setAssetAccount(2l);
        assetCategory.setAccumulatedDepreciationAccount(Long.valueOf("1"));
        assetCategory.setRevaluationReserveAccount(Long.valueOf("5"));
        assetCategory.setDepreciationExpenseAccount(Long.valueOf("3"));
        assetCategory.setUnitOfMeasurement(Long.valueOf("10"));
        assetCategory.setVersion("v1");
        assetCategory.setDepreciationRate(null);

        return assetCategory;
    }

    private AssetCategory getAssetCategoryForUpdateAsync() {

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setTenantId("ap.kurnool");
        assetCategory.setId(Long.valueOf("15"));
        assetCategory.setName("asset3");
        assetCategory.setCode("15");
        assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
        assetCategory.setParent(Long.valueOf("2"));
        assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        assetCategory.setIsAssetAllow(true);
        assetCategory.setAssetAccount(2l);
        assetCategory.setAccumulatedDepreciationAccount(Long.valueOf("1"));
        assetCategory.setRevaluationReserveAccount(Long.valueOf("5"));
        assetCategory.setDepreciationExpenseAccount(Long.valueOf("3"));
        assetCategory.setUnitOfMeasurement(Long.valueOf("10"));
        assetCategory.setVersion("v1");
        assetCategory.setDepreciationRate(Double.valueOf("0.0"));

        return assetCategory;
    }

    private AssetCategoryResponse getAssetCategoryResponse(final String filePath) throws IOException {
        final String assetJson = new FileUtils().getFileContents(filePath);
        return new ObjectMapper().readValue(assetJson, AssetCategoryResponse.class);
    }

}
