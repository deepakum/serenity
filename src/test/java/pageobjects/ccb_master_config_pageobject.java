package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_master_config_pageobject extends PageObject {
    public static final Target ABT_BROADCAST_IMG = Target.the("ABT braoadcast image")
            .locatedBy("(//*[@role='button' and @title='Broadcast'])[1]");
    public static final Target MASTER_CONFIG_ROW = Target.the("master config entries")
            .locatedBy("//*[@oramdlabel='GENERIC_SUMMARY']/tbody/tr");
    public static final Target PACKAGE_ID(int row){
        return Target.the("package id")
                .locatedBy("(//*[@oramdlabel='GENERIC_SUMMARY']/tbody/tr)[{0}]/td[@orafield='packageID']")
                .of(String.valueOf(row));
    }
    public static final Target SERVICE_PROVIDER_ID(int row){
        return Target.the("service provider id")
                .locatedBy("(//*[@oramdlabel='GENERIC_SUMMARY']/tbody/tr)[{0}]/td[@orafield='serviceProviderID']")
                .of(String.valueOf(row));
    }
    public static final Target WRITE_OFF_ADJ_TYPE(int row){
        return Target.the("write off adjustment type")
                .locatedBy("(//*[@oramdlabel='GENERIC_SUMMARY']/tbody/tr)[{0}]/td[@orafield='writeOffAdjType']")
                .of(String.valueOf(row));
    }

    public static final Target PACKAGE_DESCRIPTION(int row){
        return Target.the("write off adjustment type")
                .locatedBy("(//*[@oramdlabel='GENERIC_SUMMARY']/tbody/tr)[{0}]/td[@orafield='packageDesc']")
                .of(String.valueOf(row));
    }
}
