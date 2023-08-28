package Utility.DB;

import Utility.BatchJob.BatchControl;
import Utility.others.PropertiesUtil;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;

import java.io.File;
import java.sql.*;
import java.util.*;

public class DB_Utils {
    static EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static Statement statement;
    private static Connection conn;

    private static Statement getStatement(){

        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(environmentVariables.getProperty("CCRS.db.url"),environmentVariables.getProperty("CCRS.db.username"),environmentVariables.getProperty("CCRS.db.password"));
            statement = conn.createStatement();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return statement;
    }

    public static List<String> getDC(){
        String query = "SELECT DEP_CTL_ID FROM ci_dep_ctl where tndr_srce_type_flg='CASH' and DEP_CTL_STATUS_FLG ='30' and BALANCED_USER_ID='CCSH-NBR' and version='12' order by cre_dttm desc";
        statement = getStatement();
        List<String> list = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                list.add(resultSet.getString("DEP_CTL_ID"));
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableList(list);
    }

    public static List<String> getOpenDC(){
        String query = "SELECT DEP_CTL_ID FROM ci_dep_ctl where tndr_srce_type_flg='CASH' and DEP_CTL_STATUS_FLG ='10' order by cre_dttm desc";
        statement = getStatement();
        List<String> list = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                list.add(resultSet.getString("DEP_CTL_ID"));
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableList(list);
    }

