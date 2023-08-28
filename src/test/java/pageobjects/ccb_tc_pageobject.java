package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_tc_pageobject extends PageObject {

    public static final Target FINANCIAL_MENU_LIST(int index){
        return Target.the("financial menu option")
                .locatedBy("//li[@id='CI_FINANCIAL_subMenuItem1x{0}']")
                .of(String.valueOf(index));
    }

    public static final Target SAVE = Target.the("refresh button")
            .locatedBy("//input[@id='IM_SAVE']");

    public static final Target COUNTER = Target.the("Counter menu option")
            .locatedBy("//li[@id='CI_MAINMENU_topMenuItem0x0']");
    public static final Target COUNTER_PAYMENTS = Target.the("Counter payment menu option")
            .locatedBy("//li[@id='CM_COUNTER_PAYMENT_subMenuItem0x0']");
    public static final Target TENDER_SOURCE = Target.the("Tender source input box")
            .locatedBy("//input[@id='TNDR_SOURCE_CD']");
    public static final Target DEPOSIT_CONTROL_ID = Target.the("Deposit control ID")
            .locatedBy("//input[@id='DEP_CTL_ID']");
    public static final Target DEPOSIT_CONTROL_INFO = Target.the("Deposit control info")
            .locatedBy("#DEP_CTL_INFO");

    public static final Target TENDER_CONTROL_INFO = Target.the("Tender control info")
            .locatedBy("//span[@id='TNDR_CTL_INFO']");
    public static final Target CONFIRM_PAYMENT = Target.the("Confirm payment button")
            .locatedBy("#createChargeCONS");
    public static final Target TENDER_CONTROL_STATUS = Target.the("Tender control status")
            .locatedBy("//select[@id='TNDR_CTL_ST_FLG']");

    public static final Target TENDER_CONTROL_TABLE(int index){
        return Target.the("tender control table")
                .locatedBy("//tbody[@id='dataTableBody']/tr/td[{0}]").of(String.valueOf(index));
    }
    public static final Target ENDING_BALANCE = Target.the("Ending balance input box")
            .locatedBy("//input[@id='END_BAL:0$ENDING_AMT']");

    public static final Target BALANCE = Target.the("balance button")
            .locatedBy("//input[@id='BALANCE_SW']");
    public static final Target LOGGED_IN_USER = Target.the("Logged in name button")
            .locatedBy("#youAreLoggedInAsSpan");

    public static final Target LOGOUT = Target.the("logout button")
            .locatedBy("//li[@id='Logout']");
    public static final Target TENDER_CONTROL_MENU = Target.the("Tender control menu")
            .locatedBy("css:#TNDRCTRL_TLBL");
    public static final Target GOTO_TC_ICON = Target.the("Goto tender control icon")
            .locatedBy("//img[@id='IM_TNDR_CTL:0$Section1_goToBtn']");

    public static final Target TC_BY_INDEX(int row, int statusRow, int col){
        String index = String.valueOf((row - 1));
        return Target.the("search TC by index")
                .locatedBy("//*[@id='dataTableBody']/tr[{0}]/td[@id='TNDR_CTL|{1}|{2}']")
                .of(String.valueOf(row),String.valueOf(statusRow),String.valueOf(col));
    }

    public static final Target TENDEDER_CONTROL_LIST = Target.the("list of TC's")
            .locatedBy("//*[@id='dataTableBody']/tr");

    public static final Target HISTORY_GOBACK_BUTTON = Target.the("Go back history button")
            .locatedBy("css:#IM_GOBACK");

    public static final Target TENDER_TYPE = Target.the("Tender type dropdown list")
            .locatedBy("css:#tenderType_0");
    public static final Target TENDER_AMOUNT_INPUT_BOX = Target.the("tender amount input boxt")
            .locatedBy("css:#tenderAmount_0");
    public static final Target CHEQUE_STRING_INPUT_BOX = Target.the("Cheque string input box")
            .locatedBy("css:#checkNumber_0");
    public static final Target RECEIPT_ID = Target.the("Receipt ID textbox")
            .locatedBy("css:#paymentEventId");

    public static final Target TC_SEARCH_ICON = Target.the("TC search icon")
            .locatedBy("css:#IM_TNDR_CTL_ID");

    public static final Target TENDERS_MENU_LABEL = Target.the("Target menu label"
    ).locatedBy("css:#TENDERS_TLBL");

    public static final Target GET_MORE_BUTTON = Target.the("Get more button")
            .locatedBy("css:#getMore");
    public static final Target GO_TO_PAYMENT_EVENT_LABEL = Target.the("Go to payment event label")
            .locatedBy("//*[@id='IM_TNDR_CTL:0$Grid_goToLbl']");
    public static final Target GO_TO_PAYMENT_EVENT_LABEL(int index) {
        return Target.the("Go to payment event label")
                .locatedBy("//*[@id='IM_TNDR_CTL:{0}$Grid_goToLbl']")
                .of(String.valueOf(index));
    }
    public static final Target PAYMENT_EVENT_ID_INPUTBOX = Target.the("Payment event id input box")
            .locatedBy("css:#PAY_EVENT_ID");
    public static final Target ALL_USERS_CHECKBOX = Target.the("All users checkbox")
            .locatedBy("css:#ALL_OPERATORS_SW");
    public static final Target TENDER_SOURCE_SEARCH_ICON = Target.the("tender source search icon")
            .locatedBy("#IM_TNDR_SOURCE_CD");

    public static final Target TENDER_SOURCE_DESCRIPTION = Target.the("tender source description")
            .locatedBy("#DESCR");
}
