package stepdefinitions;

import Utility.ABT.ABTFile;
import Utility.BatchJob.BatchControl;
import Utility.CCRS.CCBFrames;
import Utility.DB.DB_Utils;
import Utility.Server.ServerUtil;
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
import org.junit.Assert;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.Batch.Batch;
import tasks.CCB;
import tasks.LaunchMenu;
import tasks.Navigate;
import tasks.SwitchToWindow;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static Utility.ABT.wosfGlVerification.getWriteOffGlFileData;
import static Utility.Common.StringUtil.extractDigit;
import static Utility.Server.ServerUtil.*;
import static Utility.SettlementFile.ABT.*;
import static Utility.SettlementFile.MockXml.delSourceFile;
import static Utility.SettlementFile.MockXml.templatePath;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.*;

public class AbtWosfStepDefinition {

    Logger logger = LoggerFactory.getLogger(AbtWosfStepDefinition.class);
    public static Map<String, String> masterConfig = new HashMap<>();
    public static Map<String, String> dbMap = new HashMap<>();
    public static Map<String, String> glMap = new HashMap<>();
    String wosfFile = ABTFile.MOCKED_FILE_PATH + File.separator + ABTFile.WOSF_FILE + getTransactionDate() + "." + ABTFile.FILE_TYPE;

    @When("James attempts to process write off summary file")
    public void jamesAttemptsToProcessWriteOffSummaryFile(DataTable dataTable) throws Exception {
        List<List<String>> rows = dataTable.asLists(String.class);
        List<String> list = new ArrayList<>();
        for (int row = 1; row < rows.size(); row++) {
            list.add(rows.get(row).stream().collect(Collectors.joining(",")));
        }
        mockABTFile(ABTFile.WOSF_FILE);
        uploadFile(getAbtFileName(ABTFile.CSF_FILE), geBatchworkingFolderPath(BatchControl.WOSF));
        uploadFile(getAbtFileName(ABTFile.WOSF_FILE), geBatchworkingFolderPath(BatchControl.WOSF));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.WOSF, true));
    }

    @And("he verifies adjustment entry")
    public void heVerifiesAdjustmentEntry() {
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
                if (adjType.contains("Write Off")) {
                    adjustmentMap.put(adjType, amount);
                    glMap.put(adjType.trim().split("-")[0], extractDigit(amount));
                } else {
                    gstList.add(amount);
                }
            }
        }
        System.out.println("adjustmentMap : "+adjustmentMap);
        Map<String, Map<String, String>> writeOffGstMap = DB_Utils.getWriteoffAndGstAmount(getAbtFileName(ABTFile.WOSF_FILE));
        List<String> gstUiList = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : writeOffGstMap.entrySet()) {
            dbMap.put(masterConfig.get(entry.getKey()), "S$" + entry.getValue().get("Amount"));
            if (!entry.getValue().get("GST").equalsIgnoreCase("0"))
                gstUiList.add("S$" + entry.getValue().get("GST"));
        }

        System.out.println("dbMap : "+dbMap);
        theActorInTheSpotlight().attemptsTo(Ensure.that(gstUiList).isASubsetOf(gstList));
        //theActorInTheSpotlight().attemptsTo(Ensure.that(areEqual(adjustmentMap, dbMap)).isTrue());
        theActorInTheSpotlight().attemptsTo(Switch.toTheOtherWindow());
    }

    private void updateGLMap(Map<String, Map<String, String>> writeOffGstMap) {
        for (Map.Entry<String, Map<String, String>> entry : writeOffGstMap.entrySet()) {
            glMap.put(masterConfig.get(entry.getValue()).split("-")[0], entry.getValue().get("Amount"));
        }
    }

    @And("he collects master configuration data")
    public void heCollectsMasterConfigurationData() {
        theActorInTheSpotlight().attemptsTo(LaunchMenu.to("Master Configuration"));
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                , containsString("Master Configuration")));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("tabPage"));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_master_config_pageobject.ABT_BROADCAST_IMG));
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("zoneMapFrame_2"));
        int entries = CCB.getSize(ccb_master_config_pageobject.MASTER_CONFIG_ROW);
        for (int row = 1; row < entries; row++) {
            String serviceProviderID = CCB.getText(ccb_master_config_pageobject.SERVICE_PROVIDER_ID(row));
            String packageID = CCB.getText(ccb_master_config_pageobject.PACKAGE_ID(row));
            String writeOffAdjustmentType = CCB.getText(ccb_master_config_pageobject.WRITE_OFF_ADJ_TYPE(row));
            masterConfig.put(serviceProviderID + "_" + packageID, writeOffAdjustmentType);
        }
        Map<String, String> AdjDescMap = DB_Utils.getAdjustmentDescription();
        for (Map.Entry<String, String> entrySet : masterConfig.entrySet()) {
            masterConfig.put(entrySet.getKey(), AdjDescMap.get(entrySet.getValue()));
        }
        Helper.switchToFrames(CCBFrames.MAIN);
    }

    private boolean areEqual(Map<String, String> first, Map<String, String> second) {
        if (first.size() != second.size()) {
            return false;
        }
        return first.entrySet().stream()
                .allMatch(e -> e.getValue().equals(second.get(e.getKey())));
    }

    public static void getGoToFtType() {
        int entries = CCB.getSize(ccb_adjustment_search_pageobject.ADJUSTMENT_ENTRY) - 1;
        for (int row = 0; row < entries; row++) {

        }
    }

    @And("he runs all WOSF related batch job")
    public void heRunsAllWOSFRelatedBatchJob() {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.GL_ASSIGN, true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CREATE_GL_DOWNLOAD, true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.GENERATE_GL_EXTRACT, true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.EXTRACT_GL_FOR_SAP, true));
    }

    @Then("he will able to make adjustment created")
    public void heWillAbleToMakeAdjustmentCreated() {
        String serverPath = "/app/CCBPRSIT/ouaf/SFTP/CM-ABTGL/working/";
        delSourceFile(ABTFile.MOCKED_FILE_PATH);
        ServerUtil.downloadFile(serverPath,ABTFile.MOCKED_FILE_PATH);
        List<Map<String, String>> wosfListMap = getWriteOffGlFileData();
        for (Map.Entry entry : glMap.entrySet()) {
            boolean recordFound = false;
            for (Map<String, String> map : wosfListMap) {
                if (map.get("LineItemText").contains(entry.getKey().toString())) {
                    recordFound = true;
                    Assert.assertTrue(map.get("Amount").equalsIgnoreCase(entry.getValue().toString()));
                    Assert.assertTrue(map.get("GLAccount").equalsIgnoreCase("749002"));
                }
            }
            if(!recordFound)
                throw new RuntimeException("record not found in GL file");
        }
        delSourceFile(ABTFile.MOCKED_FILE_PATH);
    }
}
