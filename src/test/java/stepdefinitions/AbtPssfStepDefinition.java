package stepdefinitions;

import Utility.ABT.ABTFile;
import Utility.ABT.MockSapArXml;
import Utility.BatchJob.BatchControl;
import Utility.DB.DB_Utils;
import Utility.others.PropertiesUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.Batch.Batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Utility.ABT.MockSapArXml.copyPdfFile;
import static Utility.ABT.MockSapArXml.pdfDateList;
import static Utility.Server.ServerUtil.uploadFile;
import static Utility.SettlementFile.ABT.*;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class AbtPssfStepDefinition {

    Logger logger = LoggerFactory.getLogger(AbtPssfStepDefinition.class);

    @When("James attempts to process period pass settlement summary file by running {string} batch")
    public void jamesAttemptsToProcessPeriodPassSettlementSummaryFileByRunningBatch(String batch) throws Exception {
        String mockedFilepath = mockABTFile(ABTFile.PPSSF_FILE);
        String serverPath = ABTFile.SFTP_BASE_PATH+batch+ABTFile.SFTP_DIRECTORY;
        uploadFile(ABTFile.MOCKED_FILE_PATH+ File.separator+ ABTFile.CSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        uploadFile(ABTFile.MOCKED_FILE_PATH+File.separator+ ABTFile.PPSSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(batch,false));
//        String giroTriggerDate = DB_Utils.getBusinessDateForMerchantBatch(BatchControl.PPSSF,
//                new File(ABTFile.MOCKED_FILE_PATH+File.separator+ ABTFile.PPSSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE).getName(), "CM_GIRO_TRIGGER_DT");
//        PropertiesUtil.setProperties("giro.trigger.date",giroTriggerDate);
    }

    @When("James attempts to process period pass settlement summary file")
    public void jamesAttemptsToProcessPeriodPassSettlementSummaryFile(DataTable dataTable) throws Exception {
        List<List<String>> rows = dataTable.asLists(String.class);
        List<String> list = new ArrayList<>();
        for(int row = 1 ; row<rows.size();row++){
            list.add(rows.get(row).stream().collect(Collectors.joining(",")));
        }
        writeMockedFile(list,ABTFile.PPSSF_FILE);
        String serverPath = ABTFile.SFTP_BASE_PATH + BatchControl.PPSSF + ABTFile.SFTP_DIRECTORY;
        uploadFile(ABTFile.MOCKED_FILE_PATH+ File.separator+ ABTFile.CSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        uploadFile(ABTFile.MOCKED_FILE_PATH+File.separator+ ABTFile.PPSSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE,serverPath);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.PPSSF,false));
        String giroTriggerDate = DB_Utils.getBusinessDateForMerchantBatch(BatchControl.PPSSF,
                new File(ABTFile.MOCKED_FILE_PATH+File.separator+ ABTFile.PPSSF_FILE +getTransactionDate()+"."+ABTFile.FILE_TYPE).getName(), "CM_GIRO_TRIGGER_DT");
        PropertiesUtil.setProperties("giro.trigger.date",giroTriggerDate);
    }
    @When("he updates the GIRO txn status in the invoice table")
    public void he_updates_the_giro_txn_status_in_the_invoice_table() {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.INVRC,false));
        //theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.SAPSR,false));
    }
    @When("he generate SAP AR file for invoice generation")
    public void he_generate_sap_ar_file_for_invoice_generation() {
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.ABTAR,false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
    @When("he update invoice table and store PDF")
    public void he_update_invoice_table_and_store_pdf() {
        String mockedXmlPath = MockSapArXml.mockXmlFile(BatchControl.SARSF);
        String serverPath = ABTFile.SFTP_BASE_PATH+"CM-SARSF"+ABTFile.SFTP_DIRECTORY_UPPERCASE;
        //cleanWorkingServerFolder(serverPath);
        uploadFile(mockedXmlPath,serverPath);
        for(String pdfFileDate : pdfDateList){
            String pdfFilePath = copyPdfFile(pdfFileDate);
            uploadFile(pdfFilePath, serverPath);
        }
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-SARSF",false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
}
