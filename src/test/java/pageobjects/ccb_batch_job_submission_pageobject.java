package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_batch_job_submission_pageobject extends PageObject {

    public static final Target ADMIN_MENU(int index){
        return Target.the("Menu")
                .locatedBy("//li[@id='CI_MAINMENU_topMenuItem0x{0}']")
                .of(String.valueOf(index));
    }

    public static final Target TOOLS_SUB_MENU(int index){
        return Target.the("Menu")
                .locatedBy("//li[@id='CI_BGPROCESS_subMenuItem0x{0}']")
                .of(String.valueOf(index));
    }

    public static final Target DATE = Target.the("Batch submission date button")
            .locatedBy("//img[@id='IM_PROCESS_DT']");
    public static final Target DATE_INPUT_BOX = Target.the("Batch submission date inputbox")
            .locatedBy("//input[@id='PROCESS_DT']");
    public static final Target USER_ID = Target.the("User id input box")
            .locatedBy("//input[@id='USER_ID']");
    public static final Target DATE_ACCEPT = Target.the("Accept current date")
            .locatedBy("//input[@type='button' and @value='Accept']");

    public static final Target CLAIM_DEPOSIT_CONTROL_FLAG = Target.the("claim deposit control input box")
            .locatedBy("//input[@id='BJP:8$BATCH_PARM_VAL']");


    public static final Target TENDER_SOURCE = Target.the("tender source")
            .locatedBy("//input[@id='BJP:0$BATCH_PARM_VAL']");
    public static final Target FILE_NAME = Target.the("tender source")
            .locatedBy("//input[@id='BJP:1$BATCH_PARM_VAL']");
    public static final Target FRAME_BJP = Target.the("tender source")
            .locatedBy("//iframe[@id='BJP']");

    public static final Target PSP_LOOKUP_VAL = Target.the("PSP lookup value")
            .locatedBy("//input[@id='BJP:4$BATCH_PARM_VAL']");
    public static final Target COUNTER_FLAG = Target.the("Is counter input box")
            .locatedBy("//input[@id='BJP:1$BATCH_PARM_VAL']");

    public static final Target LOCATION_VAL = Target.the("location value input box")
            .locatedBy("//input[@id='BJP:6$BATCH_PARM_VAL']");

    public static final Target ERP_FLAG = Target.the("ERP value input box")
            .locatedBy("//input[@id='BJP:7$BATCH_PARM_VAL']");

    public static final Target RUN_STATUS = Target.the("batch run status")
            .locatedBy("//span[@id='SEARCH_RESULTS:0$RUN_STATUS']");

    public static final Target BATCH_CONTROL_CONTEXT_MENU = Target.the("batch control context menu")
            .locatedBy("css:#IM_Main_batCtx");

    public static final Target BATCH_RUN_TREE = Target.the("batch run tree menu")
            .locatedBy("css:#CI_CONTEXTBATCHCONTROL_subMenuItem0x3");

    public static final Target BATCH_RECORD_COUNT = Target.the("batch record count")
            .locatedBy("//*[@id='dataExplorerTableBody101']/tr[1]/td[2]");
    public static final Target BATCH_STATUS = Target.the("batch record count")
            .locatedBy("//*[@id='dataExplorerTableBody101']/tr[1]/td[3]");
}

