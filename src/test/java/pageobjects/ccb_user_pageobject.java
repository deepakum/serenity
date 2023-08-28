package pageobjects;

import Utility.SaleItem.SaleItem;
import Utility.others.*;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

import static Utility.others.AdminMenu.*;
import static Utility.others.SaleItemMenu.sale_item;

public class ccb_user_pageobject {

    public static final Target HOME_MENU_BUTTON = Target.the("Home menu button")
            .locatedBy("css:#IM_USER_HOME");
    public static final Target CRRS_HOME_HEADER = Target.the("CCRS home header text")
            .located(By.id("productName"));

    public static final Target BATCH_CODE = Target.the("batch code")
            .locatedBy("css:#BATCH_CD");

    public static final Target SEARCH_BATCH = Target.the("Search batch")
            .located(By.id("BU_Altr_bjSrch"));

    public static final Target LATEST_BATCH = Target.the("Latest btach")
            .locatedBy("//*[@id='dataDivision']/table/tbody/tr[1]");

    public static final Target SEARCH_MENU = Target.the("search menu box")
            .locatedBy("//input[@id='inputSearch|input']");

    public static final Target SWITCH_UI_VIEW_MENU_OPTION = Target.the("Switch UI view menu")
            .locatedBy("#toggle_ui");
    public static final Target SEARCH_MENU_OPTIONS = Target.the("Search option")
            .locatedBy("//ul[@id='ui-id-1']/li");

    public static final Target PAGE_TITLE = Target.the("searched batch header")
            .locatedBy("css:#ptitle");

    public static final Target DATA_TABLE = Target.the("parameters table")
            .locatedBy("//table[@id='dataTable']");

    public static final Target DUPLICATE_AND_QUEUE = Target.the("Duplicate and queue button")
            .locatedBy("//input[@id='ACTION_Q_SW']");

    public static final Target BATCH_JOB_STATUS = Target.the("batch job status")
            .locatedBy("//span[@id='BATCH_JOB_STAT_FLG']");

    public static final Target REFRESH = Target.the("refresh button")
            .locatedBy("//input[@id='IM_REFRESH']");

    public static final Target SAVE = Target.the("Save button")
            .locatedBy("//input[@id='IM_SAVE']");

    public static final Target BATCH_RUN_TREE_STATUS = Target.the("Batch run tree status")
            .locatedBy("//span[@id='l680919']");

    public static final Target BATCH_RUN_TREE_SEARCH = Target.the("batch run tree search")
            .locatedBy("//input[@id='BU_Main_batchSrch']");

    public static final Target MENU_BUTTON = Target.the("menu button")
            .locatedBy("//span[@id='IM_menuButton']");
    public static final Target MAIN_MENU(int index) {
        return Target.the("Main menu")
                .locatedBy("//li[@id='CI_MAINMENU_topMenuItem0x{0}']")
                .of(String.valueOf(index));
    }

    public static final Target COUNTER_SUB_MENU(String option, String subSubMenu) {
        int subMenu = 0;
        if(subSubMenu!=null){
            subMenu = 1;
        }
        int index = SubMenu.getSubMenuOptions(AdminMenu.Counter.name()).indexOf(option);
        return Target.the("#option button")
                .locatedBy("//li[@id='CM_COUNTER_PAYMENT_subMenuItem{0}x{1}']")
                .of(String.valueOf(subMenu),String.valueOf(index));
    }
    public static final Target TOOLS_SUB_MENU(String option,String subSubMenu) {
        int subMenu = 0;
        if(subSubMenu!=null){
            subMenu = 1;
        }
        int index = SubMenu.getSubMenuOptions(AdminMenu.Tools.name()).indexOf(option);
        return Target.the("#option button")
                .locatedBy("//li[@id='CI_BGPROCESS_subMenuItem{0}x{1}']")
                .of(String.valueOf(subMenu),String.valueOf(index));
    }

    public static final Target FINANCIAL_SUB_MENU(String option, String subSubMenu) {
        int subMenu = 0;
        int index = 0;
        if(subSubMenu!=null){
            subMenu = 1;
        }
        if(Config.getCurrentUser().equalsIgnoreCase("Admin")){
            index = SubMenu.getSubMenuOptions(Financial.name()).indexOf(option);
        }else if(Config.getCurrentUser().equalsIgnoreCase("Cashier")) {
            index = CashierSubMenu.getSubMenuOptions(Financial.name()).indexOf(option);
        }
        return Target.the("#option button")
                .locatedBy("//li[@id='CI_FINANCIAL_subMenuItem{0}x{1}']")
                .of(String.valueOf(subMenu),String.valueOf(index));
    }

