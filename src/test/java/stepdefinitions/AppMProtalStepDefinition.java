package stepdefinitions;

import Utility.BatchJob.CM_cdtpp;
import Utility.Notice.IdentificationNo;
import Utility.Notice.IdentificationType;
import Utility.Notice.Notice;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.actors.OnStage;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import tasks.*;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.core.Is.is;

public class AppMProtalStepDefinition {

    @Steps
    NoticeStepDefinition noticeStepDefinition;
    @Steps
    CCRSLoginStepdefinition ccrsLoginStepdefinition;
    @Steps
    DepositControlStepDefinition dcStepDefinition;
    @Steps
    BatchJobSubmissionStepDefinition batchJobSubmissionStepDefinition;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    Actor james = Actor.named("James");
    String fineUrl;

//    @Before()
//    public void setTheStage() {
//        OnStage.setTheStage(new OnlineCast());
//    }
//    @Before
//    public void launchCCB(){
//        ccrsLoginStepdefinition.james_is_at_the_ccrs_login_page("james");
//        ccrsLoginStepdefinition.james_provides_credential_as("Admin");
//        ccrsLoginStepdefinition.he_is_able_to_access_to_ccrs_application();
//    }

//    @DataTableType(replaceWithEmptyString = "[anonymous]")
//    public CM_cdtpp getDCInfo(DataTable dataTable){
//        System.out.println(dataTable.asMaps());
//        List<Map<String, String>> list = dataTable.asMaps();
//        Map<String, String> result = new HashMap<>();
//        list.stream().forEach(map -> {
//            result.putAll(map.entrySet().stream()
//                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> (String) entry.getValue()+"")));
//        });
//        return new CM_cdtpp(
//                result.get("tender"),
//                result.get("psp"),
//                result.get("location"),
//                result.get("cdc"),
//                result.get("counterFlag"),
//                result.get("erpFlag")
//        );
//    }
    @Given("James receives fine notice")
    public void james_receives_fine_notice(DataTable dataTable) {
        james = OnStage.theActor("James");
        //noticeStepDefinition.james_creates_notice_using(dataTable);
        Notice.createNotice();
    }

    @Given("James opens deposit control for portal payment")
    public void James_opens_deposit_control_for_portal_payment(DataTable dataTable) throws Exception {
        //CM_cdtpp cm_cdtpp = getDCInfo(dataTable);
        new CM_cdtpp().create("stripe", "tenderType");
    }
    @Given("James is at ccrs portal page")
    public void james_is_at_ccrs_portal_page() {
        theActorInTheSpotlight().attemptsTo(Open.browserOn().thePageNamed("EVMS.fines.url"));
    }

    @When("James attempts to search fines")
    public void james_attempts_to_search_fines() {
        theActorInTheSpotlight().attemptsTo(OneMPortal.searchNotice(IdentificationType.SG_NRIC, IdentificationNo.NRIC));
    }

    @When("he attempts to make payment using {string} mode of payment")
    public void he_selects_mode_of_payment_using_mode_of_payment(String modeOfPayment) {
//        theActorInTheSpotlight().attemptsTo(OneMPortal.makePayment(modeOfPayment));
//        Helper.customWait(10000);
//        String referenceIDText = FineAndFeesPageObject.REFERENCE_ID_TEXT.resolveFor(theActorInTheSpotlight()).getAttribute("innerText");
//        PropertiesUtil.setProperties("notice.referenceID", OneMPortal.extractReferenceIDFromText(referenceIDText));
        theActorInTheSpotlight().attemptsTo(MakePayment.using(modeOfPayment));
    }

