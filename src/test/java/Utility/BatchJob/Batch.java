package Utility.BatchJob;

import Utility.CCRS.CCBFrames;
import Utility.others.AdminMenu;
import Utility.CCRS.SearchMenu;
import Utility.CSV.DateUtil;
import Utility.DB.DB_Utils;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import Utility.others.ToolsMenu;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.actions.Switch;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Keys;
import pageobjects.ccb_batch_job_submission_pageobject;
import tasks.Batch.DuplicateJob;
import tasks.LaunchMenu;
import tasks.Navigate;
import tasks.SearchBatchJob;

import java.sql.SQLException;

import static Utility.SettlementFile.Nets_pos.dcInfoMap;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public interface Batch {

    String batchMenu = SearchMenu.BATCH_JOB_SUBMISSION;
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public default void launch(String batchName){
        theActorInTheSpotlight().attemptsTo(LaunchMenu.to(batchName));
    };

    default void select(String batchName){
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION,"Search"));
        theActorInTheSpotlight().attemptsTo(SearchBatchJob.of(batchName));
    };
    void create(String paymentMode, String tenderType);
    public default String getExpectedBankInDate(){
        return null;
    }
    public default void setBatchUser(){
        theActorInTheSpotlight().attemptsTo(
                Scroll.to(ccb_batch_job_submission_pageobject.USER_ID).then(
                Clear.field(ccb_batch_job_submission_pageobject.USER_ID)
                .then(Enter.keyValues(environmentVariables.getProperty("CCRS.Batch.username"))
                .into(ccb_batch_job_submission_pageobject.USER_ID)
                .thenHit(Keys.RETURN))));
        Helper.switchToFrames(CCBFrames.MAIN);
    }

    default void setBatchDate(String tenderSource,String tenderType){
        DateUtil.tenderType = tenderType;
        String batchBusinessDate = null;
        if (tenderType.equalsIgnoreCase("CounterNets") ||
                tenderType.equalsIgnoreCase("Braintree") ||
                tenderType.equalsIgnoreCase("EnetsDebit")||
                tenderType.equalsIgnoreCase("Paypal") ) {
            batchBusinessDate = PropertiesUtil.getProperties("batch.business.date");
        }else if (tenderType.equalsIgnoreCase("QR_Code") ||
                tenderType.equalsIgnoreCase("CreditCard")  ||
                tenderType.equalsIgnoreCase("Stripe")){
            batchBusinessDate = PropertiesUtil.getProperties("batch.business.date");
        }else {
            batchBusinessDate = DateUtil.getBatchBusinessDate(tenderSource);
        }
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage")
                .then(Clear.field(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX)
                .then(Enter.keyValues(batchBusinessDate)
                        .into(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX))));

    }
    Task setBatchParameters(String paymentMode, Class cls);
    default Task cloneJob(){
        return instrumented(DuplicateJob.class);
    }
    default void balanceOpenedDC(){
        try {
            DB_Utils.balanceDCAndTC();
            DB_Utils.balanceAllTC();
            //DB_Utils.balanceLockedDC();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
