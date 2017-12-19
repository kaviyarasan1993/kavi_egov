package org.egov.swm.web.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.SanitationStaffSchedule;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class SanitationStaffScheduleRequest {
    @Valid
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = new RequestInfo();
    @Valid
    private List<SanitationStaffSchedule> sanitationStaffSchedules = new ArrayList<>();
}