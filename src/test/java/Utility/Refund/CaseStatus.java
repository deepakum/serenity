package Utility.Refund;

public interface CaseStatus {
    String level_1 = "Pending Level 1 Approver";
    String CO_APPROVAL_STATUS = "Pending Level 2 Approver";
    String DO_APPROVAL_STATUS =  "Sent to Refund Channel (Braintree/eNETS/Stripe)";
    String AO_APPROVAL_STATUS = "Refund Channel (SAP)";
    String VENDOR_FILE = "File extracted for SAP";
    String SAP = "AP request processed by SAP";
    String REFUNDED = "refunded";
}
