package Utility.Server;
import Utility.ABT.ABTFile;
import Utility.BatchJob.BatchControl;
import Utility.CSV.DateUtil;
import Utility.Common.FileHandler;
import Utility.SettlementFile.ABT;
import Utility.others.BatchFile;
import com.jcraft.jsch.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;

import java.io.*;
import java.nio.file.spi.FileTypeDetector;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static Utility.SettlementFile.MockXml.*;

public class ServerUtil {

    private static final int REMOTE_PORT = 22;
    private static final String REMOTE_HOST = "177.26.254.229";
    private static final String USERNAME = "cissys";
    private static final int SESSION_TIMEOUT = 30000;
    private static final int CHANNEL_TIMEOUT = 5000;
    private static String keyPath = System.getProperty("user.dir")+"/src/test/resources/helper/id_rsa_229.ppk";


    public static void uploadFile(String source, String destination) {
        Session session = null;
        String keyPath = System.getProperty("user.dir")+"/src/test/resources/helper/id_rsa_229.ppk";

        try {
            JSch jSch = new JSch();
            jSch.addIdentity(keyPath);
            session = jSch.getSession(USERNAME,REMOTE_HOST,REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.cd(destination);
            cleanWorkingFolder(channelSftp,destination, FilenameUtils.getExtension(source));
            InputStream inputStream = new FileInputStream(source);
            //Filename will be extracted from source path to be used in put command
            String mockedFileName = new File(source).getName();
            channelSftp.put(inputStream,mockedFileName);
            channelSftp.exit();
            inputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) session.disconnect();
        }
        File file = new File(source);
        file.delete();
    }