    public static final Target ADD_SALE_ITEM_SUB_MENU(String option, String subSubMenu) {
        int subMenu = 0;
        int index = 0;
        if(subSubMenu!=null){
            subMenu = 1;
        }
        if(Config.getCurrentUser().equalsIgnoreCase("Maker_DBC")){
//            index = AddSaleItemSubMenu.getSubMenuOptions(saleItem.name()).indexOf(option);
            index=0;
        }else if(Config.getCurrentUser()=="Cashier") {
            index = CashierSubMenu.getSubMenuOptions(Financial.name()).indexOf(option);
        }
        return Target.the("#option button")
                .locatedBy("//li[@id='CMSLITAD_subMenuItem{0}x{1}']")
                .of(String.valueOf(subMenu),String.valueOf(index));
    }

    public static final Target SALE_ITEM_SUB_MENU(String option, String subSubMenu) {
        int subMenu = 0;
        int index = 0;
        if(subSubMenu!=null){
            subMenu = 1;
        }
        if(Config.getCurrentUser()=="Maker_DBC"){
            index = 0;
        }else if(Config.getCurrentUser()=="Financier") {
            index = CashierSubMenu.getSubMenuOptions(Sale_item.name()).indexOf(option);
        }
        return Target.the("#option button")
                .locatedBy("//li[@id='CM_SALE_ITEM_subMenuItem{0}x{1}']")
                .of(String.valueOf(subMenu),String.valueOf(index));
    }
    public static final Target REFUND_SUB_MENU(String option, String subSubMenu) {
        int subMenu = 0;
        int index = 0;
        if(subSubMenu!=null){
            subMenu = 1;
        }
        if(Config.getCurrentUser()=="Admin"){
            index = SubMenu.getSubMenuOptions(Refund.name()).indexOf(option);
        }else if(Config.getCurrentUser()=="Cashier") {
            index = CashierSubMenu.getSubMenuOptions(Refund.name()).indexOf(option);
        }
        return Target.the("#option button")
                .locatedBy("//li[@id='CM_REFUND_subMenuItem{0}x{1}']")
                .of(String.valueOf(subMenu),String.valueOf(index));
    }

    public static final Target CUSTOMER_INFORMATION_SUB_MENU(String option, String subSubMenu) {
        int subMenu = 0;
        int index = 0;
        if(subSubMenu!=null){
            subMenu = 1;
        }
        if(Config.getCurrentUser()=="Admin"){
            index = SubMenu.getSubMenuOptions(Customer_Information.name()).indexOf(option);
        }else if(Config.getCurrentUser()=="Cashier") {
            index = CashierSubMenu.getSubMenuOptions(Refund.name()).indexOf(option);
            //index = RefundSubMenu.getSubMenuOptions(RefundMenu.Customer_Information.name()).indexOf(option);
        }
        return Target.the("#option button")
                .locatedBy("//li[@id='CI_CUSTOMERINFORMATION_subMenuItem{0}x{1}']")
                .of(String.valueOf(subMenu),String.valueOf(index));
    }

    public static final Target LEAF_MENU(String rootMenu, String leafMenu){

        String menu = rootMenu.toUpperCase().replace(" ","_")+"_"+leafMenu.toUpperCase();
        switch(menu){
            case "DEPOSIT_CONTROL_SEARCH" :
                return Target.the("Search menu button").locatedBy("css:#CI0000000190");
            case "DEPOSIT_CONTROL_ADD" :
                return Target.the("Add menu button").locatedBy("css:#CI0000000187");
            case "PAYMENT_EVENT_SEARCH" :
                return Target.the("search menu button").locatedBy("css:#CI0000000011");
            case "BATCH_JOB_SUBMISSION_SEARCH" :
                return Target.the("Search menu button").locatedBy("css:#CI0000000733");
            case "BATCH_JOB_SUBMISSION_ADD" :
                return Target.the("Add menu button").locatedBy("css:#CI0000000750");
            case "TENDER_CONTROL_SEARCH" :
                return Target.the("Search menu button").locatedBy("css:#CI0000000014");
            case "TENDER_CONTROL_ADD" :
                return Target.the("Add menu button").locatedBy("css:#CI0000000105");
            case "CASE_SEARCH" :
                return Target.the("Search menu button").locatedBy("css:#caseMaint");
            case "PERSON_SEARCH" :
                return Target.the("Person search menu button").locatedBy("css:#CI0000000003");
            case "SALE_ITEM_SEARCH" :
                return Target.the("Search menu button").locatedBy("css:#CM_SALE_ITEM_subMenuItem0x0");
            case "SALE_ITEM_ADD" :
                return Target.the("Search menu button").locatedBy("css:#cmSaleItemAdd");
            default:
                return null;
        }
    }
    public static final Target LOGGED_IN_USER_SPAN = Target.the("logged in user span")
            .locatedBy("#youAreLoggedInAsSpan");
    public static final Target LOGOUT = Target.the("logout button")
            .locatedBy("#Logout");
    public static final Target PRODUCT_NAME_SPAN = Target.the("product name span")
            .locatedBy("#productName");
}
