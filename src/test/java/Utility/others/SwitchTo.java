package Utility.others;

public class SwitchTo {

    private static int defaultHandle = 0;
    private static int currentHandle;


    public static int getDefaultHandle() {
        return defaultHandle;
    }

    public static void setDefaultHandle(int defaultHandle) {
        SwitchTo.defaultHandle = defaultHandle;
    }

    public static int getCurrentHandle() {
        return currentHandle;
    }

    public static void setCurrentHandle(int currentHandle) {
        SwitchTo.currentHandle = currentHandle;
    }
}
