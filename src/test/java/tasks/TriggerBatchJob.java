package tasks;

import Utility.CCRS.CCBFrames;
import Utility.others.Helper;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import pageobjects.ccb_user_pageobject;
import questions.CCRSWebElementText;
import tasks.Batch.DuplicateJob;

import java.time.Duration;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsString;

public class TriggerBatchJob implements Performable {

    Logger logger = LoggerFactory.getLogger(TriggerBatchJob.class);
    public static Performable start(){
        return instrumented(TriggerBatchJob.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        logger.info(()->"TriggerBatchJob job started...");
        Helper.switchToFrames(CCBFrames.MAIN,CCBFrames.TAB_PAGE);
        actor.attemptsTo(CCB.duplicateAndQueueJob());
        for(int i=0 ; i < 30 ; i++){
            Helper.switchToFrames(CCBFrames.MAIN);
            actor.attemptsTo(CCB.refresh());
            Helper.switchToFrames("tabPage");
            String batchJobStatus = ccb_user_pageobject.BATCH_JOB_STATUS.waitingForNoMoreThan(Duration.ofMillis(2000)).resolveFor(theActorInTheSpotlight()).getText();
            if(batchJobStatus.equalsIgnoreCase("Ended")) break;
            Helper.customWait(1000);
        }
        actor.should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.BATCH_JOB_STATUS)
                ,containsString("Ended")));
        logger.info(()->"TriggerBatchJob job ended...");
        Helper.switchToFrames(CCBFrames.MAIN);
        try {
            DuplicateJob.batchStatus();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
