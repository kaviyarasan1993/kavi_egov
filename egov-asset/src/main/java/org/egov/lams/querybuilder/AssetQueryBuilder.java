package org.egov.lams.querybuilder;

import java.util.List;

import org.egov.lams.model.SearchAsset;

public class AssetQueryBuilder {

	public static String getSearchQuery(SearchAsset searchAsset, List<Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(
				"SELECT ASSET.ID,ASSET.ASSETCATEGORY_ID,ASSET.NAME,ASSET.CODE,"
				+ "LOCATION.LOCATION_ID,LOCATION.ZONE_ID,LOCATION.REVENUE_WARD_ID,"
				+ "LOCATION.ELECTION_WARD_ID,LOCATION.BLOCK_ID,LOCATION.DOORNUMBER"
				+ " FROM EGASSET_ASSET ASSET INNER JOIN EGASSET_LOCATIONDETAILS LOCATION"
				+ " ON ASSET.LOCATIONDETAILS_ID=LOCATION.ID WHERE");
		
		if (searchAsset.getAssetCategory() == null && searchAsset.getAssetCode() == null
				&& searchAsset.getAssetName() == null && searchAsset.getBlock() == null
				&& searchAsset.getWard() == null && searchAsset.getElectionWard() == null 
				&& searchAsset.getZone() == null && searchAsset.getLocality() == null
				&& searchAsset.getId() == null) {
			throw new RuntimeException("All search criteria are null");
		}
		
		boolean isAppendAndClause = false;

		if (searchAsset.getId() != null) {
			selectQuery.append(" ASSET.ID IN"+getIdQuery(searchAsset.getId()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
		}

		if (searchAsset.getAssetCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.ASSETCATEGORY_ID=?");
			preparedStatementValues.add(searchAsset.getAssetCategory());
		}

		/*if (searchAsset.getShoppingComplexNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.getShopping_Complex_No=?");
			preparedStatementValues.add(searchAsset.getShoppingComplexNo());
		}*/

		if (searchAsset.getAssetCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.CODE=?");
			preparedStatementValues.add(searchAsset.getAssetCode());
		}
		
		if (searchAsset.getLocality() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" LOCATION.LOCATION_ID=?");
			preparedStatementValues.add(searchAsset.getLocality());
		}

		if (searchAsset.getWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" LOCATION.REVENUE_WARD_ID=?");
			preparedStatementValues.add(searchAsset.getWard());
		}

		if (searchAsset.getElectionWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" LOCATION.ELECTION_WARD_ID=?");
			preparedStatementValues.add(searchAsset.getElectionWard());
		}

		if (searchAsset.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.TENANT_ID=?");
			preparedStatementValues.add(searchAsset.getTenantId());
		}

		if (searchAsset.getDoorNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" LOCATION.DOORNUMBER=?");
			preparedStatementValues.add(searchAsset.getDoorNo());
		}
		if (searchAsset.getBlock() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" LOCATION.BLOCK_ID=?");
			preparedStatementValues.add(searchAsset.getBlock());
		}
		if (searchAsset.getZone() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" LOCATION.ZONE_ID=?");
			preparedStatementValues.add(searchAsset.getZone());
		}
		selectQuery.append(" ORDER BY ASSET.ID");

		System.err.println(selectQuery);
		return selectQuery.toString();

	}

	private static String getIdQuery(List<Integer> idList) {

		StringBuilder query = new StringBuilder(" (");
		query.append(idList.get(0));
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + idList.get(i));
		}
		query.append(")");
		return query.toString();
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append(" AND");
		}
		return true;
	}

	public static String getAssetQuery() {
		String query = "SELECT ASSET.ID,ASSET.ASSETCATEGORY_ID,ASSET.NAME,ASSET.CODE,ASSET.LOCATIONDETAILS_ID FROM EGASSET_ASSET ASSET WHERE ASSET.ID=?";
		return query;
	}

}
