package tasks;

import Utility.CCRS.Item;
import Utility.CCRS.CCBFrames;
import Utility.CSV.CSVParser;
import Utility.CSV.StripeCsvBean;
import Utility.Payment.TenderSource;
import Utility.Refund.RefundMode;
import Utility.SettlementFile.MockChequeFile;
import Utility.SettlementFile.NetsDebit;
import Utility.Payment.PaymentMode;
import Utility.Payment.PortalPayment;
import Utility.Payment.TenderType;
import Utility.SettlementFile.Nets_pos;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.*;
import questions.CCRSWebElementText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utility.others.Helper.getWebDriver;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.is;

public class MockSettlementFile implements Task {

    static Logger logger = LoggerFactory.getLogger(MockSettlementFile.class);
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    public String mode;
    public static List<StripeCsvBean> creditCardBeanList = new ArrayList<>();

    public MockSettlementFile(String mode){
        this.mode = mode;
    }
    public static MockSettlementFile ofPayment(String mode){
        return Instrumented.instanceOf(MockSettlementFile.class).withProperties(mode);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info(()->"Fetching transaction data started..");
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.PAYMENT_EVENT, SubSubMenu.SEARCH.name()));
        String product = PortalPayment.PortalBuilder.newInstance().getProduct();

        String paymentEventID = getPaymentEventID();
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_payment_event_search_pageobject.PAYMENT_EVENT_INPUTBOX, WebElementStateMatchers.isVisible())
                .then(Enter.keyValues(paymentEventID).into(ccb_payment_event_search_pageobject.PAYMENT_EVENT_INPUTBOX))
                .then(Click.on(ccb_payment_event_search_pageobject.PAYMENT_EVENT_SEARCH_BUTTON)));
        Helper.switchToParentWindow();
        //Tender tab
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_pageobject.TENDERS_MENU_TAB));

        //Tender control
        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_pageobject.TENDER_CONTROL_CONTEXT_MENU));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_payment_event_pageobject.GO_TO_TC_CONTROL_MENU.resolveFor(theActorInTheSpotlight())).build().perform();
        action.moveToElement(ccb_payment_event_pageobject.TC_SEARCH_MENU.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Tender Control")));
        Helper.switchToFrames("tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TENDERS_MENU_LABEL));
        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.GET_MORE_BUTTON));
        Helper.switchAtWindow();

        List<Map<String, String>> csvMockDataList = new ArrayList<>();

        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        int listOfTcs = ccb_tc_pageobject.TENDEDER_CONTROL_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        for(int row = 0 ; row < listOfTcs ; row++) {
            Map<String, String> map = new HashMap<>();
            String tcCreatedDateAndTime = ccb_dc_search_pageobject.ROW_SEARCH_RESULT(row,"Create Date/Time".toUpperCase())
                    .resolveFor(theActorInTheSpotlight()).getAttribute("innerText").trim();
            String amount = ccb_dc_search_pageobject.ROW_SEARCH_RESULT(row,"Tender Amount".toUpperCase())
                    .resolveFor(theActorInTheSpotlight()).getAttribute("innerText").trim();
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.GO_TO_PAYMENT_EVENT_LABEL(row)));

            map.put("CreatedDateAndTime",tcCreatedDateAndTime);
            map.put("Amount",amount);
            map.put("MerchantID",getMerchantID());
            map.put("PaymentIntentID", getExtReferenceID());
            csvMockDataList.add(map);
            //Navigate back using history back button
            Helper.switchToFrames("main");
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.HISTORY_GOBACK_BUTTON));
            Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.GET_MORE_BUTTON));
            Helper.switchAtWindow();
            Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        }
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON));
        Helper.switchToFrames("main");
        action.moveToElement(ccb_dc_pageobject.GO_TO_DC.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Deposit Control")));
        Helper.switchToFrames("main","tabPage");
        String DC = CCB.getTextUsingAttribute(ccb_dc_pageobject.DC_NUMBER_TEXTBOX,"value");
        PropertiesUtil.setProperties("deposit.control.id",DC);
        String totalTenderAmount = CCB.getText(ccb_dc_pageobject.TOTAL_TENDER_AMOUNT_TEXTBOX);
        PropertiesUtil.setProperties("dc.endingBalanceAmount",totalTenderAmount);
        Map<String, String> depositControlMap = new HashMap<>();
        depositControlMap.put("TotaltenderAmount",totalTenderAmount);
        Nets_pos.dcInfo();
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.customWait(1000);
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
//        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
//                ,is("Characteristic Type".toUpperCase())));
        int dcCharRows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String settlementDate = null;
        for (int k = 0; k < dcCharRows; k++) {
            String charType = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Settlement Date")) {
                settlementDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                PropertiesUtil.setProperties("dc.settlement.date",settlementDate);
            }
        }
        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.HOME_MENU_BUTTON));
        depositControlMap.put("SettlementDate",settlementDate);
        depositControlMap.put("DepositControl",DC);
        logger.info(()->"Fetching transaction data finished..");
        if(mode.equalsIgnoreCase(PaymentMode.STRIPE)) {
            CSVParser.updateMultiLineCSV(csvMockDataList, depositControlMap);
        }else if(mode.equalsIgnoreCase(PaymentMode.BRAINTREE)) {
            CSVParser.updateBrainTreeCSV(csvMockDataList, depositControlMap);
        } else if(mode.equalsIgnoreCase(PaymentMode.PAYPAL)) {
            CSVParser.updatePaypalCSV(csvMockDataList, depositControlMap);
        }else if(PropertiesUtil.getProperties("refund.method").equalsIgnoreCase(RefundMode.CREDIT_CARD)) {
            creditCardBeanList = CSVParser.stripeRefundSettlementCSV(csvMockDataList, depositControlMap);
        }else if(mode.equalsIgnoreCase(PaymentMode.CREDIT_CARD)) {
            MockChequeFile.mockCCSettlementFile(csvMockDataList, depositControlMap);
        }else if(mode.equalsIgnoreCase(PaymentMode.ENETS_DEBIT)) {
            NetsDebit.mockEnetsDebitSettlement(csvMockDataList, depositControlMap);
        }
    }

    private String getAccountID(String product) {
        String accountID;

        if(product.equalsIgnoreCase(Item.NOTICE)) {
            if(mode.equalsIgnoreCase(TenderType.STRIPE)) {
                accountID = environmentVariables.getProperty("CCRS.notice.stripe.account.id");
            }
        }

        if(mode.equalsIgnoreCase(TenderType.CREDIT_CARD)) {
            accountID = environmentVariables.getProperty("CCRS.account.id");
        }else if(mode.equalsIgnoreCase(TenderType.STRIPE)) {
                accountID = environmentVariables.getProperty("CCRS.account.id");
         }else if(product.equalsIgnoreCase(Item.SALE_ITEM)) {
            accountID = environmentVariables.getProperty("CCRS.OPH.account.id");
         }else if(mode.equalsIgnoreCase(TenderType.STRIPE) && product.equalsIgnoreCase(Item.NOTICE)) {
             accountID = environmentVariables.getProperty("CCRS.notice.stripe.account.id");
        }else {
            accountID = environmentVariables.getProperty("CCRS.stripe.account.id");
        }
        System.out.println("account ID : "+accountID);
        return accountID;
    }

    private static String getPaymentEventID() {
        String paymentEventID;
        if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase(TenderSource.COUNTER)){
            paymentEventID = PropertiesUtil.getProperties("CCRS.paymentEventID");
        }else{
            paymentEventID = PropertiesUtil.getProperties("portal.paymentEventID");
        }
        System.out.println("payment event ID : "+paymentEventID);
        return paymentEventID;
    }

    private String getMerchantID() {
        Helper.switchToFrames("main", "tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main", "tabPage", "PEVT_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                , is("Characteristic Type".toUpperCase())));
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String referenceID = null;
        for (int k = 0; k < rows; k++) {
            String charType = ccb_dc_char_pageobject.PAYMENT_CHAR_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Merchant Transaction Reference ID")) {
                referenceID = ccb_dc_char_pageobject.CHAR_MERCHANT_REFERENCE_ID_TEXTBOX(k)
                        .resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                break;
            } else if (charType.equalsIgnoreCase("Reference Number")) {
                referenceID = ccb_dc_char_pageobject.CHAR_MERCHANT_REFERENCE_ID_TEXTBOX(k)
                        .resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                break;
            }
        }
        return referenceID;
    }

    private String getExtReferenceID(){
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_pageobject.TENDERS_MENU_TAB));
        Helper.switchToFrames("main","tabPage");
        String paymentIntentID = ccb_payment_event_pageobject.EXT_REFERENCE_ID_TEXTBOX.resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();

        return paymentIntentID;
    }


}
