package stepdefinitions;

import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchFactory;
import Utility.BatchJob.BatchStatus;
import Utility.BatchJob.TenderSourceType;
import Utility.SettlementFile.MockBankStatement;
import Utility.Server.ServerUtil;
import Utility.others.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import pageobjects.ccb_dc_pageobject;
import pageobjects.ccb_dc_search_pageobject;
import pageobjects.ccb_tc_pageobject;
import tasks.*;
import tasks.Batch.BalanceTenderControl;
import tasks.Batch.Batch;

import static Utility.others.DepositControlInfo.getDepositControlID;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class BankReconciliationStepDefinition {

    @Steps
    DepositControlStepDefinition dcStepDefinition;
    @Steps
    TenderControlStepDefinition tcStepDefinition;
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    @Steps
    BalancedDCStepDefinition balancedDCStepDefinition;
    static EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();

    @When("^(.*) balances deposit control$")
    public void james_balances_deposit_control(String actor) throws Exception {

        /*\
            1. Open Deposit control search page
            2. Perform search using characteristic value
            3. Look for any open DC
            4. Open DC if not found
        * */
        dcStepDefinition.james_is_at_the_batch_job_search_page(actor);
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(DC.searchByChar("NBR"));
        DC.getOpenedDC(TenderSourceType.ONLINE_CASHIERING, BatchStatus.OPEN);
        if(getDepositControlID() == null) {
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION,"Search"));
            dcStepDefinition.james_searches_batch_job_on_the_batch_job_search_page("CM-CDTPP");
            dcStepDefinition.he_provides_tender_source_psp_counter_flag_claim_deposit_control_and_counter_location(
                    "COUNTER", "COUNTER", "true", "true", "NBR","false");
            dcStepDefinition.he_attempts_to_clone_selected_deposit_control();

            BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).create("counter", "tenderType");
        }

        /*\Play role of a cashier and perform following
            1. Select a DC
            2. Add TC to DC
            3. Add a sale items
            4. Set TC to Balancing in progress by setting ending balance
          **/
        getDriver().quit();
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page("James");
        ccrsLoginStepdefinition.james_provides_credential_as("Cashier");
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        //tcStepDefinition.james_selects_already_open_deposit_control();
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(DC.searchByChar("NBR"));
        theActorInTheSpotlight().attemptsTo(DC.select(PropertiesUtil.getProperties("deposit.control.id")));
        Helper.switchAtWindow();
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_TENDER_CONTROL_MENU_TAB));
        Helper.switchToFrames("main","tabPage","Section2_tndrCtlGrd");
        int listOfTcs = ccb_tc_pageobject.TENDEDER_CONTROL_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String endingBalance = null;
        if(listOfTcs>0){ endingBalance = ccb_dc_search_pageobject.SEARCH_RESULT("Expected Ending Balance")
                .resolveFor(theActorInTheSpotlight()).getText().trim();}
        if(listOfTcs==0){
            tcStepDefinition.james_attempts_to_add_tender_control_with_tender_source_as("COUNTER");
            tcStepDefinition.he_adds_sale_items_by_choosing_payment_service_category_as_payment_service_sub_category_as_and_quantity("Plans & Publications","","2");
            tcStepDefinition.he_provides_buyer_details_like_organisation_as_postal_code_as_and_mobile_number_as("WIPRO","370066","86801794");
            tcStepDefinition.he_chooses_tender_type_as_and_proceed_to_make_payment("Cash");
            tcStepDefinition.he_will_be_able_to_add_tender_control_to_deposit_control();
        }else if(endingBalance.equalsIgnoreCase("S$0.00")){
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Counter.name(), CashierSubMenu.COUNTER_PAYMENTS));
            tcStepDefinition.he_adds_sale_items_by_choosing_payment_service_category_as_payment_service_sub_category_as_and_quantity("Plans & Publications","","2");
            tcStepDefinition.he_provides_buyer_details_like_organisation_as_postal_code_as_and_mobile_number_as("WIPRO","370066","86801794");
            tcStepDefinition.he_chooses_tender_type_as_and_proceed_to_make_payment("Cash");
            tcStepDefinition.he_will_be_able_to_add_tender_control_to_deposit_control();
        }
        else{
            Helper.switchToFrames("main","tabMenu");
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
        }
        theActorInTheSpotlight().attemptsTo(TC.moveToBalancingInProgress());

        /*\Play role of a Chief cashier and perform following
            1. Balance TC
            2. Balance DC
          **/
        getDriver().quit();
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page("James");
        theActorInTheSpotlight().attemptsTo(CCB.login("chief"));
        theActorInTheSpotlight().attemptsTo(BalanceTenderControl.to());
        theActorInTheSpotlight().attemptsTo(DC.toBalance());

        //Mock bank statements
        String mockedFilePath = MockBankStatement.counterPayment(
                PropertiesUtil.getProperties("dc.expectedBankInDate"),
                PropertiesUtil.getProperties("dc.endingBalanceAmount"),
                PropertiesUtil.getProperties("deposit.control.id"));

        //upload mocked statements to the server
        ServerUtil.uploadFile(mockedFilePath, env.getProperty("CCRS.BankStatement.server.path"));
    }
    @And("he uploads bank statement")
    public void he_uploads_bank_statement() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login("Admin"));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.UPLOAD_BANK_STATEMENT,true));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
    @And("he attempts to reconcile bank statement")
    public void he_attempts_to_reconcile_bank_statement() {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.BANK_RECONCILIATION,true));
    }
    @Then("^he is able to do bank reconciliation$")
    public void he_is_able_to_do_bank_reconciliation() {
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
}
