package tasks;

import Utility.BatchJob.Counter;
import Utility.CCRS.CCBFrames;
import Utility.Payment.NetsPOS;
import Utility.Depositontrol.Status;
import Utility.Payment.TenderSource;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.hamcrest.core.Is;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.Batch.BalanceDC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static org.hamcrest.CoreMatchers.is;
import static pageobjects.ccb_refund_initiation_pageobject.TABLE_HEADER;

public class DC {

    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    static Logger logger = LoggerFactory.getLogger(DC.class);

    public static Task search(String date, String tenderSource, String dcStatus){
        if(date.isEmpty()){
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(getCurrentDate()).into(ccb_dc_search_pageobject.DATE_INPUT_BOX));
        }
        return Task.where("search deposit control",
                SelectFromOptions.byVisibleText(tenderSource).from(ccb_dc_search_pageobject.TENDER_SOURCE_TYPE),
                SelectFromOptions.byVisibleText(dcStatus).from(ccb_dc_search_pageobject.DEPOSIT_CONTROL_STATUS),
                Click.on(ccb_dc_search_pageobject.DC_SEARCH_BUTTON)
//                Switch.toFrame("dataframe"),
//                WaitUntil.the(ccb_dc_search_pageobject.LIST_OF_DC, isVisible())
                );
    }

    public static Task searchByChar(String charValue){
        return Task.where("search deposit control",
                WaitUntil.the(ccb_dc_search_pageobject.CHARACTERISTIC_TYPE_INPUTBOX, isVisible()),
                Enter.keyValues("CM-CLOC").into(ccb_dc_search_pageobject.CHARACTERISTIC_TYPE_INPUTBOX),
                SendKeys.of(charValue).into(ccb_dc_search_pageobject.CHARACTERISTIC_VALUE_INPUTBOX),
                Click.on(ccb_dc_search_pageobject.CHAR_SEARCH_BUTTON),
                Switch.toFrame("dataframe"),
                WaitUntil.the(ccb_dc_search_pageobject.LIST_OF_DC, isVisible())
        );
    }

    public static Task searchPaymentEventByAccountID(String accountID){
        Helper.switchAtWindow(1);
        return Task.where("search payment event using account ID",
                WaitUntil.the(ccb_payment_event_search_pageobject.ACCOUNT_ID_INPUTBOX, isVisible()).then
                        (Enter.keyValues(accountID)
                        .into(ccb_payment_event_search_pageobject.ACCOUNT_ID_INPUTBOX).then(
                                Click.on(ccb_payment_event_search_pageobject.ACCOUNT_ID_SEARCH_BUTTON)
                        )),
                Switch.toFrame("dataframe"),
                WaitUntil.the(ccb_dc_search_pageobject.LIST_OF_DC, isVisible())
        );
    }

    public static Task select(String dc){
        String tenderSourceType = null;
        if (PropertiesUtil.getProperties("tender.source").equalsIgnoreCase("COUNTER")) {
            tenderSourceType = Counter.TENDER_SOURCE_TYPE;
        }else if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase("NETS-POS")){
            tenderSourceType = NetsPOS.TENDER_SOURCE_TYPE;
        }
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame(CCBFrames.DATAFRAME)));
        List< WebElementFacade > listOfOpenDc = new ArrayList<>();
        List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
        System.out.println("rows : "+rows.size());
        if (rows.size() == 0){
            throw new RuntimeException("deposit control row not found..");
        }
        for(int row=0;row<rows.size();row++){
            if(ccb_dc_search_pageobject.DEPOSIT_CONTROL_STATUS(row)
                    .resolveFor(theActorInTheSpotlight()).getText().trim()
                    .equalsIgnoreCase("Open") && ccb_dc_search_pageobject.TENDER_SOURCE_TYPE(row).resolveFor(theActorInTheSpotlight())
                    .getText().trim().equalsIgnoreCase(tenderSourceType)) {
                listOfOpenDc.add(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID(row).resolveFor(theActorInTheSpotlight()));
            }
        }
        return Task.where("select deposit ID",
            Click.on(listOfOpenDc.stream().filter(ele -> ele.getText().trim().equals(dc) ).collect(Collectors.toList()).get(0))
        );
    }

    public static String getOpenedDC(String tenderSourceType, String batchStatus){
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(DC.searchByChar(Counter.LOCATION));
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame(CCBFrames.DATAFRAME)));
        List< WebElementFacade > listOfOpenDc = new ArrayList<>();
        List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
