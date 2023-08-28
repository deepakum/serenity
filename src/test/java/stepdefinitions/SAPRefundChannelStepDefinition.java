package stepdefinitions;

import Utility.others.BatchFile;
import Utility.others.PropertiesUtil;
import Utility.Server.ServerUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.Batch.Batch;
import tasks.VerifyCaseStatus;

import static Utility.SettlementFile.MockXml.getXmlFile;
import static Utility.SettlementFile.MockXml.mockXmlFile;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class SAPRefundChannelStepDefinition {

    Logger logger = LoggerFactory.getLogger(SAPRefundChannelStepDefinition.class);
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    @When("^James attempts to move refund case to SAP refund channel$")
    public void james_attempts_to_move_refund_case_to_sap_refund_channel() {
        mockXmlFile(BatchFile.AP_STATUS_FILE, PropertiesUtil.getProperties("refund.method"));
        String source = getXmlFile(BatchFile.AP_STATUS_FILE);
        String destination = environmentVariables.getProperty("CCRS.Refund.ap.status.server.path");
        ServerUtil.uploadFile(source,destination);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CMSAPST",false));
        //TODO - batch status to be implemented
//        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
    @Then("^he will be able to move case to SAP refund channel$")
    public void he_will_be_able_to_move_case_to_sap_refund_channel() {
        theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("AP request processed by SAP"));
    }
}
