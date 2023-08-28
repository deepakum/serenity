package Utility.CSV;

public class CSVUtil {

    public static String getBalanceTransactionID(String st){
        String[] stSplit = st.split("_");
        String balanceTransactionID = "txn"+"_"+stSplit[1];
        return balanceTransactionID;
    }

    public static String getSourceID(String st){
        String[] stSplit = st.split("_");
        String balanceTransactionID = "so"+"_"+stSplit[1];
        return balanceTransactionID;
    }
    public static String getRefundSourceID(String st){
        String[] stSplit = st.split("_");
        String balanceTransactionID = "re"+"_"+stSplit[1];
        return balanceTransactionID;
    }


    public static String getAutomaticPayoutID(String st){
        String[] stSplit = st.split("_");
        String balanceTransactionID = "po"+"_"+stSplit[1];
        return balanceTransactionID;
    }

    public static String getChargeID(String st){
        String[] stSplit = st.split("_");
        String balanceTransactionID = "ch"+"_"+stSplit[1];
        return balanceTransactionID;
    }

    public static String getTenderAmount(String tenderAmount){
        String customerFacingAmount = tenderAmount.replaceAll("[^\\d.]","");
        return customerFacingAmount;
    }
}
