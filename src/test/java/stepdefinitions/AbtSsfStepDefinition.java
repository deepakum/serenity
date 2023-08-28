package stepdefinitions;

import Utility.ABT.ABTFile;
import Utility.BatchJob.BatchControl;
import Utility.CCRS.CCBFrames;
import Utility.CSV.DateUtil;
import Utility.DB.DB_Utils;
import Utility.Depositontrol.DepositControl;
import Utility.Depositontrol.DepositControlStatus;
import Utility.Server.ServerUtil;
import Utility.SettlementFile.ABT;
import Utility.SettlementFile.MockBankStatement;
import Utility.others.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.*;
import tasks.*;
import tasks.Batch.Batch;
import tasks.Batch.DuplicateJob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static Utility.Common.StringUtil.cleanTextContent;
import static Utility.Server.ServerUtil.*;
import static Utility.Server.ServerUtil.geBatchworkingFolderPath;
import static Utility.SettlementFile.ABT.*;
import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class AbtSsfStepDefinition {

    Logger logger = LoggerFactory.getLogger(AbtSsfStepDefinition.class);
    static List<Map<String,String>> mapForBankStatementList = new ArrayList<Map<String, String>>();
    static EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();

    @When("James attempts to process settlement summary file by running {string} batch")
    public void jamesAttemptsToProcessSettlementSummaryFileByRunningBatch(String batch) throws Exception {
        String mockedFilepath = mockABTFile(ABTFile.SSF_FILE);
        String serverPath = ABTFile.SFTP_BASE_PATH+batch+ABTFile.SFTP_DIRECTORY;
        uploadFile(ABTFile.MOCKED_FILE_PATH+ File.separator+ ABTFile.CSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        uploadFile(ABTFile.MOCKED_FILE_PATH+File.separator+ ABTFile.SSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(batch,true));
        theActorInTheSpotlight().attemptsTo(Batch.status());
        String giroTriggerDate = DB_Utils.getBusinessDateForMerchantBatch("SSF",
                new File(mockedFilepath).getName(), "CM_GIRO_TRIGGER_DT");
        PropertiesUtil.setProperties("giro.trigger.date",giroTriggerDate);
    }

    @When("James attempts to process settlement summary file")
    public void james_attempts_to_process_settlement_summary_file(DataTable dataTable) throws Exception {
        List<List<String>> rows = dataTable.asLists(String.class);
        List<String> list = new ArrayList<>();
        for(int row = 1 ; row<rows.size();row++){
            list.add(rows.get(row).stream().collect(Collectors.joining(",")));
        }
        mockABTFile(ABTFile.SSF_FILE);
        uploadFile(getAbtFileName(ABTFile.CSF_FILE), geBatchworkingFolderPath(BatchControl.SSF));
        uploadFile(getAbtFileName(ABTFile.SSF_FILE), geBatchworkingFolderPath(BatchControl.SSF));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.SSF, true));
        String giroTriggerDate = DB_Utils.getBusinessDateForMerchantBatch(BatchControl.SSF,
                ABTFile.SSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE, "CM_GIRO_TRIGGER_DT");
        PropertiesUtil.setProperties("giro.trigger.date",giroTriggerDate);
    }
    @When("he creates entries in merchant table by running {string} batch")
    public void he_creates_entries_in_merchant_table_by_running_batch(String batch) {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(batch,false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
    @When("he creates instruction file to dbs by running {string} batch")
    public void he_creates_instruction_file_to_dbs_by_running_batch(String batchName) {
        String[] str = {"ABBR","ABRR"};
        List<Object> settlementList = Arrays.asList(str);
        for(Object obj : str) {
            theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Tools.name(), ToolsMenu.BATCH_JOB_SUBMISSION, "Search"));
            theActorInTheSpotlight().attemptsTo(SearchBatchJob.of(batchName));
            theActorInTheSpotlight().attemptsTo(ABT.setSSFBatchParameters(obj.toString(), false));
            String giroTriggerDate = DateUtil.getCustomDate("yyyy-MM-dd HH:mm:ss","dd-MM-yyyy",PropertiesUtil.getProperties("giro.trigger.date"));
            Helper.switchToFrames(CCBFrames.MAIN,CCBFrames.TAB_PAGE);
            theActorInTheSpotlight().attemptsTo(Clear.field(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX)
                    .then(SendKeys.of(giroTriggerDate).into(ccb_batch_job_submission_pageobject.DATE_INPUT_BOX)
                            .thenHit(Keys.RETURN)));
            theActorInTheSpotlight().attemptsTo(DuplicateJob.to());
        }
        downloadInstructionFile(ABTFile.SFTP_BASE_PATH+batchName+"/",ABTFile.INSTRUCTION_TEMPLATE_PATH);

    }
    @When("he attempts to process ack1 file")
    public void he_attempts_to_process_ack1_file() throws Exception {
        List<File> fileList = java.util.Arrays.stream(new File(ABTFile.INSTRUCTION_TEMPLATE_PATH).listFiles()).collect(Collectors.toList());
        for(File file : fileList) {
            String line=null;
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                while ((line = br.readLine()) != null) {
                    if(line.startsWith("TRAILER")){
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            String mockedFilePath = mockACK1File(ABTFile.ACK1_FILE,line,file.getName());
            String serverPath = ABTFile.SFTP_BASE_PATH+BatchControl.ACK1+ABTFile.SFTP_DIRECTORY_UPPERCASE;
            uploadFile(mockedFilePath,serverPath);
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.ACK1,false));
            theActorInTheSpotlight().attemptsTo(Batch.status());
        }
    }
    @When("he attempts to process ack2 file")
    public void he_attempts_to_process_ack2_file() throws Exception {
        List<File> fileList = java.util.Arrays.stream(new File(ABTFile.INSTRUCTION_TEMPLATE_PATH).listFiles()).collect(Collectors.toList());
        for(File file : fileList) {
            List<String> mockedDataList = new ArrayList<>();
            Map<String,String> tempMap = new HashMap<>();
            String line=null;
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                int count = 0;
                while ((line = br.readLine()) != null) {
                    String baseNumber = "29112277T0000";
                    if(line.startsWith("PAYMENT")){
                        String[] temp = line.split(",");
                        String[] mocked = new String[17];
                        mocked[0] = "DATA";
                        mocked[1] = temp[1];
                        mocked[2] = temp[2];
                        mocked[3] = temp[3];
                        mocked[4] = temp[5];
                        mocked[5] = temp[33];
                        mocked[6] = getTransactionDate();
                        mocked[7] = temp[15];
                        mocked[8] = temp[20];
                        mocked[9] = temp[3];
                        mocked[10] = temp[27];
                        mocked[11] = "ACCP";
                        mocked[12] = "";
                        mocked[13] = temp[6];
                        tempMap.put("serviceProviderID",temp[6]);
                        mocked[14] = "";
                        mocked[15] = String.valueOf(0);
                        count += 1;
                        mocked[16] = baseNumber.concat(String.valueOf(count));
                        mockedDataList.add(java.util.Arrays.stream(mocked).collect(Collectors.joining(",")));
                    } else if (line.startsWith("TRAILER")) {
                        tempMap.put("amount",line.split(",")[2]);
                        mockedDataList.add(line);
                    }
                }
                mapForBankStatementList.add(tempMap);
            }catch (Exception e){
                e.printStackTrace();
            }

            String mockedFilePath = mockACK2File(ABTFile.ACK2_FILE,mockedDataList,file.getName());
            String serverPath = ABTFile.SFTP_BASE_PATH+BatchControl.ACK2+ABTFile.SFTP_DIRECTORY_UPPERCASE;
            uploadFile(mockedFilePath,serverPath);
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.ACK2,false));
            theActorInTheSpotlight().attemptsTo(Batch.status());
        }
    }
    @When("he attempts to process ack3 file")
    public void he_attempts_to_process_ack3_file() throws Exception {
        List<File> fileList = java.util.Arrays.stream(new File(ABTFile.INSTRUCTION_TEMPLATE_PATH).listFiles()).collect(Collectors.toList());
        for(File file : fileList) {
            List<String> mockedDataList = new ArrayList<>();
            String line=null;
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                int count = 0;
                while ((line = br.readLine()) != null) {
                    String baseNumber = "29112277T0000";
                    if(line.startsWith("PAYMENT")){
                        String[] temp = line.split(",");
                        String[] mocked = new String[17];
                        mocked[0] = "DATA";
                        mocked[1] = temp[1];
                        mocked[2] = temp[2];
                        mocked[3] = temp[3];
                        mocked[4] = temp[5];
                        mocked[5] = temp[33];
                        mocked[6] = getTransactionDate();
                        mocked[7] = temp[10];
                        mocked[8] = temp[20];
                        mocked[9] = temp[3];
                        mocked[10] = temp[27];
                        mocked[11] = "ACCP";
                        mocked[12] = "";
                        mocked[13] = temp[6];
                        mocked[14] = "";
                        mocked[15] = String.valueOf(0.00);
                        count += 1;
                        mocked[16] = baseNumber.concat(String.valueOf(count));
                        mockedDataList.add(java.util.Arrays.stream(mocked).collect(Collectors.joining(",")));
                    } else if (line.startsWith("TRAILER")) {
                        mockedDataList.add(line);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            String mockedFilePath = mockACK2File(ABTFile.ACK3_FILE,mockedDataList,file.getName());
            String serverPath = ABTFile.SFTP_BASE_PATH+BatchControl.ACK3+ABTFile.SFTP_DIRECTORY_UPPERCASE;
            uploadFile(mockedFilePath,serverPath);
            theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.ACK3,false));
            theActorInTheSpotlight().attemptsTo(Batch.status());
        }
    }

    @And("he verifies adjustment entry in account financial history")
    public void heVerifiesAdjustmentEntryInAccountFinancialHistory() {
        Helper.switchToFrames("main");
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Customer_Information.name(), CustomerInformationMenu.PERSON,"Search"));
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        theActorInTheSpotlight().attemptsTo(SendKeys.of("%transit%").into(ccb_person_search_pageobject.PERSON_NAME_INPUTBOX));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_person_search_pageobject.PERSON_NAME_SEACH_BUTTON));
        Helper.switchToFrames("dataframe");

        ccb_person_search_pageobject.PERSON_NAME.resolveAllFor(theActorInTheSpotlight())
                .stream().forEach(menu->menu.getText());

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
        Helper.switchToFrames(CCBFrames.TAB_PAGE,CCBFrames.DATAFRAME);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_adjustment_search_pageobject.GET_MORE_BUTTON));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_adjustment_search_pageobject.GET_MORE_BUTTON));
        int entries = CCB.getSize(ccb_adjustment_search_pageobject.ACCOUNT_FINANCIAL_HISTORY)-1;
        Map<String, String> adjustmentMap = new HashMap<>();
        for(int row=0 ;row<entries ; row++){
            String createDate = CCB.getText(ccb_adjustment_search_pageobject.ARREARS_DATE(row))
                    .replace("‑","-");
            if(createDate.equalsIgnoreCase(DateUtil.getTodayDate())){
                String adjType = CCB.getText(ccb_adjustment_search_pageobject.FINANCIAL_TRANSACTION_TYPE(row));
                String amount = CCB.getText(ccb_adjustment_search_pageobject.CURRENT_AMOUNT(row));
                if(adjType.equalsIgnoreCase("GST Charge for ABT")) {
                    adjustmentMap.put(adjType, amount);
                }else if(adjType.equalsIgnoreCase("Adjustment for Settlement Summary BCM revenue")) {
                    adjustmentMap.put(adjType, amount);
                }else if(adjType.equalsIgnoreCase("Adjustment for Settlement Summary TEL revenue")) {
                    adjustmentMap.put(adjType, amount);
                }
            }
            if(!createDate.isEmpty()) {
                if (DateUtil.extractDateFromString(createDate).getTime() > DateUtil.extractDateFromString(DateUtil.getTodayDate()).getTime())
                    break;
            }
        }

        //TODO - find out transactions details
