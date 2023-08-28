/**
 **************************************************************************
 *                 Confidentiality Information:
 * 
 * This module is the confidential and proprietary information of
 * Wipro Technologies; it is not to be copied, reproduced, or
 * transmitted in any form, by any means, in whole or in part,
 * nor is it to be used for any purpose other than that for which
 * it is expressly provided without the written permission of
 * Wipro Technologies.
 * 
 **************************************************************************
 * 					Program Description:
 * 
 * Project Name        : Centralised Collections and Refunds System
 * Object              : Constants
 * Description         : This contains all the constants       
 * 
 **************************************************************************
 * 					Change History:                                                                                                                       
 * Version   Date        Author             Reviewer/Tester    Remarks  
 **************************************************************************
 * 0.1       09-04-2018  JA321252                                Initial Version.
 **************************************************************************
 */
package Utility.ABT;

import java.math.BigInteger;

import com.ibm.icu.math.BigDecimal;

/**
 * @author JA321252
 *
 */
public class CmConstants 
{
	public static final String DUPLICATE_RECCORD = "001";
	public static final String OBUID_CHAR = "CM-OBU";
	public static final String MISSING_MANDATORY ="002";
	public static final String INVALID_DATA ="003";
	public static final String RECORD_ALREADY_EXIST ="004";
	public static final String RECORD_ALREADY_PAID ="005";
	public static final String INVALID_REFUNDAMT ="006";
	public static final String INVALID_REVENUE_CODE ="007";
	public static final String LIST_ELEMENT2 = "relatedcaseList";

	public static final String CURRENCY = "SGD";
	public static final BigDecimal BIG_DEC_ONE = new BigDecimal(1);
	public static final String todoType = "CM_OFFTD";
	public static final String ACCT_MGT_ERROR_TODO = "CM_OFFTD";
	public static final String Role = "F1_DFLT";
	public static final BigInteger MESSAGE_NO=new BigInteger("201");
	public static final BigInteger MessageCategoryId =new BigInteger("90000");
	public static final String StatusError = "Error";
	public static final BigInteger big2 =new BigInteger("1");
	public static final String LANGUAGE="ENG";
	public static final String SA_ACTIVE = "20";
	public static final String DUBLICATE ="001";



	// com.splwg.cm.domain.ChangeHandler
	public static final String CHAR_TYPE = "CM_TDCHA";
	public static final String TO_DO_TYPE = "CM_TCBAL";	
	public static final BigInteger MESSAGE_CATEGORY_ID = new BigInteger("90000");	
	public static final String TD_ROLE = "CIACTIV";		
	public static final BigInteger TD_MESSAGE_NO = new BigInteger("10005");		
	public static final String DRILL_KEY_VAL = "192";		
	public static final String SORT_KEY_VAL = "963";		
	public static final String USER = "SYSUSER";
	public static final BigInteger SEQUENCE = new BigInteger("1");

	public static final String MATCH_TYPE = "BILL ID";

	// Update Violation Info Into CCRS
	public static final String PAY_STATUS = "Success";


	//Refund Initiation Alog
	public static final String REFUND_APPROVAL_MATRIX_GROUP = "refundApprovalMatrix";
	public static final String REFUND_APPROVAL_MATRIX_LIST = "refundApprovalMatrixList";
	public static final String REFUND_APPROVAL_MATRIX_LIST_CHARGE_TYPE = "chargeType";
	public static final String REFUND_APPROVAL_MATRIX_LIST_REFUND_TYPE = "refundType";
	public static final String REFUND_APPROVAL_MATRIX_LIST_APPR_BUS_SYS = "approvalInBusinessSys";
	public static final String REFUND_APPROVAL_MATRIX_LIST_PAY_MODE = "payMethod";
	public static final String REFUND_APPROVAL_MATRIX_LIST_FINAL_APPROVAL = "finalApprovalLevelInCCRS";
	public static final String REFUND_APPROVAL_MATRIX_LIST_LEVEL1_APPROVAL = "level1Approver";
	public static final String REFUND_APPROVAL_MATRIX_LIST_LEVEL2_APPROVAL = "level2Approver";
	public static final String REFUND_APPROVAL_MATRIX_LIST_LEVEL3_APPROVAL = "level3Approver";

	//recon
	public static final String EMPTY_STRING = "";
	public static final String OPENING_FILE = "opening";
	public static final String CLOSING_FILE = "closing";
	public static final String READING_FILE = "reading";
	public static final String UNDER_SCORE = "_";
	public static final String QUESTION_MARK = "?";
	public static final String COMMA = ",";
	public static final String PIPE = "|";
	public static final BigDecimal MULTIPLIER = new BigDecimal("100");
	public static final String HYPEN = "-";
	public static final String DOT = ".";