    public static void uploadFile(String source, String destination, boolean deleteMockedFile, String fileType) {
        Session session = null;
        String keyPath = System.getProperty("user.dir")+"/src/test/resources/helper/id_rsa_132.ppk";

        try {
            JSch jSch = new JSch();
            jSch.addIdentity(keyPath);
            session = jSch.getSession(USERNAME,REMOTE_HOST,REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.cd(destination);
            cleanWorkingFolder(channelSftp,destination, FilenameUtils.getExtension(source));
            InputStream inputStream = new FileInputStream(source);
            //Filename will be extracted from source path to be used in put command
            String mockedFileName = new File(source).getName();
            channelSftp.put(inputStream,mockedFileName);
            channelSftp.exit();
            inputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if(session != null) session.disconnect();
        }
        if(deleteMockedFile) {
            FileHandler.deleteFile(fileType);
        }
    }

    public static void downloadInstructionFile(String instructionFileDirectory, String destinationDirectory) {

        delSourceFile(BatchFile.VENDOR_FILE);
        Session session = null;
        try {
            session = getChannelSftp();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd(instructionFileDirectory);
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls(instructionFileDirectory);
            int fileCount = 0;
            for (ChannelSftp.LsEntry entry : list) {
                if (entry.getFilename().contains(ABT.getTransactionDate())) {
                    fileCount += 1;
                    String instructionFile = entry.getFilename();
                    String instructionFileServerPath = instructionFileDirectory + instructionFile;
                    FileOutputStream fileOutputStream = new FileOutputStream(destinationDirectory + instructionFile);
                    channelSftp.get(instructionFileServerPath,fileOutputStream);
                    fileOutputStream.close();
                }
        }
            channelSftp.exit();
            if(!(fileCount>0))
                throw new RuntimeException("Instruction file not created...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) session.disconnect();
        }
    }

    public static void downloadFile(String serverDirectory, String destination) {

        delSourceFile(BatchFile.VENDOR_FILE);
        Session session = null;
        String file = null;
        try {
            session = getChannelSftp();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.cd(serverDirectory);
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls(serverDirectory+"*.xml");
            ChannelSftp.LsEntry lastModifiedEntry = Collections.max(list, Comparator.comparing(entry->entry.getAttrs().getMTime()));
            file = lastModifiedEntry.getFilename();
            String vendorFileServerPath = serverDirectory + file;

            FileOutputStream fileOutputStream = new FileOutputStream(destination + file);
            channelSftp.get(vendorFileServerPath,fileOutputStream);
            channelSftp.exit();
            fileOutputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) session.disconnect();
        }
    }

    public static void cleanWorkingFolder(ChannelSftp channelSftp,String workingDirectory,String fileType) {

        try {
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls(workingDirectory);
            for(ChannelSftp.LsEntry entry : list){
                if(entry.getFilename().endsWith(".".concat(fileType))) {
                    if(!entry.getFilename().contains(ABTFile.CSF_FILE))
                        channelSftp.rm(workingDirectory + entry.getFilename());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Session getChannelSftp() throws Exception{

        JSch jSch = new JSch();
        jSch.addIdentity(keyPath);
        Session session = jSch.getSession(USERNAME,REMOTE_HOST,REMOTE_PORT);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        return session;
    }

    public static void readAbtFile(String fileDirectory) {

        delSourceFile(BatchFile.VENDOR_FILE);
        Session session = null;
        String vendorFile = null;
        try {
            session = getChannelSftp();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.cd(fileDirectory);
            Vector<ChannelSftp.LsEntry> listOfFile = channelSftp.ls(fileDirectory + "*." + ABTFile.FILE_TYPE);

            List<String> checkSumFileList = getFileList(listOfFile, ABTFile.CSF_FILE);
            List<String> abtFileList = getFileList(listOfFile, ABTFile.WOSF_FILE);
            checkAbtAndCheckSumFile(checkSumFileList,abtFileList);
            for(String checkSumFile : checkSumFileList){
                boolean matchFound = false;
                String csfFileDate = getDigitValue(checkSumFile);
                String abtFileDate = null;
                for (String abtFile : abtFileList ){
                    abtFileDate = getDigitValue(abtFile);
                    if(csfFileDate.equalsIgnoreCase(abtFileDate)){
                        matchFound = true;
                    }
                }
                if(!matchFound) throw new RuntimeException(String.format("Missing file for date %s",abtFileDate));
            }

            for(String csfFile : checkSumFileList) {
                BufferedReader br = new BufferedReader(new InputStreamReader(channelSftp.get(fileDirectory + csfFile)));
                String line;
                Map<String, String> csfMap = new HashMap<>();

                for (String abtFile : abtFileList) {
                    while ((line = br.readLine()) != null) {
                        if (line.contains(abtFile)) {
                            List<String> tempList = Arrays.asList(line.split(","));
                            csfMap.put(ABTFile.FILE_NAME, checkWhitespace(tempList.get(0)));
                            csfMap.put(ABTFile.TRANSACTION_DATE, checkWhitespace(checkDate(tempList.get(1),csfFile)));
                            csfMap.put(ABTFile.TRANSACTION, checkWhitespace(tempList.get(2)));
                            csfMap.put(ABTFile.AMOUNT, checkWhitespace(checkIntegerAndDecimalPlace(tempList.get(3))));
                            csfMap.put(ABTFile.CHECKSUM_VALUE, checkWhitespace(tempList.get(4)));
                        }
                    }
                    verifyAbtFile(channelSftp.get(fileDirectory + abtFile),csfMap);
                }
            }


//            ChannelSftp.LsEntry lastModifiedEntry = Collections.max(list, Comparator.comparing(entry->entry.getAttrs().getMTime()));
//            vendorFile = lastModifiedEntry.getFilename();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) session.disconnect();
        }
    }

    private static void verifyAbtFile(InputStream inputStream, Map<String, String> csfMap) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Double amount = 0.0;
        String abtFileDate = null;
        while((line=br.readLine())!=null){
            System.out.println(line);
            List<String> tempList = Arrays.asList(line.split(","));
            amount += Double.parseDouble(tempList.get(4));
            abtFileDate = tempList.get(0);
        }
        checkAmount(amount, Double.parseDouble(csfMap.get(ABTFile.AMOUNT)));
        checkTransactionDate(abtFileDate, csfMap.get(ABTFile.TRANSACTION_DATE));
    }

    private static void checkTransactionDate(String abtFileDate, String chkSumFileDate) {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd",Locale.ENGLISH);
        try {
            Date date = sdf.parse(abtFileDate);
            Date date1 = sdf.parse(chkSumFileDate);
            long difference_In_Time = date1.getTime()-date.getTime();
            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;
            System.out.println(difference_In_Days);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private static void checkAbtAndCheckSumFile(List<String> csfList, List<String> abtFileList){
        Assert.assertTrue(csfList.size() == abtFileList.size());
    }
    private static String checkIntegerAndDecimalPlace(String amount){
        int integerPlaces = amount.replaceAll("[^0-9.]","").trim().indexOf('.');
        int decimalPlaces = amount.replaceAll("[^0-9.]","").length() - integerPlaces - 1;
        Assert.assertTrue(integerPlaces==ABTFile.INT_COUNT);
        Assert.assertTrue(decimalPlaces==ABTFile.DECIMAL_COUNT);
        return amount;
    }

    private static void checkAmount(Double chksumAmount, Double abtFileAmount){
        DecimalFormat df = new DecimalFormat("####0.00");
        Assert.assertTrue(df.format(chksumAmount).equals(df.format(abtFileAmount)));
    }
    private static String checkDate(String dateInString, String fileName) {
        Assert.assertTrue(dateInString.equalsIgnoreCase(getDigitValue(fileName)));
        return dateInString;
    }
    public static String checkWhitespace(String str) throws Exception {
        if (Character.isWhitespace(str.charAt(0)) || Character.isWhitespace(str.charAt(str.length() - 1))) {
            throw new Exception(String.format("Whitespaces found in %s",str));
        }else{
            return str;
        }
    }
    private static String getDigitValue(String fileName) {
        Pattern digitRegex = Pattern.compile("\\d+");
        Matcher matcher = digitRegex.matcher(fileName);
        String fileDate = null;
        if (matcher.find()) {
            fileDate = matcher.group();
        }
        return fileDate;
    }
    private static List<String> getFileList(Vector<ChannelSftp.LsEntry> listOfFile, String fileType){
        return listOfFile.stream().filter(file->
                file.getFilename().contains(fileType)).map(f->f.getFilename()).collect(Collectors.toList());
    }

    public static void editArchivedFile(String serverPath, String fileName, String newFilename) {

        Session session = null;
        String vendorFile = null;
        try {
            session = getChannelSftp();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.cd(serverPath);
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls(serverPath);
            list.stream().forEach(System.out::println);
            channelSftp.rename(fileName, newFilename);
            channelSftp.exit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) session.disconnect();
        }
    }
    public static String geBatchworkingFolderPath(String batchName){
        return ABTFile.SFTP_BASE_PATH+ batchName+ ABTFile.SFTP_DIRECTORY;
    }
    public static String geBatchWorkingFolderPath(String batchName){
        return ABTFile.SFTP_BASE_PATH+ batchName+ ABTFile.SFTP_DIRECTORY_UPPERCASE;
    }

    public static void main(String[] args) throws Exception {
        getChannelSftp();
    }
}
