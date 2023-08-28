package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class OneM_Oph_pageobject extends PageObject {
    public static final Target PAYMENT_METHOD_RADIO_BUTTON = Target.the("payment method radio button")
            .locatedBy("//*[@id='selBankID' and @value='152']");
    public static final Target SUBMIT_BUTTON = Target.the("submit button")
            .locatedBy("#btnSubmit");
    public static final Target PROCESS_BUTTON = Target.the("process button")
            .locatedBy("//input[@name='submitbutton' and @value='Process']");
    public static final Target PAYPAL_PAYMENT_BUTTON = Target.the("paypal payment button")
            .locatedBy("#paypal-button-div>div>div");

    public static final Target GOOGLE_PAY_PAYMENT_BUTTON = Target.the("google pay payment button")
            .locatedBy("#stripe-google-pay-button>div>button");

    public static final Target EMAIL_INPUTBOX = Target.the("email inputbox")
            .locatedBy("#email");
    public static final Target GOOGLE_EMAIL_INPUTBOX = Target.the("google email inputbox")
            .locatedBy("//input[@id='identifierId']");
    public static final Target GOOGLE_BODY = Target.the("google email inputbox")
            .locatedBy("#yDmH0d");

    public static final Target PASSWORD_INPUTBOX = Target.the("password inputbox")
            .locatedBy("#password");
    public static final Target GOOGLE_PASSWORD_INPUTBOX = Target.the("password inputbox")
            .locatedBy("//input[@name='Passwd']");
    public static final Target NEXT_BUTTON = Target.the("next button")
            .locatedBy("#btnNext");
    public static final Target PASSWORD_NEXT_BUTTON = Target.the("password next button")
            .locatedBy("#passwordNext");
    public static final Target GOOGLE_NEXT_BUTTON = Target.the("next button")
            .locatedBy("#identifierNext>div>button");
    public static final Target LOGIN_BUTTON = Target.the("login button")
            .locatedBy("#btnLogin");
    public static final Target CONTINUE_BUTTON = Target.the("continue button")
            .locatedBy("#payment-submit-btn");
    public static final Target GOOGLE_PAY_CONTINUE_BUTTON = Target.the("google pay continue button")
            .locatedBy("//*[@class='buttons-wrapper']/div/div");
}
