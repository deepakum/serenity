package tasks;

import Utility.others.PropertiesUtil;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

import static Utility.DB.DB_Utils.getPaymentEventID;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ReceiptID implements Performable {

    public static Performable fetch(){
        return instrumented(ReceiptID.class);
    }
    @Override
    public <T extends Actor> void performAs(T t) {

        String paymentEventID = getPaymentEventID();
        PropertiesUtil.setProperties("CCRS.paymentEventID",paymentEventID);
    }
}