    public static String getPaymentEventID(){
        List<String> listOfDC = getDC();
        String paymentEventID = null;
        for(String dcID : listOfDC) {
            String query = "SELECT PT.PAY_EVENT_ID FROM CI_TNDR_CTL TC, CI_PAY_TNDR PT WHERE TC.DEP_CTL_ID=" + dcID + " AND TC.TNDR_CTL_ID=PT.TNDR_CTL_ID";

            statement = getStatement();
            List<String> list = new ArrayList<>();
            try {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String paymentID = resultSet.getString("PAY_EVENT_ID");
                    list.add(paymentID);
                } resultSet.close();
                int record = -1;
                for(String pay : list) {
                    paymentEventID = pay;
                    record = refundInitiated(pay);
                    if (record==0) break;
                }
                if (record==0) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        PropertiesUtil.setProperties("CCRS.paymentEventID",paymentEventID);
        return paymentEventID;
    }

    private static int refundInitiated(String paymentEventID){
        String query = "SELECT * FROM CM_RFND_TRANS RT, CM_TRANS TT, CM_TRANS_PAY TP WHERE TT.BILL_ID=TP.BILL_ID AND RT.BILL_ID=TT.BILL_ID AND TP.PAY_EVENT_ID ="+paymentEventID;
        int record = 0;
        try {
            ResultSet resultSet = getStatement().executeQuery(query);
            while(resultSet.next()) {record++;}
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return record;
    }
    public static String refundCaseStatus(String caseID){
        System.out.println("caseID : "+caseID);
        String query = "select * from CM_RFND_TRANS where CM_RFND_ID="+caseID;
        String refundStatus = null;
        try {
            ResultSet resultSet = getStatement().executeQuery(query);
            while(resultSet.next()){
                refundStatus = resultSet.getString("CM_RFND_STATUS");
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return refundStatus;
    }


    private static List<String> getDCByCharVal(String charVal) {

        String query = "Select * from ci_dep_ctl_char where CHAR_VAL='"+charVal+"'";
        String dcQuery = "SELECT DEP_CTL_ID FROM ci_dep_ctl where tndr_srce_type_flg='CASH' and DEP_CTL_STATUS_FLG ='10'";
        statement = getStatement();
        List<String> list = new ArrayList<>();
        List<String> openDcList = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                list.add(result.getString("DEP_CTL_ID").trim());
            }
//            ResultSet res  = statement.executeQuery(dcQuery);
//            List<String> dcList = new ArrayList<>();
//            while(res.next()){
//                dcList.add(res.getString("DEP_CTL_ID").trim());
//            }
//            openDcList = list.stream().filter(dc->dcList.contains(dc)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Collections.unmodifiableList(list);
    }

    private static List<String> getDCBySourceTypeFlagVal(String sourceType) {

        String dcQuery = "SELECT DEP_CTL_ID FROM ci_dep_ctl where tndr_srce_type_flg='"+sourceType+"' and DEP_CTL_STATUS_FLG ='10'";
        statement = getStatement();
        List<String> openDcList = new ArrayList<>();
        try {
            ResultSet res  = statement.executeQuery(dcQuery);
            while(res.next()){
                openDcList.add(res.getString("DEP_CTL_ID").trim());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Collections.unmodifiableList(openDcList);
    }

    public static void balanceDCAndTC() throws SQLException {
        List<String> list = getDCByCharVal("NBR");
        statement = getStatement();
        for (String dc : list){
            String query = "select TC.TNDR_CTL_ID from CI_TNDR_CTL TC, ci_dep_ctl DC where TC.DEP_CTL_ID=DC.DEP_CTL_ID and DC.DEP_CTL_ID='"+dc+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.getRow()> 0) {
                while (resultSet.next()) {
                    String tc = resultSet.getString("TNDR_CTL_ID");
                    System.out.println("Open TC : "+tc);
                    if (tc.length() > 0) {
                        balanceTC(tc);
                    }
                }
            }
            balanceDC(dc);
        }
        //conn.close();
        //statement.close();
    }

    public static void balanceLockedDC() throws SQLException {
        List<String> list = getDCBySourceTypeFlagVal("LOCK");
        System.out.println("list : "+list);
        statement = getStatement();
        for (String dc : list){
            System.out.println("DC :"+dc);
            balanceDC(dc);
        }
        //conn.close();
        //statement.close();
    }
    public static void balanceAllTC() throws SQLException {
        String role = environmentVariables.getProperty("CCRS.Cashier.username");

        statement = getStatement();
        String query = "SELECT * FROM ci_tndr_ctl where TNDR_SOURCE_CD='COUNTER' and USER_ID='"+role+"' and TNDR_CTL_ST_FLG='10'";
        ResultSet resultSet = statement.executeQuery(query);
//        if(resultSet.getRow()> 0) {
        while (resultSet.next()) {
            String tc = resultSet.getString("TNDR_CTL_ID");
            System.out.println("open TC : "+tc);
            if (tc.length() > 0) {
                balanceTC(tc);
            }
            if(resultSet!=null) break;
        }
//        }
        //conn.close();
    }
    private static void balanceTC(String tc) throws SQLException {
        String tcBalanceQuery = "UPDATE CI_TNDR_CTL SET TNDR_CTL_ST_FLG=30 WHERE TNDR_CTL_ID='"+tc+"'";
        statement.executeQuery(tcBalanceQuery);
        statement.executeQuery("commit");
    }

    public static void editNetPosSettlementFileStatus(String fileName) {

        String fileStatusQuery = "update cm_file_status set cm_file_status=20 WHERE cm_file_name ='"+fileName+"'";
        System.out.println(fileStatusQuery);
        statement = getStatement();
        try {
            statement.executeQuery(fileStatusQuery);
            statement.executeQuery("commit");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private static void balanceDC(String dc) throws SQLException {
        String tcBalanceQuery = "UPDATE CI_DEP_CTL SET DEP_CTL_STATUS_FLG=30 WHERE DEP_CTL_ID='"+dc+"'";
        statement.executeQuery(tcBalanceQuery);
        statement.executeQuery("commit");
        //statement.close();
    }
    public static Map<String, String> getAdjustmentDescription(){
        String query = "select * from ci_adj_type_l";
        statement = getStatement();
        Map<String, String> writeOffDescMap = new HashMap<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                writeOffDescMap.put(resultSet.getString("ADJ_TYPE_CD").trim(),resultSet.getString("DESCR").trim());
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableMap(writeOffDescMap);
    }

    public static Map<String, Map<String,String>> getWriteoffAndGstAmount(String filePath){
        String fileName = new File(filePath).getName();
        String query = "select * from cm_abt_woffsf where cm_file_name = '"+fileName+"'";
        statement = getStatement();
        Map<String, Map<String,String>> writeOffDescMap = new HashMap<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                Map<String,String> tempMap = new HashMap<>();
                tempMap.put("Amount",resultSet.getString("CM_TOTWOFF_AMT_WOGST").trim());
                tempMap.put("GST",resultSet.getString("CM_GST_AMT").trim());
                writeOffDescMap.put(
                        resultSet.getString("CM_SVCPRVDR_ID").trim()+"_"+resultSet.getString("CM_PCKG_ID").trim(),
                        tempMap);
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableMap(writeOffDescMap);
    }

    public static Map<String, Map<String,String>> getRsfGstAmount(String filePath){
        String fileName = new File(filePath).getName();
        String query = "select * from cm_abt_revsf where cm_file_name = '"+fileName+"'";
        statement = getStatement();
        Map<String, Map<String,String>> writeOffDescMap = new HashMap<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                Map<String,String> tempMap = new HashMap<>();
                tempMap.put("Amount",resultSet.getString("CM_TOTWOFF_AMT_WOGST").trim());
                tempMap.put("GST",resultSet.getString("CM_GST_AMT").trim());
                writeOffDescMap.put(
                        resultSet.getString("CM_SVCPRVDR_ID").trim()+"_"+resultSet.getString("CM_PCKG_ID").trim(),
                        tempMap);
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableMap(writeOffDescMap);
    }
    public static List<String> getexistingNoticeID(){
        String query = "select CM_NOTICE_ID from cm_evms_outstndg_trns";
        statement = getStatement();
        List<String> NoticeList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                NoticeList.add(resultSet.getString("CM_NOTICE_ID").trim());
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableList(NoticeList);
    }

    public static String getBusinessDateForMerchantBatch(String batchType, String fileName,String colName){
        String settlementType = null;
        if(batchType.equalsIgnoreCase(BatchControl.PPSSF)){
            settlementType = "PPSSF";
        }else if(batchType.equalsIgnoreCase(BatchControl.SSF)){
            settlementType = "SSF";
        }
        String query = "select * from cm_abt_settlsf where CM_SETTLSF_TYPE='"+settlementType+"' and CM_FILE_NAME='"+fileName+"'";
        System.out.println(query);
        statement = getStatement();
        List<String> NoticeList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                NoticeList.add(resultSet.getString(colName).trim());
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return NoticeList.get(0);
    }

    public static void main(String[] args) throws SQLException {
        balanceAllTC();
    }
}
