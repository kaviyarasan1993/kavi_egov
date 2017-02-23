package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.lams.exception.ErrorResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.ResponseInfo;
import org.egov.lams.model.SearchAgreementsModel;
import org.egov.lams.model.wrapper.AgreementRequest;
import org.egov.lams.model.wrapper.AgreementResponse;
import org.egov.lams.web.service.AgreementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.egov.lams.exception.Error;

@RestController
public class AgreementController {
	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementController.class);

	@Autowired
	AgreementService agreementService;

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> getAgreements(@ModelAttribute @Valid SearchAgreementsModel searchAgreementsModel,BindingResult bindingResult) {

		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("AgreementController:getAgreements():searchAgreementsModel:" + searchAgreementsModel);
		AgreementResponse agreementResponse = null;
		List<Agreement> agreements = null;
		
			agreements = agreementService.searchAgreement(searchAgreementsModel);
			agreementResponse = new AgreementResponse();
			agreementResponse.setAgreement(agreements);
			agreementResponse.setResposneInfo(
					new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		return new ResponseEntity<AgreementResponse>(agreementResponse, HttpStatus.OK);
	}
	
	@PostMapping("/_Post_Create_Agreement")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid AgreementRequest agreementRequest,BindingResult errors){
		
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("agreementRequest::"+agreementRequest);
		Agreement agreement=agreementRequest.getAgreement();
		//agreementValidator.validateAgreement(agreement);
		agreement=agreementService.createAgreement(agreement);
		List<Agreement>agreements=new ArrayList<Agreement>();
		agreements.add(agreement);
		AgreementResponse agreementResponse=new AgreementResponse();
		agreementResponse.setAgreement(agreements);
		
		return new ResponseEntity<AgreementResponse>(agreementResponse, HttpStatus.CREATED);
	}
	
	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		/*responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		responseInfo.setApi_id("");
		errRes.setResponseInfo(responseInfo);*/
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFilelds().add(errs.getField());
				error.getFilelds().add(errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}

}
