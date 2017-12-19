package org.egov.swm.domain.model;

import java.util.List;

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
public class Vendor {

    @Length(min = 1, max = 256)
    @JsonProperty("vendorNo")
    private String vendorNo = null;

    @NotNull
    @Length(min = 1, max = 256)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 1, max = 256)
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @Size(min = 1)
    @JsonProperty("servicedLocations")
    private List<Boundary> servicedLocations = null;

    @NotNull
    @JsonProperty("registrationNo")
    private String registrationNo = null;

    @Valid
    @JsonProperty("supplier")
    private Supplier supplier = null;

    @JsonProperty("agreementDocument")
    private Document agreementDocument;

    @NotNull
    @Size(min = 1)
    @JsonProperty("servicesOffered")
    private List<SwmProcess> servicesOffered = null;

    @Length(min = 10, max = 500)
    @JsonProperty("details")
    private String details = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
