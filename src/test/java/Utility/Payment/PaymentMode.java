package Utility.Payment;

public interface PaymentMode {

    String STRIPE = "Credit Card (Stripe)";
    String CREDIT_CARD = "Credit Card";
    String BRAINTREE = "Credit Card (Braintree)";
    String COUNTER = "COUNTER";
    String CASH = "Cash";
    String CHEQUE = "Cheque";
    String ENETS_DEBIT = "eNETS Debit";
    String ENETS_CC = "Credit Card (ENETS)";
    String SGQR_PAYNOW = "SGQR PayNow";
    String PAYPAL = "PayPal";
    String GOOGLE_PAY = "Google Pay";
}
