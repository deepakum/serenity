package stepdefinitions;

import Utility.Server.ServerUtil;
import Utility.others.DepositControlInfo;
import Utility.SettlementFile.MockBankStatement;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.*;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class BalancedDCStepDefinition {

    Logger logger = LoggerFactory.getLogger(BalancedDCStepDefinition.class);
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static String bankStmtServerPath = environmentVariables.getProperty("CCRS.BankStatement.server.path");
    private String mockedFilePath;
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    @Given("James is playing role of chief cashier")
    public void james_is_playing_role_of_chief_cashier() {
        ccrsLoginStepdefinition.driverSetup();
        ccrsLoginStepdefinition.setTheStage();
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page("James");
        theActorInTheSpotlight().attemptsTo(CCB.login("chief"));
    }
    @When("James balanced deposit control")
    public void james_balanced_deposit_control() {
        //theActorInTheSpotlight().attemptsTo(BalanceTenderControl.to());
        //theActorInTheSpotlight().attemptsTo(BalanceDepositControl.balanced());
        mockedFilePath = MockBankStatement.counterPayment(DepositControlInfo.getExpectedBankInDate(),
                DepositControlInfo.getExpectedEndingBalance(), DepositControlInfo.getDepositControlID());
    }
    @Then("He is able to balanced deposit control")
    public void he_is_able_to_balanced_deposit_control() {
        theActorInTheSpotlight().attemptsTo(DC.setDepositControlChar());
        ServerUtil.uploadFile(mockedFilePath,bankStmtServerPath);
    }
}
