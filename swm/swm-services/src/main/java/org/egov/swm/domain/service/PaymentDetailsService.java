package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaymentDetails;
import org.egov.swm.domain.model.PaymentDetailsSearch;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetailsSearch;
import org.egov.swm.domain.repository.PaymentDetailsRepository;
import org.egov.swm.web.requests.PaymentDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsService {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private VendorPaymentDetailsService vendorPaymentDetailsService;

    public PaymentDetailsRequest create(final PaymentDetailsRequest paymentDetailsRequest) {

        validate(paymentDetailsRequest);
        validatePaymentAmount(paymentDetailsRequest);
        
        Long userId = null;
        if (paymentDetailsRequest.getRequestInfo() != null
                && paymentDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != paymentDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = paymentDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final PaymentDetails paymentDetails : paymentDetailsRequest.getPaymentDetails()) {

            setAuditDetails(paymentDetails, userId);

            paymentDetails.setCode(UUID.randomUUID().toString().replace("-", ""));

            // prepareDocuments(paymentDetails);
        }
        return paymentDetailsRepository.create(paymentDetailsRequest);
    }

    public PaymentDetailsRequest update(final PaymentDetailsRequest paymentDetailsRequest) {

        Long userId = null;
        if (paymentDetailsRequest.getRequestInfo() != null
                && paymentDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != paymentDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = paymentDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final PaymentDetails paymentDetails : paymentDetailsRequest.getPaymentDetails()) {
            setAuditDetails(paymentDetails, userId);
            // prepareDocuments(paymentDetails);
        }

        validate(paymentDetailsRequest);

        return paymentDetailsRepository.update(paymentDetailsRequest);
    }

    public Pagination<PaymentDetails> search(final PaymentDetailsSearch paymentDetailsSearch) {

        return paymentDetailsRepository.search(paymentDetailsSearch);
    }

    private void prepareDocuments(final PaymentDetails vendorPaymentDetail) {
        if (vendorPaymentDetail.getDocuments() != null) {
            final List<Document> documentList = vendorPaymentDetail.getDocuments().stream()
                    .filter(record -> record.getFileStoreId() != null).collect(Collectors.toList());

            vendorPaymentDetail.setDocuments(documentList);
        }
        vendorPaymentDetail.getDocuments().forEach(document -> setDocumentDetails(document, vendorPaymentDetail));
    }

    private void setDocumentDetails(final Document document, final PaymentDetails paymentDetails) {
        document.setId(UUID.randomUUID().toString().replace("-", ""));
        document.setTenantId(paymentDetails.getTenantId());
        document.setRefCode(paymentDetails.getCode());
        document.setAuditDetails(paymentDetails.getAuditDetails());
    }

    private void validate(final PaymentDetailsRequest paymentDetailsRequest) {

        VendorPaymentDetailsSearch vendorPaymentDetailsSearch;
        Pagination<VendorPaymentDetails> vendorPaymentDetailsPage;
        for (final PaymentDetails paymentDetail : paymentDetailsRequest.getPaymentDetails()) {

            // Validate for vendor payment
            if (paymentDetail.getVendorPaymentDetails() != null
                    && (paymentDetail.getVendorPaymentDetails().getPaymentNo() == null
                            || paymentDetail.getVendorPaymentDetails().getPaymentNo().isEmpty()))
                throw new CustomException("Payment Number", "Payment Number required ");

            if (paymentDetail.getVendorPaymentDetails() != null
                    && paymentDetail.getVendorPaymentDetails().getPaymentNo() != null) {

                vendorPaymentDetailsSearch = new VendorPaymentDetailsSearch();

                vendorPaymentDetailsSearch.setTenantId(paymentDetail.getTenantId());

                vendorPaymentDetailsSearch.setPaymentNo(paymentDetail.getVendorPaymentDetails().getPaymentNo());

                vendorPaymentDetailsPage = vendorPaymentDetailsService.search(vendorPaymentDetailsSearch);

                if (vendorPaymentDetailsPage == null || vendorPaymentDetailsPage.getPagedData() == null
                        || vendorPaymentDetailsPage.getPagedData().isEmpty())
                    throw new CustomException("Payment Number", "Payment Number required "
                            + paymentDetail.getVendorPaymentDetails().getPaymentNo());
                else
                    paymentDetail.setVendorPaymentDetails(vendorPaymentDetailsPage.getPagedData().get(0));
            }

        }

    }

    private void validatePaymentAmount(PaymentDetailsRequest paymentDetailsRequest) {

        Map<String, Double> requestPaymentAmountMap = new HashMap<String, Double>();

        Map<String, Double> vendorPaymentAmountMap = new HashMap<String, Double>();

        Double amount;
        String tenantId = null;

        if (paymentDetailsRequest != null && paymentDetailsRequest.getPaymentDetails() != null
                && !paymentDetailsRequest.getPaymentDetails().isEmpty())
            tenantId = paymentDetailsRequest.getPaymentDetails().get(0).getTenantId();

        for (PaymentDetails pd : paymentDetailsRequest.getPaymentDetails()) {

            vendorPaymentAmountMap.put(pd.getVendorPaymentDetails().getPaymentNo(),
                    pd.getVendorPaymentDetails().getVendorInvoiceAmount());

            if (requestPaymentAmountMap.get(pd.getVendorPaymentDetails().getPaymentNo()) == null) {

                requestPaymentAmountMap.put(pd.getVendorPaymentDetails().getPaymentNo(), pd.getAmount());

            } else {

                amount = requestPaymentAmountMap.get(pd.getVendorPaymentDetails().getPaymentNo());
                amount = amount + pd.getAmount();

                requestPaymentAmountMap.put(pd.getVendorPaymentDetails().getPaymentNo(), amount);
            }
        }

        for (String paymentNo : vendorPaymentAmountMap.keySet()) {

            if ((requestPaymentAmountMap.get(paymentNo) + getAlreadyPaidAmount(tenantId, paymentNo)) > vendorPaymentAmountMap
                    .get(paymentNo)) {

                throw new CustomException("PaymentAmount", "Invalid Amount for Payment Number: " + paymentNo);

            }

        }

    }

    private Double getAlreadyPaidAmount(String tenantId, String paymentNo) {

        PaymentDetailsSearch search = new PaymentDetailsSearch();
        Double paidAmount = (double) 0;
        search.setTenantId(tenantId);
        search.setPaymentNo(paymentNo);

        Pagination<PaymentDetails> response = search(search);

        if (response != null && response.getPagedData() != null && !response.getPagedData().isEmpty())
            for (PaymentDetails pd : response.getPagedData()) {
                paidAmount = paidAmount + pd.getAmount();
            }

        return paidAmount;
    }

    private void setAuditDetails(final PaymentDetails contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getCode() || contract.getCode().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }
}
