package org.egov.lams.util;

import java.time.LocalDateTime;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.City;
import org.egov.lams.repository.TenantRepository;
import org.egov.lams.web.contract.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AgreementNumberUtil {
	
	public static final Logger logger = LoggerFactory.getLogger(AgreementNumberUtil.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private TenantRepository tenantRepository;

	public String generateAgrementNumber(String tenantId) {
		//FIXME ulb number should come form request info
		//String ulbNumber = propertiesManager.getUlbNumber();
		City city = tenantRepository.fetchTenantByCode(tenantId);
		String agreementNumberSequence = propertiesManager.getAgreementNumberSequence();
		String lamsPrefix = propertiesManager.getLamsPrefix();
		String sql = "SELECT nextval('" + agreementNumberSequence + "')";

		StringBuilder baseValue = new StringBuilder(lamsPrefix);

		LocalDateTime localDateTime = LocalDateTime.now();
		Integer year = localDateTime.getYear();
		baseValue.append('-' + year.toString().substring(2, 4));
		baseValue.append('-' + city.getCode());
		Long resultSet = null;
		try{
			//replace it with a generic agreement repository which will take query, input object,response object
			resultSet = jdbcTemplate.queryForObject(sql, Long.class);
		}catch (Exception e) {
			logger.info("AcknowledgementNumberService : "+ e.getMessage(),e);
			throw e;
		}
		baseValue.append(String.format("%04d", resultSet));
		System.err.println(baseValue.toString());
		return baseValue.toString().toUpperCase();
	}

}
