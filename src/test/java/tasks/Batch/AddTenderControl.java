package tasks.Batch;

import Utility.CCRS.CCBFrames;
import Utility.CCRS.User;
import Utility.others.Config;
import Utility.others.Helper;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import pageobjects.*;
import questions.CCRSWebElementText;
import stepdefinitions.BatchJobSubmissionStepDefinition;
import tasks.CCB;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.core.Is.is;

public class AddTenderControl implements Performable {

    @Steps
    BatchJobSubmissionStepDefinition batchJobSubmissionStepDefinition;
    private String tenderSource;

    public AddTenderControl(String tenderSource){
        this.tenderSource = tenderSource;
    }

    public static Performable toDC(String tenderSource){
        return Instrumented.instanceOf(AddTenderControl.class).withProperties(tenderSource);
    }

    @Override
    @Step("^(.*) attempts to add tender control to deposit control$")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames("main", "tabPage");
        actor.attemptsTo(Click.on(ccb_dc_pageobject.DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_dc_pageobject.GO_TO_TC_MENU.resolveFor(actor)).build().perform();
        action.moveToElement(ccb_dc_pageobject.TC_ADD.resolveFor(actor)).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Tender Control")));
        Helper.switchToFrames("tabPage");
        theActorInTheSpotlight().attemptsTo(
                Enter.keyValues(tenderSource).into(ccb_tc_pageobject.TENDER_SOURCE).thenHit(Keys.RETURN)
                        .then(WaitUntil.the(ccb_tc_pageobject.TENDER_SOURCE_DESCRIPTION, WebElementStateMatchers.isVisible()))
        );
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.TENDER_SOURCE_DESCRIPTION)
                ,containsStringIgnoringCase(tenderSource)));
        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.SAVE));
        Helper.customWait(2000);

        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
    }
    private String getOpenTC(){
        Helper.switchToFrames("main", "tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TC_SEARCH_ICON));
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(Enter.keyValues("COUNTER").into(ccb_tc_pageobject.TENDER_SOURCE));
        theActorInTheSpotlight().attemptsTo(SelectFromOptions.byVisibleText("Open").from(ccb_tc_search_pageobject.TENDER_CONTROL_STATUS_OPTION));
        theActorInTheSpotlight().attemptsTo(Enter.keyValues(getCurrentDate()).into(ccb_tc_search_pageobject.TC_CREATION_DATE_INPUTBOX));
        theActorInTheSpotlight().attemptsTo(Enter.keyValues(Config.getUserName()).into(ccb_tc_search_pageobject.USERNAME_INPUTBOX));
        theActorInTheSpotlight().attemptsTo(CCB.clickAnElement(ccb_dc_search_pageobject.DC_SEARCH_BUTTON.waitingForNoMoreThan(Duration.ofMillis(3000))));
        Helper.switchToFrames("dataframe");
        int listOfTC = ccb_dc_search_pageobject.LIST_OF_DC.resolveAllFor(theActorInTheSpotlight()).size();
        if(listOfTC>0){

        }
        return null;
    }
    private String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }
}
