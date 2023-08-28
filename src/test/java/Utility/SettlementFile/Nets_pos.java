package Utility.SettlementFile;

import Utility.BatchJob.BatchControl;
import Utility.CCRS.CCBFrames;
import Utility.CCRS.Item;
import Utility.CCRS.User;
import Utility.CSV.DateUtil;
import Utility.DB.DB_Utils;
import Utility.Payment.TenderSource;
import Utility.Payment.TenderType;
import Utility.Server.ServerUtil;
import Utility.Payment.PaymentMode;
import Utility.others.*;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SwitchToNewWindow;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.interactions.Actions;
import pageobjects.*;
import questions.CCRSWebElementText;
import tasks.Batch.Batch;
import tasks.CCB;
import tasks.Navigate;
import tasks.SwitchToWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static Utility.CSV.DateUtil.getDepositControlCreationDate;
import static Utility.SettlementFile.MockXml.getXmlFile;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;
import static org.hamcrest.Matchers.is;

public class Nets_pos {

    public static final String HEADER_FIRST_PARAM = "H";
    public static final String HEADER_SECOND_PARAM = "STDRPT01";
    public static final String HEADER_THIRD_PARAM = "01";
    public static final String HEADER_FOURTH_PARAM = "EFTPOS_CASHCARD";
    public static final String HEADER_FIFTH_PARAM = "N04789";
    public static final String TRANSACTION_FIRST_PARAM = "D";
    public static final String THIRD_PARAM = "Purchase";
    private String billingDate;
    private String settlementDate;
    private String transactionTime;
    private String tenderAmount;
    private String tenderType;
    private String createDateTime;
    private String extReferenceNumber;
    private String netsCashCardNumber;
    private String netsTransactionTime;
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    public static Map<String,String> amountMap = new HashMap<>();
    public static Map<String, String> dcInfoMap = new HashMap<>();
    public static void launchPaymentEvent(String accountID, String paymentEventID){

        theActorInTheSpotlight().attemptsTo(Navigate.toMenu(CashierMenu.Financial.name(),
                CashierSubMenu.PAYMENT_EVENT, SubSubMenu.SEARCH.name()));
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(ccb_payment_event_search_pageobject.PAYMENT_EVENT_INPUTBOX, WebElementStateMatchers.isVisible())
                .then(Enter.keyValues(paymentEventID).into(ccb_payment_event_search_pageobject.PAYMENT_EVENT_INPUTBOX))
                .then(Click.on(ccb_payment_event_search_pageobject.PAYMENT_EVENT_SEARCH_BUTTON)));
        theActorInTheSpotlight().attemptsTo(SwitchToWindow.targetWindow());
        Helper.switchToParentWindow();
    }

    public static void switchToTender(){
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_pageobject.TENDERS_MENU_TAB));
    }

    public static void tenderControl(){

        switchToTender();
        tenderContextMenu();
        Helper.switchToFrames("main");
        launchTenderControlByContextMenu();
        amountMap = tenderAmount();
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.TENDERS_MENU_LABEL));
        clickGetMoreButton();
        Helper.switchAtWindow();
    }

    private static void launchTenderControlByContextMenu() {
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_payment_event_pageobject.GO_TO_TC_CONTROL_MENU.resolveFor(theActorInTheSpotlight())).build().perform();
        action.moveToElement(ccb_payment_event_pageobject.TC_SEARCH_MENU.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Tender Control")));
    }

    private static void tenderContextMenu() {
        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_payment_event_pageobject.TENDER_CONTROL_CONTEXT_MENU));
    }

    public static List<Map<String, String>> getData(String counterType){

        theActorInTheSpotlight().attemptsTo(CCB.logout());
        theActorInTheSpotlight().attemptsTo(CCB.login(User.ADMIN));

        launchPaymentEvent(getAccountID(),getPaymentEventID());
        tenderControl();
        List<Map<String, String>> netPosDataList = new ArrayList<>();
        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        int TransactionCount = ccb_tc_pageobject.TENDEDER_CONTROL_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        for(int row = 0 ; row < TransactionCount ; row++) {
            Map<String, String> map = new HashMap<>();
            String tcCreatedDateAndTime = ccb_dc_search_pageobject.ROW_SEARCH_RESULT(row,"Create Date/Time".toUpperCase())
                    .resolveFor(theActorInTheSpotlight()).getAttribute("innerText")
                    .replace("‑","-").replaceAll("[^A-Za-z0-9:-]"," ").trim();
            String tenderAmount = ccb_dc_search_pageobject.ROW_SEARCH_RESULT(row,"Tender Amount".toUpperCase())
                    .resolveFor(theActorInTheSpotlight()).getAttribute("innerText").trim();
            String tenderType = ccb_dc_search_pageobject.ROW_SEARCH_RESULT(row,"Tender Type".toUpperCase())
                    .resolveFor(theActorInTheSpotlight()).getAttribute("innerText")
                    .replaceAll("[^A-Za-z0-9-:]"," ").trim();
            theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.GO_TO_PAYMENT_EVENT_LABEL(row)));

            map.put("CreatedDateAndTime",tcCreatedDateAndTime);
            map.put("tenderAmount",getTenderAmount(tenderAmount));
            map.put("tenderType",getMockTenderType(tenderType));
            map.put("netsType",getNetsTenderType(tenderType));
            switchToTender();
            map.put("extReferenceNumber",getExtReferenceNumber());
            netPosDataList.add(getNetsCashCardNumber(map));
            if(!netPosDataList.contains(map))
                netPosDataList.add(map);
            clickGoBackButton();
            clickGetMoreButton();
            Helper.switchAtWindow();
            Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        }
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DC_MAIN_MENU_TAB));
        Helper.switchToFrames("main","tabPage");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_pageobject.DEPOSIT_CONTROL_CONTEXT_MENU_BUTTON));
        Helper.switchToFrames("main");
        Actions action = new Actions(getDriver());
        action.moveToElement(ccb_dc_pageobject.GO_TO_DC.resolveFor(theActorInTheSpotlight())).click().perform();
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_user_pageobject.PAGE_TITLE)
                ,is("Deposit Control")));
        Helper.switchToFrames("main","tabPage");
        String DC = ccb_dc_pageobject.DC_NUMBER_TEXTBOX.resolveFor(theActorInTheSpotlight()).getAttribute("value");
        PropertiesUtil.setProperties("deposit.control.id",DC);
        String totalTenderAmount = ccb_dc_pageobject.TOTAL_TENDER_AMOUNT_TEXTBOX.resolveFor(theActorInTheSpotlight()).getText();
        PropertiesUtil.setProperties("dc.endingBalanceAmount",totalTenderAmount);
        Map<String, String> depositControlMap = new HashMap<>();
        depositControlMap.put("TotaltenderAmount",totalTenderAmount);
        dcInfo();
        Helper.switchToFrames("main","tabMenu");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_dc_char_pageobject.CHARACTERISTICS_MENU));
        Helper.switchToFrames("main","tabPage","DEP_CTL_CHAR");
        theActorInTheSpotlight().should(seeThat(CCRSWebElementText.getText(ccb_dc_char_pageobject.CHARACTERISTICS_TYPE_COL)
                ,is("Characteristic Type".toUpperCase())));
        int dcCharRows = ccb_dc_char_pageobject.CHAR_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        for (int k = 0; k < dcCharRows; k++) {
            String charType = ccb_dc_char_pageobject.CHARACTERISTIC_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("Settlement Date")) {
                String settlementDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                PropertiesUtil.setProperties("dc.settlement.date",settlementDate);
            }else if (charType.equalsIgnoreCase("Expected Bank In Date")) {
                String bankInDate = ccb_dc_char_pageobject.CHAR_DATE_VALUE(k).resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
                PropertiesUtil.setProperties("dc.expectedBankInDate",bankInDate);
            }
        }
        Helper.switchToFrames(CCBFrames.MAIN);
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_user_pageobject.HOME_MENU_BUTTON));
        return netPosDataList;
    }

    private static String getAccountID() {
        String accountID = null;

        if(PropertiesUtil.getProperties("sale.item.type").equalsIgnoreCase(Item.NOTICE)) {
            if(!PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CASH)) {
                accountID = environmentVariables.getProperty("CCRS.stripe.account.id");
            }
        }
        System.out.println("account ID : "+accountID);
        return accountID;
    }

    private static String getPaymentEventID() {
        String paymentEventID;
        if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase(TenderSource.COUNTER)){
            paymentEventID = PropertiesUtil.getProperties("CCRS.paymentEventID");
        }else{
            paymentEventID = PropertiesUtil.getProperties("portal.paymentEventID");
        }
        System.out.println("payment event ID : "+paymentEventID);
        return paymentEventID;
    }

    public static Map<String, String> dcInfo(){
        String dcInfoString = CCB.getText(ccb_dc_pageobject.DC_STATUS_INFO);
        List<String> info = Arrays.asList(dcInfoString.split(","));
        dcInfoMap.put("dcDate",info.get(0));
        dcInfoMap.put("tenderType",info.get(1));
        dcInfoMap.put("status",info.get(2));
        System.out.println("dcInfoMap : "+dcInfoMap);
        return dcInfoMap;
    }

    private static Map<String, String> tenderAmount(){
        Map<String, String> tenderMap = new HashMap<>();
        Helper.switchToFrames("main","tabPage","blncGrid");
        int TransactionCount = ccb_tc_pageobject.TENDEDER_CONTROL_LIST.resolveAllFor(theActorInTheSpotlight()).size();
        for(int row = 0 ; row < TransactionCount ; row++) {
            Map<String, String> map = new HashMap<>();
            String tenderAmount = ccb_dc_search_pageobject.ROW_SEARCH_RESULT(row, "Tender Total Amount")
                    .resolveFor(theActorInTheSpotlight()).getAttribute("innerText").trim();
            String tenderType = ccb_dc_search_pageobject.ROW_SEARCH_RESULT(row, "Tender Type")
                    .resolveFor(theActorInTheSpotlight()).getAttribute("innerText")
                    .replaceAll("[^A-Za-z0-9-:]", " ").trim();
            tenderMap.put(tenderType,tenderAmount);
            //theActorInTheSpotlight().attemptsTo(Click.on(TC_PageObject.GO_TO_PAYMENT_EVENT_LABEL(row)));
        }
            return tenderMap;
    }
    private static String getTenderAmount(String tenderAmount){
        return getTenderIntValue(tenderAmount.replaceAll("[^0-9.]",""));
    }

    public static String getExpectedBankInDate() {

        DateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String expectedBankInDate;
        try {
            date1 = formatter1.parse(PropertiesUtil.getProperties("dc.expectedBankInDate"));
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            expectedBankInDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }

    public static String getNextToSettlementDate() {

        DateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String expectedBankInDate;
        try {
            date1 = formatter1.parse(PropertiesUtil.getProperties("dc.settlement.date"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, 1);
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            expectedBankInDate = targetFormat.format(calendar.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }

    public static String getCCSettlementDate() {
        String transactionDate = DateUtil.getBusinessDateForCreditCardSettlement();
        DateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
        Date date1;
        String expectedBankInDate;
        try {
            date1 = formatter1.parse(transactionDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DATE,1);
            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            expectedBankInDate = targetFormat.format(calendar.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }

    private static Calendar excludingSunday(Calendar calendar){
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            System.out.println("=====");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar;
    }
    private static String getTenderIntValue(String tenderAmount){
        Double temp = Double.parseDouble(tenderAmount);
        return String.valueOf(temp.intValue());
    }
    private static String getMockTenderType(String tenderType){
        if(tenderType.contains("NETS"))
            tenderType = "EFT POS";

        return tenderType.trim().toUpperCase().replace(" ","");
    }

    private static String getNetsTenderType(String tenderType){
        if(tenderType.contains("NETS"))
            tenderType = "Unknown";

        return "NETS" + " "+tenderType.trim().replace(" ","");
    }

    public static void clickGoBackButton(){
        Helper.switchToFrames("main");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.HISTORY_GOBACK_BUTTON));
    }

    public static void clickGetMoreButton(){
        Helper.switchToFrames("main","tabPage","tenderGrid_tndrGrid");
        theActorInTheSpotlight().attemptsTo(Click.on(ccb_tc_pageobject.GET_MORE_BUTTON));
    }
    public static String settlementHeader(){
        List<String> header = new ArrayList<>();
        header.add(HEADER_FIRST_PARAM);
        header.add(HEADER_SECOND_PARAM);
        header.add(HEADER_THIRD_PARAM);
        header.add(HEADER_FOURTH_PARAM);
        header.add(HEADER_FIFTH_PARAM);
        header.addAll(DateUtil.getNetPosHeaderDateTime());

        return header.stream().collect(Collectors.joining(","));
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getTenderAmount() {
        return tenderAmount;
    }

    public void setTenderAmount(String tenderAmount) {
        this.tenderAmount = tenderAmount;
    }

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    public static String getExtReferenceNumber() {
            Helper.switchToFrames("main","tabPage");
            String extReferenceNumber = ccb_payment_event_pageobject.EXT_REFERENCE_ID_TEXTBOX.
                    resolveFor(theActorInTheSpotlight()).getAttribute("value").trim();
            return extReferenceNumber;
    }

    public void setExtReferenceNumber(String extReferenceNumber) {
        this.extReferenceNumber = extReferenceNumber;
    }

    public static Map<String, String> getNetsCashCardNumber(Map<String, String> map) {
        Helper.switchToFrames("main","tabPage","PY_TNDR_CHAR");
        int rows = CCB.getSize(ccb_payment_event_pageobject.CHARACTER_TYPE_LIST);
        String netsCashCardNumber = null;
        String netsTransactionTime = null;
        for (int k = 0; k < rows; k++) {
            String charType = ccb_payment_event_pageobject.PAYMENT_EVENT_CHARACTER_TYPE(k).resolveFor(theActorInTheSpotlight()).getSelectedVisibleTextValue().trim();
            if (charType.equalsIgnoreCase("NETS Cash Card Number")) {
                netsCashCardNumber = CCB.getTextUsingAttribute(
                        ccb_payment_event_pageobject.PAYMENT_EVENT_CHARACTER_VALUE(k),"value");
                map.put("netsCashCardNumber",netsCashCardNumber);
            }else if (charType.equalsIgnoreCase("NETS Transaction Time")) {
                netsTransactionTime = CCB.getTextUsingAttribute(
                        ccb_payment_event_pageobject.PAYMENT_EVENT_CHARACTER_VALUE(k),"value");
                map.put("netsTransactionTime",netsTransactionTime);
            }
        }
        return map;
    }

    public void setNetsCashCardNumber(String netsCashCardNumber) {
        this.netsCashCardNumber = netsCashCardNumber;
    }

    public String getNetsTransactionTime() {
        return netsTransactionTime;
    }

    public void setNetsTransactionTime(String netsTransactionTime) {
        this.netsTransactionTime = netsTransactionTime;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public static String getSettlementFileDate() {
        DateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String settlementDate;
        try {
            date1 = formatter1.parse(PropertiesUtil.getProperties("dc.settlement.date"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            settlementDate = targetFormat.format(cal.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return settlementDate;
    }

    public static String getBankSettlementFileDate() {
        DateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String settlementDate;
        try {
            date1 = formatter1.parse(PropertiesUtil.getProperties("dc.settlement.date"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
            }else if (PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.QR_CODE)) {
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            settlementDate = targetFormat.format(cal.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return settlementDate;
    }

    public static String getCreditCardBankSettlementFileDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        try {
            date = sdf.parse(getDepositControlCreationDate());
            calendar.setTime(date);
            calendar.add(Calendar.DATE,1);
            if (PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            calendar.add(Calendar.DATE,1);
            if (PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            calendar.add(Calendar.DATE,-1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf1.format(calendar.getTime());
        return formattedDate;
    }

    public static void main(String[] args) {
        System.out.println(getCreditCardBankSettlementFileDate());
    }
    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public static void mockSettlementFile(){
        List<Map<String, String>> listMap = Nets_pos.getData(PaymentMode.COUNTER);
        List<String> mockedList = new ArrayList<>();
        mockedList.add(settlementHeader());

        for(Map map : listMap){
            String entry = "";
            String dateTime;
            String dateTimeWithMicroSeconds;
            if(!map.get("tenderType").equals("EFTPOS")) {
                dateTime = map.get("netsTransactionTime").toString().trim()+".".concat("999");
                dateTimeWithMicroSeconds = dateTime + ".".concat(getRandomThreeDigits());
                entry += Nets_pos.TRANSACTION_FIRST_PARAM;
                entry += "," + map.get("tenderType");
                entry += "," + Nets_pos.THIRD_PARAM;
                entry += "," + dateTime;
                entry += "," + map.get("netsType");
                entry += padWithblankSpace(2);
                entry += "11136168900";
                entry += "," + "011110361689";
                entry += "," + "36168902";
                entry += padWithblankSpace(2);
                entry += Integer.parseInt(map.get("tenderAmount").toString())*100;
                entry += "," + "0";
                entry += padWithblankSpace(4);
                entry += "0";
                entry += padWithblankSpace(5);
                entry += dateTime;
                entry += padWithblankSpace(6);
                entry += "N";
                entry += "," + "01";
                entry += padWithblankSpace(6);
                entry += map.get("netsCashCardNumber");
                entry += padWithblankSpace(1);
                entry += "0";
                entry += padWithblankSpace(2);
            }else{
                dateTime = getTransactionDateTimeForNets(map.get("CreatedDateAndTime").toString()).trim()+".".concat("999");
                dateTimeWithMicroSeconds = dateTime + ".".concat(getRandomThreeDigits());
                entry += Nets_pos.TRANSACTION_FIRST_PARAM;
                entry += "," + map.get("tenderType");
                entry += "," + Nets_pos.THIRD_PARAM;
                entry += "," + dateTime;
                entry += "," + "Unknown";
                entry += "," + "";
                entry += "," + "11136168900";
                entry += "," + "";
                entry += "," + "36168902";
                entry += "," + "";
                entry += "," + Integer.parseInt(map.get("tenderAmount").toString())*100;
                entry += "," + "0";
                entry += padWithblankSpace(4);
                entry += "0";
                entry += padWithblankSpace(5);
                entry += dateTime;
                entry += padWithblankSpace(6);
                entry += "N";
                entry += "," + "10";
                entry += padWithblankSpace(7);
                entry += "158";
                entry += ","+map.get("extReferenceNumber");
                entry += "," + "N";
            }
            if(!mockedList.contains(entry))
                mockedList.add(entry);
        }
        mockedList.add("T"+"," + (mockedList.size()-1));
        saveMockedFile(mockedList);
    }

    public static String padWithblankSpace(int count) {
        String temp = "";
        for(int i=0 ;i<count ; i++){
            temp+=",";
        }
        return temp;
    }

    public static String netsPosFileName(){
        return BatchFile.NETS_POS+"_"+ getSettlementFileDate()+"_NEW.csv";
    }
    public static String getTransactionDateTimeForNets(String createDateTime){
        return createDateTime.replace(" ","T");
    }

    private static void saveMockedFile(List<String> dataList){
        String targetPath = System.getProperty("user.dir")+"/src/test/resources/mockedFile/";
        FileOutputStream fout = null;
        try {
            deleteMockedFile(targetPath,BatchFile.NETS_POS);
            fout = new FileOutputStream(targetPath+netsPosFileName());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String str : dataList) {
            try {
                fout.write((str+"\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void deleteMockedFile(String mockedDirectory, String fileType){
        File file = new File(mockedDirectory);
        for(File f : file.listFiles()){
            if(!f.isDirectory()){
                System.out.println(f);
                if(f.getName().contains(fileType))
                    f.delete();
            }
        }
    }

    public static void settlementBatch(){
        String source = getXmlFile(BatchFile.NETS_POS);
        String destination = environmentVariables.getProperty("CCRS.EVMS.NETS.CSV.server.path");
        ServerUtil.uploadFile(source,destination,true,".csv");
        theActorInTheSpotlight().attemptsTo(Batch.toSubmit(BatchControl.NETS,true));
        String archivePath = environmentVariables.getProperty("CCRS.EVMS.NETS.CSV.archive.path");
        DB_Utils.editNetPosSettlementFileStatus(netsPosFileName());
        ServerUtil.editArchivedFile(archivePath,netsPosFileName(),netsPosFileName().split("NEW")[0]+getCurrentTime()+"_NEW.csv");
    }

    private static String getCurrentTime(){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return timeFormat.format(date).replace(":","");
    }

    public static String getRandomThreeDigits(){
        // initialize a Random object somewhere; you should only need one
        Random random = new Random();
        return String.valueOf(random.nextInt(900) + 100);
    }

}
