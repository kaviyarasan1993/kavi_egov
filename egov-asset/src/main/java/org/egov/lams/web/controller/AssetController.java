package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.websocket.server.PathParam;

import org.egov.lams.model.Asset;
import org.egov.lams.model.AssetCategory;
import org.egov.lams.model.AssetCategoryResponse;
import org.egov.lams.model.AssetResponse;
import org.egov.lams.model.ResponseInfo;
import org.egov.lams.model.SearchAsset;
import org.egov.lams.web.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetController {

	@Autowired
	AssetService assetService;
	
	@RequestMapping(value="/_GET_ASSET_CATEGORY",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	public AssetCategoryResponse getAssetCategory(){
		AssetCategoryResponse assetCategoryResponse=new AssetCategoryResponse(); 
		List<AssetCategory> assetCategories=assetService.getAssetCategories();
		assetCategoryResponse.setAssetCategory(assetCategories);
		assetCategoryResponse.setResponseInfo(
					new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		return assetCategoryResponse;
	}
	
	@RequestMapping(method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	public AssetResponse getAsset(@ModelAttribute SearchAsset searchAsset){
		List<Asset> assetList=assetService.getAllAsset(searchAsset);
		System.out.println("searchAsset::"+searchAsset);
		AssetResponse assetResponse=new AssetResponse();
		assetResponse.setAssets(assetList);
		assetResponse.setResposneInfo(
				new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
	    return assetResponse;
		
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Asset getAsset(@PathVariable("id") Long id){
		
		System.out.println("getAsset"+id);
		return assetService.getAsset(id);
	}

}
	
	
	
	
	
	
	

