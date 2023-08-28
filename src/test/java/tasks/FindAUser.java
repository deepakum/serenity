package tasks;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class FindAUser implements Task {

    private final int id;

    public FindAUser(int id) {
        this.id = id;
    }

    public static FindAUser withId(int id){
        return Instrumented.instanceOf(FindAUser.class).withProperties(id);
    }

    @Override
    @Step("{0} fetches the user with id #id")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/api/user/{id}")
                        .with(request -> request.pathParam("id",id))
        );
    }
}
