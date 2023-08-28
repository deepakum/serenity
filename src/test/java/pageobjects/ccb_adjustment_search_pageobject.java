package pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class ccb_adjustment_search_pageobject extends PageObject {

    public static final Target ADJUSTMENT_ENTRY = Target.the("adjustment entry")
            .locatedBy("#dataTable>tbody>tr>td");
    public static final Target CREATION_DATE(int row){
        return Target.the("Creation date")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$CRE_DT']")
                .of(String.valueOf(row));
    }

    public static final Target ADJUSTMENT_TYPE(int row){
        return Target.the("Adjustment type")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$ADJ_TYPE_INFO']")
                .of(String.valueOf(row));
    }
    public static final Target AMOUNT(int row){
        return Target.the("Amount")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$ADJ_AMT']")
                .of(String.valueOf(row));
    }
    public static final Target PERSON_NAME(int row){
        return Target.the("Creation date")
                .locatedBy("//span[@id='SEARCH_RESULTS:{0}$ENTITY_NAME']")
                .of(String.valueOf(row));
    }

    public static final Target ARREARS_DATE(int row){
        return Target.the("Arrears date")
                .locatedBy("//span[@id='ACCT_FT_HIST:{0}$ARS_DT']")
                .of(String.valueOf(row));
    }
    public static final Target FINANCIAL_TRANSACTION_TYPE(int row){
        return Target.the("Financial transaction type")
                .locatedBy("//span[@id='ACCT_FT_HIST:{0}$DESCR']")
                .of(String.valueOf(row));
    }

    public static final Target CURRENT_AMOUNT(int row){
        return Target.the("current amount")
                .locatedBy("//span[@id='ACCT_FT_HIST:{0}$CUR_AMT']")
                .of(String.valueOf(row));
    }
    public static final Target ACCOUNT_FINANCIAL_HISTORY = Target.the("account financial history")
            .locatedBy("#dataTableBody>tr");
    public static final Target GET_MORE_BUTTON = Target.the("get more button")
            .locatedBy("#getMore");
}
