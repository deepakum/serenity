package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_dc_pageobject extends PageObject {

    public static final Target DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON = Target.the("Deposit control context menu button")
            .locatedBy("css:#IM_Section1_dcCntxt");

    public static final Target DEPOSIT_CONTROL_CONTEXT_MENU = Target.the("Deposit control context menu")
            .locatedBy("//div[@id='menuDiv_0']");

    public static final Target TC_ADD = Target.the("TC add")
            .locatedBy("css:#CI0000000123");
    public static final Target GO_TO_TC_MENU = Target.the("Go to tender control")
            .locatedBy("css:#CI_CONTEXTDEPOSITCONTROL_subMenuItem1x1");
    public static final Target GO_TO_DC = Target.the("Go to tender control")
            .locatedBy("css:#CI_CONTEXTDEPOSITCONTROL_subMenuItem0x0");
    public static final Target ENDING_BALANCE = Target.the("ending balance input box")
            .locatedBy("//input[@id='END_BALANCE']");

    public static final Target DC_CONTEXT_MENU(int index){
        return Target.the("DC context menu")
                .locatedBy("//li[@id='CI_CONTEXTDEPOSITCONTROL_subMenuItem1x{0}']")
                .of(String.valueOf(index));
    }
    public static final Target TC_ADD_MENU = Target.the("Tender control Add menu")
            .locatedBy("//li[@id='CI0000000105']");

    public static final Target DC_SEARCH_MENU = Target.the("DC search menu")
            .locatedBy("//li[@id='CI0000000190']");
    public static final Target TC_SEARCH_MENU = Target.the("Tender control search menu")
            .locatedBy("//li[@id='CI0000000014']");
    public static final Target SEARCH_DC = Target.the("Search dc button")
            .locatedBy("css:#IM_DEP_CTL_ID");
    public static final Target DC_NUMBER_TEXTBOX = Target.the("dc number textbox")
            .locatedBy("css:#DEP_CTL_ID");

    public static final Target DC_MAIN_MENU_TAB = Target.the("main menu label")
            .locatedBy("css:#MAIN_TLBL");

    public static final Target DC_TENDER_CONTROL_MENU_TAB = Target.the("Tender control menu tab")
            .locatedBy("css:#TNDRCTRL_TLBL");
    public static final Target TOTAL_TENDER_AMOUNT_TEXTBOX = Target.the("total tender amount textbox")
            .locatedBy("css:#TNDR_TOTAL_AMT");

    public static final Target DC_STATUS_INFO = Target.the("deposit control info")
            .locatedBy("#DEP_CTL_INFO");

    public static final Target EXPECTED_ENDING_BALACE = Target.the("Expected ending balance")
            .locatedBy("//span[@id='EXP_END_BLNC_AMT']");
    public static final Target DEP_CTL_STATUS_FLG = Target.the("deposit control status flag")
            .locatedBy("//select[@id='DEP_CTL_STATUS_FLG']");
}