	//GL Extract for SAP FS
	public static final String ROOT_ELEMENT = "CCRS";
	public static final String HEADER_ELEMENT = "HH";
	public static final String SOURCE_SYSTEM = "CCRS";
	public static final String TARGET_SYSTEM = "FS";
	public static final String BODY_HEADER_ELEMENT = "BH";
	public static final String BB_ELEMENT = "BB";
	public static final String COMPANY_CODE = "LTA";
	public static final String CURRENCY_CODE = "SGD";
	public static final String EXCHANGE_RATE = "1";
	public static final String BODY_LINE_ELEMENT = "BL";
	public static final String ACCOUNT_TYPE = "S";
	public static final String DEBIT_INDICATOR = "S";
	public static final String CREDIT_INDICATOR = "H";
	public static final String TAX_AUTOMATICALLY = "N";
	public static final String TRAILER_ELEMENT = "TT";
	public static final String DELIMITER = "|";
	public static final String BLANK = " ";
	public static final String XMLEXTENSTION = ".xml";
	public static final String CSEXTENSTION = ".cs";
	public static final String ERPCSEXTENSTION = ".CS";
	public static final String TAX_CODE = "SZ";


	//GL Extract for NFS
	public static final String CONTROL_HEADER = "999";
	public static final String CONTROL_HEADER_TRNS_ID = "JRNL";
	public static final String CONTROL_HEADER_FILE_ORIGIN = "MOC_LTA_CCRS";
	public static final String JOURNAL_HEADER = "000";
	public static final String BUSINESS_UNIT = "MOC";
	public static final String LEDGER_GROUP = "CASH";
	public static final String REVERSAL_CODE = "N";
	public static final String SOURCE = "MOT";
	public static final String ADJUSTING_ENTRY = "N";
	public static final String COST_CENTER_SCRTY_GRP = "MOC01_LTA";
	public static final String DESCR = "LTA CCRS Collection/Refund";
	public static final String JOURNAL_CONTROL = "001";
	public static final double JOURNAL_CONTROL_UNITS = 0.00;
	public static final String JOURNAL_LINE = "002";
	public static final String FUND_CODE = "FUND";
	public static final double STATISTICS_AMOUNT = 0.00;
	public static final String TRAILER_RECORD = "TRL";
	public static final String NEW_LINE_CHARATCER ="\n";

	//Tender Declaration
	public static final String MAKE_GOOD_CASHIER ="GOODCASHIER";
	public static final String MAKE_GOOD_LTA ="GOODLTA";
	public static final String EXCESS ="EXCESS";
	public static final String CLOSED ="CLOSED";
	public static final String EXCESS_REFUND ="EXCESSREFUND";

	//EVMS
	public static final String REFUND_CODE_GROUP = "revenueCodeMappingGroup";
	public static final String REFUND_CODE_LIST="revenueCodeMappingList";
	public static final String SA_NON_VEHICLE= "LTA-NVCL";
	public static final String SA_VEHICLE= "LTA-VHCL";
	public static final String SA_MISC= "LTA-MISC";
	//public static final String INVALID_REVENUE_CODE = "007";

	//WARRANT
	public static final String LIST_ELEMENTI = "relatedcaseDetails";


	//Write-Off
	public static final String WRITEOFF_APPROVAL_MATRIX_GROUP = "writeOffApprovalMatrix";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST = "writeOffApprovalMatrixList";
	public static final String WRITEOFFD_APPROVAL_MATRIX_LIST_BUSINESS_SYSTEM = "businessSystem";
	public static final String WRITEOFFD_APPROVAL_MATRIX_LIST_CHARGE_TYPE = "chargeType";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_WRITEOFF_TYPE = "writeOffType";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_INITIATOR = "initiator";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_APPR_BUS_SYS = "approvalInBusinessSys";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_PAY_MODE = "payMethod";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_FINAL_APPROVAL = "finalApprovalLevelInCCRS";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_REJECTION_APPROVAL = "rejectionApprovalLevelInCCRS";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_NOTIFICATION = "notification";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_REV_CODE = "revenueCode";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_ORG_UNIT = "busOrgFlag";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_COND_FLG = "conditionFlg";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_MIN_AMT = "writeOffMinAmt";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_MAX_AMT = "writeOffMaxAmt";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_LEVEL1_APPROVAL = "level1Approver";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_LEVEL2_APPROVAL = "level2Approver";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_LEVEL3_APPROVAL = "level3Approver";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_BUS_APPR = "bussinessApprover";
	public static final String WRITEOFF_APPROVAL_MATRIX_LIST_FIN_APPR = "financeApprover";

	//Installment
	public static final String DETAIL_ELEMENT = "DD";
	public static final String ROOT_ELEMENT1 = "XMLFragment";
	public static final String HEADER_ELEMENTI = "headerRecord";
	public static final String DETAIL_ELEMENTI = "detailRecord";
	public static final String TRAILER_ELEMENTI = "trailerRecord";

	public static final String RECORD_LOCKED ="004"; 

	public static final String INVALID_NOTICE_AMOUNT ="009";
	//ERP2 messages 
	public static final String MSG_ON_CASECHANGE001="Record is Extracted and sent to OBESP";
	//refund file from OBESP
	public static final String msgCaseLogApproved="Adjustment Freezed and settled";
	public static final String msgCaseLogRejected="Adjustements Deleted since Refund has to be rejected";
	public static final String depositControlIdCharType="CM-DEPID";
	public static final String charTypeNameTender="CM-OBEFL";
	public static final String refundAmtCharTypeCode="CM-RFAMT";

