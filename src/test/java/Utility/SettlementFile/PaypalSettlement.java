package Utility.SettlementFile;

import Utility.CSV.CSV;
import Utility.Common.StringUtil;
import Utility.others.PropertiesUtil;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PaypalSettlement {

    public static String paypalTemplateFilePath = System.getProperty("user.dir")
            +"/src/test/resources/template/PAYPAL/";
    public static String paypalMockedFilePath = System.getProperty("user.dir")
            +"/src/test/resources/mockedFile/";

    public static String getFile(String paypalTemplateFilePath, String fileType){

        String targetFile = Arrays.asList(new File(paypalTemplateFilePath).listFiles())
                .stream().filter(file->file.getName().contains(fileType)).findFirst().get().getPath();
        return targetFile;
    }

    private static List<String> readFile(String fileDirectory, String fileType){
        String file = getFile(fileDirectory,fileType);
        List<String> listData = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            listData = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listData;
    }

    private static List<String> getMockedHeader(String fileDirectory, String fileType){

        List<String> listData = readFile(fileDirectory,fileType);
        List<String> mockedDataList = new ArrayList<>();
        for(String str : listData){
            String st = null;
            if(str.contains("RH")){
                String[] arr = str.split(",");
                arr[1] = getNextToSettlementDate().concat(" +0800").replace("\"", "");
                str = Arrays.stream(arr).collect(Collectors.joining(","));
            }else if(str.contains("SH")){
                String[] arr = str.split(",");
                arr[1] = getSettlementDate().concat(" +0800").replace("\"", "");
                arr[2] = getSettlementDate().concat(" +0800").replace("\"", "");
                str = Arrays.stream(arr).collect(Collectors.joining(","));
            }
            mockedDataList.add(str);
        }
        return mockedDataList;
    }

    private static List<String> getMockedCsvBody(String fileDirectory, String fileType){
        List<String> listData = readFile(fileDirectory,fileType);
        return listData.stream().collect(Collectors.toList());
    }

    private static List<String> getMockedFooter(String fileDirectory, String fileType){
        List<String> listData = readFile(fileDirectory,fileType);
        return listData.stream().collect(Collectors.toList());
    }

    public static String getSettlementDate(){

        String dateString = PropertiesUtil.getProperties("dc.settlement.date").replace("‑","-").replaceAll("[^A-Za-z0-9-:/]"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String modifiedDate = null;
        try {
            Date date = formatter.parse(dateString);
            formatter.applyPattern("yyyy/MM/dd HH:mm:ss");
            modifiedDate = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return modifiedDate;
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
            DateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            expectedBankInDate = targetFormat.format(calendar.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }

    public static String saveMockedFile(List<String> mockedData, String fileName) {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)))) {
            mockedData.stream().forEach(t -> {
                try {
                    bw.write(StringUtil.cleanTextContent(t));
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static void savePaypalMockedCsvFile(){
        List<String> headerListData = getMockedHeader(paypalTemplateFilePath,"Header");
        List<String> footerListData = getMockedFooter(paypalTemplateFilePath,"Footer");
        List<String> bodyListData = getMockedCsvBody(paypalMockedFilePath, CSV.PAYPAL_SETTLEMENT_FILE_NAME);
        headerListData.addAll(bodyListData);
        headerListData.addAll(footerListData);
        headerListData.stream().forEach(System.out::println);
        saveMockedFile(headerListData,getFile(MockBankStatement.targetPath,"STL"));
    }
    public static void main(String[] args) {
        String tem = "219800";
        System.out.println(tem.replace("\"",""));
    }
}
