package Utility.others;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AddSaleItemSubMenu {

    static final String SALE_ITEM = "Sale Item";

    public static final List SaleItem = Arrays.asList(SALE_ITEM);
    public static final Map<String, List<String>> saleItemMenu = new HashMap<>();

    public static List<String> getSubMenuOptions(String menu){
        saleItemMenu.put(SaleItemMenu.sale_item.name(), SaleItem);
        return saleItemMenu.get(menu);
    }

}
