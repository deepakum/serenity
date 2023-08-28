package Utility.others;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.SelectedStatus;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_sale_Items_pageobject;

public class CheckCheckbox implements Interaction {

    public final Target target;

    public CheckCheckbox(Target target){
        this.target = target;
    }

    @Override
    @Step("{0} check the checkbox")
    public <T extends Actor> void performAs(T actor) {
        boolean isSelected = actor.asksFor(SelectedStatus.of(target).asBoolean());
        if(!isSelected) actor.attemptsTo(Click.on(target));
    }
}
