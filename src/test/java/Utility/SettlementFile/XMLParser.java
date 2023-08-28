package Utility.SettlementFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLParser {

    public static void main(String[] args){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("C:\\Users\\anilkodam\\Desktop\\Refund0112\\lta_ccrs_fs_aprfnd_d_20221201050348.xml"));
            doc.getDocumentElement().normalize();
            Node node = doc.getFirstChild();
            System.out.println(doc.getElementsByTagName("LineItemText").item(0).getNodeValue());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
