package tasks;

import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_user_pageobject;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class SearchBatchJob implements Performable {

    Logger logger = LoggerFactory.getLogger(SearchBatchJob.class);
    private final String batchName;
    public SearchBatchJob(String batchName){
        this.batchName = batchName;
    }

    public static Performable of(String batchName){
        return Instrumented.instanceOf(SearchBatchJob.class).withProperties(batchName);
    }

    @Override
    @Step("{0} search batch job #batchName")
    public <T extends Actor> void performAs(T actor) {
        logger.info(()->String.format("Searching %s batch job",batchName));
        actor.attemptsTo(CCB.searchBatch(batchName));
        Helper.switchToFrames("dataframe");
        actor.attemptsTo(WaitUntil.the(ccb_user_pageobject.LATEST_BATCH, isClickable()).then(
                Click.on(ccb_user_pageobject.LATEST_BATCH))
        );
        Helper.customWait(1000);
        //actor.attemptsTo(SwitchToWindow.targetWindow());
        // Throwing window error so commented until issue is resolved
        Helper.switchToParentWindow();
    }
}
