package Utility.Payment;

import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchFactory;
import Utility.BatchJob.Counter;
import Utility.CCRS.Item;
import Utility.CCRS.User;
import Utility.Common.FileHandler;
import Utility.SettlementFile.MockBankStatement;
import Utility.SettlementFile.MockChequeFile;
import Utility.SettlementFile.Nets_pos;
import Utility.SaleItem.BuyerDetails;
import Utility.Server.ServerUtil;
import Utility.others.*;
import net.serenitybdd.core.pages.WebElementState;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.ccb_dc_pageobject;
import pageobjects.ccb_dc_search_pageobject;
import pageobjects.ccb_tc_pageobject;
import stepdefinitions.BankReconciliationStepDefinition;
import stepdefinitions.CCRSLoginStepdefinition;
import tasks.*;
import tasks.Batch.AddTenderControl;
import tasks.Batch.BalanceTenderControl;
import tasks.Batch.Batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utility.SettlementFile.MockChequeFile.*;
import static Utility.SettlementFile.MockXml.getXmlFile;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static tasks.DC.select;

public class CounterPayment extends Payment {

    private String bankCode = "1234";
    private String branchCode = "123";
    private String chequeHonouredCode = "00";
    private String chequeDisHonouredCode = "01";
    private String chequeFileName = "CHQSTA";
    private String CASH = "Cash";
    private String CHEQUE = "Cheque";
    private String mode;
    private String paymentServiceCategory = "Plans & Publications";
    private String paymentServiceSubCategory = "Development & Building Control (DBC) Search Fee S7";
    private String saleQuantity;
    private static String tenderType;

    public CounterPayment(Builder builder) {
        super(builder);
    }

    public static class CounterBuilder extends Builder{

        private final String bankCode = "1234";
        private final String branchCode = "123";
        private final String chequeHonouredCode = "00";
        private final String chequeDisHonouredCode = "01";
        private final String chequeFileName = "CHQSTA";
        private final String CASH = "Cash";
        private final String CHEQUE = "Cheque";
        private String tenderSource;
        private String tenderType;
        private String paymentServiceCategory;
        private String paymentServiceSubCategory;
        private String saleQuantity;
        private static String product;
        public static Map<String, String> netsPaymentEventMap = new HashMap<>();
        private static CounterBuilder instance = null;
        @Steps
        CCRSLoginStepdefinition ccrsLoginStepdefinition;
        @Steps
        BankReconciliationStepDefinition bankReconciliationStepDefinition;
        static EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
        static Logger logger = LoggerFactory.getLogger(CounterPayment.class);
        public static CounterBuilder newInstance(){
            if(instance==null){
                instance = new CounterBuilder();
            }
            return instance;
        }

        public static String getProduct() {
            return product;
        }

        public CounterBuilder setProduct(String product) {
            PropertiesUtil.setProperties("sale.item.type",product);
            CounterBuilder.product = product;
            return this;
        }

        @Override
        public Payment build(){
            return new CounterPayment(this);
        }

        public CounterBuilder search(String item){

            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.CASHIER));

            switch (item){
                case Item.NOTICE:
                    theActorInTheSpotlight().attemptsTo(EVMS.searchPayableByNoticeNumber());
                    theActorInTheSpotlight().attemptsTo(EVMS.proceedToPay());
                    break;
                case Item.VEHICLE:
                    theActorInTheSpotlight().attemptsTo(EVMS.searchPayableByVehicleNumber());
                    theActorInTheSpotlight().attemptsTo(EVMS.proceedToPay());
                    break;
                case Item.SALE_ITEM:
                    theActorInTheSpotlight().attemptsTo(AddSaleItems.toCart(this.paymentServiceCategory,this.paymentServiceSubCategory,"automation",this.saleQuantity));
                    theActorInTheSpotlight().attemptsTo(CCB.AddBuyerDetails(BuyerDetails.NAME,BuyerDetails.PIN_CODE,BuyerDetails.MOBILE));
                    break;
            }

