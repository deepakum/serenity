package Utility.ExcelTransformer;

import java.io.File;
import java.util.Objects;

public class ExcelConfiguration {

    private final String fileName;
    private final String fileLocation = System.getProperty("user.dir")+ File.separator+"/src/test/resources/";
    private final String sheetName;
    private int index = -1;

    private ExcelConfiguration(String fileName, String sheetName, int index) {
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.index = index;
    }

    public static class ExcelConfigurationBuilder {

        private String fileName;
        private String fileLocation;
        private String sheetName;
        private int index = -1;

        public ExcelConfigurationBuilder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ExcelConfigurationBuilder setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
            return this;
        }

        public ExcelConfigurationBuilder setIndex(int index) {
            this.index = index;
            return this;
        }

        public ExcelConfigurationBuilder setSheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ExcelConfiguration build() {
            Objects.requireNonNull(fileName);
            Objects.requireNonNull(fileLocation);
            Objects.requireNonNull(sheetName);

            return new ExcelConfiguration(fileName, sheetName, index);

        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getIndex() {
        return index;
    }
}