	//refund file to ERP2
	public static final String caseLogMsgInRefundFile="Record is Extracted and sent to ERP2 System";

	//Auto writeOff batch
	public static final  String caseTypeToGenerateWhenBatchRuns="CM-INIT";
	public static final String businessSysForAutoWriteOff="ERP2";
	public static final String approvalStsCharVal_NA="NA";
	public static final String approvalStsCharVal_PEND="PEND";
	
	// ABT Constants - Start Add
	public static final String EMAIL_CONT_TYPE = "PERSONAL EMAIL";
	public static final String MOB_CONT_TYPE = "CELLPHONE";
	public static final String FAX_CONT_TYPE = "FAX NUMBER";
	public static final String OTHR_CONT_TYPE = "SG MOBILE";
		
	public static final String ABT_DETAIL_REC_DATA_AREA = "CM-AbtRevenueDetailDA";

	public static final String ABT_DETAIL_REC_GROUP = "detailRecord";
	
	public static final String REVENUE_LIST_NODE = "revenueList";
	public static final String REV_DETAIL_GROUP = "revenueDetail";
	public static final String REV_CODE_NODE = "revenueCode";
	public static final String REV_CODE_DESCR_NODE = "revenueCodeDescription";
	public static final String TAX_CODE_NODE = "taxCode";
	public static final String AMOUNT_NODE = "amount";
	public static final String GST_AMT_NODE = "gstAmount";

	public static final String REF_NUMBER_NODE = "referenceNumber";
	public static final String TOT_AMT_NODE = "totalAmount";
	public static final String ROUND_DWN_AMT_NODE = "roundDownAmount";
	public static final String EMAIL_ADDR_NODE = "emailAddress";
	public static final String CONTACT_NBR_NODE = "contactNumber";
	public static final String ALT_CONT_NBR_NODE = "altContactNumber";
	public static final String FAX_NBR_NODE = "faxNumber";
	public static final String POSTAL_CODE_NODE = "postalCode";
	public static final String ADDR_TYPE_NODE = "addressType";
	public static final String BLCK_HOUSE_NO_NODE = "blockHouseNo";
	public static final String FLOOR_NO_NODE = "floorNo";
	public static final String UNIT_NO_NODE = "unitNo";
	public static final String STREET_NAME_NODE = "streetName";
	public static final String BUILDING_NAME_NODE = "buildingName";
	public static final String CITY_NODE = "city";
	public static final String STATE_NODE = "state";
	public static final String COUNTRY_NODE = "country";

	public static final String GIRO_REGISTRATION_BO = "CM-GIRORegistration";
	public static final String GIRO_REGISTRATION_BO_ACTIVE = "ACTIVE";
	
	public static final String PERSON_CHAR_TYPE_CD = "CM-PERID";
	public static final String CRN_CHAR_TYPE_CD = "CM-CREFN";
	public static final String TAX_CHAR_TYPE_CD = "CM-TAXCD";
	// ABT Constants - End Add

	// EPS2 Constants - Start Add
	public static final String SPACE = " ";
	public static final String SLASH = "/";
	public static final String ZERO = "0";
	public static final String DOUBLE_ZERO = "00";
	public static final String BREAKSPACE = "<br>";
	
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";

	public static final String YES = "Y";
	public static final String NO = "N";
	
	public static final String NOT_APPLICABLE = "N/A";
	
	public static final String COUNTRY_SG = "SG";
	public static final String LANGUAGE_EN = "EN";
	
	public static final String ENTITY_PERSON = "Person";
	public static final String ENTITY_INVOICE = "Invoice Id";
	public static final String ENTITY_INVOICE_FEES = "Invoice Fees Id";
	public static final String ENTITY_EPS_COLL_ID = "EPS Collection Id";
	public static final String ENTITY_FEAT_CONFIG = "Feature Configuration";
	public static final String ENTITY_FC_OPT_TYPE = "Option Type";
	public static final String ENTITY_BUS_OBJ = "Business Object";
	public static final String ENTITY_CHAR_TYPE = "Characteristic Type";
	public static final String ENTITY_BILL_FACTOR = "Bill Factor";
	public static final String ENTITY_ADJ_TYPE = "Adjustment Type";
	public static final String ENTITY_DIST_CD = "Distribution Code";
	public static final String ENTITY_LKUP_FIELD = "Lookup Field";
	public static final String ENTITY_LKUP_FLDVAL = "Lookup Field/Value";
	public static final String ENTITY_MERCHANT_PAY_REF = "Merchant Payment Reference";
	public static final String ENTITY_TD_TYPE = "To Do Type";
	public static final String ENTITY_SCRIPT = "Script";
	public static final String ENTITY_BANK = "Bank";
	public static final String ENTITY_ID_TYPE = "Identifier Type";
	public static final String ENTITY_FACT = "Fact Id";
	// EPS2 Constants - End Add
}