package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_bill_pageobject extends PageObject {
    public static final Target CHAR_TAB = Target.the("char tab")
            .locatedBy("#BILLCHAR_TLBL");
    public static final Target BILL_CHAR_LIST = Target.the("list of character values")
            .locatedBy("#dataTableBody>tr");
    public static final Target BILL_CHAR_VALUE(int row) {
        return Target.the("bill char value input box")
                .locatedBy("//input[@id='BILL_CHAR:{0}$ADHOC_CHAR_VAL']").of(String.valueOf(row));
    }
    public static final Target BILL_CHAR_TYPE(int row){
        return Target.the("bill char type")
                .locatedBy("//select[@id='BILL_CHAR:{0}$CHAR_TYPE_CD']").of(String.valueOf(row));
    }

}
