package Utility.SaleItem;

import Utility.others.PropertiesUtil;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobjects.OneM_sale_item_pageobject;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class Portal {

    public static Task addToCart(){
        return Task.where("add to cart",
                Click.on(OneM_sale_item_pageobject.SALE_ITEM_DETAILS(getItemRow(),3)).then(
                        WaitUntil.the(OneM_sale_item_pageobject.VIEW_CART_BUTTON,isClickable())
        ));
    }

    public static Task viewCart(){
        return Task.where("view cart",
                Click.on(OneM_sale_item_pageobject.VIEW_CART_BUTTON),
                WaitUntil.the(ExpectedConditions.urlContains("viewCart"))
                );
    }
    public static Task buyerDetails(){
        return Task.where("view cart",
                Click.on(OneM_sale_item_pageobject.VIEW_CART_BUTTON),
                WaitUntil.the(ExpectedConditions.urlContains("buyerDetails"))
        );
    }

    public static Task enterBuyerDetails(){
        return Task.where("enter buyer details",
                Enter.keyValues(BuyerDetails.NAME).into(OneM_sale_item_pageobject.BUYER_NAME_INPUTBOX),
                Enter.keyValues(BuyerDetails.PIN_CODE).into(OneM_sale_item_pageobject.POSTAL_CODE_INPUTBOX),
                Click.on(OneM_sale_item_pageobject.SEARCH_POSTAL_CODE_BUTTON),
                Enter.keyValues(BuyerDetails.MOBILE).into(OneM_sale_item_pageobject.PHONE_NUMBER_INPUTBOX),
                Click.on(OneM_sale_item_pageobject.NEXT_BUTTON)
        );
    }

    public static Task reviewDetailsAndMakePayment(){
        return Task.where("review details and make payment",
                Click.on(OneM_sale_item_pageobject.NEXT_BUTTON),
                WaitUntil.the(ExpectedConditions.urlContains("makePayment"))
        );
    }

    public static int getItemRow(){
        int itemCount = OneM_sale_item_pageobject.ITEM_TABLE.resolveAllFor(theActorInTheSpotlight()).size();
        int row=1;
        for(; row<itemCount ; row++){
            if (OneM_sale_item_pageobject.SALE_ITEM_DETAILS(row,1)
                    .resolveFor(theActorInTheSpotlight()).getText().contains(PropertiesUtil.getProperties("sale.item.name")))
                break;
        }
        return row;
    }
}
