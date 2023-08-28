package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Steps;
import tasks.LaunchMenu;
import tasks.SearchBatchJob;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

public class BatchControlStepDefinition {

    @Steps
    BatchJobSubmissionStepDefinition batchJobSubmissionStepDefinition;

    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;

    Actor james;
    @Given("^he has selected batch job$")
    public void he_has_selected_batch_job(DataTable dataTable) {
        james = theActorCalled("James");;
        james.attemptsTo(LaunchMenu.to(dataTable.cell(0,0)));
    }

    @When("James searches batch job {string} in batch control page")
    public void james_searches_batch_job_in_batch_control_page(String batchName) {
        james.attemptsTo(SearchBatchJob.of(batchName));
    }
    @When("he selects latest batch control")
    public void he_selects_latest_batch_control() {

    }
    @Then("He is able to navigate to batch job submission page")
    public void he_is_able_to_navigate_to_batch_job_submission_page() {

    }


}
