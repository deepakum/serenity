package Utility.SettlementFile;

import Utility.Payment.TenderType;
import Utility.others.BatchFile;
import Utility.others.PropertiesUtil;
import io.opentelemetry.api.internal.StringUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static Utility.SettlementFile.Nets_pos.deleteMockedFile;

public class MockBankStatement {

    static String dcID = "4786532738";
    static String bankTemplateFilePath = System.getProperty("user.dir")+"/src/test/resources/template/bankStatementTemplate.txt";
    static String stripeTemplateFilePath = System.getProperty("user.dir")+"/src/test/resources/template/stripeBankStatementTemplate.txt";
    static String girobTemplateFilePath = System.getProperty("user.dir")+"/src/test/resources/template/giroBankStatementTemplate.txt";
    static String braintreeTemplateFilePath = System.getProperty("user.dir")+"/src/test/resources/template/braintreeBankStatementTemplate.txt";
    static String sDate1="30/01/2023";
    static String balancedAmout = "S$2,286.85";
    public static String targetPath = System.getProperty("user.dir")+"/src/test/resources/mockedFile/";
    public static String abtTargetPath = System.getProperty("user.dir")+"/src/test/resources/mockedFile/ABT/";

    public static String counterPayment(String bankInDate, String amount, String dcID) {

        String bankStatementDate = getSettlementBusinessDate(bankInDate);
        String mockedFilePath = targetPath + getFileName(bankInDate);
        deleteMockedFile(targetPath, BatchFile.BANK_SETTLEMENT);
        String amt = amount.substring(2).replace(",","").replace(".",",");
        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(bankTemplateFilePath)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":61")) {
                    String newSt = ":61:"+bankStatementDate+"CD"+amt+"NMSCNONREF";
                    st = newSt;
                } else if (st.contains("CASH DEPOSIT")) {
                    String[] splitArr = st.split("OTH?");
                    splitArr[1] = dcID;
                    st = splitArr[0] + splitArr[1];
                }
                tempList.add(st);
            }
            //tempList.stream().forEach(System.out::println);
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }

    public static String stripePayment(String bankInDate, String amount) {

        String customDate = getSettlementBusinessDate(bankInDate);
        String mockedFilePath = targetPath + getFileName(bankInDate);
        String amt = amount.substring(2).replace(",","").replace(".",",");
        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(stripeTemplateFilePath)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":61")) {
                    String newSt = ":61:"+customDate+"CD"+amt+"NTRFSTRIPE Collections";
                    st = newSt;
                }
                tempList.add(st);
            }
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }

    public static String giroPayment(List<Map<String,String>> listOfmap) {

        String customDate = getGiroSettlementBusinessDate(PropertiesUtil.getProperties("dc.expectedBankInDate"));
        String mockedFilePath = targetPath + getAbtSettlementFileName(PropertiesUtil.getProperties("dc.expectedBankInDate"));
        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(girobTemplateFilePath)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":mock-data")) {
                    for(Map<String,String> map : listOfmap){
                        String temp = null;
                        System.out.println("map.get() : "+map.get("amount"));
                        String amt = map.get("amount").replace(",","").replace(".",",");
                        temp = ":61:"+customDate+"CD"+amt+"NDDTNONREF//"+map.get("serviceProviderID");
                        temp += "\n"+"165?132204";
                        temp += "\n"+":86:TD?GIRO COLLECTION?OTH2?"+getGIRODate()+" "+map.get("serviceProviderID");
                        st = temp;
                        if(!tempList.contains(st))
                            tempList.add(st);
                    }
                }
                if(!tempList.contains(st))
                    tempList.add(st);
            }
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }
    public static String braintreePayment(String bankInDate, String amount) {

        String customDate = getSettlementBusinessDate(bankInDate);
        String mockedFilePath = targetPath + getFileName(bankInDate);
        String amt = amount.substring(2).replace(",","").replace(".",",");
        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(braintreeTemplateFilePath)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":61")) {
                    String newSt = ":61:"+customDate+"CD"+amt+"NTRFFIRST DATA MERCHANT SOLUTIONS PRIVA";
                    st = newSt;
                }
                tempList.add(st);
            }
            //tempList.stream().forEach(System.out::println);
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }

    public static String netsPosPayment(String bankInDate, List<String> amountList) {
        String netPosBankStatementFile = System.getProperty("user.dir")+"/src/test/resources/template/BankStatement/netPosBankStatement.txt";
        String customDate = getSettlementBusinessDate(bankInDate);
        String mockedFilePath = targetPath + getFileName(bankInDate);

        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(netPosBankStatementFile)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":mock-data")) {
                    String newSt = "";
                    String amt = PropertiesUtil.getProperties("dc.endingBalanceAmount").substring(2).replace(",", "").replace(".", ",");
                    newSt += ":61:" + customDate + "CD" + amt + "NTRFLAND TRANS//1689"+"\n" + "120?055331";
                    st = newSt;
                }
                tempList.add(st);
            }
            tempList.stream().forEach(System.out::println);
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }

    public static String counterCCBankStatement(String bankInDate, String amount) {
        String netPosBankStatementFile = System.getProperty("user.dir")+"/src/test/resources/template/BankStatement/counter_cc_bankstatement.txt";
        String customDate = getSettlementBusinessDate(bankInDate);
        String mockedFilePath = targetPath + getFileName(bankInDate);

        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(netPosBankStatementFile)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":mock-data")) {
                    String newSt = "";
                    String amt = PropertiesUtil.getProperties("dc.endingBalanceAmount").substring(2).replace(",", "").replace(".", ",");
                    newSt += ":61:" + customDate + "CD" + amt + "NTRF2021052100000306"+"\n" + "142?235959";
                    st = newSt;
                }
                tempList.add(st);
            }
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }
    public static String paypalPayment(String bankInDate, String amount) {
        String netPosBankStatementFile = System.getProperty("user.dir")+"/src/test/resources/template/BankStatement/portalPaypalBankStatement.txt";
        String customDate = getSettlementBusinessDate(bankInDate);
        String mockedFilePath = targetPath + getFileName(bankInDate);

        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(netPosBankStatementFile)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":mock-data")) {
                    String newSt = "";
                    String amt = PropertiesUtil.getProperties("dc.endingBalanceAmount").substring(2).replace(",", "").replace(".", ",");
                    newSt += ":61:" + customDate + "CD" + amt + "NTRF5K222ARWKJQBE"+"\n";
                    st = newSt;
                }
                tempList.add(st);
            }
            tempList.stream().forEach(System.out::println);
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }

    public static String enetsDebitPayment(String bankInDate, String amount) {
        String netPosBankStatementFile = System.getProperty("user.dir")+"/src/test/resources/template/BankStatement/enetsDebitBankStatement.txt";
        String customDate = getSettlementBusinessDate(bankInDate);
        String mockedFilePath = targetPath + getEnetsFileName(bankInDate);

        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(netPosBankStatementFile)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":mock-data")) {
                    String newSt = "";
                    String amt = PropertiesUtil.getProperties("dc.endingBalanceAmount").substring(2).replace(",", "").replace(".", ",");
                    newSt += ":61:" + customDate + "CD" + amt + "NTRFMID XXXXXXXXXX"+"\n";
                    st = newSt;
                }
                tempList.add(st);
            }
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                fout.write((str+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }
    public static String counterQRCodePayment(String bankInDate, List<Map<String,String>> amountList) {
        String netPosBankStatementFile = System.getProperty("user.dir")+"/src/test/resources/template/BankStatement/counterPayNowBankStatement.txt";
        String customDate = getQRCodExpectedBankInDate(bankInDate);
        String mockedFilePath = targetPath + getPayNowFileName(bankInDate);

        List<String> tempList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(netPosBankStatementFile)))){
            String st;
            while((st=br.readLine())!= null){
                if(st.startsWith(":mock-data")) {
                    String newSt = "";
                    int imbNo = 699;
                    for(Map<String, String> map : amountList) {
                        imbNo += 1;
                        String amt = map.get("tenderAmount").replace(".", ",");
                        if(!amt.contains(","))
                            amt = amt.concat(",00");
                        newSt += ":61:" + customDate + "CD" + amt + "NMSCNONREF//IMB1177404207"+String.valueOf(imbNo)+"\n" + "201?182226";
                        newSt += "\n"+":86:TD?Inward PayNow?CREF?"+map.get("extReferenceNumber")+"?ORDP?SENDER NAME1?OB?DBS BA\n" +
                                "NK?BC?DBSSSGSGXXX?REMI?OTHR "+map.get("extReferenceNumber")+"\n";
                    }
                    st = newSt;
                }
                tempList.add(st);
            }
            tempList.stream().map(line->line.length()>0).collect(Collectors.toList());
            FileOutputStream fout = new FileOutputStream(mockedFilePath);
            for (String str : tempList) {
                String tempString = str.trim();
                fout.write((tempString+"\n").getBytes());
            }
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }
    public static String getSettlementBusinessDate(String dateInString) {

        DateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String batchBusinessDate;
        try {
            date1 = formatter1.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            cal.add(Calendar.DATE, 1);
            Date date2 = cal.getTime();
            DateFormat targetFormat = new SimpleDateFormat("yyMMddMMdd");
            batchBusinessDate = targetFormat.format(date2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return batchBusinessDate;
    }

    public static String getGiroSettlementBusinessDate(String dateInString) {

        DateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String batchBusinessDate;
        try {
            date1 = formatter1.parse(dateInString);
            DateFormat targetFormat = new SimpleDateFormat("yyMMddMMdd");
            batchBusinessDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return batchBusinessDate;
    }

    public static String getQRCodExpectedBankInDate(String dateInString) {

        DateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String expectedBankInDate;
        try {
            date1 = formatter1.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            DateFormat targetFormat = new SimpleDateFormat("yyMMddMMdd");
            expectedBankInDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }

    private static String getFileNameDate(String dateInString) {

        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String fileDate;
        try {
            date1 = formatter1.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            cal.add(Calendar.DATE, 1);
            Date date2 = cal.getTime();
            cal = Calendar.getInstance();
            DateFormat targetFormat = new SimpleDateFormat("yyMMdd");
            fileDate = targetFormat.format(date2) + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }

    private static String getABTFileNameDate(String dateInString) {

        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String fileDate;
        try {
            date1 = formatter1.parse(dateInString);
            Calendar cal = Calendar.getInstance();
//            cal.setTime(date1);
//            cal.add(Calendar.DATE, 1);
//            Date date2 = cal.getTime();
            DateFormat targetFormat = new SimpleDateFormat("yyMMdd");
            Calendar calendar = Calendar.getInstance();
            fileDate = targetFormat.format(date1) + calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }
    private static String getQRCodeFileNameDate(String dateInString) {

        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String fileDate;
        try {
            date1 = formatter1.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            DateFormat targetFormat = new SimpleDateFormat("yyMMdd");
            cal = Calendar.getInstance();
            fileDate = targetFormat.format(date1) + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }
    private static String getFileName(String bankInDate) {
        String fileName = "SLTA03XXXXXX.CASP_MT940.D220512000001";
        String[] fArray = fileName.split("MT940.D");
        fileName = fArray[0] + "MT940.D" + getFileNameDate(bankInDate)+".txt";
        return  fileName;
    }

    private static String getEnetsFileName(String bankInDate) {
        String fileName = "SLTA03XXXXXX.CASP_MT940.D220512000001";
        String[] fArray = fileName.split("MT940.D");
        fileName = fArray[0] + "MT940.D" + getABTFileNameDate(bankInDate)+".txt";
        return  fileName;
    }
    private static String getAbtSettlementFileName(String bankInDate) {
        String fileName = "SLTA03XXXXXX.CASP_MT940.D220512000001";
        String[] fArray = fileName.split("MT940.D");
        fileName = fArray[0] + "MT940.D" + getABTFileNameDate(bankInDate)+".txt";
        return  fileName;
    }
    private static String getGIRODate(){
        String dateInString = PropertiesUtil.getProperties("dc.settlement.date");
        System.out.println(dateInString);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar;
        try {
            Date date = sdf.parse(dateInString);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return  StringUtils.padLeft(String.valueOf(calendar.get(Calendar.DATE)),2) +" "+StringUtils.padLeft(String.valueOf(calendar.get(Calendar.MONTH)+1),2)+" "+calendar.get(Calendar.YEAR);
    }
    private static String getPayNowFileName(String bankInDate) {
        String fileName = "LTAAGD01XXXX.CASP_MT940.D230424000102";
        String[] fArray = fileName.split("MT940.D");
        fileName = fArray[0] + "MT940.D" + getQRCodeFileNameDate(bankInDate)+".txt";
        return  fileName;
    }
    public static void main(String[] args) {
        System.out.println(getGIRODate());
    }
}
