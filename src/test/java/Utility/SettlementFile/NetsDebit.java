package Utility.SettlementFile;


import Utility.CSV.DateUtil;
import Utility.others.PropertiesUtil;
import Utility.Common.StringUtil;
import Utility.Common.FileHandler;
import Utility.others.BatchFile;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static Utility.SettlementFile.MockBankStatement.targetPath;
import static Utility.SettlementFile.MockXml.templatePath;

public class NetsDebit {

    public static void main(String[] args) {
        String str = "Settlement Date/Time:2023-04-27 01:59:07";
        System.out.println("Settlement Date/Time:".concat(getSettlementDate()));
        String str1 = StringUtil.cleanTextContent(" 15-06-2023 15:48:50");
        System.out.println(str1);
        System.out.println(DateUtil.getCustomDate("ddMMyyyy HH:mm:ss","yyyy-MM-dd HH:mm:ss","15062023 15:48:50"));
    }
    public static String mockEnetsDebitSettlement(List<Map<String, String>> testData, Map<String, String> map) {
        String enetsFile = String.valueOf(FileHandler.getFile(templatePath, BatchFile.ENETS_DEBIT_SETTLEMENT));
        List<String> mockupList;
        try {
            BufferedReader br = new BufferedReader(new FileReader(enetsFile));
            List<String> list = br.lines().collect(Collectors.toList());
            mockupList = new ArrayList<>();
            for (String line : list) {
                if (line.startsWith("Settlement Date/Time")) {
                    String[] arr = line.split("Settlement Date/Time:");
                    arr[1] = getSettlementDate();
                    line = "Settlement Date/Time:".concat(arr[1]);
                } else if (line.contains("20230503155600245")) {
                    for (Map<String, String> dataMap : testData) {
                        String[] arr = line.split(",");
                        arr[0] = "   ".concat(dataMap.get("PaymentIntentID"));
                        arr[1] = " ".concat(dataMap.get("PaymentIntentID"));
                        arr[2] = " ".concat(dataMap.get("MerchantID"));
                        arr[3] = " ".concat( DateUtil.getCustomDate("ddMMyyyy HH:mm:ss","yyyy-MM-dd HH:mm:ss"
                                ,StringUtil.cleanTextContent(" ".concat(dataMap.get("CreatedDateAndTime")))));
                        String rightPaddedAmount = StringUtils.rightPad(StringUtil.extractDigit(dataMap.get("Amount")), arr[5].length() - 2);
                        arr[5] = "  ".concat(rightPaddedAmount);
                        line = Arrays.stream(arr).collect(Collectors.joining(","));
                        if (!mockupList.contains(line))
                            mockupList.add(line);
                    }
                }
                mockupList.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String mockedTextFilePath = MockChequeFile.saveMockedFile(mockupList, enetsDebitFileName());
        return mockedTextFilePath;
    }

    private static String getFileNameDate() {

        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String fileDate;
        try {
            date1 = formatter1.parse(PropertiesUtil.getProperties("dc.settlement.date"));
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            fileDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }

    private static String getSettlementDate() {

        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String fileDate;
        try {
            date1 = formatter1.parse(PropertiesUtil.getProperties("dc.settlement.date"));
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            fileDate = targetFormat.format(date1)+" "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }

    public static String enetsDebitFileName(){
        String fileDate = getFileNameDate();
        return targetPath + File.separator+"TxnStatus"+fileDate+"877355000.rpt";
    }
}
