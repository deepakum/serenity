package tasks;

import Utility.others.Helper;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import pageobjects.ccb_dc_search_pageobject;

public class CharacterValue implements Task {

    String value;
    public CharacterValue(String value){
        this.value = value;
    }

    public static Performable select(String value){
        return Instrumented.instanceOf(CharacterValue.class).withProperties(value);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(Click.on(ccb_dc_search_pageobject.CHARACTERISTIC_VALUE_INPUTBOX));
        if(!value.isEmpty()) {
            actor.attemptsTo(Click.on(ccb_dc_search_pageobject.CHARACTERISTIC_VALUE_SEARCH_ICON));
            actor.attemptsTo(SwitchToWindow.targetWindow());
            Helper.switchToFrames("dataframe");

            actor.attemptsTo(Click.on(
                    ccb_dc_search_pageobject.CHARACTERISTIC_VALUE.resolveAllFor(actor)
                            .stream().filter(ele -> ele.getText().equalsIgnoreCase(value))
                            .findFirst().get())
            );
            actor.attemptsTo(SwitchToWindow.targetWindow());
        }
    }
}
