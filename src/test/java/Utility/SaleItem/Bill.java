package Utility.SaleItem;

import java.util.HashMap;
import java.util.Map;

public class Bill {

    public static Map<String, String> billCharacterMap = new HashMap<String, String>();
    static{
        billCharacterMap.put("Building Name","Macpherson garden");
        billCharacterMap.put("Block Name","66");
        billCharacterMap.put("Country","Singapore");
        billCharacterMap.put("Customer Name","WIPRO");
        billCharacterMap.put("Minimum Applicable","0");
        billCharacterMap.put("Postal Code","370066");
        billCharacterMap.put("Street Name","Circuit Road");
        billCharacterMap.put("Phone Number","+65-86801794");
        billCharacterMap.put("Payment Service Category","PP");
        billCharacterMap.put("Payment Service Sub Category","DBCSF");
    }

    public static void main(String[] args) {
        System.out.println(billCharacterMap);
    }
}
