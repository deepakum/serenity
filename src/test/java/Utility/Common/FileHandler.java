package Utility.Common;

import Utility.SettlementFile.MockBankStatement;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    public static void deleteFile(String fileType){
        List<File> filePath = Arrays.asList(new File(MockBankStatement.targetPath).listFiles()).stream().filter(file -> file.getName().endsWith(".".concat(fileType))).collect(Collectors.toList());
        filePath.stream().forEach(File::delete);
    }

    public static File getFile(String fileDirectory, String type) {
            return Arrays.asList(new File(fileDirectory).listFiles()).stream().filter(file -> file.getName().contains(type)).findFirst().get();
    }

    public static List<String> readFile(String fileDirectory, String fileType){
        File file = getFile(fileDirectory,fileType);
        List<String> listData = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            listData = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listData;
    }
    public static void main(String[] args) {

    }
}
