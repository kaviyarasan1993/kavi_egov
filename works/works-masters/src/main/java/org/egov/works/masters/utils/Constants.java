package org.egov.works.masters.utils;

/**
 * Created by ramki on 3/11/17.
 */
public class Constants {
    //Messages
    public static final String KEY_SCHEDULERCATEGORY_CODE_MANDATORY="WMS.SCHEDULECATEGORY.CODE.MANDATORY";
    public static final String MESSAGE_SCHEDULERCATEGORY_CODE_MANDATORY="ScheduleCategory code is mandatory: ";

    public static final String KEY_SCHEDULERCATEGORY_CODE_INVALID="WMS.SCHEDULECATEGORY.CODE.INVALID";
    public static final String MESSAGE_SCHEDULERCATEGORY_CODE_INVALID="Given ScheduleCategory is invalid: ";

    public static final String KEY_UOM_CODE_INVALID="WMS.UOM.CODE.INVALID";
    public static final String MESSAGE_UOM_CODE_INVALID="Given UOM is invalid: ";

    public static final String KEY_UOM_CODE_MANDATORY="WMS.UOM.CODE.MANDATORY";
    public static final String MESSAGE_UOM_CODE_MANDATORY="UOM code is invalid: ";

    public static final String KEY_SOR_DATES_SHOULDNOT_OVERLAP="WMS.SOR.DATES.SHOULDNOT.OVERLAP";
    public static final String MESSAGE_SOR_DATES_SHOULDNOT_OVERLAP="SOR Dates should not be overlaped";

    public static final String KEY_SOR_CODE_EXISTS="WMS.SOR.CODE.EXISTS";
    public static final String MESSAGE_SOR_CODE_EXISTS="Given SOR already exists: ";

    public static final String KEY_SOR_EITHER_SOR_OR_MARKETRATE_ISREQUIRED="WMS.SOR.EITHER.SORORMARKET.REQUIRED";
    public static final String MESSAGE_SOR_EITHER_SOR_OR_MARKETRATE_ISREQUIRED="Either SOR or Market rate is mandatory";

    public static final String KEY_SOR_MARKETRATE_DATES_SHOULDNOT_OVERLAP="WMS.SOR.MARKETRATE.DATES.SHOULDNOT.OVERLAP";
    public static final String MESSAGE_SOR_MARKETRATE_DATES_SHOULDNOT_OVERLAP="SOR Market rate dates should not be overlaped";

    public static final String KEY_SOR_RATE_SHOULDBE_GREATERTHANZERO="WMS.SOR.RATE.SHOULDBE.GREATERTHANZERO";
    public static final String MESSAGE_SOR_RATE_SHOULDBE_GREATERTHANZERO="SOR rate should be greater than zero: ";

    public static final String KEY_SOR_CODE_UPDATE_NOTALLOWED="WMS.SOR.CODE.UPDATE.NOTALLOWED";
    public static final String MESSAGE_SOR_CODE_UPDATE_NOTALLOWED="You are trying to update code, not allowed";

    public static final String KEY_SOR_KEY_INVALID="WMS.SOR.KEY.INVALID";
    public static final String MESSAGE_SOR_KEY_INVALID="Given SOR key is not valid: ";

    public static final String KEY_SOR_BOTH_RATES_SHOULDNOT_PRESENT="WMS.SOR.BOTH.RATES.SHOULDNOT.PRESENT";
    public static final String MESSAGE_SOR_BOTH_RATES_SHOULDNOT_PRESENT="SOR cannot exist with both the rates ";

    public static final String KEY_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES="WMS.SOR.SHOULDNOT.MULTIPLE.OPEN.ENDEDDATES";
    public static final String MESSAGE_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES="SOR cannot have multiple open ended dates ";

    public static final String KEY_SOR_THEREARE_DUPLICATE_CODES="WMS.SOR.THEREARE.DUPLICATE.CODES";
    public static final String MESSAGE_SOR_THEREARE_DUPLICATE_CODES="There are duplicate combincations of code and schedule category in given list ";

    public static final String KEY_SOR_THEREARE_DE="WMS.SOR.THEREARE.DE";
    public static final String MESSAGE_SOR_THEREARE_DE="There are Estimates with this SOR: ";

    public static final String KEY_ESTIMATETEMPLATE_CODE_EXISTS="WMS.ESTIMATETEMPLATE.CODE.EXISTS";
    public static final String MESSAGE_ESTIMATETEMPLATE_CODE_EXISTS="Given Estimate Template already exists: ";

    public static final String KEY_ESTIMATETEMPLATE_MIN_ONE_ETA_REQUIRED="WMS.ESTIMATETEMPLATE.MIN_ONE.ETA.REQUIRED";
    public static final String MESSAGE_ESTIMATETEMPLATE_MIN_ONE_ETA_REQUIRED="Minumum one Activity is required";

    public static final String KEY_ESTIMATETEMPLATE_UOM_REQUIRED="WMS.ESTIMATETEMPLATE.UOM.REQUIRED";
    public static final String MESSAGE_ESTIMATETEMPLATE_UOM_REQUIRED="UOM is required for activity";

    public static final String KEY_ESTIMATETEMPLATE_NONSOR_DESCRIPTION_REQUIRED="WMS.ESTIMATETEMPLATE.NONSOR.DESCRIPTION.REQUIRED";
    public static final String MESSAGE_ESTIMATETEMPLATE_NONSOR_DESCRIPTION_REQUIRED="Description is required for Non SOR";

