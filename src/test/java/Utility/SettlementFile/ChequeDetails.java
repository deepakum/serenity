package Utility.SettlementFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChequeDetails {
    private String chequeNo = "123456";
    private String bankCode;
    private String branchCode;
    private String accountNo="12345678901";
}
