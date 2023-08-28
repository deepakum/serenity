package Utility.SettlementFile;

import Utility.ABT.ABTFile;
import Utility.ABT.CmGenerateChecksumBatch;
import Utility.CCRS.CCBFrames;
import Utility.CSV.DateUtil;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Switch;
import pageobjects.ccb_abt_pageobject;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ABT {

    public static List<String> writeOffList = new ArrayList<>();
    static List<String> AbtFileList = new ArrayList<>();
    static Double totalAmount;
    static List<String> ackDateList = new ArrayList<>();
    public static String mockABTFile(String fileType) throws Exception {

        File abtTemplateFile = Arrays.stream(new File(ABTFile.TEMPLATE_PATH).listFiles()).filter(file->file.getName().contains(fileType)).findFirst().get();
        String line =null;
        //List<String> tempList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(abtTemplateFile))) {
            while ((line = br.readLine()) != null) {
                AbtFileList.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return writeMockedFile(AbtFileList,fileType);
    }

    public static String mockACK1File(String fileType,String trailerLine,String baseFileName) throws Exception {

        File abtTemplateFile = Arrays.stream(new File(ABTFile.TEMPLATE_PATH).listFiles()).filter(file->file.getName().contains(fileType)).findFirst().get();
        String line =null;
        List<String> tempList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(abtTemplateFile))) {
            while ((line = br.readLine()) != null) {
                if(line.startsWith("HEADER")){
                    ackDateList = DateUtil.getAck1Date();
                    String[] temp = line.split(",");
                    temp[2] = ackDateList.get(0);
                    temp[4] = "SGPAY"+"-"+ ackDateList.get(1)+"-001";
                    line = Arrays.stream(temp).collect(Collectors.joining(","));
                }else if(line.startsWith("TRAILER")){
                    line = trailerLine;
                }
                tempList.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return saveMockedACKFile(tempList,ackDateList.get(1),baseFileName);
    }

    public static String mockACK2File(String fileType,List<String> trailerLine,String baseFileName) throws Exception {

        File abtTemplateFile = Arrays.stream(new File(ABTFile.TEMPLATE_PATH).listFiles()).filter(file->file.getName().contains(fileType)).findFirst().get();
        String line =null;
        List<String> tempList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(abtTemplateFile))) {
            while ((line = br.readLine()) != null) {
                if(line.startsWith("HEADER")){
                    ackDateList = DateUtil.getAck1Date();
                    String[] temp = line.split(",");
                    temp[2] = "SGPAY"+"-"+ ackDateList.get(1)+"-001";
                    temp[3] = ackDateList.get(0);
                    line = Arrays.stream(temp).collect(Collectors.joining(","));
                }
                tempList.add(line);
            }
            tempList.addAll(trailerLine);
        }catch (Exception e){
            e.printStackTrace();
        }
        return saveMockedACK2File(tempList,ackDateList.get(1),baseFileName,fileType);
    }
    public static String writeMockedFile(List<String> tempList, String fileType) throws Exception {

        List<String> abtDataList = new ArrayList<>();
        String transactionDate = getTransactionDate();
        totalAmount = 0.0;
        try(FileOutputStream fout = new FileOutputStream(ABTFile.MOCKED_FILE_PATH+File.separator+fileType+transactionDate+"."+ABTFile.FILE_TYPE)) {

            for(int i=0;i<tempList.size();i++){
                String line = tempList.get(i);
                String temp = updateDate(line,fileType);
                totalAmount += getAmount(line,fileType);
                writeOffList.add(getServiceID(line).intValue()+"_"+getPackageID(line).intValue());
                if(i==tempList.size()-1) {
                    fout.write((temp).getBytes());
                }else{
                    fout.write((temp + "\n").getBytes());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        abtDataList.add(fileType+transactionDate+"."+ABTFile.FILE_TYPE);
        abtDataList.add(transactionDate);
        abtDataList.add(String.valueOf(tempList.size()));
        DecimalFormat df = new DecimalFormat("####0.00");
        abtDataList.add(String.valueOf(df.format(totalAmount)));
        abtDataList.add(CmGenerateChecksumBatch.checkSHAABT(ABTFile.MOCKED_FILE_PATH,fileType));
        return mockedCsfFile(abtDataList,ABTFile.CSF_FILE);
    }

    private static String mockedCsfFile(List<String> tempList, String fileType) throws Exception {

        String transactionDate = getTransactionDate();
        String filePath = ABTFile.MOCKED_FILE_PATH+File.separator+fileType+transactionDate+"."+ABTFile.FILE_TYPE;
        try(FileOutputStream fout = new FileOutputStream(filePath)) {
            fout.write((tempList.stream().collect(Collectors.joining(","))).getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        return filePath;
    }

    private static String saveMockedACKFile(List<String> tempList, String fileDate, String baseFileName) throws Exception {
        String mockedFilePath = ABTFile.MOCKED_FILE_PATH+File.separator+baseFileName+".D"+fileDate+".ACK1";
        try(FileOutputStream fout = new FileOutputStream(mockedFilePath)) {
            fout.write((tempList.stream().collect(Collectors.joining("\n"))).getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        return mockedFilePath;
    }

    private static String saveMockedACK2File(List<String> tempList, String fileDate, String baseFileName,String ext) throws Exception {
        String mockedFilePath = ABTFile.MOCKED_FILE_PATH+File.separator+baseFileName+".D"+fileDate+"."+ext;
        try(FileOutputStream fout = new FileOutputStream(mockedFilePath)) {
            fout.write((tempList.stream().collect(Collectors.joining("\n"))).getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        return mockedFilePath;
    }

    private static String updateDate(String line,String fileType){
        String[] tempLineList = line.split(",");
        if(fileType.equalsIgnoreCase(ABTFile.SSF_FILE)){
            tempLineList[0] = getAbtSettlementDate();
            tempLineList[5] = getAbtSettlementDate();
        }else {
            tempLineList[0] = getAbtSettlementDate();
        }
        return Arrays.asList(tempLineList).stream().collect(Collectors.joining(","));
    }

    private static Double getAmount(String line, String fileType){
        String[] tempLineList = line.split(",");
        Double amount = 0.0;
        if(fileType.equalsIgnoreCase(ABTFile.RSF_FILE)) {
            amount = Double.parseDouble(tempLineList[12]);
        }else if(fileType.equalsIgnoreCase(ABTFile.SSF_FILE)){
            amount = Double.parseDouble(tempLineList[6]);
        }else if(fileType.equalsIgnoreCase(ABTFile.CTSF_FILE)){
            amount = Double.parseDouble(tempLineList[3]);
        }else if(fileType.equalsIgnoreCase(ABTFile.PPSSF_FILE) || fileType.equalsIgnoreCase(ABTFile.BCSF_FILE)){
            amount = Double.parseDouble(tempLineList[5]);
        }else{
            amount = Double.parseDouble(tempLineList[4]);
        }
        return amount;
    }
    private static Double getPackageID(String line){
        String[] tempLineList = line.split(",");
        return Double.parseDouble(tempLineList[2]);
    }
    private static Double getServiceID(String line){
        String[] tempLineList = line.split(",");
        return Double.parseDouble(tempLineList[1]);
    }
    public static String getTransactionDate() {

        String fileDate;
        try {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            fileDate = targetFormat.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }

    public static String getCreationDate() {

        String fileDate;
        try {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            fileDate = targetFormat.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }
    private static String getAbtSettlementDate() {

        String fileDate;
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-15);
            Date date = cal.getTime();
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            fileDate = targetFormat.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }
    public static final Task setSSFBatchParameters(String businessSystem, boolean invoiceFlag){
        return Task.where("set abt settlement type",
                Switch.toFrame(CCBFrames.TAB_PAGE),
                Switch.toFrame(CCBFrames.BJP),
                Enter.theValue(businessSystem).into(ccb_abt_pageobject.BUSINESS_SYSTEM_INPUTBOX),
                Enter.theValue(String.valueOf(invoiceFlag)).into(ccb_abt_pageobject.ABT_SETTLEMENT_TYPE_INPUTBOX),
                Switch.toDefaultContext());
    }

    public static String getAbtFileName(String batchFile){
        return ABTFile.MOCKED_FILE_PATH+File.separator+ batchFile +getTransactionDate()+"."+ABTFile.FILE_TYPE;
    }

    public static void main(String[] args) throws Exception {
    }
}