    public static final String KEY_ESTIMATETEMPLATE_EITHER_SOR_OR_NONSOR_ISREQUIRED="WMS.ESTIMATETEMPLATE.EITHER.SOR.OR.NONSOR.ISREQUIRED";
    public static final String MESSAGE_ESTIMATETEMPLATE_EITHER_SOR_OR_NONSOR_ISREQUIRED="Either SOR or Non SOR is mandatory for activity";

    public static final String KEY_ESTIMATETEMPLATE_BOTH_SORANDNONSOR_SHOULDNOT_PRESENT="WMS.ESTIMATETEMPLATE.BOTH.SORANDNONSOR.SHOULDNOT.PRESENT";
    public static final String MESSAGE_ESTIMATETEMPLATE_BOTH_SORANDNONSOR_SHOULDNOT_PRESENT="Activity should not have both SOR and Non SOR ";

    public static final String KEY_ESTIMATETEMPLATE_THEREARE_DUPLICATE_CODES="WMS.ESTIMATETEMPLATE.THEREARE.DUPLICATE.CODES";
    public static final String MESSAGE_ESTIMATETEMPLATE_THEREARE_DUPLICATE_CODES="There are duplicate of code in given list ";

    public static final String KEY_ESTIMATETEMPLATE_DUPLICATE_SOR_NOTALLOWED="WMS.ESTIMATETEMPLATE.DUPLICATE.SOR.NOTALLOWED";
    public static final String MESSAGE_ESTIMATETEMPLATE_DUPLICATE_SOR_NOTALLOWED="Duplicate SORs not allowed for activity in a Template";

    public static final String KEY_TYPEOFWORK_CODE_INVALID="WMS.TYPEOFWORK.CODE.INVALID";
    public static final String MESSAGE_TYPEOFWORK_CODE_INVALID="Given Type Of Work is invalid: ";

    public static final String KEY_SUBTYPEOFWORK_CODE_INVALID="WMS.SUBTYPEOFWORK.CODE.INVALID";
    public static final String MESSAGE_SUBTYPEOFWORK_CODE_INVALID="Given Type Of Work is invalid: ";

    public static final String KEY_SCHEDULEOFRATE_ID_INVALID="WMS.SCHEDULEOFRATE.ID.INVALID";
    public static final String MESSAGE_SCHEDULEOFRATE_ID_INVALID="Given Schedule Of Rate is invalid: ";

    public static final String KEY_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID="WMS.CONTRACTOR.CONTRACTORCLASS.CLASS.INVALID";
    public static final String MESSAGE_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID="Given ContractorClass is invalid: ";
    
    public static final String KEY_CONTRACTOR_BANK_CODE_INVALID="WMS.CONTRACTOR.BANK.CODE.INVALID";
    public static final String MESSAGE_CONTRACTOR_BANK_CODE_INVALID="Given Bank is invalid: ";
    
    public static final String KEY_CONTRACTOR_ACCOUNTCODE_GLCODE_INVALID="WMS.CONTRACTOR.ACCOUNTCODE.GLCODE.INVALID";
    public static final String MESSAGE_CONTRACTOR_ACCOUNTCODE_GLCODE_INVALID="Given Accountcode is invalid: ";
    
    public static final String KEY_CONTRACTOR_CODE_INVALID="WMS.CONTRACTOR.CODE.INVALID";
    public static final String MESSAGE_CONTRACTOR_CODE_INVALID="Given Code already exist: ";
    
    public static final String KEY_CONTRACTOR_CODE_MODIFY="WMS.CONTRACTOR.CODE.MODIFY";
    public static final String MESSAGE_CONTRACTOR_CODE_MODIFY="Cannot Modify Code: ";
    
    public static final String KEY_CONTRACTOR_STATUS_INVALID="WMS.CONTRACTOR.STATUS.INVALID";
    public static final String MESSAGE_CONTRACTOR_STATUS_INVALID="Given Status is invalid: ";
    
    public static final String KEY_CONTRACTOR_BANKACCOUNTNUMBER_INVALID="WMS.CONTRACTOR.BANKACCOUNTNUMBER.INVALID";
    public static final String MESSAGE_CONTRACTOR_BANKACCOUNTNUMBER_INVALID="BankAccountNumber cannot be null. Please provide correct value";
    
    public static final String KEY_CONTRACTOR_ACCOUNTCODE_INVALID="WMS.CONTRACTOR.ACCOUNTCODE.INVALID";
    public static final String MESSAGE_CONTRACTOR_ACCOUNTCODE_INVALID="AccountCode cannot be null. Please provide correct value";
    
    public static final String KEY_CONTRACTOR_IFSCCODE_INVALID="WMS.CONTRACTOR.IFSCCODE.INVALID";
    public static final String MESSAGE_CONTRACTOR_IFSCCODE_INVALID="IfscCode cannot be null. Please provide correct value";

    public static final String KEY_MILESTONETEMPLATE_MIN_ONE_ETA_REQUIRED="WMS.MILESTONETEMPLATE.MIN_ONE.ETA.REQUIRED";
    public static final String MESSAGE_MILESTONETEMPLATE_MIN_ONE_ETA_REQUIRED="Minumum one Activity is required";

    public static final String KEY_MILESTONETEMPLATE_THEREARE_DUPLICATE_CODES="WMS.MILESTONETEMPLATE.THEREARE.DUPLICATE.CODES";
    public static final String MESSAGE_MILESTONETEMPLATE_THEREARE_DUPLICATE_CODES="There are duplicate of code in given list ";

    public static final String KEY_MILESTONETEMPLATE_CODE_EXISTS="WMS.MILESTONETEMPLATE.CODE.EXISTS";
    public static final String MESSAGE_MILESTONETEMPLATE_CODE_EXISTS="Given Estimate Template already exists: ";
}
