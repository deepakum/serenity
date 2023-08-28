package tasks;

import Utility.BatchJob.BatchControl;
import Utility.BatchJob.BatchStatus;
import Utility.CCRS.CCBFrames;
import Utility.CCRS.User;
import Utility.CSV.DateUtil;
import Utility.Payment.TenderType;
import Utility.others.*;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Switch;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import pageobjects.ccb_tc_search_pageobject;
import pageobjects.ccb_dc_search_pageobject;
import tasks.Batch.BalanceTenderControl;
import tasks.Batch.BalancingInProgress;
import tasks.Batch.BatchJobStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static pageobjects.ccb_refund_initiation_pageobject.TABLE_HEADER;

public class TC {

    public static Performable moveToBalancingInProgress(){
        return instrumented(BalancingInProgress.class);
    }

    public static Performable toBalance(){
        return Tasks.instrumented(BalanceTenderControl.class, new Object[0]);
    }

    public static String getTC(String tenderSource, String batchStatus){
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.TENDER_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(TC.searchByTenderSource(tenderSource));
        List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
        String dcNumber = null;
        for(int row=0;row<rows.size();row++){
            String status = CCB.getText(ccb_tc_search_pageobject.TC_STATUS(row));
            if(status.equalsIgnoreCase(batchStatus)) {
                dcNumber = CCB.getText(ccb_dc_search_pageobject.DEPOSIT_CONTROL_ID(row));
                PropertiesUtil.setProperties("deposit.control.id", dcNumber);
                PropertiesUtil.setProperties("dc.create.date.time", CCB.getText(ccb_tc_search_pageobject.TC_CREATE_DATE_TIME(row)));
                theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_search_pageobject.TC_STATUS(row)));
                break;
            }
        }
        Helper.switchToParentWindow();
        return dcNumber;
    }

    public static String getAvailableDateForTC(String tenderSource){
        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(), CashierSubMenu.TENDER_CONTROL, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(TC.searchByTenderSource(tenderSource));
        //04‑07‑2023 15:30:40
        String dcCreationDate = CCB.getText(ccb_dc_search_pageobject.CREATION_DATE_TIME(0));
        dcCreationDate = dcCreationDate.split(" ")[0].replaceAll("‑","-");
        String dcDate = null;
//        if(!dcCreationDate.equalsIgnoreCase(DateUtil.getTodayDate())) {
//            dcDate = DateUtil.getTodayDate();
//        }else {
            List<WebElementFacade> rows = ccb_dc_search_pageobject.TABLE_ROW.resolveAllFor(theActorInTheSpotlight());
            int offsetDate = 0;
            for (int row = 2; row <rows.size() ; row++) {
                offsetDate += 1;
                dcCreationDate = CCB.getText(ccb_dc_search_pageobject.CREATION_DATE_TIME(row))
                        .split(" ")[0].replaceAll("‑","-");
                dcDate = DateUtil.getOffsetDate(-offsetDate);
                String status = CCB.getText(ccb_dc_search_pageobject.STAUS(row));
                String OpenedDCDate = PropertiesUtil.getProperties("dc.create.date.time")
                        .split(" ")[0].replaceAll("‑","-");
                if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                    if (!isSunday(dcDate)) {
                        if (!dcCreationDate.equalsIgnoreCase(dcDate) && !OpenedDCDate.equalsIgnoreCase(dcDate)) {
                            break;
                        }
                    }
                }else if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.QR_CODE)) {
                    if (!isMonday(dcDate)) {
                        if (!dcCreationDate.equalsIgnoreCase(dcDate) && !OpenedDCDate.equalsIgnoreCase(dcDate)) {
                            break;
                        }
                    }
                }else if(PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.STRIPE)) {
                    if (notSundayOrMonday(dcDate)) {
                        if (!dcCreationDate.equalsIgnoreCase(dcDate) && !OpenedDCDate.equalsIgnoreCase(dcDate)) {
                            break;
                        }
                    }
                }else if(!dcCreationDate.equalsIgnoreCase(dcDate) && !OpenedDCDate.equalsIgnoreCase(dcDate)) {
                    break;
                }

                }
        Helper.switchToParentWindow();
        return dcDate;
    }
    public static boolean isSunday(String dcDate){
        boolean sunday = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(dcDate);
            cal.setTime(date);
                if (!(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                    sunday = false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return sunday;
    }

    public static boolean isMonday(String dcDate){
        boolean sunday = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(dcDate);
            cal.setTime(date);
            if (!(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)) {
                sunday = false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return sunday;
    }

    public static boolean notSundayOrMonday(String dcDate){
        boolean sunday = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(dcDate);
            cal.setTime(date);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                sunday = false;
            }else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    sunday = false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return sunday;
    }
    public static Task searchByTenderSource(String tenderSource){
        if(tenderSource.equalsIgnoreCase(TenderType.QR_CODE)) {
            tenderSource = "DBS";
        }else if(tenderSource.equalsIgnoreCase(TenderType.CREDIT_CARD)){
            tenderSource = "UOB-POS";
        }
        return Task.where("search tender control using tender source",
                WaitUntil.the(ccb_tc_search_pageobject.TENDER_SOURCE, WebElementStateMatchers.isVisible()),
                Enter.keyValues(tenderSource.toUpperCase()).into(ccb_tc_search_pageobject.TENDER_SOURCE),
                Click.on(ccb_tc_search_pageobject.SEARCH_BY_DATE),
                Switch.toFrame("dataframe"),
                WaitUntil.the(ccb_tc_search_pageobject.LIST_OF_TC, WebElementStateMatchers.isVisible())
        );
    }
}
