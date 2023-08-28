package tasks.Batch;

import Utility.CCRS.CCBFrames;
import Utility.Depositontrol.DepositControlStatus;
import Utility.others.DepositControlInfo;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.thucydides.core.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.ccb_user_pageobject;
import pageobjects.ccb_dc_pageobject;
import pageobjects.ccb_tc_pageobject;
import questions.CCRSWebElementText;
import tasks.DC;

import java.time.Duration;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;

public class BalanceDC implements Performable {

    Logger logger = LoggerFactory.getLogger(BalanceDC.class);

    @Step("{0} attempts to balance deposit control")
    public <T extends Actor> void performAs(T actor) {

        Helper.switchToFrames(CCBFrames.MAIN,CCBFrames.TAB_MENU);
        actor.attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
        Helper.switchToFrames(CCBFrames.MAIN,CCBFrames.TAB_PAGE);
        actor.should(seeThat(CCRSWebElementText.getText(ccb_tc_pageobject.DEPOSIT_CONTROL_INFO
                        .waitingForNoMoreThan(Duration.ofMillis(2000)))
                ,containsString(DepositControlStatus.OPEN)));
        String balanceAmt = ccb_dc_pageobject.EXPECTED_ENDING_BALACE.resolveFor(actor).getText();
        logger.info(String.format("Balance amount is %s",balanceAmt));
        DepositControlInfo.setExpectedEndingBalance(balanceAmt);
        PropertiesUtil.setProperties("dc.endingBalanceAmount",balanceAmt);
        actor.attemptsTo(Clear.field(ccb_dc_pageobject.ENDING_BALANCE)
                .then(Enter.keyValues(balanceAmt).into(ccb_dc_pageobject.ENDING_BALANCE)));
        actor.attemptsTo(SelectFromOptions.byVisibleText(DepositControlStatus.BALANCED)
                .from(ccb_dc_pageobject.DEP_CTL_STATUS_FLG));
        Helper.switchToFrames(CCBFrames.MAIN);
        actor.attemptsTo(Click.on(ccb_user_pageobject.SAVE));
        actor.attemptsTo(DC.setDepositControlChar());
    }
}
