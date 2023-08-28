package tasks;

import Utility.others.Helper;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.ui.Select;
import pageobjects.ccb_sale_Items_pageobject;

public class SaleItem implements Performable {

    private String category;

    public SaleItem(String category){
        this.category = category;
    }

    public static Performable ofPaymentServiceCategory(String category){
        return Instrumented.instanceOf(SaleItem.class).withProperties(category);
    }
    @Override
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames("tabPage","zoneMapFrame_1");
        actor.attemptsTo(CCB.clickAnElement(ccb_sale_Items_pageobject.SALE_ITEM));
        actor.attemptsTo(Select.option(category).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_CATEGORY));
        actor.attemptsTo(Select.optionNumber(0).from(ccb_sale_Items_pageobject.PAYMENT_SERVICE_SUB_CATEGORY));
        actor.attemptsTo(Click.on(ccb_sale_Items_pageobject.SEARCH_SALE_ITEM));
        for(int row = 0 ; row<=2 ; row++){
            actor.attemptsTo(Click.on(ccb_sale_Items_pageobject.SALE_ITEM_CHECKBOX(String.valueOf(row))));
            actor.attemptsTo(Enter.keyValues("2").into(ccb_sale_Items_pageobject.SALE_ITEM_QUANTITY(String.valueOf(row))));
        }

    }
}
