package Utility.others;

public interface Message {
    public static final String TENDER_SOURCE_WRONG_LOCATION = "The Deposit Control and the Tender Source have different Tender Source Types.\n" +
            "\n" +
            "The Tender Source and the Deposit Control associated with\n" +
            "a Tender Control must both have the same Tender Source Type.\n" +
            "\n" +
            "Either the Tender Source or the Deposit Control must be changed\n" +
            "on the Tender Control.";
    String PURCHASE_MESSAGE = "No Sale Item or Notice or Notice Instalment or Application List is selected.\n" +
            "Please select an item for payment.";
    String PARTIAL_PAYMENT_ALERT = "Partial payment is not allowed";
    String PAYMENT_SUMMARY_ERROR_POPUP = "No Sale Item or Notice or Notice Instalment or Application List is selected.\n" +
            "Please select an item for payment.";
}
