package org.egov.lams.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lams.model.Asset;
import org.egov.lams.model.SearchAsset;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class AssetRowMapper implements RowMapper<Asset> {

	@Override
	public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Asset searchAsset=new Asset();
		searchAsset.setCategory(Long.toString(rs.getLong("assetcategory_id")));
		searchAsset.setCode(rs.getString("code"));
		searchAsset.setName(rs.getString("name"));
		searchAsset.setId(rs.getLong("id"));
		searchAsset.setLocality(Long.toString(rs.getLong("locationdetails_id")));
		return searchAsset;
	}

	

}
