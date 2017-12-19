package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class VendorContract {

    @NotNull
    @Length(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("vendor")
    private Vendor vendor = null;

    @Size(min = 6, max = 128)
    @JsonProperty("contractNo")
    private String contractNo = null;

    @NotNull
    @JsonProperty("contractDate")
    private Long contractDate = null;

    @NotNull
    @JsonProperty("contractPeriodFrom")
    private Long contractPeriodFrom = null;

    @NotNull
    @JsonProperty("contractPeriodTo")
    private Long contractPeriodTo = null;

    @NotNull
    @JsonProperty("securityDeposit")
    private Double securityDeposit = null;

    @NotNull
    @JsonProperty("paymentAmount")
    private Double paymentAmount = null;

    @NotNull
    @JsonProperty("paymentTerms")
    private PaymentTerms paymentTerms = null;

    @Length(min = 0, max = 500)
    @JsonProperty("remarks")
    private String remarks = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
