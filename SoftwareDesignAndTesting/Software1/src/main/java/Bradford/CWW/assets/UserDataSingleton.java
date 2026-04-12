package Bradford.CWW.assets;

public class UserDataSingleton {
    private static UserData userData;

    private UserDataSingleton() {

    }
    public static UserData getInstance() {
        if (userData == null) {
            userData = new UserData();
        }
        return userData;
    }

    public static void resetInstance() {
        userData = null;
    }
}
