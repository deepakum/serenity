package Utility.others;

public class Config {

    private static String currentUser = "Admin";
    private static String userName;
    private static String name;

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        Config.currentUser = currentUser;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Config.userName = userName;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Config.name = name;
    }
}
