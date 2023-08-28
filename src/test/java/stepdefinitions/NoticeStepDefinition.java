package stepdefinitions;

import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchStatus;
import Utility.BatchJob.TenderSourceType;
import Utility.SettlementFile.MockBankStatement;
import Utility.SettlementFile.MockXml;
import Utility.Server.ServerUtil;
import Utility.others.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.pages.WebElementState;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.ccb_dc_pageobject;
import pageobjects.ccb_dc_search_pageobject;
import pageobjects.ccb_tc_pageobject;
import tasks.*;
import tasks.Batch.BalanceTenderControl;
import tasks.Batch.Batch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Utility.others.DepositControlInfo.getDepositControlID;
import static Utility.SettlementFile.MockXml.getXmlFile;
import static Utility.SettlementFile.MockXml.mockNoticeFile;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class NoticeStepDefinition {

    Logger logger = LoggerFactory.getLogger(NoticeStepDefinition.class);
    @Steps
    DepositControlStepDefinition dcStepDefinition;
    @Steps
    TenderControlStepDefinition tcStepDefinition;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    static EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
    Map<String, String> result;

    @When("James attempts to create notice")
    public void james_creates_notice_using(DataTable dataTable) {

        List<Map<String, String>> list = dataTable.asMaps();
        result = new HashMap<>();
        list.stream().forEach(map -> {
            result.putAll(map.entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> (String) entry.getValue())));
        });
        mockNoticeFile(BatchFile.EVMS_NOTICE_FILE,result);
        String source = getXmlFile(BatchFile.EVMS_NOTICE_FILE);
        String destination = environmentVariables.getProperty("CCRS.EVMS.XML.server.path");
        ServerUtil.uploadFile(source,destination,true,"xml");
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.LOAD_NEW_VIOLATION,false));
    }
    @Then("he will be to able to create notice")
    public void he_will_be_to_able_to_create_notice() {

    }

    @When("James attempts to search notice")
    public void james_attempts_to_search_notice() {
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page("James");
        ccrsLoginStepdefinition.james_provides_credential_as("Cashier");
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
    }

    @When("he attempts to edit the notice")
    public void he_attempts_to_edit_the_notice(DataTable updatedDataTable) {
        List<Map<String, String>> list = updatedDataTable.asMaps();
        Map<String, String> editMap = new HashMap<>();
        list.stream().forEach(map -> {
            editMap.putAll(map.entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> (String) entry.getValue())));
        });
        result.putAll(editMap);
        MockXml.editNoticeFile(BatchFile.EVMS_NOTICE_FILE,result);
        String source = getXmlFile(BatchFile.EVMS_EDIT_NOTICE_FILE);
        String destination = environmentVariables.getProperty("CCRS.EVMS.editedXML.server.path");
        ServerUtil.uploadFile(source,destination,true,"xml");
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-EVMUP",false));
    }
    @Then("he will be to able to edit the notice")
    public void he_will_be_to_able_to_edit_the_notice() {

    }
    @Then("he will be able to search notice")
    public void he_will_be_able_to_search_notice() {
        theActorInTheSpotlight().attemptsTo(EVMS.searchNoticeAtOneMPortal());
        theActorInTheSpotlight().attemptsTo(EVMS.selectNotice());
        theActorInTheSpotlight().attemptsTo(EVMS.proceedToPay());
        theActorInTheSpotlight().attemptsTo(CCB.addTender("Cash")
                .then(CCB.confirmPayment("")));
        theActorInTheSpotlight().should(eventually(seeThat(
                WebElementQuestion.the(ccb_tc_pageobject.RECEIPT_ID), WebElementState::isCurrentlyVisible)));
        PropertiesUtil.setProperties("CCRS.paymentEventID", ccb_tc_pageobject.RECEIPT_ID
        .resolveFor(theActorInTheSpotlight()).getText());
    }

    @When("^(.*) balances deposit control for notice$")
    public void james_balances_deposit_control_for_notice(String actor) {
//        dcStepDefinition.james_is_at_the_batch_job_search_page(actor);
//        if(RegisterDriver.getOpenDC()>1) {
//            theActorInTheSpotlight().attemptsTo(DC.search());
//        }
//        if(getDepositControlID() == null) {
//            theActorInTheSpotlight().attemptsTo(CCRS.searchBatchJobByName("Batch Job Submission"));
//            theActorInTheSpotlight().attemptsTo(CCRS.selectBatch(theActorInTheSpotlight(),"Batch Job Submission"));
//            theActorInTheSpotlight().attemptsTo(SearchBatchJob.of("CM-CDTPP"));
//            dcStepDefinition.he_provides_tender_source_psp_counter_flag_claim_deposit_control_and_counter_location(
//                    "COUNTER", "COUNTER", "true", "true", "NBR");
//            dcStepDefinition.he_attempts_to_clone_selected_deposit_control();
//        }

                /*\
            1. Open Deposit control search page
            2. Perform search using characteristic value
            3. Look for any open DC
            4. Open DC if not found
        * */
        dcStepDefinition.james_is_at_the_batch_job_search_page(actor);
//        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
//        theActorInTheSpotlight().attemptsTo(DC.searchByChar("NBR"));
        DC.getOpenedDC(TenderSourceType.ONLINE_CASHIERING, BatchStatus.OPEN);
        if(getDepositControlID() == null) {
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION,"Search"));
            dcStepDefinition.james_searches_batch_job_on_the_batch_job_search_page("CM-CDTPP");
            dcStepDefinition.he_provides_tender_source_psp_counter_flag_claim_deposit_control_and_counter_location(
                    "COUNTER", "COUNTER", "true", "true", "NBR","false");
            dcStepDefinition.he_attempts_to_clone_selected_deposit_control();
        }


        //Cashier create TC and add to DC. set TC status to balancing in progress
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
        if(listOfTcs ==0){
            tcStepDefinition.james_attempts_to_add_tender_control_with_tender_source_as("COUNTER");
            theActorInTheSpotlight().attemptsTo(EVMS.searchNoticeAtOneMPortal());
            theActorInTheSpotlight().attemptsTo(EVMS.selectNotice());
            theActorInTheSpotlight().attemptsTo(EVMS.proceedToPay());
            theActorInTheSpotlight().attemptsTo(CCB.addTender("Cash")
                    .then(CCB.confirmPayment("")));
            theActorInTheSpotlight().should(eventually(seeThat(
                    WebElementQuestion.the(ccb_tc_pageobject.RECEIPT_ID), WebElementState::isCurrentlyVisible)));
            PropertiesUtil.setProperties("CCRS.paymentEventID", ccb_tc_pageobject.RECEIPT_ID.resolveFor(theActorInTheSpotlight()).getText());
            tcStepDefinition.he_will_be_able_to_add_tender_control_to_deposit_control();
        } else if(endingBalance.equalsIgnoreCase("S$0.00")) {
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Counter.name(), CashierSubMenu.COUNTER_PAYMENTS));
            theActorInTheSpotlight().attemptsTo(EVMS.searchNoticeAtOneMPortal());
            theActorInTheSpotlight().attemptsTo(EVMS.selectNotice());
            theActorInTheSpotlight().attemptsTo(EVMS.proceedToPay());
            theActorInTheSpotlight().attemptsTo(CCB.addTender("Cash")
                    .then(CCB.confirmPayment("")));
            theActorInTheSpotlight().should(eventually(seeThat(
                    WebElementQuestion.the(ccb_tc_pageobject.RECEIPT_ID), WebElementState::isCurrentlyVisible)));
            PropertiesUtil.setProperties("CCRS.paymentEventID", ccb_tc_pageobject.RECEIPT_ID.resolveFor(theActorInTheSpotlight()).getText());
            tcStepDefinition.he_will_be_able_to_add_tender_control_to_deposit_control();
        }
        else{
            Helper.switchToFrames("main","tabMenu");
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
        }
        theActorInTheSpotlight().attemptsTo(TC.moveToBalancingInProgress());

        //Chief officer
        getDriver().quit();
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page("James");
        theActorInTheSpotlight().attemptsTo(CCB.login("chief"));
        theActorInTheSpotlight().attemptsTo(BalanceTenderControl.to());
        theActorInTheSpotlight().attemptsTo(DC.toBalance());

        String mockedFilePath = MockBankStatement.counterPayment(
                PropertiesUtil.getProperties("dc.expectedBankInDate"),
                PropertiesUtil.getProperties("dc.endingBalanceAmount"),
                PropertiesUtil.getProperties("deposit.control.id"));
        ServerUtil.uploadFile(mockedFilePath, env.getProperty("CCRS.BankStatement.server.path"));
    }

}
