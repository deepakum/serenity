package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_dc_char_pageobject extends PageObject {

    public static final Target EXPECTED_BANK_IN_DATE = Target.the("Expected bank in date input box")
            .locatedBy("//input[@id='DEP_CTL_CHAR:2$ADHOC_CHAR_VAL']");

    public static final Target CHARACTERISTICS_MENU = Target.the("Tender control menu")
            .locatedBy("//*[@id='CHAR_TLBL']");
    public static final Target CHARACTERISTICS_TYPE_COL = Target.the("Characteristics type column")
            .locatedBy("//span[@id='L_CHAR_TYPE_CD']");
    public static final Target CHARACTERISTICS_HEADER = Target.the("Characteristics header menu")
            .locatedBy("//*[@id='headerTableHead']/tr/th");

    public static final Target LIST_OF_CHAR_LIST(int row, int col){
        return Target.the("Character values")
                .locatedBy("//*[@id='DEP_CTL_CHAR|{0}|{1}']")
                .of(String.valueOf(row),String.valueOf(col));
    }

    public static final Target CHARACTERISTIC_TYPE(int row){
        return Target.the("Characteristic type value")
                .locatedBy("//select[@id='DEP_CTL_CHAR:{0}$CHAR_TYPE_CD']").of(String.valueOf(row));
    }

    public static final Target PAYMENT_CHAR_TYPE(int row){
        return Target.the("Characteristic type value")
                .locatedBy("//select[@id='PYE_CHAR:{0}$CHAR_TYPE_CD']").of(String.valueOf(row));
    }

    public static final Target CHARACTERISTIC_VALUE(int row){
        return Target.the("Characteristic type value")
                .locatedBy("//input[@id='DEP_CTL_CHAR:{0}$CHAR_VAL']").of(String.valueOf(row));
    }

    public static final Target CHAR_DATE_VALUE(int row){
        return Target.the("Characteristic type value")
                .locatedBy("//input[@id='DEP_CTL_CHAR:{0}$ADHOC_CHAR_VAL']").of(String.valueOf(row));
    }

    public static final Target RECONCILIATION_STATUS_VALUE(int row){
        return Target.the("Characteristic type value")
                .locatedBy("//input[@id='DEP_CTL_CHAR:{0}$CHAR_VAL']").of(String.valueOf(row));
    }
    public static final Target CHAR_MERCHANT_REFERENCE_ID_TEXTBOX(int row){
        return Target.the("Merchant Reference id textbox")
                .locatedBy("//input[@id='PYE_CHAR:{0}$ADHOC_CHAR_VAL']").of(String.valueOf(row));
    }

    public static final Target CHARACTERISTIC_VALUE_DESC(int row){
        return Target.the("Characteristic value description")
                .locatedBy("css:#DEP_CTL_CHAR:{0}CHAR_VAL_DESCR").of(String.valueOf(row));
    }

    public static final Target CHAR_LIST = Target.the("list of character values")
                .locatedBy("#dataTableBody>tr");

    public static final Target DC_CONTEXT_MENU_BUTTON = Target.the("dc context menu button")
            .locatedBy("css:#IM_Main_dcCntxt");

    public static final Target GOTO_TC_MENU_LABEL = Target.the("Go to tender conrtrol menu label")
            .locatedBy("css:#CI_CONTEXTDEPOSITCONTROL_subMenuItem1x2");

    public static final Target TC_SEARCH_MENU = Target.the("Tender control search menu")
            .locatedBy("css:#CI0000000122");

    public static final Target TC_CHARACTERISTIC_TYPE(int row){
        return Target.the("Characteristic type value")
                .locatedBy("//select[@id='TNDR_CTL_CHAR:{0}$CHAR_TYPE_CD']").of(String.valueOf(row));
    }
    public static final Target COUNTER_LOCATION_VALUE(int row){
        return Target.the("location value")
                .locatedBy("//input[@id='TNDR_CTL_CHAR:{0}$CHAR_VAL']").of(String.valueOf(row));
    }
    public static final Target COUNTER_LOCATION_SEARCH_ICON(int row){
        return Target.the("location search icon")
                .locatedBy("//img[@id='IM_TNDR_CTL_CHAR:{0}$CHAR_VAL']").of(String.valueOf(row));
    }
}
