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

package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.SearchAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(AssetQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT *,"
			+ "asset.id AS assetId,loc.id AS locationId,assetcategory.id AS assetcategoryId,"
			+ "asset.name as assetname,asset.code as assetcode,"
			+ "assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode"
			+ " FROM egasset_asset asset "
			+ "INNER JOIN egasset_assetcategory assetcategory "
			+ "ON asset.assetcategory = assetcategory.id "	
			+ "INNER JOIN egasset_location loc "
			+ "ON asset.location = loc.id";

	@SuppressWarnings("rawtypes")
	public String getQuery(SearchAsset searchAsset, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, searchAsset);
		addOrderByClause(selectQuery, searchAsset);
		addPagingClause(selectQuery, preparedStatementValues, searchAsset);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues, SearchAsset searchAsset) {

		if (searchAsset.getId() == null && searchAsset.getName() == null
				&& searchAsset.getCode() == null && searchAsset.getDepartment() == null 
				&& searchAsset.getAssetCategory() == null 
				&& searchAsset.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (searchAsset.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" ASSET.tenantId = ?");
			preparedStatementValues.add(searchAsset.getTenantId());
		}

		if (searchAsset.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.id IN ("+getIdQuery(searchAsset.getId()));
		}

		if (searchAsset.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.name = ?");
			preparedStatementValues.add(searchAsset.getName());
		}

		if (searchAsset.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.code = ?");
			preparedStatementValues.add(searchAsset.getCode());
		}

		if (searchAsset.getDepartment() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.department = ?");
			preparedStatementValues.add(searchAsset.getDepartment());
		}

		if (searchAsset.getAssetCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.assetCategory = ?");
			preparedStatementValues.add(searchAsset.getAssetCategory());
		}
		
		if (searchAsset.getStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ASSET.status = ?");
			preparedStatementValues.add(searchAsset.getStatus());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, SearchAsset searchAsset) {
		String sortBy = (searchAsset.getSortBy() == null ? "id" : searchAsset.getSortBy());
		String sortOrder = (searchAsset.getSortOrder() == null ? "asc" : searchAsset.getSortOrder());
		selectQuery.append(" ORDER BY ASSET." + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues, SearchAsset searchAsset) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
		if (searchAsset.getPageSize() != null)
			pageSize = searchAsset.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (searchAsset.getPageNumber() != null)
			pageNumber = searchAsset.getPageNumber() - 1;
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
		StringBuilder query = null;
		if (idList.size() >= 1) {
			query = new StringBuilder(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + idList.get(i));
			}
		}
		return query.append(")").toString();
	}
}
