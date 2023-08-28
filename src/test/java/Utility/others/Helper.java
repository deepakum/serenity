package Utility.others;
import Utility.CCRS.CCBFrames;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static net.serenitybdd.core.Serenity.getDriver;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class Helper {

    static EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
    static Logger logger = LoggerFactory.getLogger(Helper.class);

    public static void switchToFrames(String frame_1) {
        if (frame_1.equalsIgnoreCase("main")) {
            theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
        }
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(frame_1));
    }

    public static void switchToFrames(String frame_1, String frame_2) {
        if (frame_1.equalsIgnoreCase("main")) {
            theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
        }
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(frame_1));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(frame_2));
    }

    public static void switchToFrames(String frame_1, String frame_2, String frame_3) {
        if (frame_1.equalsIgnoreCase("main")) {
            theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
        }
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(frame_1));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(frame_2));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(frame_3));
    }

    public static WebDriver getWebDriver() {
        return BrowseTheWeb.as(theActorInTheSpotlight()).getDriver();
    }

    public static void switchAtWindow(int index) {
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(index + 1))
                .forNoMoreThan(Duration.ofMillis(5000)));
        List<String> handles = new ArrayList<>(getWebDriver().getWindowHandles());
        getWebDriver().switchTo().window(handles.get(index));
        if (index == 0) {
            getWebDriver().switchTo().defaultContent();
            theActorInTheSpotlight().attemptsTo(Switch.toFrame("main"));
        }
    }

    public static void switchAtWindow() {
        Helper.customWait(2500);
        List<String> handles = new ArrayList<>(BrowseTheWeb.as(theActorInTheSpotlight()).
                getDriver().getWindowHandles());
        if (handles.size() == 1) {
            theActorInTheSpotlight().attemptsTo(Switch.toWindow(handles.get(0)));
            theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
            theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.MAIN));
        } else if (handles.size() == 2) {
            getWebDriver().switchTo().window(handles.get(handles.size() - 1));
            getDriver().switchTo().defaultContent();
        }
        Helper.customWait(1000);
    }

    public static void switchToNewWindow() {
        Helper.customWait(1000);
        List<String> handles;
        int handleCount = 0;
        int loopCount = 0;
        while (handleCount != 2) {
            handles = new ArrayList<>(BrowseTheWeb.as(theActorInTheSpotlight())
                    .getDriver().getWindowHandles());
            handleCount = handles.size();
            for (String handle : handles) {
                if (!PropertiesUtil.getProperties("parent.window.handle").equalsIgnoreCase(handle)) {
                    PropertiesUtil.setProperties("child.window.handle", handle);
                    theActorInTheSpotlight().attemptsTo(Switch.toWindow(handle));
                    break;
                }
            }
            loopCount +=1;
            if(loopCount>20){
                throw new RuntimeException("Failed to switch to new Window");
            }
            Helper.customWait(200);
        }
    }

    public static void switchToParentWindow() {
        Helper.customWait(2000);
        //theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(1)));
        WebDriver driver = null;
        try {
            logger.info(()->"switching to parent widow..");
            driver = BrowseTheWeb.as(theActorInTheSpotlight()).getDriver();
            if (driver != null) {
                System.out.println("windowHandleCount : " + driver.getWindowHandles());
                List<String> handles = new ArrayList<>(BrowseTheWeb.as(theActorInTheSpotlight()).
                        getDriver().getWindowHandles());
                theActorInTheSpotlight().attemptsTo(Switch.toWindow(handles.get(0)));
                theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
                theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.MAIN));
            } else {
                theActorInTheSpotlight().attemptsTo(Switch.toWindow(PropertiesUtil.getProperties("parent.window.handle")));
                theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
                theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.MAIN));
            }
        }catch (Exception e){
            System.out.println("Failed to switch to ccrs page");
            driver.close();
        }
    }

    public static void switchToParentWindow_old() {
        Helper.customWait(2000);
        List<String> handles;
        int handleCount = 0;
        while(handleCount!=1) {
            handles = new ArrayList<>(getDriver().getWindowHandles());
            System.out.println("handleCount : "+handles.size());
            handleCount = handles.size();
            for (String handle : handles) {
                if (PropertiesUtil.getProperties("parent.window.handle").equalsIgnoreCase(handle)) {
                    PropertiesUtil.setProperties("child.window.handle", handle);
                    logger.info(() -> String.format("switchToNewWindow()-child.window.handle : %s", handle));
                    theActorInTheSpotlight().attemptsTo(Switch.toWindow(handle));
                    theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
                    theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.MAIN));
                }
            }
            Helper.customWait(200);
        }
    }

    public static void customWait(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
