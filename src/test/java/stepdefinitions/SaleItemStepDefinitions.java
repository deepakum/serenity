package stepdefinitions;

import Utility.CCRS.User;
import Utility.CCRS.CCBFrames;
import Utility.CSV.DateUtil;
import Utility.SaleItem.SaleItemStatus;
import Utility.SaleItem.Portal;
import Utility.SaleItem.SaleItem;
import Utility.others.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.actions.CheckCheckbox;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.CCB;
import tasks.LaunchMenu;
import tasks.Navigate;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.serenitybdd.core.Serenity.getDriver;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

public class SaleItemStepDefinitions {

    JavascriptExecutor js = (JavascriptExecutor) getDriver();
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    Logger logger = LoggerFactory.getLogger(SaleItemStepDefinitions.class);
    private String editAmount;
    SaleItem saleItem;
    String newAvailableTillDate;
    @When("he tries to add sale item by choosing {string},{string}, {string} and {string}")
    public void he_tries_to_add_sale_item_by_choosing_and(String paymentService, String subCategory, String itemName, String amount) {
        saleItem = SaleItem.Builder.newInstance().create(paymentService, subCategory, itemName, amount)
                .setPsc(paymentService).setPssc(subCategory).build();
    }
    @When("he attempts to create sale item")
    public void he_attempts_to_create_sale_item() {
        SaleItem.Builder.newInstance().save().build();
    }
    @Then("he should be able to create the sale item")
    public void he_should_be_able_to_create_the_sale_item() {
        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().should(eventually(
                seeThat(WebElementQuestion.the(ccb_user_pageobject.PAGE_TITLE), isVisible())));
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE),
                Matchers.is("Sale Item Display")));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage").then(Switch.toFrame("zoneMapFrame_2")));
        theActorInTheSpotlight().should(
                seeThat(CCRSWebElementText.getText(ccb_sale_Items_pageobject.SALE_ITEM_STATUS_TEXT.waitingForNoMoreThan(Duration.ofMillis(10000))),
                        Matchers.is("INACTIVE")));
        theActorInTheSpotlight().should(
                seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.PAYMENT_SERVICE_CATEGORY_TEXT),
                        Matchers.is(saleItem.getPsc())));
        theActorInTheSpotlight().should(
                seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.PAYMENT_SERVICE_SUB_CATEGORY_TEXT),
                        Matchers.is(saleItem.getPssc())));
        theActorInTheSpotlight().should(
                seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS),
                        Matchers.is("Initiated")));
           if (saleItem.getDynamicCharge()!=null) {
            if (saleItem.getDynamicCharge().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.DYNAMIC_CHARGE_VALUE),
                        Matchers.is("N")));
            } else {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.DYNAMIC_CHARGE_VALUE),
                        Matchers.is("Y")));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.COUNTER_ONLY_VALUE),
                        Matchers.is("N")));
            }
        }
        if(saleItem.getCounterOnly()!=null) {
            if (saleItem.getCounterOnly().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.COUNTER_ONLY_VALUE),
                        Matchers.is("N")));
            } else {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.COUNTER_ONLY_VALUE),
                        Matchers.is("Y")));
            }
        }
        if(saleItem.getMinAmount()!=null) {
            if (saleItem.getMinAmount().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.MINIMUM_APPLICABLE_VALUE),
                        Matchers.is("N")));
            } else {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.MINIMUM_APPLICABLE_VALUE),
                        Matchers.is("Y")));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.MINIMUM_AMOUNT_VALUE),
                        Matchers.notNullValue()));
            }
        }
        if(saleItem.getEnterQty()!=null) {
            if (saleItem.getEnterQty().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.ENTER_QUANTITY_VALUE),
                        Matchers.is("N")));
            } else {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.ENTER_QUANTITY_VALUE),
                        Matchers.is("Y")));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SINGLE_QUANTITY_VALUE),
                        Matchers.is("N")));
            }
        }
        if(saleItem.getSingleQty()!=null) {
            if (saleItem.getSingleQty().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SINGLE_QUANTITY_VALUE),
                        Matchers.is("N")));
            } else {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SINGLE_QUANTITY_VALUE),
                        Matchers.is("Y")));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.ENTER_QUANTITY_VALUE),
                        Matchers.is("N")));
            }
        }
        if(saleItem.getEmail()!=null) {
            if (!saleItem.getEmail().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.EMAIL_ADDRESS_VALUE),
                        Matchers.is(saleItem.getEmail())));
            }
        }
        if(saleItem.getIdType()!=null) {
            if (!saleItem.getIdType().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.ID_TYPE),
                        Matchers.notNullValue()));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.ID_REFERENCE_VALUE),
                        Matchers.notNullValue()));
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.ID_TYPE_VALUE),
                        Matchers.notNullValue()));
            }
        }
        if(saleItem.getComment()!=null) {
            if (!saleItem.getComment().isEmpty()) {
                theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.COMMENTS_TEXT),
                        Matchers.notNullValue()));
            }
        }
    }
    @When("he attempts to edit unit price {string} of sale item")
    public void he_attempts_to_edit_unit_price_of_sale_item(String amount) {
        editAmount = amount;
        SaleItem.Builder.newInstance().edit(amount).build();
    }
    @Then("he should be able to edit the sale item")
    public void he_should_be_able_to_edit_the_sale_item() {
        //SaleItem.Builder.newInstance().verifyEditStatus(editAmount);
        he_should_be_able_to_create_the_sale_item();
    }

    @When("he attempts to send sale item approval request to checker")
    public void he_attempts_to_send_sale_item_approval_request_to_checker() {
        SaleItem.Builder.newInstance().sendForApproval("checker").build();
    }
    @Then("he should be able to send sale item approval to checker")
    public void he_should_be_able_to_send_sale_item_approval_to_checker() {
        SaleItem.Builder.newInstance().verifyApprovalRequest("checker").build();
    }

    @When("he attempts to reject business approval request")
    public void he_attempts_to_reject_business_approval_request() {
        SaleItem.Builder.newInstance().rejectApprovalRequest("checker");
    }
    @Then("he should be able to reject business approval request")
    public void he_should_be_able_to_reject_business_approval_request() {
        SaleItem.Builder.newInstance().verifyRejectedStatus("checker");
    }
    @When("he attempts to send sale item approval request to finance user")
    public void he_attempts_to_send_sale_item_approval_request_to_finance_user() {
        SaleItem.Builder.newInstance().sendForApproval("finance").build();
    }
    @Then("he should be able to send sale item approval to finance user")
    public void he_should_be_able_to_send_sale_item_approval_to_finance_user() {
        SaleItem.Builder.newInstance().verifyApprovalRequest("finance");
    }

    @When("he attempts to approve sale item request")
    public void he_attempts_to_approve_sale_item_request() {
        SaleItem.Builder.newInstance().approve();
    }
    @Then("he should be able to approve sale item request")
    public void he_should_be_able_to_approve_sale_item_request() {
        SaleItem.Builder.newInstance().verifyApprovedStatus();
    }

    @When("he attempts to reject finance approval request")
    public void he_attempts_to_reject_finance_approval_request() {
        SaleItem.Builder.newInstance().rejectApprovalRequest("finance");
    }
    @Then("he should be able to reject finance approval request")
    public void he_should_be_able_to_reject_finance_approval_request() {
        SaleItem.Builder.newInstance().verifyRejectedStatus("finance");
    }

    @When("he attempts to search sale item which is not approved by finance approver")
    public void he_attempts_to_search_sale_item_which_is_not_approved_by_finance_approver() {
        Helper.switchToFrames("main","tabPage","zoneMapFrame_1");
        theActorInTheSpotlight().attemptsTo(CCB.searchSaleItems(PropertiesUtil.getProperties("payment.service.category"),
                PropertiesUtil.getProperties("payment.service.sub.category")));
    }
    @Then("Counter does not display sales items until Finance Approver has approved it")
    public void counter_does_not_display_sales_items_until_finance_approver_has_approved_it() {
        List<WebElementFacade> listOfSaleItems = ccb_sale_Items_pageobject.SEARCH_SALE_TEM_BY_COLUMN_NAME("Item Name").resolveAllFor(theActorInTheSpotlight());
        boolean result = listOfSaleItems.stream().noneMatch(
                ele->ele.getText().trim().equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")));
        theActorInTheSpotlight().attemptsTo(Ensure.that(result).isTrue());
    }

    @Then("Counter does not display recently saved sale item")
    public void counter_does_not_display_recently_saved_sale_item() {
        List<WebElementFacade> listOfSaleItems = ccb_sale_Items_pageobject.SEARCH_SALE_TEM_BY_COLUMN_NAME("Item Name").resolveAllFor(theActorInTheSpotlight());
        boolean result = listOfSaleItems.stream().noneMatch(
                ele->ele.getText().trim().equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")));
        theActorInTheSpotlight().attemptsTo(Ensure.that(result).isTrue());
    }

    @When("James attempts to access to payment summary without filling the required details")
    public void james_attempts_to_access_to_payment_summary_without_filling_the_required_details() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as(User.CASHIER);
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        Helper.switchToFrames("tabPage","zoneMapFrame_1");
        //theActorInTheSpotlight().attemptsTo(Click.on(SaleItemsPageObject.PAYMENT_SUMMARY_TAB));
    }
    @Then("he should not be able to access to payment summary without filling the required details")
    public void he_should_not_be_able_to_access_to_payment_summary_without_filling_the_required_details() {

        js.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)", ccb_sale_Items_pageobject.PAYMENT_SUMMARY_TAB.resolveFor(theActorInTheSpotlight()).getElement());
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
        Alert alert = ThucydidesWebDriverSupport.getDriver().switchTo().alert();
        String alertMessage = alert.getText().trim();
        alert.accept();
        theActorInTheSpotlight().attemptsTo(Ensure.that(alertMessage).contains(Message.PAYMENT_SUMMARY_ERROR_POPUP));
    }

    @And("he tries to add sale item by choosing {string},{string}, {string},{string},{string}, {string},{string},{string},{string},{string},{string} and {string}")
    public void heTriesToAddSaleItemByChoosingAnd(String psc, String pssc, String itemName, String unitPrice, String dynamicCharge, String counterOnly, String minAmount, String enterQty, String singleQty, String email, String idType, String comment) {
        saleItem = SaleItem.Builder.newInstance().create(psc, pssc, itemName, unitPrice)
                .setDynamicCharge(dynamicCharge).setEmail(email).setMinAmount(minAmount)
                .setComment(comment).setCounterOnly(counterOnly).setEnterQty(enterQty)
                .setSingleQty(singleQty).setIdType(idType).setPsc(psc).setPssc(pssc).setAmount(unitPrice).build();
        if(!dynamicCharge.isEmpty()){
            theActorInTheSpotlight().attemptsTo(CheckCheckbox.of(ccb_sale_Items_pageobject.DYNAMIC_CHARGE_CHECKBOX));
        }
        if(!counterOnly.isEmpty()){
            theActorInTheSpotlight().attemptsTo(CheckCheckbox.of(ccb_sale_Items_pageobject.COUNTER_CHECKBOX));
        }
        if(!minAmount.isEmpty()){
            theActorInTheSpotlight().attemptsTo(CheckCheckbox.of(ccb_sale_Items_pageobject.MINIMUM_AMOUNT_APPLICABLE_CHECKBOX)
                    .then(SendKeys.of("1000").into(ccb_sale_Items_pageobject.MINIMUM_AMOUNT_INPUTBOX)));
        }
        if(!enterQty.isEmpty()){
            theActorInTheSpotlight().attemptsTo(CheckCheckbox.of(ccb_sale_Items_pageobject.ENTER_QUANTITY_CHECKBOX));
        }
        if(!singleQty.isEmpty()){
            theActorInTheSpotlight().attemptsTo(CheckCheckbox.of(ccb_sale_Items_pageobject.SINGLE_QUANTITY_CHECKBOX));
        }
        if(!email.isEmpty()){
            theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.EMAIL_ADDRESS_INPUTBOX)
                    .then(SendKeys.of(email).into(ccb_sale_Items_pageobject.EMAIL_ADDRESS_INPUTBOX)));
        }
        if(!idType.isEmpty()){
            theActorInTheSpotlight().attemptsTo(SelectFromOptions.byVisibleText(idType).from(ccb_sale_Items_pageobject.ID_TYPE_DROPDOWN));
            theActorInTheSpotlight().attemptsTo(SendKeys.of("G5588333K").into(ccb_sale_Items_pageobject.ID_NUMBER_INPUTBOX)
                            .thenHit(Keys.RETURN)
                    .then(SendKeys.of("Deepak kumar").into(ccb_sale_Items_pageobject.CUSTOMER_NAME_INPUTBOX)));
        }
        if(!comment.isEmpty()){
            theActorInTheSpotlight().attemptsTo(SendKeys.of(comment).into(ccb_sale_Items_pageobject.COMMENTS_INPUTBOX));
        }
        theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.UNIT_OF_MEASUREMENT_INPUTBOX)
                .then(SendKeys.of("S$100/ticket").into(ccb_sale_Items_pageobject.UNIT_OF_MEASUREMENT_INPUTBOX)
                .then(SendKeys.of("Macpherson").into(ccb_sale_Items_pageobject.PICKUP_LOCATION_INPUTBOX))
                .then(SendKeys.of("5").into(ccb_sale_Items_pageobject.NO_OF_DAYS_INPUTBOX))));
    }

    @Given("he has created sale item")
    public void he_has_created_sale_item(io.cucumber.datatable.DataTable dataTable) {

        theActorInTheSpotlight().attemptsTo(CCB.login(dataTable.cell(1,0)));
        String paymentService = dataTable.cell(1,1);
        String subCategory = dataTable.cell(1,2);
        String itemName = dataTable.cell(1,3);
        String unitPrice = dataTable.cell(1,4);
        saleItem = SaleItem.Builder.newInstance().create(paymentService, subCategory, itemName, unitPrice)
                .setPsc(paymentService).setPssc(subCategory).save().build();
        he_should_be_able_to_create_the_sale_item();
    }
    @When("he tries to edit sale item by choosing {string},{string}, {string},{string},{string}, {string},{string},{string},{string},{string},{string} and {string}")
    public void he_tries_to_edit_sale_item_by_choosing_and(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12) {
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame("main")
                .then(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_1")))));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.EDIT_BUTTON));
        Helper.switchAtWindow();
        heTriesToAddSaleItemByChoosingAnd(string, string2, string3, string4, string5, string6, string7, string8, string9, string10, string11, string12);
        SaleItem.Builder.newInstance().save().build();
    }
    @When("he as a {string} attempts to delete sale item")
    public void he_as_a_attempts_to_delete_sale_item(String string) {
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame("main")
                .then(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_1")))));
        Set<String> baseWindowHandles = getDriver().getWindowHandles();
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.DELETE_BUTTON));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(2))
                .forNoMoreThan(Duration.ofMillis(15000)));
        theActorInTheSpotlight().attemptsTo(Switch.toWindow(getDriver().getWindowHandles().stream()
                .filter(handle -> !handle.equalsIgnoreCase(baseWindowHandles.iterator().next())).collect(Collectors.joining(""))));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofMillis(50000));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_Items_pageobject.OK_BUTTON, isClickable()));
        js.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)", ccb_sale_Items_pageobject.OK_BUTTON.resolveFor(theActorInTheSpotlight()).getElement());
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(driver -> driver.getWindowHandles().equals(baseWindowHandles)).forNoMoreThan(Duration.ofMillis(10000)));
        baseWindowHandles.stream().forEach(handle -> theActorInTheSpotlight().attemptsTo(Switch.toWindow(handle)));

    }
    @Then("he should be able to delete the sale item")
    public void he_should_be_able_to_delete_the_sale_item() {

        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(MakerMenu.Sale_item.name(), SaleItemMenu.sale_item.name()));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                .then(Switch.toFrame("zoneMapFrame_1"))
                .then(SelectFromOptions.byVisibleText("Initiated")
                        .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
        List<WebElementFacade> listOfSaleItems = ccb_sale_Items_pageobject.SEARCH_SALE_TEM_BY_COLUMN_NAME("Item Name").resolveAllFor(theActorInTheSpotlight());
        boolean result = listOfSaleItems.stream().noneMatch(
                ele->ele.getText().trim().equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")));
        theActorInTheSpotlight().attemptsTo(Ensure.that(result).isTrue());
    }

    @And("he attempts to send with queries")
    public void heAttemptsToSendWithQueries() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login("Financier"));
        theActorInTheSpotlight().attemptsTo(SaleItem.Builder.newInstance().saleItemSearch());
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                .then(Switch.toFrame("zoneMapFrame_1"))
                .then(SelectFromOptions.byVisibleText(SaleItemStatus.FINANCE_APPROVAL)
                        .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
        WebElementFacade saleItemWebElement = ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                        theActorInTheSpotlight()).stream()
                .filter(saleItem->saleItem.getText().trim()
                        .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                .collect(Collectors.toList()).get(0);

        theActorInTheSpotlight().attemptsTo(Click.on(saleItemWebElement));
        theActorInTheSpotlight().attemptsTo(Ensure.thatTheCurrentPage().title().containsIgnoringCase("Sale Item Display"));

        theActorInTheSpotlight().attemptsTo(Switch.toFrame("main")
                .then(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_1"))));
        Set<String> baseWindowHandles = getDriver().getWindowHandles();
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_item_search_pageobject.SEND_WITH_QUERIES_BUTTON));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(2))
                .forNoMoreThan(Duration.ofMillis(15000)));
        theActorInTheSpotlight().attemptsTo(Switch.toWindow(getDriver().getWindowHandles().stream()
                .filter(handle->!handle.equalsIgnoreCase(baseWindowHandles.iterator().next())).collect(Collectors.joining(""))));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofMillis(50000));
        theActorInTheSpotlight().attemptsTo(SendKeys.of("Send with Queries").into(ccb_sale_item_search_pageobject.SEND_WITH_QUERIES_COMMENT));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_Items_pageobject.SAVE_BUTTON,isClickable()));
        js.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)",
                ccb_sale_Items_pageobject.SAVE_BUTTON.resolveFor(theActorInTheSpotlight()).getElement());
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(driver-> driver.getWindowHandles().equals(baseWindowHandles)).forNoMoreThan(Duration.ofMillis(10000)));
        baseWindowHandles.stream().forEach(handle->theActorInTheSpotlight().attemptsTo(Switch.toWindow(handle)));
    }

    @Then("he should be able to send with queries")
    public void heShouldBeAbleToSendWithQueries() {
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_2)));
        theActorInTheSpotlight().should(
                seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS),
                        Matchers.is(SaleItemStatus.INITIATED)));
        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().attemptsTo(SaleItem.Builder.newInstance().saleItemSearch());
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_1))
                .then(SelectFromOptions.byVisibleText(SaleItemStatus.SENT_WITH_QUERIES)
                        .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
        ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                        theActorInTheSpotlight()).stream()
                .filter(saleItem->saleItem.getText().trim()
                        .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                .collect(Collectors.toList()).get(0);
    }

    @When("he attempts to edit approved sale item")
    public void heAttemptsToEditApprovedSaleItem() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login("Financier"));
        SaleItem.Builder.newInstance().search(SaleItemStatus.APPROVED).select().clickEditButton().build();
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("uiMap"));
        String dateInString = ccb_sale_item_maintenance_pageobject.AVAILABLE_TILL_DATE.resolveFor(theActorInTheSpotlight()).getAttribute("value");
        newAvailableTillDate = DateUtil.getAvailableTillDate(dateInString,1);
        theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_item_maintenance_pageobject.AVAILABLE_TILL_DATE)
                .then(SendKeys.of(newAvailableTillDate).into(ccb_sale_item_maintenance_pageobject.AVAILABLE_TILL_DATE)));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_item_maintenance_pageobject.SAVE_BUTTON));
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame("main").then(Switch.toFrame("tabPage").then(Switch.toFrame("zoneMapFrame_2")))));

    }

    @Then("he will be able to edit approved sale item")
    public void heWillBeAbleToEditApprovedSaleItem() {
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_display_pageobject.AVAILABLE_TILL_DATE),
                Matchers.is(newAvailableTillDate)));
    }

    @When("he attempts to duplicate approved sale item")
    public void
    heAttemptsToDuplicateApprovedSaleItem() {
        SaleItem.Builder.newInstance().search(SaleItemStatus.APPROVED).select().clickDuplicateButton().build();
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("uiMap"));
        theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.AVAILABLE_TO_DATE_INPUT_BOX)
                .then(Enter.keyValues(DateUtil.getAvailableToDate(DateUtil.getOffsetDate(7))).into(ccb_sale_Items_pageobject.AVAILABLE_TO_DATE_INPUT_BOX)));
        theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.AVAILABLE_FROM_DATE_INPUT_BOX)
                .then(Enter.keyValues(DateUtil.getAvailableToDate(DateUtil.getTodayDate())).into(ccb_sale_Items_pageobject.AVAILABLE_FROM_DATE_INPUT_BOX)));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.SAVE_BUTTON));
    }

    @Then("he will be able to duplicate approved sale item")
    public void heWillBeAbleToDuplicateApprovedSaleItem() {
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_2)));
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_sale_item_display_pageobject.AVAILABLE_FROM_DATE),
                Matchers.is(DateUtil.getAvailableToDate(DateUtil.getTodayDate()))));
    }

    @And("he has saved sale item")
    public void heHasSavedSaleItem(DataTable dataTable) {
        theActorInTheSpotlight().attemptsTo(CCB.login(dataTable.cell(1,0)));
        String paymentService = dataTable.cell(1,1);
        String subCategory = dataTable.cell(1,2);
        String itemName = dataTable.cell(1,3);
        String unitPrice = dataTable.cell(1,4);
        saleItem = SaleItem.Builder.newInstance().create(paymentService, subCategory, itemName, unitPrice)
                .setPsc(paymentService).setPssc(subCategory).save().build();
    }

    @And("he attempts to create sale item by choosing {string},{string}, {string} and {string}")
    public void heAttemptsToCreateSaleItemByChoosingAnd(String psc, String pssc, String itemName, String unitPrice) {
        saleItem = SaleItem.Builder.newInstance().create(psc, pssc, itemName, unitPrice)
                .save().sendForApproval("checker").verifyApprovalRequest("checker")
                .sendForApproval("finance").verifyApprovalRequest("finance")
                .approve().verifyApprovedStatus()
                .setPsc(psc).setPssc(pssc)
                .build();
    }

    public static void main(String[] args) {
        Double amount = 200.00;
        Double gst = 8.00;
        Double gstAmount =  (amount*gst)/100;
        Double totalAmount = amount + gstAmount;
        Assert.assertTrue(totalAmount==215.00);
    }

    @And("He attempts to add sale item to cart")
    public void heAttemptsToAddSaleItemToCart() {
        theActorInTheSpotlight().attemptsTo(Portal.addToCart());
        theActorInTheSpotlight().attemptsTo(Portal.viewCart());
    }

    @And("He add buyer details")
    public void heAddBuyerDetails() {
        theActorInTheSpotlight().attemptsTo(Portal.buyerDetails());
        theActorInTheSpotlight().attemptsTo(Portal.enterBuyerDetails());
    }
}
