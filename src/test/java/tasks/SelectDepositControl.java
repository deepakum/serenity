package tasks;

import Utility.others.DepositControlInfo;
import Utility.others.Helper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_dc_search_pageobject;

import java.time.Duration;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SelectDepositControl implements Performable {

    public static Performable to(){
        return instrumented(SelectDepositControl.class);
    }

    @Override
    @Step("{0} chooses deposit control")
    public <T extends Actor> void performAs(T actor) {
        Helper.customWait(3000);
        Helper.switchAtWindow(1);
//        actor.attemptsTo(SelectDate.today());
//        actor.attemptsTo(SelectFromOptions.byVisibleText("Online Cashiering").from(DepositControlSearchPageObject.TENDER_SOURCE_TYPE));
//        actor.attemptsTo(SelectFromOptions.byVisibleText("Open").from(DepositControlSearchPageObject.DEPOSIT_CONTROL_STATUS));
        actor.attemptsTo(DC.search("","Online Cashiering","Open"));
        Helper.switchAtWindow(1);
        actor.attemptsTo(CCB.clickAnElement(ccb_dc_search_pageobject.DC_SEARCH_BUTTON.waitingForNoMoreThan(Duration.ofMillis(3000))));
        Helper.switchToFrames("dataframe");
        int listOfDc = ccb_dc_search_pageobject.LIST_OF_DC.resolveAllFor(actor).size();
        for (int row = listOfDc ; row >= 1; row--) {
            String depositControlID = ccb_dc_search_pageobject.DC_INFO(row, 6).resolveFor(actor).getText();
            if (depositControlID.equalsIgnoreCase(DepositControlInfo.getDepositControlID())) {
                actor.attemptsTo(Click.on(ccb_dc_search_pageobject.DC_INFO(row, 6)));
                Helper.customWait(3000);
                break;
            }
        }
        Helper.switchAtWindow(0);
    }
}
