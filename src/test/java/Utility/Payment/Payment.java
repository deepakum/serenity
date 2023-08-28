package Utility.Payment;

import Utility.BatchJob.BatchControl;
import Utility.CCRS.User;
import Utility.Depositontrol.Status;
import Utility.others.CashierMenu;
import Utility.others.CashierSubMenu;
import Utility.others.PropertiesUtil;
import Utility.others.SubSubMenu;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.Batch.Batch;
import tasks.CCB;
import tasks.DC;
import tasks.Navigate;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public abstract class Payment {

    public static String COUNTER = "COUNTER";
    public static String PORTAL = "Portal";
    public static String BANK = "DBS Bank Ltd";
    public static String ACCOUNT = "1234567890";
    public String tenderType;
    public String tenderSource;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public abstract Payment search(String item);
    public abstract Payment makePayment();
    public abstract Payment mockSettlementFile();
    public abstract Payment uploadBankStatement();
    public abstract Payment uploadSettlementFile();
    public abstract Payment bankReconciliation();
    public abstract Payment verifyDCStatus();

    public Payment(Builder builder){
        this.COUNTER = builder.COUNTER;
        this.PORTAL = builder.PORTAL;
        this.BANK = builder.BANK;
        this.ACCOUNT = builder.ACCOUNT;
        this.tenderType = builder.tenderType;
        this.tenderSource = builder.tenderSource;
    }
    public abstract static class Builder{

        private String COUNTER = "COUNTER";
        private String PORTAL = "Portal";
        private String BANK = "DBS Bank Ltd";
        private String ACCOUNT = "1234567890";
        private String tenderType;
        private String tenderSource;

        public String getCOUNTER() {
            return COUNTER;
        }

        public Builder setCOUNTER(String COUNTER) {
            this.COUNTER = COUNTER;
            return this;
        }

        public String getPORTAL() {
            return PORTAL;
        }

        public Builder setPORTAL(String PORTAL) {
            this.PORTAL = PORTAL;
            return this;
        }

        public String getBANK() {
            return BANK;
        }

        public Builder setBANK(String BANK) {
            this.BANK = BANK;
            return this;
        }

        public String getACCOUNT() {
            return ACCOUNT;
        }

        public Builder setACCOUNT(String ACCOUNT) {
            this.ACCOUNT = ACCOUNT;
            return this;
        }

        public abstract Payment build();

        public Builder bankReconciliation(){
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
            if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.QR_CODE) || PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.PORTAL_QR_CODE)){
                theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.QR_CODE_BANK_RECONCILIATION, true));
            }else {
                theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.BANK_RECONCILIATION, true));
            }
            //verifyReconciliationStatus("R");
            return this;
        };

        public Builder verifyReconciliationStatus(String status){
            String tenderType;
            if(this.tenderType.equalsIgnoreCase(TenderType.QR_CODE)){
                tenderType = "DBS";
            }else {
                tenderType = PropertiesUtil.getProperties("tender.type");
            }
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                    CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
            theActorInTheSpotlight().attemptsTo(DC.search("", tenderType, Status.balanced));
            theActorInTheSpotlight().attemptsTo(DC.selectDC());
            theActorInTheSpotlight().attemptsTo(DC.verifyStatus(status));
            return this;
        }
    }
}
