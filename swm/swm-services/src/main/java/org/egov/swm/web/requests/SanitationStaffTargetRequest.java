package org.egov.swm.web.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.SanitationStaffTarget;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class SanitationStaffTargetRequest {

    @Valid
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = new RequestInfo();
    @Valid
    private List<SanitationStaffTarget> sanitationStaffTargets = new ArrayList<>();
}