package org.egov.lams.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lams.model.AssetCategory;
import org.springframework.jdbc.core.RowMapper;

public class AssetCategoryRowMapper implements RowMapper<AssetCategory>{

	@Override
	public AssetCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		AssetCategory assetCategory=new AssetCategory();
		assetCategory.setId(rs.getLong("id"));
		assetCategory.setName(rs.getString("name"));
		assetCategory.setCode(rs.getString("code"));
		return assetCategory;
	}
}
