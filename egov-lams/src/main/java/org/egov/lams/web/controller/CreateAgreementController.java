package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementRequest;
import org.egov.lams.model.AgreementResponse;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.RequestInfo;
import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;
import org.egov.lams.web.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateAgreementController {

	@Autowired
	AgreementService agreementService;
	
	@RequestMapping(value="/_Post_Create_Agreement",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_VALUE,
					consumes=MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponse createNewAgrrement(@RequestBody AgreementRequest agreementRequest){
		System.out.println("agreementRequest: controller:"+agreementRequest);
		System.out.println("agreementRequest: controller:"+agreementRequest.getAgreement().getStatus());
		//agreementService.insert();
		
		return null;
	}
	
	@RequestMapping(value="/_Get_agreements",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public AgreementRequest getAllAgrrement(){
		
		AgreementRequest agreementRequest=new AgreementRequest();
		
		RequestInfo requestInfo=new RequestInfo();
		requestInfo.setAction("GET");
		requestInfo.setApiId("_Get_agreements");
		requestInfo.setAuthToken("auth_tok");
		requestInfo.setDid("unique id");
		requestInfo.setKey("key");
		requestInfo.setMsgId("msg_id");
		requestInfo.setRequesterId("req_id");
		requestInfo.setTs(new Date());
		requestInfo.setVer("ver");
		
		List<Agreement> agreements=new ArrayList<Agreement>();
		Agreement agreement=new Agreement();
		agreement.setAgreementDate(new Date());
		agreement.setId(1234L);
		agreement.setAgreementNumber("12345");
		
		Allottee allottee=new Allottee();
		allottee.setAadhaarNo("123456789");
		allottee.setAddress("Rajsthan");
		allottee.setContactNo(9738259074L);
		allottee.setEmailId("a@gmail.com");
		allottee.setName("mohit");
		allottee.setPanNo("123abc");
		
		agreement.setAllottee(allottee);
		
		Asset asset=new Asset();
		asset.setBlock("5");
		asset.setCategory("land");
		asset.setCode("029");
		agreement.setAsset(asset);
		
		agreement.setBankGuaranteeAmount(new Double(12));
		agreement.setBankGuaranteeDate(new Date());
		agreement.setCaseNo("23");
		agreement.setCommencementDate(new Date());
		agreement.setCouncilDate(new Date());
		agreement.setCouncilNumber("65455");
		agreement.setExpiryDate(new Date());
		agreement.setNatureOfAllotment(NatureOfAllotmentEnum.AUCTION);
		agreement.setOrderDate(new Date());
		agreement.setOrderDetails("order details");
		agreement.setOrderNo("oreder_no");
		agreement.setPaymentCycle(PaymentCycleEnum.ANNUAL);
		agreement.setRegistrationFree(new Double(500));
		agreement.setRemarks("remarks");
		agreement.setRent(new Double(25000));
		
		RentIncrementType incrementType=new RentIncrementType();
		incrementType.setAssetCategory("Land");
		incrementType.setFlatAmount(78.00);
		incrementType.setFromDate(new Date());
		incrementType.setPercentage(89.00);
		incrementType.setToDate(new Date());
		incrementType.setType("type");
		
		agreement.setRentIncrementMethod(incrementType);
		
		agreement.setRrReadingNo("rr_reading_no");
		agreement.setSecurityDeposit(new Double(5000));
		agreement.setSecurityDepositDate(new Date());
		agreement.setSolvencyCertificateDate(new Date());
		agreement.setStatus(StatusEnum.ACTIVE);
		agreements.add(agreement);
		agreementRequest.setAgreement(agreement);
		agreementRequest.setRequestInfo(requestInfo);
		return agreementRequest;
		
	}
}































