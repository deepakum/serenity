package stepdefinitions;

import Utility.others.Helper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.ui.Select;
import net.thucydides.core.annotations.Steps;
import pageobjects.ccb_sale_Items_pageobject;
import pageobjects.ccb_tc_pageobject;
import tasks.CCB;
import tasks.LaunchMenu;

import java.time.Duration;
import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class BatchJobSubmissionStepDefinition {

    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    Actor james;
    @Given("^(.*) is at the CCRS home page$")
    public void james_is_at_the_ccrs_home_page(String actor) {
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page(actor);
        ccrsLoginStepdefinition.james_provides_credential_as("Admin");
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
    }

    @When("^James searches batch job \"([^\"]*)\" in search menu$")
    public void jamesSearchesBatchJobInSearchMenu(String menu) {
        theActorInTheSpotlight().attemptsTo(LaunchMenu.to(menu));
    }
    @Then("He is able to find the batch job {string}")
    public void heIsAbleToFindTheBatchJob(String batchName) {
       Ensure.thatTheCurrentPage().currentUrl().contains("batchJobSearchPage");
    }

    @When("he selects sale item")
    public void he_selects_sale_item() {
        addSaleItem();
    }
    @When("he adds buyers details")
    public void he_adds_buyers_details() {

        AddBuyerDetailsAndConfirmPayment();
    }

    private static void AddBuyerDetailsAndConfirmPayment() {
        theActorInTheSpotlight().attemptsTo(Enter.keyValues("WIPRO")
                .into(ccb_sale_Items_pageobject.ORGANISATION));
        theActorInTheSpotlight().attemptsTo(Enter.keyValues("370066")
                .into(ccb_sale_Items_pageobject.PIN_CODE));
        ccb_sale_Items_pageobject.PIN_CODE_SEARCH.resolveFor(theActorInTheSpotlight()).click();
        ccb_sale_Items_pageobject.CONTACT_NUMBER.resolveFor(theActorInTheSpotlight())
                .sendKeys("86801794");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.PROCEED_TO_PAY));
        Helper.customWait(3000);
        theActorInTheSpotlight().attemptsTo(Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT)
                .then(Click.on(ccb_tc_pageobject.CONFIRM_PAYMENT)));
        Helper.customWait(10000);
//        try {
//            Runtime.getRuntime().exec("C:\\Users\\anilkodam\\IdeaProjects\\CCRS\\src\\test\\resources\\sendEnter.exe");
//            Helper.customWait(10000);
//            Runtime.getRuntime().exec("C:\\Users\\anilkodam\\IdeaProjects\\CCRS\\src\\test\\resources\\sendEnter.exe");
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        Helper.customWait(10000);
    }

    public static void addSaleItem(){
        Helper.switchToFrames("tabPage","zoneMapFrame_1");
        theActorInTheSpotlight().attemptsTo(CCB.clickAnElement(ccb_sale_Items_pageobject.SALE_ITEM.waitingForNoMoreThan(Duration.ofMillis(5000))));
        List<WebElementFacade> payment = ccb_sale_Items_pageobject.PAYMENT_SERVICE_CATEGORY.resolveAllFor(theActorInTheSpotlight());
        theActorInTheSpotlight().attemptsTo(Select.option("Plans & Publications").from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_CATEGORY));
        theActorInTheSpotlight().attemptsTo(Select.optionNumber(0).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_SUB_CATEGORY));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.SEARCH_SALE_ITEM));
        for(int row = 0 ; row<=2 ; row++){
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.SALE_ITEM_CHECKBOX(String.valueOf(row))));
            theActorInTheSpotlight().attemptsTo(Enter.keyValues("2").into(ccb_sale_Items_pageobject.SALE_ITEM_QUANTITY(String.valueOf(row))));
        }
    }
}


