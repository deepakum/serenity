package tasks;

import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchFactory;
import Utility.CCRS.CCBFrames;
import Utility.CSV.CSVParser;
import Utility.Server.ServerUtil;
import Utility.others.*;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.interactions.Actions;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.Batch.Batch;

import java.util.List;
import java.util.stream.Collectors;

import static Utility.SettlementFile.MockXml.getXmlFile;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.is;

public class StripeSettlementMockup implements Task {
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    String tenderSource;
    String tenderType;

    public StripeSettlementMockup(String tenderSource){
        this.tenderSource = tenderSource;
        this.tenderSource = tenderSource;
    }
    public static StripeSettlementMockup ofPayment(String tenderSource, String tenderType){
        return Instrumented.instanceOf(StripeSettlementMockup.class).withProperties(tenderSource, tenderType);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.PAYMENT_EVENT, SubSubMenu.SEARCH.name()));
        String accountID = environmentVariables.getProperty("CCRS.stripe.account.id");
        theActorInTheSpotlight().attemptsTo(DC.searchPaymentEventByAccountID(accountID));
        List<WebElementFacade> listOfTC = ccb_dc_search_pageobject.SEARCH_RESULT("Payment Event ID").resolveAllFor(theActorInTheSpotlight());
        theActorInTheSpotlight().attemptsTo(Click.on(listOfTC.stream()
                .filter(ele -> ele.getText().trim().equals(PropertiesUtil.getProperties("portal.paymentEventID")))
                .collect(Collectors.toList()).get(0)));
        Helper.switchAtWindow();
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","PEVT_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                ,is("Characteristic Type")));
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String merchantTransactionReferenceID = null;
        for (int k = 0; k < rows; k++) {
            String charType = ccb_dc_char_pageobject.PAYMENT_CHAR_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Merchant Transaction Reference ID")) {
                merchantTransactionReferenceID = ccb_dc_char_pageobject.CHAR_MERCHANT_REFERENCE_ID_TEXTBOX(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            }
        }

        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_pageobject.TENDERS_MENU_TAB));
        Helper.switchToFrames("main","tabPage");
        String paymentIntentID = ccb_payment_event_pageobject.EXT_REFERENCE_ID_TEXTBOX.resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();

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
        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        Helper.customWait(2000);
        String created = ccb_dc_search_pageobject.SEARCH_RESULT_WITH_SPECIAL_CHAR("Create Date/Time").resolveFor(theActorInTheSpotlight()).getAttribute("innerText").trim();

        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON));
        Helper.switchToFrames("main");
        action.moveToElement(ccb_dc_pageobject.GO_TO_DC.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Deposit Control")));
        Helper.switchToFrames("main","tabPage");
        String DC = ccb_dc_pageobject.DC_NUMBER_TEXTBOX.resolveFor(theActorInTheSpotlight()).getAttribute("value");
        PropertiesUtil.setProperties("deposit.control.id",DC);
        String totalTenderAmount = ccb_dc_pageobject.TOTAL_TENDER_AMOUNT_TEXTBOX.resolveFor(theActorInTheSpotlight()).getText();
        PropertiesUtil.setProperties("dc.endingBalanceAmount",totalTenderAmount);

        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                ,is("Characteristic Type")));
        int dcCharRows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String settlementDate = null;
        for (int k = 0; k < dcCharRows; k++) {
            String charType = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Settlement Date")) {
                settlementDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                PropertiesUtil.setProperties("dc.expectedBankInDate",settlementDate);
            }
        }
        CSVParser.updateStripeCSV(created, settlementDate,paymentIntentID,merchantTransactionReferenceID,totalTenderAmount);

        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.HOME_MENU_BUTTON));
        BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).create(tenderSource, tenderType);

        String source = getXmlFile(BatchFile.STRIPE_SETTLEMENT);
        String destination = environmentVariables.getProperty("CCRS.EVMS.CSV.server.path");
        ServerUtil.uploadFile(source,destination,true,".csv");
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.STRIPE,false));
    }
}
