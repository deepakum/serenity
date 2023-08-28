package Utility.Notice;

import Utility.Server.ServerUtil;
import Utility.others.PropertiesUtil;
import Utility.others.BatchFile;
import Utility.others.Helper;
import io.cucumber.datatable.DataTable;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import pageobjects.OneM_fine_and_fees_pageobject;
import tasks.Batch.Batch;
import tasks.OneMPortal;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Utility.SettlementFile.MockXml.getXmlFile;
import static Utility.SettlementFile.MockXml.mockNoticeFile;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class Notice {
    public static final String noticeStatus = "2";
    public static final String displayFlag = "p";
    public static final String offenceGroupCode = "S_NO_CC_DIR";
    public static final String revenueCode = "ERP_532";
    public static final String offenceAmount = "200.00";
    public static final int noticeDigitsLength = 10;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public static void createNotice(){
        Map<String, String> result = new HashMap<>();
        Map<String, String> ownerMap = parameters(new Owner());
        Map<String, String> vehicleMap = parameters(new Vehicle());
        Map<String, String> noticeMap = parameters(new Notice());
        result.putAll(ownerMap);
        result.putAll(noticeMap);
        result.putAll(vehicleMap);

        mockNoticeFile(BatchFile.EVMS_NOTICE_FILE,result);
        String source = getXmlFile(BatchFile.EVMS_NOTICE_FILE);
        String destination = environmentVariables.getProperty("CCRS.EVMS.XML.server.path");
        ServerUtil.uploadFile(source,destination,true,"xml");
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-LNVIN",false));
    }

    public static Map<String, String> parameters(Object obj) {
        Map<String, String> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try { map.put(field.getName(), field.get(obj).toString()); } catch (Exception e) { }
        }
        return map;
    }

    public static void createNotice(DataTable dataTable){

        List<Map<String, String>> list = dataTable.asMaps();
        Map<String, String> result = new HashMap<>();
        list.stream().forEach(map -> {
            result.putAll(map.entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> (String) entry.getValue())));
        });
        mockNoticeFile(BatchFile.EVMS_NOTICE_FILE,result);
        String source = getXmlFile(BatchFile.EVMS_NOTICE_FILE);
        String destination = environmentVariables.getProperty("CCRS.EVMS.XML.server.path");
        ServerUtil.uploadFile(source,destination,true,"xml");
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit("CM-LNVIN",false));
    }

    public static void searchNotice(String modeOfPayment){
        OnStage.theActor("James");
        theActorInTheSpotlight().attemptsTo(Open.browserOn().thePageNamed("EVMS.fines.url"));
        theActorInTheSpotlight().attemptsTo(OneMPortal.searchNotice(IdentificationType.SG_NRIC,IdentificationNo.NRIC));
    }
    public static void makePayment(String modeOfPayment){
        searchNotice(modeOfPayment);
        theActorInTheSpotlight().attemptsTo(OneMPortal.makePayment(modeOfPayment));
        Helper.customWait(10000);
        String referenceIDText = OneM_fine_and_fees_pageobject.REFERENCE_ID_TEXT.resolveFor(theActorInTheSpotlight()).getAttribute("innerText");
        PropertiesUtil.setProperties("portal.paymentEventID", OneMPortal.extractReferenceIDFromText(referenceIDText));
    }
}
