package tasks;

import Utility.others.Helper;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;
import pageobjects.ccb_dc_char_pageobject;
import pageobjects.ccb_dc_search_pageobject;
import questions.CCRSWebElementText;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.core.Is.is;

public class EditCounterLocation implements Performable {

    Logger logger = LoggerFactory.getLogger(EditCounterLocation.class);
    String location;
    String value;
    public EditCounterLocation(String location, String value){
        this.location = location;
        this.value = value;
    }

    public static EditCounterLocation to(String location, String value){
        return Instrumented.instanceOf(EditCounterLocation.class).withProperties(location,value);
    }

    @Step("{0} able to select current date")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames("tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","TNDR_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                ,is("Characteristic Type")));
        int index=0;
        for (int row = 0; row < ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size(); row++) {
            String charType = ccb_dc_char_pageobject.TC_CHARACTERISTIC_TYPE(row).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase(location)) {
                index = row;
                break;
            }
        }
        String location = ccb_dc_char_pageobject.COUNTER_LOCATION_VALUE(index).resolveFor(theActorInTheSpotlight()).getAttribute("value");
        System.out.println("Counter location : "+location);
        actor.attemptsTo(Click.on(ccb_dc_char_pageobject.COUNTER_LOCATION_SEARCH_ICON(index)));
        actor.attemptsTo(SwitchToWindow.targetWindow());
        Helper.switchToFrames("dataframe");

        actor.attemptsTo(Click.on(
                ccb_dc_search_pageobject.CHARACTERISTIC_VALUE.resolveAllFor(actor)
                        .stream().filter(ele -> ele.getText().equalsIgnoreCase(value))
                        .findFirst().get())
        );
    }
}
