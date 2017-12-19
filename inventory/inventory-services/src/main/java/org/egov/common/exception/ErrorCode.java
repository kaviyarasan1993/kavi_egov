package org.egov.common.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mani
 * List of Domain Error codes with message and detailed description
 * Donot auto format this file
 */
public enum ErrorCode {
    KAFKA_TIMEOUT_ERROR(
            "org.egov.service.kafka.timeout",
            "time out while waiting for kafka",
            "Some required service is down. Please contact Administrator"),
    INTERNAL_SERVER_ERROR(
            "org.egov.service.internal.error",
            "Internal Server error",
            "Some required service is down. Please contact Administrator"),
    SQL_ERROR(
            "org.egov.service.sql.error",
            "Error in the sql statements ",
            "Sql statment execution failed . Please contact Administrator"),
    NON_UNIQUE_VALUE(
            "non.unique.value",
            "the field {0} must be unique in the system",
            "The  value  {1} for the field {0} already exists in the system. Please provide different value"),
    REPEATED_VALUE(
            "repeated.value",
            "the field {0} must be unique ",
            "The  value  {1} for the field {0} {2}  repeated"),
    NULL_VALUE("null.value",
            "the field {0} must be not be null",
            "The  value  {1} for the field {0} not allowed in the system. Please provide correct value"),
    MANDATORY_VALUE_MISSING("mandatory.value.missing",
            "the field {0} must be not be null or empty",
            "the field {0} is Mandatory .It cannot be not be null or empty.Please provide correct value"),
    NOT_NULL("NotNull",
            "the field {0} must be not be null",
            "The  value  {1} for the field {0} not allowed in the system. Please provide correct value"),
    INVALID_REF_VALUE("invalid.ref.value",
            "the field {0} should have a valid value which exists in the system. ",
            "The  value  {1} for the field {0} does exist in system. Please provide correct value"),
    DATE_LE_CURRENTDATE("date.should.be.le.currentdate",
            "the field {0} should be Less than or Equal to Current Date. ",
            "The  value  {1} for the field {0} should be Less than or Equal to Current Date."),
    DATE_GE_CURRENTDATE("date.should.be.ge.currentdate",
            "the field {0} should be Greater than or Equal to Current Date. ",
            "The  value  {1} for the field {0} should be Greater than or Equal to Current Date."),
    DATE1_LE_DATE2("date1.should.be.le.date2",
            "the date {0} should be less than or Equal to {1} Date. ",
            "The  value  {1} for the field {0} should be Greater than or Equal to Current Date."),
    DATE1_GT_DATE2("date2.should.be.gt.date1",
            "the date {0} should be greater than {1} Date. ",
            "The  value  {2} for the field {0} should be Greater than value  {3} for the field {1} ."),
    DATE1_GE_DATE2("date2.should.be.ge.date1",
            "the date {0} should be greater than {1} Date. ",
            "The  value  {2} for the field {0} should be Greater than value  {3} for the field {1} ."),
    ADVAMT_GE_TOTAMT("advamt.ge.totamt",
            "advance amount should be less than or equal to total amount {0} ",
            "advance amount should be less than or equal to total amount {0}."),
    ADVPCT_GE_HUN("advpct.ge.hun",
            "advance percentage should be less than or equal to 100 {0} ",
            "advance percentage should be less than or equal to 100 {0}."),
    MANDATORY_BASED_ON("value2.manadatory.if.value1",
            "the field {0} is mandatory if  {1}  is selected. ",
            "The field {0} should be provided if the {1} is selected {2}."),
    FIN_YEAR_NOT_EXIST("fin.year.not.exist",
            "Financial Year does not exist for the date {0} ",
            "Financial Year does not exist for the date {0}."),
    CITY_CODE_NOT_AVAILABLE("city.code.not.available",
            "City code is not available for tenant {0} ",
            "City code is not available for tenant  {0}."),
    UPDATE_NOT_ALLOWED("update.not.allowed",
            "Updation of {0} is not allowed for status {1}",
            "The {0} of {2} is at status {1}. Updation of this is not allowed"),
    STORE_NOT_EXIST("inv.store.not.exist",
            "Store doesn't exist {0} ",
            "Store doesn't exist {0}."),
    SUPPLIER_NOT_EXIST("inv.supplier.not.exist",
            "Supplier doesn't exist {0} ",
            "Supplier doesn't exist {0}."),
    RECEIVING_STORE_NOT_EXIST("rcv.store.not.exist",
            "Receiving Store is Required {0} ",
            "Receiving Store is Required {0}."),
    MATERIAL_NAME_NOT_EXIST("mtr.name.not.exist",
            "Material Name is Required {0} ",
            "Material Name is Required {0}."),
    UOM_CODE_NOT_EXIST("uom.code.not.exist",
            "Uom is Required  {0} ",
            "Uom is Required  {0}."),
    FIELD_NOT_EXIST("field.code.not.exist",
            " {0} doesn't exists at row {1}",
            " {0} doesn't exists at row {1}."),
    RCVED_QTY_NOT_EXIST("rcved.qty.not.exist",
            "Received Quantity is Required  {0} ",
            "Received Quantity is Required  {0}."),
    RCVED_QTY_LS_ODRQTY("rcved.qty.ls.zero",
            "Received quantity should be less than order quantity at row {0} ",
            "Received quantity should be less than order quantity at row {0}."),
    RCVED_QTY_LS_PORCVEDATY("rcved.qty.ls.porcvdat",
            "Received quantity should be not be greater than receieved quantity of purchase order at row {0} ",
            "Received quantity should be not be greater than receieved quantity of purchase order at row {0}."),
    RCVED_QTY_GT_ZERO("rcved.qty.gt.zero",
            "Received Quantity Should Be Greater Than Zero {0} ",
            "Received Quantity Should Be Greater Than Zero {0}."),
    UNIT_RATE_GT_ZERO("unit.rate.gt.zero",
            "Unit Rate Should Be Greater Than Zero {0} ",
            "Unit Rate Should Be Greater Than Zero {0}."),
    UNIT_RATE_NOT_EXIST("unit.rate.not.exist",
            "Unit Rate Is Required  {0} ",
            "Unit Rate Is Required  {0}."),
    LOT_NO_NOT_EXIST("lot.no.not.exist",
            "Lot Number Is  Required  {0} ",
            "Lot Number Is Required  {0}."),
    EXP_DATE_NOT_EXIST("exp.date.not.exist",
            "Expiry Date Is Required {0} ",
            "Expiry Date Is Required {0}."),
    RCPT_DATE_LE_TODAY("rcpt.date.le.today",
            "Receipt Date Should Be Less Than Or Equal To Today Date {0} ",
            "Receipt Date Should Be Less Than Or Equal To Today Date {0}."),
    EXP_DATE_GE_TODAY("exp.date.ge.today",
            "Expiry Date Should Be Greater Than Or Equal To Today Date {0} ",
            "Expiry Date Should Be Greater Than Or Equal To Today Date {0}."),
    PO_DATE_LE_TODAY("po.date.le.today",
            "Purchase Order Date Should Be Less Than Or Equal To Today Date {0} ",
            "Purchase Order Date Should Be Less Than Or Equal To Today Date {0}."),
    EXP_DATE_GE_PODATE("exp.date.ge.podate",
            "Expected delivery date should be greater than or eqaul to po date {0} ",
            "Expected delivery date should be greater than or eqaul to po date {0}."),
    ORDQTY_LE_INDQTY("ordqty.le.indqty",
            "order quantity should be less than or equal to indent quantity {0} ",
            "order quantity should be less than or equal to indent quantity {0}."),
    QUANTITY_GT_ZERO("quantity.greaterthan.zero",
            "the field {0} should have a value greater than zero. ",
            "the value {1} for the field {0} is not valid, it should be greater than zero."),
    QUANTITY_GT_ZERO_TENDER("quantity.greaterthan.zero",
            "the field {0} should have a value greater than zero incase of tender. ",
            "the value {1} for the field {0} is not valid, it should be greater than zero incase of tender."),
    QUANTITY1_LTE_QUANTITY2("quantity1.lessthanorequalto.quantity2",
            "the quantity {0} should be less than or equal to quantity {1}",
            "The value {2} for the field {0} should be less than or equal to the value {3} for the field {1}"),
    COMBINATION_EXISTS("inv.combination.exists",
            "{0} {1} and {2} {3} combination already exists",
            "{0} {1} and {2} {3} combination already exists"),
    COMBINATION_EXISTS_ROW("inv.combination.row.exists",
            "{0} {1} and {2} {3} combination already exists at row {4}",
            "{0} {1} and {2} {3} combination already exists at row {4}"),
    DATE1_LE_DATE2ROW("date1row.should.be.le.date2",
            "{0} should be less than {1} at row {2}",
            "{0} should be less than {1} at row {2}"),
    DATE1_GT_DATE2ROW("date2row.should.be.gt.date1",
            "{0} should be greater than {1} at row {2}",
            "{0} should be greater than {1} at row {2}."),
    DATE1_GE_DATE2ROW("date2row.should.be.ge.date1",
            "{0} should be greater than equal to {1} at row {2} ",
            "{0} should be greater than equal to {1} at row {2} "),
    MATCH_TWO_FIELDS("match.two.fields",
            "{0} doesnt match {1} at row {2}",
            "{0} doesnt match {1} at row {2}"),
    MANDATORY_VALUE_MISSINGROW("mandatoryrow.value.missing",
            "the field {0} must be not be null or empty at row {1}",
            "the field {0} is Mandatory at row {1}.It cannot be not be null or empty.Please provide correct value"),
    QTY_GTR_ROW("quantity.greater.zero",
            "the field {0} must be greater than zero at row {1}",
            "the field {0} must be greater than zero at row {1}"),
    QTY_GTR_SCND_ROW("quantity.greater.other",
            "the field {0} must be greater than {1} at row {1}",
            "the field {0} must be greater than {1} at row {1}"),
    QTY_LE_SCND_ROW("quantity.lesser.other",
            "the field {0} must be less than or equal to {1} at row {2}",
            "the field {0} must be less than or equal to at row {2}"),
    FIELD_DOESNT_MATCH("field.doesnt.match",
            "field {0} doesnt match with {1} field",
            "field {0} doesnt match with {1} field"),
    RATE_CONTRACT("rate.contract.required",
            "rate contract required {0}",
            "rate contract required {0}"),
    MAT_DETAIL("mat.detail.required",
            "Material Detail required {0}",
            "Material Detail required {0}"),
    ALLOW_SCRAP_MATERIALS("scrap.material.allowed",
            "In case of write off or scrap,only scrapable items are allowed", "when issue purpose is {0} allow only"
            + " scrapable materials"),
    CODE_ALREADY_EXISTS("code.already.exist",
            "{0} code {1} already exists ",
            "{0} code {1} already exists"),
    OBJECT_NOT_FOUND("object.not.found",
            "{0} {1} not found for {2}",
            "{0} {1} not found for {2}"),
    OBJECT_NOT_FOUND_ROW("object.not.found",
            "{0} {1} not found at row {2}",
            "{0} {1} not found at row {2}"),
    OBJECT_NOT_FOUND_COMBINATION("object.combination.found",
            "{0} not found for {1}, {2}",
            "{0} not found for {1}, {2}"),
    DOESNT_MATCH("doesnt.match.object",
            "{1} {0} doesnt match with the existing {1} {2}",
            "{1} {0} doesnt match with the existing {1} {2}"),
    CATGRY_MATCH("doesnt.match.catgry",
            "Material {0} and Uom {1} combination Not Exist In The System {2}",
            "Material {0} and Uom {1} combination Not Exist In The System {2}");


    private final String code;
    private final String message;
    private final String description;
    private static final Map<String, ErrorCode> errorMap = new HashMap<String, ErrorCode>();

    static {
        for (ErrorCode error : ErrorCode.values()) {
            errorMap.put(error.code, error);
        }
    }

    ErrorCode(final String code, final String message, final String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public static ErrorCode getError(String code) {
        return errorMap.get(code);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDescription() {
        return this.description;
    }

}
