package org.egov.lams.querybuilder;

import java.util.List;

import org.egov.lams.model.SearchAsset;

public class AssetQueryBuilder {

	public static String getSearchQuery(SearchAsset searchAsset, List<Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(
				"SELECT id,assetcategory_id,name,code,locationdetails_id FROM egasset_asset asset where");
		if (searchAsset.getAssetCategory() == null && searchAsset.getAssetCode() == null
				&& searchAsset.getAssetName() == null && searchAsset.getBlock() == null && searchAsset.getWard() == null
				&& searchAsset.getElectionWard() == null && searchAsset.getZone() == null
				&& searchAsset.getLocality() == null && searchAsset.getId() == null) {
			throw new RuntimeException("All search criteria are null");
		}
		boolean isAppendAndClause = false;

		if (searchAsset.getId() != null) {
			selectQuery.append(getIdQuery(searchAsset.getId()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
		}

		if (searchAsset.getAssetCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.assetcategory_id=?");
			preparedStatementValues.add(searchAsset.getAssetCategory());
		}

		if (searchAsset.getShoppingComplexNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.getShopping_Complex_No=?");
			preparedStatementValues.add(searchAsset.getShoppingComplexNo());
		}

		if (searchAsset.getAssetCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.Asset_Code=?");
			preparedStatementValues.add(searchAsset.getAssetCode());
		}

		if (searchAsset.getLocality() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.locality=?");
			preparedStatementValues.add(searchAsset.getLocality());
		}

		if (searchAsset.getWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.ward_id=?");
			preparedStatementValues.add(searchAsset.getWard());
		}

		if (searchAsset.getElectionWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.election_ward=?");
			preparedStatementValues.add(searchAsset.getElectionWard());
		}

		if (searchAsset.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.tenant_id=?");
			preparedStatementValues.add(searchAsset.getTenantId());
		}

		if (searchAsset.getDoorNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.doorno=?");
			preparedStatementValues.add(searchAsset.getDoorNo());
		}

		selectQuery.append(" ORDER BY asset.id");

		System.err.println(selectQuery);
		return selectQuery.toString();

	}

	private static String getIdQuery(List<Integer> idList) {

		StringBuilder query = new StringBuilder(" asset.id in (");

		query.append(idList.get(0));

		for (int i = 1; i < idList.size(); i++) {

			query.append("," + idList.get(i));
		}
		query.append(")");

		return query.toString();
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {

		if (appendAndClauseFlag) {
			queryString.append(" and");
		}
		return true;
	}

	public static String getAssetQuery() {
		String query = "SELECT id,assetcategory_id,name,code,locationdetails_id FROM egasset_asset asset where asset.id=?";
		return query;
	}

}
