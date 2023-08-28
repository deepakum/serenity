package stepdefinitions;

import Utility.ABT.ABTFile;
import Utility.BatchJob.BatchControl;
import Utility.DB.DB_Utils;
import Utility.others.AdminMenu;
import Utility.others.CustomerInformationMenu;
import Utility.others.Helper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.SendKeys;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.ccb_adjustment_search_pageobject;
import pageobjects.ccb_person_pageobject;
import pageobjects.ccb_person_search_pageobject;
import pageobjects.ccb_user_pageobject;
import tasks.Batch.Batch;
import tasks.CCB;
import tasks.Navigate;
import tasks.SwitchToWindow;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static Utility.Common.StringUtil.extractDigit;
import static Utility.Server.ServerUtil.geBatchworkingFolderPath;
import static Utility.Server.ServerUtil.uploadFile;
import static Utility.SettlementFile.ABT.*;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class AbtRsfStepDefinition {

    public static Map<String, String> masterConfig = new HashMap<>();
    public static Map<String, String> dbMap = new HashMap<>();
    public static Map<String, String> glMap = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(AbtRsfStepDefinition.class);

    @When("James attempts to process revenue summary file by running {string} batch")
    public void jamesAttemptsToProcessRevenueSummaryFileByRunningBatch(String batch) throws Exception {
        mockABTFile(ABTFile.RSF_FILE);
        String serverPath = ABTFile.SFTP_BASE_PATH+batch+ABTFile.SFTP_DIRECTORY;
        uploadFile(ABTFile.MOCKED_FILE_PATH+ File.separator+ ABTFile.CSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        uploadFile(ABTFile.MOCKED_FILE_PATH+File.separator+ ABTFile.RSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(batch,false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }

    @When("James attempts to process revenue summary file")
    public void jamesAttemptsToProcessRevenueSummaryFile(DataTable dataTable) throws Exception {
        List<List<String>> rows = dataTable.asLists(String.class);
        List<String> list = new ArrayList<>();
        for(int row = 1 ; row<rows.size();row++){
            list.add(rows.get(row).stream().collect(Collectors.joining(",")));
        }
        mockABTFile(ABTFile.RSF_FILE);
        uploadFile(getAbtFileName(ABTFile.CSF_FILE), geBatchworkingFolderPath(BatchControl.RSF));
        uploadFile(getAbtFileName(ABTFile.RSF_FILE), geBatchworkingFolderPath(BatchControl.RSF));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.RSF, true));
    }
    @When("he process the records to create journal entries to create SAP GL Posting File using {string} batch")
    public void he_process_the_records_to_create_journal_entries_to_create_sap_gl_posting_file_using_batch(String string) {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.GL_ASSIGN,true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CREATE_GL_DOWNLOAD,true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.GENERATE_GL_EXTRACT,true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.EXTRACT_GL_FOR_SAP,true));
    }
    @Then("he will able to send sap gl positing file to SAP")
    public void he_will_able_to_send_sap_gl_positing_file_to_sap() {

    }

    @And("he verifies revenue entry")
    public void heVerifiesRevenuementEntry() {
        Helper.switchToFrames("main");
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Customer_Information.name(), CustomerInformationMenu.PERSON, "Search"));
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(SendKeys.of("%transit%").into(ccb_person_search_pageobject.PERSON_NAME_INPUTBOX));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_person_search_pageobject.PERSON_NAME_SEACH_BUTTON));
        Helper.switchToFrames("dataframe");

        ccb_person_search_pageobject.PERSON_NAME.resolveAllFor(theActorInTheSpotlight())
                .stream().forEach(menu -> menu.getText());

        theActorInTheSpotlight().attemptsTo(Click.on(
                ccb_person_search_pageobject.PERSON_NAME.resolveAllFor(theActorInTheSpotlight())
                        .stream().filter(ele -> ele.getText().equalsIgnoreCase("Transit Link"))
                        .findFirst().get())
        );
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("dashboard"));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_person_pageobject.ACCOUNT_CONTEXT_IMG));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_person_pageobject.GO_TO_ACCOUNT_PAYMENT_HISTORY_MENU.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        theActorInTheSpotlight().should(eventually(
                seeThat(WebElementQuestion.the(ccb_user_pageobject.PAGE_TITLE), isVisible())));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage"));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("dataframe"));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_person_pageobject.GO_TO_FT_TYPE_IMAGE));
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("dataframe"));

        int entries = CCB.getSize(ccb_adjustment_search_pageobject.ADJUSTMENT_ENTRY) - 1;
        Map<String, String> adjustmentMap = new HashMap<>();
        List<String> gstList = new ArrayList<>();
        for (int row = 0; row < 30; row++) {
            String createDate = CCB.getText(ccb_adjustment_search_pageobject.CREATION_DATE(row))
                    .replace("‑", "-");
            String personName = CCB.getText(ccb_adjustment_search_pageobject.PERSON_NAME(row));
            if (createDate.equalsIgnoreCase(getCreationDate()) && personName.equalsIgnoreCase("Transit Link")) {
                String adjType = CCB.getText(ccb_adjustment_search_pageobject.ADJUSTMENT_TYPE(row));
                String amount = CCB.getText(ccb_adjustment_search_pageobject.AMOUNT(row));
                if (adjType.contains("Concession Pass")) {
                    String[] temp = adjType.split("-");
                    temp[2] = "CMPT201CP";
                    adjustmentMap.put(Arrays.stream(temp).collect(Collectors.joining(",")), amount);
                    glMap.put(adjType.trim().split("-")[0], extractDigit(amount));
                }else if (adjType.contains("Patron Claim Refund")) {
                    String[] temp = adjType.split("-");
                    temp[2] = "CMPT201PCR";
                    adjustmentMap.put(Arrays.stream(temp).collect(Collectors.joining(",")), amount);
                    glMap.put(adjType.trim().split("-")[0], extractDigit(amount));
                }else if (adjType.contains("Manual Adjustment")) {
                    String[] temp = adjType.split("-");
                    temp[2] = "CMPT201MA";
                    adjustmentMap.put(Arrays.stream(temp).collect(Collectors.joining(",")), amount);
                    glMap.put(adjType.trim().split("-")[0], extractDigit(amount));
                }else if (adjType.contains("Fare Revenue")) {
                    String[] temp = adjType.split("-");
                    temp[2] = "CMPT201FR";
                    adjustmentMap.put(Arrays.stream(temp).collect(Collectors.joining(",")), amount);
                    glMap.put(adjType.trim().split("-")[0], extractDigit(amount));
                }else if (adjType.contains("Fare Harmonisation")) {
                    String[] temp = adjType.split("-");
                    temp[2] = "CMPT201FH";
                    adjustmentMap.put(Arrays.stream(temp).collect(Collectors.joining(",")), amount);
                    glMap.put(adjType.trim().split("-")[0], extractDigit(amount));
                }else if (adjType.contains("Differential Fare")) {
                    String[] temp = adjType.split("-");
                    temp[2] = "CMPT201DF";
                    adjustmentMap.put(Arrays.stream(temp).collect(Collectors.joining(",")), amount);
                    glMap.put(adjType.trim().split("-")[0], extractDigit(amount));
                } else {
                    gstList.add(amount);
                }
            }
        }
        System.out.println("adjustmentMap : "+adjustmentMap);
