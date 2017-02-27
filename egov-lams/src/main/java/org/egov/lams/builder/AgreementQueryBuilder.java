package org.egov.lams.builder;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.lams.model.SearchAgreementsModel;
import org.egov.lams.web.controller.AgreementController;

public class AgreementQueryBuilder {

	public static final Logger logger = LoggerFactory.getLogger(AgreementQueryBuilder.class);

	@SuppressWarnings("unchecked")
	public static String agreementQueryBuilder(SearchAgreementsModel agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder("select * from eglams_agreement agreement");

		if (!(agreementsModel.getAgreementId() == null && agreementsModel.getAgreementNumber() == null
				&& (agreementsModel.getFromDate() == null && agreementsModel.getToDate() == null)
				&& agreementsModel.getStatus() == null && agreementsModel.getTenderNumber() == null
				&& agreementsModel.getTinNumber() == null && agreementsModel.getTradelicenseNumber() == null))
		{
			selectQuery.append(" where");
			boolean isAppendAndClause = false;

			if (agreementsModel.getAgreementId() != null) {
				selectQuery.append(" agreement.id in (" + getIdQuery(agreementsModel.getAgreementId()));
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			}

			if (agreementsModel.getAgreementNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.agreement_no=?");
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
				selectQuery.append(" agreement.Trade_License_number=?");
				preparedStatementValues.add(agreementsModel.getTradelicenseNumber());
			}

			if (agreementsModel.getAsset() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.asset=?");
				preparedStatementValues.add(agreementsModel.getAsset());
			}

			if (agreementsModel.getAllottee() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" agreement.allottee=?");
				preparedStatementValues.add(agreementsModel.getAllottee());
			}

			if (agreementsModel.getFromDate() != null) {

				if (agreementsModel.getToDate() != null) {
					if (agreementsModel.getToDate().compareTo(agreementsModel.getFromDate()) < 0)
						throw new RuntimeException("ToDate cannot be lesser than fromdate");
					isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
					selectQuery.append(" agreement.agreement_date>=?");
					preparedStatementValues.add(agreementsModel.getFromDate());
					addAndClauseIfRequired(isAppendAndClause, selectQuery);
					selectQuery.append(" agreement.agreement_date<=?");
					preparedStatementValues.add(agreementsModel.getToDate());
				}
			}

		}

		/*
		 * put default date value of one year span, considering current date as toDate
		 * default offset value has to be set
		 */

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
		System.err.println(selectQuery.toString());
		return selectQuery.toString();
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {

		if (appendAndClauseFlag) {
			queryString.append(" and");
		}
		return true;
	}

	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = null;
		if (idList.size() >= 1) {
			query = new StringBuilder(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + idList.get(i));
			}
		}
		return query.append(")").toString();
	}
	
	public static String findRentIncrementTypeQuery() {
		String query = "SELECT * FROM eglams_rentincrementtype rent where rent.id=?";
		return query;
	}
}
