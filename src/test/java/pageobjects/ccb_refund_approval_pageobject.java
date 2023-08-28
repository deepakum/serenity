package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class ccb_refund_approval_pageobject extends PageObject {

    public static final Target BUSINESS_SYSTEM_DROPDOWN = Target.the("Business system dropdown")
            .locatedBy("css:#request_busSystem");

    public static final Target RECEIPT_ID_INPUTBOX = Target.the("Receipt id inputbox")
            .locatedBy("css:#request_payEventId");

    public static final Target REFUND_ID_INPUTBOX = Target.the("Receipt id inputbox")
            .locatedBy("css:#request_refundId");

    public static final Target STATUS_TEXT_LABEL = Target.the("Case status text label")
            .locatedBy("css:#STATUS_LBL");

    public static final Target REFUND_OPTION(String option){
        List list = REFUND_TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getText()).collect(Collectors.toList());
        int index = list.indexOf(option);
        return Target.the("Refund option #option")
                .locatedBy("#refundRecord>tr>td:nth-child({0})").of(String.valueOf(index+1))
                ;
    }

    public static final Target REFUND_ID_TEXT(String option) {
        List list = REFUND_TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getText()).collect(Collectors.toList());
        int index = list.indexOf(option);
        return Target.the("Refund id text")
                .locatedBy("#refundRecord>tr>td:nth-child(1)>a>span").of(String.valueOf
                        (index));
    }

    public static final Target APPROVE_CHECKBOX_BUTTON = Target.the("Approve checkbox button")
            .locatedBy("css:#isApproved1_0");
    public static final Target REFUND_TABLE_HEADER = Target.the("Refund table header")
            .locatedBy("css:#refundRecord>thead>tr>th");
    public static final Target SUBMIT_BUTTON = Target.the("submit button")
            .locatedBy("css:#submitBtn");
}
