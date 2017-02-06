package org.egov.lams.web.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.FetchAgreementsModel;
import org.egov.lams.querybuilder.AgreementQueryBuilder;
import org.egov.lams.rowmapper.AgreementRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AgreementService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Agreement> searchAgreement(FetchAgreementsModel fetchAgreementsModel) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = AgreementQueryBuilder.searchQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		List<Agreement> agreements = null;
		try {
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
			System.out.println("agreement:" + agreements);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("No record found");
		}
		return agreements;
	}

}
