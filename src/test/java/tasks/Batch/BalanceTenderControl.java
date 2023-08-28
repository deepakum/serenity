package tasks.Batch;

import Utility.CCRS.CCBFrames;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.*;
import net.thucydides.core.annotations.Step;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.DC;
import tasks.SwitchToWindow;

import java.util.Arrays;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class BalanceTenderControl implements Performable {

    Logger logger = LoggerFactory.getLogger(BalanceTenderControl.class);
    public static Performable to(){
        return instrumented(BalanceTenderControl.class);
    }

    @Step("{0} able to balanced all TC's")
    public <T extends Actor> void performAs(T actor) {

        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        theActorInTheSpotlight().attemptsTo(DC.selectDCByID());
        Helper.switchToParentWindow();
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Deposit Control")));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_MENU));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TENDER_CONTROL_MENU));
        Helper.switchToFrames("main","tabPage","Section2_tndrCtlGrd");

        int listOfTcs = ccb_tc_pageobject.TENDEDER_CONTROL_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        for(int tcRow = 1 ; tcRow <= listOfTcs ; tcRow++) {
            String status = ccb_tc_pageobject.TC_BY_INDEX(tcRow, tcRow - 1, 9)
                    .resolveFor(theActorInTheSpotlight()).getText().trim();
            if (status.equalsIgnoreCase("Balancing in Progress")) {
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TC_BY_INDEX(tcRow, tcRow - 1, 1)));
                Helper.switchToFrames(CCBFrames.MAIN);
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                        , is("Tender Control")));
                Helper.switchToFrames(CCBFrames.TAB_PAGE,"blncGrid");
                theActorInTheSpotlight().attemptsTo(Clear.field(ccb_tc_pageobject.ENDING_BALANCE),
                        Enter.keyValues(ccb_tc_pageobject.TENDER_CONTROL_TABLE(3)
                        .resolveFor(theActorInTheSpotlight()).getText())
                        .into(ccb_tc_pageobject.ENDING_BALANCE)
                );
                Helper.switchToFrames(CCBFrames.MAIN, CCBFrames.TAB_PAGE);
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.BALANCE));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.TENDER_CONTROL_STATUS)
                        ,containsString("Balanced")));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.TENDER_CONTROL_INFO)
                        , containsString("Balanced")));

                //Navigate back using history back button
                Helper.switchToFrames(CCBFrames.MAIN);
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.HISTORY_GOBACK_BUTTON));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                        , is("Deposit Control")));
                Helper.switchToFrames(CCBFrames.MAIN, CCBFrames.TAB_PAGE, "Section2_tndrCtlGrd");
            }
        }
        //Balance Deposit control when all Tender control are balanced
        //balanceDC();
    }

//    private void balanceDC(){
////        Helper.switchToFrames("main","tabMenu");
////        theActorInTheSpotlight().attemptsTo(Click.on(DC_PageObject.DC_MAIN_MENU_TAB));
////        Helper.switchToFrames("main","tabPage");
////        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(TC_PageObject.DEPOSIT_CONTROL_INFO
////                        .waitingForNoMoreThan(Duration.ofMillis(2000)))
////                ,containsString("Open")));
////        String balanceAmt = BalancedDCPageObject.EXPECTED_ENDING_BALACE.resolveFor(theActorInTheSpotlight()).getText();
////        logger.info(()->String.format("Balance amount is %s",balanceAmt));
////        DepositControlInfo.setExpectedEndingBalance(balanceAmt);
////        PropertiesUtil.setProperties("recon.endingBalanceAmount",balanceAmt);
////        theActorInTheSpotlight().attemptsTo(Clear.field(BalancedDCPageObject.ENDING_BALANCE)
////                .then(Enter.keyValues(balanceAmt).into(BalancedDCPageObject.ENDING_BALANCE)));
////        theActorInTheSpotlight().attemptsTo(SelectFromOptions.byVisibleText("Balanced")
////                .from(BalancedDCPageObject.DEP_CTL_STATUS_FLG));
////        Helper.switchToFrames("main");
////        theActorInTheSpotlight().attemptsTo(Click.on(CCRSHomePageObject.SAVE));
////        Helper.customWait(5000);
////        theActorInTheSpotlight().attemptsTo(BankInDate.displayed());
//    }
}
