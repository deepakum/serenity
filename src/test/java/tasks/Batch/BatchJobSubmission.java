package tasks.Batch;

import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchFactory;
import Utility.CCRS.CCBFrames;
import Utility.CSV.DateUtil;
import Utility.Payment.TenderSource;
import Utility.Payment.TenderType;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.SendKeys;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.ccb_batch_job_submission_pageobject;
import tasks.CCB;
import tasks.Navigate;
import tasks.SearchBatchJob;
import tasks.TriggerBatchJob;
import static Utility.CSV.DateUtil.getReconBusinessDate;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static tasks.CCB.getWebElement;

public class BatchJobSubmission implements Performable {

    static Logger logger = LoggerFactory.getLogger(BatchJobSubmission.class);
    private String batchName;
    private boolean dateFlag;

    public BatchJobSubmission(String batchName, boolean dateFlag){
        this.batchName = batchName;
        this.dateFlag = dateFlag;
    }

    @Override
    @Step("{0} attempts to submit batch job")
    public <T extends Actor> void performAs(T actor) {

        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.numberOfWindowsToBe(2)));
        actor.attemptsTo(SearchBatchJob.of(batchName));
        Helper.switchToFrames(CCBFrames.TAB_PAGE);
        String batchBusinessDate;
        if(dateFlag){
            switch(batchName){
                case BatchControl.UPLOAD_BANK_STATEMENT:
                    if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CREDIT_CARD)){
                        batchBusinessDate = DateUtil.getBusinessDateForCreditCardSettlement();
                    } else if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderSource.DBS)){
                        batchBusinessDate = DateUtil.getBusinessDateForQRCodeSettlement();
                    } else if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.ENETS_DEBIT)){
                        batchBusinessDate = Batch.enetsDebitSettlementBusinessDate();
                    } else if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.ABBR)){
                        batchBusinessDate = Batch.expectedBankInDate();
                    }else {
                        batchBusinessDate = Batch.settlementBusinessDate();
                    }
                    if (PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderSource.DBS) || PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.PORTAL_QR_CODE)) {
                        this.setBankStatementFileName("LTAAGD01XXXX.CASP_MT940");
                    }else{
                        this.setBankStatementFileName("SLTA03XXXXXX.CASP_MT940");
                    }
                    break;
                case BatchControl.BANK_RECONCILIATION:
                    batchBusinessDate = Batch.expectedBankInDate();
                    break;
//                case BatchControl.NETS:
                case BatchControl.CREDIT_CARD:
                    batchBusinessDate = DateUtil.getBusinessDateForCreditCardSettlement();
                    break;
//                case BatchControl.PAYPAL_SETTLEMENT_JOB:
                default:
                    batchBusinessDate = Batch.settlementBusinessDate();
                    break;
            }
            logger.info(()->String.format("batch business date is %s",batchBusinessDate));
            actor.attemptsTo(Clear.field(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX)
                    .then(SendKeys.of(batchBusinessDate)
                    .into(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX)
                    .thenHit(Keys.RETURN)));
        }else{
            actor.attemptsTo(Clear.field(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX));
        }
        BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchUser();
        actor.attemptsTo(TriggerBatchJob.start());
    }
    private void setBankStatementFileName(String fileName){
        BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchUser();
        Helper.switchAtWindow();
        Helper.switchToFrames(CCBFrames.TAB_PAGE,CCBFrames.BJP);
        CCB.getJs().executeScript("arguments[0].scrollIntoView(true);",getWebElement(ccb_batch_job_submission_pageobject.TENDER_SOURCE));
        theActorInTheSpotlight().attemptsTo(Clear.field(ccb_batch_job_submission_pageobject.FILE_NAME)
                .then(SendKeys.of(fileName).into(ccb_batch_job_submission_pageobject.FILE_NAME)
                        .then(Switch.toDefaultContext())
                        .then(Switch.toFrame(CCBFrames.MAIN))
                        .then(Switch.toFrame(CCBFrames.TAB_PAGE))));
    }
}
