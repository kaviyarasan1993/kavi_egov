package org.egov.lams.web.service;

import java.util.List;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.SearchAgreementsModel;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.web.controller.AgreementController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchAgreementService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementController.class);

	@Autowired
	private AgreementRepository agreementRepository;

	public List<Agreement> searchAgreement(SearchAgreementsModel fetchAgreementsModel) {
		logger.info("inside SearchAgreementService.class searchAgreement method");
		/**
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

		if (!isAgreementNull && !isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findAgreementsByAllotee");
			return agreementRepository.findAgreementsByAllotee(fetchAgreementsModel);
			
		} else if (!isAgreementNull && isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findAgreementsByAllotee");
			return agreementRepository.findAgreementsByAgreementAndAllotee(fetchAgreementsModel);

		} else if (!isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findAgreementsByAgreementAndAsset : both agreement and ");
			return agreementRepository.findAgreementsByAgreementAndAsset(fetchAgreementsModel);

		} else if ((isAgreementNull && isAssetNull && !isAllotteeNull)
				|| (isAgreementNull && !isAssetNull && !isAllotteeNull)) {
			logger.info("agreementRepository.findAgreementsByAllotee : only allottee || allotte and asset");
			return agreementRepository.findAgreementsByAllotee(fetchAgreementsModel);

		} else if (isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findAgreementsByAsset : only asset");
			return agreementRepository.findAgreementsByAsset(fetchAgreementsModel);

		} else if (!isAgreementNull && isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findAgreementsByAgreement : only agreement");
			return agreementRepository.findAgreementsByAgreement(fetchAgreementsModel);
		} else {
			// if no values are given for all the three criteria objects
			// (isAgreementNull && isAssetNull && isAllotteeNull)
			logger.info("agreementRepository.findAgreementsByAgreement : all values null");
			return agreementRepository.findAgreementsByAgreement(fetchAgreementsModel);
		}
	}

}
