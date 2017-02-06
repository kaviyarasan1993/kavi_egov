package org.egov.lams.querybuilder;

import java.util.List;

import org.egov.lams.model.FetchAgreementsModel;

public class AgreementQueryBuilder {

	@SuppressWarnings("unchecked")
	public static String searchQueryBuilder(FetchAgreementsModel agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {
//TODO all alias names should be some meaningful names
		StringBuilder selectQuery = new StringBuilder(
				"select *,e.id as agreementid from eglams_agreement e inner join eglams_rentincrementtype  r on e.rent_increment_method=r.id "
						+ " inner join  eglams_asset ass on e.asset = ass.asset_id");

		// if statement to check the arguments and build the where criteria
		if (!(agreementsModel.getAgreementId() == null && agreementsModel.getAgreementNumber() == null
				&& agreementsModel.getStatus() == null && agreementsModel.getTenantId() == null
				&& (agreementsModel.getFromDate() == null && agreementsModel.getToDate() == null)
				&& agreementsModel.getTenderNumber() == null && agreementsModel.getTinNumber() == null
				&& agreementsModel.getTradelicenseNumber() == null && agreementsModel.getMobilenumber() == null)) {

			selectQuery.append(" where");
			boolean isAppendAndClause = false;
			System.err.println("where append");

			if (agreementsModel.getAgreementId() != null) {
				selectQuery.append(" e.id=?");
				preparedStatementValues.add(agreementsModel.getAgreementId());
				isAppendAndClause = true;
			}

			if (agreementsModel.getAgreementNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" e.agreement_number=?");
				preparedStatementValues.add(agreementsModel.getAgreementNumber());
			}

			if (agreementsModel.getStatus() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" e.status=?");
				preparedStatementValues.add(agreementsModel.getStatus().toString());
			}

			if (agreementsModel.getTenantId() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" e.tenant_id=?");
				preparedStatementValues.add(agreementsModel.getTenantId());
			}

			if (agreementsModel.getTenderNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" e.tender_number=?");
				preparedStatementValues.add(agreementsModel.getTenderNumber());
			}

			if (agreementsModel.getTinNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" e.tin_number=?");
				preparedStatementValues.add(agreementsModel.getTinNumber());
			}

			if (agreementsModel.getTradelicenseNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" e.TradeLicense_number=?");
				preparedStatementValues.add(agreementsModel.getTradelicenseNumber());
			}

			if (agreementsModel.getFromDate() != null) {

				if (agreementsModel.getToDate() != null) {
					isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
					selectQuery.append(" e.agreement_date>=?");
					preparedStatementValues.add(agreementsModel.getFromDate());
					addAndClauseIfRequired(isAppendAndClause, selectQuery);
					selectQuery.append(" e.agreement_date<=?");
					preparedStatementValues.add(agreementsModel.getToDate());
				}
			}

			// appending asset table details in query

			if (agreementsModel.getAssetCategory() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.asset_category=?");
				preparedStatementValues.add(agreementsModel.getAssetCategory());
			}

			if (agreementsModel.getShoppingComplexNo() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.getShopping_Complex_No=?");
				preparedStatementValues.add(agreementsModel.getShoppingComplexNo());
			}

			if (agreementsModel.getAssetCode() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.Asset_Code=?");
				preparedStatementValues.add(agreementsModel.getAssetCode());
			}

			if (agreementsModel.getLocality() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.locality=?");
				preparedStatementValues.add(agreementsModel.getLocality());
			}

			if (agreementsModel.getRevenueWard() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.revenue_ward=?");
				preparedStatementValues.add(agreementsModel.getRevenueWard());
			}

			if (agreementsModel.getElectionWard() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.election_ward=?");
				preparedStatementValues.add(agreementsModel.getElectionWard());
			}

			if (agreementsModel.getTenantId() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.tenant_id=?");
				preparedStatementValues.add(agreementsModel.getTenantId());
			}

			if (agreementsModel.getDoorno() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.doorno=?");
				preparedStatementValues.add(agreementsModel.getDoorno());
			}

			// for appending allottee details

			if (agreementsModel.getAllotteeName() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.Allottee_Name=?");
				preparedStatementValues.add(agreementsModel.getAllotteeName());
			}
			if (agreementsModel.getMobilenumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" ass.Mobilenumber=?");
				preparedStatementValues.add(agreementsModel.getMobilenumber());
			}

		}
		// if all fields are null directly build query without where condition

		selectQuery.append(" ORDER BY e.id");
		selectQuery.append(" LIMIT ?");
		if (agreementsModel.getSize() != null) 
			preparedStatementValues.add(Integer.parseInt(agreementsModel.getSize()));
		else preparedStatementValues.add(20);

		selectQuery.append(" OFFSET ?");

		if (agreementsModel.getOffSet() != null)
			preparedStatementValues.add(Integer.parseInt(agreementsModel.getOffSet()));
		else
			preparedStatementValues.add(0);
		/*
		 * remove System.err.println when code is completed fully
		 */

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