//        Map<String, Map<String, String>> writeOffGstMap = DB_Utils.getRsfGstAmount(getAbtFileName(ABTFile.WOSF_FILE));
//        List<String> gstUiList = new ArrayList<>();
//        for (Map.Entry<String, Map<String, String>> entry : writeOffGstMap.entrySet()) {
//            dbMap.put(masterConfig.get(entry.getKey()), "S$" + entry.getValue().get("Amount"));
//            if (!entry.getValue().get("GST").equalsIgnoreCase("0"))
//                gstUiList.add("S$" + entry.getValue().get("GST"));
//        }
//
//        System.out.println("dbMap : "+dbMap);
//        theActorInTheSpotlight().attemptsTo(Ensure.that(gstUiList).isASubsetOf(gstList));
        //theActorInTheSpotlight().attemptsTo(Ensure.that(areEqual(adjustmentMap, dbMap)).isTrue());
        theActorInTheSpotlight().attemptsTo(Switch.toTheOtherWindow());
    }

    private void updateGLMap(Map<String, Map<String, String>> writeOffGstMap) {
        for (Map.Entry<String, Map<String, String>> entry : writeOffGstMap.entrySet()) {
            glMap.put(masterConfig.get(entry.getValue()).split("-")[0], entry.getValue().get("Amount"));
        }
    }
}
