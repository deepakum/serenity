package tasks;

import Utility.CCRS.CCBFrames;
import Utility.CSV.DateUtil;
import Utility.SettlementFile.MockChequeFile;
import Utility.Payment.CounterInfo;
import Utility.Payment.PaymentMode;
import Utility.Payment.TenderType;
import Utility.others.*;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.conditions.Check;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.ui.Select;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.serenitybdd.core.Serenity.getDriver;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;
import static org.hamcrest.core.Is.is;

public class CCB {

    Logger logger = LoggerFactory.getLogger(CCB.class);
    public static WebDriver driver;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public static Task login(String role) {
        String username;
        String password;
        String name = null;
        if (role.equalsIgnoreCase("Admin")) {
            username = environmentVariables.getProperty("CCRS.Admin.username");
            password = environmentVariables.getProperty("CCRS.Admin.password");
        } else if (role.equalsIgnoreCase("Cashier")) {
            username = environmentVariables.getProperty("CCRS.Cashier.username");
            password = environmentVariables.getProperty("CCRS.Cashier.password");
        }else if (role.equalsIgnoreCase("Chief")) {
            username = environmentVariables.getProperty("CCRS.ChiefCashier.username");
            password = environmentVariables.getProperty("CCRS.ChiefCashier.password");
        }else if (role.equalsIgnoreCase("RO")) {
            username = environmentVariables.getProperty("CCRS.RO.username");
            password = environmentVariables.getProperty("CCRS.RO.password");
        }else if (role.equalsIgnoreCase("CO")) {
            username = environmentVariables.getProperty("CCRS.CO.username");
            password = environmentVariables.getProperty("CCRS.CO.password");
        }else if (role.equalsIgnoreCase("AO")) {
            username = environmentVariables.getProperty("CCRS.AO.username");
            password = environmentVariables.getProperty("CCRS.AO.password");
        }else{
            username = environmentVariables.getProperty("CCRS."+role+".username");
            password = environmentVariables.getProperty("CCRS."+role+".password");
            name = environmentVariables.getProperty("CCRS."+role+".name");
        }

        Config.setCurrentUser(role);
        Config.setUserName(username);
        if(name!=null)
            Config.setName(name);

        //getDriver().navigate().to("http://177.26.254.132:6700/ouaf/");
        return Task.where("Login to ccb application",
                WaitUntil.the(ExpectedConditions.visibilityOf(
                        ccb_login_pageobject.USERNAME_INPUTBOX.resolveFor(theActorInTheSpotlight()))),
                Enter.keyValues(username).into(ccb_login_pageobject.USERNAME_INPUTBOX),
                Enter.keyValues(password).into(ccb_login_pageobject.PASSWORD_INPUTBOX),
                Click.on(ccb_login_pageobject.LOGIN_BUTTON),
                Switch.toDefaultContext(),
                Switch.toFrame(CCBFrames.MAIN),
                setParentWindowHandle()
        );
    }

    public static Performable setParentWindowHandle(){
        return instrumented(WindowHandles.class);
    }
    public static Task searchBatchJobByName(String batchName){
        Helper.switchToFrames(CCBFrames.MAIN);
        return Task.where("search batch job",
                WaitUntil.the(ccb_user_pageobject.SEARCH_MENU,WebElementStateMatchers.isVisible())
                        .then(Enter.keyValues(batchName).into(ccb_user_pageobject.SEARCH_MENU)),
                WaitUntil.the(ccb_user_pageobject.SEARCH_MENU_OPTIONS,WebElementStateMatchers.isVisible())
        );
    }

    public static Task selectMenu(String batchName){
        return Task.where("select the menu",
                WaitUntil.the(ccb_user_pageobject.SEARCH_MENU_OPTIONS,WebElementStateMatchers.isVisible()),
                Click.on(ccb_user_pageobject.SEARCH_MENU_OPTIONS
                        .resolveAllFor(theActorInTheSpotlight()).get(2)
//                        .stream().filter(ele->ele.getText().equals(batchName))
//                        .findFirst().get()
                ));
    }

    public static Task searchBatch(String batchName){
        Helper.switchToNewWindow();
        return Task.where("search batch #batchName",
                WaitUntil.the(ccb_user_pageobject.BATCH_CODE,isVisible()).forNoMoreThan(Duration.ofMillis(10000)).then(
                Clear.field(ccb_user_pageobject.BATCH_CODE).then(Enter.keyValues(batchName).into(ccb_user_pageobject.BATCH_CODE))),
                WaitUntil.the(ccb_user_pageobject.SEARCH_BATCH, isVisible())
                        .then(Click.on(ccb_user_pageobject.SEARCH_BATCH))
                );
    }

