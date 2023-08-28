package tasks;

import Utility.others.Helper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SwitchToWindow implements Performable {

    public static Performable targetWindow(){
        return instrumented(SwitchToWindow.class);
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        Helper.switchAtWindow();
    }
}
