package tasks.Batch;

import Utility.CCRS.CCBFrames;
import Utility.others.Helper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.CheckCheckbox;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.DoubleClick;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.conditions.Check;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import org.junit.Assert;
import org.openqa.selenium.interactions.Actions;
import pageobjects.ccb_batch_job_submission_pageobject;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_batch_run_tree_search_pageobject;
import questions.CCRSWebElementText;
import tasks.CCB;
import tasks.TriggerBatchJob;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.*;

public class DuplicateJob implements Task {

    public static Task to(){return instrumented(DuplicateJob.class);}
    @Override
    @Step("(.*) able to duplicate job")
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(TriggerBatchJob.start());
        try {
            this.batchStatus();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void batchStatus() throws Exception {

        Helper.switchToFrames("main","dashboard");
        if (CCB.getText(ccb_batch_job_submission_pageobject.BATCH_STATUS).equalsIgnoreCase("Error")){
            runControl();
            throw new Exception("Batch job failed");
        }else {
            theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_batch_job_submission_pageobject.BATCH_STATUS)
                    , containsString("Complete")));
            String count = CCB.getText(ccb_batch_job_submission_pageobject.BATCH_RECORD_COUNT);
            Assert.assertTrue("job completed , but "+count + " records found",Integer.parseInt(count)>0);
        }
        Helper.switchToFrames(CCBFrames.MAIN);
    }

    public static void runControl(){

        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_batch_job_submission_pageobject.BATCH_CONTROL_CONTEXT_MENU));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_batch_job_submission_pageobject.BATCH_RUN_TREE.resolveFor(theActorInTheSpotlight())).click().perform();
        Helper.switchAtWindow(1);
        Helper.switchToFrames("dataframe");
        theActorInTheSpotlight().attemptsTo(DoubleClick.on(ccb_batch_run_tree_search_pageobject.RUN_STATUS_TAB));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_user_pageobject.LATEST_BATCH, isClickable()).then(
                        Click.on(ccb_user_pageobject.LATEST_BATCH)));
        Helper.switchAtWindow();
        Helper.switchToFrames(CCBFrames.TAB_MENU);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_batch_run_tree_search_pageobject.RUN_CONTROL_TAB));
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(Switch.toFrame(CCBFrames.TAB_PAGE));
        CheckCheckbox.of(ccb_batch_run_tree_search_pageobject.DO_NOT_RESTART_SW)
                .then(Click.on(ccb_batch_run_tree_search_pageobject.DO_NOT_RESTART_SW))
                .then(Switch.toFrame(CCBFrames.MAIN)
                .then(Click.on(ccb_batch_run_tree_search_pageobject.SAVE_BUTTON)));

    }

    private void batchJobStatus(){
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_batch_job_submission_pageobject.BATCH_CONTROL_CONTEXT_MENU));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_batch_job_submission_pageobject.BATCH_RUN_TREE.resolveFor(theActorInTheSpotlight())).click().perform();
        Helper.switchAtWindow(1);
        Helper.switchToFrames("dataframe");
        theActorInTheSpotlight().attemptsTo(DoubleClick.on(ccb_batch_run_tree_search_pageobject.BATCH_NUMBER_TAB));
        theActorInTheSpotlight().should(eventually(seeThat(
                WebElementQuestion.the(ccb_batch_job_submission_pageobject.RUN_STATUS), isVisible())));
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_batch_job_submission_pageobject.RUN_STATUS),
                containsString("Complete")));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_batch_job_submission_pageobject.RUN_STATUS));
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_user_pageobject.PAGE_TITLE, isVisible()));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage"));
    }
}
