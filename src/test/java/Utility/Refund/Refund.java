package Utility.Refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tasks.CCB;
import tasks.TransactionDetails;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Refund {

    public String refundMode;
    public String job;
    public String account;
    public String bank;
    public String businessSystem = "Enterprise Violation Management System";
    public String refundType = "full";
    public String application;
    public abstract Refund transactionDetails();
    public abstract Refund refundDetails();
    public abstract Refund refundInitiation();
    public abstract Refund coApproval();
    public abstract Refund aoApproval();
    public abstract Refund doApproval();
    public abstract Refund createVendorFile();
    public abstract Refund moveCaseToSapChannel();
    public abstract Refund clearance();
    //public abstract ChequeRefund build();
    public Refund(Builder builder){
        this.bank = builder.bank;
        this.refundType = builder.refundType;
        this.refundMode = builder.refundMode;
        this.job = builder.job;
        this.account = builder.account;
        this.businessSystem = builder.businessSystem;
        this.application = builder.application;
    }
    public abstract static class Builder{
        private String refundMode;
        private String job;
        private String account;
        private String bank;
        private String businessSystem = "Enterprise Violation Management System";
        private String refundType = "full";
        private String application;
        public abstract Refund build();
        public Builder transactionDetails(){
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login("RO"));
            theActorInTheSpotlight().attemptsTo(TransactionDetails.add(getRefundType(), getBusinessSystem(),getApplication()));
            return this;
        };

        public Builder setRefundMode(String refundMode) {
            this.refundMode = refundMode;
            return this;
        }

        public Builder setJob(String job) {

            this.job = job;
            return this;
        }

        public Builder setAccount(String account) {
            this.account = account;
            return this;
        }

        public Builder setBank(String bank) {
            this.bank = bank;
            return this;
        }

        public Builder setBusinessSystem(String businessSystem) {
            this.businessSystem = businessSystem;
            return this;
        }

        public Builder setRefundType(String refundType) {
            this.refundType = refundType;
            return this;
        }

        public String getRefundMode() {
            return refundMode;
        }

        public String getJob() {
            return job;
        }

        public String getAccount() {
            return account;
        }

        public String getBank() {
            return bank;
        }
        public String getBusinessSystem() {
            return businessSystem;
        }

        public String getRefundType() {
            return refundType;
        }

        public String getApplication() {
            return application;
        }
        public Builder setApplication(String application){
            this.application = application;
            return this;
        }
    }
}
