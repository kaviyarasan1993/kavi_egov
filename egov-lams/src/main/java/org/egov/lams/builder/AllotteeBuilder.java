package org.egov.lams.builder;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.SearchAgreementsModel;

public class AllotteeBuilder {

	public static String getAllotteeUrl(SearchAgreementsModel searchAllottee) {

		if (searchAllottee.getAllottee() == null && searchAllottee.getMobilenumber() == null)
			throw new RuntimeException("all asset search fields are null");

		boolean isAppendAndClause = false;
		StringBuilder allotteeParams = new StringBuilder();

		if (searchAllottee.getAllottee() != null) {
			allotteeParams.append("allotteeId=" + getIdQuery(searchAllottee.getAllottee()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, allotteeParams);
		}

		if (searchAllottee.getMobilenumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, allotteeParams);
			allotteeParams.append("mobileNumber=" + searchAllottee.getMobilenumber());
		}
		return allotteeParams.toString();
	}

	public List<Long> getAllotteeIdList(List<Allottee> allottees) {

		List<Long> idList = new ArrayList<>();
		if (allottees != null) {
			idList.add(allottees.get(0).getId());
			for (Allottee allottee : allottees) {
				idList.add(allottee.getId());
			}
		}
		return idList;
	}

	public List<Long> getAllotteeIdListByAgreements(List<Agreement> agreementList) {

		List<Long> idList = new ArrayList<>();
		if (agreementList != null) {
			idList.add(agreementList.get(0).getAllottee().getId());
			for (Agreement agreement : agreementList) {
				idList.add(agreement.getAllottee().getId());
			}
		}
		return idList;
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append(" &");
		}
		return true;
	}
	
	//throw null pointer expection 
	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = new StringBuilder(Long.toString(idList.get(0)));
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + idList.get(i));
		}
		return query.toString();
	}
}
