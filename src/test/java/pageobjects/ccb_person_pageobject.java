package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_person_pageobject extends PageObject {

    public static final Target ACCOUNT_CONTEXT_IMG = Target.the("account context image")
            .locatedBy("#data_202>table>tbody>tr:nth-child(2)>td:nth-child(1)>img");
    public static final Target GO_TO_ACCOUNT_PAYMENT_HISTORY_MENU = Target.the("Go to account payment history")
            .locatedBy("#CI_CONTEXTACCOUNT_subMenuItem0x3");
    public static final Target GO_TO_FT_TYPE_IMAGE = Target.the("Go to FT type image")
            .locatedBy("//img[@id='IM_ACCT_FT_HIST:0$AFHList_goTo']");
}
