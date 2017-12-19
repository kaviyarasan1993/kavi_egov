package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds the basic data for a Work Order
 */
@ApiModel(description = "An Object that holds the basic data for a Work Order")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-04T07:18:54.555Z")

public class WorkOrder {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptance")
    private LetterOfAcceptance letterOfAcceptance = null;

    @JsonProperty("workOrderDetails")
    private List<WorkOrderDetail> workOrderDetails = null;

    @JsonProperty("workOrderDate")
    private Long workOrderDate = null;

    @JsonProperty("workOrderNumber")
    private String workOrderNumber = null;

    @JsonProperty("documentDetails")
    private List<DocumentDetail> documentDetails = null;

    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("status")
    private WorkOrderStatus status = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public WorkOrder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Work Order
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Work Order")

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorkOrder tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Work Order
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Work Order")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public WorkOrder letterOfAcceptance(LetterOfAcceptance letterOfAcceptance) {
        this.letterOfAcceptance = letterOfAcceptance;
        return this;
    }

    /**
     * LOA reference
     * @return letterOfAcceptance
     **/
    @ApiModelProperty(required = true, value = "LOA reference")
    @NotNull

//    @Valid

    public LetterOfAcceptance getLetterOfAcceptance() {
        return letterOfAcceptance;
    }

    public void setLetterOfAcceptance(LetterOfAcceptance letterOfAcceptance) {
        this.letterOfAcceptance = letterOfAcceptance;
    }

    public WorkOrder workOrderDetails(List<WorkOrderDetail> workOrderDetails) {
        this.workOrderDetails = workOrderDetails;
        return this;
    }

    public WorkOrder addWorkOrderDetailsItem(WorkOrderDetail workOrderDetailsItem) {
        if (this.workOrderDetails == null) {
            this.workOrderDetails = new ArrayList<WorkOrderDetail>();
        }
        this.workOrderDetails.add(workOrderDetailsItem);
        return this;
    }

    /**
     * Array of Work Order Details
     * @return workOrderDetails
     **/
    @ApiModelProperty(value = "Array of Work Order Details")

    @Valid
    @Size(min = 1)
    public List<WorkOrderDetail> getWorkOrderDetails() {
        return workOrderDetails;
    }

    public void setWorkOrderDetails(List<WorkOrderDetail> workOrderDetails) {
        this.workOrderDetails = workOrderDetails;
    }

    public WorkOrder workOrderDate(Long workOrderDate) {
        this.workOrderDate = workOrderDate;
        return this;
    }

    /**
     * Epoch time of Work Order Date. If the Work order is spillover then the Work Order Date is user entered. Otherwise it is
     * default to current date. This field is allowed to edit during rejected status or drafts for Spillover Work Order.
     * @return workOrderDate
     **/
    @ApiModelProperty(required = true, value = "Epoch time of Work Order Date. If the Work order is spillover then the Work Order Date is user entered. Otherwise it is default to current date. This field is allowed to edit during rejected status or drafts for Spillover Work Order.")
    @NotNull

    public Long getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(Long workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

    public WorkOrder workOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
        return this;
    }

    /**
     * Work Order Number. Auto generated. If the Work order is spillover then the Work Order number is user entered. Otherwise it
     * is auto generated. This field is allowed to edit during rejected status or drafts for Spillover Work Order.
     * @return workOrderNumber
     **/
    @ApiModelProperty(value = "Work Order Number. Auto generated. If the Work order is spillover then the Work Order number is user entered. Otherwise it is auto generated. This field is allowed to edit during rejected status or drafts for Spillover Work Order.")

    @Pattern(regexp = "[a-zA-Z0-9-/]+")
    @Size(max = 50)
    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public WorkOrder documentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
        return this;
    }

    public WorkOrder addDocumentDetailsItem(DocumentDetail documentDetailsItem) {
        if (this.documentDetails == null) {
            this.documentDetails = new ArrayList<DocumentDetail>();
        }
        this.documentDetails.add(documentDetailsItem);
        return this;
    }

    /**
     * Array of document details
     * @return documentDetails
     **/
    @ApiModelProperty(value = "Array of document details")

    @Valid

    public List<DocumentDetail> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
    }

    public WorkOrder workFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
        return this;
    }

    /**
     * Get workFlowDetails
     * @return workFlowDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public WorkFlowDetails getWorkFlowDetails() {
        return workFlowDetails;
    }

    public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
    }

    public WorkOrder auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }

    public WorkOrder status(WorkOrderStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     * @return status
     **/
    @ApiModelProperty(value = "")

    @Valid

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
    }

    public WorkOrder stateId(String stateId) {
        this.stateId = stateId;
        return this;
    }

    /**
     * State id of the workflow
     * @return stateId
     **/
    @ApiModelProperty(value = "State id of the workflow")

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public WorkOrder deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    /**
     * Boolean value to identify whether the object is deleted or not from UI.
     * @return deleted
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrder workOrder = (WorkOrder) o;
        return Objects.equals(this.id, workOrder.id) &&
                Objects.equals(this.tenantId, workOrder.tenantId) &&
                Objects.equals(this.letterOfAcceptance, workOrder.letterOfAcceptance) &&
                Objects.equals(this.workOrderDetails, workOrder.workOrderDetails) &&
                Objects.equals(this.workOrderDate, workOrder.workOrderDate) &&
                Objects.equals(this.workOrderNumber, workOrder.workOrderNumber) &&
                Objects.equals(this.documentDetails, workOrder.documentDetails) &&
                Objects.equals(this.workFlowDetails, workOrder.workFlowDetails) &&
                Objects.equals(this.auditDetails, workOrder.auditDetails) &&
                Objects.equals(this.status, workOrder.status) &&
                Objects.equals(this.stateId, workOrder.stateId) &&
                Objects.equals(this.deleted, workOrder.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, letterOfAcceptance, workOrderDetails, workOrderDate, workOrderNumber, documentDetails,
                workFlowDetails, auditDetails, status, stateId, deleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WorkOrder {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    letterOfAcceptance: ").append(toIndentedString(letterOfAcceptance)).append("\n");
        sb.append("    workOrderDetails: ").append(toIndentedString(workOrderDetails)).append("\n");
        sb.append("    workOrderDate: ").append(toIndentedString(workOrderDate)).append("\n");
        sb.append("    workOrderNumber: ").append(toIndentedString(workOrderNumber)).append("\n");
        sb.append("    documentDetails: ").append(toIndentedString(documentDetails)).append("\n");
        sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
