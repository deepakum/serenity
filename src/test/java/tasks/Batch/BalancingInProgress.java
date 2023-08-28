package tasks.Batch;

import Utility.BatchJob.Counter;
import Utility.Payment.NetsPOS;
import Utility.others.*;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.CheckCheckbox;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.CCB;
import tasks.DC;
import tasks.Navigate;

import java.time.Duration;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class BalancingInProgress implements Task {

    @Step("{0} able to set TC status to balancing in progress")
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        String charValue = null;
        if (PropertiesUtil.getProperties("tender.source").equalsIgnoreCase("COUNTER")) {
            charValue = Counter.LOCATION;
        }else if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase("NETS-POS")){
            charValue = NetsPOS.LOCATION;
        }
        theActorInTheSpotlight().attemptsTo(DC.searchByChar(charValue));
        theActorInTheSpotlight().attemptsTo(DC.select(PropertiesUtil.getProperties("deposit.control.id")));
        Helper.switchToParentWindow();
        actor.attemptsTo(WaitUntil.the(ccb_user_pageobject.CRRS_HOME_HEADER, WebElementStateMatchers.isVisible()));
        Helper.switchToFrames("tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TENDER_CONTROL_MENU));
        Helper.switchToFrames("main","tabPage","Section2_tndrCtlGrd");

        int listOfTcs = ccb_tc_pageobject.TENDEDER_CONTROL_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        for(int i = 1 ; i <= listOfTcs ; i++) {
            String status = ccb_tc_pageobject.TC_BY_INDEX(i, i - 1, 9).resolveFor(theActorInTheSpotlight()).getText().trim();
            if (status.equalsIgnoreCase("Open")) {

                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TC_BY_INDEX(i, i - 1, 1)));
                Helper.switchToFrames("main");
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                        , is("Tender Control")));
                Helper.switchToFrames("tabPage");
                theActorInTheSpotlight().attemptsTo(SelectFromOptions.byVisibleText("Balancing in Progress")
                        .from(ccb_tc_pageobject.TENDER_CONTROL_STATUS
                                .waitingForNoMoreThan(Duration.ofMillis(3000))));
                if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase("NETS-POS")){
                    theActorInTheSpotlight().attemptsTo(CheckCheckbox.of(ccb_tc_pageobject.ALL_USERS_CHECKBOX));
                }
                theActorInTheSpotlight().attemptsTo(CCB.setEndingBalance());
//                    theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.TENDER_CONTROL_INFO)
//                            ,containsString("Balancing in Progress")));

                //Navigate back using history back button
                Helper.switchToFrames("main");
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.HISTORY_GOBACK_BUTTON));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                        , is("Deposit Control")));
            }
        }
    }
}
