package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_tc_search_pageobject extends PageObject {

    public static final Target TC_CREATION_DATE_BUTTON = Target.the("TC creation date button")
            .locatedBy("//img[@id='IM_CRE_DTTM']");
    public static final Target TC_CREATION_DATE_INPUTBOX = Target.the("TC creation date inputbox")
            .locatedBy("css:#CRE_DTTM");

    public static final Target USERNAME_INPUTBOX = Target.the("username inputbox")
            .locatedBy("css:#USER_ID");
    public static final Target SEARCH_BY_DATE = Target.the("search button")
            .locatedBy("//input[@id='BU_Altr_depCtlAlSr']");
    public static final Target LATEST_TC = Target.the("latest open TC")
            .locatedBy("(//*[@id='dataTableBody']/tr)[1]");
    public static final Target TENDER_SOURCE = Target.the("Tender source inputbox")
            .locatedBy("css:#TNDR_SOURCE_CD");
    public static final Target TENDER_CONTROL_STATUS_OPTION = Target.the("Tender control status option")
            .locatedBy("css:#TNDR_CTL_ST_FLG");
    public static final Target LIST_OF_TC = Target.the("List of tender control")
            .locatedBy("//*[@id='dataTableBody']/tr/td[6]");

    public static final Target TC_CREATE_DATE_TIME(int rowIndex) {
        return Target.the("tc creation date and time")
                .locatedBy("//*[@id='SEARCH_RESULTS:{0}$CRE_DTTM']").of(String.valueOf(rowIndex));
    }

    public static final Target TC_STATUS(int rowIndex) {
        return Target.the("tc creation date and time")
                .locatedBy("//*[@id='SEARCH_RESULTS:{0}$TNDR_CTL_ST_FLG']").of(String.valueOf(rowIndex));
    }
    public static final Target TENDER_SOURCE_SEARCH_BUTTON = Target.the("tender source search button")
            .locatedBy("#BU_Section1_tndrSrSrch");
}
