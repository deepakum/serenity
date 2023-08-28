package stepdefinitions;

import Utility.BatchJob.BatchControl;
import Utility.CCRS.Item;
import Utility.CCRS.User;
import Utility.Payment.*;
import Utility.Refund.RefundMode;
import Utility.SettlementFile.MockBankStatement;
import Utility.SettlementFile.MockChequeFile;
import Utility.SettlementFile.Nets_pos;
import Utility.Refund.GIRORefundMethod;
import Utility.Server.ServerUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import Utility.others.BatchFile;
import Utility.others.PropertiesUtil;
import tasks.*;
import tasks.Batch.BalanceTenderControl;
import tasks.Batch.Batch;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static Utility.SettlementFile.MockChequeFile.*;
import static Utility.SettlementFile.MockXml.getXmlFile;
import static Utility.SettlementFile.MockXml.mockNoticeFile;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class CounterStepDefinition {

    static Logger logger = LoggerFactory.getLogger(CounterStepDefinition.class);
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    @Steps
    BankReconciliationStepDefinition bankReconciliationStepDefinition;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    static Map<String, String> result;
    @And("he waits for the bank to process the {string} payment")
    public void heWaitsForTheBankToProcessThePayment(String tenderType) {

        theActorInTheSpotlight().attemptsTo(CCB.logout());
        ccrsLoginStepdefinition.james_provides_credential_as("Cashier");
        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
        theActorInTheSpotlight().attemptsTo(TC.moveToBalancingInProgress());
                /*\Play role of a Chief cashier and perform following
            1. Balance TC
            2. Balance DC
          **/
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login("chief"));
        theActorInTheSpotlight().attemptsTo(BalanceTenderControl.to());
        theActorInTheSpotlight().attemptsTo(DC.toBalance());

        //Mock bank statements
        String mockedFilePath = MockBankStatement.counterPayment(
                PropertiesUtil.getProperties("dc.expectedBankInDate"),
                PropertiesUtil.getProperties("dc.endingBalanceAmount"),
                PropertiesUtil.getProperties("deposit.control.id"));

        //upload mocked statements to the server
        ServerUtil.uploadFile(mockedFilePath, environmentVariables.getProperty("CCRS.BankStatement.server.path"));

        bankReconciliationStepDefinition.he_uploads_bank_statement();
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login("Admin"));
        bankReconciliationStepDefinition.he_attempts_to_reconcile_bank_statement();

        if(tenderType.equalsIgnoreCase(TenderType.CHEQUE)){
            String mockedTextFilePath = MockChequeFile.saveMockedFile(MockChequeFile(getBankInDate(),
                            chequeDetails.getChequeNo(),
                            CounterInfo.bankCode, CounterInfo.branchCode,
                            extractDigitFromString(PropertiesUtil.getProperties("dc.endingBalanceAmount")),
                            chequeDetails.getAccountNo(), CounterInfo.chequeHonouredCode),
                            getMockedFilePath(CounterInfo.chequeFileName)
            );
            ServerUtil.uploadFile(mockedTextFilePath, environmentVariables.getProperty("CCRS.Cheque.server.path"));
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CHEQUE,true));
        }
    }
    @When("he claims refund siting {string} for his transaction")
    public void he_claims_refund_siting_for_his_transaction(String reason) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("his claim is approved or rejected {string} depends on the eligibility {string}")
    public void hisClaimIsApprovedOrRejectedDependsOnTheEligibility(String status, String eligibility) {
    }
    @Then("refund will be processed based on the {string} on selected {string} of payment")
    public void refund_will_be_processed_based_on_the_on_selected_of_payment(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("James attempts to clear fines at counter using {string} payment method")
    public void james_attempts_to_clear_fines_at_counter_using_payment_method(String tenderType) throws Exception {
        CounterPayment.CounterBuilder.newInstance()
                .setTenderType(tenderType)
                .setTenderSource(TenderSource.COUNTER)
                .setProduct(Item.NOTICE)
                .search(Item.NOTICE)
                .makePayment()
                .settlePayment()
                .build();
    }
    @When("James does nothing")
    public void jamesDoesNothing() {
    }

    @And("James attempts to purchase sale item at counter using {string} payment method")
    public void jamesAttemptsToPurchaseSaleItemAtCounterUsingPaymentMethod(String tenderType) {
        CounterPayment.CounterBuilder.newInstance()
                .setPaymentServiceCategory("DBC Processing Fees")
                .setPaymentServiceSubCategory("Carpark Processing Fee")
                .setSaleQuantity("1")
                .setTenderType(tenderType)
                .search(Item.SALE_ITEM)
                .makePayment()
                .build();
    }

    @When("he retrieves all NETS-POS transactions details")
    public void heRetrievesAllNETSPOSTransactionsDetails() {
        CounterPayment.CounterBuilder.newInstance()
                .setTenderSource(PaymentMode.COUNTER)
                .setTenderType(TenderType.NETS_POS)
//                .mockSettlementFile()
//                .openNewDCToBalanceExistingDC()
//                .runBatch()
//                .getExpectedBankInDate()
                .uploadBankStatement(Nets_pos.amountMap)
//                .bankReconciliation()
                .build();
    }

    @When("he retrieves credit card transactions details")
    public void heRetrievesCreditCardTransactionsDetails() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
        CounterPayment.CounterBuilder.newInstance()
                .setTenderSource(PaymentMode.COUNTER)
                .setTenderType(TenderType.CREDIT_CARD)
                .mockCCSettlementFile()
                .openNewDCToBalanceExistingDC()
                .runBatch()
                .getExpectedBankInDate()
                .uploadBankStatement()
                .bankReconciliation()
                .build();
    }

    @When("he retrieves all QR code transactions details")
    public void heRetrievesAllQrCodeTransactionsDetails() {
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
        CounterPayment.CounterBuilder.newInstance()
                .setTenderSource(PaymentMode.COUNTER)
                .setTenderType(TenderType.QR_CODE)
                .openNewDCToBalanceExistingDC()
                .getExpectedBankInDate()
                .uploadBankStatement(Nets_pos.getData(PaymentMode.COUNTER))
                .bankReconciliation()
                .build();
    }
    @When("James attempts to purchase sale item at counter using nets payment method")
    public static void james_attempts_to_purchase_sale_item_at_counter_using_nets_payment_method(DataTable dataTable) {

        List<Map<String, String>> list = dataTable.asMaps();
        result = new HashMap<>();
        for(Map<String,String> entry : list){
            CounterPayment.CounterBuilder.newInstance()
                    .setTenderType(entry.get("tender type"))
                    .setPaymentServiceCategory(entry.get("payment service category"))
                    .setPaymentServiceSubCategory(entry.get("payment service sub-category"))
                    .setSaleQuantity(entry.get("quantity"))
                    .search(Item.SALE_ITEM)
                    .makePayment()
                    .build();
        }
        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
    }
    @When("James attempts to purchase notice at counter using nets payment method")
    public void james_attempts_to_purchase_notice_at_counter_using_nets_payment_method(DataTable dataTable) {
        PropertiesUtil.setProperties("sale.item.type",Item.NOTICE);
        List<Map<String, String>> list = dataTable.asMaps();
        result = new HashMap<>();
        for(Map<String,String> tenderType : list) {
            list.stream().forEach(map -> {
                result.putAll(map.entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey(), entry -> (String) entry.getValue())));
            });
            mockNoticeFile(BatchFile.EVMS_NOTICE_FILE, result);
            String source = getXmlFile(BatchFile.EVMS_NOTICE_FILE);
            String destination = environmentVariables.getProperty("CCRS.EVMS.XML.server.path");
            ServerUtil.uploadFile(source, destination, true, "xml");
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.LOAD_NEW_VIOLATION, false));

            //Make payment
            CounterPayment.CounterBuilder.newInstance()
                    .setTenderType(tenderType.get("tender type"))
                    .setTenderSource(TenderSource.COUNTER)
                    .search(Item.NOTICE)
                    .makePayment()
                    .build();
            theActorInTheSpotlight().attemptsTo(CCB.logout());
            theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));
        }
    }

    @When("he processes Nets-POS refund payment method for outstanding invoice")
    public void he_processes_nets_pos_refund_payment_method_for_outstanding_invoice(DataTable dataTable) throws FileNotFoundException {

        List<Map<String, String>> dataList = dataTable.asMaps();
        for(Map<String,String> entry : dataList) {

            String tenderType = entry.get("tender type");
            switch (tenderType){
                case TenderType.CASH_CARD:
                case TenderType.NETS:
                case TenderType.FLASH_PAY:
                case TenderType.QR_CODE:
                    if(entry.get("refund method").equalsIgnoreCase(RefundMode.GIRO)){
                        GIRORefundMethod.GIRORefundBuilder.newInstance()
                                .setBusinessSystem(entry.get("business system"))
                                .setRefundType("Partial")
                                .setBank(Payment.BANK)
                                .setAccount(Payment.ACCOUNT)
                                .setRefundMode(RefundMode.GIRO)
                                .initiate(entry.get("refund method"),entry.get("refund reason"),"counter")
                                .coApproval()
                                .aoApproval()
                                .createVendorFile()
                                .moveCaseToSapChannel()
                                .clearance()
                                .build();
                    }else if(entry.get("refund method").equalsIgnoreCase(RefundMode.CHEQUE)){
                        GIRORefundMethod.GIRORefundBuilder.newInstance()
                                .setBusinessSystem(entry.get("business system"))
                                .setRefundType("Partial")
                                .setRefundMode(RefundMode.CHEQUE)
                                .initiate(entry.get("refund method"),entry.get("refund reason"),"counter")
                                .coApproval()
                                .aoApproval()
                                .createVendorFile()
                                .moveCaseToSapChannel()
                                .clearance()
                                .build();
                    }
                    break;
            }
        }
    }

    @Then("he processes refund using different payment method for outstanding invoice")
    public void he_processes_refund_using_different_payment_method_for_outstanding_invoice(DataTable dataTable) throws FileNotFoundException {

        List<Map<String, String>> dataList = dataTable.asMaps();
        for(Map<String,String> entry : dataList) {

            String refundMethod = entry.get("refund method");
            switch (refundMethod){
                case RefundMode.GIRO:
                        GIRORefundMethod.GIRORefundBuilder.newInstance()
                                .setBusinessSystem(entry.get("business system"))
                                .setRefundType("Partial")
                                .setBank(Payment.BANK)
                                .setAccount(Payment.ACCOUNT)
                                .setRefundMode(RefundMode.GIRO)
                                .initiate(entry.get("refund method"),entry.get("refund reason"),entry.get("application"))
                                .coApproval()
                                .aoApproval()
                                .createVendorFile()
                                .moveCaseToSapChannel()
                                .clearance()
                                .build();
                        break;
                case RefundMode.CHEQUE:
                        GIRORefundMethod.GIRORefundBuilder.newInstance()
                                .setBusinessSystem(entry.get("business system"))
                                .setRefundType("Partial")
                                .setRefundMode(RefundMode.CHEQUE)
                                .initiate(entry.get("refund method"),entry.get("refund reason"),entry.get("application"))
                                .coApproval()
                                .aoApproval()
                                .createVendorFile()
                                .moveCaseToSapChannel()
                                .clearance()
                                .build();
                        break;
                case RefundMode.CREDIT_CARD:
                    GIRORefundMethod.GIRORefundBuilder.newInstance()
                            .setBusinessSystem(entry.get("business system"))
                            .setRefundType("Partial")
                            .setRefundMode(RefundMode.CREDIT_CARD)
                            .initiate(entry.get("refund method"),entry.get("refund reason"),entry.get("application"))
                            .coApproval()
                            .aoApproval()
                            .doApproval()
                            .build();
                    PortalPayment.PortalBuilder.newInstance()
                            .setTenderType(PropertiesUtil.getProperties("tender.type"))
                            .setTenderSource(PropertiesUtil.getProperties("tender.source"))
                            .mockSettlementFile()
                            .build();
                    break;
                case RefundMode.PAYPAL:
                    GIRORefundMethod.GIRORefundBuilder.newInstance()
                            .setBusinessSystem(entry.get("business system"))
                            .setRefundType("Partial")
                            .setRefundMode(RefundMode.PAYPAL)
                            .initiate(entry.get("refund method"),entry.get("refund reason"),entry.get("application"))
                            .coApproval()
                            .aoApproval()
                            .doApproval()
                            .moveCaseToSapChannel()
                            .clearance()
                            .build();
                    break;
            }
        }
    }
}
