package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class OneM_sale_item_pageobject extends PageObject {

    public static final Target SALE_ITEM_DETAILS(int row, int col){
        return Target.the("item details")
                .locatedBy("#data-table-main>tbody>tr:nth-child({0})>td:nth-child({1})")
                .of(String.valueOf(row),String.valueOf(col));
    }

    public static final Target ITEM_TABLE = Target.the("item details")
            .locatedBy("#data-table-main>tbody>tr");
    public static final Target VIEW_CART_BUTTON = Target.the("view cart button")
            .locatedBy("//button[@class='btn ccrs_addcart']");
    public static final Target BUYER_NAME_INPUTBOX = Target.the("buyer name inputbox")
            .locatedBy("//input[@name='name']");
    public static final Target POSTAL_CODE_INPUTBOX = Target.the("postal code inputbox")
            .locatedBy("//input[@name='postalcode']");
    public static final Target SEARCH_POSTAL_CODE_BUTTON = Target.the("search postal code button")
            .locatedBy("//input[@name='postalcode']/following-sibling::button");
    public static final Target PHONE_NUMBER_INPUTBOX = Target.the("phone number inputbox")
            .locatedBy("//input[@name='phoneNo']");
    public static final Target NEXT_BUTTON = Target.the("next button")
            .locatedBy("//button[@class='btn another-element ccrs_pdbtn']");
}

