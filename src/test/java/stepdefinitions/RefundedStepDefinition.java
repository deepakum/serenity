package stepdefinitions;

import Utility.Server.ServerUtil;
import Utility.others.BatchFile;
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

public class RefundedStepDefinition {

    Logger logger = LoggerFactory.getLogger(RefundedStepDefinition.class);
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    @When("^James attempts to close refund$")
    public void james_attempts_to_close_refund(String refundMethod) {
        mockXmlFile(BatchFile.SAP_CLEARENCE_FILE,refundMethod);
        String source = getXmlFile(BatchFile.SAP_CLEARENCE_FILE);
        String destination = environmentVariables.getProperty("CCRS.Refund.clearance.server.path");
        ServerUtil.uploadFile(source,destination);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-SAPCL",false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
    @Then("^he will be able to close refund$")
    public void he_will_be_able_to_close_refund() {
        theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("Refunded"));
    }
}