            return this;
        }

        public CounterBuilder makePayment() {

            theActorInTheSpotlight().attemptsTo(CCB.addTender(this.tenderType).then(CCB.confirmPayment(this.tenderType)));
            theActorInTheSpotlight().should(eventually(seeThat(
                    WebElementQuestion.the(ccb_tc_pageobject.RECEIPT_ID), WebElementState::isCurrentlyVisible)));
            String receiptID = CCB.getText(ccb_tc_pageobject.RECEIPT_ID);
            PropertiesUtil.setProperties("CCRS.paymentEventID", receiptID);
            netsPaymentEventMap.put(this.tenderType,receiptID);
            System.out.println("netsPaymentEventMap : "+netsPaymentEventMap);
            return this;
        }

        public CounterBuilder getExpectedBankInDate(){

            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                    CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
            if(getTenderType().equalsIgnoreCase(TenderType.QR_CODE)){
//                theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
//                theActorInTheSpotlight().attemptsTo(DC.search(" ", TenderSourceType.LOCKBOX, Status.balancingInProgress));
                theActorInTheSpotlight().attemptsTo(DC.search(PropertiesUtil.getProperties("deposit.control.id")));
                theActorInTheSpotlight().attemptsTo(DC.settlementDate());
            }else {
                theActorInTheSpotlight().attemptsTo(DC.search(PropertiesUtil.getProperties("deposit.control.id")));
                theActorInTheSpotlight().attemptsTo(DC.setDepositControlChar());
            }
            Helper.switchToParentWindow();

            return this;
        }

        public CounterBuilder uploadBankStatement(Map<String, String> map){

            String mockedFilePath = null;
            if(getTenderType().equalsIgnoreCase(TenderType.NETS_POS)) {
                mockedFilePath = MockBankStatement.netsPosPayment(
                        PropertiesUtil.getProperties("dc.settlement.date"), new ArrayList<>(map.values()));
            }else if (getTenderType().equalsIgnoreCase(TenderType.QR_CODE)) {
                mockedFilePath = MockBankStatement.netsPosPayment(
                        PropertiesUtil.getProperties("dc.settlement.date"), new ArrayList<>(map.values()));
            }

            ServerUtil.uploadFile(mockedFilePath, environmentVariables.getProperty("CCRS.BankStatement.server.path"));
            FileHandler.deleteFile("csv");
//            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.UPLOAD_BANK_STATEMENT, true));
//            theActorInTheSpotlight().attemptsTo(Batch.status());

            return this;
        }

        public CounterBuilder uploadBankStatement(List<Map<String, String>> map){

            String mockedFilePath = null;
            if (getTenderType().equalsIgnoreCase(TenderType.QR_CODE)) {
                mockedFilePath = MockBankStatement.counterQRCodePayment(
                        Nets_pos.getBankSettlementFileDate(), map);
            }
            System.out.println("mockedFilePath : "+mockedFilePath);
            ServerUtil.uploadFile(mockedFilePath, environmentVariables.getProperty("CCRS.BankStatement.server.path"));
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.UPLOAD_BANK_STATEMENT, true));
            theActorInTheSpotlight().attemptsTo(Batch.status());

            return this;
        }

        public CounterBuilder uploadBankStatement(){

            String mockedFilePath = null;
            System.out.println("getTenderSource() : "+this.getTenderSource());
            if (getTenderType().equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                mockedFilePath = MockBankStatement.counterCCBankStatement(
                        Nets_pos.getCreditCardBankSettlementFileDate(),
                        PropertiesUtil.getProperties("dc.endingBalanceAmount"));
            }
            ServerUtil.uploadFile(mockedFilePath, environmentVariables.getProperty("CCRS.BankStatement.server.path"));
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.UPLOAD_BANK_STATEMENT, true));
            theActorInTheSpotlight().attemptsTo(Batch.status());

            return this;
        }
        public CounterBuilder settlePayment() {

//            /*
//            * Set batch control status to balancing in progress
//            */
//            theActorInTheSpotlight().attemptsTo(CCB.logout());
//            theActorInTheSpotlight().attemptsTo(CCB.login(User.CASHIER));
//            theActorInTheSpotlight().attemptsTo(TC.moveToBalancingInProgress());
//
//            /*
//             * balance TC and DC
//             */
//            theActorInTheSpotlight().attemptsTo(CCB.logout());
//            theActorInTheSpotlight().attemptsTo(CCB.login(User.CHIEF_CASHIER));
//            theActorInTheSpotlight().attemptsTo(BalanceTenderControl.to());
//            theActorInTheSpotlight().attemptsTo(DC.toBalance());

            /*
            * Mock bank statements
            * settlement business date is same as expected bank in date for counter payment
            * Expected bank in date is settlement date plus one
            */
            String mockedFilePath = MockBankStatement.counterPayment(
                    PropertiesUtil.getProperties("dc.settlement.date"),
                    PropertiesUtil.getProperties("dc.endingBalanceAmount"),
                    PropertiesUtil.getProperties("deposit.control.id"));

            //upload mocked statements to the server
            ServerUtil.uploadFile(mockedFilePath, env.getProperty("CCRS.BankStatement.server.path"));

            /*
            * Settle bank statement
            */
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.UPLOAD_BANK_STATEMENT,true));
            theActorInTheSpotlight().attemptsTo(Batch.status());

            /*
            * Bank reconciliation
            * */
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.BANK_RECONCILIATION,true));

            if (tenderType.equalsIgnoreCase(TenderType.CHEQUE)) {
                String mockedTextFilePath = MockChequeFile.saveMockedFile(MockChequeFile.MockChequeFile(getBankInDate(),
                                chequeDetails.getChequeNo(),
                                CounterInfo.bankCode, CounterInfo.branchCode,
                                extractDigitFromString(PropertiesUtil.getProperties("dc.endingBalanceAmount")),
                                chequeDetails.getAccountNo(), CounterInfo.chequeHonouredCode),
                        getMockedFilePath(CounterInfo.chequeFileName)
                );
                ServerUtil.uploadFile(mockedTextFilePath, env.getProperty("CCRS.Cheque.server.path"));
                theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CHEQUE, true));

            }
            return this;
        }

        public CounterBuilder mockSettlementFile(){
            Nets_pos.mockSettlementFile();
            return this;
        }

        public CounterBuilder mockCCSettlementFile(){
            theActorInTheSpotlight().attemptsTo(MockSettlementFile.ofPayment(this.getTenderType()));
            return this;
        }
        public CounterBuilder runBatch(){
            if(this.getTenderType().equalsIgnoreCase(TenderType.NETS_POS)) {
                Nets_pos.settlementBatch();
            } else if(this.getTenderType().equalsIgnoreCase(TenderType.CREDIT_CARD)){
                String source = getXmlFile(BatchFile.CREDIT_CARD);
                String destination = environmentVariables.getProperty("CCRS.COUNTER.CC.server.path");
                ServerUtil.uploadFile(source,destination);
                theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CREDIT_CARD,true));
