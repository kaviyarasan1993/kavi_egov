package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.lams.exception.Error;
import org.egov.lams.exception.ErrorResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.RequestInfo;
import org.egov.lams.model.ResponseInfo;
import org.egov.lams.model.enums.StatusEnum;
import org.egov.lams.model.wrapper.AgreementRequest;
import org.egov.lams.model.wrapper.AgreementResponse;
import org.egov.lams.service.AgreementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgreementController {
	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementController.class);

	@Autowired
	AgreementService agreementService;

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody RequestInfo resuestInfo, BindingResult bindingResult,
			@RequestParam(name = "tenantId", required = false) String tenantId,
			@RequestParam(name = "agreementId", required = false) List<Long> agreementId,
			@RequestParam(name = "agreementNumber", required = false) String agreementNumber,
			@RequestParam(name = "tenderNumber", required = false) String tenderNumber,
			@RequestParam(name = "fromDate", required = false) Date fromDate,
			@RequestParam(name = "toDate", required = false) Date toDate,
			@RequestParam(name = "status", required = false) StatusEnum status,
			@RequestParam(name = "assetCategory", required = false) Long assetCategory,
			@RequestParam(name = "shoppingComplexNo", required = false) String shoppingComplexNo,
			@RequestParam(name = "assetCode", required = false) String assetCode,
			@RequestParam(name = "locality", required = false) Long locality,
			@RequestParam(name = "revenueWard", required = false) Long revenueWard,
			@RequestParam(name = "electionWard", required = false) Long electionWard,
			@RequestParam(name = "doorno", required = false) Long doorno,
			@RequestParam(name = "allotteeName", required = false) Long allottee,
			@RequestParam(name = "asset", required = false) Long asset,
			@RequestParam(name = "mobileNumber", required = false) Long mobileNumber,
			@RequestParam(name = "tinNumber", required = false) String tinNumber,
			@RequestParam(name = "tradelicense_number", required = false) String tradelicenseNumber,
			@RequestParam(name = "offset", required = false) String offset,
			@RequestParam(name = "size", required = false) String size) {

		//allottee name will be replaced by allottee field since the input is long representing allottee id
		List<Long> alloteeList = null;
		if (allottee != null) {
			alloteeList = new ArrayList<>();
			alloteeList.add(allottee);
		}

		List<Long> assetList = null;
		if (asset != null) {
			assetList = new ArrayList<>();
			assetList.add(allottee);
		}
		
		AgreementCriteria agreementCriteria = new AgreementCriteria(
				tenantId, agreementId, agreementNumber, tenderNumber, fromDate, toDate, status, tinNumber,
				tradelicenseNumber, assetCategory, shoppingComplexNo, assetCode, locality, revenueWard, electionWard,
				doorno, mobileNumber, offset, size, assetList, alloteeList);
				

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("AgreementController:getAgreements():searchAgreementsModel:" + agreementCriteria);

		List<Agreement> agreements = agreementService.searchAgreement(agreementCriteria);
		return getSuccessResponse(agreements);
	}

	private ResponseEntity<?> getSuccessResponse(List<Agreement> agreements) {
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreement(agreements);
		agreementResponse.setResposneInfo(
				new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		return new ResponseEntity<AgreementResponse>(agreementResponse, HttpStatus.OK);
	}

	@PostMapping("/_Post_Create_Agreement")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("agreementRequest::" + agreementRequest);
		Agreement agreement = agreementRequest.getAgreement();
		// agreementValidator.validateAgreement(agreement);
		agreement = agreementService.createAgreement(agreement);
		List<Agreement> agreements = new ArrayList<Agreement>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreement(agreements);

		return new ResponseEntity<AgreementResponse>(agreementResponse, HttpStatus.CREATED);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		/*
		 * ResponseInfo responseInfo = new ResponseInfo();
		 * responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		 * responseInfo.setApi_id(""); errRes.setResponseInfo(responseInfo);
		 */
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