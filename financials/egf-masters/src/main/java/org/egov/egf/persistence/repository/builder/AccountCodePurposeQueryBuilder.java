/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.egf.persistence.repository.builder;

import java.util.List;

import org.egov.egf.config.ApplicationProperties;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountCodePurposeQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(AccountCodePurposeQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT acp.id AS acp_id, acp.name AS acp_name,acp.createdBy AS acp_createdBy,"
			+ " acp.createdDate AS acp_createdDate, acp.lastModifiedBy AS acp_lastModifiedBy,"
			+ " acp.lastModifiedDate AS acp_lastModifiedDate, acp.tenantId AS acp_tenantId "
			+ " FROM egf_accountcodepurpose acp ";

	@SuppressWarnings("rawtypes")
	public String getQuery(AccountCodePurposeGetRequest accountCodePurposeGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, accountCodePurposeGetRequest);
		addOrderByClause(selectQuery, accountCodePurposeGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, accountCodePurposeGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			AccountCodePurposeGetRequest accountCodePurposeGetRequest) {

		if (accountCodePurposeGetRequest.getId() == null && accountCodePurposeGetRequest.getName() == null
				&& accountCodePurposeGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (accountCodePurposeGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" acp.tenantId = ?");
			preparedStatementValues.add(accountCodePurposeGetRequest.getTenantId());
		}

		if (accountCodePurposeGetRequest.getId() != null && !accountCodePurposeGetRequest.getId().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" acp.id IN " + getIdQuery(accountCodePurposeGetRequest.getId()));
		}

		if (accountCodePurposeGetRequest.getName() != null && !accountCodePurposeGetRequest.getName().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" acp.name = ? ");
			preparedStatementValues.add(accountCodePurposeGetRequest.getName());
		}

	}

	private void addOrderByClause(StringBuilder selectQuery,
			AccountCodePurposeGetRequest accountCodePurposeGetRequest) {
		String sortBy = (accountCodePurposeGetRequest.getSortBy() == null ? "acp.name  "
				: accountCodePurposeGetRequest.getSortBy());
		String sortOrder = (accountCodePurposeGetRequest.getSortOrder() == null ? "ASC"
				: accountCodePurposeGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			AccountCodePurposeGetRequest accountCodePurposeGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.egfLeaveSearchPageSizeDefault());
		if (accountCodePurposeGetRequest.getPageSize() != null)
			pageSize = accountCodePurposeGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (accountCodePurposeGetRequest.getPageNumber() != null)
			pageNumber = accountCodePurposeGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 * 
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}

	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append(", " + idList.get(i));
			}
		}
		return query.append(")").toString();
	}
}
