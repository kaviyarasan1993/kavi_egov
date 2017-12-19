
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

package org.egov.asset.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.Location;
import org.egov.asset.model.YearWiseDepreciation;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.builder.AssetQueryBuilder;
import org.egov.asset.repository.builder.DepreciationReportQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetRowMapper;
import org.egov.asset.repository.rowmapper.YearWiseDepreciationRowMapper;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetMasterService;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AssetRowMapper assetRowMapper;

	@Autowired
	private AssetQueryBuilder assetQueryBuilder;

	@Autowired
	private YearWiseDepreciationRowMapper yearWiseDepreciationRowMapper;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private AssetMasterService assetMasterService;

	@Autowired
	private AssetCommonService assetCommonService;

	@Autowired
	private DepreciationReportQueryBuilder depreciationReportQueryBuilder;

	public List<Asset> findForCriteria(final AssetCriteria assetCriteria) {

		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = assetQueryBuilder.getQuery(assetCriteria, preparedStatementValues);
		List<Asset> assets = new ArrayList<Asset>();
		try {
			log.debug("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			assets = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), assetRowMapper);
			log.debug("Assets From Criteria::" + assets);
		} catch (final Exception ex) {
			log.debug("the exception from findforcriteria : " + ex);
		}
		return assets;
	}

	public List<Asset> findAssetByCode(final String code) {
		final AssetCriteria assetCriteria = new AssetCriteria();
		assetCriteria.setCode(code);
		return findForCriteria(assetCriteria);
	}

	@Transactional
	public Asset create(final AssetRequest assetRequest) {

		log.debug("the asset request in repository create : " + assetRequest);
		final RequestInfo requestInfo = assetRequest.getRequestInfo();
		final Asset asset = assetRequest.getAsset();

		String property = null;
		try {
			mapper.setSerializationInclusion(Include.NON_EMPTY);
			final Asset asset2 = new Asset();
			asset2.setAssetAttributes(asset.getAssetAttributes());
			property = mapper.writeValueAsString(asset2);
		} catch (final JsonProcessingException e) {
			log.info("the exception in insert from parsing attributes : " + e);
		}

		final String query = assetQueryBuilder.getInsertQuery();

		String modeOfAcquisition = null;
		String status = null;

		if (asset.getModeOfAcquisition() != null)
			modeOfAcquisition = asset.getModeOfAcquisition().toString();

		if (asset.getStatus() != null)
			status = asset.getStatus();

		if (asset.getEnableYearWiseDepreciation() != null && asset.getEnableYearWiseDepreciation())
			asset.setDepreciationRate(null);
		
		if (asset.getAssetCategory() != null && asset.getAssetCategory().getAssetCategoryType().equals(AssetCategoryType.MOVABLE)) {
			asset.setSurveyNumber(null);
		    asset.setMarketValue(null);
		}
		
		final Location location = asset.getLocationDetails();

		final Object[] obj = new Object[] { asset.getId(), asset.getAssetCategory().getId(), asset.getName(),
				asset.getCode(), asset.getDepartment().getId(), asset.getAssetDetails(), asset.getDescription(),
				asset.getDateOfCreation(), asset.getRemarks(), asset.getLength(), asset.getWidth(),
				asset.getTotalArea(), modeOfAcquisition, status, asset.getTenantId(), location.getZone(),
				location.getRevenueWard(), location.getStreet(), location.getElectionWard(), location.getDoorNo(),
				location.getPinCode(), location.getLocality(), location.getBlock(), property,
				requestInfo.getUserInfo().getId(), new Date(), requestInfo.getUserInfo().getId(), new Date(),
				asset.getGrossValue(), asset.getAccumulatedDepreciation(), asset.getAssetReference(),
				asset.getVersion(), asset.getEnableYearWiseDepreciation(),
				assetCommonService.getDepreciationRate(asset.getDepreciationRate()),asset.getSurveyNumber(),asset.getMarketValue()};
		try {
			jdbcTemplate.update(query, obj);
		} catch (final Exception ex) {
			log.debug("the exception from insert query : " + ex);
		}
		if (asset.getEnableYearWiseDepreciation() != null && asset.getEnableYearWiseDepreciation())
			saveYearWiseDepreciation(assetRequest, asset.getYearWiseDepreciation());
		return asset;
	}

	public Asset update(final AssetRequest assetRequest) {
		final RequestInfo requestInfo = assetRequest.getRequestInfo();
		final Asset asset = assetRequest.getAsset();

		String property = null;
		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			final Asset asset2 = new Asset();
			asset2.setAssetAttributes(asset.getAssetAttributes());
			property = mapper.writeValueAsString(asset2);

		} catch (final JsonProcessingException e) {
			log.debug("exception from parsing the assetattributes : " + e);
		}

		final String query = assetQueryBuilder.getUpdateQuery();

		log.debug("asset update query::" + query);

		String modeOfAcquisition = null;
		String status = null;

		if (asset.getModeOfAcquisition() != null)
			modeOfAcquisition = asset.getModeOfAcquisition().toString();

		if (asset.getStatus() != null)
			status = asset.getStatus();
		
		if (asset.getAssetCategory() != null && asset.getAssetCategory().getAssetCategoryType().equals(AssetCategoryType.MOVABLE)) {
			asset.setSurveyNumber(null);
		    asset.setMarketValue(null);
		}

		final Location location = asset.getLocationDetails();

		final Object[] obj = new Object[] { asset.getAssetCategory().getId(), asset.getName(),
				asset.getDepartment().getId(), asset.getAssetDetails(), asset.getDescription(), asset.getRemarks(),
				asset.getLength(), asset.getWidth(), asset.getTotalArea(), modeOfAcquisition, status,
				location.getZone(), location.getRevenueWard(), location.getStreet(), location.getElectionWard(),
				location.getDoorNo(), location.getPinCode(), location.getLocality(), location.getBlock(), property,
				requestInfo.getUserInfo().getId(), new Date(), asset.getGrossValue(),
				asset.getAccumulatedDepreciation(), asset.getAssetReference(), asset.getVersion(), asset.getSurveyNumber(),asset.getMarketValue(),
				asset.getCode(), asset.getTenantId()};
		try {
			log.debug("query1::" + query + "," + Arrays.toString(obj));
			final int i = jdbcTemplate.update(query, obj);
			log.debug("output of update query : " + i);
		} catch (final Exception ex) {
			log.debug("the exception from update asset : " + ex);
		}
		final List<AssetStatus> assetStatuses = assetMasterService.getStatuses(AssetStatusObjectName.ASSETMASTER,
				Status.DISPOSED, asset.getTenantId());
		log.debug("assetStatus check for asset update:: " + assetStatuses);
		if (!assetStatuses.isEmpty()) {
			final AssetStatus assetStatus = assetStatuses.get(0);
			if (!assetStatus.getStatusValues().get(0).getCode().equalsIgnoreCase(status)) {
				log.debug("Updating Depreciation Data for asset :: " + asset.getName());
				updateDepreciationData(assetRequest);
			}
		}
		return asset;
	}

	public void updateDepreciationData(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();
		final List<Asset> assets = findAssetByCode(asset.getCode());
		if (assets != null && !assets.isEmpty()) {
			final Asset oldAsset = assets.get(0);
			log.debug("Old Asset :: " + oldAsset);
			final boolean oldAssetEnableYWD = oldAsset.getEnableYearWiseDepreciation();
			final boolean reqAssetEnableYWD = asset.getEnableYearWiseDepreciation();
			if (!oldAssetEnableYWD && reqAssetEnableYWD || oldAssetEnableYWD && reqAssetEnableYWD) {
				log.info("updating enable year wise depreciation :: (false to true) or (true to true)");
				updateYearWiseDepreciationData(assetRequest, oldAsset);
			} else if (oldAssetEnableYWD && !reqAssetEnableYWD || !oldAssetEnableYWD && !reqAssetEnableYWD) {
				log.info("updating enable year wise depreciation :: (true to false) or (false to false)");
				updateAssetDepreciationRate(asset, true);
			}
		} else
			throw new RuntimeException(
					"Asset " + asset.getName() + " is not found for tenantid :: " + asset.getTenantId());
	}

	public void updateYearWiseDepreciationData(final AssetRequest assetRequest, final Asset oldAsset) {
		final Asset asset = assetRequest.getAsset();

		final List<YearWiseDepreciation> reqYearWiseDepreciations = asset.getYearWiseDepreciation();

		final List<YearWiseDepreciation> dbYearWiseDepreciations = getDBYearWiseDepreciations(oldAsset);
		
		log.debug("Year wise Depreciations from DB :: " + dbYearWiseDepreciations);
		log.debug("Year wise Depreciations from request :: " + reqYearWiseDepreciations);

		final List<YearWiseDepreciation> rywds = new ArrayList<>();
		final List<YearWiseDepreciation> uywds = new ArrayList<>();
		final List<YearWiseDepreciation> iywds = new ArrayList<>();

		final List<Long> dbYWDIds = new ArrayList<>();
		for (final YearWiseDepreciation dbYwd : dbYearWiseDepreciations)
			dbYWDIds.add(dbYwd.getId());
		
		log.debug("saved year wise depreciation ids :: " + dbYWDIds);
		
		final Iterator<YearWiseDepreciation> itr = reqYearWiseDepreciations.iterator();
		while (itr.hasNext()) {
			final YearWiseDepreciation reqYwd = itr.next();
			if (dbYWDIds.contains(reqYwd.getId())) {
				uywds.add(reqYwd);
				itr.remove();
				removeFromDBYearWiseDepreciations(dbYearWiseDepreciations, reqYwd);
			} else if (reqYwd.getId() == null)
				iywds.add(reqYwd);
		}

		if (!dbYearWiseDepreciations.isEmpty())
			rywds.addAll(dbYearWiseDepreciations);

		saveYearWiseDepreciation(assetRequest, iywds);
		updateYearWiseDepreciation(assetRequest, uywds);
		removeYearWiseDepreciation(assetRequest, rywds);

		updateAssetDepreciationRate(asset, false);

	}

	public void removeFromDBYearWiseDepreciations(final List<YearWiseDepreciation> dbYearWiseDepreciations,
			final YearWiseDepreciation reqYwd) {
		int i = 0;

		log.debug("dbYearWiseDepreciations :: " + dbYearWiseDepreciations);
		for (final YearWiseDepreciation ywd : dbYearWiseDepreciations)
			if (ywd.getId() == reqYwd.getId())
				break;
			else
				i++;

		log.debug("removable index from dbYearWiseDepreciations:: " + i);
		dbYearWiseDepreciations.remove(i);

	}

	public List<YearWiseDepreciation> getDBYearWiseDepreciations(final Asset oldAsset) {
		final String queryToGetYearWiseDepreciation = AssetQueryBuilder.GETYEARWISEDEPRECIATIONQUERY;
		log.debug("Get Year Wise Depreciation Query :: " + queryToGetYearWiseDepreciation);
		final List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add(oldAsset.getId());
		preparedStatementValues.add(oldAsset.getTenantId());
		log.debug("parameters for searching year wise depreciations :: " + preparedStatementValues);

		final List<YearWiseDepreciation> dbYearWiseDepreciations = jdbcTemplate.query(queryToGetYearWiseDepreciation,
				preparedStatementValues.toArray(), yearWiseDepreciationRowMapper);
		return dbYearWiseDepreciations;
	}

	public void updateAssetDepreciationRate(final Asset asset, final boolean changeDepRateInAsset) {
		String depreciationRateUpdateQuery = null;
		if (changeDepRateInAsset)
			depreciationRateUpdateQuery = AssetQueryBuilder.ASSETINCLUDEDEPRECIATIONRATEUPDATEQUERY;
		else
			depreciationRateUpdateQuery = AssetQueryBuilder.ASSETEXCLUDEDEPRECIATIONRATEUPDATEQUERY;
		log.debug("Asset Depreciation Rate Update Query : " + depreciationRateUpdateQuery);
		final List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add(asset.getEnableYearWiseDepreciation());
		if (changeDepRateInAsset)
			preparedStatementValues.add(assetCommonService.getDepreciationRate(asset.getDepreciationRate()));
		preparedStatementValues.add(asset.getCode());
		preparedStatementValues.add(asset.getTenantId());
		log.debug("Asset Depreciation Rate Update Parameters : " + preparedStatementValues);
		jdbcTemplate.update(depreciationRateUpdateQuery, preparedStatementValues.toArray());
	}

	public void saveYearWiseDepreciation(final AssetRequest assetRequest,
			final List<YearWiseDepreciation> yearWiseDepreciations) {
		final RequestInfo requestInfo = assetRequest.getRequestInfo();
		final Asset asset = assetRequest.getAsset();

		log.debug("Year Wise Details Insert Query ::" + AssetQueryBuilder.BATCHINSERTQUERY);
		log.debug("Year Wise Depreciations for Insert ::" + yearWiseDepreciations);
		jdbcTemplate.batchUpdate(AssetQueryBuilder.BATCHINSERTQUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(final PreparedStatement ps, final int index) throws SQLException {
				final YearWiseDepreciation yearWiseDepreciation = yearWiseDepreciations.get(index);
				ps.setDouble(1, assetCommonService.getDepreciationRate(yearWiseDepreciation.getDepreciationRate()));
				ps.setString(2, yearWiseDepreciation.getFinancialYear());
				ps.setLong(3, asset.getId());
				ps.setObject(4, yearWiseDepreciation.getUsefulLifeInYears());
				ps.setString(5, asset.getTenantId());
				ps.setString(6, requestInfo.getUserInfo().getId().toString());
				ps.setLong(7, new Date().getTime());
				ps.setString(8, requestInfo.getUserInfo().getId().toString());
				ps.setLong(9, new Date().getTime());
			}

			@Override
			public int getBatchSize() {
				return yearWiseDepreciations.size();
			}
		});
	}

	public void updateYearWiseDepreciation(final AssetRequest assetRequest,
			final List<YearWiseDepreciation> yearWiseDepreciations) {
		final RequestInfo requestInfo = assetRequest.getRequestInfo();
		final Asset asset = assetRequest.getAsset();

		log.debug("Year Wise Details Update Query ::" + AssetQueryBuilder.BATCHUPDATEQUERY);
		log.debug("Year Wise Depreciations for Update ::" + yearWiseDepreciations);
		jdbcTemplate.batchUpdate(AssetQueryBuilder.BATCHUPDATEQUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(final PreparedStatement ps, final int index) throws SQLException {
				final YearWiseDepreciation yearWiseDepreciation = yearWiseDepreciations.get(index);
				ps.setDouble(1, assetCommonService.getDepreciationRate(yearWiseDepreciation.getDepreciationRate()));
				ps.setObject(2, yearWiseDepreciation.getUsefulLifeInYears());
				ps.setString(3, requestInfo.getUserInfo().getId().toString());
				ps.setLong(4, new Date().getTime());
				ps.setString(5, requestInfo.getUserInfo().getId().toString());
				ps.setLong(6, new Date().getTime());
				ps.setLong(7, asset.getId());
				ps.setString(8, yearWiseDepreciation.getFinancialYear());
				ps.setString(9, asset.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return yearWiseDepreciations.size();
			}
		});
	}

	public void removeYearWiseDepreciation(final AssetRequest assetRequest,
			final List<YearWiseDepreciation> yearWiseDepreciations) {
		final Asset asset = assetRequest.getAsset();

		log.debug("Year Wise Details Delete Query ::" + AssetQueryBuilder.YEARWISEDEPRECIATIONDELETEQUERY);
		log.debug("Year Wise Depreciations for Delete ::" + yearWiseDepreciations);
		jdbcTemplate.batchUpdate(AssetQueryBuilder.YEARWISEDEPRECIATIONDELETEQUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(final PreparedStatement ps, final int index) throws SQLException {
				final YearWiseDepreciation yearWiseDepreciation = yearWiseDepreciations.get(index);
				ps.setString(1, yearWiseDepreciation.getFinancialYear());
				ps.setLong(2, asset.getId());
				ps.setString(3, asset.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return yearWiseDepreciations.size();
			}
		});

	}

	/*public List<Asset> getDepreciatedAsset(final DepreciationReportCriteria depreciationReportCriteria) {
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
				preparedStatementValues);
		List<Asset> assets = new ArrayList<Asset>();
		try {
			log.debug("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			assets = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), assetRowMapper);
			log.debug("AssetRepository::" + assets);
		} catch (final Exception ex) {
			log.debug("the exception from findforcriteria : " + ex);
		}
		return assets;
	}*/
}