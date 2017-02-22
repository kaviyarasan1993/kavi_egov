package org.egov.lams.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.lams.model.Asset;
import org.egov.lams.model.AssetCategory;
import org.egov.lams.model.AssetCategoryResponse;
import org.egov.lams.model.AssetResponse;
import org.egov.lams.model.ResponseInfo;
import org.egov.lams.model.SearchAsset;
import org.egov.lams.web.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetController {

	@Autowired
	AssetService assetService;

	@RequestMapping(value = "/_GET_ASSET_CATEGORY", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public AssetCategoryResponse getAssetCategory() {
		AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
		List<AssetCategory> assetCategories = assetService.getAssetCategories();
		assetCategoryResponse.setAssetCategory(assetCategories);
		assetCategoryResponse.setResponseInfo(
				new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		return assetCategoryResponse;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public AssetResponse getAsset(@ModelAttribute SearchAsset searchAsset) {
		System.out.println("searchAsset1::" + searchAsset);
		List<Asset> assetList = assetService.getAllAsset(searchAsset);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assetList);
		assetResponse.setResposneInfo(
				new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		return assetResponse;

	}

	/*@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public AssetResponse getAsset(@ModelAttribute List<Integer> assetIdList) {
		List<Asset> assetList = assetService.getAssetList(assetIdList);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assetList);
		assetResponse.setResposneInfo(
				new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		return assetResponse;

	}*/

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Asset getAsset(@PathVariable("id") Long id) {

		System.out.println("getAsset" + id);
		return assetService.getAsset(id);
	}

}
