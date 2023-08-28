package Utility.BatchJob;

public interface BatchControl {

    String DEPOSIT_CONTROL = "CM-CDTPP";
    String STRIPE = "CM-STRPE";
    String UPLOAD_BANK_STATEMENT = "CM-UPBNK";
    String BANK_RECONCILIATION = "CM-BNKRN";
    String QR_CODE_BANK_RECONCILIATION = "CM-SGQRN";
    String LOAD_NEW_VIOLATION = "CM-LNVIN";
    String BRAINTREE = "CM-BRNTR";
    String CHEQUE = "CM-CHQST";
    String RSF = "CM-ARSF";
    String SSF = "CM-ASSF";
    String WOSF = "CM-AWOSF";
    String CM_ABTMP = "CM-ABTMP";
    String CM_GIROB = "CM-GIROB";
    String PPSSF = "CM-APSSF";
    String BCSF = "CM-ABCSF";
    String GCSF = "CM-AGCSF";
    String CTSF = "CM-CTSF";
    String NETS = "CM-NETSP";
    String CREDIT_CARD = "CM-UOBP";
    String VENDOR_JIRO = "CM-SAPVG";
    String VENDOR_CHEQUE = "CM-SAPVF";
    String SAP_CAHANNEL = "CMSAPST";
    String CLEARANCE = "CM-SAPCL";
    String PAYPAL_SETTLEMENT_JOB = "CM-PYPAL";
    String ENETS_DEBIT_SETTLEMENT_JOB = "CM-NETSD";
    String ACK3 = "CM-GACK3";
    String ACK2 = "CM-GACK2";
    String ACK1 = "CM-PACK1";
    String INVRC = "CM-INOCL";
    String SAPSR = "CM-SAPSR";
    String ABTAR = "CM-ABTAR";
    String SARSF = "CM-SAPSF";
    String GL_ASSIGN = "GLASSIGN";
    String CREATE_GL_DOWNLOAD = "GLS";
    String GENERATE_GL_EXTRACT = "CM-GLEXT";
    String EXTRACT_GL_FOR_SAP = "CM-ABTGL";
 }
