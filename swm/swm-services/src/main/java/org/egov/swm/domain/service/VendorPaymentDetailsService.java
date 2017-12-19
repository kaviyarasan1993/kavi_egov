package org.egov.swm.domain.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetailsSearch;
import org.egov.swm.domain.repository.VendorPaymentDetailsRepository;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VendorPaymentDetailsService {

    private final VendorPaymentDetailsRepository vendorPaymentDetailsRepository;

    private final VendorContractService vendorContractService;

    private final EmployeeRepository employeeRepository;

    private final IdgenRepository idgenRepository;

    private final String idGenNameForPaymentNumberPath;

    public VendorPaymentDetailsService(final VendorPaymentDetailsRepository vendorPaymentDetailsRepository,
            final VendorContractService vendorContractService, final EmployeeRepository employeeRepository,
            final IdgenRepository idgenRepository,
            @Value("${egov.swm.vendor.paymentdetails.paymentno.idgen.name}") final String idGenNameForPaymentNumberPath) {
        this.vendorPaymentDetailsRepository = vendorPaymentDetailsRepository;
        this.vendorContractService = vendorContractService;
        this.employeeRepository = employeeRepository;
        this.idgenRepository = idgenRepository;
        this.idGenNameForPaymentNumberPath = idGenNameForPaymentNumberPath;
    }

    public VendorPaymentDetailsRequest create(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {
        validate(vendorPaymentDetailsRequest);

        Long userId = null;
        if (vendorPaymentDetailsRequest.getRequestInfo() != null
                && vendorPaymentDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsRequest.getVendorPaymentDetails()) {
            setAuditDetails(vendorPaymentDetails, userId);
            vendorPaymentDetails.setPaymentNo(generatePaymentNumber(vendorPaymentDetails.getTenantId(),
                    vendorPaymentDetailsRequest.getRequestInfo()));
            prepareDocuments(vendorPaymentDetails);
        }
        return vendorPaymentDetailsRepository.create(vendorPaymentDetailsRequest);
    }

    public VendorPaymentDetailsRequest update(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        Long userId = null;
        if (vendorPaymentDetailsRequest.getRequestInfo() != null
                && vendorPaymentDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsRequest.getVendorPaymentDetails()) {
            setAuditDetails(vendorPaymentDetails, userId);
            prepareDocuments(vendorPaymentDetails);
        }

        validateForUniquePaymentInfoInRequest(vendorPaymentDetailsRequest);
        validate(vendorPaymentDetailsRequest);

        return vendorPaymentDetailsRepository.update(vendorPaymentDetailsRequest);
    }

    public Pagination<VendorPaymentDetails> search(final VendorPaymentDetailsSearch vendorPaymentDetailsSearch) {

        return vendorPaymentDetailsRepository.search(vendorPaymentDetailsSearch);
    }

    private void validateForUniquePaymentInfoInRequest(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        final List<String> paymentNumbersList = vendorPaymentDetailsRequest.getVendorPaymentDetails().stream()
                .map(VendorPaymentDetails::getPaymentNo).collect(Collectors.toList());

        if (paymentNumbersList.size() != paymentNumbersList.stream().distinct().count())
            throw new CustomException("Payment No", "Duplicate paymentNo in given Vendor Payment Details:");
    }

    private void prepareDocuments(final VendorPaymentDetails vendorPaymentDetail) {
        if (vendorPaymentDetail.getDocuments() != null) {
            final List<Document> documentList = vendorPaymentDetail.getDocuments().stream()
                    .filter(record -> record.getFileStoreId() != null).collect(Collectors.toList());

            vendorPaymentDetail.setDocuments(documentList);
        }
        vendorPaymentDetail.getDocuments().forEach(document -> setDocumentDetails(document, vendorPaymentDetail));
    }

    private void setDocumentDetails(final Document document, final VendorPaymentDetails vendorPaymentDetails) {
        document.setId(UUID.randomUUID().toString().replace("-", ""));
        document.setTenantId(vendorPaymentDetails.getTenantId());
        document.setRefCode(vendorPaymentDetails.getPaymentNo());
        document.setAuditDetails(vendorPaymentDetails.getAuditDetails());
    }

    private void validate(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        EmployeeResponse employeeResponse;
        Pagination<VendorContract> vendorContractPage;
        for (final VendorPaymentDetails vendorPaymentDetail : vendorPaymentDetailsRequest.getVendorPaymentDetails()) {

            // Validate for vendor contract
            if (vendorPaymentDetail.getVendorContract() != null
                    && (vendorPaymentDetail.getVendorContract().getContractNo() == null
                            || vendorPaymentDetail.getVendorContract().getContractNo().isEmpty()))
                throw new CustomException("Vehicle Contract", "Vehicle Contract Number required ");

            if (vendorPaymentDetail.getVendorContract() != null
                    && vendorPaymentDetail.getVendorContract().getContractNo() != null) {

                vendorContractPage = getVendorContracts(vendorPaymentDetail);

                if (vendorContractPage == null || vendorContractPage.getPagedData() == null
                        || vendorContractPage.getPagedData().isEmpty())
                    throw new CustomException("Vehicle Contract", "Vehicle Contract Number required "
                            + vendorPaymentDetail.getVendorContract().getContractNo());
                else
                    vendorPaymentDetail.setVendorContract(vendorContractPage.getPagedData().get(0));
            }

            // Validate for employee
            if (vendorPaymentDetail.getEmployee() != null && (vendorPaymentDetail.getEmployee().getCode() == null
                    || vendorPaymentDetail.getEmployee().getCode().isEmpty()))
                throw new CustomException("Employee", "Employee code required" + vendorPaymentDetail.getPaymentNo());

            if (vendorPaymentDetail.getEmployee() != null && vendorPaymentDetail.getEmployee().getCode() != null) {

                employeeResponse = employeeRepository.getEmployeeByCode(vendorPaymentDetail.getEmployee().getCode(),
                        vendorPaymentDetail.getTenantId(), vendorPaymentDetailsRequest.getRequestInfo());

                if (employeeResponse == null || employeeResponse.getEmployees() == null
                        || employeeResponse.getEmployees().isEmpty())
                    throw new CustomException("Employee",
                            "Given Employee is invalid: " + vendorPaymentDetail.getEmployee().getCode());
                else
                    vendorPaymentDetail.setEmployee(employeeResponse.getEmployees().get(0));
            }
        }

    }

    private Pagination<VendorContract> getVendorContracts(final VendorPaymentDetails vendorPaymentDetail) {
        final VendorContractSearch vendorContractSearch = new VendorContractSearch();
        vendorContractSearch.setTenantId(vendorPaymentDetail.getTenantId());
        vendorContractSearch.setContractNo(vendorPaymentDetail.getVendorContract().getContractNo());

        return vendorContractService.search(vendorContractSearch);
    }

    private String generatePaymentNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForPaymentNumberPath);
    }

    private void setAuditDetails(final VendorPaymentDetails contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getPaymentNo() || contract.getPaymentNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }
}
