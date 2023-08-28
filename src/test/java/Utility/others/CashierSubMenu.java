package Utility.others;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CashierSubMenu {

    static final String DEPOSIT_CONTROL = "Deposit Control";
    static final String TENDER_CONTROL = "Tender Control";
    static final String PAYMENT = "Payment";
    static final String PAYMENT_EVENT = "Payment Event";
    static final String BATCH_JOB_SUBMISSION = "Batch Job Submission";
    static final String BATCH_RUN_TREE = "Batch Run Tree";
    static final String COUNTER_PAYMENTS = "Counter Payments";
    static final String PENDING_TRANSACTIONS = "Pending Transactions";

    public static final List counter = Arrays.asList(COUNTER_PAYMENTS,PENDING_TRANSACTIONS);
    public static final List Tools = Arrays.asList(BATCH_JOB_SUBMISSION,BATCH_RUN_TREE);
    public static final List Financial = Arrays.asList(
            DEPOSIT_CONTROL,PAYMENT,PAYMENT_EVENT,TENDER_CONTROL);
    public static final Map<String, List<String>> mainMenu = new HashMap<>();

    public static List<String> getSubMenuOptions(String menu){
        mainMenu.put(AdminMenu.Counter.name(), counter);
        mainMenu.put(AdminMenu.Tools.name(), Tools);
        mainMenu.put(AdminMenu.Financial.name(),Financial);
        return mainMenu.get(menu);
    }

}
