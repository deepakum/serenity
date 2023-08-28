package tasks;

import Utility.BatchJob.Braintree;
import Utility.Payment.EnetsCreditCard;
import Utility.Payment.Stripe;
import Utility.others.Helper;
import Utility.Payment.PaymentMode;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.OneM_Oph_pageobject;
import pageobjects.OneM_ents_credit_card_pageobject;
import pageobjects.OneM_fine_and_fees_pageobject;
import stepdefinitions.PortalStepDefinition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.serenitybdd.core.Serenity.getDriver;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

public class MakePayment implements Task {

    static Logger logger = LoggerFactory.getLogger(PortalStepDefinition.class);
    private String mode;

    public MakePayment(String mode) {
        this.mode = mode;
    }

    public static MakePayment using(String mode){
        return Instrumented.instanceOf(MakePayment.class).withProperties(mode);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info(()->String.format("Start purchasing item using %s payment method",mode));
        if (mode.equalsIgnoreCase(PaymentMode.STRIPE)) {
            String cardNumber = Stripe.CARD_NUMBER;
            String expiryDate = Stripe.EXPIRY_DATE;
            String cvc = Stripe.CVC;

            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(Scroll.to(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode)).then(
                    Click.on(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode))));
            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.CARD_ELEMENT, isVisible()));
            actor.attemptsTo(Click.on(OneM_fine_and_fees_pageobject.CARD_ELEMENT));
            actor.attemptsTo(Switch.toFrame(0));
            actor.attemptsTo(Enter.keyValues(cardNumber).into(OneM_fine_and_fees_pageobject.CARD_NUMBER_INPUTBOX),
                    Enter.keyValues(expiryDate).into(OneM_fine_and_fees_pageobject.EXPIRY_DATE_INPUTBOX).then(
                            Enter.keyValues(cvc).into(OneM_fine_and_fees_pageobject.CVC_INPUTBOX)));
            actor.attemptsTo(Switch.toDefaultContext());
            actor.attemptsTo(Click.on(OneM_fine_and_fees_pageobject.ST_SUBMIT_PAYMENT_BUTTON));

        } else if (mode.equalsIgnoreCase(PaymentMode.CREDIT_CARD)) {
            String cardNumber = Stripe.CARD_NUMBER;
            String expiryDate = Stripe.EXPIRY_DATE;
            String cvc = Stripe.CVC;

            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(Scroll.to(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode)).then(
                    Click.on(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode))));

            actor.attemptsTo(Enter.keyValues(cardNumber).into(OneM_fine_and_fees_pageobject.CARD_NUMBER_INPUTBOX),
                    Enter.keyValues(expiryDate).into(OneM_fine_and_fees_pageobject.EXPIRY_DATE_INPUTBOX).then(
                            Enter.keyValues(cvc).into(OneM_fine_and_fees_pageobject.CVC_INPUTBOX)));
            actor.attemptsTo(Switch.toDefaultContext());
            actor.attemptsTo(Click.on(OneM_fine_and_fees_pageobject.ST_SUBMIT_PAYMENT_BUTTON));

        } else if (mode.equalsIgnoreCase(PaymentMode.ENETS_CC)) {

            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(Scroll.to(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode)).then(
                    Click.on(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode))));
            Helper.customWait(2000);
            actor.attemptsTo(Enter.keyValues(EnetsCreditCard.NAME_ON_CARD).into(OneM_ents_credit_card_pageobject.NAME_ON_CARD_INPUTBOX));
            actor.attemptsTo(Enter.keyValues(EnetsCreditCard.CARD_NUMBER).into(OneM_ents_credit_card_pageobject.CARD_NUMBER_INPUTBOX));
            actor.attemptsTo(Enter.keyValues(EnetsCreditCard.CVV).into(OneM_ents_credit_card_pageobject.CVV_INPUTBOX));
            actor.attemptsTo(SelectFromOptions.byVisibleText(EnetsCreditCard.EXP_MONTH).from(OneM_ents_credit_card_pageobject.EXPIRY_MONTH_DROPDOWN));
            actor.attemptsTo(SelectFromOptions.byVisibleText(EnetsCreditCard.EXP_YEAR).from(OneM_ents_credit_card_pageobject.EXPIRY_YEAR_DROPDOWN));
            actor.attemptsTo(Click.on(OneM_ents_credit_card_pageobject.SUBMIT_BUTTON));

        } else if (mode.equalsIgnoreCase(PaymentMode.BRAINTREE)) {
            String cardNumber = Braintree.CARD_NUMBER;
            String expiryDate = Braintree.EXPIRY_DATE;
            String cvc = Braintree.CVC;

            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(Scroll.to(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode).waitingForNoMoreThan(Duration.ofMillis(5000))).then(
                    Click.on(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode))));
            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.BRAINTREE_CARD_FORM, isVisible()));
            actor.attemptsTo(Click.on(OneM_fine_and_fees_pageobject.BRAINTREE_CARD_FORM));
            actor.attemptsTo(Switch.toFrame("braintree-hosted-field-number"));
            actor.attemptsTo(Enter.keyValues(cardNumber).into(OneM_fine_and_fees_pageobject.BRAINTREE_CARD_NUMBER_INPUT_BOX));
            actor.attemptsTo(Switch.toDefaultContext());
            actor.attemptsTo(Switch.toFrame("braintree-hosted-field-expirationDate"));
            actor.attemptsTo(Enter.keyValues(expiryDate).into(OneM_fine_and_fees_pageobject.BRAINTREE_EXPIRATION_DATE_INPUT_BOX));
            actor.attemptsTo(Switch.toDefaultContext());
            actor.attemptsTo(Switch.toFrame("braintree-hosted-field-cvv"));
            actor.attemptsTo(Enter.keyValues(cvc).into(OneM_fine_and_fees_pageobject.BRAINTREE_CVV_INPUT_BOX));
            actor.attemptsTo(Switch.toDefaultContext());
            actor.attemptsTo(Click.on(OneM_fine_and_fees_pageobject.BT_SUBMIT_PAYMENT_BUTTON));
            Helper.customWait(10000);
            Helper.switchToFrames("Cardinal-CCA-IFrame");
            actor.attemptsTo(Enter.keyValues(Braintree.CODE).into(OneM_fine_and_fees_pageobject
                    .CODE_INPUT_BOX).then(
                    Click.on(OneM_fine_and_fees_pageobject.SUBMIT_CODE_BUTTON)
            ));
            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.SUBMIT_CODE_BUTTON, isNotPresent()));
        } else if (mode.equalsIgnoreCase(PaymentMode.ENETS_DEBIT)) {
            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(Scroll.to(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode)).then(
                    Click.on(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode))));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.PAYMENT_METHOD_RADIO_BUTTON));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.SUBMIT_BUTTON));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.PROCESS_BUTTON));
        } else if (mode.equalsIgnoreCase(PaymentMode.PAYPAL)) {
            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(WaitUntil.the(OneM_Oph_pageobject.PAYPAL_PAYMENT_BUTTON
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isClickable()));
            actor.attemptsTo(Scroll.to(OneM_Oph_pageobject.PAYPAL_PAYMENT_BUTTON).then(
                    Click.on(OneM_Oph_pageobject.PAYPAL_PAYMENT_BUTTON)));
            actor.attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(2)));
            actor.attemptsTo(SwitchToWindow.targetWindow());
            actor.attemptsTo(WaitUntil.the(OneM_Oph_pageobject.EMAIL_INPUTBOX.waitingForNoMoreThan(Duration.ofMillis(30000)), isVisible()).then(
                    SendKeys.of("ccrs@lta.gov.sg").into(OneM_Oph_pageobject.EMAIL_INPUTBOX)));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.NEXT_BUTTON));
            actor.attemptsTo(SendKeys.of("P@ssw0rd").into(OneM_Oph_pageobject.PASSWORD_INPUTBOX));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.LOGIN_BUTTON));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.CONTINUE_BUTTON));
            Helper.customWait(2000);
            ArrayList<String> tabs = new ArrayList<String>(
                    BrowseTheWeb.as(theActorInTheSpotlight()).getDriver().getWindowHandles());
            theActorInTheSpotlight().attemptsTo(Switch.toWindow(tabs.get(0)));

        } else if (mode.equalsIgnoreCase(PaymentMode.GOOGLE_PAY)) {
            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(Scroll.to(OneM_Oph_pageobject.GOOGLE_PAY_PAYMENT_BUTTON).then(
                    Click.on(OneM_Oph_pageobject.GOOGLE_PAY_PAYMENT_BUTTON)));
            Helper.customWait(2000);
            theActorInTheSpotlight().attemptsTo(Switch.toWindowTitled("Sign in - Google Accounts - Google Chrome"));
            theActorInTheSpotlight().attemptsTo(JavaScriptClick.on(OneM_Oph_pageobject.GOOGLE_EMAIL_INPUTBOX));
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(OneM_Oph_pageobject.GOOGLE_EMAIL_INPUTBOX, isVisible()));
            List<String> username = Arrays.asList("CcrsPortal".split(""));
            for (int key = 0; key < username.size(); key++) {
                actor.attemptsTo(SendKeys.of(username.get(key)).into(OneM_Oph_pageobject.GOOGLE_EMAIL_INPUTBOX));
                //Helper.customWait(1000);
            }
            Helper.customWait(1000);
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.GOOGLE_NEXT_BUTTON));
            actor.attemptsTo(SendKeys.of("Ccrs@1794").into(OneM_Oph_pageobject.GOOGLE_PASSWORD_INPUTBOX));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.PASSWORD_NEXT_BUTTON));
            actor.attemptsTo(SwitchToWindow.targetWindow());
            actor.attemptsTo(Switch.toFrame("sM432dIframe"));
            actor.attemptsTo(Click.on(OneM_Oph_pageobject.GOOGLE_PAY_CONTINUE_BUTTON));
            Helper.customWait(2000);
            ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
            getDriver().switchTo().window(tabs.get(0));

        } else if (mode.equalsIgnoreCase(PaymentMode.SGQR_PAYNOW)) {
            actor.attemptsTo(WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET
                    .waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible()));
            actor.attemptsTo(Scroll.to(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode))
                    .then(WaitUntil.the(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode), isVisible())).then(
                            Click.on(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(mode))));
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            String session = (String) js.executeScript(String.format(
                    "return sessionStorage.getItem('%s');", "vuex"));
            String ccrsRefNo = session.split("ccrsRefNo")[1].substring(3, 13);
            System.out.println("ccrsRefNo : " + ccrsRefNo);
            String sgSimulatorUrl = "https://www.paysvcwip.com/stg-api-pg-ws/dbsicnsimulator?orderId=DICNP" + ccrsRefNo + "&orderAmount=" + "100.00";
            js.executeScript("window.open();");
            ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
            getDriver().switchTo().window(tabs.get(1));
            theActorInTheSpotlight().attemptsTo(Open.url(sgSimulatorUrl));
            ArrayList<String> tabsNew = new ArrayList<String>(getDriver().getWindowHandles());
            getDriver().switchTo().window(tabsNew.get(0));

        }

        if (PropertiesUtil.getProperties("sale.item.type").equalsIgnoreCase("Notice")) {
            actor.attemptsTo(WaitUntil.the(ExpectedConditions.visibilityOf(
                    OneM_fine_and_fees_pageobject.PAYMENT_CONFIRMATION
                            .waitingForNoMoreThan(Duration.ofMillis(10000)).resolveFor(theActorInTheSpotlight()))));
            String referenceIDText = CCB.getTextUsingAttribute(OneM_fine_and_fees_pageobject.NOTICE_REFERENCE_ID_TEXT,"innerText");
            String referenceID = OneMPortal.extractReferenceIDFromText(referenceIDText);
            theActorInTheSpotlight().attemptsTo(Ensure.that(referenceID).isNotEmpty());
            logger.info(()->String.format("Reference ID is %s",referenceID));
            PropertiesUtil.setProperties("portal.paymentEventID", referenceID);
            if (mode.equalsIgnoreCase(PaymentMode.SGQR_PAYNOW))
                getDriver().quit();

        } else {
            actor.attemptsTo(WaitUntil.the(ExpectedConditions.visibilityOf(
                    OneM_fine_and_fees_pageobject.REFERENCE_ID_TEXT.waitingForNoMoreThan(Duration.ofMillis(30000)).resolveFor(theActorInTheSpotlight()))));
            String referenceIDText = OneM_fine_and_fees_pageobject.REFERENCE_ID_TEXT.resolveFor(theActorInTheSpotlight()).getAttribute("innerText");
            String referenceID = OneMPortal.extractReferenceIDFromText(referenceIDText);
            theActorInTheSpotlight().attemptsTo(Ensure.that(referenceID).isNotEmpty());
            logger.info(()->String.format("Reference ID is %s",referenceID));
            PropertiesUtil.setProperties("portal.paymentEventID", referenceID);
            if (mode.equalsIgnoreCase(PaymentMode.SGQR_PAYNOW))
                getDriver().quit();
        }
        logger.info(()->String.format("Item purchase successful.."));
    }

}
