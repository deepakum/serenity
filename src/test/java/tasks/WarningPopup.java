package tasks;

import Utility.Payment.TenderType;
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

public class WarningPopup implements Performable {

    public Target target;
    public String tenderType;

    public WarningPopup(Target target, String tenderType){
        this.target = target;
        this.tenderType = tenderType;
    }
    public static Performable dismiss(Target target, String tenderType){
        return Instrumented.instanceOf(WarningPopup.class).withProperties(target,tenderType);
    }
    @Override
    public <T extends Actor> void performAs(T actor) {

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].click();", target.resolveFor(theActorInTheSpotlight()));
        actor.attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
        Alert alert = getDriver().switchTo().alert();
        System.out.println(alert.getText());
        alert.accept();
        actor.attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
        System.out.println(alert.getText());
        alert.accept();
        if(tenderType.equalsIgnoreCase(TenderType.CASH_CARD) || tenderType.equalsIgnoreCase(TenderType.FLASH_PAY)
                || tenderType.equalsIgnoreCase(TenderType.CREDIT_CARD)
                || tenderType.equalsIgnoreCase(TenderType.NETS)){
            actor.attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
            System.out.println(alert.getText());
            alert.accept();
        }
    }
}
