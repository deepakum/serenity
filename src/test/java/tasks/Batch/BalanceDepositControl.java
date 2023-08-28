package tasks.Batch;

import Utility.others.Helper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_tc_pageobject;
import pageobjects.ccb_dc_pageobject;
import questions.CCRSWebElementText;
import tasks.SelectDepositControl;

import java.time.Duration;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class BalanceDepositControl implements Performable {

    public static String balanceAmt;
    public static Performable balanced(){
        return instrumented(BalanceDepositControl.class);
    }

    @Override
    @Step("{0} able to balance deposit control")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames("main");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.HOME_MENU_BUTTON));
        theActorInTheSpotlight().attemptsTo(SelectDepositControl.to());
//        try {
//            Runtime.getRuntime().exec("C:\\Users\\anilkodam\\IdeaProjects\\CCRS\\src\\test\\resources\\sendEnter.exe");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Deposit Control")));
        Helper.switchToFrames("tabPage");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.DEPOSIT_CONTROL_INFO
                        .waitingForNoMoreThan(Duration.ofMillis(2000)))
                ,containsString("Open")));
        balanceAmt = ccb_dc_pageobject.EXPECTED_ENDING_BALACE.resolveFor(theActorInTheSpotlight()).getText();
        theActorInTheSpotlight().attemptsTo(Clear.field(ccb_dc_pageobject.ENDING_BALANCE)
                .then(Enter.keyValues(balanceAmt).into(ccb_dc_pageobject.ENDING_BALANCE)));
        theActorInTheSpotlight().attemptsTo(SelectFromOptions.byVisibleText("Balanced")
                .from(ccb_dc_pageobject.DEP_CTL_STATUS_FLG));
        Helper.switchToFrames("main");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.SAVE));
        Helper.customWait(10000);
        Helper.switchToFrames("tabPage");
    }
}
