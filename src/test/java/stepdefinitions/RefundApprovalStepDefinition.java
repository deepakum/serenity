package stepdefinitions;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.RefundApproval;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class RefundApprovalStepDefinition {

    Logger logger = LoggerFactory.getLogger(RefundApprovalStepDefinition.class);
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;

    @Given("^(.*) is at the ccrs refund approval page$")
    public void james_is_at_the_ccrs_refund_approval_page(String actor, DataTable dataTable) {
        String officer = dataTable
                .cell(1,0);
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page(actor);
        ccrsLoginStepdefinition.james_provides_credential_as(officer);
    }
    @When("^James attempts to \"([^\"]*)\" refund for \"([^\"]*)\" business system$")
    public void james_attempts_to_refund_for_business_system(String approve, String businessSys) {
        theActorInTheSpotlight().attemptsTo(RefundApproval.to(businessSys));
    }
    @Then("^he will be able to \"([^\"]*)\" refund with case status \"([^\"]*)\"$")
    public void he_will_be_able_to_refund(String approve, String status) {

    }
}
