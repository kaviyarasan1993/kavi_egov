package org.egov.lams.querybuilder;

import java.util.List;

import org.egov.lams.model.SearchAsset;

public class AssetQueryBuilder {
	
	public static String getSearchQuery(SearchAsset searchAsset,List<Object> obj){
		
		StringBuilder selectQuery = new StringBuilder(
				"SELECT id,assetcategory_id,name,code,locationdetails_id FROM egasset_asset asset where");

		// if statement to check the arguments and build the where criteria

		if (searchAsset.getAssetCategory() == null && searchAsset.getAssetCode()== null
				&& searchAsset.getAssetName() == null && searchAsset.getBlock() == null
				&& searchAsset.getWard() == null && searchAsset.getElectionWard() == null
				&& searchAsset.getZone()==null&& searchAsset.getLocality()==null){
			throw new RuntimeException("All search criteria are null");  
		}
		boolean isAllFieldsNull = true;

		if (searchAsset.getAssetCategory() != null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.assetcategory_id=?");
				isAllFieldsNull = false;
				obj.add(searchAsset.getAssetCategory());
			}
		}
		if (searchAsset.getAssetCode() != null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.code=?");
				isAllFieldsNull = false;
			} else
				selectQuery.append(" and asset.code=?");
			obj.add(searchAsset.getAssetCode());
		}
		if (searchAsset.getAssetName() != null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.name=?");
				isAllFieldsNull = false;
			} else
				selectQuery.append(" and asset.name=?");
			obj.add(searchAsset.getAssetName());
		}
		/*if (searchAsset.getBlock() != null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.block=?");
				isAllFieldsNull = false;
			} else
				selectQuery.append(" and asset.block=?");
			obj.add(searchAsset.getBlock());
		}*/
		if (searchAsset.getElectionWard() != null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.ward_id=?");
				isAllFieldsNull = false;
			} else
				selectQuery.append(" and asset.ward_id=?");
			obj.add(searchAsset.getElectionWard());
		}
		if (searchAsset.getLocality()!= null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.locationdetails_id=?");
				isAllFieldsNull = false;
			} else
				selectQuery.append(" and asset.locationdetails_id=?");
			obj.add(new Long(searchAsset.getLocality()));
		}
	/*	if (searchAsset.getWard()!= null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.TradeLicense_number=?");
				isAllFieldsNull = false;
			} else
				selectQuery.append(" and asset.TradeLicense_number=?");
			obj.add(searchAsset.getWard());
		}*/
		/*if (searchAsset.getZone()!= null) {
			if (isAllFieldsNull) {
				selectQuery.append(" asset.TradeLicense_number=?");
				isAllFieldsNull = false;
			} else
				selectQuery.append(" and asset.TradeLicense_number=?");
			obj.add(searchAsset.getZone());
		}*/
		/*if (!isAllFieldsNull) {
			selectQuery.append("ORDER BY e.id ");
			if (searchAsset.getSize() != null){
				selectQuery.append("LIMIT ? ");
				obj.add(Integer.parseInt(searchAsset.getSize()));
				}
			if (searchAsset.getOffSet() != null){
				selectQuery.append("OFFSET ?");
				obj.add(Integer.parseInt(searchAsset.getOffSet()));
			}
		}*/
		System.err.println(selectQuery);
		return selectQuery.toString();
		
	}
	public static String getAssetQuery(){
		String query="SELECT id,assetcategory_id,name,code,locationdetails_id FROM egasset_asset asset where asset.id=?";
		return query;
	}

}
