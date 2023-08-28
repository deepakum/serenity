package Utility.others;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OfficerSubMenu {

    static final String CASE = "Case";
    static final String SERVICE_MANAGEMENT = "Service Management";

    public static final List Customer_Information = Arrays.asList(CASE,SERVICE_MANAGEMENT);

    public static final Map<String, List<String>> mainMenu = new HashMap<>();
    public static List<String> getSubMenuOptions(String menu){
        mainMenu.put(OfficerMenu.Customer_Information.name(), Customer_Information);
        return mainMenu.get(menu);
    }
}
