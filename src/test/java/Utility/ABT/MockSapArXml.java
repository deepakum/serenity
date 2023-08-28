package Utility.ABT;

import Utility.BatchJob.BatchControl;
import Utility.SettlementFile.MockBankStatement;
import Utility.SettlementFile.XmlNode;
import Utility.others.BatchFile;
import Utility.others.Helper;
import Utility.others.PropertiesUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import javax.imageio.stream.FileImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class MockSapArXml {

    static Logger logger = LoggerFactory.getLogger(MockSapArXml.class);
    private static Document document;
    static Map<String, String> map = new HashMap<String, String>();
    static List<String> nodeList = new ArrayList<String>();
    public static String templatePath = System.getProperty("user.dir") + File.separator + "/src/test/resources/template/ABT/";
    public static String paypalTemplatePath = System.getProperty("user.dir") + File.separator + "/src/test/resources/template/PAYPAL";
    static String totalValue;
    static Map<String, String> mapTT = new HashMap<String, String>();
    public static List<String> pdfDateList = new ArrayList<>();
    private static List<Map<String, String>> getSapArData() {

        List<Map<String,String>> listMap = new ArrayList<>();

        try {
            document = getDocument(getTemplateFile(BatchFile.SAPAR));
            NodeList nList = document.getElementsByTagName("BH");
            String pdfDate;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Map<String, String> map = new HashMap<String, String>();
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    map.put(XmlNode.RECORD_TYPE,"BB");
                    map.put(XmlNode.COMPANY_CODE,eElement.getElementsByTagName(XmlNode.COMPANY_CODE).item(0).getTextContent());
                    map.put(XmlNode.DOCUMENT_DATE,eElement.getElementsByTagName(XmlNode.DOCUMENT_DATE).item(0).getTextContent());
                    map.put(XmlNode.POSTING_DATE,eElement.getElementsByTagName(XmlNode.POSTING_DATE).item(0).getTextContent());
                    map.put(XmlNode.CURRENCY,eElement.getElementsByTagName(XmlNode.CURRENCY).item(0).getTextContent());
                    map.put(XmlNode.EXCHANGE_RATE,eElement.getElementsByTagName(XmlNode.EXCHANGE_RATE).item(0).getTextContent());
                    map.put(XmlNode.REFERENCE,eElement.getElementsByTagName(XmlNode.REFERENCE).item(0).getTextContent());
                    String headerText = eElement.getElementsByTagName(XmlNode.HEADER_TEXT).item(0).getTextContent();
                    map.put(XmlNode.HEADER_TEXT,headerText);
                    map.put(XmlNode.FISCAL_YEAR,String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                    Helper.customWait(1000);
                    pdfDate = getFileNameDate();
                    pdfDateList.add(pdfDate);
                    listMap.add(map);
                }
            }

            NodeList ttList = document.getElementsByTagName("TT");
            for (int temp = 0; temp < ttList.getLength(); temp++) {
                Node node = ttList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    mapTT.put(XmlNode.RECORD_TYPE,eElement.getElementsByTagName(XmlNode.RECORD_TYPE).item(0).getTextContent());
                    mapTT.put(XmlNode.TOTAL_RECORDS,String.valueOf(listMap.size()));
                    mapTT.put(XmlNode.TOTAL_VALUE,eElement.getElementsByTagName(XmlNode.TOTAL_VALUE).item(0).getTextContent());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }

    public static String mockXmlFile(String batchFile) {

        List<Map<String, String>> listOfMaps = getSapArData();

        try {
            document = getDocument(getTemplateFile(batchFile));
            Element root = document.getDocumentElement();
            for(Map<String,String> map : listOfMaps) {
                Element newServer = document.createElement("BB");
                for(String key : map.keySet()){
                    Element name = document.createElement(key);
                    name.appendChild(document.createTextNode(map.get(key)));
                    newServer.appendChild(name);
                    root.appendChild(newServer);
                }
            }
            Element newServer = document.createElement("TT");
            for(String key : mapTT.keySet()){
                Element name = document.createElement(key);
                name.appendChild(document.createTextNode(mapTT.get(key)));
                newServer.appendChild(name);
                root.appendChild(newServer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
       return  saveXml(document, getMockedFilePath(batchFile));
    }

    public static String copyPdfFile(String pdfFileDate){
        File abtTemplateFile = Arrays.stream(new File(ABTFile.TEMPLATE_PATH).listFiles()).filter(file->file.getName().contains("pdf")).findFirst().get();
        String filePath;
        try {
            filePath = ABTFile.MOCKED_FILE_PATH+File.separator+abtTemplateFile.getName().split("_m_")[0]+"_m_"+pdfFileDate+".pdf";
            FileUtils.copyFile(abtTemplateFile, new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }
    public static String getFileName(String fileName) {
        String updatedFileName = fileName + getFileNameDate() + ".xml";
        return updatedFileName;
    }

    private static String getCurrentYear() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    static String getFileNameDate() {
        return new SimpleDateFormat("yyyyMMddHMs").format(new Date());
    }

    public static Document getDocument(File filePath) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(filePath);
        document.getDocumentElement().normalize();
        return document;
    }

    private static String saveXml(Document document, String mockedFilePath) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
            document.setXmlStandalone(true);
            document.setXmlVersion("1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new File(mockedFilePath)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mockedFilePath;
    }

    public static File getTemplateFile(String type) {
        return Arrays.asList(new File(templatePath).listFiles()).stream().filter(file -> file.getName().contains(type)).findFirst().get();
    }

    public static String getMockedFilePath(String text) {
        return MockBankStatement.abtTargetPath + File.separator + getFileName(getTemplateFile(text).getName().split("_m_")[0] + "_m_");
    }

    public static String getXmlFile(String type) {
        return Arrays.asList(new File(MockBankStatement.targetPath).listFiles()).stream().filter(file -> file.getName().contains(type)).findFirst().get().getPath();
    }


    public static void main(String[] args) throws IOException {

    }
}
