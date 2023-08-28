package Utility.SaleItem;

import Utility.CCRS.CCBFrames;
import Utility.CSV.DateUtil;
import Utility.others.*;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.hamcrest.Matchers;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_sale_item_display_pageobject;
import pageobjects.ccb_sale_item_search_pageobject;
import pageobjects.ccb_sale_Items_pageobject;
import questions.CCRSWebElementText;
import tasks.CCB;
import tasks.LaunchMenu;
import tasks.Navigate;

import java.time.Duration;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static net.serenitybdd.core.Serenity.getDriver;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

public class SaleItem {
    private String psc;
    private String pssc;
    private String itemName;
    private String amount;
    private String dynamicCharge;
    private String counterOnly;
    private String minAmount;
    private String enterQty;
    private String singleQty;
    private String email;
    private String idType;
    private String comment;

    private SaleItem(Builder builder){
        this.psc = builder.psc;
        this.pssc = builder.pssc;
        this.itemName = builder.itemName;
        this.amount = builder.amount;
        this.comment = builder.comment;
        this.idType = builder.idType;
        this.email = builder.email;
        this.singleQty = builder.singleQty;
        this.enterQty = builder.enterQty;
        this.minAmount = builder.minAmount;
        this.counterOnly = builder.counterOnly;
        this.dynamicCharge = builder.dynamicCharge;
    }

    public String getPsc() {
        return psc;
    }

    public String getPssc() {
        return pssc;
    }

    public String getItemName() {
        return itemName;
    }

    public String getAmount() {
        return amount;
    }

    public String getDynamicCharge() {
        return dynamicCharge;
    }

