package Utility.SettlementFile;

public interface XmlNode {

    static final String LINE_ITEM_TEXT = "LineItemText";
    static final String REFERENCE = "Reference";
    static final String TIME_STAMP_1 = "Timestamp";
    static final String AMOUNT = "Amount";
    static final String TOTAL_VALUE = "TotalValue";
    static final String DOCUMENT_DATE = "DocumentDate";
    static final String AMOUNT_PAID = "AmountPaid";
    static final String FISCAL_YEAR = "FiscalYear";
    static final String TIME_STAMP_2 = "TimeStamp";
    static final String PAYMENT_DOCUMENT_DATE = "PaymentDocumentDate";
    static final String VALUE_DATE = "ValueDate";
    static final String STATUS = "status";
    static final String SAPST_CHEQUE_STATUS = "C04";
    static final String APRFND_CHEQUE_STATUS = "C06";
    static final String SAPST_GIRO_STATUS = "G01";
    static final String APRFND_GIRO_STATUS = "G02";
    static final String REMARKS = "Remarks";
    static final String CHEQUE_REMARKS = "Cheque Cleared";
    static final String GIRO_REMARKS = "GIRO Cleared";
    String RECORD_TYPE = "RecordType";
    String COMPANY_CODE = "CompanyCode";
    String CURRENCY = "Currency";
    String EXCHANGE_RATE = "ExchangeRate";

    String HEADER_TEXT = "HeaderText";
    String POSTING_DATE = "PostingDate";
    String TOTAL_RECORDS = "TotalRecords";
    String PDF_NAME = "PDFName";
    String LINE_ITEM = "LineItem";
    String ACCOUNT_TYPE = "AcctType";
    String GLA_ACCOUNT = "GLAccount";
    String DEBIT_CREDIT_INDICATOR = "DrCrIndicat";
    String TAX_CODE = "TaxCode";
    String TAX_AMOUNT = "TaxAmount";
    String FUND = "Fund";
    String FUND_CENTER = "FundCenter";
    String COST_CENTER = "CostCenter";
    String INTERNAL_ORDER = "InternalOrder";
    String TAX_AUTOMATICALLY = "TaxAutomatically";

}
