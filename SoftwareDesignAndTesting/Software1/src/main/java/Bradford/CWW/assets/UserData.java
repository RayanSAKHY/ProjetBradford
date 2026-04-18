package Bradford.CWW.assets;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    private Map<String,User> users;

    public UserData(){
        users = new HashMap<String,User>();
        addingTestUsers();
    }

    public void addingTestUsers() {
        users.put("",new User());
        users.put("test",new User("test","azerty"));
        users.put("I lost",new User("I lost","the game"));
    }

    public boolean usernameExists(String username) {
        return users.containsKey(username);
    }

    public User getUser(String username){
        return users.get(username);
    }

    public void addUser(String username,String password) {
        users.put(username, new User(username,password));
    }
}
