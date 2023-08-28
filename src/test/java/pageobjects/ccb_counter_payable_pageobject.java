package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_counter_payable_pageobject extends PageObject {

    public static final Target VEHICLE_NUMBER_INPUTBOX = Target.the("Vehicle number inputbox")
            .locatedBy("#outstndingGroup_vehicleNo");
    public static final Target NOTICE_NUMBER_INPUTBOX = Target.the("Notice number inputbox")
            .locatedBy("#outstndingGroup_noticeNo");
    public static final Target SEARCH_BUTTON = Target.the("Search button")
            .locatedBy("#searchButton");
    public static final Target PAYABLE_NOTICE_ROW = Target.the("payable notice row")
            .locatedBy("#outstndingNotice");
    public static final Target NOTICE_CHECKBOX = Target.the("notice checkbox")
            .locatedBy("#selectBoxEVMS_0");
    public static final Target BILLING_ADDRESS_CHECKBOX = Target.the("billing address checkbox")
            .locatedBy("#castAddressRdo_0");
    public static final Target PROCEED_TO_PAY_BUTTON = Target.the("proceed to pay button")
            .locatedBy("#proceedBtnEVMS");
    public static final Target CAN_NUMBER_TEXTBOX = Target.the("CAN number textbox")
            .locatedBy("#canNo_0");
    public static final Target TRANSACTION_DATE_TIME_TEXTBOX = Target.the("Date and time textbox")
            .locatedBy("#txnDttm_0");
    public static final Target TERMINAL_RESPONSE_TEXTBOX = Target.the("Terminal response textbox")
            .locatedBy("#terminalResponse_0");
}
