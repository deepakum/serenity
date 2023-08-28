package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_login_pageobject;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class OpenTheApplication implements Task {

    private ccb_login_pageobject ccrsLoginPage;
    public static Actor actor;

    public static OpenTheApplication onTheHomePage(){
        return instrumented(OpenTheApplication.class);
    }

    @Step("^{0} opens the application on the home page$")
    public <T extends Actor> void performAs(T actor) {
        //actor.attemptsTo(Open.browserOn().the(CCRSLoginPageObject.class));
        actor.attemptsTo(Open.browserOn().the(ccb_login_pageobject.class));
    }
}
