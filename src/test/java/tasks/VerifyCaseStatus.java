package tasks;

import Utility.CCRS.CCBFrames;
import Utility.others.*;
import com.vladsch.flexmark.ext.tables.TableHead;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_case_search_pageobject;
import pageobjects.ccb_refund_approval_pageobject;
import questions.CCRSWebElementText;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static Utility.others.Helper.getWebDriver;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class VerifyCaseStatus implements Performable {

    private String status;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public VerifyCaseStatus(String status){
        this.status = status;
    }

    public static Performable withText(String status){
        return Instrumented.instanceOf(VerifyCaseStatus.class).withProperties(status);
    }

    @Step("{0} attempts to verify case status")
    public <T extends Actor> void performAs(T actor) {

        if(Config.getCurrentUser().equalsIgnoreCase("Admin")) {
            actor.attemptsTo(Navigate.toMenu(AdminMenu.Customer_Information.name(),
                    CustomerInformationMenu.CASE, SubSubMenu.SEARCH.name()));
        }else {
            PropertiesUtil.setProperties("parent.window.handle",getDriver().getWindowHandle());
            Helper.switchToFrames(CCBFrames.MAIN);
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.HOME_MENU_BUTTON));
            theActorInTheSpotlight().attemptsTo(LaunchMenu.to("Case"));
        }
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_case_search_pageobject.CASE_ID_SEARCH_BOX
        ,WebElementStateMatchers.isVisible()).forNoMoreThan(Duration.ofMillis(10000))
                .then(Enter.keyValues(PropertiesUtil.getProperties("refund.id"))
                        .into(ccb_case_search_pageobject.CASE_ID_SEARCH_BOX)
                        .then(Click.on(ccb_case_search_pageobject.CASE_SEARCH_BUTTON))
        ));
        actor.attemptsTo(SwitchToWindow.targetWindow());
        actor.should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE),
                containsStringIgnoringCase("Case")));
        Helper.switchToFrames(CCBFrames.TAB_PAGE);
        actor.should(eventually(seeThat(WebElementQuestion.the(ccb_refund_approval_pageobject.STATUS_TEXT_LABEL),
                WebElementStateMatchers.isVisible())));
        actor.should(seeThat(CCRSWebElementText.getText(ccb_refund_approval_pageobject.STATUS_TEXT_LABEL),
                containsStringIgnoringCase(status)));
        Helper.switchToFrames(CCBFrames.MAIN);
    }
}
