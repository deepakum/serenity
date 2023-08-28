package stepdefinitions;

import Utility.others.Helper;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import pageobjects.ccb_batch_job_submission_pageobject;
import questions.CCRSWebElementText;
import tasks.*;
import tasks.Batch.DuplicateJob;

import static Utility.others.DepositControlInfo.getDepositControlID;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsString;

public class PaymentReconStepDefinition {

    Logger logger = LoggerFactory.getLogger(PaymentReconStepDefinition.class);

    @Steps
    BatchJobSubmissionStepDefinition batchJobSubmissionStepDefinition;

    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    @Steps
    TenderControlStepDefinition tenderControlStepDefinition;
    @Steps
    DepositControlStepDefinition depositControlStepDefinition;

    String dc_id;

    @When("^(.*) opens deposit control$")
    public void james_opens_deposit_control(String actor) {
        dc_id = getDepositControlID();
        if(dc_id == null) {
            Helper.switchAtWindow(0);
            batchJobSubmissionStepDefinition.jamesSearchesBatchJobInSearchMenu("Batch Job Submission");
            theActorInTheSpotlight().attemptsTo(SearchBatchJob.of("CM-CDTPP"));
            //theActorInTheSpotlight().attemptsTo(SetDepositControlValues.using(ts, psp, counterFlag, "COUNTER", "NBR"));
            theActorInTheSpotlight().attemptsTo(DuplicateJob.to());
            theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_batch_job_submission_pageobject.RUN_STATUS),
                    containsString("Complete")));
        }
        Helper.switchAtWindow(0);
        depositControlStepDefinition.james_is_at_the_batch_job_search_page(actor);
        depositControlStepDefinition.james_searches_batch_job_on_the_batch_job_search_page("CM-CDTPP");
        if(getDepositControlID() == null) {
            depositControlStepDefinition.he_provides_tender_source_psp_counter_flag_claim_deposit_control_and_counter_location("COUNTER", "COUNTER", "true", "true", "NBR","false");
            depositControlStepDefinition.he_attempts_to_clone_selected_deposit_control();
        }
    }
    @When("he adds tender control and makes payment")
    public void he_adds_tender_control_and_makes_payment() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as("Cashier");
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        tenderControlStepDefinition.james_selects_already_open_deposit_control();
        tenderControlStepDefinition.james_attempts_to_add_tender_control_with_tender_source_as("COUNTER");
        tenderControlStepDefinition.he_adds_sale_items_by_choosing_payment_service_category_as_payment_service_sub_category_as_and_quantity("Plans & Publications","","2");
        tenderControlStepDefinition.he_provides_buyer_details_like_organisation_as_postal_code_as_and_mobile_number_as("WIPRO","370066","86801794");
        tenderControlStepDefinition.he_chooses_tender_type_as_and_proceed_to_make_payment("Cash");
        tenderControlStepDefinition.he_will_be_able_to_add_tender_control_to_deposit_control();
    }
    @When("he sets tender control to balancing in progress")
    public void he_sets_tender_control_to_balancing_in_progress() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as("Cashier");
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        theActorInTheSpotlight().attemptsTo(TC.moveToBalancingInProgress());
    }

    @When("^(.*) upload bank statement$")
    public void he_upload_bank_statement(String James) {
//        batchJobSubmissionStepDefinition.james_is_at_the_ccrs_home_page(James);
//        theActorInTheSpotlight().attemptsTo(BatchJobSubmission.to("CM-UPBNK",true));

    }
    @Then("^he is able to upload bank statement$")
    public void he_is_able_to_upload_bank_statement() {
//        theActorInTheSpotlight().attemptsTo(BatchJobStatus.of("CM-UPBNK"));
    }

    @When("^(.*) perform bank reconciliation$")
    public void james_perform_bank_reconciliation(String James) {
//        batchJobSubmissionStepDefinition.james_is_at_the_ccrs_home_page(James);
//        theActorInTheSpotlight().attemptsTo(BatchJobSubmission.to("CM-BNKRN",true));
    }

}
