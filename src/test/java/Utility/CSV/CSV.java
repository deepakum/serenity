package Utility.CSV;

public interface CSV {

    String paymentModeType = "card";
    String cardBrand = "Visa";
    String cardFunding = "Credit";
    String cardCountry = "US";
    String paymentMode = "stripe_credit_card";
    String businessIDPrefix = "LTA-CCRS-";
    Double fee = 5.9;
    String BT_DATE_FORMAT = "yyyy-MM-dd";
    String BT_SETTLEMENT_FILE_DATE_FORMAT = "yyyyMMdd";

    String ST_SETTLEMENT_FILE_DATE_FORMAT = "ddMMyyy";
    String BT_SETTLEMENT_FILE_NAME = "STL-BT-";
    String ST_SETTLEMENT_FILE_NAME = "StripeSettlement_";
    String PAYPAL_SETTLEMENT_FILE_NAME = "STL-";
}
