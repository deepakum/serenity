package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_payment_event_pageobject extends PageObject {
    
    public static final Target EXT_REFERENCE_ID_TEXTBOX = Target.the("reference id textbox")
            .locatedBy("//*[@id='PAY_TNDR$EXT_REFERENCE_ID']");
    public static final Target TENDER_CONTROL_CONTEXT_MENU = Target.the("tender control context menu")
            .locatedBy("css:#IM_TndrCntrl_tCtlCntx");
    public static final Target TENDERS_MENU_TAB = Target.the("tenders menu tab")
            .locatedBy("css:#TENDERS_TLBL");
    public static final Target GO_TO_TC_CONTROL_MENU = Target.the("go to tc control menu")
            .locatedBy("css:#CI_CONTEXTTENDERCONTROL_subMenuItem1x0");
    public static final Target TC_SEARCH_MENU = Target.the("tc search menu")
            .locatedBy("css:#CI0000000104");
    public static final Target MATCH_VALUE_INPUT_BOX = Target.the("match value input box")
            .locatedBy("//input[@id='PAY:0$MATCH_VAL']");
    public static final Target PAY_LIST_ACCOUNT_CONTEXT_ICON = Target.the("pay list account context icon")
            .locatedBy("//img[@id='IM_PAY:0$payList_acctCtx']");
    public static final Target GO_TO_BILL_MENU = Target.the("go to bill menu")
            .locatedBy("css:#CI_CONTEXTACCOUNT_subMenuItem1x9");
    public static final Target GO_TO_BILL_SEARCH_MENU = Target.the("go to bill search menu")
            .locatedBy("css:#CI0000000175");
    public static final Target PAYMENT_EVENT_CHARACTER_TYPE(int row){
        return Target.the("payment event character type")
                .locatedBy("//select[@id='PY_TNDR_CHAR:{0}$CHAR_TYPE_CD']")
                .of(String.valueOf(row));

    }
    public static final Target PAYMENT_EVENT_CHARACTER_VALUE(int row){
        return Target.the("payment event character value")
                .locatedBy("//input[@id='PY_TNDR_CHAR:{0}$ADHOC_CHAR_VAL']")
                .of(String.valueOf(row));

    }

    public static final Target CHARACTER_TYPE_LIST = Target.the("list of character types")
            .locatedBy("#dataTableBody>tr");
}
