package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.Indent;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.model.Material;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListResponse;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.model.PurchaseIndentDetail;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.PurchaseOrder.PurchaseTypeEnum;
import org.egov.inv.model.PurchaseOrder.StatusEnum;
import org.egov.inv.model.PurchaseOrderDetail;
import org.egov.inv.model.PurchaseOrderDetailSearch;
import org.egov.inv.model.PurchaseOrderRequest;
import org.egov.inv.model.PurchaseOrderResponse;
import org.egov.inv.model.PurchaseOrderSearch;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Supplier;
import org.egov.inv.model.SupplierGetRequest;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.IndentEntity;
import org.egov.inv.persistence.entity.PriceListEntity;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.egov.inv.persistence.repository.PriceListJdbcRepository;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.egov.inv.persistence.repository.SupplierJdbcRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class PurchaseOrderService extends DomainService {

    @Autowired
    private PurchaseOrderJdbcRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Autowired
    private PriceListJdbcRepository priceListjdbcRepository;
    
    @Autowired
    private IndentJdbcRepository indentJdbcRepository;

    @Autowired
    private IndentService indentService;

    @Autowired
    private PriceListService priceListService;
    
    @Autowired
    private MaterialService materialService;

    @Autowired
    private UomService uomService;
    
    @Autowired
    SupplierJdbcRepository supplierJdbcRepository;

    @Value("${inv.purchaseorders.save.topic}")
    private String saveTopic;

    @Value("${inv.pricelists.podetail.required}")
    private Boolean priceListConfig;

    @Value("${inv.purchaseorders.save.key}")
    private String saveKey;

    @Value("${inv.purchaseorders.update.topic}")
    private String updateTopic;

    @Value("${inv.purchaseorders.update.key}")
    private String updateKey;

    @Value("${inv.purchaseorders.nonindent.save.topic}")
    private String saveNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.save.key}")
    private String saveNonIndentKey;

    @Value("${inv.purchaseorders.nonindent.update.topic}")
    private String updateNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.update.key}")
    private String updateNonIndentKey;

    @Value("${usetotal.indent.quantity}")
    private boolean isTotalIndentQuantityUsedInPo;

    @Autowired
    private MaterialReceiptJdbcRepository materialJdbcRepository;

    @Autowired
    private MdmsRepository mdmsRepository;

    private String INDENT_MULTIPLE = "Multiple";

    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {

        try {
            List<PurchaseOrder> purchaseOrders = purchaseOrderRequest.getPurchaseOrders();
            InvalidDataException errors = new InvalidDataException();
            validate(purchaseOrders, Constants.ACTION_CREATE, tenantId);
            List<String> sequenceNos = purchaseOrderRepository.getSequence(PurchaseOrder.class.getSimpleName(), purchaseOrders.size());
            int i = 0;
            for (PurchaseOrder purchaseOrder : purchaseOrders) {

                if (purchaseOrder.getAdvanceAmount() != null) {
                    if (purchaseOrder.getAdvanceAmount().compareTo(purchaseOrder.getTotalAmount()) > 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }
                }

                if (purchaseOrder.getAdvanceAmount() != null && purchaseOrder.getAdvancePercentage() != null) {

                    if (purchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), purchaseOrder.getAdvancePercentage() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                    if (purchaseOrder.getAdvanceAmount().compareTo(purchaseOrder.getTotalAmount()) > 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                    if ((purchaseOrder.getTotalAmount().multiply(purchaseOrder.getAdvancePercentage())).divide(new BigDecimal(100), RoundingMode.FLOOR).compareTo(purchaseOrder.getAdvanceAmount().divide(new BigDecimal(1), RoundingMode.FLOOR)) < 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvancePercentage() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                }

                purchaseOrder.setStatus(StatusEnum.APPROVED);
                String purchaseOrderNumber = appendString(purchaseOrder);
                purchaseOrder.setId(sequenceNos.get(i));

                if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                    purchaseOrder.setPurchaseType(purchaseOrders.get(0).getPurchaseType());
                } else
                    purchaseOrder.setPurchaseType(PurchaseTypeEnum.INDENT);

                //TODO: move to id-gen with format <ULB short code>/<Store Code>/<fin. Year>/<serial No.>
                purchaseOrder.setPurchaseOrderNumber(purchaseOrderNumber);
                i++;
                int j = 0;
                purchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
                List<String> detailSequenceNos = purchaseOrderRepository.getSequence(PurchaseOrderDetail.class.getSimpleName(), purchaseOrder.getPurchaseOrderDetails().size());

                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    purchaseOrderDetail.setId(detailSequenceNos.get(j));
                    purchaseOrderDetail.setTenantId(purchaseOrder.getTenantId());
                    
                    if (purchaseOrderDetail.getPurchaseIndentDetails() != null
                            && purchaseOrderDetail.getPurchaseIndentDetails().size() > 0) {
                        int k = 0;
                        List<String> poIndentDetailSequenceNos = purchaseOrderRepository.getSequence(
                                PurchaseIndentDetail.class.getSimpleName(),
                                purchaseOrderDetail.getPurchaseIndentDetails().size());

                        // Order quantity must be less than (tenderQuantity - usedQuantity) in case of tender
                        if(null != purchaseOrder.getRateType() && purchaseOrder.getRateType().toString().equals("One Time Tender"))
                        if(null != purchaseOrderDetail.getOrderQuantity() && purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getTenderQuantity().subtract(purchaseOrderDetail.getUsedQuantity())) > 0) {
                        	errors.addDataError(ErrorCode.QTY_LE_SCND_ROW.getCode(), " at serial no." + purchaseOrder.getPurchaseOrderDetails().indexOf(purchaseOrderDetail));
                        }
                        
                        for (PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail
                                .getPurchaseIndentDetails()) {
                            purchaseIndentDetail.setId(poIndentDetailSequenceNos.get(k));
                            purchaseIndentDetail.setTenantId(purchaseOrder.getTenantId());
                            k++;
                        }

                    }

                    //Logic to split PODetail order quantity across multiple indentdetails starts
                    BigDecimal totalIndentQuantity = new BigDecimal(0);
                    
                    if(purchaseOrderDetail.getPurchaseIndentDetails()!=null)
                    for(PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail.getPurchaseIndentDetails()) {
                    	totalIndentQuantity = totalIndentQuantity.add(purchaseIndentDetail.getIndentDetail().getIndentQuantity());
                    }
                    
                    for(PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail.getPurchaseIndentDetails()) {
                    	if(totalIndentQuantity.compareTo(purchaseOrderDetail.getOrderQuantity())>=0){
	                    	purchaseIndentDetail.getIndentDetail().setPoOrderedQuantity(purchaseOrderDetail.getOrderQuantity());
                    	}
                    }
                    //Logic to split PODetail order quantity across multiple indentdetails ends

                    Object uom = mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", purchaseOrderDetail.getUom().getCode(), Uom.class);
                    purchaseOrderDetail.setUom((Uom) uom);
                    // purchaseOrderDetail.setUom(uomService.getUom(purchaseOrderDetail.getTenantId(), purchaseOrderDetail.getUom().getCode(),new RequestInfo()));
                    if (purchaseOrderDetail.getOrderQuantity() != null) {
                        purchaseOrderDetail.setOrderQuantity(purchaseOrderDetail.getOrderQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getIndentQuantity() != null) {
                        purchaseOrderDetail.setIndentQuantity(purchaseOrderDetail.getIndentQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getTenderQuantity() != null) {
                        purchaseOrderDetail.setTenderQuantity(purchaseOrderDetail.getTenderQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getUsedQuantity() != null) {
                        purchaseOrderDetail.setUsedQuantity(purchaseOrderDetail.getUsedQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getUnitPrice() != null) {
                        purchaseOrderDetail.setUnitPrice(purchaseOrderDetail.getUnitPrice().divide(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    j++;
                }

            }

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.

            if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                if (purchaseOrders.get(0).getPurchaseType().toString()
                        .equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
                    kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
                else
                    kafkaQue.send(saveNonIndentTopic, saveNonIndentKey, purchaseOrderRequest);
            } else { //TODO: REMOVE BELOW, IF PURCHASE TYPE IS PROPER. oTHER WISE BY DEFAULT PASSING TO INDENT PURCHASE.
                kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
            }
            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
            response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }

    @Transactional
    public PurchaseOrderResponse update(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {

        try {
            List<PurchaseOrder> purchaseOrder = purchaseOrderRequest.getPurchaseOrders();
            validate(purchaseOrder, Constants.ACTION_UPDATE, tenantId);

            for (PurchaseOrder eachPurchaseOrder : purchaseOrder) {
            	
            	if(eachPurchaseOrder.getStatus().equals(PurchaseOrder.StatusEnum.fromValue("Rejected"))) {
                	for (PurchaseOrderDetail eachPurchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                		for(PurchaseIndentDetail purchaseIndentDetail : eachPurchaseOrderDetail.getPurchaseIndentDetails()) {
                			purchaseIndentDetail.getIndentDetail().setPoOrderedQuantity(new BigDecimal(0));
                			purchaseIndentDetail.getIndentDetail().setIndentQuantity(new BigDecimal(0));
                		}
                	}
                }

            	else {
	                InvalidDataException errors = new InvalidDataException();
	            	
	                if(eachPurchaseOrder.getPurchaseOrderNumber() == null) {
	            		errors.addDataError(ErrorCode.NULL_VALUE.getCode(), eachPurchaseOrder.getPurchaseOrderNumber() + " at serial no." + (purchaseOrder.indexOf(eachPurchaseOrder) + 1));
	            	}
	                
	                if (eachPurchaseOrder.getAdvanceAmount() != null) {
	                    if (eachPurchaseOrder.getAdvanceAmount().compareTo(eachPurchaseOrder.getTotalAmount()) > 0) {
	                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), eachPurchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrder.indexOf(eachPurchaseOrder) + 1));
	                    }
	                }
	
	                BigDecimal totalAmount = new BigDecimal(0);
	                eachPurchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_UPDATE));
	
	                for (PurchaseOrderDetail eachPurchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
	                	
	                    //Logic to split PODetail order quantity across multiple indentdetails starts
	                    BigDecimal totalIndentQuantity = new BigDecimal(0);
	                    
	                    if(eachPurchaseOrderDetail.getPurchaseIndentDetails()!=null)
	                    for(PurchaseIndentDetail purchaseIndentDetail : eachPurchaseOrderDetail.getPurchaseIndentDetails()) {
	                    	totalIndentQuantity = totalIndentQuantity.add(purchaseIndentDetail.getIndentDetail().getIndentQuantity());
	                    }
	                    
	                    for(PurchaseIndentDetail purchaseIndentDetail : eachPurchaseOrderDetail.getPurchaseIndentDetails()) {
	                    	if(totalIndentQuantity.compareTo(eachPurchaseOrderDetail.getOrderQuantity())>=0){
		                    	purchaseIndentDetail.getIndentDetail().setPoOrderedQuantity(eachPurchaseOrderDetail.getOrderQuantity());
	                    	}
	                    }
	                    //Logic to split PODetail order quantity across multiple indentdetails ends
	                	
	                    eachPurchaseOrderDetail.setUom(uomService.getUom(eachPurchaseOrderDetail.getTenantId(), eachPurchaseOrderDetail.getUom().getCode(), new RequestInfo()));
	                    if (eachPurchaseOrderDetail.getOrderQuantity() != null) {
	                        eachPurchaseOrderDetail.setOrderQuantity(eachPurchaseOrderDetail.getOrderQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
	                    }
	                    if (eachPurchaseOrderDetail.getIndentQuantity() != null) {
	                        eachPurchaseOrderDetail.setIndentQuantity(eachPurchaseOrderDetail.getIndentQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
	                    }
	                    if (eachPurchaseOrderDetail.getTenderQuantity() != null) {
	                        eachPurchaseOrderDetail.setTenderQuantity(eachPurchaseOrderDetail.getTenderQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
	                    }
	                    if (eachPurchaseOrderDetail.getUsedQuantity() != null) {
	                        eachPurchaseOrderDetail.setUsedQuantity(eachPurchaseOrderDetail.getUsedQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
	                    }
	                    if (eachPurchaseOrderDetail.getUnitPrice() != null) {
	                        eachPurchaseOrderDetail.setUnitPrice(eachPurchaseOrderDetail.getUnitPrice().divide(eachPurchaseOrderDetail.getUom().getConversionFactor()));
	                    }
	                    totalAmount = totalAmount.add(eachPurchaseOrderDetail.getOrderQuantity().multiply(eachPurchaseOrderDetail.getUnitPrice()).add(totalAmount));
	                }
	                eachPurchaseOrder.setTotalAmount(totalAmount);
	            }
            }

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.

            if (purchaseOrder.size() > 0 && purchaseOrder.get(0).getPurchaseType() != null) {
                if (purchaseOrder.get(0).getPurchaseType().toString()
                        .equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
                    kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest);
                else
                    kafkaQue.send(updateNonIndentTopic, updateNonIndentKey, purchaseOrderRequest);
            } else
                kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest); //TODO REMOVE THIS IF PUCHASE TYPE PRESENT.

            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
            response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }


    public PurchaseOrderResponse search(PurchaseOrderSearch is) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        Pagination<PurchaseOrder> search = purchaseOrderRepository.search(is);

        if (search.getPagedData().size() > 0) {
            for (PurchaseOrder purchaseOrder : search.getPagedData()) {
                PurchaseOrderDetailSearch purchaseOrderDetailSearch = new PurchaseOrderDetailSearch();
                purchaseOrderDetailSearch.setPurchaseOrder(purchaseOrder.getPurchaseOrderNumber());
                purchaseOrderDetailSearch.setTenantId(purchaseOrder.getTenantId());
                Pagination<PurchaseOrderDetail> detailPagination = purchaseOrderDetailService.search(purchaseOrderDetailSearch);
                purchaseOrder.setPurchaseOrderDetails(detailPagination.getPagedData().size() > 0 ? detailPagination.getPagedData() : Collections.EMPTY_LIST);
            }
        }

        response.setPurchaseOrders(search.getPagedData());
        response.setPage(getPage(search));
        return response;

    }

    private void validate(List<PurchaseOrder> pos, String method, String tenantId) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (pos == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrders", null);
                    }
                }
                break;

                case Constants.ACTION_UPDATE: {
                    if (pos == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrders", null);
                    }
                }
                break;

            }

            Long currentMilllis = System.currentTimeMillis();

            if(!method.equals(Constants.ACTION_SEARCH_INDENT_FOR_PO))
            for (PurchaseOrder eachPurchaseOrder : pos) {
                BigDecimal totalAmount = new BigDecimal(0);
                int index = pos.indexOf(eachPurchaseOrder) + 1;
                if (eachPurchaseOrder.getPurchaseOrderDate() > currentMilllis) {
					String date = convertEpochtoDate(eachPurchaseOrder.getPurchaseOrderDate());
                    errors.addDataError(ErrorCode.PO_DATE_LE_TODAY.getCode(), date + " at serial no." + index);
                }
                if (null != eachPurchaseOrder.getExpectedDeliveryDate()) {
                    if (eachPurchaseOrder.getExpectedDeliveryDate() < eachPurchaseOrder.getPurchaseOrderDate()) {
						String date = convertEpochtoDate(eachPurchaseOrder.getExpectedDeliveryDate());
                        errors.addDataError(ErrorCode.EXP_DATE_GE_PODATE.getCode(), date + " at serial no." + index);
                    }
                }

                if (eachPurchaseOrder.getAdvanceAmount() != null && eachPurchaseOrder.getAdvancePercentage() != null) {

                    if (eachPurchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), eachPurchaseOrder.getAdvancePercentage() + " at serial no." + (pos.indexOf(eachPurchaseOrder) + 1));
                    }

                } else if (eachPurchaseOrder.getAdvancePercentage() != null) {
                    if (eachPurchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), eachPurchaseOrder.getAdvancePercentage() + " at serial no." + (pos.indexOf(eachPurchaseOrder) + 1));
                    }
                }

                String indentNumbers = "";
                if(null != eachPurchaseOrder.getPurchaseOrderDetails())
                for (PurchaseOrderDetail purchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                	IndentEntity ie = indentJdbcRepository.findById(IndentEntity.builder().indentNumber(purchaseOrderDetail.getIndentNumber()).tenantId(purchaseOrderDetail.getTenantId()).build());
                	PriceListEntity ple = priceListjdbcRepository.findById(PriceListEntity.builder().id(purchaseOrderDetail.getPriceList().getId()).tenantId(purchaseOrderDetail.getTenantId()).build());
                	//RateContract reference validation
                	if(ple.getId() == null) {
                		throw new CustomException("priceList", "RateContract" + purchaseOrderDetail.getPriceList() + " doesn't exists");
                	}
                	if(ie.getId() == null) {
                		throw new CustomException("indentnumber", "IndentNumber " + purchaseOrderDetail.getIndentNumber() + " doesn't exists");
                	}
                    indentNumbers += purchaseOrderDetail.getIndentNumber() + ",";
                }
                indentNumbers.replaceAll(",$", "");

                IndentSearch is = IndentSearch.builder().ids(new ArrayList<String>(Arrays.asList(indentNumbers.split(",")))).tenantId(tenantId).build();
                IndentResponse isr = indentService.search(is, new RequestInfo());

                for (Indent in : isr.getIndents()) {
                    if (in.getIndentDate().compareTo(eachPurchaseOrder.getPurchaseOrderDate()) > 0) {
						String date = convertEpochtoDate(eachPurchaseOrder.getPurchaseOrderDate());
                        errors.addDataError(ErrorCode.DATE1_LE_DATE2.getCode(), date + " at serial no." + pos.indexOf(eachPurchaseOrder));
                    }
                }
                
                //Supplier reference validation
                if (null != eachPurchaseOrder.getSupplier() && !isEmpty(eachPurchaseOrder.getSupplier().getCode())) {
                    SupplierGetRequest supplierGetRequest = new SupplierGetRequest();
                    supplierGetRequest.setCode(Collections.singletonList(eachPurchaseOrder.getSupplier().getCode()));
                    supplierGetRequest.setTenantId(eachPurchaseOrder.getTenantId());
                    Pagination<Supplier> supplierPagination = supplierJdbcRepository.search(supplierGetRequest);
                    if (!(supplierPagination.getPagedData().size() > 0)) {
                        throw new CustomException("supplier", "Supplier " + eachPurchaseOrder.getSupplier().getCode() + " doesn't exists");
                    }
                }
                
                //RateType reference validation
                if (!Arrays.stream(PriceList.RateTypeEnum.values()).anyMatch((t) -> t.equals(PriceList.RateTypeEnum.fromValue(eachPurchaseOrder.getRateType().toString())))) {
                    throw new CustomException("rateType", "Please enter a valid RateType");
                }
                
                if (null != eachPurchaseOrder.getPurchaseOrderDetails()) {
                    for (PurchaseOrderDetail poDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                        int detailIndex = eachPurchaseOrder.getPurchaseOrderDetails().indexOf(poDetail) + 1;
                        
                        //validating ratecontracts for each POLine
                        boolean isRateContractExist =false;
	            		if(purchaseOrderRepository.isRateContractsExists(eachPurchaseOrder.getSupplier().getCode(), eachPurchaseOrder.getRateType().toString(), poDetail.getMaterial().getCode()))
	            		{
	            			isRateContractExist = true;
	            			break;
	            		}
                        if(!isRateContractExist)
                        	throw new CustomException("rateContract", "No RateContracts exist for the given combination of indent, supplier and ratetype");
                        
                        
                        //validation of order quantity incase of tender
                        if(( poDetail.getOrderQuantity().add(poDetail.getUsedQuantity()) ).compareTo(poDetail.getTenderQuantity()) > 0) {
                        	throw new CustomException("orderQuantity", "Order Quantity exceeding the permitted");
                        }
                        
                        //Material reference validation
                        if(null != poDetail.getMaterial()) {
                        	if(validateMaterial(tenantId, poDetail.getMaterial())) {
                        		throw new CustomException("material", "Material " + poDetail.getMaterial().getCode() + " doesn't exists");
                        	}
                        }
                        
                        if (null != poDetail.getMaterial() && StringUtils.isEmpty(poDetail.getMaterial().getCode())) {
                            errors.addDataError(ErrorCode.MAT_DETAIL.getCode(), " at serial no." + detailIndex);
                        }

                        //ratecontract mandatory, then rate, price, quantity are mandatory at each line level
                        if (priceListConfig && null == poDetail.getPriceList().getId()) {
                            errors.addDataError(ErrorCode.RATE_CONTRACT.getCode(), " at serial no." + detailIndex);
                        }
                        
                        if (priceListConfig && (null == poDetail.getOrderQuantity() || poDetail.getOrderQuantity().compareTo(new BigDecimal(0))<0)) {
                        	throw new CustomException("orderQuantity", "OrderQuantity cannnot be blank");
                        }
                        
                        if (priceListConfig && (null == poDetail.getUnitPrice() || poDetail.getUnitPrice().compareTo(new BigDecimal(0))<0)) {
                        	throw new CustomException("unitPrice", "UnitPrice cannnot be blank");
                        }
                        
                        //validate the rates entered for creating PO with the one's in pricelist
                        if(poDetail.getPriceList()!=null && poDetail.getPriceList().getPriceListDetails()!=null)
                        for(PriceListDetails pld : poDetail.getPriceList().getPriceListDetails()) {
                        	if(pld.getMaterial().getCode()!=null && poDetail.getMaterial().getCode()!=null)
                        	if(pld.getMaterial().getCode().equals(poDetail.getMaterial().getCode()) && pld.getRatePerUnit().compareTo(poDetail.getUnitPrice().doubleValue())!=0) {
                        		throw new CustomException("materialRate", "Material rate entered is not matching with rate contract rate");
                        	}
                        }

                        if (null != poDetail.getOrderQuantity() && null != poDetail.getIndentQuantity()) {
                            int res = poDetail.getOrderQuantity().compareTo(poDetail.getIndentQuantity());
                            if (res == 1) {
                                errors.addDataError(ErrorCode.ORDQTY_LE_INDQTY.getCode(), eachPurchaseOrder.getExpectedDeliveryDate().toString() + " at serial no." + detailIndex);
                            }
                        }
                        totalAmount = totalAmount.add(poDetail.getOrderQuantity().multiply(poDetail.getUnitPrice()).add(totalAmount));
                    }
                    eachPurchaseOrder.setTotalAmount(totalAmount);
                } else
                    errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrdersDetail", null);
            }


        } catch (IllegalArgumentException e) {

        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;

    }


    // GET INDENT OBJECTS BY PASSING ID'S
    // Iterate through each indent
    // supplier and rate type is required. -- mandatory fields.?????
    // for each indent check available quantity to procure.COMBINE
    // MATERIALS WHICH ARE NOT ISSUED. Build indent detail list.
    // for each material, group together and build indent detail and po
    // link . Save indent number at po detail level. ?
    // for each material, check rate contract and amount detail by
    // supplier. If not found, skip that material.
    // Get rate contract details for each line
    // Tender quantity and used quantity should be required.
    // Show unit rate based on rate contract.
    // Get available quantity for each material- ?
    // Send indent detail list for each row of po line.
    // Build Po reponse object and send back.

    public PurchaseOrderResponse preparePoFromIndents(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));

        List<PurchaseOrder> purchaseOrders = purchaseOrderRequest.getPurchaseOrders();
        validate(purchaseOrders, Constants.ACTION_SEARCH_INDENT_FOR_PO, tenantId);
        List<PurchaseOrder> finalPurchaseOrders = new ArrayList<PurchaseOrder>();

        for (PurchaseOrder purchaseOrder : purchaseOrders) {
        	InvalidDataException errors = new InvalidDataException();
            // TODO: validation of supplier, rate type and indents.

        	//RateType reference validation
            if (!Arrays.stream(PriceList.RateTypeEnum.values()).anyMatch((t) -> t.equals(PriceList.RateTypeEnum.fromValue(purchaseOrder.getRateType().toString())))) {
                throw new CustomException("rateType", "Please select a valid RateType");
            }
        	
            IndentSearch indentSearch = new IndentSearch();
            indentSearch.setIds(purchaseOrder.getIndentNumbers());
            indentSearch.setTenantId(tenantId);
            IndentResponse indentResponse = indentService.search(indentSearch, purchaseOrderRequest.getRequestInfo());

            Map<String, PurchaseOrderDetail> purchaseOrderLines = new HashMap<String, PurchaseOrderDetail>();

            // configure to check whether full quantity of indent to be consider or quantity pending ( out of issue) used to create po.

            // convert all items into purchase uom by referring each indent.

            //Logic to check if indent lines are valid for PO Creation
            for(String indentNo:purchaseOrder.getIndentNumbers()){
            	if(!purchaseOrderRepository.getIsIndentValidForPOCreate(indentNo)) {
            		throw new CustomException("indents", "One / Some of the selected indents is not eligible for Creation of PO");            		
            	}
            }
            
            boolean isRateContractExist =false;
            for(Indent indent : indentResponse.getIndents()) {
            	for(IndentDetail indentDetail : indent.getIndentDetails()) {
            		//checking if atleast one ratecontract exists for materials in the given indents
            		if(purchaseOrderRepository.isRateContractsExists(purchaseOrder.getSupplier().getCode(), purchaseOrder.getRateType().toString(), indentDetail.getMaterial().getCode()))
            		{
            			isRateContractExist = true;
            			break;
            		}
            	}
            }
            if(!isRateContractExist)
            	throw new CustomException("rateContract", "No RateContracts exist for the given combination of indent, supplier and ratetype");
            
            for (Indent indent : indentResponse.getIndents()) {

                for (IndentDetail indentDetail : indent.getIndentDetails()) {
                    PurchaseOrderDetail purchaseOrderDetail = null;
                    BigDecimal pendingQty = BigDecimal.ZERO;

                    // Consider full indent quantity to be used in
                    // poorderquantity
                    if (!isTotalIndentQuantityUsedInPo) {
                        if (indentDetail != null && indentDetail.getIndentQuantity()
                                .compareTo(indentDetail.getPoOrderedQuantity() != null
                                        ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO) > 0) {
                            pendingQty = indentDetail.getIndentQuantity()
                                    .subtract(indentDetail.getPoOrderedQuantity() != null
                                            ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO);

                        }
                    } else {

                        if (indentDetail != null && (indentDetail.getIndentQuantity()
                                .subtract(indentDetail.getTotalProcessedQuantity() != null
                                        ? indentDetail.getTotalProcessedQuantity() : BigDecimal.ZERO)
                                .compareTo(indentDetail.getPoOrderedQuantity() != null
                                        ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO) > 0)) {
                            pendingQty = indentDetail.getIndentQuantity()
                                    .subtract(indentDetail.getTotalProcessedQuantity() != null
                                            ? indentDetail.getTotalProcessedQuantity() : BigDecimal.ZERO)
                                    .subtract(indentDetail.getPoOrderedQuantity() != null
                                            ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO);

                        }

                    }

                    PriceListSearchRequest priceListSearchRequest = new PriceListSearchRequest();
                    priceListSearchRequest.setTenantId(tenantId);
                    priceListSearchRequest.setSupplierName(purchaseOrder.getSupplier().getCode());
                    priceListSearchRequest.setRateType(purchaseOrder.getRateType().toString());
                    priceListSearchRequest.setRateContractDate(System.currentTimeMillis());

                    priceListSearchRequest.setActive(true);

                    // write api to get price list by passing material,supplier, rate type, active one and with current date.

                    PriceListResponse priceListResponse = priceListService.searchPriceList(priceListSearchRequest,
                            purchaseOrderRequest.getRequestInfo());

                    // get used quantity for each tender type rate types and used ones.

                    // save indent number, tender used quantity, total indent  qty?

                    if (purchaseOrderLines.get(indentDetail.getMaterial().getCode()) == null) {
                        purchaseOrderDetail = new PurchaseOrderDetail();
                        purchaseOrderDetail.setMaterial(indentDetail.getMaterial());
                        purchaseOrderDetail.setUom(indentDetail.getMaterial().getPurchaseUom());
                        purchaseOrderDetail.setIndentQuantity(pendingQty);
                        if (null != purchaseOrder.getRateType() && purchaseOrder.getRateType().toString().equals("One Time Tender")) {
                            purchaseOrderDetail.setTenderQuantity(new BigDecimal(priceListjdbcRepository.getTenderQty(purchaseOrder.getSupplier().getCode(), indentDetail.getMaterial().getCode(), purchaseOrder.getRateType().toString())));
                        }
                        purchaseOrderDetail.setUsedQuantity(new BigDecimal(purchaseOrderRepository.getUsedQty(purchaseOrder.getSupplier().getCode(), indentDetail.getMaterial().getCode(), purchaseOrder.getRateType().toString())));
                        purchaseOrderDetail.setIndentNumber(indent.getIndentNumber());
                        purchaseOrderDetail.setTenantId(tenantId);
                        buildPurchaseOrderIndentDetail(purchaseOrderRequest, purchaseOrder, indentDetail,
                                purchaseOrderDetail, pendingQty, tenantId);

                    } else {
                        purchaseOrderDetail = purchaseOrderLines.get(indentDetail.getMaterial().getCode());
                        purchaseOrderDetail.setIndentQuantity(purchaseOrderDetail.getIndentQuantity().add(pendingQty));
                        purchaseOrderDetail.setIndentNumber(INDENT_MULTIPLE);
                        buildPurchaseOrderIndentDetail(purchaseOrderRequest, purchaseOrder, indentDetail,
                                purchaseOrderDetail, pendingQty, tenantId);

                    }

                    if (priceListResponse != null && priceListResponse.getPriceLists() != null && priceListResponse.getPriceLists().size() > 0)
                        purchaseOrderDetail.setPriceList(priceListResponse.getPriceLists().get(0));//TODO: SHOULD SUPPORT MULTIPLE PRICE LIST.

                    purchaseOrder.addPurchaseOrderDetailsItem(purchaseOrderDetail);

                }

            }

            finalPurchaseOrders.add(purchaseOrder);
        }

        response.setPurchaseOrders(finalPurchaseOrders);

        return response;
    }

    public Boolean checkAllItemsSuppliedForPo(PurchaseOrderSearch purchaseOrderSearch) {

        Boolean flag = true;

        PurchaseOrderResponse search = search(purchaseOrderSearch);

        if (search.getPurchaseOrders().size() > 0) {
            List<PurchaseOrder> purchaseOrders = search.getPurchaseOrders();

            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    if (purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getReceivedQuantity()) > 0) {
                        return false;
                    }
                }
            }
        } else
            throw new CustomException("po", "Purchase order not found");

        return flag;
    }


    public PurchaseOrderResponse getPOPendingToDeliver(PurchaseOrderSearch purchaseOrderSearch) {

        PurchaseOrderResponse search = search(purchaseOrderSearch);

        if (search.getPurchaseOrders().size() > 0) {
            List<PurchaseOrder> purchaseOrders = search.getPurchaseOrders();

            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    if (purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getReceivedQuantity()) == 0) {
                        purchaseOrder.getPurchaseOrderDetails().remove(purchaseOrderDetail);
                    }
                }
            }
        } else
            throw new CustomException("po", "Purchase order not found");

        return search;
    }

	private boolean validateMaterial(String tenantId, Material material) {
		Material materialFromMdms = materialService.fetchMaterial(tenantId, material.getCode(), new org.egov.inv.model.RequestInfo());
		if(materialFromMdms == null) {
			return false;
		}
		if(material.getBaseUom().getCode()!=null)
		if(!materialFromMdms.getBaseUom().getCode().equals(material.getBaseUom().getCode())) {
			return false;
		}
		return true;
	}
	
    private void buildPurchaseOrderIndentDetail(PurchaseOrderRequest purchaseOrderRequest, PurchaseOrder purchaseOrder,
                                                IndentDetail indentDetail, PurchaseOrderDetail purchaseOrderDetail, BigDecimal pendingQty, String tenantId) {
        PurchaseIndentDetail poIndentDetail = new PurchaseIndentDetail();
        poIndentDetail.setTenantId(tenantId);
        poIndentDetail.setAuditDetails(
                getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
        poIndentDetail.setQuantity(pendingQty);
        poIndentDetail.setIndentDetail(indentDetail);
        purchaseOrderDetail.addPurchaseIndentDetailsItem(poIndentDetail);
    }

    private String appendString(PurchaseOrder poOrder) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String code = "PO/";
        int id = Integer.valueOf(materialJdbcRepository.getSequence(poOrder));
        String idgen = String.format("%05d", id);
        String purchaseOrderNumber = code + idgen + "/" + year;
        return purchaseOrderNumber;
    }
    private String convertEpochtoDate(Long date)
	 {
		 Date epoch = new Date(date);
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 String s2 = format.format(epoch);
		 return s2;
	 }

}