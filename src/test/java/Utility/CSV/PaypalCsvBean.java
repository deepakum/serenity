package Utility.CSV;

import com.opencsv.bean.CsvBindByName;

public class PaypalCsvBean {

    @CsvBindByName(column = "CH")
    private String ch;
    @CsvBindByName(column = "Transaction ID")
    private String TransactionId;
    @CsvBindByName(column = "Invoice ID")
    private String InvoiceID;
    @CsvBindByName(column = "PayPal Reference ID")
    private String PaypalReferenceID;
    @CsvBindByName(column = "PayPal Reference ID Type")
    private String PaypalReferenceIdType;
    @CsvBindByName(column = "Transaction Event Code")
    private String TransactionEventCode;
    @CsvBindByName(column = "Transaction Initiation Date")
    private String TransactionInitiationDate;
    @CsvBindByName(column = "Transaction Completion Date")
    private String TransactionCompletionDate;
    @CsvBindByName(column = "Transaction Debit or Credit")
    private String TransactionDaebitorCredit;
    @CsvBindByName(column = "Gross Transaction Amount")
    private String GrossTransactionAmount;
    @CsvBindByName(column = "Gross Transaction Currency")
    private String GrossTransactionCurrency;
    @CsvBindByName(column = "Fee Debit or Credit")
    private String FeeDebitOrCredit;
    @CsvBindByName(column = "Fee Amount")
    private int FeeAmount;
    @CsvBindByName(column = "ProcessorSettlementResponseText")
    private String ProcessorSettlementResponseText;
    @CsvBindByName(column = "Consumer ID")
    private String ConsumerID;
    @CsvBindByName(column = "Payment Tracking ID")
    private String PaymentTrackingID;
    @CsvBindByName(column = "Store ID")
    private String StoreID;
    @CsvBindByName(column = "Bank Reference ID")
    private String BankReferenceID;
    @CsvBindByName(column = "Credit Transactional Fee")
    private String CreditCardTransactionalFee;
    @CsvBindByName(column = "Credit Promotional Fee")
    private String CreditCardPromotionalFee;
    @CsvBindByName(column = "Credit Term")
    private String CreditTerm;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getPaypalReferenceID() {
        return PaypalReferenceID;
    }

    public void setPaypalReferenceID(String paypalReferenceID) {
        PaypalReferenceID = paypalReferenceID;
    }

    public String getPaypalReferenceIdType() {
        return PaypalReferenceIdType;
    }

    public void setPaypalReferenceIdType(String paypalReferenceIdType) {
        PaypalReferenceIdType = paypalReferenceIdType;
    }

    public String getTransactionEventCode() {
        return TransactionEventCode;
    }

    public void setTransactionEventCode(String transactionEventCode) {
        TransactionEventCode = transactionEventCode;
    }

    public String getTransactionInitiationDate() {
        return TransactionInitiationDate;
    }

    public void setTransactionInitiationDate(String transactionInitiationDate) {
        TransactionInitiationDate = transactionInitiationDate;
    }

    public String getTransactionCompletionDate() {
        return TransactionCompletionDate;
    }

    public void setTransactionCompletionDate(String transactionCompletionDate) {
        TransactionCompletionDate = transactionCompletionDate;
    }

    public String getTransactionDaebitorCredit() {
        return TransactionDaebitorCredit;
    }

    public void setTransactionDaebitorCredit(String transactionDaebitorCredit) {
        TransactionDaebitorCredit = transactionDaebitorCredit;
    }

    public String getGrossTransactionAmount() {
        return GrossTransactionAmount;
    }

    public void setGrossTransactionAmount(String grossTransactionAmount) {
        GrossTransactionAmount = grossTransactionAmount;
    }

    public String getGrossTransactionCurrency() {
        return GrossTransactionCurrency;
    }

    public void setGrossTransactionCurrency(String grossTransactionCurrency) {
        GrossTransactionCurrency = grossTransactionCurrency;
    }

    public String getFeeDebitOrCredit() {
        return FeeDebitOrCredit;
    }

    public void setFeeDebitOrCredit(String feeDebitOrCredit) {
        FeeDebitOrCredit = feeDebitOrCredit;
    }

    public int getFeeAmount() {
        return FeeAmount;
    }

    public void setFeeAmount(int feeAmount) {
        this.FeeAmount = feeAmount;
    }

    public String getProcessorSettlementResponseText() {
        return ProcessorSettlementResponseText;
    }

    public void setProcessorSettlementResponseText(String processorSettlementResponseText) {
        ProcessorSettlementResponseText = processorSettlementResponseText;
    }

    public String getConsumerID() {
        return ConsumerID;
    }

    public void setConsumerID(String consumerID) {
        ConsumerID = consumerID;
    }
}