    public String getCounterOnly() {
        return counterOnly;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public String getEnterQty() {
        return enterQty;
    }

    public String getSingleQty() {
        return singleQty;
    }

    public String getEmail() {
        return email;
    }

    public String getIdType() {
        return idType;
    }

    public String getComment() {
        return comment;
    }

    public static class Builder{
        private String psc;
        private String pssc;
        private String itemName;
        private String amount;
        private String dynamicCharge;
        private String counterOnly;
        private String minAmount;
        private String enterQty;
        private String singleQty;
        private String email;
        private String idType;
        private String comment;
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        public static Builder newInstance()
        {
            return new Builder();
        }

        private Builder() {
        }

        public String getPsc() {
            return psc;
        }

        public String getDynamicCharge() {
            return dynamicCharge;
        }

        public Builder setDynamicCharge(String dynamicCharge) {
            this.dynamicCharge = dynamicCharge;
            return this;
        }

        public String getCounterOnly() {
            return counterOnly;
        }

        public Builder setCounterOnly(String counterOnly) {
            this.counterOnly = counterOnly;
            return this;
        }

        public String getMinAmount() {
            return minAmount;
        }

        public Builder setMinAmount(String minAmount) {
            this.minAmount = minAmount;
            return this;
        }

        public String getEnterQty() {
            return enterQty;
        }

        public Builder setEnterQty(String enterQty) {
            this.enterQty = enterQty;
            return this;
        }

        public String getSingleQty() {
            return singleQty;
        }

        public Builder setSingleQty(String singleQty) {
            this.singleQty = singleQty;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getIdType() {
            return idType;
        }

        public Builder setIdType(String idType) {
            this.idType = idType;
            return this;
        }

        public String getComment() {
            return comment;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setPsc(String psc) {
            this.psc = psc;
            return this;
        }

        public String getPssc() {
            return this.pssc;
        }

        public Builder setPssc(String pssc) {
            this.pssc = pssc;
            return this;
        }

        public String getItemName() {
            return this.itemName;
        }

        public Builder setItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public String getAmount() {
            return amount;
        }

        public Builder setAmount(String amount) {
            this.amount = amount;
            return this;
        }

        public SaleItem build()
        {
            return new SaleItem(this);
        }


        public Builder create(String psc, String pssc, String itemName, String amount,String dynamicCharge, String counterOnly, String minAmount, String enterQty, String singleQty, String email, String idType, String comment) {

            PropertiesUtil.setProperties("payment.service.category",psc);
            PropertiesUtil.setProperties("payment.service.sub.category",pssc);
            Helper.switchToFrames(CCBFrames.MAIN);
            theActorInTheSpotlight().attemptsTo(LaunchMenu.to("Sale Item"));
            Helper.switchAtWindow();
            theActorInTheSpotlight().attemptsTo(Switch.toFrame("uiMap").then(
                    SelectFromOptions.byVisibleText(psc).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_CATEGORY_DROPDOWN))
            );
            theActorInTheSpotlight().attemptsTo(
                    SelectFromOptions.byVisibleText(pssc).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_SUB_CATEGORY_DROPDOWN));
            String currentDate = DateUtil.getBatchBusinessDate("");
            String saleItemName = itemName+"_"+ DateUtil.getCustomDate("dd-MM-yyyy", "dd_MM_yyyy",currentDate)+"_"+new Random().nextInt(99) + 1;
            PropertiesUtil.setProperties("sale.item.name",saleItemName);
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(saleItemName).into(ccb_sale_Items_pageobject.ITEM_NAME_INPUT_BOX));
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(psc+"_"+saleItemName).into(ccb_sale_Items_pageobject.ITEM_DESCRIPTION_INPUT_BOX));
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(amount).into(ccb_sale_Items_pageobject.UNIT_PRICE_INPUT_BOX));
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(DateUtil.getBatchBusinessDate("")).into(ccb_sale_Items_pageobject.AVAILABLE_FROM_DATE_INPUT_BOX));
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(DateUtil.getAvailableToDate(DateUtil.getBatchBusinessDate(""))).into(ccb_sale_Items_pageobject.AVAILABLE_TO_DATE_INPUT_BOX));

            return this;
        }
        public Builder create(String psc, String pssc, String itemName, String amount) {

            PropertiesUtil.setProperties("payment.service.category",psc);
            PropertiesUtil.setProperties("payment.service.sub.category",pssc);
            Helper.switchToFrames(CCBFrames.MAIN);
            //theActorInTheSpotlight().attemptsTo(LaunchMenu.to("Sale Item"));
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(MakerMenu.Add_sale_item.name(), SaleItemMenu.sale_item.name(),"Add"));
            Helper.switchToFrames(CCBFrames.MAIN);
            theActorInTheSpotlight().attemptsTo(Switch.toFrame("uiMap").then(
                    SelectFromOptions.byVisibleText(psc).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_CATEGORY_DROPDOWN))
            );
            theActorInTheSpotlight().attemptsTo(
                    SelectFromOptions.byVisibleText(pssc).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_SUB_CATEGORY_DROPDOWN));
            String currentDate = DateUtil.getBatchBusinessDate("");
            String saleItemName = itemName+"_"+ DateUtil.getCustomDate("dd-MM-yyyy", "dd_MM_yyyy",currentDate)+"_"+new Random().nextInt(99) + 1;
            PropertiesUtil.setProperties("sale.item.name",saleItemName);
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(saleItemName).into(ccb_sale_Items_pageobject.ITEM_NAME_INPUT_BOX));
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(psc+"_"+saleItemName).into(ccb_sale_Items_pageobject.ITEM_DESCRIPTION_INPUT_BOX));
            theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.UNIT_PRICE_INPUT_BOX)
                    .then(Enter.keyValues(amount).into(ccb_sale_Items_pageobject.UNIT_PRICE_INPUT_BOX).thenHit(Keys.RETURN)));
            SaleItemMaintenance.verifyGstAmount(Double.parseDouble(amount));
            theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.AVAILABLE_FROM_DATE_INPUT_BOX)
                    .then(Enter.keyValues(DateUtil.getBatchBusinessDate("")).into(ccb_sale_Items_pageobject.AVAILABLE_FROM_DATE_INPUT_BOX)));
            theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.AVAILABLE_TO_DATE_INPUT_BOX)
                    .then(Enter.keyValues(DateUtil.getAvailableToDate(DateUtil.getBatchBusinessDate(""))).into(ccb_sale_Items_pageobject.AVAILABLE_TO_DATE_INPUT_BOX)));

            return this;
        }
        public Builder save(){
            theActorInTheSpotlight().attemptsTo(
                    Scroll.to(ccb_sale_Items_pageobject.SAVE_BUTTON).then(Click.on(ccb_sale_Items_pageobject.SAVE_BUTTON)));
            return this;
        }
        public Builder edit(String amount){
            Helper.switchToFrames(CCBFrames.MAIN);
            theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                    .then(Switch.toFrame("zoneMapFrame_1"))
                    .then(SelectFromOptions.byVisibleText("Initiated")
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
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.EDIT_BUTTON));
            Helper.switchAtWindow();
            theActorInTheSpotlight().attemptsTo(Switch.toFrame("uiMap").then(WaitUntil.the(ccb_sale_Items_pageobject.UNIT_PRICE_INPUT_BOX,isVisible())
                    .then(Clear.field(ccb_sale_Items_pageobject.UNIT_PRICE_INPUT_BOX)).then(Enter.keyValues(amount).into(ccb_sale_Items_pageobject.UNIT_PRICE_INPUT_BOX))));
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.SAVE_BUTTON));
            Helper.switchAtWindow();
            return this;
        }
        public Builder verifyEditStatus(String editAmount){
            theActorInTheSpotlight().attemptsTo(Ensure.thatTheCurrentPage().title().containsIgnoringCase("Sale Item Display"));
            theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage").then(Switch.toFrame("zoneMapFrame_2")));
            theActorInTheSpotlight().attemptsTo(Ensure.that(ccb_sale_Items_pageobject.UNIT_PRICE.waitingForNoMoreThan(Duration.ofMillis(5000))
                    .resolveFor(theActorInTheSpotlight()).getText()).contains(editAmount)
            );
            return this;
        }

        public Task saleItemSearch() {
            return instrumented(SaleItemSearch.class);
        }

        public Task saleItemAdd() {
            return instrumented(SaleItemAdd.class);
        }
        public Builder sendForApproval(String role){

            if (role.equalsIgnoreCase("checker")) {
                Helper.switchToFrames(CCBFrames.MAIN);
                theActorInTheSpotlight().attemptsTo(SaleItem.Builder.newInstance().saleItemSearch());
                theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                        .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_1))
                        .then(SelectFromOptions.byVisibleText(SaleItemStatus.INITIATED)
                                .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
                WebElementFacade saleItemWebElement = ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                                theActorInTheSpotlight()).stream()
                        .filter(saleItem -> saleItem.getText().trim()
                                .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                        .collect(Collectors.toList()).get(0);
                theActorInTheSpotlight().attemptsTo(Click.on(saleItemWebElement));
                theActorInTheSpotlight().attemptsTo(Ensure.thatTheCurrentPage().title().containsIgnoringCase("Sale Item Display"));

                theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.MAIN)
                        .then(Switch.toFrame(CCBFrames.TAB_PAGE)
                                .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_1))));
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.SEND_TO_BUSINESS_USERS_BUTTON));
            }else if (role.equalsIgnoreCase("finance")){
                theActorInTheSpotlight().attemptsTo(CCB.logout());
                theActorInTheSpotlight().attemptsTo(CCB.login("Checker_DBC"));
                Helper.switchToFrames(CCBFrames.MAIN);
                theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_1"))
                        .then(SelectFromOptions.byVisibleText(SaleItemStatus.BUSINESS_APPROVAL)
                                .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
                WebElementFacade saleItemWebElement = ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                                theActorInTheSpotlight()).stream()
                        .filter(saleItem->saleItem.getText().trim()
                                .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                        .collect(Collectors.toList()).get(0);
                theActorInTheSpotlight().attemptsTo(Click.on(saleItemWebElement));
                Helper.customWait(1000);
                theActorInTheSpotlight().attemptsTo(Ensure.thatTheCurrentPage().title().containsIgnoringCase("Sale Item Display"));

                theActorInTheSpotlight().attemptsTo(Switch.toFrame("main")
                        .then(Switch.toFrame("tabPage")
                                .then(Switch.toFrame("zoneMapFrame_1"))));
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_item_search_pageobject.SEND_TO_FINANCE_USERS_BUTTON));
            }
            return this;
        }
        public Builder verifyApprovalRequest(String role){
            if (role.equalsIgnoreCase("checker")) {
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN
                        ,isNotVisible()));
                Helper.switchAtWindow();
                this.switchToSaleItemSearch();
                //Helper.customWait(3000);
                theActorInTheSpotlight().attemptsTo(
                        WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS,isVisible()).forNoMoreThan(Duration.ofMillis(10000)));
                theActorInTheSpotlight().should(
                        seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS),
                                Matchers.is("Pending For Business User Approval")));

                Helper.switchToFrames(CCBFrames.MAIN);
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.HOME_MENU_BUTTON));
                theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_1"))
                        .then(SelectFromOptions.byVisibleText("Pending For Business User Approval")
                                .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
                ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                                theActorInTheSpotlight()).stream()
                        .filter(saleItem->saleItem.getText().trim()
                                .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                        .collect(Collectors.toList()).get(0);
            }else if(role.equalsIgnoreCase("finance")){
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN
                        ,isNotVisible()));
                Helper.switchAtWindow();
                this.switchToSaleItemSearch();
                theActorInTheSpotlight().should(
                        seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS),
                                Matchers.is("Pending For Finance User Approval")));

                Helper.switchToFrames(CCBFrames.MAIN);
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.HOME_MENU_BUTTON));
                theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_1"))
                        .then(SelectFromOptions.byVisibleText("Pending For Finance User Approval")
                                .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
                WebElementFacade saleItemWebElement = ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                                theActorInTheSpotlight()).stream()
                        .filter(saleItem->saleItem.getText().trim()
                                .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                        .collect(Collectors.toList()).get(0);
            }
            return this;
        }
        public Builder rejectApprovalRequest(String role){

            if (role.equalsIgnoreCase("checker")) {
                theActorInTheSpotlight().attemptsTo(CCB.logout());
                theActorInTheSpotlight().attemptsTo(CCB.login("Checker_DBC"));
                Helper.switchToFrames(CCBFrames.MAIN);
                theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_1"))
                        .then(SelectFromOptions.byVisibleText("Pending For Business User Approval")
                                .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
                WebElementFacade saleItemWebElement = ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                                theActorInTheSpotlight()).stream()
                        .filter(saleItem -> saleItem.getText().trim()
                                .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                        .collect(Collectors.toList()).get(0);
                theActorInTheSpotlight().attemptsTo(Click.on(saleItemWebElement));
                Helper.customWait(1000);
                theActorInTheSpotlight().attemptsTo(Ensure.thatTheCurrentPage().title().containsIgnoringCase("Sale Item Display"));

                theActorInTheSpotlight().attemptsTo(Switch.toFrame("main")
                        .then(Switch.toFrame("tabPage")
                                .then(Switch.toFrame("zoneMapFrame_1"))));
                Set<String> baseWindowHandles = getDriver().getWindowHandles();
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_item_search_pageobject.REJECT_BUTTON));
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(2))
                        .forNoMoreThan(Duration.ofMillis(15000)));
                theActorInTheSpotlight().attemptsTo(Switch.toWindow(getDriver().getWindowHandles().stream()
                        .filter(handle -> !handle.equalsIgnoreCase(baseWindowHandles.iterator().next())).collect(Collectors.joining(""))));

                getDriver().manage().timeouts().pageLoadTimeout(Duration.ofMillis(50000));
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_Items_pageobject.SAVE_BUTTON, isClickable()));

                js.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)", ccb_sale_Items_pageobject.SAVE_BUTTON.resolveFor(theActorInTheSpotlight()).getElement());

                //theActorInTheSpotlight().attemptsTo(JavaScriptClick.on(SaleItemsPageObject.SAVE_BUTTON));
                System.out.println("After save button click");
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(driver -> driver.getWindowHandles().equals(baseWindowHandles)).forNoMoreThan(Duration.ofMillis(10000)));
                baseWindowHandles.stream().forEach(handle -> theActorInTheSpotlight().attemptsTo(Switch.toWindow(handle)));
            }else if(role.equalsIgnoreCase("finance")){
                theActorInTheSpotlight().attemptsTo(CCB.logout());
                theActorInTheSpotlight().attemptsTo(CCB.login("Financier"));
                Helper.switchToFrames(CCBFrames.MAIN);
//                theActorInTheSpotlight().attemptsTo(LaunchMenu.to("Sale Item Search"));
                theActorInTheSpotlight().attemptsTo(SaleItem.Builder.newInstance().saleItemSearch());
                Helper.switchAtWindow();
                theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                        .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_1))
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
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_item_search_pageobject.REJECT_BUTTON));
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(2))
                        .forNoMoreThan(Duration.ofMillis(15000)));
                theActorInTheSpotlight().attemptsTo(Switch.toWindow(getDriver().getWindowHandles().stream()
                        .filter(handle->!handle.equalsIgnoreCase(baseWindowHandles.iterator().next())).collect(Collectors.joining(""))));
                getDriver().manage().timeouts().pageLoadTimeout(Duration.ofMillis(50000));
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_Items_pageobject.SAVE_BUTTON,isClickable()));
                js.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)",
                        ccb_sale_Items_pageobject.SAVE_BUTTON.resolveFor(theActorInTheSpotlight()).getElement());
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(driver-> driver.getWindowHandles().equals(baseWindowHandles)).forNoMoreThan(Duration.ofMillis(10000)));
                baseWindowHandles.stream().forEach(handle->theActorInTheSpotlight().attemptsTo(Switch.toWindow(handle)));
            }

            return this;
        }
        public Builder verifyRejectedStatus(String role){
            if (role.equalsIgnoreCase("checker")) {
                theActorInTheSpotlight().attemptsTo(Switch.toFrame("main").then(Switch.toFrame("tabPage")
                        .then(Switch.toFrame("zoneMapFrame_2"))));
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS, isVisible()));
                theActorInTheSpotlight().should(
                        seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS),
                                Matchers.is("Rejected By Business User")));
            }else if(role.equalsIgnoreCase("finance")){
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN
                        ,isNotVisible()));
                Helper.switchAtWindow();
                theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                        .then(Switch.toFrame("zoneMapFrame_2")));
                theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS,isVisible()));
                theActorInTheSpotlight().should(
                        seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS),
                                Matchers.is("Rejected By Finance User")));
            }
            return this;
        }
        public Builder approve(){
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login("Financier"));
            theActorInTheSpotlight().attemptsTo(SaleItem.Builder.newInstance().saleItemSearch());
            Helper.switchAtWindow();
            theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                    .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_1))
                    .then(SelectFromOptions.byVisibleText("Pending For Finance User Approval")
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
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_item_search_pageobject.APPROVE_BUTTON));

            return this;
        }
        public Builder verifyApprovedStatus(){
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN
                    ,isNotVisible()));
            Helper.switchAtWindow();
            theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                    .then(Switch.toFrame("zoneMapFrame_2")));
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS,isVisible()));
            theActorInTheSpotlight().should(
                    seeThat(CCRSWebElementText.getText(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS),
                            Matchers.is("Approved")));
            return this;
        }
        public Builder search(String saleItemStatus){
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN
                    ,isNotVisible()));
            Helper.switchToFrames(CCBFrames.MAIN);
