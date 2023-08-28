package stepdefinitions;

import Utility.CCRS.CCBFrames;
import Utility.CCRS.Home;
import Utility.others.Config;
import Utility.others.Helper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SendKeys;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.hamcrest.Matchers;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_login_pageobject;
import questions.CCRSWebElementText;
import tasks.CCB;
import tasks.OpenTheApplication;

import java.util.List;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class CCRSLoginStepdefinition {

    private final Logger log = LoggerFactory.getLogger(CCRSLoginStepdefinition.class);
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    Actor james;
    @Before
    public void driverSetup(){
        WebDriverManager.chromedriver().setup();
    }
    @Before()
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("^(.*) is at the CCRS login page$")
    public void james_is_at_the_ccrs_login_page(String actor) {
        james = OnStage.theActor(actor);
        theActorCalled(actor).attemptsTo(OpenTheApplication.onTheHomePage());
        theActorInTheSpotlight().attemptsTo(Switch.toTheOtherWindow());
    }
    @When("^James provides credential as \"([^\"]*)\"$")
    public void james_provides_credential_as(String role) {
        theActorInTheSpotlight().attemptsTo(CCB.login(role));
    }
    @Then("^He is able to access to CCRS application$")
    public void he_is_able_to_access_to_ccrs_application() {
        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().should(eventually(
                seeThat(WebElementQuestion.the(ccb_user_pageobject.PAGE_TITLE), isVisible())));
    }

    @When("James as a {string} attempts to login to CCRS page")
    public void james_as_a_attempts_to_login_to_ccrs_page(String user) {
        theActorInTheSpotlight().attemptsTo(CCB.login(user));
    }
    @Then("he should be able to login to ccrs page")
    public void he_should_be_able_to_login_to_ccrs_page() {
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("main"));
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PRODUCT_NAME_SPAN),
                containsStringIgnoringCase(Home.PRODUCT_NAME)));
        List<String> brandingList = ccb_login_pageobject.BRANDING_BAR_MENU.resolveAllFor(theActorInTheSpotlight())
                        .stream().map(web -> web.getTextValue()).collect(Collectors.toList());
        List<String> toolBarList = ccb_login_pageobject.TOOL_BAR_MENU.resolveAllFor(theActorInTheSpotlight())
                .stream().filter(web -> !web.getTextValue().isEmpty()).map(web->web.getTextValue().trim()).collect(Collectors.toList());
        theActorInTheSpotlight().attemptsTo((Ensure.that(brandingList)
                        .contains(Home.ABOUT, Config.getName().toUpperCase())));
        theActorInTheSpotlight().attemptsTo((Ensure.that(toolBarList)
                        .contains(Home.MENU)));
        theActorInTheSpotlight().attemptsTo(Ensure.that(ccb_user_pageobject.SEARCH_MENU).isDisplayed());
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.LOGGED_IN_USER_SPAN).then(
                Ensure.that(ccb_user_pageobject.LOGOUT).isDisplayed()
        ).then(SendKeys.of(Keys.CANCEL).into(ccb_user_pageobject.LOGGED_IN_USER_SPAN)));
        theActorInTheSpotlight().attemptsTo(Ensure.that(ccb_user_pageobject.REFRESH).isDisplayed());
    }

    @When("he attempts to logout from the application")
    public void he_attempts_to_logout_from_the_application() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
    }
    @Then("he should be able to logout from ccrs application")
    public void he_should_be_able_to_logout_from_ccrs_application() {
        theActorInTheSpotlight().should(eventually(seeThat(WebElementQuestion.the(
                ccb_login_pageobject.USERNAME_INPUTBOX),isVisible()
        )));
    }

    @When("James as a {string} attempts to login to CCRS page using {string} and {string}")
    public void james_as_a_attempts_to_login_to_ccrs_page_using_and(String role, String username, String password) {
        theActorInTheSpotlight().attemptsTo(Enter.keyValues(username).into(ccb_login_pageobject.USERNAME_INPUTBOX).then(
                Enter.keyValues(password).into(ccb_login_pageobject.PASSWORD_INPUTBOX)).then(
                Click.on(ccb_login_pageobject.LOGIN_BUTTON)));
    }
    @Then("he will be unable to get access to ccrs application")
    public void he_will_be_unable_to_get_access_to_ccrs_application() {
        theActorInTheSpotlight().should(
                seeThat(CCRSWebElementText.getText(ccb_login_pageobject.ERROR_MSG_SPAN), Matchers.is("Unable to Login"))
        );
    }
}
