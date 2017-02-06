package org.egov.lams.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;
import org.springframework.jdbc.core.RowMapper;

public class AgreementRowMapper implements RowMapper<Agreement> {
//TODO parameter names should be meaningful.
	@Override
	public Agreement mapRow(ResultSet rs, int arg1) throws SQLException {
		//TODO : do not use SOP, use loggers
		Agreement agreement=new Agreement();
		RentIncrementType rentIncrementMethod = new RentIncrementType();

		// setting id value for allotte object

		Allottee allottee = new Allottee();
		allottee.setId(rs.getLong("allottee"));
		allottee.setName(rs.getString("allottee_name"));
		allottee.setContactNo(rs.getLong("mobilenumber"));
		agreement.setAllottee(allottee);
		
		
		//setting value for the asset object
		
		Asset asset=new Asset();
		asset.setCategory(rs.getString("asset_category"));
		asset.setId(rs.getLong("asset_id")); 
		asset.setLocality(rs.getString("locality"));
		asset.setCode(rs.getString("asset_code"));
		asset.setElectionward(rs.getString("election_ward"));
		agreement.setAsset(asset);
		
		
	/*	these variables are not present in asset model 
	 * shopping_complex_no 
	   revenue_ward
	   tenant_id
	   doorno 
*/
		// setting values for RentIncrementType object.
		rentIncrementMethod.setAssetCategory(rs.getString("asset_category"));
		rentIncrementMethod.setFlatAmount(rs.getString("flat_amount"));
		rentIncrementMethod.setFromDate(rs.getDate("FromDate"));
		rentIncrementMethod.setId(rs.getLong("rent_increment_method"));
		rentIncrementMethod.setPercentage(rs.getString("percentage"));
		rentIncrementMethod.setToDate(rs.getDate("toDate"));
		rentIncrementMethod.setType(rs.getString("type"));

		// setting values for Agreement object
		
		agreement.setId(rs.getLong("id"));
		agreement.setAgreementDate(rs.getDate("agreement_Date"));
		agreement.setAgreementNumber(rs.getString("agreement_number"));
		agreement.setBankGuaranteeAmount(rs.getDouble("bank_guarantee_amount"));
		agreement.setBankGuaranteeDate(rs.getDate("bank_guarantee_date"));
		agreement.setCaseNo(rs.getString("case_no"));
		agreement.setCommencementDate(rs.getDate("commencement_date"));
		agreement.setCouncilDate(rs.getDate("council_date"));
		agreement.setCouncilNumber(rs.getString("council_number"));
		agreement.setExpiryDate(rs.getDate("expiry_date"));
		
//to uppercase for case safety since we dont have UI dropdown
	
		String natureOfAllotment = (rs.getString("nature_of_allotment"));
		agreement.setNatureOfAllotment(NatureOfAllotmentEnum.fromValue(natureOfAllotment)); //throws RuntimeExcepton("enum key not found")			
		agreement.setOrderDate(rs.getDate("order_date"));
		agreement.setOrderDetails(rs.getString("order_details"));
		agreement.setOrderNo(rs.getString("order_no"));
		String PaymentCycle = (rs.getString("payment_cycle"));
		agreement.setPaymentCycle(PaymentCycleEnum.fromValue(PaymentCycle)); //throws RuntimeExcepton("enum key not found")
		agreement.setRegistrationFree(rs.getDouble("registration_free"));
		agreement.setRemarks(rs.getString("remarks"));
		agreement.setRent(rs.getDouble("rent"));
		agreement.setRrReadingNo(rs.getString("rr_reading_no"));
		agreement.setSecurityDeposit(rs.getDouble("security_deposit"));
		agreement.setSecurityDepositDate(rs.getDate("security_deposit_date"));
		agreement.setSolvencyCertificateDate(rs.getDate("solvency_certificate_date"));
		agreement.setSolvencyCertificateNo(rs.getString("solvency_certificate_no"));
		String status = (rs.getString("status"));
		agreement.setStatus(StatusEnum.fromValue(status)); //throws RuntimeExcepton("enum key not found")
		
		agreement.setTradelicenseNumber(rs.getString("tin_number")); 

		// agreement.setTenantId(rs.get("tenant_id"));
		agreement.setTenderDate(rs.getDate("tender_date"));
		agreement.setTenderNumber(rs.getString("tender_number"));
		agreement.setTradelicenseNumber(rs.getString("tradelicense_number"));

		agreement.setRentIncrementMethod(rentIncrementMethod);
		
		
		return agreement;
	}
	

}
