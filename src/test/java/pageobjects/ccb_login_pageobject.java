package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class ccb_login_pageobject extends PageObject {

    public static final Target USERNAME_INPUTBOX = Target.the("username input textbox")
            .located(By.id("userId"));

    public static final Target PASSWORD_INPUTBOX = Target.the("password input box")
            .located(By.id("password"));

    public static final Target LOGIN_BUTTON = Target.the("login button")
            .located(By.id("loginButton"));

    public static final Target TOOL_BAR_MENU = Target.the("tool bar menu")
            .locatedBy("#toolbarnew>div>span");

    public static final Target BRANDING_BAR_MENU = Target.the("branding bar menu")
            .locatedBy("#brandingArea>div[class='userMenuArea']>span");
    public static final Target HISTORY_TAB = Target.the("history tab")
            .locatedBy("#IM_HISTORY");
    public static final Target ERROR_MSG_SPAN = Target.the("error message span")
            .locatedBy("#mainMessage");
}
