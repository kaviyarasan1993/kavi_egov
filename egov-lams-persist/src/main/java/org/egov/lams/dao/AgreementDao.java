package org.egov.lams.dao;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;
import org.egov.lams.querybuilder.AgreementQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
/***********************************************************************************************************************************/
@Repository
public class AgreementDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementDao.class);
	
	public void saveAgreement(Agreement agreement){
		
		LOGGER.info("AgreementDao agreement::"+agreement);
		
		String agreementinsert =AgreementQueryBuilder.insertAgreementQuery();
		
		Object[] obj = new Object[] { agreement.getAgreementDate(),agreement.getAgreementNumber(),agreement.getBankGuaranteeAmount(), 
				agreement.getBankGuaranteeDate(),agreement.getCaseNo(), agreement.getCommencementDate(),agreement.getCouncilDate(),
				agreement.getCouncilNumber(), agreement.getExpiryDate(), NatureOfAllotmentEnum.AUCTION.toString(), agreement.getOrderDate(), 
				agreement.getOrderDetails(), agreement.getOrderNo(),PaymentCycleEnum.ANNUAL.toString(),
				agreement.getRegistrationFee(), agreement.getRemarks(), agreement.getRent(), agreement.getRrReadingNo(), 
				agreement.getSecurityDeposit(), agreement.getSecurityDepositDate(), agreement.getSolvencyCertificateDate(),
				agreement.getSolvencyCertificateNo(),StatusEnum.EVICTED.toString(),agreement.getTinNumber(),agreement.getTenderDate(),
				agreement.getTenderNumber(), agreement.getTradelicenseNumber(), agreement.getAllottee().getId(), 
				agreement.getAsset().getId(),agreement.getRentIncrementMethod().getId()};
		
		try{
			jdbcTemplate.update(agreementinsert, obj);
		}catch(DataAccessException ex){
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}
}
