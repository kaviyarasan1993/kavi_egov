package org.egov.lams.web.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.egov.lams.builder.AgreementQueryBuilder;
import org.egov.lams.builder.AgreementsBuilder;
import org.egov.lams.builder.AllotteeBuilder;
import org.egov.lams.builder.AssetBuilder;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.SearchAgreementsModel;
import org.egov.lams.model.wrapper.AssetResponse;
import org.egov.lams.rowmapper.AgreementRowMapper;
import org.egov.lams.web.controller.AgreementController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchAgreementService {

	public static final Logger logger = LoggerFactory.getLogger(AgreementController.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	RestTemplate restTemplate;

	private AllotteeBuilder allotteeBuilder = new AllotteeBuilder();

	private AssetBuilder assetBuilder= new AssetBuilder();

	public List<Agreement> searchAgreement(SearchAgreementsModel fetchAgreementsModel) {

		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		List<Asset> assets = null;
		List<Allottee> allottees = null;

		/*
		 * three boolean variables isAgreementNull,isAssetNull and
		 * isAllotteeNull declared to indicate whether criteria arguments for
		 * each of the Agreement,Asset and Allottee objects are given or not.
		 */
		boolean isAgreementNull = (fetchAgreementsModel.getAgreementId() == null
				&& fetchAgreementsModel.getAgreementNumber() == null && fetchAgreementsModel.getStatus() == null
				&& (fetchAgreementsModel.getFromDate() == null && fetchAgreementsModel.getToDate() == null)
				&& fetchAgreementsModel.getTenderNumber() == null && fetchAgreementsModel.getTinNumber() == null
				&& fetchAgreementsModel.getTradelicenseNumber() == null);

		boolean isAllotteeNull = (fetchAgreementsModel.getAllotteeName() == null
				&& fetchAgreementsModel.getMobilenumber() == null);

		boolean isAssetNull = (fetchAgreementsModel.getAssetCategory() == null
				&& fetchAgreementsModel.getShoppingComplexNo() == null && fetchAgreementsModel.getAssetCode() == null
				&& fetchAgreementsModel.getLocality() == null && fetchAgreementsModel.getRevenueWard() == null
				&& fetchAgreementsModel.getElectionWard() == null && fetchAgreementsModel.getTenantId() == null
				&& fetchAgreementsModel.getDoorno() == null);

		/*
		 * if condition when at-least one criteria value is given for all the
		 * three objects
		 */
		if (!isAgreementNull && !isAssetNull && !isAllotteeNull) {

			allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
			fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdList(allottees));
			String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel,
					preparedStatementValues);
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
			fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
			assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
			agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		}
		/*
		 * if condition when both Agreement and Allottee criteria values are
		 * given.
		 */
		else if (!isAgreementNull && isAssetNull && !isAllotteeNull) {

			String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel,
					preparedStatementValues);
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
			fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
			allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
			fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
			assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
			agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		}

		/*
		 * if condition when both Agreement and Asset criteria values are given.
		 */
		else if (!isAgreementNull && !isAssetNull && isAllotteeNull) {
			String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel,
					preparedStatementValues);
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
			fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
			assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
			agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
			fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
			allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
		}
		/*
		 * if condition when only Allottee criteria values are given. 
		 * OR
		 * if condition when both Asset and Allottee criteria values are given.
		 */
		else if ((isAgreementNull && isAssetNull && !isAllotteeNull)
				|| (isAgreementNull && !isAssetNull && !isAllotteeNull)) {

			allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
			fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdList(allottees));
			String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel,
					preparedStatementValues);
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
			fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
			assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
			agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);

		}
		/*
		 * if condition when only Asset criteria values are given.
		 */
		else if (isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info(fetchAgreementsModel + "------the fetch model object");
			assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
			logger.info(assets + "---the assets objects for asset criteria");
			fetchAgreementsModel.setAsset(assetBuilder.getAssetIdList(assets));
			String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel,
					preparedStatementValues);
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
			fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
			allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
			agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
		}

		/*
		 * if condition when criteria for all the three objects is null.
		 * OR
		 * if condition when only Agreement criteria values are given.
		 */
		else if ((isAgreementNull && isAssetNull && isAllotteeNull)
				|| (!isAgreementNull && isAssetNull && isAllotteeNull)) {

			try {
				String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel,
						preparedStatementValues);
				agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
				logger.info(agreements.toString());
				fetchAgreementsModel.setAsset(assetBuilder.getAssetIdListByAgreements(agreements));
				fetchAgreementsModel.setAllottee(allotteeBuilder.getAllotteeIdListByAgreements(agreements));
				assets = getAssets(AssetBuilder.getAssetUrl(fetchAgreementsModel));
				allottees = getAllottees(AllotteeBuilder.getAllotteeUrl(fetchAgreementsModel));
				agreements = AgreementsBuilder.mapAgreements(agreements, allottees, assets);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException("No record found");
			}
		}
		return agreements;
	}
	/*
	 * method to return a list of Allottee objects by making an API call to Allottee API 
	 */
	public List<Allottee> getAllottees(String string) {
		//URI url = null;
		// restTemplate.getForObject(url, Allottee.class);
		return null;
	}
	/*
	 * method to return a list of Asset objects by making an API call to Asset API 
	 */
	public List<Asset> getAssets(String string) {
		URI url = null;
		try {
			url = new URI("http://localhost:8080/asset?" + string);
			logger.info("http://localhost:8080/asset?" + string);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		AssetResponse assetResponse = restTemplate.getForObject(url, AssetResponse.class);
		logger.info("AssetList assetResponse:" + assetResponse);
		return assetResponse.getAssets();
	}
}
