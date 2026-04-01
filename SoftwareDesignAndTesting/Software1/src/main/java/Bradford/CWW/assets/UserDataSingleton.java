package Bradford.CWW.assets;

public class UserDataSingleton {
    private UserData userData;

    public UserData getInstance() {
        if (userData == null) {
            userData = new UserData();
        }
        return userData;
    }

}
