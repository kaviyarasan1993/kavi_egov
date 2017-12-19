package org.egov.swm.web.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.VehicleMaintenance;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class VehicleMaintenanceRequest {
    @Valid
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = new RequestInfo();
    @Valid
    private List<VehicleMaintenance> vehicleMaintenances = new ArrayList<>();
}