import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty","json:target/serenity-reports/cucumber_report.json"
        ,"html:target/serenity-reports//serenity-html-report"},
        //features = "src/test/resources/features/SaleItem/SaleItemEditedByMakerUser.feature",
        dryRun = false,
        tags = "@counterPaymentChe"
)
public class CucumberTestSuite {

}