//        Assert.assertEquals(adjustmentMap.get("Adjustment for Settlement Summary BCM revenue"),"S$-366.00");
//        Assert.assertEquals(adjustmentMap.get("Adjustment for Settlement Summary TEL revenue"),"S$-144.00");
//        Assert.assertEquals(adjustmentMap.get("GST Charge for ABT"),"S$0.00");
        theActorInTheSpotlight().attemptsTo(Switch.toDefaultContext());
    }

    @And("he verifies adjustment entry for settlement summary file")
    public void heVerifiesAdjustmentEntryForSettlementSummaryFile() {

        Helper.switchToFrames("main");
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(AdminMenu.Customer_Information.name(), CustomerInformationMenu.PERSON,"Search"));
        Helper.switchAtWindow();
        theActorInTheSpotlight().attemptsTo(SendKeys.of("%transit%").into(ccb_person_search_pageobject.PERSON_NAME_INPUTBOX));
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_person_search_pageobject.PERSON_NAME_SEACH_BUTTON));
        Helper.switchToFrames("dataframe");

        ccb_person_search_pageobject.PERSON_NAME.resolveAllFor(theActorInTheSpotlight())
                .stream().forEach(menu->menu.getText());

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
        Helper.switchToNewWindow();
        theActorInTheSpotlight().attemptsTo(Switch.toFrame("dataframe"));

        int entries = CCB.getSize(ccb_adjustment_search_pageobject.ADJUSTMENT_ENTRY)-1;
        Map<String, String> adjustmentMap = new HashMap<>();
        for(int row=0 ;row<30 ; row++){
            String createDate = CCB.getText(ccb_adjustment_search_pageobject.CREATION_DATE(row))
                    .replace("‑","-");
            String personName = CCB.getText(ccb_adjustment_search_pageobject.PERSON_NAME(row));
            if(createDate.equalsIgnoreCase(DateUtil.getTodayDate()) && personName.equalsIgnoreCase("Transit Link")){
                String adjType = CCB.getText(ccb_adjustment_search_pageobject.ADJUSTMENT_TYPE(row));
                String amount = CCB.getText(ccb_adjustment_search_pageobject.AMOUNT(row));
                if(adjType.equalsIgnoreCase("GST Charge for ABT")) {
                    adjustmentMap.put(adjType, amount);
                }else if(adjType.equalsIgnoreCase("Adjustment for Settlement Summary BCM revenue")) {
                    adjustmentMap.put(adjType, amount);
                }else if(adjType.equalsIgnoreCase("Adjustment for Settlement Summary TEL revenue")) {
                    adjustmentMap.put(adjType, amount);
                }
            }
        }

        //TODO - find out transactions details
