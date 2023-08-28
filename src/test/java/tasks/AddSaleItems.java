package tasks;

import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_sale_Items_pageobject;

import java.util.List;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class AddSaleItems implements Performable {

    private String paymentServiceCategory="Plans & Publications";
    private String paymentServiceSubCategory="Development & Building Control (DBC) Search Fee S7";
    private String quantity;
    private String itemName;

    public AddSaleItems(String paymentServiceCategory, String paymentServiceSubCategory, String itemName, String quantity){
        this.paymentServiceCategory = paymentServiceCategory;
        this.paymentServiceSubCategory = paymentServiceSubCategory;
        this.quantity = quantity;
        this.itemName = itemName;
    }

    public static AddSaleItems toCart(String paymentServiceCategory, String paymentServiceSubCategory, String itemName, String quantity){
       return Instrumented.instanceOf(AddSaleItems.class).withProperties(paymentServiceCategory,paymentServiceSubCategory,itemName,quantity);
    }

    @Override
    @Step("^(.*) attempts to add sale items$")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames("main","tabPage","zoneMapFrame_1");
        actor.attemptsTo(CCB.searchSaleItems(this.paymentServiceCategory,this.paymentServiceSubCategory));
        List<WebElementFacade> listOfSaleItems = ccb_sale_Items_pageobject.SEARCH_SALE_TEM_BY_COLUMN_NAME("Item Name".toUpperCase()).resolveAllFor(theActorInTheSpotlight());
        if (itemName.isEmpty() || itemName.length()==0){
            itemName = PropertiesUtil.getProperties("sale.item.name");
        }
        for(int itemRow = 0 ; itemRow <= listOfSaleItems.size() ; itemRow++){
            if(listOfSaleItems.get(itemRow).getText().trim().contains(itemName)){
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_sale_Items_pageobject.SALE_ITEM_CHECKBOX(String.valueOf(itemRow))));
                theActorInTheSpotlight().attemptsTo(Clear.field(ccb_sale_Items_pageobject.SALE_ITEM_QUANTITY(String.valueOf(itemRow)))
                        .then(Enter.keyValues(quantity).into(ccb_sale_Items_pageobject.SALE_ITEM_QUANTITY(String.valueOf(itemRow)))));
                break;
            }
        }

    }
}
