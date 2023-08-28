package Utility.others;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SubMenu {

    public static final List counter = Arrays.asList("Counter Payments","Pending Transactions");
    public static final List Tools = Arrays.asList("Batch Job Submission","Batch Run Tree");
    public static final List customer_information = Arrays.asList("Account","Account Management",
            "Account SAs for Debt Class","Account/Person Replicator","Bill Print Group","Case"
    ,"Customer Contact","Declaration","landlord Agreement","Loan","Non-billed Budget","Person","Premise");
    public static final List Refund = Arrays.asList("Refund Initiation","Refund Approval","Miscellaneous Refund");
    public static final List Financial = Arrays.asList("Adjustment","Adjustment Staging Control",
            "Adjustment Upload Staging","Bill","Bill Segment","Billable Charge","Billable Charge Upload Staging",
            "Deposit Control","Deposit Control Staging","Financial Transaction","Match Event","Multi-Cancel/Rebill",
            "Off Cycle Bill Generator","Payment","Payment Event","Payment Event Quick Add",
            "Payment Event Upload Staging","Payment Portal","Payment Quick Add","Payment Upload Staging",
            "Statement","Tender Control");
    public static final List Add_sale_item = Arrays.asList("Sale Item");
    public static final Map<String, List<String>> mainMenu = new HashMap<>();
    public static List<String> getSubMenuOptions(String menu){
        mainMenu.put(AdminMenu.Counter.name(), counter);
        mainMenu.put(AdminMenu.Tools.name(), Tools);
        mainMenu.put(AdminMenu.Financial.name(),Financial);
        mainMenu.put(AdminMenu.Refund.name(),Refund);
        mainMenu.put(AdminMenu.Add_Sale_Item.name(),Add_sale_item);
        mainMenu.put(AdminMenu.Customer_Information.name(),customer_information);
        return mainMenu.get(menu);
    }
}
