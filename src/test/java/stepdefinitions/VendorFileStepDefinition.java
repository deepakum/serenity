package stepdefinitions;

import Utility.Server.ServerUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.Batch.Batch;

import static Utility.SettlementFile.MockXml.templatePath;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class VendorFileStepDefinition {

    String batchJob;
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    @When("^James attempts to trigger \"([^\"]*)\" batch job to generate vendor file$")
    public void james_attempts_to_trigger_batch_job_to_generate_vendor_file(String jobName) {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(jobName,false));
    }
    @Then("^he will be able to generate vendor file to the location \"([^\"]*)\"$")
    public void he_will_be_able_to_generate_vendor_file_to_the_location(String serverLocation) {
        theActorInTheSpotlight().attemptsTo(Batch.status());
        ServerUtil.downloadFile(serverLocation,templatePath);
    }

}
