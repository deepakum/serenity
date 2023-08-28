package Utility.others;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SaleItemSearchSubMenu {

    String SALE_ITEM = "Sale Item Search";

    List SaleItemSearch = Arrays.asList(SALE_ITEM);
    Map<String, List<String>> saleItemSearchSubMenuMap = new HashMap<>();

    static List<String> getSubMenuOptions(String menu){
        saleItemSearchSubMenuMap.put(SaleItemMenu.sale_item_search.name(), SaleItemSearch);
        return saleItemSearchSubMenuMap.get(menu);
    }

}
