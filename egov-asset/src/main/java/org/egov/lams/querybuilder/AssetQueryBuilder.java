package org.egov.lams.querybuilder;

import java.util.List;

import org.egov.lams.model.SearchAsset;

public class AssetQueryBuilder {

	public static String getSearchQuery(SearchAsset searchAsset, List<Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(
				"SELECT asset.id,asset.assetcategory_id,asset.name,asset.code,"
				+ "location.location_id,location.zone_id,location.revenue_ward_id,"
				+ "location.election_ward_id,location.block_id,location.doornumber"
				+ " FROM egasset_asset asset inner join egasset_locationdetails location"
				+ " ON asset.locationdetails_id=location.id where");
		
		if (searchAsset.getAssetCategory() == null && searchAsset.getAssetCode() == null
				&& searchAsset.getAssetName() == null && searchAsset.getBlock() == null
				&& searchAsset.getWard() == null && searchAsset.getElectionWard() == null 
				&& searchAsset.getZone() == null && searchAsset.getLocality() == null
				&& searchAsset.getId() == null) {
			throw new RuntimeException("All search criteria are null");
		}
		
		boolean isAppendAndClause = false;

		if (searchAsset.getId() != null) {
			selectQuery.append(" asset.id in"+getIdQuery(searchAsset.getId()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
		}

		if (searchAsset.getAssetCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.assetcategory_id=?");
			preparedStatementValues.add(searchAsset.getAssetCategory());
		}

		/*if (searchAsset.getShoppingComplexNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.getShopping_Complex_No=?");
			preparedStatementValues.add(searchAsset.getShoppingComplexNo());
		}*/

		if (searchAsset.getAssetCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.Code=?");
			preparedStatementValues.add(searchAsset.getAssetCode());
		}
		
		if (searchAsset.getLocality() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" location.location_id=?");
			preparedStatementValues.add(searchAsset.getLocality());
		}

		if (searchAsset.getWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" location.revenue_ward_id=?");
			preparedStatementValues.add(searchAsset.getWard());
		}

		if (searchAsset.getElectionWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" location.election_ward_id=?");
			preparedStatementValues.add(searchAsset.getElectionWard());
		}

		if (searchAsset.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" asset.tenant_id=?");
			preparedStatementValues.add(searchAsset.getTenantId());
		}

		if (searchAsset.getDoorNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" location.doornumber=?");
			preparedStatementValues.add(searchAsset.getDoorNo());
		}
		if (searchAsset.getBlock() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" location.block_id=?");
			preparedStatementValues.add(searchAsset.getBlock());
		}
		if (searchAsset.getZone() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" location.Zone_id=?");
			preparedStatementValues.add(searchAsset.getZone());
		}
		selectQuery.append(" ORDER BY asset.id");

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
			queryString.append(" and");
		}
		return true;
	}

	public static String getAssetQuery() {
		String query = "SELECT id,assetcategory_id,name,code,locationdetails_id FROM egasset_asset asset where asset.id=?";
		return query;
	}

}
