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

package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.Department;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.ModeOfAcquisitionEnum;
import org.egov.asset.model.enums.StatusEnum;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AssetRowMapper implements RowMapper<Asset> {

	@Override
	public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
		Asset asset = new Asset();
		asset.setId(rs.getLong("assetId"));
		asset.setName(rs.getString("assetname"));
		asset.setCode(rs.getString("assetcode"));
		asset.setAssetDetails(rs.getString("assetDetails"));
		//asset.setTenantId(rs.getString("tenantId"));
		asset.setModeOfAcquisition(ModeOfAcquisitionEnum.fromValue(rs.getString("modeofacquisition"))); 
		asset.setStatus(StatusEnum.fromValue("status"));
		asset.setDescription(rs.getString("description"));
		asset.setDateOfCreation(rs.getDate("dateOfCreation"));
		asset.setRemarks(rs.getString("remarks"));
		asset.setLength(rs.getString("length"));
		asset.setWidth(rs.getString("width"));
		asset.setTotalArea(rs.getString("totalArea"));
		asset.setProperties(rs.getString("properties"));
		
		
		
		
		
		Department department=new Department();
		department.setId(rs.getLong("department"));
		asset.setDepartment(department);
		
		Location location=new Location();
		location.setId(rs.getLong("locationId"));
		location.setBlock(rs.getLong("block"));
		location.setLocality(rs.getLong("locality"));
		location.setDoorNo(rs.getString("doorNo"));
		location.setElectionWard(rs.getString("electionWard"));
		location.setRevenueWard(rs.getString("revenueWard"));
		location.setPinCode(rs.getString("pincode"));
		location.setZone(rs.getString("zone"));
		location.setStreet(rs.getString("street"));
		asset.setLocationDetails(location);
		
		AssetCategory assetCategory=new AssetCategory();
		assetCategory.setId(rs.getLong("assetcategoryId"));
		assetCategory.setAccumulatedDepreciationAccount(rs.getString("accumulatedDepreciationAccount"));
		assetCategory.setAssetAccount(rs.getString("assetAccount"));
		assetCategory.setName(rs.getString("assetCategoryName"));
		assetCategory.setCode(rs.getString("assetcategorycode"));
		assetCategory.setParentId(rs.getString("parentId"));
		assetCategory.setDepreciationExpenseAccount(rs.getString("depreciationExpenseAccount"));
		assetCategory.setDepreciationMethod(rs.getString("depreciationMethod"));
		assetCategory.setAccumulatedDepreciationAccount(rs.getString("accumulatedDepreciationAccount"));
		assetCategory.setRevaluationReserveAccount(rs.getString("revaluationReserveAccount"));
		assetCategory.setUnitOfMeasurement(rs.getString("unitOfMeasurement"));
		assetCategory.setCustomFields(rs.getString("customFields"));
		
		
		asset.setAssetCategory(assetCategory);
		return asset;
	}
}