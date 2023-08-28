package tasks;

import Utility.CCRS.Item;
import Utility.Payment.TenderSource;
import Utility.Refund.GIRORefundMethod;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.Keys;
import pageobjects.ccb_refund_initiation_pageobject;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class TransactionDetails implements Task {

    static Logger logger= LoggerFactory.getLogger(TransactionDetails.class);
    String businessSystem;
    String refundType;
    String application;

    public TransactionDetails(String refundType, String businessSystem, String application){
        this.refundType = refundType;
        this.businessSystem = businessSystem;
        this.application = application;
    }

    public static Performable add(String refundType, String businessSystem, String application){
        return Instrumented.instanceOf(TransactionDetails.class).withProperties(refundType,businessSystem,application);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        String receiptID;
        if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase(TenderSource.COUNTER)){
            receiptID = PropertiesUtil.getProperties("CCRS.paymentEventID");
        }else {
            receiptID = PropertiesUtil.getProperties("portal.paymentEventID");
        }
        logger.info(()->String.format("Receipt id is %s",receiptID));
        Helper.switchToFrames("main","tabPage","zoneMapFrame_1");
        theActorInTheSpotlight().attemptsTo(SelectFromOptions.byVisibleText(businessSystem)
                .from(ccb_refund_initiation_pageobject.BUSINESS_SYSTEM_OPTIONS));
        theActorInTheSpotlight().attemptsTo(Clear.field(
                ccb_refund_initiation_pageobject.RECEIPT_ID_INPUTBOX)
                .then(Enter.keyValues(receiptID)
                .into(ccb_refund_initiation_pageobject.RECEIPT_ID_INPUTBOX))
        );
        theActorInTheSpotlight().attemptsTo(Click.on(
                ccb_refund_initiation_pageobject.SEARCH_BUTTON)
                .then(Ensure.that(ccb_refund_initiation_pageobject.ERROR_MSG)
                        .attribute("innerText").isEmpty()
        ));

        if(PropertiesUtil.getProperties("sale.item.type").equalsIgnoreCase(Item.NOTICE)) {
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(
                    ccb_refund_initiation_pageobject.REFUND_AMOUNT_INPUTBOX(0),
                    WebElementStateMatchers.isVisible()).then(Enter.keyValues(String.valueOf(100))
                    .into(ccb_refund_initiation_pageobject.REFUND_AMOUNT_INPUTBOX(0))
                    .thenHit(Keys.ENTER)));
        }else if(PropertiesUtil.getProperties("sale.item.type").equalsIgnoreCase(Item.SALE_ITEM)){
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_refund_initiation_pageobject.REFUND_AMOUNT_INPUTBOX(0),WebElementStateMatchers.isVisible()).then(Enter.keyValues(String.valueOf(0.5))
                    .into(ccb_refund_initiation_pageobject.TOTAL_SALE_QUANTITY(0))
                    .thenHit(Keys.ENTER)));
        }else{
            throw new RuntimeException("Purchase item is not set...");
        }
    }
}
