package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class VehicleTripSheetDetails {

    @NotNull
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Size(min = 1, max = 256)
    @JsonProperty("tripNo")
    private String tripNo = null;

    @NotNull
    @JsonProperty("vehicle")
    private Vehicle vehicle = null;

    @NotNull
    @JsonProperty("route")
    private Route route = null;

    @NotNull
    @JsonProperty("tripStartDate")
    private Long tripStartDate = null;

    @NotNull
    @JsonProperty("tripEndDate")
    private Long tripEndDate = null;

    @JsonProperty("inTime")
    private Long inTime = null;

    @JsonProperty("outTime")
    private Long outTime = null;

    @JsonProperty("entryWeight")
    private Double entryWeight = null;

    @JsonProperty("exitWeight")
    private Double exitWeight = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