//        List<String> listOfTableHeaders = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
//                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9]"," ").trim()).collect(Collectors.toList());
//        int dcsCol = listOfTableHeaders.indexOf("Deposit Control Status".toUpperCase()) + 1;
//        int tstCol = listOfTableHeaders.indexOf("Tender Source Type".toUpperCase()) + 1;
        for(int row=0;row<5;row++){
            String status = ccb_dc_search_pageobject.DEPOSIT_CONTROL_STATUS(row).resolveFor(theActorInTheSpotlight())
                    .getText().trim();
            String tenderSource = ccb_dc_search_pageobject.TENDER_SOURCE_TYPE(row).resolveFor(theActorInTheSpotlight())
                    .getText().trim();
            logger.info(()->String.format("status = %s",status));
            if(status.equalsIgnoreCase(batchStatus) && tenderSource.equalsIgnoreCase(tenderSourceType)) {
                listOfOpenDc.add(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID(row).resolveFor(theActorInTheSpotlight()));
                PropertiesUtil.setProperties("dc.create.date.time", ccb_dc_search_pageobject.CREATE_DATE_AND_TIME(row).resolveFor(theActorInTheSpotlight())
                        .getText().trim());
                break;
            }
        }
        String dcNumber = "";
        if(listOfOpenDc.size()>0) {
            dcNumber = listOfOpenDc.stream().findFirst().get().getText();
            DepositControlInfo.setDepositControlID(dcNumber);
            PropertiesUtil.setProperties("deposit.control.id", dcNumber);
        }else{
            PropertiesUtil.setProperties("deposit.control.id", dcNumber);
        }
        theActorInTheSpotlight().attemptsTo(Switch.toTheOtherWindow());
        Helper.switchToFrames("main");

        return dcNumber;
    }

    public static Performable search(String dcID){

        return Task.where("search deposit control by ID",
                Enter.keyValues(dcID).into(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID),
                Click.on(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID_SEARCH)
        );
    }

    public static Task search(){
        return instrumented(SearchOpenDC.class);
    }

    public static Task toBeSelected(int row){
        return Task.where("select a DC",
                Click.on(ccb_dc_search_pageobject.DC_INFO(row, 3)),
                SwitchToWindow.targetWindow(),
                WaitUntil.the(ccb_user_pageobject.CRRS_HOME_HEADER, isVisible()));
    }
    private static Task selectDate(){

        return Task.where("Choose a date",
            Click.on(ccb_dc_search_pageobject.DATE_BUTTON).afterWaitingUntilEnabled(),
            SwitchToWindow.targetWindow(),
            Click.on(ccb_dc_search_pageobject.DATE_ACCEPT),
            Switch.toParentFrame());
    }

    public static Performable toBalance(){
        return instrumented(BalanceDC.class);
    }

    public static Performable setDepositControlChar(){
        return instrumented(DcCharacteristics.class);
    }

    public static Performable settlementDate(){
        return instrumented(SettlementDate.class);
    }

    public static Performable selectDC(){
        logger.info(()->"Search and select balanced deposit control");
        String depositControlID = PropertiesUtil.getProperties("deposit.control.id");
        List<WebElementFacade> listOfDCs = ccb_dc_search_pageobject.SEARCH_RESULT("Deposit Control ID").resolveAllFor(theActorInTheSpotlight());
        return  Task.where("select a dc",
                Click.on(listOfDCs.stream().filter(ele ->
                        ele.getText().trim().equals(depositControlID))
                        .collect(Collectors.toList()).get(0)),
                SwitchToWindow.targetWindow()
        );
    }
    private static String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public static Task getStatus(String status){
        return Task.where("get DC status",
                Navigate.toMenu(
                        CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()),
                DC.search(PropertiesUtil.getProperties("deposit.control.id")),
                Switch.toTheOtherWindow(),
                Switch.toFrame("main"),
                Switch.toFrame("tabMenu"),
                Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU),
                Switch.toFrame("main"),
                Switch.toFrame("tabPage"),
                Switch.toFrame("DEP_CTL_CHAR")
                );
    }

    public static Performable verifyStatus(String status){

        Helper.switchToFrames("tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                , Is.is("Characteristic Type")));
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String dcStatus = null;
        for (int row = 0; row < rows; row++) {
            String charType = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(row).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Reconcilation Status Deposit Control")) {
                dcStatus = ccb_dc_char_pageobject.RECONCILIATION_STATUS_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            }
        }
        return Task.where("verify dc status",Ensure.that(dcStatus).containsIgnoringCase(status));
    }

    public static Performable verifyDCStatus(String status){
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(DC.search("", TenderSource.STRIPE, Status.balancingInProgress));
        theActorInTheSpotlight().attemptsTo(DC.selectDC());
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage"));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_dc_pageobject.DC_STATUS_INFO,isVisible()));
        String batchInfo = CCB.getText(ccb_dc_pageobject.DC_STATUS_INFO);
        return Task.where("verify dc status",Ensure.that(batchInfo).containsIgnoringCase(status));
    }

    public static Task selectDCByLocation(){
        return Task.where("select deposit control using location",
//                CCRS.logout(),
//                CCRS.loginAs(Roles.CASHIER),
//                Navigate.toMenu(CashierMenu.Financial.name(),
//                        CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()),
//                DC.searchByChar(Counter.LOCATION),
                DC.select(PropertiesUtil.getProperties("deposit.control.id")),
                SwitchToWindow.targetWindow());
    }
    public static Task selectDCByID(){
        String dcNumber = PropertiesUtil.getProperties("deposit.control.id");
        return Task.where("select deposit control by ID",
                WaitUntil.the(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID,isVisible()),
                Enter.keyValues(dcNumber).into(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID),
                Click.on(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID_SEARCH));
    }
}
