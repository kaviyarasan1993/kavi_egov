package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class AgreementRowMapper implements RowMapper<Agreement> {

	public static final Logger logger = LoggerFactory.getLogger(AgreementRowMapper.class);

	@Override
	public Agreement mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Agreement agreement = new Agreement();
		agreement.setId(rs.getLong("id"));
		Date agreementDate = rs.getDate("agreement_date");
		agreement.setAgreementDate(agreementDate);
		agreement.setAgreementNumber(rs.getString("agreement_no"));
		agreement.setBankGuaranteeAmount(rs.getDouble("bank_guarantee_amount"));
		Date bankGuaranteeDate = rs.getDate("bank_guarantee_date");
		agreement.setBankGuaranteeDate(bankGuaranteeDate);
		agreement.setCaseNo(rs.getString("case_no"));
		Date commencementDate = rs.getDate("commencement_date");
		agreement.setCommencementDate(commencementDate);
		Date councilDate = rs.getDate("council_date");
		agreement.setCouncilDate(councilDate);
		agreement.setCouncilNumber(rs.getString("council_number"));
		Date expiryDate = rs.getDate("expiry_date");
		agreement.setExpiryDate(expiryDate);
		String natureOfAllotment = (rs.getString("nature_of_allotment"));
		agreement.setNatureOfAllotment(NatureOfAllotmentEnum.fromValue(natureOfAllotment));
		Date orderDate = rs.getDate("order_date");
		agreement.setOrderDate(orderDate);
		agreement.setOrderDetails(rs.getString("order_details"));
		agreement.setOrderNo(rs.getString("order_no"));
		String PaymentCycle = (rs.getString("payment_cycle"));
		agreement.setPaymentCycle(PaymentCycleEnum.fromValue(PaymentCycle));
		agreement.setRegistrationFee(rs.getDouble("registration_fee"));
		agreement.setRemarks(rs.getString("remarks"));
		agreement.setRent(rs.getDouble("rent"));
		agreement.setRrReadingNo(rs.getString("rr_reading_no"));
		String status = (rs.getString("status"));
		agreement.setStatus(StatusEnum.fromValue(status));
		agreement.setTinNumber(rs.getString("tin_number"));
		// agreement.setTenantId(rs.getString("tenant_id"));
		Date tenderDate = rs.getDate("tender_date");
		agreement.setTenderDate(tenderDate);
		agreement.setTenderNumber(rs.getString("tender_number"));
		agreement.setSecurityDeposit(rs.getDouble("security_deposit"));
		Date securityDepositDate = rs.getDate("security_deposit_date");
		agreement.setSecurityDepositDate(securityDepositDate);
		Date solvencyCertificateDate = rs.getDate("solvency_certificate_date");
		agreement.setSolvencyCertificateDate(solvencyCertificateDate);
		agreement.setSolvencyCertificateNo(rs.getString("solvency_certificate_no"));
		agreement.setTradelicenseNumber(rs.getString("trade_license_number"));

		Allottee allottee = new Allottee();
		allottee.setId(rs.getLong("allottee"));
		agreement.setAllottee(allottee);

		Asset asset = new Asset();
		asset.setId(rs.getLong("asset"));
		agreement.setAsset(asset);

		return agreement;
	}

}
