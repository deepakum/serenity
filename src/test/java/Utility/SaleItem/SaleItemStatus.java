package Utility.SaleItem;

public interface SaleItemStatus {
    
    public static final String PAYMENT_SERVICE_CATEGORY = "Plans & Publications";
    public static final String PAYMENT_SERVICE_SUB_CATEGORY = "";
    public static final int QUANTITY = 2;
    String APPROVED = "Approved";
    String INITIATED = "Initiated";
    String BUSINESS_APPROVAL = "Pending For Business User Approval";
    String FINANCE_APPROVAL = "Pending For Finance User Approval";
    String REJECT_BY_BUSINESS_USER = "Rejected By Business User";
    String REJECT_BY_FINANCE_USER = "Rejected By Finance User";
    String SENT_WITH_QUERIES = "Sent with Queries";
}
