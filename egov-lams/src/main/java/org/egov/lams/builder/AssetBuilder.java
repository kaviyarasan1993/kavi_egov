package org.egov.lams.builder;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Asset;
import org.egov.lams.model.SearchAgreementsModel;

public class AssetBuilder {

	public static String getAssetUrl(SearchAgreementsModel searchAsset) {

		StringBuilder assetParams = new StringBuilder();

		if (searchAsset.getAssetCategory() == null && searchAsset.getElectionWard() == null
				&& searchAsset.getRevenueWard() == null && searchAsset.getAsset() == null
				&& searchAsset.getLocality() == null && searchAsset.getAssetCode() == null) {
			throw new RuntimeException("All search criteria for asset details are null");
		}
		boolean isAppendAndClause = false;

		if (searchAsset.getAsset() != null) {
			assetParams.append("Id=" + getIdParams(searchAsset.getAsset()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
		}
		if (searchAsset.getAssetCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("assetCategory=" + searchAsset.getAssetCategory());
		}
		/*
		 * if (searchAsset.getShoppingComplexNo() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, assetParams);
		 * assetParams.append("getShoppingComplexNo="+searchAsset.
		 * getShoppingComplexNo()); }
		 */
		if (searchAsset.getAssetCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("AssetCode=" + searchAsset.getAssetCode());
		}
		if (searchAsset.getLocality() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("locality=" + searchAsset.getLocality());
		}
		if (searchAsset.getRevenueWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("ward=" + searchAsset.getRevenueWard());
		}

		/*
		 * if (searchAsset.getElectionWard() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, assetParams);
		 * assetParams.append("election_Ward="+searchAsset.getElectionWard()); }
		 */
		if (searchAsset.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("tenantId=searchAsset.getTenantId()");
		}
		/*
		 * if (searchAsset.getDoorNo() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, assetParams);
		 * assetParams.append("doorno=?");
		 * preparedStatementValues.add(searchAsset.getDoorNo()); }
		 */
		return assetParams.toString();
	}

	public List<Long> getAssetIdList(List<Asset> assetList) {
		List<Long> idList = new ArrayList<>();
		if (assetList != null) {
			idList.add(assetList.get(0).getId());
			for (Asset asset : assetList) {
				idList.add(asset.getId());
			}
		}
		return idList;
	}

	public List<Long> getAssetIdListByAgreements(List<Agreement> agreementList) {
		List<Long> idList = new ArrayList<>();
		if (agreementList.size() >= 1 && agreementList!=null) {
			for (Agreement agreement : agreementList) 
				idList.add(agreement.getAsset().getId());
		} else
			throw new RuntimeException("the agreement list is null for given criteria");
		return idList;
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append(" &");
		}
		return true;
	}

	private static String getIdParams(List<Long> idList) {
		StringBuilder query = new StringBuilder(Long.toString(idList.get(0)));
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + idList.get(i));
		}
		return query.toString();
	}
}
