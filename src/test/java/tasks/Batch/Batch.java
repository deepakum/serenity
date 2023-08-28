package tasks.Batch;

import Utility.others.PropertiesUtil;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Performable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Batch {

    public static Performable status(){
        return instrumented(BatchJobStatus.class);
    }

    public static Performable toSubmit(String batchName,boolean dateFlag){
        return Instrumented.instanceOf(BatchJobSubmission.class).withProperties(batchName,dateFlag);
    }

    public static Performable edit(String tender, String psp, String counterFlag,String cdc, String location, String erpFlag){
        return Instrumented.instanceOf(EditBatchJobData.class)
                .withProperties(tender,psp,counterFlag,cdc,location, erpFlag);
    }
    public static String expectedBankInDate(){
        String bankInDate = PropertiesUtil.getProperties("dc.expectedBankInDate");
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date1;
        String expectedBankInDate;
        try {
            date1 = formatter1.parse(bankInDate);
            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            expectedBankInDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }

    public static String settlementBusinessDate(){

        String bankInDate = PropertiesUtil.getProperties("dc.settlement.date");
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date1;
        String expectedBankInDate;
        try {
            date1 = formatter1.parse(bankInDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, 1);
            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            expectedBankInDate = targetFormat.format(calendar.getTime());;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }

    public static String enetsDebitSettlementBusinessDate(){

        String bankInDate = PropertiesUtil.getProperties("dc.settlement.date");
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date1;
        String expectedBankInDate;
        try {
            date1 = formatter1.parse(bankInDate);
            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            expectedBankInDate = targetFormat.format(date1);;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expectedBankInDate;
    }
}
