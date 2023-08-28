package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;


public class ccb_batch_run_tree_search_pageobject extends PageObject {

    public static final Target BATCH_RUN_TREE_TAB(int index){
        return Target.the("Batch run tree tab")
                .locatedBy("//*[@id='tabRow']/td[{0}]").of(String.valueOf(index));
    }
    public static final Target DO_NOT_RESTART_SW = Target.the("Do not restart check box")
            .locatedBy("//input[@id='DO_NOT_RESTART_SW']");

    public static final Target BATCH_RUN_STATUS = Target.the("Batch run status")
            .locatedBy("//*[@id='tStart']/tbody/tr[1]//span[5]");

    public static final Target BATCH_NUMBER_TAB = Target.the("Batch number tab")
            .locatedBy("#L_BATCH_NBR");

    public static final Target RUN_STATUS_TAB = Target.the("Batch number tab")
            .locatedBy("#L_RUN_STATUS");
    public static final Target RUN_CONTROL_TAB = Target.the("Run control tab")
            .locatedBy("#BRTREE_RLBL");
    public static final Target SAVE_BUTTON = Target.the("save button")
            .locatedBy("#IM_SAVE");
}
