package tasks;

import Utility.Payment.Stripe;
import Utility.Payment.PaymentMode;
import Utility.others.PropertiesUtil;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.waits.WaitUntil;
import pageobjects.OneM_fine_and_fees_pageobject;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class OneMPortal {

    public String mode;
    public OneMPortal(String mode){
        this.mode = mode;
    }
    public static Task searchNotice(String idType, String idNumber){
        return Task.where("Search notice",
                SelectFromOptions.byVisibleText(idType).from(OneM_fine_and_fees_pageobject.IDENTIFICATION_TYPE),
                Enter.theValue(idNumber).into(OneM_fine_and_fees_pageobject.IDENTIFICATION_NUMBER_INPUTBOX),
                Scroll.to(OneM_fine_and_fees_pageobject.NOTICE_NUMBER_INPUTBOX),
                Enter.theValue(PropertiesUtil.getProperties("CCRS.NoticeNo"))
                        .into(OneM_fine_and_fees_pageobject.NOTICE_NUMBER_INPUTBOX).then(
                                Click.on(OneM_fine_and_fees_pageobject.NOTICE_SUBMIT_BUTTON)
                        ),
                WaitUntil.the(OneM_fine_and_fees_pageobject.NOTICE_ROW,isVisible()).forNoMoreThan(Duration.ofMillis(10000)),
                Scroll.to(OneM_fine_and_fees_pageobject.NOTICE_ROW),
                Click.on(OneM_fine_and_fees_pageobject.NOTICE_AMOUNT_CHKBOX),
                Click.on(OneM_fine_and_fees_pageobject.REVIEW_PAYMENT_DETAILS_BUTTON),
                WaitUntil.the(OneM_fine_and_fees_pageobject.CONFIRM_BUTTON,isVisible()),
                Scroll.to(OneM_fine_and_fees_pageobject.CONFIRM_BUTTON).then(
                        Click.on(OneM_fine_and_fees_pageobject.CONFIRM_BUTTON)),
                WaitUntil.the(OneM_fine_and_fees_pageobject.MODE_OF_PAYMENT_WIDGET.waitingForNoMoreThan(Duration.ofMillis(5000)), isVisible())
        );
    }

    public static Performable makePayment(String paymentMode){
        String cardNumber = null;
        String expiryDate = null;
        String cvc = null;

        if(paymentMode.equalsIgnoreCase(PaymentMode.STRIPE)) {
            cardNumber = Stripe.CARD_NUMBER;
            expiryDate = Stripe.EXPIRY_DATE;
            cvc = Stripe.CVC;

            return Task.where("make stripe payment",
                    Scroll.to(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(paymentMode)).then(
                            Click.on(OneM_fine_and_fees_pageobject.PAYMENT_TYPE_BUTTON(paymentMode))),
                    WaitUntil.the(OneM_fine_and_fees_pageobject.CARD_ELEMENT, isVisible()),
                    Click.on(OneM_fine_and_fees_pageobject.CARD_ELEMENT),
                    Switch.toFrame(0),
                    Enter.keyValues(cardNumber).into(OneM_fine_and_fees_pageobject.CARD_NUMBER_INPUTBOX),
                    Enter.keyValues(expiryDate).into(OneM_fine_and_fees_pageobject.EXPIRY_DATE_INPUTBOX).then(
                            Enter.keyValues(cvc).into(OneM_fine_and_fees_pageobject.CVC_INPUTBOX)),
                    Switch.toDefaultContext(),
                    Click.on(OneM_fine_and_fees_pageobject.ST_SUBMIT_PAYMENT_BUTTON)
                    //WaitUntil.the(FineAndFeesPageObject.CONFIRMATION_TEXT,isVisible())
                    );
        }else{
            return null;
        }
    }
    public static String extractReferenceIDFromText(String text){
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String DC="";
        if(matcher.find()){
            DC = matcher.group().trim();
        }
        return DC;
    }
}
