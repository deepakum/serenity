package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

@DefaultUrl("https://www.paysvcwip.com/#/components/finesEnquire")
public class OneM_fine_and_fees_pageobject extends PageObject {


    public static final Target IDENTIFICATION_TYPE = Target.the("identification type")
            .locatedBy("//div[@class='searchbox']/select");
    public static final Target IDENTIFICATION_NUMBER_INPUTBOX = Target.the("identification number inputbox")
            .locatedBy("(//div[@class='searchbox']/input)[1]");
    public static final Target NOTICE_NUMBER_INPUTBOX = Target.the("Notice number inputbox")
            .locatedBy("(//div[@class='searchbox']/input)[3]");
    public static final Target NOTICE_SUBMIT_BUTTON = Target.the("Notice submit button")
            .locatedBy("//div[@class='buttonbox']");
    public static final Target SELECT_ALL_CHKBOX = Target.the("Select all chkbox")
            .locatedBy("(//i[@class='cr-icon fa fa-check'])[1]");
    public static final Target NOTICE_AMOUNT_CHKBOX = Target.the("Notice amount chkbox")
            .locatedBy("(//span[@class='cr'])[2]/i");
    public static final Target REVIEW_PAYMENT_DETAILS_BUTTON =
            Target.the("Review payment details button")
                    .locatedBy("//button[@class='btn-proceed fright']");
    public static final Target CONFIRM_BUTTON = Target.the("confirm button")
            .locatedBy("//a[@class='btn-proceed btn-confirm pull-right']");
    public static final Target MODE_OF_PAYMENT_WIDGET = Target.the("payment option")
            .locatedBy("//div[@class='row ccrs_paymentinfo cardinfo']");
    public static final Target PAYMENT_TEXT_BUTTON = Target.the("payment button")
            .locatedBy("//div[@class='ccrs_modeofpayment']/following-sibling::p");
    public static final Target PAYMENT_MODE_BUTTON(String paymentType){
        PAYMENT_TEXT_BUTTON.resolveAllFor(theActorInTheSpotlight())
                .stream().forEach(ele-> System.out.println(ele.getText()));
        List<String> list = PAYMENT_TEXT_BUTTON.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getText().trim()).collect(Collectors.toList());
        System.out.println("list : "+list);
        int index = list.indexOf(paymentType) +1;
        System.out.println(index);
        return Target.the("payment button")
                .locatedBy("(//*[@class='ccrs_modeofpayment'])[{0}]").of(String.valueOf(index));
    }
    public static final Target PAYMENT_TYPE_BUTTON(String paymentType) {
        return Target.the("stripe payment button")
                .locatedBy("//p[contains(text(),'{0}')]/parent::div/div[@class='ccrs_modeofpayment']")
                .of(paymentType);
    }
    public static final Target OTHER_PAYMENT_BUTTON(String paymentType) {
        return Target.the("stripe payment button")
                .locatedBy("//p[contains(text(),'{0}')]/parent::div/div[@class='ccrs_opay_modeofpayment']")
                .of(paymentType);
    }
    public static final Target CARD_ELEMENT = Target.the("card element")
            .locatedBy("css:#card-element");
    public static final Target BRAINTREE_CARD_FORM = Target.the("card form")
            .locatedBy("css:#cardForm");
    public static final Target CARD_NUMBER_INPUTBOX =
         Target.the("card number inputbox")
                .locatedBy("(//*[@class='InputContainer']/input)[1]");

    public static final Target BRAINTREE_CARD_NUMBER_INPUT_BOX =
            Target.the("credit card number input box")
                    .locatedBy("css:#credit-card-number");

    public static final Target BRAINTREE_EXPIRATION_DATE_INPUT_BOX =
            Target.the("expiration date input box")
                    .locatedBy("css:#expiration");
    public static final Target BRAINTREE_CVV_INPUT_BOX =
            Target.the("cvv input box")
                    .locatedBy("css:#cvv");
    public static final Target EXPIRY_DATE_INPUTBOX = Target.the("expiry date inputbox")
            .locatedBy("(//*[@class='InputContainer']/input)[2]");

    public static final Target CVC_INPUTBOX = Target.the("cvc inputbox")
            .locatedBy("(//*[@class='InputContainer']/input)[3]");
    public static final Target ST_SUBMIT_PAYMENT_BUTTON = Target.the("submit payment button")
            .locatedBy("css:#stripeCardPaybutton");
    public static final Target BT_SUBMIT_PAYMENT_BUTTON = Target.the("submit payment button")
            .locatedBy("css:#bt_cc_submit");
    public static final Target CODE_INPUT_BOX = Target.the("code input box")
            .locatedBy("//input[@name='challengeDataEntry']");
    public static final Target SUBMIT_CODE_BUTTON = Target.the("submit code button")
            .locatedBy("//input[@value='SUBMIT']");
    public static final Target CONFIRMATION_TEXT = Target.the("payment confirmation text")
            .locatedBy("//h3[contains(text(),'Confirmation')]");
    public static final Target REFERENCE_ID_TEXT = Target.the("reference ID text")
            .locatedBy("//div[@class='ccrs_confirm-message']/p");
    public static final Target NOTICE_REFERENCE_ID_TEXT = Target.the("reference ID text")
            .locatedBy("//div[@class='dashboard__contentfine']/div[3]/p");

    public static final Target PAYMENT_CONFIRMATION = Target.the("reference ID text")
            .locatedBy("//div[@class='dashboard__contentfine']");

    public static final Target NOTICE_ROW = Target.the("notice entry")
            .locatedBy("#tab1>table>tbody>tr");
}
