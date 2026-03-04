package Bradford.CWW;

import Bradford.CWW.MFA.IMFAStrategy;
import Bradford.CWW.asssets.User;

import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;
import java.util.Scanner;

public class Login {
    private MFALogin mfaLogin = new MFALogin();
    private Map<String, User> users = new HashMap<>();


    public Login() {
        users.put("",new User());
        users.put("test",new User("test","azerty"));
    }

    public boolean login(String username, String password) {
        boolean connected = false;
        int nbEssai = 0;
        credentialsPrint(username, password);
        if (users.containsKey(username)){
            User user = users.get(username);
            if (user.getPassword().equals(password)) {
                if (mfaLogin.twoStepVerif(user)) {
                    connected = true;
                }
            }
            else {
                System.out.println("Wrong Credentials. Retry in a short time");
                nbEssai++;
            }
        }
        else {
            System.out.println("Wrong Credentials. Retry in a short time");
            nbEssai++;
        }


        if (connected) {
            System.out.println("Login successful");
        }
        else {
            System.out.println("Login failed");
        }
        return connected;
    }



    private void credentialsPrint(String username, String password) {
        System.out.println("Username: "+username);
        StringBuilder hiddenPassword= new StringBuilder();
        hiddenPassword.append("*".repeat(password.length()));
        System.out.println("Password: "+hiddenPassword);
    }
}
