package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.swm.web.contract.Employee;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorPaymentDetails {

    @NotNull
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId;

    @Length(min = 1, max = 128)
    @JsonProperty("paymentNo")
    private String paymentNo;

    @NotNull
    @JsonProperty("vendorContract")
    private VendorContract vendorContract;

    @NotNull
    @JsonProperty("vendorInvoiceAmount")
    private Double vendorInvoiceAmount;

    @JsonProperty("documents")
    private List<Document> documents;

    @NotNull
    @Size(min = 1, max = 256)
    @JsonProperty("invoiceNo")
    private String invoiceNo;

    @NotNull
    @JsonProperty("fromDate")
    private Long fromDate;

    @NotNull
    @JsonProperty("toDate")
    private Long toDate;

    @JsonProperty("employee")
    private Employee employee;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
}
