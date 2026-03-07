package Bradford.CWW.asssets;

public class User{

    private String username;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setHashedPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {
        this("","");
    }

    public User(String username, String hashedPassword) {
        this.username = username;
        this.password = hashedPassword;
    }
}
