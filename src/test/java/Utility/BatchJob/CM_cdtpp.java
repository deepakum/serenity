package Utility.BatchJob;

import Utility.CCRS.CCBFrames;
import Utility.CCRS.User;
import Utility.Payment.*;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import pageobjects.ccb_batch_job_submission_pageobject;
import tasks.*;
import tasks.Batch.AddTenderControl;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static tasks.CCB.getWebElement;

public class CM_cdtpp implements Batch {

    private final String batchName = BatchControl.DEPOSIT_CONTROL;
    static Logger logger = LoggerFactory.getLogger(CM_cdtpp.class);
    private String tenderType;

    @Override
    public void create(String tenderSource, String tenderType) {

        logger.info(() -> "CM_cdtpp.create() started");

        if (tenderSource == null) {
            try {
                throw new Exception("Please provide payment mode!!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        PropertiesUtil.setProperties("tender.source", tenderSource);
        PropertiesUtil.setProperties("tender.type", tenderType);
        logger.info(() -> "Tender source : " + tenderSource);
        logger.info(() -> "Tender type : " + tenderType);

//        if (!CCRS.getTargetText(ccb_user_pageobject.LOGGED_IN_USER_SPAN).equalsIgnoreCase(CCBUserInfo.SYSTEM_USER_NAME)){
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
//        }

        this.balanceOpenedDC();

        switch (tenderSource) {
            case TenderSource.STRIPE:
                if(!(TC.getTC(TenderSource.STRIPE,BatchStatus.OPEN).length()>0)) {
                    theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, Stripe.class));
                    theActorInTheSpotlight().attemptsTo(this.cloneJob());
                }
                break;
            case TenderSource.BRAINTREE:
                if(!(TC.getTC(TenderSource.BRAINTREE,BatchStatus.OPEN).length()>0)) {
                    theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, Braintree.class));
                    theActorInTheSpotlight().attemptsTo(this.cloneJob());
                }
                break;
            case TenderSource.ENETS_DEBIT:
                if(!(TC.getTC(TenderSource.ENETS_DEBIT,BatchStatus.OPEN).length()>0)) {
                    theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, EnetsDebit.class));
                    theActorInTheSpotlight().attemptsTo(this.cloneJob());
                }
                break;
            case TenderSource.PAYPAL:
                if(!(TC.getTC(TenderSource.PAYPAL,BatchStatus.OPEN).length()>0)) {
                    theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, EnetsDebit.class));
                    theActorInTheSpotlight().attemptsTo(this.cloneJob());
                }
                break;
            case TenderSource.DBS:
                if(!(TC.getTC(TenderSource.DBS,BatchStatus.OPEN).length()>0)) {
                    theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, QR_Code.class));
                    theActorInTheSpotlight().attemptsTo(this.cloneJob());
                }
                break;
            case TenderSource.COUNTER:
                switch (tenderType){
                    case TenderType.NETS:
                        this.select(batchName);
                        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext()
                                .then(Switch.toFrame(CCBFrames.MAIN)));
                        theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, Counter.class));
                        theActorInTheSpotlight().attemptsTo(this.cloneJob());
                        DC.getOpenedDC(TenderSourceType.ONLINE_CASHIERING,BatchStatus.OPEN);
                        addTCToDC(tenderSource);
                        if(!(TC.getTC(TenderSource.NETS,BatchStatus.OPEN).length()>0)) {
                            this.select(batchName);
                            theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, CounterNets.class));
                            theActorInTheSpotlight().attemptsTo(this.cloneJob());
                        }
                            break;
                    case TenderType.QR_CODE:
                        this.select(batchName);
                        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext()
                                .then(Switch.toFrame(CCBFrames.MAIN)));
                        theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, Counter.class));
                        theActorInTheSpotlight().attemptsTo(this.cloneJob());
                        DC.getOpenedDC(TenderSourceType.ONLINE_CASHIERING,BatchStatus.OPEN);
                        addTCToDC(tenderSource);
                        if(!(TC.getTC(TenderSource.DBS,BatchStatus.OPEN).length()>0)) {
                            this.select(batchName);
                            theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, QR_Code.class));
                            theActorInTheSpotlight().attemptsTo(this.cloneJob());
                        }
                        break;
                    case TenderType.CREDIT_CARD:
                        this.select(batchName);
                        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext()
                                .then(Switch.toFrame(CCBFrames.MAIN)));
                        theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, Counter.class));
                        theActorInTheSpotlight().attemptsTo(this.cloneJob());
                        DC.getOpenedDC(TenderSourceType.ONLINE_CASHIERING,BatchStatus.OPEN);
                        addTCToDC(tenderSource);
                        if(!(TC.getTC(TenderSource.UOB_POS,BatchStatus.OPEN).length()>0)) {
                            this.select(batchName);
                            theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, CreditCard.class));
                            theActorInTheSpotlight().attemptsTo(this.cloneJob());
                            DC.getOpenedDC(TenderSourceType.ONLINE_CASHIERING,BatchStatus.OPEN);
                        }
                        break;
                    default:
                        this.select(batchName);
                        theActorInTheSpotlight().attemptsTo(this.setBatchParameters(tenderSource, Counter.class));
                        theActorInTheSpotlight().attemptsTo(this.cloneJob());
                        DC.getOpenedDC(TenderSourceType.ONLINE_CASHIERING,BatchStatus.OPEN);
                        addTCToDC(tenderSource);
                        break;
                }break;
        }
        logger.info(()->"CM_cdtpp.create() ended successfully..");
    }

    private void addTCToDC(String tenderSource){
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login(User.CASHIER));
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(DC.searchByChar(Counter.LOCATION));
        theActorInTheSpotlight().attemptsTo(DC.select(PropertiesUtil.getProperties("deposit.control.id")));
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        theActorInTheSpotlight().attemptsTo(AddTenderControl.toDC(tenderSource));
    }

    public Task setBatchParameters(String paymentMode, Class batchType) {
        setBatchDate(PaymentMode.COUNTER,batchType.getSimpleName());
        setBatchUser();
        Field[] field = batchType.getDeclaredFields();
        Map<String, String> fieldNames = getFieldNames(field);
        Helper.switchAtWindow();
        Helper.switchToFrames(CCBFrames.TAB_PAGE,CCBFrames.BJP);
        CCB.getJs().executeScript("arguments[0].scrollIntoView(true);",getWebElement(ccb_batch_job_submission_pageobject.TENDER_SOURCE));
        //theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_batch_job_submission_pageobject.FRAME_BJP,isVisible()));
        return Task.where("Edit batch job properties",
                Scroll.to(ccb_batch_job_submission_pageobject.TENDER_SOURCE),
                Clear.field(ccb_batch_job_submission_pageobject.TENDER_SOURCE),
                Enter.keyValues(fieldNames.get("TENDER")).into(ccb_batch_job_submission_pageobject.TENDER_SOURCE),
                Clear.field(ccb_batch_job_submission_pageobject.COUNTER_FLAG),
                Enter.keyValues(String.valueOf(String.valueOf(fieldNames.get("COUNTER_FLAG")))).into(ccb_batch_job_submission_pageobject.COUNTER_FLAG),
                Clear.field(ccb_batch_job_submission_pageobject.PSP_LOOKUP_VAL),
                Enter.keyValues(fieldNames.get("PSP")).into(ccb_batch_job_submission_pageobject.PSP_LOOKUP_VAL),
                Clear.field(ccb_batch_job_submission_pageobject.LOCATION_VAL),
                Enter.keyValues(fieldNames.get("LOCATION")).into(ccb_batch_job_submission_pageobject.LOCATION_VAL),
                Scroll.to(ccb_batch_job_submission_pageobject.CLAIM_DEPOSIT_CONTROL_FLAG),
                Clear.field(ccb_batch_job_submission_pageobject.CLAIM_DEPOSIT_CONTROL_FLAG),
                Enter.keyValues(String.valueOf(fieldNames.get("CDC"))).into(ccb_batch_job_submission_pageobject.CLAIM_DEPOSIT_CONTROL_FLAG).thenHit(Keys.RETURN));
    }

    private static Map<String, String> getFieldNames(Field[] fields) {
        Map<String, String> fieldNames = new HashMap<>();
        for (Field field : fields) {
            try {
                fieldNames.put(field.getName(), field.get(field.getName()).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fieldNames;
    }

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }
}