package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class ccb_sale_Items_pageobject extends PageObject {

    public static final Target SALE_ITEM = Target.the("sale item button")
            .locatedBy("#ophTab");

    public static final Target PAYMENT_SERVICE_CATEGORY = Target.the("payment service category")
            .locatedBy("#paySvcCat");
    public static final Target PAYMENT_SERVICE_SUB_CATEGORY = Target.the("payment service sub category")
            .locatedBy("//select[@id='paySvcSubCat']");
    public static final Target PAGE_TITLE = Target.the("page title")
            .locatedBy("//div[@id='ptitle']");

    public static final Target SALE_ITEM_CHECKBOX(String itemNum) { return Target.the("Sale item checkbox")
            .locatedBy("//input[@id='selectedSL_{0}']").of(itemNum);
    }

    public static final Target SALE_ITEM_QUANTITY(String quantity){
        return Target.the("item quantity")
                .locatedBy("//input[@id='quantity_{0}']").of(quantity);
    }
    public static final Target SEARCH_SALE_ITEM = Target.the("search sale item button")
            .locatedBy("(//select[@id='paySvcCat']/parent::td/following-sibling::td)[3]/input");

    public static final Target ORGANISATION = Target.the("organisation input box")
            .locatedBy("css:#name");
    public static final Target PIN_CODE = Target.the("pin code input box")
            .locatedBy("css:#postalCode");

    public static final Target PIN_CODE_SEARCH = Target.the("pin code search button")
            .locatedBy("//input[@id='postalCode']/following-sibling::img");

    public static final Target CONTACT_NUMBER = Target.the("contact number input box")
            .locatedBy("//input[@id='contactNumber']");

    public static final Target PROCEED_TO_PAY = Target.the("proceed to pay button")
            .locatedBy("//input[@id='proceedBtnOPH']");
    public static final Target PAYMENT_SERVICE_CATEGORY_DROPDOWN = Target.the("payment service category dropdown")
            .locatedBy("#paymentServiceCategory");
    public static final Target PAYMENT_SERVICE_SUB_CATEGORY_DROPDOWN = Target.the("payment service sub category dropdown")
            .locatedBy("#paymentServiceSubCategory");
    public static final Target ITEM_NAME_INPUT_BOX = Target.the("item name input box")
            .locatedBy("#title");
    public static final Target ITEM_DESCRIPTION_INPUT_BOX = Target.the("item description input box")
            .locatedBy("#description");
    public static final Target UNIT_PRICE_INPUT_BOX = Target.the("unit price input box")
            .locatedBy("#amount");
    public static final Target AVAILABLE_FROM_DATE_INPUT_BOX = Target.the("available from date input box")
            .locatedBy("#detailsGroup_availableFrom");
    public static final Target AVAILABLE_TO_DATE_INPUT_BOX = Target.the("available to date input box")
            .locatedBy("#detailsGroup_availableTo");
    public static final Target SAVE_BUTTON = Target.the("save button")
            .locatedBy("//input[@oramdlabel='SAVE_BTN_LBL']");
    public static final Target SALE_ITEM_STATUS_TEXT = Target.the("sale item status text")
            .locatedBy("//td[@oraerrorelement='itemStatus']");
    public static final Target EDIT_BUTTON = Target.the("edit button")
            .locatedBy("(//input[@oramdlabel='C1_EDIT_LBL'])[1]");
    public static final Target DELETE_BUTTON = Target.the("edit button")
            .locatedBy("//input[@oramdlabel='DELETE_LBL']");
    public static final Target OK_BUTTON = Target.the("ok button")
            .locatedBy("#IM_OK");
    public static final Target SEND_TO_BUSINESS_USERS_BUTTON = Target.the("send to business users button")
            .locatedBy("//input[@type='button' and @value='Send to Business Users']");
    public static final Target UNIT_PRICE = Target.the("unit price")
            .locatedBy("//td[@orafield='amount']");
    public static final Target SALE_ITEM_TABLE_HEADER = Target.the("Case table header")
            .locatedBy("css:#saleItemTable>thead>tr>th");
    public static final Target SEARCH_SALE_TEM_BY_COLUMN_NAME(String colName){
        List list = SALE_ITEM_TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9/]"," ").trim()).collect(Collectors.toList());
        int index = list.indexOf(colName) + 2;
        return Target.the("Search sale item by col name")
                .locatedBy("css:#saleItemTable>tbody>tr>td:nth-child({0})").of(String.valueOf(index))
                ;
    }
    public static final Target PAYMENT_SUMMARY_TAB = Target.the("payment summary tab")
            .locatedBy("#consTab");
    public static final Target DYNAMIC_CHARGE_CHECKBOX = Target.the("dynamic charge checkbox")
            .locatedBy("#dynamicCharge");
    public static final Target UNIT_OF_MEASUREMENT_INPUTBOX = Target.the("unit of measurement inputbox")
            .locatedBy("#detailsGroup_msrmntUnit");
    public static final Target PICKUP_LOCATION_INPUTBOX = Target.the("pickup location inputbox")
            .locatedBy("#detailsGroup_pickLoc");
    public static final Target NO_OF_DAYS_INPUTBOX = Target.the("no of days inputbox")
            .locatedBy("#detailsGroup_days");
    public static final Target REFERENCE_NUMBER_CHECKBOX = Target.the("reference number checkbox")
            .locatedBy("#detailsGroup_ReferenceNo");
    public static final Target REFERENCE_NUMBER_INPUTBOX = Target.the("reference number inputbox")
            .locatedBy("#detailsGroup_ReferenceNoPmt");
    public static final Target COUNTER_CHECKBOX = Target.the("counter checkbox")
            .locatedBy("#detailsGroup_counter");
    public static final Target INTERNAL_TO_LTA_CHECKBOX = Target.the("internal to lta")
            .locatedBy("#detailsGroup_Intrnl");
    public static final Target MINIMUM_AMOUNT_APPLICABLE_CHECKBOX = Target.the("minimum amount applicable checkbox")
            .locatedBy("#detailsGroup_minApplicable");
    public static final Target MINIMUM_AMOUNT_INPUTBOX = Target.the("minimum amount inputbox")
            .locatedBy("#detailsGroup_minimumAmount");
    public static final Target ENTER_QUANTITY_CHECKBOX = Target.the("enter quantity checkbob")
            .locatedBy("#detailsGroup_enterTheQuantity");
    public static final Target SINGLE_QUANTITY_CHECKBOX = Target.the("single quantity checkbox")
            .locatedBy("#detailsGroup_singleQuantityApplicable");
    public static final Target EMAIL_ADDRESS_INPUTBOX = Target.the("email adress inputbox")
            .locatedBy("#customerEmail_0");
    public static final Target ID_TYPE_DROPDOWN = Target.the("id type dropdown")
            .locatedBy("#detailsGroup_nricUenDropDown");
    public static final Target ID_NUMBER_INPUTBOX = Target.the("id number inputbox")
            .locatedBy("#detailsGroup_customerNricUen");
    public static final Target CUSTOMER_NAME_INPUTBOX = Target.the("customer name inputbox")
            .locatedBy("#detailsGroup_custName");
    public static final Target COMMENTS_INPUTBOX = Target.the("comments inputbox")
            .locatedBy("#boGroup_queriesGrroup_comments");


}
