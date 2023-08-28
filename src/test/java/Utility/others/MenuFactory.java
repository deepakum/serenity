package Utility.others;

import net.serenitybdd.screenplay.targets.Target;
import pageobjects.ccb_user_pageobject;

import java.util.Objects;

public class MenuFactory {

    public static void main(String[] args) throws Exception {

        System.out.println(getMainMenuTarget("Financial").getCssOrXPathSelector());
        //System.out.println(getSubMenuTarget("Financial","Tender Control").getCssOrXPathSelector());
    }

    public static Target getMainMenuTarget(String type) {

        System.out.println("Config.getCurrentUser() : "+Config.getCurrentUser());
        if (Config.getCurrentUser() == "Admin"){
            AdminMenu menuValue = AdminMenu.valueOf(type);
            switch (Objects.requireNonNull(menuValue)){
                case Sale_item:
                    return ccb_user_pageobject.MAIN_MENU(AdminMenu.valueOf(type).ordinal());
                case Counter:
                    return ccb_user_pageobject.MAIN_MENU(AdminMenu.valueOf(type).ordinal());
                case Financial:
                    return ccb_user_pageobject.MAIN_MENU(AdminMenu.valueOf(type).ordinal());
                case Tools:
                    return ccb_user_pageobject.MAIN_MENU(AdminMenu.valueOf(type).ordinal());
                case Refund:
                    return ccb_user_pageobject.MAIN_MENU(AdminMenu.valueOf(type).ordinal());
                case Customer_Information:
                    return ccb_user_pageobject.MAIN_MENU(AdminMenu.valueOf(type).ordinal());
                default:
                    System.out.println("Option not available, please use valid menu option");
                    return Target.the(String.format("%s is missing, please add it in getMainMenuTarget()",type)).locatedBy("");
            }
        }else if(Config.getCurrentUser() == "Cashier"){
            CashierMenu menuValue = CashierMenu.valueOf(type);
            switch (Objects.requireNonNull(menuValue)){
                case Counter:
                    return ccb_user_pageobject.MAIN_MENU(CashierMenu.valueOf(type).ordinal());
                case Financial:
                    return ccb_user_pageobject.MAIN_MENU(CashierMenu.valueOf(type).ordinal());
                case Tools:
                    return ccb_user_pageobject.MAIN_MENU(CashierMenu.valueOf(type).ordinal());
                default:
                    System.out.println("Option not available, please use valid menu option");
                    return null;
            }
        }else if(Config.getCurrentUser() == "Chief"){
            ChiefCashierMenu menuValue = ChiefCashierMenu.valueOf(type);
            switch (Objects.requireNonNull(menuValue)){
                case Counter:
                    return ccb_user_pageobject.MAIN_MENU(ChiefCashierMenu.valueOf(type).ordinal());
                case Financial:
                    return ccb_user_pageobject.MAIN_MENU(ChiefCashierMenu.valueOf(type).ordinal());
                case Tools:
                    return ccb_user_pageobject.MAIN_MENU(ChiefCashierMenu.valueOf(type).ordinal());
                default:
                    System.out.println("Option not available, please use valid menu option");
                    return null;
            }
        }else if(Config.getCurrentUser() == "AO" || Config.getCurrentUser() == "CO"){
            OfficerMenu menuValue = OfficerMenu.valueOf(type);
            switch (Objects.requireNonNull(menuValue)){
                case Customer_Information:
                    return ccb_user_pageobject.MAIN_MENU(OfficerMenu.valueOf(type).ordinal());
                default:
                    System.out.println("Option not available, please use valid menu option");
                    return null;
            }
        }else if(Config.getCurrentUser().equalsIgnoreCase("Maker_DBC")){
            MakerMenu menuValue = MakerMenu.valueOf(type);
            switch (Objects.requireNonNull(menuValue)){
                case Sale_item:
                    return ccb_user_pageobject.MAIN_MENU(MakerMenu.valueOf(type).ordinal());
                case Add_sale_item:
                    System.out.println("MakerMenu.valueOf(type).ordinal() : "+MakerMenu.valueOf(type).ordinal());
                    return ccb_user_pageobject.MAIN_MENU(MakerMenu.valueOf(type).ordinal());
                default:
                    System.out.println("Option not available, please use valid menu option");
                    return null;
            }
        } else if(Config.getCurrentUser() == "Financier"){
        MakerMenu menuValue = MakerMenu.valueOf(type);
        switch (Objects.requireNonNull(menuValue)){
            case Sale_item:
                return ccb_user_pageobject.MAIN_MENU(MakerMenu.valueOf(type).ordinal());
            case Add_sale_item:
                return ccb_user_pageobject.MAIN_MENU(CashierMenu.valueOf(type).ordinal());
            default:
                System.out.println("Option not available, please use valid menu option");
                return null;
        }
    }
        else {
            return null;
        }
    }

    public static Target getSubMenuTarget(String menu, String subMenu, String subSubMenu ) {

        if (Config.getCurrentUser().equalsIgnoreCase("Admin") || Config.getCurrentUser().equalsIgnoreCase("Cashier")) {
            AdminMenu menuValue = AdminMenu.valueOf(menu);
            switch (menuValue) {
                case Counter:
                    return ccb_user_pageobject.COUNTER_SUB_MENU(subMenu, subSubMenu);
                case Tools:
                    return ccb_user_pageobject.TOOLS_SUB_MENU(subMenu, subSubMenu);
                case Financial:
                    return ccb_user_pageobject.FINANCIAL_SUB_MENU(subMenu, subSubMenu);
                case Refund:
                    return ccb_user_pageobject.REFUND_SUB_MENU(subMenu, null);
                case Sale_item:
                    return ccb_user_pageobject.SALE_ITEM_SUB_MENU(subMenu, subSubMenu);
                case Add_Sale_Item:
                    return ccb_user_pageobject.ADD_SALE_ITEM_SUB_MENU(subMenu, subSubMenu);
                case Customer_Information:
                    return ccb_user_pageobject.CUSTOMER_INFORMATION_SUB_MENU(subMenu, subSubMenu);
                default:
                    System.out.println("Option not available, please use valid menu option");
                    return null;
            }
        } else if (Config.getCurrentUser().equalsIgnoreCase("Maker_DBC")) {
            MakerMenu menuValue = MakerMenu.valueOf(menu);
            switch (Objects.requireNonNull(menuValue)) {
                case Sale_item:
                    return ccb_user_pageobject.MAIN_MENU(MakerMenu.valueOf(menu).ordinal());
                case Add_sale_item:
                    return ccb_user_pageobject.ADD_SALE_ITEM_SUB_MENU(subMenu, subSubMenu);
                case Sale_item_search:
                    return ccb_user_pageobject.SALE_ITEM_SUB_MENU(subMenu, subSubMenu);
                default:
                    System.out.println("Option not available, please use valid menu option");
                    return null;
            }
        }        else {
            return null;
        }
    }
}
