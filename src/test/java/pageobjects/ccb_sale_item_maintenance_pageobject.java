package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_sale_item_maintenance_pageobject extends PageObject {

    public static final Target GST_PERCENTAGE_INPUTBOX = Target.the("gst percentage input box")
            .locatedBy("#gstPercent");
    public static final Target GST_AMOUNT_INPUTBOX = Target.the("gst amount input box")
            .locatedBy("#gstAmount");
    public static final Target TOTAL_AMOUNT_INPUTBOX = Target.the("total amount input box")
            .locatedBy("#totalAmount");
    public static final Target AVAILABLE_TILL_DATE = Target.the("available till date")
            .locatedBy("#detailsGroup_availableTo");
    public static final Target SAVE_BUTTON = Target.the("save date")
            .locatedBy("//input[@oramdlabel='SAVE_BTN_LBL']");
}
