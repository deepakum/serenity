package Utility.ABT;

import Utility.SettlementFile.XmlNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.*;

import static Utility.ABT.MockSapArXml.getDocument;

public class wosfGlVerification {

    static Map<String, String> mapTT = new HashMap<String, String>();
    static Map<String, String> mapBH = new HashMap<String, String>();
    public static List<Map<String,String>> wosfListMap = new ArrayList<>();
    public static String AbtTemplatePath = System.getProperty("user.dir") + File.separator + "/src/test/resources/mockedFile/ABT/";
    public static List<Map<String, String>> getWriteOffGlFileData() {

        try {
            Document document = getDocument(getTemplateFile(ABTFile.WOSF_GL_FILE));
            NodeList nList = document.getElementsByTagName("BH");
            String pdfDate;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Map<String, String> map = new HashMap<String, String>();
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    mapBH.put(XmlNode.RECORD_TYPE,eElement.getElementsByTagName(XmlNode.RECORD_TYPE).item(0).getTextContent());
                    mapBH.put(XmlNode.COMPANY_CODE,eElement.getElementsByTagName(XmlNode.COMPANY_CODE).item(0).getTextContent());
                    mapBH.put(XmlNode.DOCUMENT_DATE,eElement.getElementsByTagName(XmlNode.DOCUMENT_DATE).item(0).getTextContent());
                    mapBH.put(XmlNode.POSTING_DATE,eElement.getElementsByTagName(XmlNode.POSTING_DATE).item(0).getTextContent());
                    mapBH.put(XmlNode.CURRENCY,eElement.getElementsByTagName(XmlNode.CURRENCY).item(0).getTextContent());
                    mapBH.put(XmlNode.EXCHANGE_RATE,eElement.getElementsByTagName(XmlNode.EXCHANGE_RATE).item(0).getTextContent());
                    mapBH.put(XmlNode.REFERENCE,eElement.getElementsByTagName(XmlNode.REFERENCE).item(0).getTextContent());
                    String headerText = eElement.getElementsByTagName(XmlNode.HEADER_TEXT).item(0).getTextContent();
                    mapBH.put(XmlNode.HEADER_TEXT,headerText);
                    mapBH.put(XmlNode.FISCAL_YEAR,String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                }
            }

            NodeList lList = document.getElementsByTagName("BL");
            for (int temp = 0; temp < lList.getLength(); temp++) {
                Map<String, String> map = new HashMap<String, String>();
                Node node = lList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    map.put(XmlNode.RECORD_TYPE,eElement.getElementsByTagName(XmlNode.RECORD_TYPE).item(0).getTextContent());
                    map.put(XmlNode.REFERENCE,eElement.getElementsByTagName(XmlNode.REFERENCE).item(0).getTextContent());
                    map.put(XmlNode.LINE_ITEM,eElement.getElementsByTagName(XmlNode.LINE_ITEM).item(0).getTextContent());
                    map.put(XmlNode.ACCOUNT_TYPE,eElement.getElementsByTagName(XmlNode.ACCOUNT_TYPE).item(0).getTextContent());
                    map.put(XmlNode.DEBIT_CREDIT_INDICATOR,eElement.getElementsByTagName(XmlNode.DEBIT_CREDIT_INDICATOR).item(0).getTextContent());
                    map.put(XmlNode.GLA_ACCOUNT,eElement.getElementsByTagName(XmlNode.GLA_ACCOUNT).item(0).getTextContent());
                    map.put(XmlNode.AMOUNT,eElement.getElementsByTagName(XmlNode.AMOUNT).item(0).getTextContent());
                    map.put(XmlNode.LINE_ITEM_TEXT,eElement.getElementsByTagName(XmlNode.LINE_ITEM_TEXT).item(0).getTextContent());
                    map.put(XmlNode.TAX_CODE,eElement.getElementsByTagName(XmlNode.TAX_CODE).item(0).getTextContent());
                    map.put(XmlNode.TAX_AMOUNT,eElement.getElementsByTagName(XmlNode.TAX_AMOUNT).item(0).getTextContent());
                    map.put(XmlNode.FUND,eElement.getElementsByTagName(XmlNode.FUND).item(0).getTextContent());
                    map.put(XmlNode.FUND_CENTER,eElement.getElementsByTagName(XmlNode.FUND_CENTER).item(0).getTextContent());
                    map.put(XmlNode.COST_CENTER,eElement.getElementsByTagName(XmlNode.COST_CENTER).item(0).getTextContent());
                    map.put(XmlNode.INTERNAL_ORDER,eElement.getElementsByTagName(XmlNode.INTERNAL_ORDER).item(0).getTextContent());
                    map.put(XmlNode.TAX_AUTOMATICALLY,eElement.getElementsByTagName(XmlNode.TAX_AUTOMATICALLY).item(0).getTextContent());
                    wosfListMap.add(map);
                }
            }
            NodeList ttList = document.getElementsByTagName("TT");
            for (int temp = 0; temp < ttList.getLength(); temp++) {
                Node node = ttList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    mapTT.put(XmlNode.RECORD_TYPE,eElement.getElementsByTagName(XmlNode.RECORD_TYPE).item(0).getTextContent());
                    mapTT.put(XmlNode.TOTAL_RECORDS,String.valueOf(wosfListMap.size()));
                    mapTT.put(XmlNode.TOTAL_VALUE,eElement.getElementsByTagName(XmlNode.TOTAL_VALUE).item(0).getTextContent());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wosfListMap;
    }
    public static File getTemplateFile(String type) {
        return Arrays.asList(new File(AbtTemplatePath).listFiles()).stream().filter(file -> file.getName().contains(type)).findFirst().get();
    }
    public static void main(String[] args) {
        //System.out.println(getWriteOffGlFileData());
//        System.out.println(getAbtFileName(ABTFile.WOSF_FILE));
//        Map<String,Map<String,String>> writeOffGstMap = DB_Utils.getWriteoffAndGstAmount(getAbtFileName(ABTFile.WOSF_FILE));
//        System.out.println(writeOffGstMap);
    }
}
