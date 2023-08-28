package tasks;

import Utility.others.Helper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Clear;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_batch_job_submission_pageobject;

import java.time.Duration;
import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class setBatchSubmissionDate implements Performable {

    List<String> handles;
    public static Performable today(){
        return instrumented(setBatchSubmissionDate.class);
    }

    @Override
    @Step("{0} able to select current date")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Clear.field(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX));
        actor.attemptsTo(CCB.clickAnElement(ccb_batch_job_submission_pageobject.DATE.waitingForNoMoreThan(Duration.ofMillis(2000))));
        Helper.switchAtWindow(1);
        actor.attemptsTo(CCB.clickAnElement(ccb_batch_job_submission_pageobject.DATE_ACCEPT));
        Helper.switchAtWindow(0);
    }
}
