package Utility.SaleItem;

import Utility.CCRS.CCBFrames;
import Utility.others.Helper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import org.openqa.selenium.interactions.Actions;
import pageobjects.ccb_sale_item_search_pageobject;
import pageobjects.ccb_user_pageobject;

import static net.serenitybdd.core.Serenity.getDriver;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class SaleItemAdd implements Task {
    @Override
    public <T extends Actor> void performAs(T actor) {
        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.MENU_BUTTON));
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_sale_item_search_pageobject.SALE_ITEM_MENU.resolveFor(theActorInTheSpotlight())).build().perform();
        action.moveToElement(ccb_sale_item_search_pageobject.SALE_ITEM_SEARCH_MENU.resolveFor(theActorInTheSpotlight())).click().perform();
    }
}
