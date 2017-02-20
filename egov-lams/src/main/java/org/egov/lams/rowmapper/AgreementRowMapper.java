package org.egov.lams.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;
import org.springframework.jdbc.core.RowMapper;

public class AgreementRowMapper implements RowMapper<Agreement> {
	@Override
	public Agreement mapRow(ResultSet rs, int rowNum) throws SQLException {
		//TODO : do not use SOP, use loggers
		Agreement agreement=new Agreement();

		// setting id value for allotte object

		Allottee allottee = new Allottee();
		allottee.setId(rs.getLong("allottee"));
		
		/*allottee.setName(rs.getString("allottee_name"));
		allottee.setContactNo(rs.getLong("mobilenumber"));*/
		agreement.setAllottee(allottee);
		
		
		//setting value for the asset object
		
		Asset asset=new Asset();
	//	asset.setCategory(rs.getLong("asset_category"));
		asset.setId(rs.getLong("asset")); 
	/*	asset.setLocality(rs.getLong("locality"));
		asset.setCode(rs.getString("asset_code"));
		asset.setElectionward(rs.getLong("election_ward"));
		asset.setWard(rs.getLong("revenue_ward"));
		asset.setDoorNo(rs.getLong("doorno"));*/
		
		agreement.setAsset(asset);
		//  shopping_complex_no 
	
		
		// setting values for Agreement object
		
		agreement.setId(rs.getLong("id"));
		agreement.setAgreementDate(rs.getDate("agreement_Date"));
		agreement.setAgreementNumber(rs.getString("agreement_no"));
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
		agreement.setRegistrationFee(rs.getDouble("registration_fee"));
		agreement.setRemarks(rs.getString("remarks"));
		agreement.setRent(rs.getDouble("rent"));
		agreement.setRrReadingNo(rs.getString("rr_reading_no"));
		agreement.setSecurityDeposit(rs.getDouble("security_deposit"));
		agreement.setSecurityDepositDate(rs.getDate("security_deposit_date"));
		agreement.setSolvencyCertificateDate(rs.getDate("solvency_certificate_date"));
		agreement.setSolvencyCertificateNo(rs.getString("solvency_certificate_no"));
		String status = (rs.getString("status"));
		agreement.setStatus(StatusEnum.fromValue(status)); 
		agreement.setTinNumber(rs.getString("tin_number"));

		// agreement.setTenantId(rs.get("tenant_id"));
		agreement.setTenderDate(rs.getDate("tender_date"));
		agreement.setTenderNumber(rs.getString("tender_number"));
		agreement.setTradelicenseNumber(rs.getString("trade_license_number"));

		
		
		return agreement;
	}
	

}
