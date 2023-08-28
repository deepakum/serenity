package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class OneM_ents_credit_card_pageobject extends PageObject {

    public static final Target NAME_ON_CARD_INPUTBOX = Target.the("name on card")
            .locatedBy("#name");
    public static final Target CARD_NUMBER_INPUTBOX = Target.the("card number inputbox")
            .locatedBy("#cardNo");
    public static final Target CVV_INPUTBOX = Target.the("password inputbox")
            .locatedBy("#cvv");
    public static final Target EXPIRY_MONTH_DROPDOWN = Target.the("expiry month dropdown")
            .locatedBy("#selExpiryMonth");
    public static final Target EXPIRY_YEAR_DROPDOWN = Target.the("expiry year dropdown")
            .locatedBy("#selectYearId");
    public static final Target SUBMIT_BUTTON = Target.the("submit button")
            .locatedBy("#btnSubmit");

}
