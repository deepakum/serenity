package stepdefinitions;

import Utility.CSV.DateUtil;
import Utility.others.Helper;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.thucydides.core.annotations.Steps;
import pageobjects.ccb_batch_job_submission_pageobject;
import pageobjects.ccb_dc_pageobject;
import questions.CCRSWebElementText;
import tasks.Batch.BalanceTenderControl;
import tasks.Batch.DuplicateJob;
import tasks.CCB;
import tasks.DC;
import tasks.SearchBatchJob;
import tasks.Batch.EditBatchJobData;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.core.Is.is;

public class DepositControlStepDefinition {

    Logger logger = LoggerFactory.getLogger(DepositControlStepDefinition.class);

    @Steps
    BatchJobSubmissionStepDefinition batchJobSubmissionStepDefinition;

    @Given("^(.*) is at the batch job search page$")
    public void james_is_at_the_batch_job_search_page(String actor) {
        batchJobSubmissionStepDefinition.james_is_at_the_ccrs_home_page(actor);
    }

    @When("^James searches batch job \"([^\"]*)\" on the batch job search page$")
    public void james_searches_batch_job_on_the_batch_job_search_page(String batch) {
        theActorInTheSpotlight().attemptsTo(SearchBatchJob.of(batch));
    }

    @When("^he provides tender source \"([^\"]*)\", PSP \"([^\"]*)\", counter Flag \"([^\"]*)\", claim deposit control \"([^\"]*)\" ,counter location \"([^\"]*)\" and business system \"([^\"]*)\"$")
    public void he_provides_tender_source_psp_counter_flag_claim_deposit_control_and_counter_location(String tendersouce, String psp, String counterFlag, String cdc, String location, String erpFlag) {
        theActorInTheSpotlight().attemptsTo(EditBatchJobData.using(tendersouce,psp,counterFlag,cdc,location, erpFlag));
    }

    @When("he attempts to clone selected deposit control")
    public void he_attempts_to_clone_selected_deposit_control() {
        theActorInTheSpotlight().attemptsTo(DuplicateJob.to());
    }
    @Then("He will be able to create deposit control by selecting the existing deposit control")
    public void he_will_be_able_to_create_deposit_control_by_selecting_the_existing_deposit_control() {
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_batch_job_submission_pageobject.RUN_STATUS),
                containsString("Complete")));
    }
    @When("he attempts to balance deposit control")
    public void he_attempts_to_balance_deposit_control() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login("chief"));
        theActorInTheSpotlight().attemptsTo(BalanceTenderControl.to());
        theActorInTheSpotlight().attemptsTo(DC.toBalance());
    }
    @Then("he should be able to balance deposit control")
    public void he_should_be_able_to_balance_deposit_control() {
        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_pageobject.DC_STATUS_INFO)
                ,containsString("Balanced")));
    }
    @Then("he should be create deposit control by running batch job")
    public void he_should_be_create_deposit_control_by_running_batch_job() {
        theActorInTheSpotlight().attemptsTo(Ensure.that(DateUtil.compareDCCreationDate()).hasValue().isEqualTo(0));
    }
}
