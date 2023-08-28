package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_sale_item_display_pageobject extends PageObject {

    public static final Target AVAILABLE_TILL_DATE = Target.the("available till date")
            .locatedBy("//td[@orafield='detailsGroup/availableTo']");
    public static final Target AVAILABLE_FROM_DATE = Target.the("available till date")
            .locatedBy("//td[@orafield='detailsGroup/availableFrom']");
    public static final Target DUPLICATE_BUTTON = Target.the("duplicate button")
            .locatedBy("//input[@oramdlabel='DUP_LBL']");


}
