package Utility.Payment;

import Utility.CCRS.Item;
import Utility.CCRS.User;
import Utility.Notice.Notice;
import Utility.Server.ServerUtil;
import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchFactory;
import Utility.BatchJob.BatchStatus;
import Utility.Depositontrol.Status;
import Utility.SettlementFile.MockBankStatement;
import Utility.SaleItem.Portal;
import Utility.SettlementFile.Nets_pos;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.screenplay.actors.OnStage;
import Utility.BatchJob.Braintree;
import tasks.*;
import tasks.Batch.Batch;

import static Utility.SettlementFile.MockXml.getXmlFile;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class PortalPayment extends Payment{

    static Logger logger = LoggerFactory.getLogger(PortalPayment.class);
    private String mode;
    private String product;
    public static class PortalBuilder extends Builder {

        private String tenderSource;
        private String tenderType;
        private static String product;
        private static PortalBuilder single_instance = null;

        public static PortalBuilder newInstance(){
            if(single_instance == null){
                single_instance = new PortalBuilder();
            }
            return single_instance;
        }

        public PortalBuilder search(String item) {
            logger.info(()->String.format("search notice item at %s",item));
            setProduct(item);
            if (item.equalsIgnoreCase(Item.NOTICE)){
                Notice.searchNotice(getTenderSource());
            }else if (item.equalsIgnoreCase(Item.SALE_ITEM)){
                theActorInTheSpotlight().attemptsTo(Portal.addToCart());
                theActorInTheSpotlight().attemptsTo(Portal.viewCart());
                theActorInTheSpotlight().attemptsTo(Portal.buyerDetails());
                theActorInTheSpotlight().attemptsTo(Portal.enterBuyerDetails());
            }
            return this;
        }

        public PortalBuilder makePayment() {
            theActorInTheSpotlight().attemptsTo(MakePayment.using(this.getTenderType()));
            return this;
        }

        public PortalBuilder mockSettlementFile() {
            OnStage.theActor("James");
            theActorCalled("James").attemptsTo(OpenTheApplication.onTheHomePage());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
            theActorInTheSpotlight().attemptsTo(MockSettlementFile.ofPayment(this.getTenderType()));
            return this;
        }

        public PortalBuilder uploadBankStatement(){

            String mockedFilePath = null;
            if(this.getTenderSource().equalsIgnoreCase(TenderSource.STRIPE)) {
                mockedFilePath = MockBankStatement.stripePayment(
                        PropertiesUtil.getProperties("dc.settlement.date"),
                        PropertiesUtil.getProperties("dc.endingBalanceAmount")
                );
            }else if(this.getTenderSource().equalsIgnoreCase(TenderSource.BRAINTREE)) {
                mockedFilePath = MockBankStatement.braintreePayment(
                        PropertiesUtil.getProperties("dc.settlement.date"),
                        PropertiesUtil.getProperties("dc.endingBalanceAmount")
                );
            }else if(this.getTenderSource().equalsIgnoreCase(TenderSource.PAYPAL)) {
                mockedFilePath = MockBankStatement.paypalPayment(
                        PropertiesUtil.getProperties("dc.settlement.date"),
                        PropertiesUtil.getProperties("dc.endingBalanceAmount")
                );
            }else if(this.getTenderSource().equalsIgnoreCase(TenderSource.DBS)) {
                mockedFilePath = MockBankStatement.counterQRCodePayment(
                        PropertiesUtil.getProperties("dc.settlement.date"),
                        Nets_pos.getData(PaymentMode.SGQR_PAYNOW)
                );
            }else if(this.getTenderSource().equalsIgnoreCase(TenderSource.ENETS_DEBIT)) {
                mockedFilePath = MockBankStatement.enetsDebitPayment(
                        PropertiesUtil.getProperties("dc.expectedBankInDate"),
                        PropertiesUtil.getProperties("dc.endingBalanceAmount")
                );
            }else if (this.getTenderSource().equalsIgnoreCase(TenderSource.CREDIT_CARD)) {
                mockedFilePath = MockBankStatement.counterCCBankStatement(
                        Nets_pos.getCreditCardBankSettlementFileDate(),
                        PropertiesUtil.getProperties("dc.endingBalanceAmount"));
            }

            ServerUtil.uploadFile(mockedFilePath, environmentVariables.getProperty("CCRS.BankStatement.server.path"));
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.UPLOAD_BANK_STATEMENT, true));
            theActorInTheSpotlight().attemptsTo(Batch.status());

            return this;
        }
        public PortalBuilder openNewDCToBalanceExistingDC(){

            String batchBusinessDate = TC.getAvailableDateForTC(this.getTenderSource());
            logger.info(()->String.format("new DC business date is %s",batchBusinessDate));
            PropertiesUtil.setProperties("batch.business.date",batchBusinessDate);
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION,"Search"));
            theActorInTheSpotlight().attemptsTo(SearchBatchJob.of(BatchControl.DEPOSIT_CONTROL));

            switch(this.getTenderType()){
                case PaymentMode.STRIPE:
                    theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, Stripe.class));
                    break;
                case PaymentMode.BRAINTREE:
                    theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, Braintree.class));
                    break;
                case PaymentMode.PAYPAL:
                    theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, Paypal.class));
                    break;
                case PaymentMode.ENETS_DEBIT:
                    theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, EnetsDebit.class));
                    break;
                case PaymentMode.CREDIT_CARD:
                    theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, CreditCard.class));
                    break;
                case PaymentMode.SGQR_PAYNOW:
                    theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, QR_Code.class));
                    break;
            }
            theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).cloneJob());
            //theActorInTheSpotlight().attemptsTo(DC.verifyDCStatus(BatchStatus.BALANCING_IN_PROGRESS));
            return this;
        }

        public PortalBuilder getExpectedBankInDate(){
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                    CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
            if(getTenderType().equalsIgnoreCase(PaymentMode.SGQR_PAYNOW)){
                theActorInTheSpotlight().attemptsTo(DC.search(PropertiesUtil.getProperties("deposit.control.id")));
                theActorInTheSpotlight().attemptsTo(DC.settlementDate());
            }else {
//                theActorInTheSpotlight().attemptsTo(DC.search("", Utility.BatchJob.TenderSourceType.LOCKBOX, Status.balanced));
//                theActorInTheSpotlight().attemptsTo(DC.selectDC());
                theActorInTheSpotlight().attemptsTo(DC.search(PropertiesUtil.getProperties("deposit.control.id")));
                theActorInTheSpotlight().attemptsTo(DC.setDepositControlChar());
            }

            return this;
        }

        public PortalBuilder runSettlementBatch(){

            String source;
            String destination;
            switch(getTenderType())
            {
                case TenderType.STRIPE:
                    source = getXmlFile(BatchFile.STRIPE_SETTLEMENT);
                    destination = environmentVariables.getProperty("CCRS.EVMS.ST.CSV.server.path");
                    ServerUtil.uploadFile(source,destination,true,"csv");
                    theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.STRIPE,true));
                    break;
                case TenderType.BRAINTREE:
                    source = getXmlFile(BatchFile.BRAINTREE_SETTLEMENT);
                    destination = environmentVariables.getProperty("CCRS.EVMS.BT.CSV.server.path");
                    ServerUtil.uploadFile(source,destination,true,"csv");
                    theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.BRAINTREE,true));
                    break;
                case TenderType.PAYPAL:
                    source = getXmlFile(BatchFile.PAYPAL_SETTLEMENT);
                    destination = environmentVariables.getProperty("CCRS.EVMS.PAYPAL.CSV.server.path");
                    ServerUtil.uploadFile(source,destination,true,"csv");
                    theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.PAYPAL_SETTLEMENT_JOB,true));
                    break;
                case TenderType.ENETS_DEBIT:
                    source = getXmlFile(BatchFile.ENETS_DEBIT_SETTLEMENT);
                    destination = environmentVariables.getProperty("CCRS.EVMS.Enets.debit.CSV.server.path");
                    ServerUtil.uploadFile(source,destination,true,"rpt");
                    theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.ENETS_DEBIT_SETTLEMENT_JOB,true));
                    break;
            }
                //verifyDCStatus("I");
                return this;
        }

        public Payment build(){
            return new PortalPayment(this);
        }

        public String getTenderSource() {
            return tenderSource;
        }

        public PortalBuilder setTenderSource(String tenderSource) {
            this.tenderSource = tenderSource;
            return this;
        }

        public String getTenderType() {
            return tenderType;
        }

        public PortalBuilder setTenderType(String tenderType) {
            this.tenderType = tenderType;
            return this;
        }

        public String getProduct() {
            return product;
        }

        public PortalBuilder setProduct(String product) {
            PropertiesUtil.setProperties("sale.item.type",product);
            this.product = product;
            return this;
        }
    }
    public PortalPayment(Builder builder){
        super(builder);
    }
    @Override
    public Payment search(String item) {
        return null;
    }

    @Override
    public Payment makePayment() {
        return null;
    }

    @Override
    public Payment mockSettlementFile() {
        return null;
    }

    @Override
    public Payment uploadBankStatement() {
        return null;
    }

    @Override
    public Payment uploadSettlementFile() {
        return null;
    }

    @Override
    public Payment bankReconciliation() {
        return null;
    }

    @Override
    public Payment verifyDCStatus() {
        return null;
    }

    public static void main(String[] args) {
        PortalPayment.PortalBuilder.newInstance().build();
    }
}
