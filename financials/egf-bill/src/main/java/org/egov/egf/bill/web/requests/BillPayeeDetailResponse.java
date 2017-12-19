package org.egov.egf.bill.web.requests;

import java.util.List;

import lombok.Data;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.bill.web.contract.BillPayeeDetailContract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public @Data class BillPayeeDetailResponse {
    private ResponseInfo responseInfo;
    private List<BillPayeeDetailContract> billPayeeDetails;
    private PaginationContract page;
}