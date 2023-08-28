package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;


public class ccb_case_search_pageobject extends PageObject {

    public static final Target CASE_ID_SEARCH_BOX = Target.the("Case ID search box")
            .locatedBy("css:#CASE_ID");

    public static final Target CASE_SEARCH_BUTTON = Target.the("Case search button")
            .locatedBy("css:#BU_CASESRCH");

}
