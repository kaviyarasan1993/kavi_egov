package org.egov.lams.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.DemandReason;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.helper.DemandHelper;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandReasonResponse;
import org.egov.lams.web.contract.DemandRequest;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.UserErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class DemandRepository {

	private static final Logger LOGGER = Logger.getLogger(DemandRepository.class);

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	DemandHelper demandHelper;
	
	

	public List<DemandReason> getDemandReason(AgreementRequest agreementRequest) {

		List<DemandReason> demandReasons = new ArrayList<>();
		Agreement agreement = agreementRequest.getAgreement();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(agreement.getCommencementDate());
		
		if (agreement.getPaymentCycle().equals(PaymentCycle.MONTH))
			calendar.add(Calendar.MONTH, 1);
		else if (agreement.getPaymentCycle().equals(PaymentCycle.QUARTER))
			calendar.add(Calendar.MONTH, 3);
		else if (agreement.getPaymentCycle().equals(PaymentCycle.HALFYEAR))
			calendar.add(Calendar.MONTH, 6);
		else
			calendar.add(Calendar.YEAR, 1);
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		String taxReason = null;
		LOGGER.info("month plus start date is : " + date);
		if (agreement.getSource().equals(Source.DATA_ENTRY)) {
			taxReason = propertiesManager.getTaxReasonAdvanceTax();
			demandReasons.addAll(getDemandReasonsForTaxReason(agreementRequest, date, taxReason));
			taxReason = propertiesManager.getTaxReasonGoodWillAmount();
			demandReasons.addAll(getDemandReasonsForTaxReason(agreementRequest, date, taxReason));
			return demandReasons;
		}
		for (int i = 0; i < 3; i++) {
			if (i == 0 && agreement.getSource().equals(Source.SYSTEM) && agreement.getStatus().equals(Status.WORKFLOW)) {
				taxReason = propertiesManager.getTaxReasonAdvanceTax();
				demandReasons.addAll(getDemandReasonsForTaxReason(agreementRequest, date, taxReason));
			} else if (i == 1 && agreement.getSource().equals(Source.SYSTEM) && agreement.getStatus().equals(Status.WORKFLOW)) {
				taxReason = propertiesManager.getTaxReasonGoodWillAmount();
				demandReasons.addAll(getDemandReasonsForTaxReason(agreementRequest, date, taxReason));
			} else if (i == 2 && agreement.getStatus().equals(Status.ACTIVE)) {
				taxReason = propertiesManager.getTaxReasonRent();
				date = agreementRequest.getAgreement().getExpiryDate();
				demandReasons.addAll(getDemandReasonsForTaxReason(agreementRequest, date, taxReason));
			}
		}
		return demandReasons;
	}
	
	/*
	 * to get demand reason RENT upto current installment in Add/Edit Demand
	 */
	
	public List<DemandReason> getLegacyDemandReason(AgreementRequest agreementRequest) {

		List<DemandReason> demandReasons = new ArrayList<>();
		Agreement agreement = agreementRequest.getAgreement();
		String taxReason = null;
		taxReason = propertiesManager.getTaxReasonRent();
		Date effectiveTodate = getEfectiveTodate(agreement);
		demandReasons.addAll(getDemandReasonsForTaxReason(agreementRequest, effectiveTodate, taxReason));
		return demandReasons;

	}
	
	/*
	 * API to fetch current installment end date based on agreement payment
	 * cycle and current date
	 */
	private Date getEfectiveTodate(Agreement agreement) {
		Calendar cal = Calendar.getInstance();
		if (agreement.getPaymentCycle().equals(PaymentCycle.QUARTER)) {
			cal = getEffectiveQuarterToDate();
		} else if (agreement.getPaymentCycle().equals(PaymentCycle.HALFYEAR)) {

			cal = getEffectiveHalfYearToDate();
		} else if (agreement.getPaymentCycle().equals(PaymentCycle.ANNUAL)) {

			cal = getEffectiveFinYearToDate();
		}

		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();

	}

	private Calendar getEffectiveQuarterToDate() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (month < 3) {
			cal.set(Calendar.MONTH, 2); // 4th quarter
		} else if (month < 6) {
			cal.set(Calendar.MONTH, 5); // 1st Quarter
		} else if (month < 9) {
			cal.set(Calendar.MONTH, 8); // 2nd quarter
		} else {
			cal.set(Calendar.MONTH, 11); // 3rd quarter
		}
		return cal;
	}

	private Calendar getEffectiveHalfYearToDate() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (month < 3) {
			cal.set(Calendar.MONTH, 2);
		} else if (month >= 3 && month <= 8) {
			cal.set(Calendar.MONTH, 8);
		} else {
			cal.set(Calendar.MONTH, 2);
			cal.add(Calendar.YEAR, 1);
		}
		return cal;
	}

	private Calendar getEffectiveFinYearToDate() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (month < 3) {
			cal.set(Calendar.MONTH, 2);
		} else {
			cal.set(Calendar.MONTH, 2);
			cal.add(Calendar.YEAR, 1);
		}
		return cal;
	}
	
	
	private List<DemandReason> getDemandReasonsForTaxReason(AgreementRequest agreementRequest, Date date,
			String taxReason) {

		LOGGER.info("todate value : " + date);
		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandReasonSearchPath()
				+ demandHelper.getDemandReasonUrlParams(agreementRequest, taxReason, date);

		LOGGER.info("DemandRepository getDemandReason url:" + url);
		DemandReasonResponse demandReasonResponse = null;
		try {
			demandReasonResponse = restTemplate.postForObject(url, agreementRequest.getRequestInfo(),
					DemandReasonResponse.class);
			LOGGER.info(demandReasonResponse);
		} catch (HttpClientErrorException e) {
			String errorResponseBody = e.getResponseBodyAsString();
			LOGGER.info("Following exception occurred: " + e.getResponseBodyAsString());
			UserErrorResponse userErrorResponse = null;
			try {
				userErrorResponse = objectMapper.readValue(errorResponseBody, UserErrorResponse.class);
			} catch (JsonMappingException jme) {
				LOGGER.error(
						"Exception Occurred While Mapping JSON Response From demand Service : " + jme.getMessage());
				throw new RuntimeException(jme);
			} catch (JsonProcessingException jpe) {
				LOGGER.error(
						"Exception Occurred While Processing JSON Response From demand Service : " + jpe.getMessage());
				throw new RuntimeException(jpe);
			} catch (IOException ioe) {
				LOGGER.error("Exception Occurred Calling demand Service : " + ioe.getMessage());
				throw new RuntimeException(ioe);
			}
			LOGGER.error("exception from demand module inside first catch block ::"
					+ userErrorResponse.getError().toString());
		} catch (Exception e) {
			LOGGER.error("Exception Occurred While Calling demandReason Service : " + e.getMessage());
			throw e;
		}
		LOGGER.info("demandReasonResponse:" + demandReasonResponse);

		return demandReasonResponse.getDemandReasons();
	}

	public List<Demand> getDemandList(AgreementRequest agreementRequest, List<DemandReason> demandReasons) {

		Agreement agreement = agreementRequest.getAgreement();
		Demand demand = new Demand();
		List<Demand> demandList = new ArrayList<>();
		List<DemandDetails> demandDetails = new ArrayList<>();
		demand.setTenantId(agreement.getTenantId());
		demand.setInstallment(demandReasons.get(0).getTaxPeriod());
		demand.setModuleName("Leases And Agreements");
		
		if (agreement.getDemands() != null) 
			demand.setId(agreement.getDemands().get(0));
		
		DemandDetails demandDetail = null;
		for (DemandReason demandReason : demandReasons) {

			demandDetail = new DemandDetails();
			demandDetail.setCollectionAmount(BigDecimal.ZERO);
			demandDetail.setRebateAmount(BigDecimal.ZERO);
			demandDetail.setTaxReason(demandReason.getName());
			demandDetail.setTaxReasonCode(demandReason.getName());
			demandDetail.setTaxPeriod(demandReason.getTaxPeriod());
			
			LOGGER.info("the demand reason object in the loop : " + demandReason);
			if ("RENT".equalsIgnoreCase(demandReason.getName())) {
				demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getRent()));
			} else if ("GOODWILL_AMOUNT".equalsIgnoreCase(demandReason.getName())) {
				if (agreement.getGoodWillAmount() != null)
					demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getGoodWillAmount()));
				if (agreement.getCollectedGoodWillAmount() != null)
					demandDetail.setCollectionAmount(BigDecimal.valueOf(agreement.getCollectedGoodWillAmount()));
			} else if ("ADVANCE_TAX".equalsIgnoreCase(demandReason.getName())) {
				demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getSecurityDeposit()));
				if (agreement.getCollectedSecurityDeposit() != null)
					demandDetail.setCollectionAmount(BigDecimal.valueOf(agreement.getCollectedSecurityDeposit()));
			}
			if(demandDetail.getTaxAmount()!=null)
			demandDetails.add(demandDetail);
		}

		demand.setDemandDetails(demandDetails);
		demandList.add(demand);
		LOGGER.info("the demand object result after adding details : " + demandList);

		return demandList;
	}

	public DemandResponse createDemand(List<Demand> demands, RequestInfo requestInfo) {
		LOGGER.info("DemandRepository createDemand demands:" + demands.toString());
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemand(demands);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getCreateDemandSevice();

		return restTemplate.postForObject(url, demandRequest, DemandResponse.class);
	}

	public DemandResponse getDemandBySearch(DemandSearchCriteria demandSearchCriteria, RequestInfo requestInfo) {

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandSearchServicepath()
				+ "?demandId=" + demandSearchCriteria.getDemandId();
		LOGGER.info("the url of demand search API call ::: is " + url);

		if (requestInfo == null) {
			// FIXME remove this when application is running good
			LOGGER.info("requestInfo ::: is null ");
			requestInfo = new RequestInfo();
			requestInfo.setApiId("apiid");
			requestInfo.setVer("ver");
			requestInfo.setTs(new Date());
		}

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, requestInfo, DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception thrown from demand search api call ::: " + e);
		}
		LOGGER.info("the response form demand search api call ::: " + demandResponse);
		return demandResponse;
	}

	public DemandResponse updateDemandForCollection(List<Demand> demands, RequestInfo requestInfo) {

		LOGGER.info("DemandRepository createDemand demands:" + demands.toString());
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemand(demands);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getUpdateDemandBasePath()
				+ demands.get(0).getId() + "/" + propertiesManager.getUpdateDemandService();
		LOGGER.info("the url for update demand API call is  ::: " + url);

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, demandRequest, DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception raised during update demand API call ::: " + e);
		}
		LOGGER.info("the exception raised during update demand API call ::: " + demandResponse);
		return demandResponse;
	}

	public DemandResponse updateDemand(List<Demand> demands, RequestInfo requestInfo) {

		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemand(demands);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getUpdateDemandBasePath()
				+ propertiesManager.getUpdateDemandService();

		LOGGER.info("the url for update demand API call is  ::: " + url);

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, demandRequest, DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception raised during update demand API call ::: " + e);
		}
		LOGGER.info("the exception raised during update demand API call ::: " + demandResponse);
		return demandResponse;
	}
}
