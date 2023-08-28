package Utility.SettlementFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Utility.others.BatchFile;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import static Utility.DB.DB_Utils.getexistingNoticeID;


public class MockXml {

    static Logger logger = LoggerFactory.getLogger(MockXml.class);
    private static Document document;
    static Map<String, String> map = new HashMap<String, String>();
    static List<String> nodeList = new ArrayList<String>();
    public static String templatePath = System.getProperty("user.dir") + File.separator + "/src/test/resources/template/";
    public static String downloadedFilePath = System.getProperty("user.dir") + File.separator + "/src/test/resources/template/Download/";
    public static String paypalTemplatePath = System.getProperty("user.dir") + File.separator + "/src/test/resources/template/PAYPAL";

    private static Map<String, String> getVendorData() {

        nodeList.addAll(Arrays.asList(XmlNode.LINE_ITEM_TEXT, XmlNode.REFERENCE, XmlNode.TIME_STAMP_1, XmlNode.AMOUNT, XmlNode.TOTAL_VALUE, XmlNode.DOCUMENT_DATE,
                XmlNode.REMARKS, XmlNode.STATUS));
        Map<String, String> map = new HashMap<String, String>();
        try {
            document = getDocument(getTemplateFile(BatchFile.VENDOR_FILE));
            for (int j = 0; j < document.getDocumentElement().getChildNodes().getLength(); j++) {
                NodeList node = document.getDocumentElement().getChildNodes().item(j).getChildNodes();
                for (int k = 0; k < node.getLength(); k++) {
                    for (String nodeName : nodeList) {
                        if (node.item(k).getNodeName().equals(nodeName)) {
                            map.put(nodeName, node.item(k).getTextContent().trim());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void mockNoticeFile(String batchFile, Map<String, String> updatedMap) {

        try {
            document = getDocument(getTemplateFile(batchFile));
            DocumentTraversal trav = (DocumentTraversal) document;
            NodeIterator it = trav.createNodeIterator(document.getDocumentElement(),
                    NodeFilter.SHOW_ELEMENT, null, true);

            for (Node node = it.nextNode(); node != null; node = it.nextNode()) {
                String name = node.getNodeName();
                for (String nodeName : updatedMap.keySet()) {
                    if (name.equals(nodeName)) {
                        node.setTextContent(updatedMap.get(nodeName));
                    }
                }
                if (name.equals("noticeAmount")) {
                    node.setTextContent(updatedMap.get("offenceAmount"));
                }else if(name.equals("noticeNo")){
                    String noticeNumber = getNoticeNo();
                    logger.info(()->"Notice number is "+ noticeNumber);
                    node.setTextContent(noticeNumber);
                }else if(name.equals("issueDate")){
                    node.setTextContent(String.valueOf(getIssueDate()));
                }else if(name.equals("expiryDate")){
                    node.setTextContent(String.valueOf(getExpiryDate()));
                }
            }
            saveXml(document, getNoticeFilePath(batchFile));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editNoticeFile(String batchFile, Map<String, String> updatedMap) {

        try {
            document = getDocument(new File(getXmlFile(batchFile)));
            DocumentTraversal trav = (DocumentTraversal) document;
            NodeIterator it = trav.createNodeIterator(document.getDocumentElement(),
                    NodeFilter.SHOW_ELEMENT, null, true);

            for (Node node = it.nextNode(); node != null; node = it.nextNode()) {
                String name = node.getNodeName();
                for (String nodeName : updatedMap.keySet()) {
                    if (name.equals(nodeName)) {
                        node.setTextContent(updatedMap.get(nodeName));
                    }
                }
                if (name.equals("noticeAmount")) {
                    node.setTextContent(updatedMap.get("offenceAmount"));
                }
            }
            saveXml(document, getUpdatedNoticeFilePath(batchFile));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mockXmlFile(String batchFile, String refundMethod) {

        Map<String, String> updatedMap = updatedElementMap(getVendorData());
        if (batchFile.equalsIgnoreCase(BatchFile.AP_STATUS_FILE) && refundMethod.equalsIgnoreCase("Cheque")) {
            updatedMap.put(XmlNode.STATUS, XmlNode.SAPST_CHEQUE_STATUS);
        } else if (batchFile.equalsIgnoreCase(BatchFile.AP_STATUS_FILE) && refundMethod.equalsIgnoreCase("GIRO")) {
            updatedMap.put(XmlNode.STATUS, XmlNode.SAPST_GIRO_STATUS);
            updatedMap.put(XmlNode.REFERENCE, PropertiesUtil.getProperties("refund.id"));
        } else if (batchFile.equalsIgnoreCase(BatchFile.SAP_CLEARENCE_FILE) && refundMethod.equalsIgnoreCase("GIRO")) {
            updatedMap.put(XmlNode.REMARKS, XmlNode.GIRO_REMARKS);
            updatedMap.put(XmlNode.REFERENCE,PropertiesUtil.getProperties("refund.id"));
            updatedMap.put(XmlNode.STATUS, XmlNode.APRFND_GIRO_STATUS);
        } else if (batchFile.equalsIgnoreCase(BatchFile.SAP_CLEARENCE_FILE) && refundMethod.equalsIgnoreCase("Cheque")) {
            updatedMap.put(XmlNode.REMARKS, XmlNode.CHEQUE_REMARKS);
            updatedMap.put(XmlNode.STATUS, XmlNode.APRFND_CHEQUE_STATUS);
        }

        try {
            document = getDocument(getTemplateFile(batchFile));
            for (int j = 0; j < document.getDocumentElement().getChildNodes().getLength(); j++) {
                NodeList node = document.getDocumentElement().getChildNodes().item(j).getChildNodes();
                for (int k = 0; k < node.getLength(); k++) {
                    for (String nodeName : nodeList) {
                        if (node.item(k).getNodeName().equals(nodeName)) {
                            node.item(k).setTextContent(updatedMap.get(nodeName));
                        }
                    }
                }
            }
            saveXml(document, getMockedFilePath(batchFile));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileName(String fileName) {
        String updatedFileName = fileName + getFileNameDate() + ".xml";
        return updatedFileName;
    }

    private static String getCurrentYear() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    static String getFileNameDate() {
        return new SimpleDateFormat("yyyyMMddHHMMss").format(new Date());
    }

    private static String getIssueDate() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.MONTH,2);
        return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
    }

    private static String getExpiryDate() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR,1);
        cal.add(Calendar.MONTH,1);
        return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
    }
    private static Document getDocument(File filePath) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(filePath);
        document.getDocumentElement().normalize();
        return document;
    }

    private static void saveXml(Document document, String mockedFilePath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        document.setXmlStandalone(true);
        document.setXmlVersion("1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new File(mockedFilePath)));
    }

    public static File getTemplateFile(String type) {
        return Arrays.asList(new File(templatePath).listFiles()).stream().filter(file -> file.getName().contains(type)).findFirst().get();
    }

    public static File getPaypalTemplateFile(String type) {
        return Arrays.asList(new File(paypalTemplatePath).listFiles()).stream().filter(file -> file.getName().contains(type)).findFirst().get();
    }
    public static void delSourceFile(String directory) {
        try {
            Arrays.asList(new File(directory).listFiles())
                    .stream().forEach(file -> file.delete());
        }catch (Exception e){
            System.out.println(String.format("%s has not files to be deleted...",directory));
        }
    }

    public static String getMockedFilePath(String text) {
        return MockBankStatement.targetPath + File.separator + getFileName(getTemplateFile(text).getName().split("_d_")[0] + "_d_");
    }

    public static String getUpdatedNoticeFilePath(String text) {
        String filePath = Arrays.asList(new File(MockBankStatement.targetPath).listFiles()).stream().filter(file -> file.getName().contains(text)).findFirst().get().getPath();
        return filePath.split("-D-")[0] + "UPD" + "-D-" + filePath.split("-D-")[1];
    }
    public static String getNoticeFilePath(String text) {
        return MockBankStatement.targetPath + File.separator + getFileName(getTemplateFile(text).getName().split("-D-")[0] + "-D-");
    }

    public static String getXmlFile(String type) {
        return Arrays.asList(new File(MockBankStatement.targetPath).listFiles()).stream().filter(file -> file.getName().contains(type)).findFirst().get().getPath();
    }

    private static Map<String, String> updatedElementMap(Map<String, String> map) {
        nodeList.addAll(Arrays.asList(XmlNode.AMOUNT_PAID, XmlNode.TOTAL_VALUE, XmlNode.FISCAL_YEAR, XmlNode.TIME_STAMP_2,
                XmlNode.PAYMENT_DOCUMENT_DATE, XmlNode.VALUE_DATE));
        map.put(XmlNode.AMOUNT_PAID, map.get(XmlNode.AMOUNT));
        map.put(XmlNode.TOTAL_VALUE, map.get(XmlNode.AMOUNT));
        map.put(XmlNode.FISCAL_YEAR, getCurrentYear());
        map.put(XmlNode.PAYMENT_DOCUMENT_DATE, map.get(XmlNode.DOCUMENT_DATE));
        map.put(XmlNode.VALUE_DATE, map.get(XmlNode.DOCUMENT_DATE));
        map.put(XmlNode.TIME_STAMP_2, map.get(XmlNode.TIME_STAMP_1));
        return map;
    }

    private static String getProperties(String key) {
        Properties p = new Properties();
        String value = null;
        try {
            p.load(new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "config.properties")));
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String getNoticeNo() {

        String noticeNo = null;
        for(int i=0 ; i<200 ; i++) {
            Random rnd = new Random();
            int n = 10 + rnd.nextInt(90);
            String tempNoticeNo = getIssueDate() + "" + n;
            if(!getexistingNoticeID().contains(tempNoticeNo)) {
                noticeNo = tempNoticeNo;
                break;
            }
        }
        if(noticeNo==null)
            throw new RuntimeException("Unable to create notice number");

        PropertiesUtil.setProperties("CCRS.NoticeNo", noticeNo);
        return noticeNo;
    }

    public static void main(String[] args) {
        delSourceFile("C:\\Users\\anilkodam\\CCRS_UI_Automation\\CCRS\\src\\test\\resources\\mockedFile\\ABT\\");
    }
}
