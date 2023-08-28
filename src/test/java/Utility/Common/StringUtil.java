package Utility.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String cleanTextContent(String text)
    {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

    public static String extractDigit(String amount){
        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(amount);
        String extractedData = null;
        if(matcher.find())
            extractedData = cleanTextContent(matcher.group());
        return extractedData;
    }
    public static void main(String[] args) {

    }
}
