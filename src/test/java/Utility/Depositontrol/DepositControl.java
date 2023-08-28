package Utility.Depositontrol;

import Utility.CCRS.CCBFrames;
import Utility.CSV.DateUtil;
import Utility.SettlementFile.Nets_pos;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Switch;
import org.openqa.selenium.interactions.Actions;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.is;
import static pageobjects.ccb_refund_initiation_pageobject.TABLE_HEADER;

public class DepositControl {
    static Map<String, String> depositControlMap = new HashMap<>();
    static Logger logger = LoggerFactory.getLogger(DepositControl.class);

    public static void getChar(String tenderType, String dcStatus){
        PropertiesUtil.setProperties("tender.type",tenderType);
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.TENDER_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(TC.searchByTenderSource(tenderType));
        List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
        List<String> listOfTableHeaders = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9]"," ").trim()).collect(Collectors.toList());
        int dcIdCol = listOfTableHeaders.indexOf("Deposit Control ID") + 1;
        int statusCol = listOfTableHeaders.indexOf("Status") + 1;
        String dcNumber = null;
        for(int row=1;row<rows.size();row++){
            String status = CCB.getText(ccb_dc_search_pageobject.COL_DATA(row,statusCol));
            String createDateAndTime = CCB.getText(ccb_tc_search_pageobject.TC_CREATE_DATE_TIME(row-1)).replace("‑","-");
            String dcCreationDate = DateUtil.getCustomDate("dd-MM-yyyy HH:mm:ss","dd-MM-yyyy",createDateAndTime);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance();
            if(sdf.format(calendar.getTime()).equalsIgnoreCase(dcCreationDate)) {
                dcNumber = CCB.getText(ccb_dc_search_pageobject.COL_DATA(row,dcIdCol));
                PropertiesUtil.setProperties("deposit.control.id", dcNumber);
                PropertiesUtil.setProperties("dc.create.date.time", CCB.getText(ccb_tc_search_pageobject.TC_CREATE_DATE_TIME(row-1)));
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_search_pageobject.TC_CREATE_DATE_TIME(row-1)));
                Helper.switchToParentWindow();
                theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE));
                break;
            }
        }
        goToDepositControl();
        getDepositControlInfo();
        theActorInTheSpotlight().attemptsTo(DC.setDepositControlChar());
    }

    public static void goToDepositControl(){
        Actions action = new Actions(getDriver());
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON));
        Helper.switchToFrames("main");
        action.moveToElement(ccb_dc_pageobject.GO_TO_DC.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Deposit Control")));
    }

    private static void getDepositControlInfo(){
        Helper.switchToFrames("main","tabPage");
        String DC = CCB.getTextUsingAttribute(ccb_dc_pageobject.DC_NUMBER_TEXTBOX,"value");
        logger.info(()->String.format("deposit control ID : %s",DC));
        PropertiesUtil.setProperties("deposit.control.id",DC);
        String totalTenderAmount = CCB.getText(ccb_dc_pageobject.TOTAL_TENDER_AMOUNT_TEXTBOX);
        PropertiesUtil.setProperties("dc.endingBalanceAmount",totalTenderAmount);
        depositControlMap.put("TotaltenderAmount",totalTenderAmount);
        depositControlMap.put("DepositControl",DC);
        Nets_pos.dcInfo();
        Helper.switchToFrames(CCBFrames.MAIN);
    }

}
