package Utility.others;

import Utility.BatchJob.Counter;
import Utility.Payment.NetsPOS;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.webdriver.WebDriverFacade;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.*;
import questions.CCRSWebElementText;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.core.Is.is;
import static pageobjects.ccb_refund_initiation_pageobject.TABLE_HEADER;

public class TenderControl {

    static Set<String> baseWindowHandles;

    public static void add(){
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage"));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON));
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame("main")));
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_dc_pageobject.GO_TO_TC_MENU.resolveFor(theActorInTheSpotlight())).build().perform();
        action.moveToElement(ccb_dc_pageobject.TC_ADD.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Tender Control")));
    }
    public static void addTenderSource(){
        add();
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame("main").
                then(Switch.toFrame("tabPage"))));
        switchToNewWindow(ccb_tc_pageobject.TENDER_SOURCE_SEARCH_ICON);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_search_pageobject.TENDER_SOURCE_SEARCH_BUTTON));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("dataFrame").
                then(WaitUntil.the(ccb_dc_search_pageobject.TABLE_ROW,isVisible())));
        Serenity.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofMillis(50000));
        List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
        List<String> listOfTableHeaders = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9]"," ").trim()).collect(Collectors.toList());
        int tenderSourceTypeCol = listOfTableHeaders.indexOf("Tender Source Type") + 1;
        for(int row = 1 ; row < rows.size() ; row++){
            Target target = ccb_dc_search_pageobject.COL_DATA(row,tenderSourceTypeCol);
            if(!target.resolveFor(theActorInTheSpotlight()).getText().trim()
                    .equalsIgnoreCase("Online Cashiering")) {
                switchToBaseWindow(target);
                save();
                handleAlert(Message.TENDER_SOURCE_WRONG_LOCATION);
                theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext().then(Switch.toFrame("main").
                        then(Switch.toFrame("tabPage"))));
                switchToNewWindow(ccb_tc_pageobject.TENDER_SOURCE_SEARCH_ICON);
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_search_pageobject.TENDER_SOURCE_SEARCH_BUTTON));
                theActorInTheSpotlight().attemptsTo(Switch.toFrame("dataFrame").
                        then(WaitUntil.the(ccb_dc_search_pageobject.TABLE_ROW,isVisible())));
            }
        }
    }

    public static void switchToNewWindow(Target target){
        baseWindowHandles = Serenity.getDriver().getWindowHandles();
        JavascriptExecutor js = (WebDriverFacade) getDriver();
        js.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)",target.resolveFor(theActorInTheSpotlight()).getElement());
        //theActorInTheSpotlight().attemptsTo(Click.on(target));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(2))
                .forNoMoreThan(Duration.ofMillis(15000)));
        theActorInTheSpotlight().attemptsTo(Switch.toWindow(Serenity.getDriver().getWindowHandles().stream()
                .filter(handle->!handle.equalsIgnoreCase(baseWindowHandles.iterator().next())).collect(Collectors.joining(""))));
    }
    public static void switchToBaseWindow(Target target){
        JavascriptExecutor js = (WebDriverFacade) getDriver();
        js.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)",target.resolveFor(theActorInTheSpotlight()).getElement());
        //theActorInTheSpotlight().attemptsTo(Click.on(target));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(driver-> driver.getWindowHandles().equals(baseWindowHandles)).forNoMoreThan(Duration.ofMillis(10000)));
        baseWindowHandles.stream().forEach(handle->theActorInTheSpotlight().attemptsTo(Switch.toWindow(handle)));
    }

    public static Task select(){
        String tenderSourceType = null;
        if (PropertiesUtil.getProperties("tender.source").equalsIgnoreCase("COUNTER")) {
            tenderSourceType = Counter.TENDER_SOURCE_TYPE;
        }else if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase("NETS-POS")){
            tenderSourceType = NetsPOS.TENDER_SOURCE_TYPE;
        }
        List<WebElementFacade> listOfOpenDc = new ArrayList<>();
        List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
        List<String> listOfTableHeaders = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9]"," ").trim()).collect(Collectors.toList());
        int col = listOfTableHeaders.indexOf("Deposit Control Status") + 1;
        int tstCol = listOfTableHeaders.indexOf("Tender Source Type") + 1;
        for(int row=1;row<rows.size();row++){
            if(ccb_dc_search_pageobject.COL_DATA(row,col)
                    .resolveFor(theActorInTheSpotlight()).getText().trim()
                    .equalsIgnoreCase("Open") && ccb_dc_search_pageobject.COL_DATA(row,tstCol).resolveFor(theActorInTheSpotlight())
                    .getText().trim().equalsIgnoreCase(tenderSourceType)) {
                listOfOpenDc.add(ccb_dc_search_pageobject.COL_DATA(row, listOfTableHeaders.indexOf("Deposit Control ID") + 1).resolveFor(theActorInTheSpotlight()));
                break;
            }
        }
        return Task.where("select deposit ID",
                Click.on(listOfOpenDc.stream().filter(ele -> ele.getText().trim().equals("dc") ).collect(Collectors.toList()).get(0))
        );
    }
    public static void save(){
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("main")
                .then(Click.on(ccb_tc_pageobject.SAVE))
        );
    }
    public static void handleAlert(String message){

        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
        Alert alert = getDriver().switchTo().alert();
        String alertMessage = alert.getText().trim();
        System.out.println(alertMessage);
        alert.accept();
        theActorInTheSpotlight().attemptsTo(Ensure.that(alertMessage).contains(message)
        );
    }
}
