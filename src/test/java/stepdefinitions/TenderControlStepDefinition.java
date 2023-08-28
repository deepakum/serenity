package stepdefinitions;

import Utility.BatchJob.Counter;
import Utility.CCRS.User;
import Utility.SaleItem.BillCharType;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.pages.WebElementState;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.*;
import tasks.Batch.AddTenderControl;

import java.util.List;
import java.util.stream.Collectors;

import static Utility.SaleItem.Bill.billCharacterMap;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TenderControlStepDefinition {

    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    @Steps
    BatchJobSubmissionStepDefinition batchJobSubmissionStepDefinition;
    EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
    Actor james;
    Logger logger = LoggerFactory.getLogger(TenderControlStepDefinition.class);
    @Given("^(.*) is at CCRS Counter page$")
    public void james_is_at_ccrs_counter_page(String name) {
        james = theActorCalled(name);
        ccrsLoginStepdefinition.driverSetup();
        ccrsLoginStepdefinition.setTheStage();
        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page(name);
        ccrsLoginStepdefinition.james_provides_credential_as("Cashier");
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
    }

    @Given("^James selects already open deposit control$")
    public void james_selects_already_open_deposit_control() {
        theActorInTheSpotlight().attemptsTo(DC.search());
    }

    @When("^James attempts to add tender control with tender source as \"([^\"]*)\"$")
    public void james_attempts_to_add_tender_control_with_tender_source_as(String tenderSource) {
        theActorInTheSpotlight().attemptsTo(AddTenderControl.toDC(tenderSource));
    }
    @Then("he is able to add tender control")
    public void he_is_able_to_add_tender_control() {

//        theActorInTheSpotlight().attemptsTo(Click.on(CCRSHomePageObject.MENU_BUTTON));
//        Actions action = new Actions(getDriver());
//        WebElement goTo = TenderControlPageObject.COUNTER.waitingForNoMoreThan(Duration.ofMillis(3000)).resolveFor(theActorInTheSpotlight());
//        action.moveToElement(goTo).build().perform();
//        WebElement add = TenderControlPageObject.COUNTER_PAYMENTS.waitingForNoMoreThan(Duration.ofMillis(3000)).resolveFor(theActorInTheSpotlight());
//        action.moveToElement(add).click().perform();

    }

    @And("he adds sale items by choosing payment service category as {string}, payment service sub category as {string}, sale item {string} and quantity {string}")
    public void heAddsSaleItemsByChoosingPaymentServiceCategoryAsPaymentServiceSubCategoryAsSaleItemAndQuantity(String psc, String pssc, String saleItem, String quantity) {
        // need to provide logic for user login based on tenser type
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as(User.CASHIER);
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        theActorInTheSpotlight().attemptsTo(AddSaleItems.toCart(psc,pssc,saleItem,quantity));
    }
    @When("^he adds sale items by choosing payment service category as \"([^\"]*)\", payment service sub category as \"([^\"]*)\", sale item as \"([^\"]*)\" and quantity \"([^\"]*)\"$")
    public void he_adds_sale_items_by_choosing_payment_service_category_as_payment_service_sub_category_as_and_quantity(String paymentServiceCategory, String paymentServiceSubCategory, String quantity) {
        //theActorInTheSpotlight().attemptsTo(AddSaleItems.toCart(paymentServiceCategory,paymentServiceSubCategory,quantity));
    }
    @When("^he provides buyer details, like Organisation as \"([^\"]*)\", postal code as \"([^\"]*)\" and mobile number as \"([^\"]*)\"$")
    public void he_provides_buyer_details_like_organisation_as_postal_code_as_and_mobile_number_as(String org, String pinCode, String mobile) {
        theActorInTheSpotlight().attemptsTo(CCB.AddBuyerDetails(org,pinCode,mobile));
    }
    @When("^he chooses tender type to \"([^\"]*)\" and proceed to make payment$")
    public void he_chooses_tender_type_as_and_proceed_to_make_payment(String tenderType) {
        theActorInTheSpotlight().attemptsTo(CCB.addTender(tenderType).then(CCB.confirmPayment(tenderType)));
        theActorInTheSpotlight().should(eventually(seeThat(
                WebElementQuestion.the(ccb_tc_pageobject.RECEIPT_ID), WebElementState::isCurrentlyVisible)));
        PropertiesUtil.setProperties("CCRS.paymentEventID",
                ccb_tc_pageobject.RECEIPT_ID.resolveFor(theActorInTheSpotlight()).getText());
    }
    @Then("^he will be able to add tender control to deposit control$")
    public void he_will_be_able_to_add_tender_control_to_deposit_control() {
        Helper.customWait(5000);
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_tc_pageobject.RECEIPT_ID, isVisible()));
        theActorInTheSpotlight().should(eventually(seeThat(
                WebElementQuestion.the(ccb_tc_pageobject.RECEIPT_ID), WebElementState::isCurrentlyVisible)));
        PropertiesUtil.setProperties("CCRS.paymentEventID", ccb_tc_pageobject.RECEIPT_ID.resolveFor(theActorInTheSpotlight()).getText());
    }

    @When("^James changes tender control status to \"([^\"]*)\"$")
    public void james_changes_tender_control_status_to(String tenderControlStatus) {
        theActorInTheSpotlight().attemptsTo(CCB.setTenderControlStatus(tenderControlStatus));
    }
    @When("^he attempts to balance it by setting ending balance$")
    public void he_attempts_to_balance_it_by_setting_ending_balance() {
        theActorInTheSpotlight().attemptsTo(CCB.setEndingBalance());
    }
    @Then("^he will be able to set tender control status to balancing in progress$")
    public void he_will_be_able_to_set_tender_control_status_to_balancing_in_progress() {
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.TENDER_CONTROL_INFO)
                ,containsString("Balancing in Progress")));
    }


    @When("^James attempts to set tender control to balancing in progress$")
    public void james_attempts_to_set_tender_control_to_balancing_in_progress() {

//        theActorInTheSpotlight().attemptsTo(Navigate.to(6,3,TenderControlPageObject.TC_SEARCH_MENU));
//        Helper.customWait(3000);
//        Helper.switchAtWindow(1);
//        theActorInTheSpotlight().attemptsTo(setTenderControlCreationDate.toToday());
//        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(CCRSHomePageObject.BATCH_MENU_HEADER)
//                ,is("Tender Control")));
//        Helper.switchToFrames("tabPage");
//        theActorInTheSpotlight().attemptsTo(SelectFromOptions.byIndex(2)
//                .from(TenderControlPageObject.TENDER_CONTROL_STATUS));
//        Helper.switchToFrames("blncGrid");
//        String totalTenderAmount = TenderControlPageObject.TENDER_CONTROL_TABLE(3)
//                .resolveFor(theActorInTheSpotlight()).getText();
//        theActorInTheSpotlight().attemptsTo(Clear.field(TenderControlPageObject.ENDING_BALANCE));
//        theActorInTheSpotlight().attemptsTo(Enter.keyValues(totalTenderAmount)
//                .into(TenderControlPageObject.ENDING_BALANCE));
//        Helper.switchToFrames("main");
//        theActorInTheSpotlight().attemptsTo(Click.on(TenderControlPageObject.SAVE));
//        Helper.customWait(5000);
    }
    @Then("^he is able to set tender control to balancing in progress$")
    public void he_is_able_to_set_tender_control_to_balancing_in_progress() {
        Helper.switchToFrames("tabPage");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.TENDER_CONTROL_INFO)
                ,containsString("Balancing in Progress")));
    }

    @When("James exit the application")
    public void james_exit_the_application() {
        Helper.switchToFrames("main");
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login(User.CHIEF_CASHIER));
    }
    @Then("he is able to leave application successfully")
    public void he_is_able_to_leave_application_successfully() {
        Helper.customWait(3000);
        //theActorInTheSpotlight().attemptsTo(SelectDepositControl.to());
        
    }

    @And("he attempts to add tender control using {string} to the deposit control")
    public void heAttemptsToAddTenderControlUsingToTheDepositControl(String tenderSource) {
        this.selectDC();
        theActorInTheSpotlight().attemptsTo(AddTenderControl.toDC(tenderSource));
    }

    private void selectDC(){
        //ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page("James");
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as(User.CASHIER);
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(DC.searchByChar(Counter.LOCATION));
        theActorInTheSpotlight().attemptsTo(DC.select(PropertiesUtil.getProperties("deposit.control.id")));
        Helper.switchAtWindow();
    }
    @When("he tries to alter counter location to {string}")
    public void he_tries_to_alter_counter_location_to(String string) {
        theActorInTheSpotlight().attemptsTo(EditCounterLocation.to("Counter Location","HSO1"));
    }
    @Then("he will not be able to save different counter location")
    public void he_will_not_be_able_to_save_different_counter_location() {
        try {
            Alert alert = getDriver().switchTo().alert();
            theActorInTheSpotlight().attemptsTo(Ensure.that(alert.getText()).contains("No error popup while changing the location"));
            alert.accept();
        }catch(NoSuchWindowException e){
            System.out.println(e.getMessage());
            Assert.fail("No error popup while changing the location");
            Ensure.reportSoftAssertions();
        }
    }
    @When("he attempts to add tender control using different {string} than that of deposit control")
    public void he_attempts_to_add_tender_control_using_different_than_that_of_deposit_control(String string) {
        selectDC();
        TenderControl.addTenderSource();
    }
    @Then("he should not be able to add tender control with different tender source than that of deposit control")
    public void he_should_not_be_able_to_add_tender_control_with_different_tender_source_than_that_of_deposit_control() {

    }
    @When("he attempts to proceed for payment without selecting sale item")
    public void he_attempts_to_proceed_for_payment_without_selecting_sale_item() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as(User.CASHIER);
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                .then(Switch.toFrame("zoneMapFrame_1")
                .then(Click.on(ccb_sale_Items_pageobject.SALE_ITEM))
                ));
    }
    @Then("he should not be able to make payment without selecting sale item")
    public void he_should_not_be_able_to_make_payment_without_selecting_sale_item() {
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.PROCEED_TO_PAY));
        TenderControl.handleAlert(Message.PURCHASE_MESSAGE);
    }
    @When("he chooses tender type to {string} and proceed to make {string} payment")
    public void he_chooses_tender_type_to_and_proceed_to_make_payment(String tenderType, String percent) {
        theActorInTheSpotlight().attemptsTo(CCB.addTender(tenderType));
        String amountInString = ccb_tc_pageobject.TENDER_AMOUNT_INPUT_BOX.resolveFor(theActorInTheSpotlight()).getAttribute("value")
                .trim().replace(",","");
        Double percentage = 0.0;
        if(percent.equalsIgnoreCase("partial")){
            percentage = 0.5;
        }
        Double amount = Double.parseDouble(amountInString)*percentage;
        theActorInTheSpotlight().attemptsTo(Clear.field(ccb_tc_pageobject.TENDER_AMOUNT_INPUT_BOX)
                .then(Enter.keyValues(String.valueOf(amount)).into(ccb_tc_pageobject.TENDER_AMOUNT_INPUT_BOX)));
    }
    @Then("he should not be able to make partial payment")
    public void he_should_not_be_able_to_make_partial_payment() {
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.CONFIRM_PAYMENT)
                .then(Click.on(ccb_tc_pageobject.CONFIRM_PAYMENT)));
        TenderControl.handleAlert(Message.PARTIAL_PAYMENT_ALERT);
    }
    @Then("he should be able to generate receipt making the Payment")
    public void he_should_be_able_to_generate_receipt_making_the_payment() {
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.RECEIPT_ID));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt("main")));
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE),
                containsString("Payment Event")));


    }
    @Given("he attempts to check purchase details in the bill")
    public void he_attempts_to_check_purchase_details_in_the_bill() {

        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as(User.ADMIN);
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();

        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Financial.name(), FinancialMenu.PAYMENT_EVENT,"Search"));
        theActorInTheSpotlight().attemptsTo(Enter.keyValues(env.getProperty("CCRS.account.id"))
                        .into(ccb_payment_event_search_pageobject.ACCOUNT_ID_INPUTBOX).then(
                                Click.on(ccb_payment_event_search_pageobject.ACCOUNT_ID_SEARCH_BUTTON)
                        ),
                Switch.toFrame("dataframe"),
                WaitUntil.the(ccb_dc_search_pageobject.LIST_OF_DC, isVisible()));
        List<WebElementFacade> paymentEventId = ccb_dc_search_pageobject.SEARCH_RESULT("Payment Event ID").resolveAllFor(theActorInTheSpotlight());
        theActorInTheSpotlight().attemptsTo(Click.on(paymentEventId.stream()
                .filter(ele -> ele.getText().trim().equals(PropertiesUtil.getProperties("CCRS.paymentEventID")))
                .collect(Collectors.toList()).get(0)));
        Helper.switchAtWindow();
        Helper.switchToFrames("tabPage","payGridpaymentGri");
        String matchValue = ccb_payment_event_pageobject.MATCH_VALUE_INPUT_BOX.resolveFor(theActorInTheSpotlight()).getAttribute("value");
        logger.info(()->"matchValue : "+matchValue);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_pageobject.PAY_LIST_ACCOUNT_CONTEXT_ICON));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.scrollToElement(ccb_payment_event_pageobject.GO_TO_BILL_MENU.resolveFor(theActorInTheSpotlight()).getElement());
        action.moveToElement(ccb_payment_event_pageobject.GO_TO_BILL_MENU.resolveFor(theActorInTheSpotlight()).getElement()).build().perform();
        action.moveToElement(ccb_payment_event_pageobject.GO_TO_BILL_SEARCH_MENU.resolveFor(theActorInTheSpotlight()).getElement()).click().perform();
        Helper.switchAtWindow();
        Helper.switchToFrames("dataframe");
        List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
        for(int i=0 ;i<rows.size();i++){
            String matchText = ccb_payment_event_search_pageobject.BILL_ID_SPAN(i).resolveFor(theActorInTheSpotlight()).getText();
            if (matchText.equalsIgnoreCase(matchValue)){
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_search_pageobject.BILL_ID_SPAN(i)));
                Helper.switchAtWindow();
                break;
            }
        }
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE),
                containsString("Bill")));
        Helper.switchToFrames("tabMenu");

    }
    @Then("he should be able see purchase details in the bill")
    public void he_should_be_able_see_purchase_details_in_the_bill() {
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_bill_pageobject.CHAR_TAB));
        Helper.switchToFrames("main","tabPage","BILL_CHAR");
        int rowCount = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        for (int row = 0; row < rowCount; row++) {
            String charType = ccb_bill_pageobject.BILL_CHAR_TYPE(row).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            String charValue = ccb_bill_pageobject.BILL_CHAR_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            switch (charType){
                case BillCharType.BUILDING_NAME:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.BLOCK_NAME:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.COUNTRY:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.CUSTOMER_NAME:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.POSTAL_CODE:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.PHONE_NUMBER:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.MINIMUM_APPLICABLE:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.PAYMENT_SERVICE_CATEGORY:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.PAYMENT_SERVICE_SUB_CATEGORY:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.STREET_NAME:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).containsIgnoringCase(billCharacterMap.get(charType)));
                    break;
                case BillCharType.SALE_ITEM_ID:
                    theActorInTheSpotlight().attemptsTo(Ensure.that(charValue).isNotEmpty());
                    break;
                default:
                    //throw new Exception("Option not available");
            }

        }
    }
}
