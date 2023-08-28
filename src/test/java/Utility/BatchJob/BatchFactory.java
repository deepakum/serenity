package Utility.BatchJob;

public class BatchFactory {
    public static Batch getInstance(String batchType){

        if(batchType==null){
            return null;
        }
        if(batchType.equalsIgnoreCase(BatchControl.DEPOSIT_CONTROL)){
            return new CM_cdtpp();
        }
        return null;
    }
}
