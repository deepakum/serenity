package Utility.Refund;

import Utility.others.Helper;
import Utility.Payment.Payment;
import Utility.others.PropertiesUtil;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Steps;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_refund_initiation_pageobject;
import questions.CCRSWebElementText;
import stepdefinitions.CCRSLoginStepdefinition;
import tasks.CCB;
import tasks.RefundInitiation;
import tasks.TransactionDetails;
import tasks.VerifyCaseStatus;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class CashRefund extends Refund{

    private static String reason = Reason.DISPUTE;
    private String refundMode;
    private String job;
    private String account;
    private String bank;
    private String businessSystem = "Enterprise Violation Management System";
    private String refundType = "full";
    public CashRefund(CashRefundBuilder cashRefundBuilder){
        super(cashRefundBuilder);
        this.refundType = cashRefundBuilder.refundType;
        this.refundMode = cashRefundBuilder.refundMode;
        this.job = cashRefundBuilder.job;
        this.businessSystem = cashRefundBuilder.businessSystem;
        this.account = cashRefundBuilder.account;
        this.bank = cashRefundBuilder.bank;
    }

    public static class CashRefundBuilder extends Builder{

        private String refundMode;
        private String job;
        private String account;
        private String bank;
        private String businessSystem;
        private String refundType;
        private String application;
        @Steps
        private
        CCRSLoginStepdefinition ccrsLoginStepdefinition;

        public static CashRefundBuilder newInstance(){
            return new CashRefundBuilder();
        }
        @Override
        public Refund build() {
            return new CashRefund(this);
        }

        public CashRefundBuilder refundInitiation(String refundMethod,String businessSystem,String reason,String percentage){
            //refundMode,businessSystem,reason,percentage

            if(refundMethod.equalsIgnoreCase("GIRO")) {
                setAccount(Payment.ACCOUNT);
                setBank(Payment.BANK);
            }
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login("RO"));
            theActorInTheSpotlight().attemptsTo(TransactionDetails.add(percentage, businessSystem,"counter"));
            theActorInTheSpotlight().attemptsTo(RefundInitiation.using(businessSystem,refundMethod,reason, account, bank, getApplication()));
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", ccb_refund_initiation_pageobject.SUBMIT_BUTTON.resolveFor(theActorInTheSpotlight()));
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
            Alert alert = getDriver().switchTo().alert();
            System.out.println(alert.getText());
            alert.accept();
            theActorInTheSpotlight().attemptsTo(WaitUntil.the(ExpectedConditions.alertIsPresent()));
            System.out.println(alert.getText());
            alert.accept();
            Helper.switchToFrames("main");
            theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE),
                    containsStringIgnoringCase("Case")));
            Helper.switchToFrames("tabPage");
            PropertiesUtil.setProperties("refund.id",
                    ccb_refund_initiation_pageobject.CASE_ID_INPUT_BOX.resolveFor(theActorInTheSpotlight()).getAttribute("value").trim());
            theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText("Pending Level 1 Approver"));
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
        public CashRefundBuilder setRefundMode(String refundMode) {
            this.refundMode = refundMode;
            return this;
        }

        @Override
        public CashRefundBuilder setJob(String job) {
            this.job = job;
            return this;
        }

        @Override
        public CashRefundBuilder setAccount(String account) {
            this.account = account;
            return this;
        }

        @Override
        public CashRefundBuilder setBank(String bank) {
            this.bank = bank;
            return this;
        }

        @Override
        public CashRefundBuilder setBusinessSystem(String businessSystem) {
            this.businessSystem = businessSystem;
            return this;
        }

        @Override
        public CashRefundBuilder setRefundType(String refundType) {
            this.refundType = refundType;
            return this;
        }

        public String getApplication() {
            return application;
        }

        public CashRefundBuilder setApplication(String application) {
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
        new CashRefund.CashRefundBuilder().transactionDetails().build();
    }
}
