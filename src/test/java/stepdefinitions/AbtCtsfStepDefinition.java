package stepdefinitions;

import Utility.ABT.ABTFile;
import Utility.BatchJob.BatchControl;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.Batch.Batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Utility.Server.ServerUtil.uploadFile;
import static Utility.SettlementFile.ABT.*;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class AbtCtsfStepDefinition {

    Logger logger = LoggerFactory.getLogger(AbtCtsfStepDefinition.class);

    @When("James attempts to process complimentary tickets summary file by running {string} batch")
    public void jamesAttemptsToComplimentaryTicketsSummaryFileByRunningBatch(String batch) throws Exception {
        mockABTFile(ABTFile.CTSF_FILE);
        String serverPath = ABTFile.SFTP_BASE_PATH+batch+ABTFile.SFTP_DIRECTORY;
        uploadFile(ABTFile.MOCKED_FILE_PATH + File.separator+ ABTFile.CSF_FILE + getTransactionDate()+"." + ABTFile.FILE_TYPE, serverPath);
        uploadFile(ABTFile.MOCKED_FILE_PATH + File.separator+ ABTFile.CTSF_FILE + getTransactionDate()+"." + ABTFile.FILE_TYPE, serverPath);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(batch,false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }

    @When("James attempts to process complimentary tickets summary file")
    public void jamesAttemptsToComplimentaryTicketsSummaryFile(DataTable dataTable) throws Exception {
        List<List<String>> rows = dataTable.asLists(String.class);
        List<String> list = new ArrayList<>();
        for(int row = 1 ; row<rows.size();row++){
            list.add(rows.get(row).stream().collect(Collectors.joining(",")));
        }
        writeMockedFile(list,ABTFile.CTSF_FILE);
        String serverPath = ABTFile.SFTP_BASE_PATH + BatchControl.CTSF + ABTFile.SFTP_DIRECTORY;
        uploadFile(ABTFile.MOCKED_FILE_PATH + File.separator+ ABTFile.CSF_FILE + getTransactionDate()+"." + ABTFile.FILE_TYPE, serverPath);
        uploadFile(ABTFile.MOCKED_FILE_PATH + File.separator+ ABTFile.CTSF_FILE + getTransactionDate()+"." + ABTFile.FILE_TYPE, serverPath);
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.CTSF,false));
        theActorInTheSpotlight().attemptsTo(Batch.status());
    }
}
