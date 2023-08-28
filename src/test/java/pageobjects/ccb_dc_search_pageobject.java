package pageobjects;

import Utility.others.Helper;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

import java.util.List;
import java.util.stream.Collectors;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static pageobjects.ccb_refund_initiation_pageobject.TABLE_HEADER;

public class ccb_dc_search_pageobject extends PageObject {

    public static Target DATE_BUTTON = Target.the("date button")
            .locatedBy("css:#IM_CRE_DTTM");

    public static Target DATE_INPUT_BOX = Target.the("date input box")
            .locatedBy("css:#CRE_DTTM");
    public static Target DC_SEARCH_BUTTON = Target.the("dc search button")
            .locatedBy("css:#BU_Alt_altSearch");
    public static final Target DATE_ACCEPT = Target.the("Accept button")
            .locatedBy("//input[@type='button' and @value='Accept']");
    public static final Target CREATION_DATE_TIME(int row) {
        return Target.the("latest deposit control")
                .locatedBy("//*[@id='SEARCH_RESULTS:{0}$CRE_DTTM']")
                .of(String.valueOf(row));
    }
    public static final Target STAUS(int row) {
        return Target.the("latest deposit control")
                .locatedBy("//*[@id='SEARCH_RESULTS:{0}$TNDR_CTL_ST_FLG']")
                .of(String.valueOf(row));
    }
    public static final Target DC_INFO(int row, int col){
        return Target.the("DC information")
                .locatedBy("//tbody[@id='dataTableBody']/tr[{0}]/td[{1}]/span")
                .of(String.valueOf(row),String.valueOf(col));
    }

    public static final Target ALL_DCS =Target.the("All dc number")
            .locatedBy("//tbody[@id='dataTableBody']/tr/td[6]/span");

    public static final Target TENDER_SOURCE_TYPE = Target.the("Tender source type")
            .locatedBy("css:#TNDR_SRCE_TYPE_FLG");

    public static final Target CHARACTERISTIC_TYPE_INPUTBOX = Target.the("Characteristic type inputbox")
            .locatedBy("css:#CHAR_TYPE_CD");
    public static final Target CHARACTERISTIC_VALUE_INPUTBOX = Target.the("Characteristic value inputbox")
            .locatedBy("css:#ADHOC_CHAR_VAL");
    public static final Target CHARACTERISTIC_VALUE_SEARCH_ICON = Target.the("Characteristic value inputbox")
            .locatedBy("css:#IM_CHAR_VAL");
    public static final Target CHARACTERISTIC_VALUE = Target.the("character value")
            .locatedBy("css:#dataTableBody>tr>td:nth-child(2)>span");
    public static final Target CHAR_SEARCH_BUTTON = Target.the("Characteristic type search button")
            .locatedBy("css:#BU_alt2_charSch");
    public static final Target DEPOSIT_CONTROL_STATUS = Target.the("Deposit control status")
            .locatedBy("css:#DEP_CTL_STATUS_FLG");

    public static final Target LIST_OF_DC = Target.the("List of deposit control")
            .locatedBy("//*[@id='dataTableBody']/tr/td[6]");

    public static final Target DC_DATE_TEXT = Target.the("deposit control date text")
            .locatedBy("//*[@id='dataTableBody']/tr/td[1]");

    public static final Target DEPOSIT_CONTROL_ID = Target.the("deposit control id inputbox")
            .locatedBy("css:#DEP_CTL_ID");

    public static final Target DEPOSIT_CONTROL_ID_SEARCH = Target.the("Deposit control id search button")
            .locatedBy("css:#BU_Main_depCtlSrch");

        public static final Target SEARCH_RESULT(String col){

            List list = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                    .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9/]"," ").trim()).collect(Collectors.toList());
            int index = list.indexOf(col);
            return Target.the("Search DC by col name")
                    .locatedBy("css:#dataTableBody>tr>td:nth-child({0})").of(String.valueOf(index+1))
                    ;
        }

    public static final Target SEARCH_RESULT_WITH_SPECIAL_CHAR(String col){
        List list = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9/]"," ").trim()).collect(Collectors.toList());
        int index = list.indexOf(col)+2;
        return Target.the("Search DC by col name")
                .locatedBy("css:#dataTableBody>tr>td:nth-child({0})").of(String.valueOf(index))
                ;
    }

    public static final Target ROW_SEARCH_RESULT(int row,String col){
        List list = TABLE_HEADER.resolveAllFor(theActorInTheSpotlight())
                .stream().map(ele->ele.getAttribute("innerText").replaceAll("[^A-Za-z0-9/]"," ").trim()).collect(Collectors.toList());
        int index = list.indexOf(col)+2;
        return Target.the("Search DC by col name")
                .locatedBy("css:#dataTableBody>tr:nth-child({0})>td:nth-child({1})").of(String.valueOf(row+1),String.valueOf(index))
                ;
    }

    public static final Target TABLE_ROW = Target.the("Table row")
                .locatedBy("css:#dataTableBody>tr");

    public static final Target COL_DATA(int row, int col){
        return Target.the("column wise data")
                .locatedBy("#dataTableBody>tr:nth-child({0})>td:nth-child({1})>span")
                .of(String.valueOf(row),String.valueOf(col));
    }
    public static final Target TENDER_SOURCE_TYPE(int row){
        return Target.the("tender source flag")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$TNDR_SRCE_TYPE_FLG']")
                .of(String.valueOf(row));
    }

    public static final Target DEPOSIT_CONTROL_ID(int row){
        return Target.the("deposit control id")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$DEP_CTL_ID']")
                .of(String.valueOf(row));
    }
    public static final Target DEPOSIT_CONTROL_STATUS(int row){
        return Target.the("deposit control status")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$DEP_CTL_STATUS_FLG']")
                .of(String.valueOf(row));
    }
    public static final Target CREATE_DATE_AND_TIME(int row){
        return Target.the("create date and time")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$CRE_DTTM']")
                .of(String.valueOf(row));
    }
}