//                theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
//                        CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
//                theActorInTheSpotlight().attemptsTo(DC.search(" ", TenderSourceType.LOCKBOX, Status.balanced));
//                try{
//                    theActorInTheSpotlight().attemptsTo(DC.selectDC());
//                }catch (IndexOutOfBoundsException io){
//                    logger.error("DC failed to be balanced");
//                }
            }
            return this;
        }

        public CounterBuilder openNewDCToBalanceExistingDC(){

            String batchBusinessDate = TC.getAvailableDateForTC(this.getTenderType());
            PropertiesUtil.setProperties("batch.business.date",batchBusinessDate);
            if(this.getTenderType().equalsIgnoreCase(TenderType.NETS_POS) || this.getTenderType().equalsIgnoreCase(TenderType.NETS)) {
                theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION,"Search"));
                theActorInTheSpotlight().attemptsTo(SearchBatchJob.of(BatchControl.DEPOSIT_CONTROL));
                theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, CounterNets.class));
                theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).cloneJob());
            } else if(this.getTenderType().equalsIgnoreCase(TenderType.QR_CODE)) {
                theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION,"Search"));
                theActorInTheSpotlight().attemptsTo(SearchBatchJob.of(BatchControl.DEPOSIT_CONTROL));
                theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, QR_Code.class));
                theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).cloneJob());
            } else if(this.getTenderType().equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION,"Search"));
                theActorInTheSpotlight().attemptsTo(SearchBatchJob.of(BatchControl.DEPOSIT_CONTROL));
                theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).setBatchParameters(tenderSource, CreditCard.class));
                theActorInTheSpotlight().attemptsTo(BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).cloneJob());