//            theActorInTheSpotlight().attemptsTo(LaunchMenu.to("Sale Item Search"));
            theActorInTheSpotlight().attemptsTo(SaleItem.Builder.newInstance().saleItemSearch());
            theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE)
                    .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_1))
                    .then(SelectFromOptions.byVisibleText(saleItemStatus)
                            .from(ccb_sale_item_search_pageobject.SALE_ITEM_STATUS_DROPDOWN)));
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.visibilityOfAllElements(
                    ccb_sale_item_search_pageobject.SALE_ITEM_TABLE_ROWS.resolveAllFor(theActorInTheSpotlight())
                            .stream().map(WebElementFacade::getElement).collect(Collectors.toList()))));
            return this;
        };
        public Builder select(){
            WebElementFacade saleItemWebElement = ccb_sale_item_search_pageobject.SEARCH_SALE_ITEM_BY_COLUMN_NAME("Item Name").resolveAllFor(
                            theActorInTheSpotlight()).stream()
                    .filter(saleItem->saleItem.getText().trim()
                            .equalsIgnoreCase(PropertiesUtil.getProperties("sale.item.name")))
                    .collect(Collectors.toList()).get(0);
            theActorInTheSpotlight().attemptsTo(Click.on(saleItemWebElement));
            theActorInTheSpotlight().attemptsTo(Ensure.thatTheCurrentPage().title().containsIgnoringCase("Sale Item Display"));
            return this;
        }
        public Builder clickEditButton(){
            theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.MAIN)
                    .then(Switch.toFrame(CCBFrames.TAB_PAGE)
                            .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_1))));
            theActorInTheSpotlight().attemptsTo(JavaScriptClick.on(ccb_sale_Items_pageobject.EDIT_BUTTON));
            Helper.switchAtWindow();
            return this;
        }
        public Builder switchToSaleItemSearch(){
            theActorInTheSpotlight().attemptsTo(
                    Switch.toFrame(CCBFrames.TAB_PAGE)
                    .then(Switch.toFrame(CCBFrames.ZONE_MAP_Frame_2))
            );
            return this;
        }
        public Builder clickDuplicateButton(){
            theActorInTheSpotlight().attemptsTo(Switch.toFrame("main")
                    .then(Switch.toFrame("tabPage")
                            .then(Switch.toFrame("zoneMapFrame_1"))));
            theActorInTheSpotlight().attemptsTo(JavaScriptClick.on(ccb_sale_item_display_pageobject.DUPLICATE_BUTTON));
            Helper.switchAtWindow();
            return this;
        }
    }

    public SaleItem create(){
        return this;
    };
    public SaleItem rejectApprovalRequest(String role){
        return this;
    };
    public SaleItem verifyRejectedStatus(String role){
        return this;
    };
    public SaleItem sendForApproval(String role){
        return this;
    };
    public SaleItem verifyApprovalRequest(String role){
        return this;
    };
    public SaleItem verifyApprovedStatus(){
        return this;
    }
    public SaleItem approve(){
        return this;
    }
    public SaleItem edit(){
        return this;
    };
    public SaleItem verifyEditStatus(){
        return this;
    }
    public SaleItem save(){
        return this;
    };
    public SaleItem search(String saleItemStatus){
        return this;
    };
    public SaleItem select(){
        return this;
    };
    public SaleItem clickEditButton(){return this;}
    public SaleItem clickDuplicateButton(){return this;}

    public static void main(String[] args) {
        SaleItem saleItem = SaleItem.Builder.newInstance().setMinAmount("200").build();
        System.out.println(saleItem.minAmount);
    }
}
