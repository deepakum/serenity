package Utility.others;

public class DepositControlInfo {

    private static String depositControlID;
    private static String expectedEndingBalance;
    private static String depositControlStatus;
    private static String tenderSourceType;
    private static String user;
    public static final String SETTLEMENT_DATE_FORMAT = "dd/MM/yyyy";

    private static String expectedBankInDate;

    public DepositControlInfo(String depositControlID, String depositControlStatus, String user, String tenderSourceType, String createDateAndTime){
        this.depositControlID = depositControlID;
        this.expectedEndingBalance = createDateAndTime;
        this.depositControlStatus = depositControlStatus;
        this.tenderSourceType = tenderSourceType;
        this.user =user;
    }

    public static String getDepositControlID() {
        return depositControlID;
    }

    public static void setDepositControlID(String depositControlID) {
        DepositControlInfo.depositControlID = depositControlID;
    }

    public static String getExpectedEndingBalance() {
        return expectedEndingBalance;
    }

    public static void setExpectedEndingBalance(String expectedEndingBalance) {
        DepositControlInfo.expectedEndingBalance = expectedEndingBalance;
    }

    public static String getDepositControlStatus() {
        return depositControlStatus;
    }

    public static void setDepositControlStatus(String depositControlStatus) {
        DepositControlInfo.depositControlStatus = depositControlStatus;
    }

    public static String getTenderSourceType() {
        return tenderSourceType;
    }

    public static void setTenderSourceType(String tenderSourceType) {
        DepositControlInfo.tenderSourceType = tenderSourceType;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        DepositControlInfo.user = user;
    }

    public static String getExpectedBankInDate() {
        return expectedBankInDate;
    }

    public static void setExpectedBankInDate(String expectedBankInDate) {
        DepositControlInfo.expectedBankInDate = expectedBankInDate;
    }

    @Override
    public String toString() {
        return (depositControlID+"_"+depositControlStatus+"_"+tenderSourceType).toString();
    }
}
