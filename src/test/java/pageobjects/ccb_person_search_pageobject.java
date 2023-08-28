package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_person_search_pageobject extends PageObject {

    public static final Target PERSON_NAME_INPUTBOX = Target.the("Person name inputbox")
            .locatedBy("#ENTITY_NAME");
    public static final Target PERSON_NAME_SEACH_BUTTON = Target.the("Person name search button")
            .locatedBy("#BU_Alternate_search");
    public static final Target PERSON_NAME = Target.the("character value")
            .locatedBy("css:#dataTableBody>tr>td:nth-child(1)>span");

}
