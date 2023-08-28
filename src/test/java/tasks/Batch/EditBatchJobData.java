package tasks.Batch;

import Utility.CSV.DateUtil;
import Utility.others.Helper;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Keys;
import pageobjects.ccb_batch_job_submission_pageobject;

public class EditBatchJobData implements Task {

    private final String tender;
    private final String psp;
    private final String counterFlag;
    private final String location;
    private final String cdc;
    private final String erpFlag;
    public static boolean batchBusinessDate = false;

    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    public EditBatchJobData(String tender, String psp, String counterFlag, String cdc, String location, String erpFlag){
        this.tender = tender;
        this.psp = psp;
        this.counterFlag = counterFlag;
        this.cdc = cdc;
        this.location = location;
        this.erpFlag = erpFlag;

    }

    public static Task using(String tender, String psp, String counterFlag,String cdc, String location, String erpFlag){
        return Instrumented.instanceOf(EditBatchJobData.class).withProperties(tender,psp,counterFlag,cdc,location, erpFlag);
    }

    @Step("^{0} attempts to set batch parameters$")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames("tabPage");
        actor.attemptsTo(Clear.field(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX)
                .then(Enter.keyValues(DateUtil.getBatchBusinessDate(tender))
                        .into(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX)));
        Helper.switchToFrames("main","tabPage");
        actor.attemptsTo(Clear.field(ccb_batch_job_submission_pageobject.USER_ID).then(
                Enter.keyValues(environmentVariables.getProperty("CCRS.Batch.username"))
                        .into(ccb_batch_job_submission_pageobject.USER_ID)
                        .thenHit(Keys.RETURN)
        ));
        this.batchBusinessDate = true;
        Helper.switchToFrames("main","tabPage","BJP");
        //actor.attemptsTo(CCRS.setBatchParameters(tender, psp, counterFlag,cdc,location,erpFlag));
    }

}
