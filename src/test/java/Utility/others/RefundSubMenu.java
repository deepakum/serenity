package Utility.others;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RefundSubMenu {

    static final String REFUND_INITIATION = "Refund Initiation";
    static final String CASE = "Case";


    public static final List REFUND = Arrays.asList(REFUND_INITIATION);
    public static final List Customer_Information = Arrays.asList(CASE);
    public static final Map<String, List<String>> refundMenu = new HashMap<>();

    public static List<String> getSubMenuOptions(String menu){
        refundMenu.put(RefundMenu.Refund.name(), REFUND);
        refundMenu.put(RefundMenu.Customer_Information.name(), Customer_Information);
        return refundMenu.get(menu);
    }

}
