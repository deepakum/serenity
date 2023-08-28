package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_refund_initiation_pageobject extends PageObject {

    public static final Target BUSINESS_SYSTEM_OPTIONS = Target.the("Business system dropdown box")
            .locatedBy("css:#request_busSys");
    public static final Target RECEIPT_ID_INPUTBOX = Target.the("Receipt ID inputbox")
            .locatedBy("css:#request_payEventId");
    
    public static final Target ERROR_MSG = Target.the("Error message text")
            .locatedBy("css:#ERRMSG-TEXT-SPAN");
    public static final Target SEARCH_BUTTON = Target.the("Refund initiation Search button")
            .locatedBy("//input[@value='Search']");

    public static final Target TRANSACTION_DETAILS(int tableIndex) {
        return Target.the("Transaction details")
                .locatedBy("//table[@id='refundsToSelect']/tbody[{0}]/tr/td").of(String.valueOf(tableIndex));
    }
    public static final Target TOTAL_SALE_ITEMS(int row){
        return Target.the("Total sale items inputbox")
                .locatedBy("css:#totSaleItems_{0}").of(String.valueOf(row));
    }

    public static final Target TOTAL_SALE_QUANTITY(int row){
        return Target.the("Total sale quantity input box")
                .locatedBy("css:#noOfSaleItem_{0}").of(String.valueOf(row));
    }
    public static final Target REFUND_AMOUNT_INPUTBOX(int row){
        return Target.the("Total sale quantity input box")
                .locatedBy("css:#refAmt_{0}").of(String.valueOf(row));
    }

    public static final Target REFUND_METHOD_DROPDOWN_BOX = Target.the("Refund method dropdown box")
            .locatedBy("css:#refundMethod");

    public static final Target CITY_INPUT_BOX = Target.the("city input box")
            .locatedBy("css:#city");

    public static final Target POSTAL_CODE_INPUT_BOX = Target.the("postal code input box")
            .locatedBy("css:#postal");

    public static final Target BLOCK_INPUT_BOX = Target.the("block input box")
            .locatedBy("css:#block");

    public static final Target COUNTRY_LIST_BOX = Target.the("country list box")
            .locatedBy("css:#country");

    public static final Target STREET_INPUT_BOX = Target.the("street input box")
            .locatedBy("css:#street");

    public static final Target GIRO_ACCOUNT_INPUTBOX = Target.the("Giro account number inputbox")
            .locatedBy("css:#giroAcct");

    public static final Target BANK_NAME_DROPDOWN = Target.the("Bank name dropdown list")
            .locatedBy("css:#bankName");

    public static final Target REFUND_REASON = Target.the("Refund reason dropdown")
            .locatedBy("#refundRsn");

    public static final Target SUBMIT_BUTTON = Target.the("Submit button")
            .locatedBy("css:#submitBtn");

    public static final Target PERSON_ID_TEXTBOX = Target.the("Person id textbox")
            .locatedBy("css:#PER_ID");

    public static final Target PERSON_ID_SEARCH_BUTTON = Target.the("Person ID search button")
            .locatedBy("css:#BU_PERSRCH");

    public static final Target ACCOUNT_ID_TEXTBOX = Target.the("account id textbox")
            .locatedBy("css:#ACCT_ID");

    public static final Target ACCOUNT_ID_SEARCH_BUTTON = Target.the("Account ID search button")
            .locatedBy("css:#BU_ACCTSRCH");

    public static final Target TABLE_HEADER = Target.the("Case table header")
            .locatedBy("css:#headerTableHead>tr>th>span");

    public static final Target CASE_ID_INPUT_BOX = Target.the("Case id input box")
            .locatedBy("css:#CASE_ID");
    public static final Target CONTACT_NUMBER_INPUTBOX = Target.the("contact number input box")
            .locatedBy("css:#refundeePhone");

}
