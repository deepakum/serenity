package tasks;

import Utility.CCRS.CCBFrames;
import Utility.others.DepositControlInfo;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.actions.SwitchToNewWindow;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_dc_char_pageobject;
import questions.CCRSWebElementText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.core.Is.is;

public class DcCharacteristics implements Performable {

    Logger logger = LoggerFactory.getLogger(DcCharacteristics.class);

    @Step("{0} able to set deposit control characteristics")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToParentWindow();
        Helper.switchToFrames(CCBFrames.TAB_MENU);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                ,is("Characteristic Type".toUpperCase())));
        int rows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        String expectedBankingDate = null;
        String settlementDate = null;
        for (int row = 0; row < rows; row++) {
            String charType = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(row).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Expected Bank In Date")) {
                expectedBankingDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            }else if (charType.equalsIgnoreCase("Settlement Date")) {
                settlementDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            }else if (charType.equalsIgnoreCase("Original Tender Source")) {
                String tenderSource = ccb_dc_char_pageobject.CHAR_DATE_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            }else if (charType.equalsIgnoreCase("Expected Bank In Amount")) {
                String expectedBankInAmount = ccb_dc_char_pageobject.CHAR_DATE_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            }
        }
        DepositControlInfo.setExpectedBankInDate(expectedBankingDate);
        PropertiesUtil.setProperties("dc.expectedBankInDate",expectedBankingDate);
        String finalExpectedBankingDate = expectedBankingDate;
        logger.info(()->"Expected Bank in date : "+ finalExpectedBankingDate);
        String finalSettlementDate = settlementDate;
        logger.info(()->"Settlement date : "+ finalSettlementDate);
        PropertiesUtil.setProperties("dc.settlement.date",settlementDate);

        String expectedBankingAmount = null;
        for (int row = 0; row < rows; row++) {
            String charType = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(row).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Expected Bank In Amount")) {
                expectedBankingAmount = "S$".concat(ccb_dc_char_pageobject.CHAR_DATE_VALUE(row).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim());
            }
        }
        String finalExpectedBankingAmount = expectedBankingAmount;
        logger.info(()->"Expected Bank in Amount : "+ finalExpectedBankingAmount);
        DepositControlInfo.setExpectedEndingBalance(expectedBankingAmount);
        PropertiesUtil.setProperties("dc.endingBalanceAmount",expectedBankingAmount);
        Helper.switchToFrames(CCBFrames.MAIN);
    }
}
