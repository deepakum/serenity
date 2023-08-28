package questions;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

import java.time.Duration;

public class CCRSWebElementText implements Question {

    Target target;

    public CCRSWebElementText(Target target){
        this.target = target;
    }

    public static Question getText(Target target){
        return Instrumented.instanceOf(CCRSWebElementText.class).withProperties(target);
    }
    @Override
    public Object answeredBy(Actor actor) {
        return Text.of(target.waitingForNoMoreThan(Duration.ofMillis(5000)))
                .answeredBy(actor).trim();
    }
}
