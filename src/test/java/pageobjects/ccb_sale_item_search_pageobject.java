package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class ccb_sale_item_search_pageobject extends PageObject {

    public static final Target SALE_ITEM_STATUS_DROPDOWN = Target.the("sale item status dropdown")
            .locatedBy("#saleItemStatus");
    public static final Target SALE_ITEM_TABLE_HEADER = Target.the("sale item table header")
            .locatedBy("css:#table1>thead>tr>th");
    public static final Target SEARCH_SALE_ITEM_BY_COLUMN_NAME(String colName){
        List list = SALE_ITEM_TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9/]"," ").trim()).collect(Collectors.toList());
        int index = list.indexOf(colName.toUpperCase()) + 1;
        return Target.the("Search sale item by col name")
                .locatedBy("css:#table1>tbody>tr>td:nth-child({0})").of(String.valueOf(index))
                ;
    }
    public static final Target SALE_ITEM_TABLE_ROWS = Target.the("Sale item table rows")
            .locatedBy("#table1>tbody>tr");

    public static final Target SALE_ITEM_STATUS = Target.the("sale item status")
            .locatedBy("//td[@oraerrorelement='statusDescription']");
    public static final Target SEND_TO_FINANCE_USERS_BUTTON = Target.the("send to business users button")
            .locatedBy("//input[@type='button' and @value='Send to Finance User']");
    public static final Target APPROVE_BUTTON = Target.the("approve button")
            .locatedBy("//input[@value='Approve']");
    public static final Target REJECT_BUTTON = Target.the("reject button")
            .locatedBy("//input[@value='Reject']");
    public static final Target SEND_WITH_QUERIES_BUTTON = Target.the("send with queries button")
            .locatedBy("//input[@type='button' and @value='Send with Queries']");

    public static final Target SEND_WITH_QUERIES_COMMENT = Target.the("send with queries comment")
            .locatedBy("#boGroup_queriesGrroup_comments");


    public static final Target DYNAMIC_CHARGE_VALUE = Target.the("dynamic charge value")
            .locatedBy("//td[@orafield='dynamicCharge']");
    public static final Target COUNTER_ONLY_VALUE = Target.the("counter only value")
            .locatedBy("//td[@orafield='detailsGroup/counter']");
    public static final Target MINIMUM_APPLICABLE_VALUE = Target.the("minimum applicable value")
            .locatedBy("//td[@orafield='minimumApplicable']");
    public static final Target MINIMUM_AMOUNT_VALUE = Target.the("minimum applicable value")
            .locatedBy("//td[@orafield='minimumAmount']");
    public static final Target EMAIL_ADDRESS_VALUE = Target.the("email address value")
            .locatedBy("//td[@orafield='customerEmail']");
    public static final Target ENTER_QUANTITY_VALUE = Target.the("enter quantity value")
            .locatedBy("//td[@orafield='enterTheQuantity']");
    public static final Target SINGLE_QUANTITY_VALUE = Target.the("enter quantity value")
            .locatedBy("//td[@orafield='singleQuantityApplicable']");
    public static final Target ID_TYPE = Target.the("ID type")
            .locatedBy("//td[@orafield='idTypeVal']");
    public static final Target ID_TYPE_VALUE = Target.the("ID type value")
            .locatedBy("//td[@orafield='detailsGroup/customerNricUen']");
    public static final Target CUSTOMER_NAME_VALUE = Target.the("ID type value")
            .locatedBy("//td[@orafield='detailsGroup/custName']");
    public static final Target ID_REFERENCE_VALUE = Target.the("ID reference value")
            .locatedBy("//td[@orafield='referenceNumber']");
    public static final Target COMMENTS_TEXT = Target.the("comments value")
            .locatedBy("//td[@orafield='queriesGrroup/comments']");
    public static final Target PAYMENT_SERVICE_CATEGORY_TEXT = Target.the("payment service category value")
            .locatedBy("//td[@orafield='paymentSvcCatDescr']");
    public static final Target PAYMENT_SERVICE_SUB_CATEGORY_TEXT = Target.the("payment service sub category value")
            .locatedBy("//td[@orafield='paymentSvcCatSubDescr']");
    public static final Target SALE_ITEM_MENU = Target.the("sale item menu")
            .locatedBy("#CI_MAINMENU_topMenuItem0x0");
    public static final Target ADD_SALE_ITEM_MENU = Target.the("add sale item menu")
            .locatedBy("#CI_MAINMENU_topMenuItem1x0");
    public static final Target SALE_ITEM_SEARCH_MENU = Target.the("sale item search menu")
            .locatedBy("#CM_SALE_ITEM_subMenuItem0x0");

}
