package tasks;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class HandleAlert implements Task {

    String handle;
    String alertMessage;
    public HandleAlert(String handle, String alertMessage){
        this.handle = handle;
        this.alertMessage = alertMessage;
    }

    public static Performable option(String handle, String alertMessage){
        return Instrumented.instanceOf(HandleAlert.class).withProperties(handle,alertMessage);
    }
    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
        Alert alert = getDriver().switchTo().alert();
        System.out.println(alert.getText());
        actor.attemptsTo(Ensure.that(alert.getText().trim()).contains(alertMessage));
        if(handle.equalsIgnoreCase("accept")) {
            alert.accept();
        }else if(handle.isEmpty()){
            alert.accept();
        }else{
            alert.dismiss();
        }
    }
}
