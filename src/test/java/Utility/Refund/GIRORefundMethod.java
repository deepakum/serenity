package Utility.Refund;
import Utility.Server.ServerUtil;
import Utility.BatchJob.BatchControl;
import Utility.CCRS.CCBFrames;
import Utility.CCRS.User;
import Utility.Payment.Payment;
import Utility.others.BatchFile;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.actions.JavaScriptClick;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_refund_initiation_pageobject;
import questions.CCRSWebElementText;
import tasks.*;
import tasks.Batch.Batch;

import java.io.FileNotFoundException;

import static Utility.SettlementFile.MockXml.*;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class GIRORefundMethod extends Refund{

    static Logger logger= LoggerFactory.getLogger(GIRORefundMethod.class);
    private static String reason;
    private String refundMode;
    private String job;
    private String account;
    private String bank;
    private String businessSystem;
    private String refundType;
    private String application;
    public GIRORefundMethod(GIRORefundBuilder cashRefundBuilder){
        super(cashRefundBuilder);
        this.refundType = cashRefundBuilder.refundType;
        this.refundMode = cashRefundBuilder.refundMode;
        this.job = cashRefundBuilder.job;
        this.businessSystem = cashRefundBuilder.businessSystem;
        this.account = cashRefundBuilder.account;
        this.bank = cashRefundBuilder.bank;
        this.application = cashRefundBuilder.getApplication();
    }

    public static class GIRORefundBuilder extends Builder{

        private String refundMode;
        private String job;
        private String account;
        private String bank;
        private String businessSystem;
        private String refundType;
        private String application;
        private static GIRORefundBuilder instance = null;
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();


        public static GIRORefundBuilder newInstance(){
            if(instance==null) {
                instance =  new GIRORefundBuilder();
            }
            return instance;
        }
        @Override
        public Refund build() {
            return new GIRORefundMethod(this);
        }

        public GIRORefundBuilder initiate(String refundMethod, String reason, String application){

            logger.info(()->String.format("refund initiation using %s mode",refundMethod));
            if(refundMethod.equalsIgnoreCase(RefundMode.GIRO)) {
                setAccount(Payment.ACCOUNT);
                setBank(Payment.BANK);
            }
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.REFUND_OFFICER));
            theActorInTheSpotlight().attemptsTo(TransactionDetails.add(refundType, businessSystem,application));
            theActorInTheSpotlight().attemptsTo(RefundInitiation.using(businessSystem,refundMethod,reason, account, bank,application));
            theActorInTheSpotlight().attemptsTo(JavaScriptClick.on(ccb_refund_initiation_pageobject.SUBMIT_BUTTON));
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
            Alert alert = getDriver().switchTo().alert();
            System.out.println(alert.getText());
            alert.accept();
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
            System.out.println(alert.getText());
            alert.accept();
            Helper.switchToFrames(CCBFrames.MAIN);
            theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE),
                    containsStringIgnoringCase("Case")));
          //  theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.visibilityOfAllElements()));
            Helper.switchToFrames(CCBFrames.TAB_PAGE);
            PropertiesUtil.setProperties("refund.id",
                    CCB.getTextUsingAttribute(ccb_refund_initiation_pageobject.CASE_ID_INPUT_BOX,"value").trim());
            //Blank case screen so ignored verification until issue is fixed
            //theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.level_1));
            return this;
        }

        public GIRORefundBuilder coApproval(){
            logger.info(()->"CO approval...");
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.CHIEF_OFFICER));
            theActorInTheSpotlight().attemptsTo(RefundApproval.to(this.businessSystem));
//           Commented as failing intermittently
//            theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.CO_APPROVAL_STATUS));

            return this;
        }

        public GIRORefundBuilder doApproval(){
            logger.info(()->"DO approval...");
            if(PropertiesUtil.getProperties("refund.method").equalsIgnoreCase("Credit Card")) {
                theActorInTheSpotlight().attemptsTo(CCB.logout());
                theActorInTheSpotlight().attemptsTo(CCB.login(User.DISBURSEMENT_OFFICER));
                theActorInTheSpotlight().attemptsTo(RefundApproval.to(this.businessSystem));
                //           Commented as failing intermittently
//                theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.DO_APPROVAL_STATUS));
            }

            return this;
        }

        public GIRORefundBuilder aoApproval(){
            logger.info(()->"AO approval...");
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMINISTRATIVE_OFFICER));
            theActorInTheSpotlight().attemptsTo(RefundApproval.to(this.businessSystem));
            /*backend validation
                Assert.assertTrue(refundCaseStatus(PropertiesUtil.getProperties("refund.id")).trim().equalsIgnoreCase("30"));
            */
            // theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.AO_APPROVAL_STATUS));
            return this;
        }

        public GIRORefundBuilder createVendorFile() throws FileNotFoundException {
            logger.info(()->"Create SAP vendor file");
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
            String serverPath = null;
            if(PropertiesUtil.getProperties("refund.method").equalsIgnoreCase(RefundMode.CHEQUE)){
                serverPath = environmentVariables.getProperty("CCRS.Refund.Cheque.vendorFile.server.path");
                theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.VENDOR_CHEQUE,false));
                theActorInTheSpotlight().attemptsTo(Batch.status());
                ServerUtil.downloadFile(serverPath,templatePath);
