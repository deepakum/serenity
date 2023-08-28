package tasks;

import Utility.CCRS.CCBFrames;
import Utility.others.Helper;
import Utility.others.MenuFactory;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Switch;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pageobjects.ccb_user_pageobject;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class Navigate implements Performable {

    Logger logger = LoggerFactory.getLogger(Navigate.class);
    private String menu;
    private String subMenu;
    private String subSubMenu;

    public Navigate(String menu, String subMenu, String subSubMenu){
        this.menu = menu;
        this.subMenu = subMenu;
        this.subSubMenu = subSubMenu;
    }

    public Navigate(String menu, String subMenu){
        this.menu = menu;
        this.subMenu = subMenu;
    }

    public static Performable toMenu(String menu, String subMenu, String subSubMenu){
        return Instrumented.instanceOf(Navigate.class).withProperties(menu,subMenu,subSubMenu);
    }

    public static Performable toMenu(String menu, String subMenu){
        return Instrumented.instanceOf(Navigate.class).withProperties(menu,subMenu);
    }
    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info(()->String.format("Navigating to %s->%s->%s menu",menu,subMenu,subSubMenu));
        Helper.switchToFrames(CCBFrames.MAIN);
        actor.attemptsTo(Click.on(ccb_user_pageobject.MENU_BUTTON));
        Actions action = new Actions(getDriver());
        WebElement mainMenuWebElement = MenuFactory.getMainMenuTarget(menu).resolveFor(actor);
        action.moveToElement(mainMenuWebElement).build().perform();
        WebElement subMenuWebElement = MenuFactory.getSubMenuTarget(menu,subMenu,subSubMenu).resolveFor(actor);
        if(subSubMenu != null){
            action.moveToElement(subMenuWebElement).build().perform();
            WebElement leafMenu = ccb_user_pageobject.LEAF_MENU(subMenu,subSubMenu).resolveFor(actor);
            action.moveToElement(leafMenu).click().perform();
        }else{
            action.moveToElement(subMenuWebElement).click().perform();
        }
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
    }
}
