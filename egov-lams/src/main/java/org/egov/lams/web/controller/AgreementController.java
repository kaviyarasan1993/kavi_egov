package org.egov.lams.web.controller;

import java.util.Date;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.ResponseInfo;
import org.egov.lams.model.SearchAgreementsModel;
import org.egov.lams.model.wrapper.AgreementResponse;
import org.egov.lams.web.service.SearchAgreementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgreementController {
    public static final Logger LOGGER=LoggerFactory.getLogger(AgreementController.class);

	@Autowired
	SearchAgreementService agreementService; 
	
	@RequestMapping(value="/", 
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponse getAgreements(@ModelAttribute SearchAgreementsModel searchAgreementsModel)
	{
		LOGGER.info("AgreementController getAgreements() searchAgreementsModel:"+searchAgreementsModel);
		AgreementResponse agreementResponse = null;
		List<Agreement> agreements = null;
		try {
			agreements = agreementService.searchAgreement(searchAgreementsModel);
			agreementResponse = new AgreementResponse();
			agreementResponse.setAgreement(agreements);
			agreementResponse.setResposneInfo(
			new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return agreementResponse;
	}
}
