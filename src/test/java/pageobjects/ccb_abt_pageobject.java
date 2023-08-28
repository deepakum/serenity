package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static pageobjects.ccb_refund_initiation_pageobject.TABLE_HEADER;

public class ccb_abt_pageobject extends PageObject {

    public static final Target SEARCH_RESULT(String col){

            List list = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9/]"," ").trim()).collect(Collectors.toList());
        int index = list.indexOf(col);

        return Target.the("package id by col name")
                .locatedBy("css:#dataTableBody>tr>td:nth-child({0})").of(String.valueOf(index+1))
                ;
    }

    public static final Target ABT_SETTLEMENT_TYPE_INPUTBOX=Target.the("abt settlement type inputbox")
            .locatedBy("//input[@id='BJP:7$BATCH_PARM_VAL']");
    public static final Target BUSINESS_SYSTEM_INPUTBOX=Target.the("business system inputbox")
            .locatedBy("//input[@id='BJP:6$BATCH_PARM_VAL']");

}
