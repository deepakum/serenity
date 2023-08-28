package questions;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BatchTreeStatus implements Question {

    Target target;

    public BatchTreeStatus(Target target){
        this.target = target;
    }

    public static Question getText(Target target){
        return Instrumented.instanceOf(BatchTreeStatus.class).withProperties(target);
    }
    @Override
    public List<String> answeredBy(Actor actor) {
        return Collections.singletonList(Text.of(target).answeredBy(actor));
    }
}
