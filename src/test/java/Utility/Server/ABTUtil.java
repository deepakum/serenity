package Utility.Server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ABTUtil {

    public static void main(String[] args) {
        String dateInString = "20230427";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        try {
            Date date = dateFormat.parse(dateInString);
            System.out.println(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
