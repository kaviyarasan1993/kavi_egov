
package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.repository.builder.AssetCategoryQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetCategoryRowMapper;
import org.egov.asset.service.AssetCommonService;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AssetCategoryRowMapper assetCategoryRowMapper;

    @Autowired
    private AssetCategoryQueryBuilder assetCategoryQueryBuilder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AssetCommonService assetCommonService;

    public List<AssetCategory> search(final AssetCategoryCriteria assetCategoryCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = assetCategoryQueryBuilder.getQuery(assetCategoryCriteria, preparedStatementValues);

        List<AssetCategory> assetCategory = new ArrayList<AssetCategory>();
        try {
            assetCategory = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), assetCategoryRowMapper);
        } catch (final Exception exception) {
            log.info("the exception in assetcategory search :" + exception);
        }
        return assetCategory;
    }

    public AssetCategory create(final AssetCategoryRequest assetCategoryRequest) {

        final RequestInfo requestInfo = assetCategoryRequest.getRequestInfo();
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final String queryStr = assetCategoryQueryBuilder.getInsertQuery();

        mapper.setSerializationInclusion(Include.NON_NULL);

        final AssetCategory assetCategory2 = new AssetCategory();
        assetCategory2.setAssetFieldsDefination(assetCategory.getAssetFieldsDefination());

        String customFields = null;
        String assetCategoryType = null;
        String depreciationMethod = null;

        if (assetCategory.getAssetCategoryType() != null)
            assetCategoryType = assetCategory.getAssetCategoryType().toString();

        if (assetCategory.getDepreciationMethod() != null)
            depreciationMethod = assetCategory.getDepreciationMethod().toString();

        try {
            customFields = mapper.writeValueAsString(assetCategory2);
            log.debug("customFields:::" + customFields);
        } catch (final JsonProcessingException e) {
            log.debug("the exception in assetcategory customfileds mapping :" + e);
        }

        final Object[] obj = new Object[] { assetCategory.getId(), assetCategory.getName(), assetCategory.getCode(),
                assetCategory.getParent(), assetCategoryType, depreciationMethod,
                assetCommonService.getDepreciationRate(assetCategory.getDepreciationRate()),
                assetCategory.getAssetAccount(), assetCategory.getAccumulatedDepreciationAccount(),
                assetCategory.getRevaluationReserveAccount(), assetCategory.getDepreciationExpenseAccount(),
                assetCategory.getUnitOfMeasurement(), customFields, assetCategory.getTenantId(),
                requestInfo.getUserInfo().getId(), new Date().getTime(), requestInfo.getUserInfo().getId(),
                new Date().getTime(), assetCategory.getIsAssetAllow(), assetCategory.getVersion() };

        try {
            jdbcTemplate.update(queryStr, obj);
        } catch (final Exception exception) {
            log.debug("the exception in assetcategory insert :" + exception);
        }

        return assetCategory;
    }

    public AssetCategory update(final AssetCategoryRequest assetCategoryRequest) {

        final RequestInfo requestInfo = assetCategoryRequest.getRequestInfo();
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final String queryStr = assetCategoryQueryBuilder.getUpdateQuery();

        mapper.setSerializationInclusion(Include.NON_NULL);

        final AssetCategory assetCategory2 = new AssetCategory();
        assetCategory2.setAssetFieldsDefination(assetCategory.getAssetFieldsDefination());

        String customFields = null;
        String assetCategoryType = null;
        String depreciationMethod = null;

        if (assetCategory.getAssetCategoryType() != null)
            assetCategoryType = assetCategory.getAssetCategoryType().toString();

        if (assetCategory.getDepreciationMethod() != null)
            depreciationMethod = assetCategory.getDepreciationMethod().toString();

        try {
            customFields = mapper.writeValueAsString(assetCategory2);
            log.info("customFields:::" + customFields);
        } catch (final JsonProcessingException e) {
            log.info("the exception in assetcategory customfileds mapping :" + e);
        }

        final Object[] obj = new Object[] { assetCategory.getParent(), assetCategoryType, depreciationMethod,
                assetCommonService.getDepreciationRate(assetCategory.getDepreciationRate()),
                assetCategory.getAssetAccount(), assetCategory.getAccumulatedDepreciationAccount(),
                assetCategory.getRevaluationReserveAccount(), assetCategory.getDepreciationExpenseAccount(),
                assetCategory.getUnitOfMeasurement(), customFields, requestInfo.getUserInfo().getId(),
                new Date().getTime(), assetCategory.getIsAssetAllow(), assetCategory.getVersion(),
                assetCategory.getCode(), assetCategory.getTenantId() };

        try {
            log.info("asset category update query::" + queryStr + "," + Arrays.toString(obj));
            final int i = jdbcTemplate.update(queryStr, obj);
            log.info("output of update asset category query : " + i);
        } catch (final Exception exception) {
            log.info("the exception in assetcategory update :" + exception);
        }

        return assetCategory;
    }
}
