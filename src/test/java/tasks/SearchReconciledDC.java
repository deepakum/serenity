package tasks;

import Utility.others.CashierMenu;
import Utility.others.CashierSubMenu;
import Utility.others.Helper;
import Utility.others.SubSubMenu;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.pages.ListOfWebElementFacades;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.interactions.Actions;
import pageobjects.*;
import questions.CCRSWebElementText;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static Utility.others.PropertiesUtil.setProperties;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.core.Is.is;

public class SearchReconciledDC implements Task {

    Logger logger = LoggerFactory.getLogger(SearchReconciledDC.class);

    public static Task byDate(){
        return instrumented(SearchReconciledDC.class);
    }

    @Step("{0} attempts to search deposit control")
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        actor.attemptsTo(SelectFromOptions.byVisibleText("Online Cashiering").from(ccb_dc_search_pageobject.TENDER_SOURCE_TYPE));
        actor.attemptsTo(SelectFromOptions.byVisibleText("Balanced").from(ccb_dc_search_pageobject.DEPOSIT_CONTROL_STATUS));
        actor.attemptsTo(CCB.clickAnElement(ccb_dc_search_pageobject.DC_SEARCH_BUTTON.waitingForNoMoreThan(Duration.ofMillis(3000))));
        Helper.switchToFrames("dataframe");
        ListOfWebElementFacades listOfDc = ccb_dc_search_pageobject.LIST_OF_DC.resolveAllFor(actor);
        List<String> listOfBalancedDC = listOfDc.stream().map(ele->ele.getText()).collect(Collectors.toList());
        String DC = ccb_dc_search_pageobject.DC_INFO(2, 6).resolveFor(actor).getText();
        actor.attemptsTo(Click.on(ccb_dc_search_pageobject.DC_INFO(2, 3)));
        Helper.switchAtWindow();
        actor.attemptsTo(WaitUntil.the(ccb_user_pageobject.CRRS_HOME_HEADER, WebElementStateMatchers.isVisible()));
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                        ,is("Characteristic Type")));
        boolean flag = getLocation();
        if(flag) setProperties("refund.id",DC);
        while(!flag) {
            listOfBalancedDC.remove(DC);
            for (String dcNumber : listOfBalancedDC) {
                Helper.switchToFrames("main", "tabPage");
                actor.attemptsTo(Click.on(ccb_dc_pageobject.SEARCH_DC));
                Helper.switchAtWindow();
                actor.attemptsTo(Enter.keyValues(dcNumber).into(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID));
                actor.attemptsTo(Click.on(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID_SEARCH));
                Helper.switchAtWindow();
                Helper.switchToFrames("tabPage","DEP_CTL_CHAR");
                flag = getLocation();
                if (flag) {
                    System.out.println("DC number : "+dcNumber);
                    setProperties("refund.id",dcNumber);
                    DC = dcNumber;
                    break;
                }
            }
        }
        System.out.println(getPaymentEventID());

    }

    private boolean getPaymentEventID() {
        Helper.switchToFrames("main", "tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.DC_CONTEXT_MENU_BUTTON));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_dc_char_pageobject.GOTO_TC_MENU_LABEL.resolveFor(theActorInTheSpotlight())).build().perform();
        action.moveToElement(ccb_dc_char_pageobject.TC_SEARCH_MENU.resolveFor(theActorInTheSpotlight())).click().perform();
        Helper.switchAtWindow();
        Helper.switchToFrames("tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TENDERS_MENU_LABEL));
        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.GET_MORE_BUTTON));
        Helper.switchAtWindow();
        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        Helper.customWait(2000);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.GO_TO_PAYMENT_EVENT_LABEL));
        Helper.switchToFrames("main","tabPage");
        String paymentEventID = ccb_tc_pageobject.PAYMENT_EVENT_ID_INPUTBOX.resolveFor(theActorInTheSpotlight()).getAttribute("value");
        setProperties("refund.paymentEventID",paymentEventID);
        return false;
    }

    private boolean getLocation() {
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        boolean flag = false;
        for (int k = 0; k < rows; k++) {
            String counterLocation = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (counterLocation.equalsIgnoreCase("Counter Location")) {
                String location = ccb_dc_char_pageobject.CHARACTERISTIC_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                System.out.println("location : "+location);
                if (location.equalsIgnoreCase("NBR")) {
                    flag = getDCRecStatus();
                } break;
            }
        }
        return flag;
    }

    private boolean getDCRecStatus() {
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        boolean flag = false;
        for (int k = 0; k < rows; k++) {
            String DCRecStatus = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (DCRecStatus.equalsIgnoreCase("Reconcilation Status Deposit Control")) {
                String status = ccb_dc_char_pageobject.CHARACTERISTIC_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                //String DCStatusDesc = DcCharacteristicsPageObject.CHARACTERISTIC_VALUE_DESC(k).resolveFor(theActorInTheSpotlight()).getText().trim();
                System.out.println("rec status : "+status);
                if (status.equalsIgnoreCase("R") ) {
                    flag = true;
                } break;
            }
        }
        return flag;
    }
}
