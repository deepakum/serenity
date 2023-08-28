package stepdefinitions;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.TransactionDetails;
import tasks.RefundInitiation;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class RefundInitiationStepDefinition {

    Logger logger = LoggerFactory.getLogger(RefundInitiationStepDefinition.class);
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;

    @Given("^(.*) is at the ccrs refund page$")
    public void james_is_at_the_ccrs_refund_page(String actor) {
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page(actor);
        ccrsLoginStepdefinition.james_provides_credential_as("RO");
    }
    @When("^James attempts to initiate \"([^\"]*)\" refund for \"([^\"]*)\" business system$")
    public void james_attempts_to_initiate_refund_for_business_system(String refundType, String businessSystem) {
        theActorInTheSpotlight().attemptsTo(TransactionDetails.add(refundType,businessSystem,"counter"));
    }
    @And("^he provides mode of refund to \"([^\"]*)\" and reason as \"([^\"]*)\" for the refund$")
    public void he_provides_mode_of_refund_to_and_reason_as_for_the_refund(String mode, String reason) {
        theActorInTheSpotlight().attemptsTo(RefundInitiation.using(mode, reason,"","","",""));
    }
    @Then("^he will be able to initiate refund$")
    public void he_will_be_able_to_initiate_refund() {

    }
}
