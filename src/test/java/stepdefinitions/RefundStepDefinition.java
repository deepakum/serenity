package stepdefinitions;

import Utility.CCRS.User;
import Utility.Refund.CashRefund;
import Utility.Refund.RefundMode;
import Utility.Server.ServerUtil;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_refund_initiation_pageobject;
import questions.CCRSWebElementText;
import tasks.*;
import tasks.Batch.Batch;

import static Utility.SettlementFile.MockXml.templatePath;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class RefundStepDefinition {

    Logger logger = LoggerFactory.getLogger(RefundStepDefinition.class);

    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    @Steps
    SAPRefundChannelStepDefinition sapRefundChannelStepDefinition;
    @Steps
    RefundedStepDefinition refundedStepDefinition;
    EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
    String business = "Development & Building Control";
    String refundMethod = "Cheque";
    String account;
    String bank;
    @When("James attempts to initiate refund")
    public void james_attempts_to_initiate_refund(DataTable dataTable) {

        theActorInTheSpotlight().attemptsTo(ReceiptID.fetch());
        business = dataTable.cell(1,0).trim();
        String type = dataTable.cell(1,1).trim();
        refundMethod = dataTable.cell(1,2).trim();
        String reason = dataTable.cell(1,3).trim();
        if(refundMethod.equalsIgnoreCase("GIRO")) {
            account = dataTable.cell(1, 4).trim();
            bank = dataTable.cell(1, 5).trim();
        }
        String application = dataTable.cell(1, 6).trim();
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as("RO");
        theActorInTheSpotlight().attemptsTo(TransactionDetails.add(type,business,"counter"));
        theActorInTheSpotlight().attemptsTo(RefundInitiation.using(business,refundMethod,reason,account,bank,application));
        Helper.switchToFrames("main");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE),
                containsStringIgnoringCase("Case")));
        Helper.switchToFrames("tabPage");
        PropertiesUtil.setProperties("refund.id",
                ccb_refund_initiation_pageobject.CASE_ID_INPUT_BOX.resolveFor(theActorInTheSpotlight()).getAttribute("value").trim());
        theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("Pending Level 1 Approver"));
    }

    @And("he initiates refund for {string} by {string} method siting {string} to avail {string} refund")
    public void heInitiatesRefundForByMethodSitingToAvailRefund(String businessSystem, String refundMode, String reason, String percentage) {

        business = businessSystem;
        refundMethod = refundMode;
        CashRefund.CashRefundBuilder.newInstance().refundInitiation(refundMode,businessSystem,reason,percentage).build();
    }
    @When("he gets first level approval from CO officer")
    public void he_gets_first_level_approval_from_co_officer() {
        Helper.switchAtWindow();
        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as("CO");
        theActorInTheSpotlight().attemptsTo(RefundApproval.to(business));
        //Helper.dismissWarningPopup();
        //Assert.assertTrue(refundCaseStatus(PropertiesUtil.getProperties("refund.id")).trim().equalsIgnoreCase("20"));
        theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("Pending Level 2 Approver"));
    }
    @When("he also gets second level approval from AO officer")
    public void he_also_gets_second_level_approval_from_ao_officer() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as("AO");
        theActorInTheSpotlight().attemptsTo(RefundApproval.to(business));
        //Helper.dismissWarningPopup();
        //Assert.assertTrue(refundCaseStatus(PropertiesUtil.getProperties("refund.id")).trim().equalsIgnoreCase("30"));
        theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("Refund Channel (SAP)"));
    }
    @When("^he generates vendor file by triggering \"([^\"]*)\" batch job$")
    public void he_generates_vendor_file_by_triggering_batch_job(String job) {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as(User.ADMIN);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(job,false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
        String serverPath = null;
        if(PropertiesUtil.getProperties("refund.method").equalsIgnoreCase(RefundMode.CHEQUE)){
            serverPath = "/app/CCBPRSIT/ouaf/SFTP/CM-SAPVF/";
        }else if(PropertiesUtil.getProperties("refund.method").equalsIgnoreCase(RefundMode.GIRO)){
            serverPath = "/app/CCBPRSIT/ouaf/SFTP/CM-SAPVG/";
        }
        ServerUtil.downloadFile(serverPath,templatePath);
        theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("File extracted for SAP"));
        //Assert.assertTrue(refundCaseStatus(PropertiesUtil.getProperties("refund.id")).trim().equalsIgnoreCase("60"));
    }
    @When("^he moves case to SAP channel by triggering \"([^\"]*)\" batch job$")
    public void he_moves_case_to_sap_channel_by_triggering_batch_job(String job) {
        sapRefundChannelStepDefinition.james_attempts_to_move_refund_case_to_sap_refund_channel();
        theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("AP request processed by SAP"));
        //Assert.assertTrue(refundCaseStatus(PropertiesUtil.getProperties("refund.id")).trim().equalsIgnoreCase("70"));
    }
    @When("^he tries to process refund by triggering \"([^\"]*)\" batch job$")
    public void he_tries_to_process_refund_by_triggering_batch_job(String job) {
        refundedStepDefinition.james_attempts_to_close_refund(refundMethod);
        //Assert.assertTrue(refundCaseStatus(PropertiesUtil.getProperties("refund.id")).trim().equalsIgnoreCase("80"));
    }
    @Then("^he will be able to process refund$")
    public void he_will_be_able_to_process_refund() {
        PropertiesUtil.setProperties("CCRS.paymentEventID","");
        PropertiesUtil.setProperties("CCRS.paymentEventID","");
        PropertiesUtil.setProperties("sale.item.type","");
    }

}