    public static Task batchRunTreeSearch(String batchName){
        return Task.where("search batch #batchName",
                Enter.keyValues(batchName).into(ccb_user_pageobject.BATCH_CODE),
                Click.on(ccb_user_pageobject.BATCH_RUN_TREE_SEARCH)
        );
    }

    public static Task clickAnElement(Target target){
        return Task.where("clicking a #target",
                Click.on(target));
    }

    public static Task refresh(){
        return Task.where("Refresh to update job status",
                Click.on(ccb_user_pageobject.REFRESH));
    }

    public static Task duplicateAndQueueJob(){
        return Task.where("duplicate and queue the job",
                Click.on(ccb_user_pageobject.DUPLICATE_AND_QUEUE));
    }

    public static Task logout(){
        return Task.where("logging out",
                Switch.toDefaultContext(),
                Switch.toFrame(CCBFrames.MAIN)
                .then(Click.on(ccb_tc_pageobject.LOGGED_IN_USER)),
                Click.on(ccb_tc_pageobject.LOGOUT));
    }

    public static Task reLogin(String user){
        return Task.where("re-login",
                Switch.toDefaultContext(),
                Switch.toFrame(CCBFrames.MAIN)
                        .then(Check.whether(!ccb_user_pageobject.LOGGED_IN_USER_SPAN
                                .resolveFor(theActorInTheSpotlight())
                                .containsText("English System")).andIfSo(Click.on(ccb_tc_pageobject.LOGGED_IN_USER).then
                                (Click.on(ccb_tc_pageobject.LOGOUT)).then(login(user))))
        );
    }

    public static Task switchUiView(){
        return Task.where("switch UI view",
                Switch.toDefaultContext(),
                Switch.toFrame("main").then(Switch.toFrame("visualUserPage"))
                        .then(Click.on(ccb_tc_pageobject.LOGGED_IN_USER)),
                Click.on(ccb_user_pageobject.SWITCH_UI_VIEW_MENU_OPTION));
    }

    public static Task goTotenderControl(){
        return Task.where("Navigate to tender control",
                Click.on(ccb_tc_pageobject.TENDER_CONTROL_MENU),
                Click.on(ccb_tc_pageobject.GOTO_TC_ICON));
    }