//                theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
//                        CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
//                theActorInTheSpotlight().attemptsTo(DC.search(" ", TenderSourceType.LOCKBOX, Status.balancingInProgress));
//                try{
//                    theActorInTheSpotlight().attemptsTo(DC.selectDC());
//                }catch (IndexOutOfBoundsException io){
//                    logger.error("DC failed to moved to balancing in progress on opening new DC");
//                }
            }
            return this;
        }
        /*
            Function will do the followings:
                1. Search for any available TC
                2. Capture ending balance
                3. Add sale item if TC having no sale item
                4. Open TC if no TC found
         */
        public CounterBuilder addTC(String psc, String pssc, String itemName, String quantity){

            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                    CashierSubMenu.DEPOSIT_CONTROL, SubSubMenu.SEARCH.name()));
            theActorInTheSpotlight().attemptsTo(DC.searchByChar(Counter.LOCATION));
            theActorInTheSpotlight().attemptsTo(select(
                    PropertiesUtil.getProperties("deposit.control.id")));
            Helper.switchAtWindow();
            Helper.switchToFrames("main","tabMenu");
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_TENDER_CONTROL_MENU_TAB));
            Helper.switchToFrames("main","tabPage","Section2_tndrCtlGrd");
            int listOfTcs = ccb_tc_pageobject.TENDEDER_CONTROL_LIST
                    .resolveAllFor(theActorInTheSpotlight()).size();
            String endingBalance = null;
            if(listOfTcs>0){
                endingBalance = ccb_dc_search_pageobject.SEARCH_RESULT("Expected Ending Balance")
                        .resolveFor(theActorInTheSpotlight()).getText().trim();}
            if(listOfTcs==0){
                theActorInTheSpotlight().attemptsTo(AddTenderControl.toDC(Counter.TENDER));
                addSaleItems(psc,pssc,itemName,quantity);
            }else if(endingBalance.equalsIgnoreCase("S$0.00")){
                theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Counter.name(), CashierSubMenu.COUNTER_PAYMENTS));
                addSaleItems(psc,pssc,itemName,quantity);
            }
            else{
                Helper.switchToFrames("main","tabMenu");
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
            }
            return this;
        }

        private CounterBuilder addSaleItems(String psc, String pssc, String itemName, String quantity) {
            theActorInTheSpotlight().attemptsTo(AddSaleItems.toCart(psc,pssc,itemName,quantity));
            return this;
        }

        public String getBankCode() {
            return bankCode;
        }
        public String getBranchCode() {
            return branchCode;
        }
        public String getChequeHonouredCode() {
            return chequeHonouredCode;
        }
        public String getChequeDisHonouredCode() {
            return chequeDisHonouredCode;
        }
        public String getChequeFileName() {
            return chequeFileName;
        }
        public String getCASH() {
            return CASH;
        }
        public String getCHEQUE() {
            return CHEQUE;
        }
        public String getTenderSource() {
            return tenderSource;
        }
        public CounterBuilder setTenderSource(String tenderSource) {
            this.tenderSource = tenderSource;
            return this;
        }

        public String getTenderType() {
            return tenderType;
        }

        public CounterBuilder setTenderType(String tenderType) {
            PropertiesUtil.setProperties("tender.type",tenderType);
            this.tenderType = tenderType;
            return this;
        }

        public String getPaymentServiceCategory() {
            return paymentServiceCategory;
        }

        public CounterBuilder setPaymentServiceCategory(String paymentServiceCategory) {
            this.paymentServiceCategory = paymentServiceCategory;
            return this;
        }

        public String getPaymentServiceSubCategory() {
            return paymentServiceSubCategory;
        }

        public CounterBuilder setPaymentServiceSubCategory(String paymentServiceSubCategory) {
            this.paymentServiceSubCategory = paymentServiceSubCategory.trim();
            return this;
        }

        public String getSaleQuantity() {
            return saleQuantity;
        }

        public CounterBuilder setSaleQuantity(String saleQuantity) {
            this.saleQuantity = saleQuantity;
            return this;
        }
    }
    public static String getTenderType() {
        return tenderType;
    }
    @Override
    public Payment search(String item) {
        return null;
    }
    @Override
    public Payment makePayment() {
        return this;
    }
    @Override
    public Payment mockSettlementFile() {
        return this;
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
}
