package org.egov.lams.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.service.AgreementService;
import org.egov.lams.service.PaymentService;
import org.egov.lams.web.contract.BillReceiptInfoReq;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	PaymentService paymentService;

	@Autowired
	AgreementService agreementService;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(
			@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam (name="agreementNumber", required=false)  String agreementNumber,
			@RequestParam (name="acknowledgementNumber", required=false)  String acknowledgementNumber,
			@RequestParam (name="tenantId", required=true)  String tenantId) {
		LOGGER.info("PaymentController create:"+agreementNumber+","+acknowledgementNumber+"tenantId ::"+tenantId);
		AgreementCriteria agreementCriteria = new AgreementCriteria();
		if (StringUtils.isNotBlank(agreementNumber))
			agreementCriteria.setAgreementNumber(agreementNumber);
		else if (StringUtils.isNotBlank(acknowledgementNumber))
			agreementCriteria.setAcknowledgementNumber(acknowledgementNumber);
		
		agreementCriteria.setTenantId(tenantId);
		Agreement agreement = agreementService.searchAgreement(
				agreementCriteria,requestInfoWrapper.getRequestInfo()).get(0);
		System.out.print("PaymentController - create - agreement - "+agreement);
		return new ResponseEntity<>(paymentService.generateBillXml(agreement,
				requestInfoWrapper.getRequestInfo()), HttpStatus.OK);
	}

	@PostMapping("/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody BillReceiptInfoReq billReceiptInfoReq) {
		
//		System.out.print("lams-services PaymentController update request info  ::: "+billReceiptInfoReq.getRequestInfo());
		return paymentService.updateDemand(billReceiptInfoReq);
	}

}
