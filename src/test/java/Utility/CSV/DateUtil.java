package Utility.CSV;

import Utility.Common.StringUtil;
import Utility.Payment.TenderType;
import Utility.SettlementFile.Nets_pos;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.opentelemetry.api.internal.StringUtils;
import tasks.Batch.EditBatchJobData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DateUtil {

    static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    public static String tenderType;
    public static String getCreatedDate(String createdDate){

        String dateString = createdDate.replace("‑","-").replaceAll("[^A-Za-z0-9-:]"," ");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        String created = null;
        try {
            date = formatter.parse(dateString);
            formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
            created = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return created;
    }

    public static String getCreatedUTC(String createdDate){

        String dateString = createdDate.replace("‑","-").replaceAll("[^A-Za-z0-9-:]"," ");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        String createdUTC = null;
        try {
            date = formatter.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR,8);
            Date modifiedDate = cal.getTime();
            formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
            createdUTC = formatter.format(modifiedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return createdUTC;
    }

    public static String getBusinessDateForCreditCardSettlement(String settlementDate){

        String dateString = settlementDate.replace("‑","-").replaceAll("[^A-Za-z0-9-:/]"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String modifiedDate = null;
        try {
            Date date = formatter.parse(dateString);
            formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
            modifiedDate = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return modifiedDate;
    }
    public static String getEffectiveAtUTC(String settlementDate){

        String dateString = settlementDate.replace("‑","-").replaceAll("[^A-Za-z0-9-:/]"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = null;
        String createdUTC = null;
        try {
            date = formatter.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR,0);
            Date modifiedDate = cal.getTime();
            formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
            createdUTC = formatter.format(modifiedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return createdUTC;
    }

    public static String getBatchBusinessDate(String modeOfPayment){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("tenderType : "+tenderType);
        if(modeOfPayment.equalsIgnoreCase("CounterNets")){
            return getOffsetDate(1);
        }else if(EditBatchJobData.batchBusinessDate){
            return getBatchBusinessDate(1);
        }else {

            if (modeOfPayment.equalsIgnoreCase("Stripe")) {
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    cal.add(Calendar.DAY_OF_MONTH, -2);
                }
            }
            return sdf.format(cal.getTime());
        }
    }

    public static String getBatchBusinessDate(int dateOffset){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(PropertiesUtil.getProperties("dc.settlement.date"));
            cal.setTime(date);
            cal.add(Calendar.DATE, dateOffset);
            if (PropertiesUtil.getProperties("tender.type").equalsIgnoreCase(TenderType.CREDIT_CARD)) {
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    cal.add(Calendar.DAY_OF_MONTH, -2);
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf1.format(cal.getTime());
        return formattedDate;
    }

    public static String getBusinessDateForCreditCardSettlement(){
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
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf1.format(calendar.getTime());
        return formattedDate;
    }
    public static String getBusinessDateForQRCodeSettlement(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        try {
            date = sdf.parse(getDepositControlCreationDate());
            calendar.setTime(date);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf1.format(calendar.getTime());
        return formattedDate;
    }


    public static void main(String[] args) {
        System.out.println(getBusinessDateForCreditCardSettlement());
    }
    public static String getReconBusinessDate(int dateOffset){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(PropertiesUtil.getProperties("dc.expectedBankInDate"));
            cal.setTime(date);
            cal.add(Calendar.DATE, dateOffset);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf1.format(cal.getTime());
        return formattedDate;
    }

    public static String getCustomDate(String dateFormat, String requiredFormat, String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(requiredFormat);
        String formattedDate = sdf1.format(date);
        return formattedDate;
    }

    public static String mockBTTransactionDateAndTime(String dateInString) {

        SimpleDateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
        Date date1;
        String fileDate;
        try {
            date1 = formatter1.parse(dateInString);
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            fileDate = targetFormat.format(date1) + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fileDate;
    }

    public static String getAvailableToDate(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(dateInString);
            cal.setTime(date);
            cal.add(Calendar.MONTH, 1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf1.format(cal.getTime());
        return formattedDate;
    }

    public static Date extractDateFromString(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
    public static int compareDCCreationDate(){
        String dateInString = PropertiesUtil.getProperties("dc.create.date.time").replace("‑","-").replaceAll("[^A-Za-z0-9-:]"," ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Date currentDate = null;
        Calendar cal = Calendar.getInstance();
        try {
            currentDate = cal.getTime();
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return sdf1.format(date).compareTo(sdf1.format(currentDate));
    }

    public static String getDepositControlCreationDate(){
        String dateInString = StringUtil.cleanTextContent(PropertiesUtil.getProperties("dc.create.date.time").replace("‑","-"));
        SimpleDateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date date1;
        String transactionDate;
        try {
            date1 = formatter1.parse(dateInString);
            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            transactionDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return transactionDate;
    }
    public static String getAvailableTillDate(String dateInString, int offset){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = sdf.parse(dateInString);
            cal.setTime(date);
            cal.add(Calendar.DATE, offset);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf1.format(cal.getTime());
        return formattedDate;
    }
    public static String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String formattedDate = sdf.format(cal.getTime());
        return formattedDate;
    }
    public static List<String> getAck1Date(){
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String formattedDate = sdf.format(cal.getTime());
        String[] tempDate = formattedDate.split("\\s");
        String date1 = Arrays.asList(tempDate).stream().collect(Collectors.joining("T"));
        dateList.add(date1);
        tempDate[0] = tempDate[0].replace("-","");
        tempDate[1] = tempDate[1].replace(":","");
        String date2 = Arrays.asList(tempDate).stream().collect(Collectors.joining(""));
        dateList.add(date2);
        return Collections.unmodifiableList(dateList);
    }

    public static String getOffsetDate(int offset){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        String formattedDate = sdf.format(cal.getTime());
        return formattedDate;
    }
    public static String getTransactionDatTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        String formattedDate = sdf.format(cal.getTime())+"T"+
                StringUtils.padLeft(String.valueOf(cal.get(Calendar.HOUR)),2) +":"+
                StringUtils.padLeft(String.valueOf(cal.get(Calendar.MINUTE)),2)+":"+
                StringUtils.padLeft(String.valueOf(cal.get(Calendar.SECOND)),2);
        logger.info(()->String.format("Transaction date and time is %s",formattedDate));
        return formattedDate;
    }

    public static Collection<? extends String> getNetPosHeaderDateTime() {
        List<String> dateList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        dateList.add(Nets_pos.getNextToSettlementDate());
        dateList.add(timeFormat.format(date));
        dateList.add(Nets_pos.getSettlementFileDate());

        return dateList;
    }

}