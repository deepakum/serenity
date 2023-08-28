package Utility.CSV;

import com.opencsv.bean.CsvBindByName;

public class BrainTreeCsvBean {

    @CsvBindByName(column = "TransactionId")
    private String TransactionId;
    @CsvBindByName(column = "OrderId")
    private String OrderId;
    @CsvBindByName(column = "TransactionDateTime")
    private String TransactionDateTime;
    @CsvBindByName(column = "TransactionAmount")
    private String TransactionAmount;
    @CsvBindByName(column = "Status")
    private String Status;
    @CsvBindByName(column = "Type")
    private String Type;
    @CsvBindByName(column = "PaytenderType")
    private String PaytenderType;
    @CsvBindByName(column = "SettlementBatchId")
    private String SettlementBatchId;
    @CsvBindByName(column = "GatewayRejectionReason")
    private String GatewayRejectionReason;
    @CsvBindByName(column = "ProcessorAuthorizationCode")
    private String ProcessorAuthorizationCode;
    @CsvBindByName(column = "ProcessorResponseCode")
    private String ProcessorResponseCode;
    @CsvBindByName(column = "ProcessorResponseText")
    private String ProcessorResponseText;
    @CsvBindByName(column = "ProcessorSettlementResponseCode")
    private String ProcessorSettlementResponseCode;
    @CsvBindByName(column = "ProcessorSettlementResponseText")
    private String ProcessorSettlementResponseText;


    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getTransactionDateTime() {
        return TransactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        TransactionDateTime = transactionDateTime;
    }

    public String getTransactionAmount() {
        return TransactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        TransactionAmount = transactionAmount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPaytenderType() {
        return PaytenderType;
    }

    public void setPaytenderType(String paytenderType) {
        PaytenderType = paytenderType;
    }

    public String getSettlementBatchId() {
        return SettlementBatchId;
    }

    public void setSettlementBatchId(String settlementBatchId) {
        SettlementBatchId = settlementBatchId;
    }

    public String getGatewayRejectionReason() {
        return GatewayRejectionReason;
    }

    public void setGatewayRejectionReason(String gatewayRejectionReason) {
        GatewayRejectionReason = gatewayRejectionReason;
    }

    public String getProcessorAuthorizationCode() {
        return ProcessorAuthorizationCode;
    }

    public void setProcessorAuthorizationCode(String processorAuthorizationCode) {
        ProcessorAuthorizationCode = processorAuthorizationCode;
    }

    public String getProcessorResponseCode() {
        return ProcessorResponseCode;
    }

    public void setProcessorResponseCode(String processorResponseCode) {
        ProcessorResponseCode = processorResponseCode;
    }

    public String getProcessorResponseText() {
        return ProcessorResponseText;
    }

    public void setProcessorResponseText(String processorResponseText) {
        ProcessorResponseText = processorResponseText;
    }

    public String getProcessorSettlementResponseCode() {
        return ProcessorSettlementResponseCode;
    }

    public void setProcessorSettlementResponseCode(String processorSettlementResponseCode) {
        ProcessorSettlementResponseCode = processorSettlementResponseCode;
    }

    public String getProcessorSettlementResponseText() {
        return ProcessorSettlementResponseText;
    }

    public void setProcessorSettlementResponseText(String processorSettlementResponseText) {
        ProcessorSettlementResponseText = processorSettlementResponseText;
    }
}
