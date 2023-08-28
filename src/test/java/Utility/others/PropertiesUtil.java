package Utility.others;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesUtil {

    static String path = System.getProperty("user.dir")+ File.separator+"src/test/resources/"+File.separator+ "serenity.properties";

    public static void main(String[] args){
        setProperties("refund.amount","Deepak");
    }

    public static void setProperties(String key, String value){
        if(value!=null && key!=null) {
            FileOutputStream out = null;
            try {
                Properties p = new Properties();
                FileInputStream in = new FileInputStream(path);
                p.load(in);
                in.close();
                out = new FileOutputStream(path, false);
                p.setProperty(key, value);
                p.store(out, null);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public static String getProperties(String key){
        String value = null;
        try {
            Properties p = new Properties();
            FileInputStream inputStream = new FileInputStream(path);
            p.load(inputStream);
            value = p.getProperty(key);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return value;
    }
}
