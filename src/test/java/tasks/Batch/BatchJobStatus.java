package tasks.Batch;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class BatchJobStatus implements Performable {

    @Step("{0} able to verify batch Job status of #batchName")
    public <T extends Actor> void performAs(T actor) {

        //Not applicable
//        Helper.switchToFrames("tabPage");
//        actor.attemptsTo(Click.on(ccb_batch_job_submission_pageobject.BATCH_CONTROL_CONTEXT_MENU));
//        Helper.switchToFrames("main");
//        Actions action = new Actions(getDriver());
//        action.moveToElement(ccb_batch_job_submission_pageobject.BATCH_RUN_TREE.resolveFor(actor)).click().perform();
//        Helper.switchAtWindow();
//        Helper.switchToFrames("dataframe");
//        String batchStatus = ccb_batch_job_submission_pageobject.RUN_STATUS.resolveFor(actor).getText().trim();
//        if(batchStatus.equalsIgnoreCase("Complete")) {
//            theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_batch_job_submission_pageobject.RUN_STATUS),
//                    containsString("Complete")));
//            theActorInTheSpotlight().attemptsTo(Click.on(ccb_batch_job_submission_pageobject.RUN_STATUS));
//        }else if(batchStatus.equalsIgnoreCase("Error")){
//            runControl();
//        }
//        Helper.switchAtWindow(0);
    }
}
