package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static pageobjects.ccb_refund_initiation_pageobject.TABLE_HEADER;

public class ccb_payment_event_search_pageobject extends PageObject {

    public static final Target ACCOUNT_ID_INPUTBOX = Target.the("account id inputbox")
            .locatedBy("css:#ACCT_ID");
    public static final Target ACCOUNT_ID_SEARCH_BUTTON = Target.the("account id search button")
            .locatedBy("css:#BU_Altr2_acctIdSrch");
    public static final Target PAYMENT_EVENT_INPUTBOX = Target.the("payment event inputbox")
            .locatedBy("css:#PAY_EVENT_ID");
    public static final Target PAYMENT_EVENT_SEARCH_BUTTON = Target.the("payment event search button")
            .locatedBy("css:#BU_Main_payEvtSrch");

    public static final Target SEARCH_RESULT(String col){

        List list = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9/]"," ").trim()).collect(Collectors.toList());
        int index = list.indexOf(col) + 1 ;
        System.out.println("index : "+index);
        return Target.the("Search DC by col name")
                .locatedBy("css:#dataTableBody>tr>td:nth-child({0})>span").of(String.valueOf(index+1))
                ;
    }
    public static final Target BILL_ID_SPAN(int row){
        return Target.the("bill id span")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$BILL_ID']").of(String.valueOf(row));
    }
}
