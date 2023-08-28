package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import pageobjects.ccb_user_pageobject;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class CCRSLoginResult implements Question {

    public static CCRSLoginResult displayed(){
        return new CCRSLoginResult();
    }

    @Override
    public String answeredBy(Actor actor) {

        return Text.of(ccb_user_pageobject.CRRS_HOME_HEADER)
                .answeredBy(actor);
    }
}
