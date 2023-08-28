package Utility.SaleItem;

import net.serenitybdd.screenplay.ensure.Ensure;
import pageobjects.ccb_sale_item_maintenance_pageobject;
import tasks.CCB;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class SaleItemMaintenance {

    public static void verifyGstAmount(Double amount){
        Double gst = Double.valueOf(CCB.getTextUsingAttribute(ccb_sale_item_maintenance_pageobject.GST_PERCENTAGE_INPUTBOX,"value"));
        Double gstAmount =  (amount*gst)/100;
        Double totalAmount = amount + gstAmount;
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(ccb_sale_item_maintenance_pageobject.GST_AMOUNT_INPUTBOX).value().contains(String.valueOf(gstAmount.doubleValue()))
        );
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(ccb_sale_item_maintenance_pageobject.TOTAL_AMOUNT_INPUTBOX).value().contains(String.valueOf(totalAmount.doubleValue()))
        );
    }
}
