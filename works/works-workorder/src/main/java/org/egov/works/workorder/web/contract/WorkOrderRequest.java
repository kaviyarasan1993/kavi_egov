package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Wor kOrder items are used in case of search results, also multiple  Work Order item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Wor kOrder items are used in case of search results, also multiple  Work Order item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class WorkOrderRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("workOrders")
    private List<WorkOrder> workOrders = null;

    public WorkOrderRequest requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    /**
     * Get requestInfo
     *
     * @return requestInfo
     **/
    @ApiModelProperty(value = "")

    @Valid

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public WorkOrderRequest workOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
        return this;
    }

    public WorkOrderRequest addWorkOrdersItem(WorkOrder workOrdersItem) {
        if (this.workOrders == null) {
            this.workOrders = new ArrayList<WorkOrder>();
        }
        this.workOrders.add(workOrdersItem);
        return this;
    }

    /**
     * Used for create and update only
     *
     * @return workOrders
     **/
    @ApiModelProperty(value = "Used for create and update only")

    @Valid

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrderRequest workOrderRequest = (WorkOrderRequest) o;
        return Objects.equals(this.requestInfo, workOrderRequest.requestInfo) &&
                Objects.equals(this.workOrders, workOrderRequest.workOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestInfo, workOrders);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WorkOrderRequest {\n");

        sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
        sb.append("    workOrders: ").append(toIndentedString(workOrders)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

