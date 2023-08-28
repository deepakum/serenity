package tasks;

import Utility.others.PropertiesUtil;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;

public class WindowHandles implements Performable {

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        String parentHandle = driver.getWindowHandle();
        PropertiesUtil.setProperties("parent.window.handle",parentHandle);
    }
}