//        Assert.assertEquals(adjustmentMap.get("Adjustment for Settlement Summary BCM revenue"),"S$-732.00");
//        Assert.assertEquals(adjustmentMap.get("Adjustment for Settlement Summary TEL revenue"),"S$-288.00");
//        Assert.assertEquals(adjustmentMap.get("GST Charge for ABT"),"S$0.00");
        Helper.switchToParentWindow();
    }
    @When("he captures characters of deposit control")
    public void he_captures_characters_of_deposit_control() {
        Helper.switchToParentWindow();
        DepositControl.getChar("ABBR", DepositControlStatus.BALANCED);
//        List<Map<String,String>> mapForBankStatementList = new ArrayList<>();
//        Map<String,String> map1 = new HashMap<>();
//        map1.put("serviceProviderID","00298");
//        map1.put("amount","-144.00");
//        mapForBankStatementList.add(map1);
//        Map<String,String> map2 = new HashMap<>();
//        map2.put("serviceProviderID","00301");
//        map2.put("amount","-366.00");
//        mapForBankStatementList.add(map2);
        String mockedFilePath = MockBankStatement.giroPayment(mapForBankStatementList);
        ServerUtil.uploadFile(mockedFilePath, env.getProperty("CCRS.BankStatement.server.path"),true,"txt");
    }
    @When("he performs bank reconciliation")
    public void he_performs_bank_reconciliation() {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.BANK_RECONCILIATION,true));
    }
    @When("he process the records to create journal entries to create SAP GL Posting File using running set of batch job")
    public void he_process_the_records_to_create_journal_entries_to_create_sap_gl_posting_file_using_running_set_of_batch_job() {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.GL_ASSIGN,true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CREATE_GL_DOWNLOAD,true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.GENERATE_GL_EXTRACT,true));
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.EXTRACT_GL_FOR_SAP,true));
    }
    @Then("he will able to create SAP GL file")
    public void he_will_able_to_create_sap_gl_file() {

    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getCustomDate("yyyy-MM-dd HH:mm:ss","dd-MM-yyyy",
                cleanTextContent(PropertiesUtil.getProperties("giro.trigger.date"))));
    }
}
