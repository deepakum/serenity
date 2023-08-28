package Utility.ABT;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SSF {
    private String batch;
    private String date;
    private String providerId;
    private String packageID ;
    private String passTypeID;
    private String fundingSource;
    private String totalTransferFares;
}
