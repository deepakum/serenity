package tasks;

import Utility.others.DepositControlInfo;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_dc_char_pageobject;
import questions.CCRSWebElementText;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.core.Is.is;

public class SettlementDate implements Performable {

    Logger logger = LoggerFactory.getLogger(SettlementDate.class);

    @Step("{0} able to select current date")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToParentWindow();
        Helper.switchToFrames("tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                ,is("Characteristic Type".toUpperCase())));
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String settlementDate = null;
        for (int row = 0; row < rows; row++) {
            String charType = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(row).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Expected Bank In Date")) {
                settlementDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            } else if (charType.equalsIgnoreCase("Settlement Date")) {
            settlementDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
        }
        }
        DepositControlInfo.setExpectedBankInDate(settlementDate);
        PropertiesUtil.setProperties("dc.settlement.date",settlementDate);
        String finalExpectedBankingDate = settlementDate;
        logger.info(()->"Expected Bank in date : "+ finalExpectedBankingDate);
    }
}
