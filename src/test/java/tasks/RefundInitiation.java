package tasks;

import Utility.Refund.BusinessSystem;
import Utility.Refund.RefundMode;
import Utility.SaleItem.BuyerDetails;
import Utility.others.PropertiesUtil;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import pageobjects.ccb_refund_initiation_pageobject;

public class RefundInitiation implements Task{

    static EnvironmentVariables environmentVariables= SystemEnvironmentVariables.createEnvironmentVariables();
    String mode;
    String reason;
    String account;
    String bank;
    String businessSystem;
    String application;
    public RefundInitiation(String businessSystem,String mode, String reason, String account, String bank,String application){
        this.application = application;
        this.businessSystem = businessSystem;
        this.mode = mode;
        this.reason = reason;
        this.account = account;
        this.bank = bank;
    }

    public static RefundInitiation using(String businessSystem,String mode, String reason, String account, String bank,String application) {
        return Instrumented.instanceOf(RefundInitiation.class).withProperties(businessSystem,mode,reason,account,bank,application);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        PropertiesUtil.setProperties("refund.method", mode);
        switch (mode) {
            case RefundMode.CHEQUE:
                if(businessSystem.equalsIgnoreCase(BusinessSystem.SALE_ITEM)){
                    actor.attemptsTo(SelectFromOptions.byVisibleText(mode).from(ccb_refund_initiation_pageobject.REFUND_METHOD_DROPDOWN_BOX)
                            .then(SelectFromOptions.byVisibleText(reason).from(ccb_refund_initiation_pageobject.REFUND_REASON)
                                    .then(Enter.keyValues(BuyerDetails.CITY).into(ccb_refund_initiation_pageobject.CITY_INPUT_BOX)
                                    )));
                } else if (businessSystem.equalsIgnoreCase(BusinessSystem.VIOLATION)) {
                    actor.attemptsTo(SelectFromOptions.byVisibleText(mode).from(ccb_refund_initiation_pageobject.REFUND_METHOD_DROPDOWN_BOX)
                            .then(SelectFromOptions.byVisibleText(reason).from(ccb_refund_initiation_pageobject.REFUND_REASON)
                                    .then(Enter.keyValues(BuyerDetails.CITY).into(ccb_refund_initiation_pageobject.CITY_INPUT_BOX)
                                            .then(Enter.keyValues(BuyerDetails.MOBILE).into(ccb_refund_initiation_pageobject.CONTACT_NUMBER_INPUTBOX)
                                                    .then(Enter.keyValues(BuyerDetails.PIN_CODE).into(ccb_refund_initiation_pageobject.POSTAL_CODE_INPUT_BOX))
                                                    .then(Enter.keyValues(BuyerDetails.BLOCK).into(ccb_refund_initiation_pageobject.BLOCK_INPUT_BOX))
                                                    .then(Enter.keyValues(BuyerDetails.STREET).into(ccb_refund_initiation_pageobject.STREET_INPUT_BOX))
                                                    .then(SelectFromOptions.byVisibleText(BuyerDetails.CITY).from(ccb_refund_initiation_pageobject.COUNTRY_LIST_BOX))))));
                }
                break;
            case RefundMode.GIRO:

                actor.attemptsTo(SelectFromOptions.byVisibleText(mode).from(ccb_refund_initiation_pageobject.REFUND_METHOD_DROPDOWN_BOX)
                        .then(Enter.keyValues(account).into(ccb_refund_initiation_pageobject.GIRO_ACCOUNT_INPUTBOX)
                                .then(Enter.keyValues(bank).into(ccb_refund_initiation_pageobject.BANK_NAME_DROPDOWN)
                                        .then(Enter.keyValues(BuyerDetails.MOBILE).into(ccb_refund_initiation_pageobject.CONTACT_NUMBER_INPUTBOX)
                                                .then(SelectFromOptions.byVisibleText(reason).from(ccb_refund_initiation_pageobject.REFUND_REASON)
                                                )))));
                break;
            case RefundMode.CREDIT_CARD:
                actor.attemptsTo(SelectFromOptions.byVisibleText(mode).from(ccb_refund_initiation_pageobject.REFUND_METHOD_DROPDOWN_BOX)
                        .then(Scroll.to(ccb_refund_initiation_pageobject.REFUND_REASON)
                                .then(SelectFromOptions.byVisibleText(reason).from(ccb_refund_initiation_pageobject.REFUND_REASON)
                                        .then(WaitUntil.the(ccb_refund_initiation_pageobject.REFUND_REASON, WebElementStateMatchers.containsText(reason)))
                                )));
                break;
        }
    }
}
