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

package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.YearWiseDepreciation;
import org.egov.asset.model.enums.KafkaTopicName;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetService {

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private AssetCommonService assetCommonService;

	public AssetResponse getAssets(final AssetCriteria searchAsset, final RequestInfo requestInfo) {
		log.info("AssetService getAssets");
		final List<Asset> assets = assetRepository.findForCriteria(searchAsset);
		return getAssetResponse(assets, requestInfo);
	}

	public AssetResponse create(final AssetRequest assetRequest) {
		final Asset asset = assetRepository.create(assetRequest);
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse createAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();

		asset.setCode(assetCommonService.getCode("%06d", Sequence.ASSETCODESEQUENCE));

		asset.setId(assetCommonService.getNextId(Sequence.ASSETSEQUENCE));

		setDepriciationRateAndEnableYearWiseDepreciation(asset);

		log.debug("assetRequest createAsync::" + assetRequest);

		logAwareKafkaTemplate.send(applicationProperties.getCreateAssetTopicName(), KafkaTopicName.SAVEASSET.toString(),
				assetRequest);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse update(final AssetRequest assetRequest) {

		final Asset asset = assetRepository.update(assetRequest);
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse updateAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();
		setDepriciationRateAndEnableYearWiseDepreciation(asset);

		log.debug("assetRequest updateAsync::" + assetRequest);

		logAwareKafkaTemplate.send(applicationProperties.getUpdateAssetTopicName(),
				KafkaTopicName.UPDATEASSET.toString(), assetRequest);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	private AssetResponse getAssetResponse(final List<Asset> assets, final RequestInfo requestInfo) {
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
		return assetResponse;
	}

	private void setDepriciationRateAndEnableYearWiseDepreciation(final Asset asset) {
		final List<YearWiseDepreciation> yearWiseDepreciation = asset.getYearWiseDepreciation();

		log.debug("Year wise depreciations from Request :: " + yearWiseDepreciation);

		final Boolean enableYearWiseDepreciation = asset.getEnableYearWiseDepreciation();

		log.debug("Enable year wise depreciaition from Request :: " + enableYearWiseDepreciation);

		log.debug("Asset ID from Request :: " + asset.getId());
		if (enableYearWiseDepreciation != null && enableYearWiseDepreciation && yearWiseDepreciation != null
				&& !yearWiseDepreciation.isEmpty())
			for (final YearWiseDepreciation depreciationRate : yearWiseDepreciation)
				depreciationRate.setAssetId(asset.getId());
		else if (enableYearWiseDepreciation != null && !enableYearWiseDepreciation) {
			asset.setEnableYearWiseDepreciation(false);
			final Double depreciationRate = assetCommonService.getDepreciationRate(asset.getDepreciationRate());

			log.debug("Depreciation rate for asset create :: " + depreciationRate);
			asset.setDepreciationRate(depreciationRate);
		}
	}

	public Asset getAsset(final String tenantId, final Long assetId, final RequestInfo requestInfo) {
		final List<Long> assetIds = new ArrayList<>();
		assetIds.add(assetId);
		final AssetCriteria assetCriteria = AssetCriteria.builder().tenantId(tenantId).id(assetIds).build();
		final List<Asset> assets = getAssets(assetCriteria, requestInfo).getAssets();
		if (assets != null && !assets.isEmpty())
			return assets.get(0);
		else
			throw new RuntimeException(
					"There is no asset exists for id ::" + assetId + " for tenant id :: " + tenantId);
	}

	/*public AssetResponse getDepreciationReport(final RequestInfo requestInfo,
			final DepreciationReportCriteria depreciationReportCriteria) {
		final List<Asset> assets = assetRepository.getDepreciatedAsset(depreciationReportCriteria);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
		return assetResponse;
	}*/
}