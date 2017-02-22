package org.egov.lams.repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.egov.lams.builder.AgreementQueryBuilder;
import org.egov.lams.builder.AgreementsBuilder;
import org.egov.lams.builder.AllotteeBuilder;
import org.egov.lams.builder.AssetBuilder;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.SearchAgreementsModel;
import org.egov.lams.model.wrapper.AssetResponse;
import org.egov.lams.repository.rowmapper.AgreementRowMapper;
import org.egov.lams.web.controller.AgreementController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class AgreementRepository {
	public static final Logger logger = LoggerFactory.getLogger(AgreementController.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	RestTemplate restTemplate;

	private AllotteeBuilder allotteeBuilder = new AllotteeBuilder();

	private AssetBuilder assetBuilder = new AssetBuilder();

	public List<Agreement> findAgreementsByAllotee(SearchAgreementsModel fetchAgreementsModel) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findAgreementsByAllotee");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		List<Asset> assets = null;
		List<Allottee> allottees = null;

		allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
		fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdList(allottees));
		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
		assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
		agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		
		return agreements;
	}

	public List<Agreement> findAgreementsByAsset(SearchAgreementsModel fetchAgreementsModel) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findAgreementsByAsset");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		List<Asset> assets = null;
		List<Allottee> allottees = null;

		assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
		fetchAgreementsModel.setAsset(assetBuilder.getAssetIdList(assets));
		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
		allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
		agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);

		return agreements;
	}

	public List<Agreement> findAgreementsByAgreement(SearchAgreementsModel fetchAgreementsModel) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findAgreementsByAgreement");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		List<Asset> assets = null;
		List<Allottee> allottees = null;

		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
		fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
		assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
		allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
		agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		
		return agreements;
	}

	public List<Agreement> findAgreementsByAgreementAndAllotee(SearchAgreementsModel fetchAgreementsModel) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findAgreementsByAgreementAndAllotee");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		List<Asset> assets = null;
		List<Allottee> allottees = null;

		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
		allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
		fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
		assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
		agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		
		return agreements;
	}

	public List<Agreement> findAgreementsByAgreementAndAsset(SearchAgreementsModel fetchAgreementsModel) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findAgreementsByAgreementAndAsset");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		List<Asset> assets = null;
		List<Allottee> allottees = null;
		
		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
		assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
		agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
		allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
		agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		
		return agreements;
	}
	
	/*
	 * method to return a list of Allottee objects by making an API call to Allottee API 
	 */
	public List<Allottee> getAllottees(String string) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside Allottee API caller");
		/* URI url = null;
		 restTemplate.getForObject(url, Allottee.class);
		try {
			url = new URI(propertiesManager.getAssetServiceHostName() + "?" + string);
			allotteeResponse = restTemplate.getForObject(url, AllotteeResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("check if entered asset API url is correct or the asset service is running");
		}
		if (allotteeResponse.getAllottee() == null || allotteeResponse.getAllottee().size()<=0)
			throw new RuntimeException("No assets found for given criteria");
*/
		return null;
	}
		
	/*
	 * method to return a list of Asset objects by making an API call to Asset API 
	 */
	public List<Asset> getAssets(String string) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside Asset API caller");
		URI url = null;
		AssetResponse assetResponse = null;
		try {
			url = new URI(propertiesManager.getAssetServiceHostName() + "?" + string);
			assetResponse = restTemplate.getForObject(url, AssetResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("check if entered asset API url is correct or the asset service is running");
		}
		if (assetResponse.getAssets() == null || assetResponse.getAssets().size()<=0)
			throw new RuntimeException("No assets found for given criteria");

		return assetResponse.getAssets();
	}
}
