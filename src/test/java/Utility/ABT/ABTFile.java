package Utility.ABT;

import java.io.File;

public interface ABTFile {

    String FILE_TYPE = "csv";
    String WOSF_FILE = "LTA-ABT-CCRS-WOSF-D-";
    String RSF_FILE = "LTA-ABT-CCRS-RSF-D-";
    String CSF_FILE = "LTA-ABT-CCRS-CSF-D-";
    String SSF_FILE = "LTA-ABT-CCRS-SSF-D-";
    String ACK1_FILE = "ACK1";
    String ACK2_FILE = "ACK2";
    String ACK3_FILE = "ACK3";
    String PPSSF_FILE = "LTA-ABT-CCRS-PPSSF-D-";
    String BCSF_FILE = "LTA-ABT-CCRS-BCSF-D-";
    String GCSF_FILE = "LTA-ABT-CCRS-GCSF-D-";
    String CTSF_FILE = "LTA-ABT-CCRS-CTSF-D-";
    String FILE_NAME = "fileName";
    String TRANSACTION_DATE = "transactionDate";
    String AMOUNT = "amount";
    String CHECKSUM_VALUE = "checksumValue";
    String TRANSACTION = "transaction";
    int INT_COUNT = 11;
    int DECIMAL_COUNT = 2;
    String TEMPLATE_PATH = System.getProperty("user.dir") + File.separator + "/src/test/resources/template/ABT/";
    String INSTRUCTION_TEMPLATE_PATH = System.getProperty("user.dir") + File.separator + "/src/test/resources/template/ABT/instructionFile/";
    String MOCKED_FILE_PATH = System.getProperty("user.dir")+"/src/test/resources/mockedFile/ABT/";
    public static String SFTP_BASE_PATH = "/app/CCBPRSIT/ouaf/SFTP/";
    public static String SFTP_DIRECTORY = "/working/";
    public static String SFTP_DIRECTORY_UPPERCASE = "/Working/";
    String WOSF_GL_FILE = "lta_ccrs_fs_gl_d";
}
