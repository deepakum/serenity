package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_evms_payment_pageobject extends PageObject {

    public static final Target NOTICE_NO_INPUT_BOX = Target.the("Notice number input box")
            .locatedBy("css:#outstndingGroup_noticeNo");
    public static final Target SEARCH_BUTTON = Target.the("Notice search button")
            .locatedBy("css:#searchButton");
    public static final Target EVMS_CHECK_BOX = Target.the("EVMS check box")
            .locatedBy("css:#selectBoxEVMS_0");
    public static final Target EVMS_BILLING_RADIO_BUTTON = Target.the("EVMS billing radio button")
            .locatedBy("css:#castAddressRdo_0");
    public static final Target PROCEED_TO_PAY_BUTTON = Target.the("Proceed to pay button")
            .locatedBy("css:#proceedBtnEVMS");
    public static final Target ORGANISATION_NAME_INPUT_BOX = Target.the("Organisation name input box")
            .locatedBy("css:#offName");
    public static final Target EMAIL_ADDRESS_INPUT_BOX = Target.the("Email address input box")
            .locatedBy("css:#receiptEmailId");
}
