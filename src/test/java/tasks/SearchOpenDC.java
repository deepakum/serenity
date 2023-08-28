package tasks;

import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;
import pageobjects.*;
import questions.CCRSWebElementText;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.core.Is.is;

public class SearchOpenDC implements Task {

    Logger logger = LoggerFactory.getLogger(SearchOpenDC.class);

    @Step("{0}} attempts to search deposit control")
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        actor.attemptsTo(DC.search("","Online Cashiering","Open"));
        List<String> totalNumberOfDC = ccb_dc_search_pageobject.LIST_OF_DC.resolveAllFor(actor)
                .stream().map(ele->ele.getText().trim()).collect(Collectors.toList());
        Collections.reverse(totalNumberOfDC);
        String DCNumber = totalNumberOfDC.get(0);
        actor.attemptsTo(tasks.DC.toBeSelected(totalNumberOfDC.size()));
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(
                ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL),is("Characteristic Type")));
        boolean flag = getLocation();
        while(!flag) {
            totalNumberOfDC.remove(DCNumber);
            for (String dcNumber : totalNumberOfDC) {
                Helper.switchToFrames("main", "tabPage");
                actor.attemptsTo(Click.on(ccb_dc_pageobject.SEARCH_DC));
                Helper.switchAtWindow();
                //Search DC by ID
                //actor.attemptsTo(DC.search(DCNumber));
                actor.attemptsTo(DC.search("","Online Cashiering","Open"));
                List<WebElementFacade> listOfDc = ccb_dc_search_pageobject.SEARCH_RESULT("Deposit Control ID").resolveAllFor(actor);
                actor.attemptsTo(Click.on(listOfDc.stream().filter(ele -> ele.getText().trim().equals(dcNumber)).collect(Collectors.toList()).get(0)));
                Helper.switchAtWindow();
                Helper.switchToFrames("tabPage","DEP_CTL_CHAR");
                flag = getLocation();
                if (flag == true) {
                    DCNumber = dcNumber;
                    break;
                }
            }
        }
        if(flag){
            String finalDC = DCNumber;
            logger.info(()->String.format("Deposit control ID : %s ", finalDC));
            DepositControlInfo.setDepositControlID(DCNumber);
            PropertiesUtil.setProperties("deposit.control.id",DCNumber);
            Helper.switchToFrames("main","tabMenu");
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
        }

    }

    private boolean getLocation() {
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        boolean flag = false;
        for (int k = 0; k < rows; k++) {
            String counterLocation = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (counterLocation.equalsIgnoreCase("Counter Location")) {
                String location = ccb_dc_char_pageobject.CHARACTERISTIC_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                if (location.equalsIgnoreCase("NBR")) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

}
