package tasks;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class OneAlertPopup implements Performable {

    public Target target;
    public OneAlertPopup(Target target){
        this.target = target;
    }

    public static Performable accept(Target target){
        return Instrumented.instanceOf(OneAlertPopup.class).withProperties(target);
    }
    @Override
    public <T extends Actor> void performAs(T actor) {

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].click();", target.resolveFor(theActorInTheSpotlight()));
        actor.attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
        Alert alert = getDriver().switchTo().alert();
        System.out.println(alert.getText());
        alert.accept();;

    }
}