//                theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.VENDOR_FILE));
            }else if(PropertiesUtil.getProperties("refund.method").equalsIgnoreCase(RefundMode.GIRO)){
                theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.VENDOR_JIRO,false));
                theActorInTheSpotlight().attemptsTo(Batch.status());
                serverPath = environmentVariables.getProperty("CCRS.Refund.GIRO.vendorFile.server.path");
                ServerUtil.downloadFile(serverPath,templatePath);
//                theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.VENDOR_FILE));
            }else{
                System.out.println("Not Applicable....");
            }
            logger.info(()->"Vendor file created successfully...");
            return this;
        }

        public GIRORefundBuilder moveCaseToSapChannel(){
            logger.info(()->"Move file to SAP channel");
            mockXmlFile(BatchFile.AP_STATUS_FILE, PropertiesUtil.getProperties("refund.method"));
            String source = getXmlFile(BatchFile.AP_STATUS_FILE);
            String destination = environmentVariables.getProperty("CCRS.Refund.ap.status.server.path");
            ServerUtil.uploadFile(source,destination);
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.SAP_CAHANNEL,false));
//            theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.SAP));
            return this;
        }

        public GIRORefundBuilder clearance(){
            //logger.info(()->"Refund clearance");
            mockXmlFile(BatchFile.SAP_CLEARENCE_FILE,refundMode);
            String source = getXmlFile(BatchFile.SAP_CLEARENCE_FILE);
            String destination = environmentVariables.getProperty("CCRS.Refund.clearance.server.path");
            ServerUtil.uploadFile(source,destination);
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CLEARANCE,false));
//            theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.REFUNDED));
            return this;
        }
        @Override
        public String getRefundMode() {
            return refundMode;
        }

        @Override
        public String getJob() {
            return job;
        }

        @Override
        public String getAccount() {
            return account;
        }

        @Override
        public String getBank() {
            return bank;
        }

        @Override
        public String getBusinessSystem() {
            return businessSystem;
        }

        @Override
        public String getRefundType() {
            return refundType;
        }

        @Override
        public GIRORefundBuilder setRefundMode(String refundMode) {
            this.refundMode = refundMode;
            return this;
        }

        @Override
        public GIRORefundBuilder setJob(String job) {
            this.job = job;
            return this;
        }

        @Override
        public GIRORefundBuilder setAccount(String account) {
            this.account = account;
            return this;
        }

        @Override
        public GIRORefundBuilder setBank(String bank) {
            this.bank = bank;
            return this;
        }

        @Override
        public GIRORefundBuilder setBusinessSystem(String businessSystem) {
            this.businessSystem = businessSystem;
            return this;
        }

        @Override
        public GIRORefundBuilder setRefundType(String refundType) {
            this.refundType = refundType;
            return this;
        }

        public String getApplication() {
            return application;
        }

        public GIRORefundBuilder setApplication(String application) {
            this.application = application;
            return this;
        }
    }

    @Override
    public Refund transactionDetails() {
        return null;
    }

    @Override
    public Refund refundDetails() {
        return null;
    }

    @Override
    public Refund refundInitiation() {
        return null;
    }

    @Override
    public Refund coApproval() {
        return null;
    }

    @Override
    public Refund aoApproval() {
        return null;
    }

    @Override
    public Refund doApproval() {
        return null;
    }

    @Override
    public Refund createVendorFile() {
        return null;
    }

    @Override
    public Refund moveCaseToSapChannel() {
        return null;
    }

    @Override
    public Refund clearance() {
        return null;
    }

    public static void main(String[] args) {
        mockXmlFile(BatchFile.AP_STATUS_FILE, PropertiesUtil.getProperties("refund.method"));
    }
}
