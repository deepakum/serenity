package stepdefinitions;

import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchFactory;
import Utility.CCRS.Item;
import Utility.CCRS.SaleItemCategories;
import Utility.Payment.CounterPayment;
import Utility.Payment.PortalPayment;
import Utility.Payment.TenderSource;
import Utility.Refund.BusinessSystem;
import Utility.Refund.ChequeRefund;
import Utility.Refund.GIRORefundMethod;
import Utility.Refund.Reason;
import Utility.Server.ServerUtil;
import Utility.others.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.CCB;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static Utility.SettlementFile.MockXml.getXmlFile;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class PortalStepDefinition {

    static Logger logger = LoggerFactory.getLogger(PortalStepDefinition.class);
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    Actor james;
    String tenderSource;
    @Before()
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
        james = OnStage.theActor("James");
    }
    @Given("James opens deposit control for {string} tender source and {string} tender type")
    public void jamesOpensDepositControlForTenderSourceAndTenderType(String tenderSource, String tenderType) {
        BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL)
                .create(tenderSource,tenderType);
    }
    @When("James attempts to clear fines using {string} payment method")
    public void james_attempts_to_clear_fines_using_payment_method(String paymentMode) {
        PortalPayment.PortalBuilder.newInstance()
                .setTenderSource(paymentMode)
                .search(Item.NOTICE)
                .makePayment()
                .mockSettlementFile()
                .openNewDCToBalanceExistingDC()
                .runSettlementBatch()
                .getExpectedBankInDate()
                .uploadBankStatement()
                .bankReconciliation()
                .build();

    }

    @Given("James attempts to purchase an {string} at 1M portal using {string}, {string} tender source")
    public void james_attempts_to_purchase_an_at_1m_portal_using_tender_source(String item, String tenderType, String tenderSource) {

        logger.info(()->"james_attempts_to_purchase_an_at_1m_portal_using_tender_source");
        PortalPayment.PortalBuilder.newInstance()
                .setTenderType(tenderType)
                .setTenderSource(tenderSource)
                .setProduct(item)
                .search(item)
                .makePayment()
                .mockSettlementFile()
                .openNewDCToBalanceExistingDC()
                .runSettlementBatch()
                .getExpectedBankInDate()
                .uploadBankStatement()
                .bankReconciliation()
                .build();
    }

    @Given("he processes credit card refund for outstanding invoice")
    public void he_processes_credit_card_refund_for_outstanding_invoice() {
        PortalPayment.PortalBuilder.newInstance()
                .search(PropertiesUtil.getProperties("sale.item.type"))
                .makePayment()
                .mockSettlementFile()
                .openNewDCToBalanceExistingDC()
                .runSettlementBatch()
                .build();
    }
    @Given("James attempts to purchase an item using braintree service provider")
    public void james_attempts_to_purchase_an_item_using_braintree_service_provider(DataTable dataTable) {

        List<Map<String, String>> list = dataTable.asMaps();
        for(Map<String,String> entry : list) {
            PortalPayment.PortalBuilder.newInstance()
                    .setTenderType(entry.get("tender type"))
                    .setTenderSource(entry.get("tender source"))
                    .setProduct(entry.get("item"))
                    .search(entry.get("item"))
                    .makePayment()
                    .mockSettlementFile()
                    .openNewDCToBalanceExistingDC()
                    .runSettlementBatch()
                    .getExpectedBankInDate()
                    .uploadBankStatement()
                    .bankReconciliation()
                    .build();
        }
    }

    @When("he Processes refund using {string} payment method for outstanding invoice")
    public void he_processes_refund_using_payment_method_for_outstanding_invoice(String refundMode) {

        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login("Admin"));
        ChequeRefund.ChequeRefundBuilder.newInstance()
                .setRefundMode(refundMode)
                .setBusinessSystem(BusinessSystem.SALE_ITEM)
                .transactionDetails().build();
    }

    @Given("he processes refund using {string} payment method for {string} purchase")
    public void he_processes_refund_using_payment_method_for_purchase(String refundMode, String item) throws FileNotFoundException {

        GIRORefundMethod.GIRORefundBuilder.newInstance()
                .setBusinessSystem(getBusinessType(item))
                .setRefundType("Partial")
                .setRefundMode(refundMode)
                .setApplication("portal")
                .initiate(refundMode, Reason.DISPUTE,"portal")
                .coApproval()
                .aoApproval()
                .doApproval()
                .createVendorFile()
                .moveCaseToSapChannel()
                .clearance()
                .build();
    }
    @When("James is at ccrs {string} portal page")
    public void jamesIsAtCcrsPortalPage(String category) {

        if(category.equalsIgnoreCase(SaleItemCategories.PP)){
            category = "PP";
        }else if(category.equalsIgnoreCase(SaleItemCategories.TD)){
            category = "TD";
        }else if(category.equalsIgnoreCase(SaleItemCategories.DBCF)){
            category = "DBCF";
        }else if(category.equalsIgnoreCase(SaleItemCategories.HVP)){
            category = "HVP";
        }else if(category.equalsIgnoreCase(SaleItemCategories.MS)){
            category = "MS";
        }else if(category.equalsIgnoreCase(SaleItemCategories.SE)) {
            category = "TD";
        }

        String url = "portal."+category;
        theActorInTheSpotlight().attemptsTo(Open.browserOn().thePageNamed(url));
    }
    private String getBusinessType(String item){
        String businessSystem = null;
        if(item.equalsIgnoreCase(Item.SALE_ITEM)){
            businessSystem = BusinessSystem.SALE_ITEM;
        }if(item.equalsIgnoreCase(Item.NOTICE)){
            businessSystem = BusinessSystem.VIOLATION;
        }
        return businessSystem;
    }

    @When("James attempts to purchase sale item at counter using {string} by choosing {string}, {string} and {string} item")
    public void james_attempts_to_purchase_sale_item_at_counter_using_by_choosing_and_item(String tenderType, String psc, String pssc, String quantity) {
        CounterPayment.CounterBuilder.newInstance()
                .setTenderType(tenderType)
                .setPaymentServiceSubCategory(pssc)
                .setPaymentServiceCategory(psc)
                .setSaleQuantity(quantity)
                .setTenderSource(TenderSource.COUNTER)
                .setProduct(Item.SALE_ITEM)
                .search(Item.SALE_ITEM)
                .makePayment()
                .settlePayment()
                .build();
    }
    @When("he attempts to process refund by {string} on {string} purchased on {string}")
    public void he_attempts_to_process_refund_by_on_purchased_on(String refundMode, String businessSystem, String application) throws FileNotFoundException {
        GIRORefundMethod.GIRORefundBuilder.newInstance()
                .setBusinessSystem(businessSystem)
                .setRefundType("Partial")
                .setRefundMode(refundMode)
                .setApplication(application)
                .initiate(refundMode, Reason.DISPUTE,application)
                .coApproval()
                .aoApproval()
                .doApproval()
                .createVendorFile()
                .moveCaseToSapChannel()
                .clearance()
                .build();
    }

    public static void main(String[] args) {
        String source = getXmlFile(BatchFile.BRAINTREE_SETTLEMENT);
        String destination = environmentVariables.getProperty("CCRS.EVMS.BT.CSV.server.path");
        ServerUtil.uploadFile(source,destination,true,".csv");
    }
}
