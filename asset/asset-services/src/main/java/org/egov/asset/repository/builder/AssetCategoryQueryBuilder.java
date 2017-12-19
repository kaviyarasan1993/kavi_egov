package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.model.AssetCategoryCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetCategoryQueryBuilder {

    private static final String SELECT_BASE_QUERY = "SELECT *" + " FROM egasset_assetcategory assetcategory ";

    public String getQuery(final AssetCategoryCriteria assetCategoryCriteria,
            final List<Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(SELECT_BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, assetCategoryCriteria);
        addOrderByClause(selectQuery, assetCategoryCriteria);
        addPagingClause(selectQuery, preparedStatementValues, assetCategoryCriteria);
        log.info("selectQuery::" + selectQuery);
        log.info("preparedstmt values : " + preparedStatementValues);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final AssetCategoryCriteria assetCategoryCriteria) {

        if (assetCategoryCriteria.getId() == null && assetCategoryCriteria.getName() == null
                && assetCategoryCriteria.getCode() == null && assetCategoryCriteria.getTenantId() == null
                && assetCategoryCriteria.getAssetCategoryType().isEmpty())
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (assetCategoryCriteria.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" assetcategory.tenantId = ?");
            preparedStatementValues.add(assetCategoryCriteria.getTenantId());
        }

        if (assetCategoryCriteria.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.id = ?");
            preparedStatementValues.add(assetCategoryCriteria.getId());
        }

        if (assetCategoryCriteria.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.name ilike ?");
            preparedStatementValues.add("%" + assetCategoryCriteria.getName() + "%");
        }

        if (assetCategoryCriteria.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.code = ?");
            preparedStatementValues.add(assetCategoryCriteria.getCode());
        }

        if (assetCategoryCriteria.getAssetCategoryType() != null
                && assetCategoryCriteria.getAssetCategoryType().size() != 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.assetcategorytype IN ("
                    + getAssetCategoryTypeQuery(assetCategoryCriteria.getAssetCategoryType()));
        }

    }

    /**
     * This method is always called at the beginning of the method so that and
     * is prepended before the field's predicate is handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

    private static String getAssetCategoryTypeQuery(final List<String> categoryTypes) {
        StringBuilder query = null;
        if (categoryTypes.size() >= 1) {
            query = new StringBuilder("'" + categoryTypes.get(0).toString() + "'");
            for (int i = 1; i < categoryTypes.size(); i++)
                query.append(",'" + categoryTypes.get(i) + "'");
        }
        return query.append(")").toString();
    }

    private void addPagingClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final AssetCategoryCriteria assetCategoryCriteria) {

        selectQuery.append(" LIMIT ?");
        preparedStatementValues.add(500); // Set limit to pageSize

        selectQuery.append(" OFFSET ?");
        final long pageNumber = 0; // Default pageNo is zero meaning first page
        preparedStatementValues.add(pageNumber);
    }

    private void addOrderByClause(final StringBuilder selectQuery, final AssetCategoryCriteria assetCategoryCriteria) {
        selectQuery.append(" ORDER BY assetcategory.name");
    }

    public String getInsertQuery() {
        return "INSERT into egasset_assetcategory "
                + "(id,name,code,parentid,assetcategorytype,depreciationmethod,depreciationrate,assetaccount,accumulateddepreciationaccount,"
                + "revaluationreserveaccount,depreciationexpenseaccount,unitofmeasurement,customfields,tenantid,createdby,createddate,"
                + "lastmodifiedby,lastmodifieddate,isassetallow,version)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public String getUpdateQuery() {
        return "UPDATE egasset_assetcategory SET "
                + "parentid=?,assetcategorytype=?,depreciationmethod=?,depreciationrate=?,assetaccount=?,accumulateddepreciationaccount=?,"
                + "revaluationreserveaccount=?,depreciationexpenseaccount=?,unitofmeasurement=?,customfields=?,"
                + "lastmodifiedby=?,lastmodifieddate=?,isassetallow=?,version=?" + "WHERE code=? and tenantid=?";
    }
}
