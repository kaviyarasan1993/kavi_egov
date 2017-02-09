package org.egov.lams.querybuilder;

import java.util.List;

import org.egov.lams.model.SearchAgreementsModel;

public class AgreementQueryBuilder {

	@SuppressWarnings("unchecked")
	public static String searchQueryBuilder(SearchAgreementsModel agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(
				"select * from eglams_agreement agreement inner join  eglams_asset asset on agreement.asset = asset.asset_id");

		// if statement to check the arguments and build the where criteria
		if (!(agreementsModel.getAgreementId() == null && agreementsModel.getAgreementNumber() == null
		        && agreementsModel.getStatus() == null && agreementsModel.getTenantId() == null
				&& (agreementsModel.getFromDate() == null && agreementsModel.getToDate() == null)
				&& agreementsModel.getTenderNumber() == null && agreementsModel.getTinNumber() == null
				&& agreementsModel.getTradelicenseNumber() == null && agreementsModel.getAssetCategory() == null
				&& agreementsModel.getShoppingComplexNo() == null && agreementsModel.getAssetCode() == null
				&& agreementsModel.getLocality() == null && agreementsModel.getRevenueWard() == null
				&& agreementsModel.getElectionWard() == null && agreementsModel.getTenantId() == null
				&& agreementsModel.getDoorno() == null && agreementsModel.getAllotteeName() == null
				&& agreementsModel.getMobilenumber() == null)) {

			selectQuery.append(" where");
			boolean isAppendAndClause = false;

			if (agreementsModel.getAgreementId() != null) {
				selectQuery.append(" agreement.id=?");
				preparedStatementValues.add(agreementsModel.getAgreementId());
				isAppendAndClause = true;
			}

			if (agreementsModel.getAgreementNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.agreement_number=?");
				preparedStatementValues.add(agreementsModel.getAgreementNumber());
			}

			if (agreementsModel.getStatus() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.status=?");
				preparedStatementValues.add(agreementsModel.getStatus().toString());
			}

			if (agreementsModel.getTenantId() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.tenant_id=?");
				preparedStatementValues.add(agreementsModel.getTenantId());
			}

			if (agreementsModel.getTenderNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.tender_number=?");
				preparedStatementValues.add(agreementsModel.getTenderNumber());
			}

			if (agreementsModel.getTinNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.tin_number=?");
				preparedStatementValues.add(agreementsModel.getTinNumber());
			}

			if (agreementsModel.getTradelicenseNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.TradeLicense_number=?");
				preparedStatementValues.add(agreementsModel.getTradelicenseNumber());
			}

			if (agreementsModel.getFromDate() != null) {

				if (agreementsModel.getToDate() != null) {
					isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
					selectQuery.append(" agreement.agreement_date>=?");
					preparedStatementValues.add(agreementsModel.getFromDate());
					addAndClauseIfRequired(isAppendAndClause, selectQuery);
					selectQuery.append(" agreement.agreement_date<=?");
					preparedStatementValues.add(agreementsModel.getToDate());
				}
			}

			// appending asset table details in query

			if (agreementsModel.getAssetCategory() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.asset_category=?");
				preparedStatementValues.add(agreementsModel.getAssetCategory());
			}

			if (agreementsModel.getShoppingComplexNo() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.getShopping_Complex_No=?");
				preparedStatementValues.add(agreementsModel.getShoppingComplexNo());
			}

			if (agreementsModel.getAssetCode() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.Asset_Code=?");
				preparedStatementValues.add(agreementsModel.getAssetCode());
			}

			if (agreementsModel.getLocality() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.locality=?");
				preparedStatementValues.add(agreementsModel.getLocality());
			}

			if (agreementsModel.getRevenueWard() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.revenue_ward=?");
				preparedStatementValues.add(agreementsModel.getRevenueWard());
			}

			if (agreementsModel.getElectionWard() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.election_ward=?");
				preparedStatementValues.add(agreementsModel.getElectionWard());
			}
			if (agreementsModel.getTenantId() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.tenant_id=?");
				preparedStatementValues.add(agreementsModel.getTenantId());
			}

			if (agreementsModel.getDoorno() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.doorno=?");
				preparedStatementValues.add(agreementsModel.getDoorno());
			}

			// for appending allottee details

			if (agreementsModel.getAllotteeName() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.Allottee_Name=?");
				preparedStatementValues.add(agreementsModel.getAllotteeName());
			}
			if (agreementsModel.getMobilenumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" asset.Mobilenumber=?");
				preparedStatementValues.add(agreementsModel.getMobilenumber());
			}

		}
		// if all fields are null directly build query without where condition

		selectQuery.append(" ORDER BY agreement.id");
		selectQuery.append(" LIMIT ?");
		if (agreementsModel.getSize() != null)
			preparedStatementValues.add(Integer.parseInt(agreementsModel.getSize()));
		else
			preparedStatementValues.add(20);

		selectQuery.append(" OFFSET ?");

		if (agreementsModel.getOffSet() != null)
			preparedStatementValues.add(Integer.parseInt(agreementsModel.getOffSet()));
		else
			preparedStatementValues.add(0);
// remove sys err
		System.err.println("The dynamic query is:: " + selectQuery);

		return selectQuery.toString();
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {

		if (appendAndClauseFlag) {
			queryString.append(" and");
		}
		return true;
	}
}
