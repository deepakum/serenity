package Utility.SettlementFile;

import Utility.CSV.CSVUtil;
import Utility.Payment.CounterInfo;
import Utility.others.BatchFile;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static Utility.SettlementFile.MockBankStatement.targetPath;
import static Utility.SettlementFile.MockXml.getTemplateFile;
import static Utility.SettlementFile.Nets_pos.deleteMockedFile;
import static Utility.SettlementFile.Nets_pos.getSettlementFileDate;

public class MockChequeFile {

    public static ChequeDetails chequeDetails = new ChequeDetails();

    public static void main(String[] args) {
        System.out.println(extractDigitFromString("S$428.00"));
    }
    public static List<String> MockChequeFile(String bankingDate, String newChequeNo, String bankCode, String branchCode, String amount, String accountNo, String chequeStatusCode) {

        File chequeFile = MockXml.getTemplateFile(CounterInfo.chequeFileName);
        List<String> tempList = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader(chequeFile))){
            String st;
            while((st=br.readLine())!= null){

                String[] arr = st.split("\\s+");
                if(st.startsWith("FILEHDR")) {
                    String tempString = arr[3];
                    st = st.replace(tempString, bankingDate+"D");

                }else if(st.startsWith("CHEQUES")) {

                    String chequeNo = arr[2];
                    String chequeString = chequeNo.replace(chequeNo.subSequence(6, 12), newChequeNo)
                            .replace(chequeNo.subSequence(12, 16), bankCode)
                            .replace(chequeNo.subSequence(16, 19), branchCode)
                            .replace(chequeNo.subSequence(19, 31), String.format("%12s", amount).replace(' ', '0'))
                            .replace(chequeNo.subSequence(31, 33), chequeStatusCode);
                    String oldAccountNo = arr[5];
                    st = st.replace(chequeNo, chequeString).replace(oldAccountNo, accountNo);
                    String checkStatus = arr[3];

                    if(chequeStatusCode.equals("00")) {
                        st = st.replace(checkStatus, "HONOURED");
                    }else {
                        st = st.replace(checkStatus, "DISHONOURED");
                    }

                }else if(st.startsWith("SUMMARY")){

                    String oldAmount = arr[3];
                    st = st.replace(oldAmount, String.format("%20s", amount).replace(' ', '0'));
                }

                tempList.add(st);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(tempList);
    }

    public static String mockCCSettlementFile(List<Map<String, String>> testData, Map<String, String> map) {

        deleteMockedFile(targetPath, BatchFile.CREDIT_CARD);
        File chequeFile = MockXml.getTemplateFile(CounterInfo.creditCardSettlementFileName);
        List<String> tempList = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader(chequeFile))){
            String st;
            int rightPadSpace;
            while((st=br.readLine())!= null){

                String[] arr = st.split("\\|");
                if(st.startsWith("H")) {
                    st = st.replace(arr[4],uobPosFile());
                    st = st.replace(arr[3], getSettlementFileDate());
                    tempList.add(st);
                }else if(st.startsWith("D")) {
                    String tempDataString;
                    for(Map<String, String> tempMap : testData) {
                        int amount = (int) Float.parseFloat(CSVUtil.getTenderAmount(tempMap.get("Amount")))*100;
                        tempDataString = st.replace(arr[13], String.format("%023d", amount));
                        rightPadSpace = arr[17].length()-tempMap.get("PaymentIntentID").length();
                        tempDataString = tempDataString.replace(arr[17], StringUtils.rightPad(tempMap.get("PaymentIntentID"),rightPadSpace,""));
                        rightPadSpace = arr[17].length()-tempMap.get("MerchantID").length();
                        tempDataString = tempDataString.replace(arr[18], StringUtils.rightPad(tempMap.get("MerchantID"),rightPadSpace,""));
                        if(!tempList.contains(tempDataString))
                            tempList.add(tempDataString);
                    }
                }else if(st.startsWith("T")){
                    int totalAmount = (int) Float.parseFloat(CSVUtil.getTenderAmount(map.get("TotaltenderAmount")))*100;
                    st = st.replace(arr[1],String.format("%08d",testData.size()));
                    st = st.replace(arr[2],String.format("%023d",totalAmount));
                    tempList.add(st);
                }

            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        String mockedTextFilePath = MockChequeFile.saveMockedFile(tempList,uobPosFileName());
        return mockedTextFilePath;
    }


    public static String saveMockedFile(List<String> mockedData, String fileName) {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)))) {
            mockedData.stream().forEach(t -> {
                try {
                    bw.write(t);
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

    public static String getChequeNo() {
        return String.valueOf((100000 + new Random().nextInt(900000)));
    }

    public static String getAccountNo() {
        Random rnd = new Random();
        return String.valueOf(10000000000L + ((long)rnd.nextInt(900000000)*100) + rnd.nextInt(100));
    }

    public static String getFileName(String fileName) {
        String updatedFileName = fileName + "." +MockXml.getFileNameDate();
        return updatedFileName;
    }

    public static String uobPosFileName(){
        String date = getSettlementFileDate();
        Calendar cal = Calendar.getInstance();
        String fileDate = date + String.format("%02d",cal.get(Calendar.HOUR))
                + String.format("%02d",cal.get(Calendar.MINUTE)) +
                String.format("%02d",cal.get(Calendar.SECOND));
        return targetPath + File.separator+"UOB_SETT_"+fileDate+"_01.TXT";
    }

    public static String uobPosFile(){
        String date = getSettlementFileDate();
        Calendar cal = Calendar.getInstance();
        String fileDate = date + String.format("%02d",cal.get(Calendar.HOUR))
                + String.format("%02d",cal.get(Calendar.MINUTE)) +
                String.format("%02d",cal.get(Calendar.SECOND));
        return "UOB_SETT_"+fileDate+"_01.TXT";
    }

    private static String getFileNameDate(String dateInString) {

        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date1;
        String fileDate;
        try {
            date1 = formatter1.parse(dateInString);
            DateFormat targetFormat = new SimpleDateFormat("yyMMdd");
            Calendar cal = Calendar.getInstance();
            fileDate = targetFormat.format(date1) + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }

    public static String getMockedFilePath(String text) {
        String[] path = getTemplateFile(text).getName().split(text);
        path[1] = text + "." + MockXml.getFileNameDate();
        return targetPath + File.separator + Arrays.stream(path).collect(Collectors.joining());
    }
    public static String getBankInDate() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
    }

    public static String extractDigitFromString(String amount){
        return amount.replaceAll("[^0-9]+", "");
    }
    public static String getChequeDetails(){
        chequeDetails.setChequeNo(getChequeNo());
        chequeDetails.setAccountNo(getAccountNo());
        chequeDetails.setBankCode(CounterInfo.bankCode);
        chequeDetails.setBranchCode(CounterInfo.branchCode);
        return "1"+"-"+chequeDetails.getChequeNo()+"-"+chequeDetails.getBankCode()+"-"+chequeDetails.getBranchCode()
                +"-"+chequeDetails.getAccountNo();
    }

    public static String extractReferenceIDFromText(String text){
        String regex = "[-]?[0-9]*\\.?[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String DC="";
        if(matcher.find()){
            DC = matcher.group().trim();
        }
        return DC;
    }
}
