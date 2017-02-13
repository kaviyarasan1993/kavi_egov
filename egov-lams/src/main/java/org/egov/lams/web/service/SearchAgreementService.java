package org.egov.lams.web.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.SearchAgreementsModel;
import org.egov.lams.querybuilder.AgreementQueryBuilder;
import org.egov.lams.rowmapper.AgreementRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SearchAgreementService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Agreement> searchAgreement(SearchAgreementsModel fetchAgreementsModel) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = AgreementQueryBuilder.searchQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		List<Agreement> agreements = null;
		try {
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("No record found");
		}
		return agreements;
	}

}
