package org.egov.lams.web.controller;

import java.util.Date;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementResponse;
import org.egov.lams.model.FetchAgreementsModel;
import org.egov.lams.model.ResponseInfo;
import org.egov.lams.model.enums.StatusEnum;
import org.egov.lams.web.service.AgreementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO name the class with Search instead fetch
@RestController
public class FetchAgreementController {
    public static final Logger LOG=LoggerFactory.getLogger(FetchAgreementController.class);

	@Autowired
	AgreementService agreementService; 
	
	@RequestMapping(value="/", 
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponse getAgreements(@RequestParam(name="tenant_id",required=false)String tenantId,
			   @RequestParam(name="agreement_id",required=false)Long agreementId,
			   @RequestParam(name="agreement_number",required=false)String agreementNumber,
			   @RequestParam(name="tender_number",required=false)String tenderNumber,
			   @RequestParam(name="from_date",required=false)@DateTimeFormat(pattern="dd/MM/yyyy") Date fromDate,
			   @RequestParam(name="to_date",required=false)@DateTimeFormat(pattern="dd/MM/yyyy") Date toDate,
			   @RequestParam(name="status",required=false)StatusEnum status,
			   @RequestParam(name="asset_category",required=false)String asset_category,
			   @RequestParam(name="shopping_complex_no",required=false)String shoppingComplexNo,
			   @RequestParam(name="asset_code",required=false)String assetCode,
			   @RequestParam(name="locality",required=false)String locality,
			   @RequestParam(name="revenue_ward",required=false)String revenueWard,
			   @RequestParam(name="election_ward",required=false)String electionWard,
			   @RequestParam(name="doorno",required=false)String doorno,
			   @RequestParam(name="offset",required=false)String offset,
			   @RequestParam(name="allottee_name",required=false)String allotteeName,
			   @RequestParam(name="mobilenumber",required=false)Long mobilenumber,
			   @RequestParam(name="tin_number",required=false)String tinNumber,
			   @RequestParam(name="tradelicense_number",required=false)String tradelicenseNumber,
			   @RequestParam(name="size",required=false)String size)
	{
		
		//TODO rename FetchAgreementsModel to SearchAgreementModel
		FetchAgreementsModel fetchAgreementsModel=new FetchAgreementsModel();
		fetchAgreementsModel.setTenantId(tenantId); //TODO - tenant_id no need to be taken for search API.
		fetchAgreementsModel.setAgreementId(agreementId);
		fetchAgreementsModel.setAgreementNumber(agreementNumber);
		fetchAgreementsModel.setFromDate(fromDate);
		fetchAgreementsModel.setStatus(status);
		fetchAgreementsModel.setTenderNumber(tenderNumber);
		fetchAgreementsModel.setToDate(toDate);	
		fetchAgreementsModel.setOffSet(offset);
		fetchAgreementsModel.setSize(size);
		fetchAgreementsModel.setAssetCategory(asset_category);
		fetchAgreementsModel.setShoppingComplexNo(shoppingComplexNo);;
		fetchAgreementsModel.setAssetCode(assetCode);
		fetchAgreementsModel.setLocality(locality); //TODO All the ids should be number, wherever the form feld is drop down its valid.
		fetchAgreementsModel.setRevenueWard(revenueWard);
		fetchAgreementsModel.setElectionWard(electionWard);
		fetchAgreementsModel.setDoorno(doorno);
		fetchAgreementsModel.setAllotteeName(allotteeName);
		fetchAgreementsModel.setMobilenumber(mobilenumber);
		fetchAgreementsModel.setTinNumber(tinNumber);
		fetchAgreementsModel.setTradelicenseNumber(tradelicenseNumber);

		AgreementResponse agreementResponse = null;
		List<Agreement> agreements = null;
		try {
			agreementResponse = new AgreementResponse();
			agreements = agreementService.searchAgreement(fetchAgreementsModel);
			agreementResponse.setAgreement(agreements);
			agreementResponse.setResposneInfo(
					new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return agreementResponse;
	}
}
