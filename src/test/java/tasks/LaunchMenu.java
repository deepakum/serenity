package tasks;

import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class LaunchMenu implements Performable {

    private String menu;

    public LaunchMenu(String menu){
        this.menu = menu;
    }

    public static LaunchMenu to(String menu){
        return Instrumented.instanceOf(LaunchMenu.class).withProperties(menu);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        PropertiesUtil.setProperties("parent.window.handle",getDriver().getWindowHandle());
        actor.attemptsTo(CCB.searchBatchJobByName(menu));
        actor.attemptsTo(CCB.selectMenu(menu));
        Helper.switchToParentWindow();
    }
}
