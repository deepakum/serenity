package tasks;

import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import pageobjects.ccb_refund_approval_pageobject;
import pageobjects.ccb_refund_initiation_pageobject;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class RefundApproval implements Performable {

    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    String businessSys;

    public RefundApproval(String businessSys){
        this.businessSys = businessSys;
    }

    public static Performable to(String businessSys){
        return Instrumented.instanceOf(RefundApproval.class).withProperties(businessSys);
    }
    @Override
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames("main","tabPage","zoneMapFrame_1");
        theActorInTheSpotlight().attemptsTo(SelectFromOptions.byVisibleText(businessSys)
                .from(ccb_refund_approval_pageobject.BUSINESS_SYSTEM_DROPDOWN));
        theActorInTheSpotlight().attemptsTo(Enter.keyValues(PropertiesUtil.getProperties("refund.id"))
                    .into(ccb_refund_approval_pageobject.REFUND_ID_INPUTBOX));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_refund_initiation_pageobject.SEARCH_BUTTON));

        Helper.switchToFrames("main","tabPage","zoneMapFrame_1");
        PropertiesUtil.setProperties("refund.id",
                ccb_refund_approval_pageobject.REFUND_ID_TEXT("Refund ID").resolveFor(theActorInTheSpotlight()).getText());
        PropertiesUtil.setProperties("refund.date",
                ccb_refund_approval_pageobject.REFUND_OPTION("Creation Date").resolveFor(theActorInTheSpotlight()).getText());
        PropertiesUtil.setProperties("refund.amount",
                ccb_refund_approval_pageobject.REFUND_OPTION("Refund Amount(S$)").resolveFor(theActorInTheSpotlight()).getText());
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_refund_approval_pageobject.REFUND_OPTION("Approve").find(ccb_refund_approval_pageobject.APPROVE_CHECKBOX_BUTTON)));
        theActorInTheSpotlight().attemptsTo(WarningPopup.dismiss(ccb_refund_approval_pageobject.SUBMIT_BUTTON, ""));
    }
}
