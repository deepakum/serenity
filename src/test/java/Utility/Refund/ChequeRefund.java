package Utility.Refund;

import Utility.others.Helper;
import tasks.RefundInitiation;
import tasks.VerifyCaseStatus;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class ChequeRefund extends Refund{

    private static String reason = Reason.DISPUTE;
    private String refundMode;
    private String job;
    private String account;
    public String bank;
    private String businessSystem = "Enterprise Violation Management System";
    private String refundType = "full";
    private String application;
    public ChequeRefund(Builder builder){
        super(builder);
        this.refundMode = builder.getRefundMode();
    }

    public static String getReason() {
        return reason;
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
    public String getBusinessSystem() {
        return businessSystem;
    }

    @Override
    public String getRefundType() {
        return refundType;
    }

    @Override
    public String getApplication() {
        return application;
    }

    public static class ChequeRefundBuilder extends Builder{

        @Override
        public Refund build() {
            return new ChequeRefund(this);
        }

        public static ChequeRefundBuilder newInstance(){
            return new ChequeRefundBuilder();
        }
        public ChequeRefundBuilder refundDetails() {
            theActorInTheSpotlight().attemptsTo(RefundInitiation.using(this.getBusinessSystem(),
                    this.getRefundMode(), getReason(),this.getAccount(),this.getBank(),this.getApplication()));
            theActorInTheSpotlight().attemptsTo(VerifyCaseStatus.withText(CaseStatus.level_1));
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
         new ChequeRefund.ChequeRefundBuilder().setRefundMode("abcd").build();
    }
}