    @When("he upload csv file and bank statement to CCRS portal")
    public void he_upload_csv_file_and_bank_statement_to_CCRS_portal() {

        //launchCCB();

        /* Following attributes to be collected for CSV mocking using payment event ID
            1. Merchant reference ID
            2. Merchant transaction id
            3. Create date and time
            4. settlement date
            5. Deposit control ID
         */
//
//        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.PAYMENT_EVENT, SubSubMenu.SEARCH.name()));
//        theActorInTheSpotlight().attemptsTo(DC.searchPaymentEventByAccountID());
//        List<WebElementFacade> listOfTC = DepositControlSearchPageObject.SEARCH_RESULT("Payment Event ID").resolveAllFor(theActorInTheSpotlight());
//        theActorInTheSpotlight().attemptsTo(Click.on(listOfTC.stream()
//                .filter(ele -> ele.getText().trim().equals(PropertiesUtil.getProperties("notice.referenceID")))
//                .collect(Collectors.toList()).get(0)));
//        Helper.switchAtWindow();
//        Helper.switchToFrames("main","tabMenu");
//        theActorInTheSpotlight().attemptsTo(Click.on(DcCharacteristicsPageObject.CHARACTERISTICS_MENU));
//        Helper.switchToFrames("main","tabPage","PEVT_CHAR");
//        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(DcCharacteristicsPageObject.CHARACTERISTICS_TYPE_COL)
//                ,is("Characteristic Type")));
//        int rows = DcCharacteristicsPageObject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
//        String merchantTransactionReferenceID = null;
//        for (int k = 0; k < rows; k++) {
//            String charType = DcCharacteristicsPageObject.PAYMENT_CHAR_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
//            if (charType.equalsIgnoreCase("Merchant Transaction Reference ID")) {
//                merchantTransactionReferenceID = DcCharacteristicsPageObject.CHAR_MERCHANT_REFERENCE_ID_TEXTBOX(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
//            }
//        }
//
//        Helper.switchToFrames("main","tabMenu");
//        theActorInTheSpotlight().attemptsTo(Click.on(PaymentEventPageObject.TENDERS_MENU_TAB));
//        Helper.switchToFrames("main","tabPage");
//        String paymentIntentID = PaymentEventPageObject.REFERENCE_ID_TEXTBOX.resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
//
//        theActorInTheSpotlight().attemptsTo(Click.on(PaymentEventPageObject.TENDER_CONTROL_CONTEXT_MENU));
//        Helper.switchToFrames("main");
//        Actions action = new Actions(getDriver());
//        action.moveToElement(PaymentEventPageObject.GO_TO_TC_CONTROL_MENU.resolveFor(theActorInTheSpotlight())).build().perform();
//        action.moveToElement(PaymentEventPageObject.TC_SEARCH_MENU.resolveFor(theActorInTheSpotlight())).click().perform();
//        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(CCRSHomePageObject.PAGE_TITLE)
//                ,is("Tender Control")));
//        Helper.switchToFrames("tabMenu");
//        theActorInTheSpotlight().attemptsTo(Click.on(TC_PageObject.TENDERS_MENU_LABEL));
//        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
//        theActorInTheSpotlight().attemptsTo(Click.on(TC_PageObject.GET_MORE_BUTTON));
//        Helper.switchAtWindow();
//        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
//        Helper.customWait(2000);
//        String created = DepositControlSearchPageObject.SEARCH_RESULT_WITH_SPECIAL_CHAR("Create Date/Time").resolveFor(theActorInTheSpotlight()).getAttribute("innerText").trim();
//
//        Helper.switchToFrames("main","tabMenu");
//        theActorInTheSpotlight().attemptsTo(Click.on(DC_PageObject.DC_MAIN_MENU_TAB));
//        Helper.switchToFrames("main","tabPage");
//        theActorInTheSpotlight().attemptsTo(Click.on(DC_PageObject.DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON));
//        Helper.switchToFrames("main");
//        action.moveToElement(DC_PageObject.GO_TO_DC.resolveFor(theActorInTheSpotlight())).click().perform();
//        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(CCRSHomePageObject.PAGE_TITLE)
//                ,is("Deposit Control")));
//        Helper.switchToFrames("main","tabPage");
//        String DC = DC_PageObject.DC_NUMBER_TEXTBOX.resolveFor(theActorInTheSpotlight()).getAttribute("value");
//        PropertiesUtil.setProperties("recon.depositControlID",DC);
//        String totalTenderAmount = DC_PageObject.TOTAL_TENDER_AMOUNT_TEXTBOX.resolveFor(theActorInTheSpotlight()).getText();
//        PropertiesUtil.setProperties("recon.endingBalanceAmount",totalTenderAmount);
//
//        Helper.switchToFrames("main","tabMenu");
//        theActorInTheSpotlight().attemptsTo(Click.on(DcCharacteristicsPageObject.CHARACTERISTICS_MENU));
//        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
//        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(DcCharacteristicsPageObject.CHARACTERISTICS_TYPE_COL)
//                ,is("Characteristic Type")));
//        int dcCharRows = DcCharacteristicsPageObject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
//        String settlementDate = null;
//        for (int k = 0; k < dcCharRows; k++) {
//            String charType = DcCharacteristicsPageObject.CHARACTERISTIC_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
//            if (charType.equalsIgnoreCase("Settlement Date")) {
//                settlementDate = DcCharacteristicsPageObject.CHAR_SETTLEMENT_DATE_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
//                PropertiesUtil.setProperties("recon.expectedBankingDate",settlementDate);
//            }
//        }
//        CSVParser.updateCSV(created, settlementDate,paymentIntentID,merchantTransactionReferenceID,totalTenderAmount);
//
//        //Create new DC to set TC to b balancing in progress
//        BatchFactory.getInstance(BatchControl.DEPOSIT_CONTROL).create();
//
//        String source = getXmlFile(BatchFile.EVMS_NOTICE_FILE);
//        String destination = environmentVariables.getProperty("CCRS.EVMS.CSV.server.path");
//        ServerUtil.uploadFile(source,destination,true,".csv");
//        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-STRPE",false));
//
//        //Mock bank statements
//        String mockedFilePath = MockBankStatement.stripePayment(
//                PropertiesUtil.getProperties("recon.expectedBankingDate"),
//                PropertiesUtil.getProperties("recon.endingBalanceAmount"));
//
//        //upload mocked statements to the server
//        ServerUtil.uploadFile(mockedFilePath, environmentVariables.getProperty("CCRS.BankStatement.server.path"));
//        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-UPBNK", true));
//        theActorInTheSpotlight().attemptsTo(Batch.status());
//
//        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-BNKRN", true));
//
//        theActorInTheSpotlight().attemptsTo(Batch.status());
    }

    @Then("he will be to able to clear fines")
    public void he_will_be_to_able_to_clear_fines() {


    }

}
