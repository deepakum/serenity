package tasks;

import Utility.others.PropertiesUtil;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import pageobjects.ccb_counter_payable_pageobject;
import pageobjects.ccb_evms_payment_pageobject;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isSelected;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class EVMS {

    static EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
    static String noticeNo = PropertiesUtil.getProperties("CCRS.NoticeNo");

    public static Performable searchNoticeAtOneMPortal(){
        return Task.where("Search notice number #noticeNo",
                Switch.toFrame("tabPage"),
                Switch.toFrame("zoneMapFrame_1"),
                Enter.keyValues(noticeNo).into(ccb_evms_payment_pageobject.NOTICE_NO_INPUT_BOX),
                Click.on(ccb_evms_payment_pageobject.SEARCH_BUTTON),
                WaitUntil.the(ccb_evms_payment_pageobject.EVMS_CHECK_BOX, isVisible()));
    }

    public static Performable selectNotice(){
        return Task.where("Select notice",
                Scroll.to(ccb_evms_payment_pageobject.EVMS_CHECK_BOX),
                Click.on(ccb_evms_payment_pageobject.EVMS_CHECK_BOX),
                WaitUntil.the(ccb_evms_payment_pageobject.EVMS_CHECK_BOX, isSelected()),
                Click.on(ccb_evms_payment_pageobject.EVMS_BILLING_RADIO_BUTTON),
                WaitUntil.the(ccb_evms_payment_pageobject.EVMS_BILLING_RADIO_BUTTON,isSelected()),
                WaitUntil.the(ccb_evms_payment_pageobject.ORGANISATION_NAME_INPUT_BOX,isVisible()));
    }

    public static Performable proceedToPay(){
        return Task.where("Proceed to pay",
                Scroll.to(ccb_evms_payment_pageobject.PROCEED_TO_PAY_BUTTON),
                Click.on(ccb_evms_payment_pageobject.PROCEED_TO_PAY_BUTTON),
                Switch.toDefaultContext(),
                Switch.toFrame("main"),
                Switch.toFrame("tabPage"),
                Switch.toFrame("zoneMapFrame_1"),
                WaitUntil.the(ccb_evms_payment_pageobject.EMAIL_ADDRESS_INPUT_BOX,isVisible()));
    }

    public static Performable searchPayableByNoticeNumber(){
        return Task.where("Search notice number #noticeNo",
                Switch.toFrame("tabPage").then(
                        Switch.toFrame("zoneMapFrame_1")),
                WaitUntil.the(ccb_counter_payable_pageobject.NOTICE_NUMBER_INPUTBOX,isVisible())
                        .then(Enter.keyValues(PropertiesUtil.getProperties("CCRS.NoticeNo")).into(ccb_counter_payable_pageobject.NOTICE_NUMBER_INPUTBOX)),
                Click.on(ccb_counter_payable_pageobject.SEARCH_BUTTON),
                WaitUntil.the(ccb_counter_payable_pageobject.PAYABLE_NOTICE_ROW, isVisible()),
                CheckCheckbox.of(ccb_counter_payable_pageobject.NOTICE_CHECKBOX).then(
                        CheckCheckbox.of(ccb_counter_payable_pageobject.BILLING_ADDRESS_CHECKBOX)
                ));
    }
    public static Performable searchPayableByVehicleNumber(){
        return Task.where("Search notice number #noticeNo",
                Switch.toFrame("tabPage").then(
                        Switch.toFrame("zoneMapFrame_1")),
                Enter.keyValues(noticeNo).into(ccb_counter_payable_pageobject.VEHICLE_NUMBER_INPUTBOX),
                Click.on(ccb_counter_payable_pageobject.SEARCH_BUTTON),
                WaitUntil.the(ccb_counter_payable_pageobject.PAYABLE_NOTICE_ROW, isVisible()),
                CheckCheckbox.of(ccb_counter_payable_pageobject.NOTICE_CHECKBOX).then(
                        CheckCheckbox.of(ccb_counter_payable_pageobject.BILLING_ADDRESS_CHECKBOX)
                ));
    }
}
