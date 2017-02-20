package org.egov.lams.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lams.model.Asset;
import org.springframework.jdbc.core.RowMapper;

public class AssetRowMapper implements RowMapper<Asset> {

	@Override
	public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
		Asset searchAsset = new Asset();
		searchAsset.setCategory(rs.getLong("assetcategory_id"));
		searchAsset.setCode(rs.getString("code"));
		searchAsset.setName(rs.getString("name"));
		searchAsset.setId(rs.getLong("id"));
		searchAsset.setLocality(rs.getLong("locationdetails_id"));
		// searchAsset.setWard(rs.getLong("ward_id"));
		// searchAsset.setZone(rs.getLong("zone"));
		//searchAsset.setElectionward(rs.getLong("electionward_id"));
		//searchAsset.setDoorNo(rs.getString("doorno"));
		return searchAsset;
	}

}
