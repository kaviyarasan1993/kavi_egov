package org.egov.lams.web.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Asset;
import org.egov.lams.model.AssetCategory;
import org.egov.lams.model.SearchAsset;
import org.egov.lams.querybuilder.AssetQueryBuilder;
import org.egov.lams.rowmapper.AssetCategoryRowMapper;
import org.egov.lams.rowmapper.AssetRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<AssetCategory> getAssetCategories(){
		String query="SELECT id,assetcategory_id,code,name FROM egasset_asset_category";
		List<AssetCategory> assetList=null;
		try{
			assetList=jdbcTemplate.query(query,new AssetCategoryRowMapper());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("assetList:"+assetList);
		
		return assetList;
	}
	
	
	public List<Asset> getAllAsset(SearchAsset searchAsset){
		List<Object> obj=new ArrayList<Object>();
		String query=AssetQueryBuilder.getSearchQuery(searchAsset, obj);
		List<Asset> assetList=null;
		try{
			System.out.println("query::>>"+query+"obj>>"+obj);
			assetList=jdbcTemplate.query(query,obj.toArray(),new AssetRowMapper());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("assetList:"+assetList);
		
		return assetList;
	}
	
	public Asset getAsset(Long id){
		
		String query=AssetQueryBuilder.getAssetQuery();
		Object []obj=new Object[]{ id };
		Asset asset=null;
		try{
			System.out.println("query::>>"+query+"obj>>"+obj);
			 asset = (Asset)jdbcTemplate.queryForObject(query, obj, new AssetRowMapper());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("assetList:"+asset);
		
		return asset;
	}
}