    public static Task searchSaleItems(String paymentServiceCategory, String paymentServiceSubCategory){

        return Task.where("Choose sale items",
                Click.on(ccb_sale_Items_pageobject.SALE_ITEM),
                WaitUntil.the(ccb_sale_Items_pageobject.PAYMENT_SERVICE_CATEGORY,isVisible()),
                Select.option(paymentServiceCategory).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_CATEGORY),
                Select.option(paymentServiceSubCategory).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_SUB_CATEGORY),
                Click.on(ccb_sale_Items_pageobject.SEARCH_SALE_ITEM)
        );
    }
    public static Task AddBuyerDetails(String org, String pinCode, String mobile){

        return Task.where("Add buyer details such as #org, #pinCode and #mobile",
                Enter.keyValues(org).into(ccb_sale_Items_pageobject.ORGANISATION),
                Enter.keyValues(pinCode).into(ccb_sale_Items_pageobject.PIN_CODE),
                Click.on(ccb_sale_Items_pageobject.PIN_CODE_SEARCH),
                Enter.keyValues(mobile).into(ccb_sale_Items_pageobject.CONTACT_NUMBER),
                Click.on(ccb_sale_Items_pageobject.PROCEED_TO_PAY)
        );
    }

    public static Task addTender(String tenderType){

        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_tc_pageobject.CONFIRM_PAYMENT,isVisible()));
        Task task = null;
        switch (tenderType) {
            case TenderType.CASH:
                task = Task.where("Add tender type to #tenderType",
                        Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT),
                        Select.option(tenderType).from(ccb_tc_pageobject.TENDER_TYPE)
                );
                break;
            case TenderType.CHEQUE:
                String chequeNo = MockChequeFile.getChequeDetails();
                task =  Task.where("Cheque tender type",
                        Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT),
                        Select.option(tenderType).from(ccb_tc_pageobject.TENDER_TYPE),
                        Enter.keyValues(chequeNo).into(ccb_tc_pageobject.CHEQUE_STRING_INPUT_BOX)
                );
                break;
            case TenderType.CASH_CARD:
                task =  Task.where("Cash card tender type",
                        Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT),
                        Select.option(tenderType).from(ccb_tc_pageobject.TENDER_TYPE),
                        Enter.keyValues(CounterInfo.CAN_NUMBER).into(ccb_counter_payable_pageobject.CAN_NUMBER_TEXTBOX),
                        Enter.keyValues(DateUtil.getTransactionDatTime()).into(ccb_counter_payable_pageobject.TRANSACTION_DATE_TIME_TEXTBOX)
                );
                break;
            case TenderType.CREDIT_CARD:
                task =  Task.where("Credit card tender type",
                        Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT),
                        Select.option(tenderType).from(ccb_tc_pageobject.TENDER_TYPE)
                );
                break;
            case TenderType.FLASH_PAY:
                return Task.where("Flash pay tender type",
                        Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT),
                        Select.option(tenderType).from(ccb_tc_pageobject.TENDER_TYPE),
                        Enter.keyValues(CounterInfo.CAN_NUMBER).into(ccb_counter_payable_pageobject.CAN_NUMBER_TEXTBOX),
                        Enter.keyValues(DateUtil.getTransactionDatTime()).into(ccb_counter_payable_pageobject.TRANSACTION_DATE_TIME_TEXTBOX)
                );
            case TenderType.NETS:
                task =  Task.where("Nets tender type",
                        Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT),
                        Select.option(tenderType).from(ccb_tc_pageobject.TENDER_TYPE)
                );
                break;
            case TenderType.QR_CODE:
                task =  Task.where("QR code tender type",
                        Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT),
                        Select.option(tenderType).from(ccb_tc_pageobject.TENDER_TYPE),
                        Clear.field(ccb_counter_payable_pageobject.TERMINAL_RESPONSE_TEXTBOX).then(
                                Enter.keyValues(CounterInfo.TERMINAL_RESPONSE).
                                        into(ccb_counter_payable_pageobject.TERMINAL_RESPONSE_TEXTBOX))

                );
                break;
        }
        return task;
    }

    public static Task confirmPayment(String tenderType){
        return Task.where("confirm payment",
                Scroll.to(ccb_tc_pageobject.CONFIRM_PAYMENT).then(
                        Click.on(ccb_tc_pageobject.CONFIRM_PAYMENT)
                ),
                WarningPopup.dismiss(ccb_tc_pageobject.CONFIRM_PAYMENT,tenderType),
                WaitUntil.the(ccb_tc_pageobject.RECEIPT_ID, WebElementStateMatchers.isVisible())
        );
    }

    public static Task setTenderControlStatus(String tenderControlStatus){
        return Task.where("Setting tender control status to #tenderControlStatus",
                Click.on(ccb_tc_pageobject.TENDER_CONTROL_MENU),
                Switch.toDefaultContext(),
                Switch.toFrame("main"),
                Switch.toFrame("tabPage"),
                Switch.toFrame("Section2_tndrCtlGrd").then(Click.on(ccb_tc_pageobject.GOTO_TC_ICON)),
                Switch.toDefaultContext(),
                Switch.toFrame("main"),
                Switch.toFrame("tabPage").then(SelectFromOptions.byVisibleText(tenderControlStatus)
                        .from(ccb_tc_pageobject.TENDER_CONTROL_STATUS))
        );
    }

    public static Task selectDepositControlByID(){
        System.out.println("Config.getCurrentUser() : "+Config.getCurrentUser());
        if (Config.getCurrentUser() != "chief") {
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        }else {
            theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        }

        return Task.where("select deposit control from DC search page using ID",
                WaitUntil.the(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID, isVisible()),
                Enter.keyValues(PropertiesUtil.getProperties("deposit.control.id")).into(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID),
                Click.on(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID_SEARCH)
                );
    }

    public static Task setEndingBalance(){
        Helper.switchToFrames("blncGrid");
        return Task.where("set ending balance",
                Clear.field(ccb_tc_pageobject.ENDING_BALANCE),
                Enter.keyValues(ccb_tc_pageobject.TENDER_CONTROL_TABLE(3)
                        .resolveFor(theActorInTheSpotlight()).getText())
                        .into(ccb_tc_pageobject.ENDING_BALANCE),
                Switch.toDefaultContext(),
                Switch.toFrame("main"),
                OneAlertPopup.accept(ccb_tc_pageobject.SAVE),
                Switch.toDefaultContext(),
                Switch.toFrame("main"),
                Switch.toFrame("tabPage"),
                WaitUntil.the(ccb_tc_pageobject.TENDER_CONTROL_INFO,isVisible())
        );
    }

    public static Task selectTenderControlMenu(){
        return Task.where("Navigate to Tender control menu",
        Switch.toFrame("tabMenu").then(Click.on(ccb_tc_pageobject.TENDER_CONTROL_MENU)));
    }

    public  static Task selectTC(){
        Helper.switchToFrames("main","tabPage","Section2_tndrCtlGrd");
        return Task.where("select TC",
                Click.on(ccb_tc_pageobject.GOTO_TC_ICON));
    }

    public static Task searchCaseUsingPersonID(String idNumber){
        return Task.where("search case",
            Enter.keyValues(idNumber).into(ccb_refund_initiation_pageobject.PERSON_ID_TEXTBOX),
            Click.on(ccb_refund_initiation_pageobject.PERSON_ID_SEARCH_BUTTON),
                Switch.toFrame("dataframe"),
                WaitUntil.the(ccb_dc_search_pageobject.LIST_OF_DC, WebElementStateMatchers.isVisible())
                );
    }

    public static Task searchCaseUsingAccountID(String idNumber){
        return Task.where("search case",
                Enter.keyValues(idNumber).into(ccb_refund_initiation_pageobject.ACCOUNT_ID_TEXTBOX),
                Click.on(ccb_refund_initiation_pageobject.ACCOUNT_ID_SEARCH_BUTTON),
                Switch.toFrame("dataframe"),
                WaitUntil.the(ccb_dc_search_pageobject.LIST_OF_DC, WebElementStateMatchers.isVisible())
        );
    }

    public static void switchToNewWindow(){
        String baseWin = getDriver().getWindowHandle();
        System.out.println("baseWin : "+baseWin);
        for(String winHandle : getDriver().getWindowHandles()){
            System.out.println("winHandle : "+winHandle);
            if(!winHandle.equals(baseWin)){
                getDriver().switchTo().window(winHandle);
                getDriver().switchTo().defaultContent();
            }
        }
    }

    public static void switchToWindow(String windowTitle) {
        Set<String> windows = getDriver().getWindowHandles();
        for (String window : windows) {
            getDriver().switchTo().window(window);
            if (getDriver().getTitle().contains(windowTitle)) {
                return;
            }
        }
    }

    public static void switchToOtherWindow(){
        String cHandle = getDriver().getWindowHandle();
        String newWindowHandle = null;
        Set<String> allWindowHandles = getDriver().getWindowHandles();
        for (int i = 0; i < 20; i++) {
            if (allWindowHandles.size() > 1) {
                for (String allHandlers : allWindowHandles) {
                    if (!allHandlers.equals(cHandle))
                        newWindowHandle = allHandlers;
                }
                getDriver().switchTo().window(newWindowHandle);
                break;
            }
        }
        if (cHandle == newWindowHandle) {
            throw new RuntimeException("Time out - No window found");
        }
    }

    /*
    * Parameters : Payment type
    * Function : upload mocked file to CCRS server
    * */
    public static Performable processSettlementFile(String tenderSource, String tenderType){

        switch (tenderSource) {
            case PaymentMode.STRIPE:
                return Instrumented.instanceOf(StripeSettlementMockup.class).withProperties(tenderSource,tenderType);
            default:
                return null;
        }
    }
    public static String getText(Target target){
        return target.resolveFor(theActorInTheSpotlight()).getText().trim();
    }
    public static String getTargetText(Target target){
        return target.resolveAllFor(theActorInTheSpotlight()).stream().findFirst().get().getText().trim();
    }
    public static int getSize(Target target){
        return target.resolveAllFor(theActorInTheSpotlight()).size();
    }
    public static List<String> getListText(Target target){
        return target.resolveAllFor(theActorInTheSpotlight()).stream().map(WebElementFacade::getText)
                .collect(Collectors.toList());
    }
    public static String getTextUsingAttribute(Target target, String attribute){
        return target.resolveFor(theActorInTheSpotlight()).getAttribute(attribute).trim();
    }

    public static WebDriver getDriver(){
        return BrowseTheWeb.as(theActorInTheSpotlight()).getDriver();
    }

    public static JavascriptExecutor getJs(){
        driver = BrowseTheWeb.as(theActorInTheSpotlight()).getDriver();
        return (JavascriptExecutor) driver;
    }
    public static WebElement getWebElement(Target target){
        return target.resolveFor(theActorInTheSpotlight());
    }
}
