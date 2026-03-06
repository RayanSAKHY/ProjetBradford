package Bradford.CWW;

import Bradford.CWW.MFA.*;
import Bradford.CWW.asssets.User;

import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;
import java.util.Scanner;

public class Login {
    private MFALogin mfalogin;
    private Map<String, User> users = new HashMap<>();

    public Login() {
        users.put("",new User());
        users.put("test",new User("test","azerty"));
    }

    public boolean login(String username, String password) {
        IMFAStrategy strategy = null;
        boolean connected = false;
        int nbEssai = 0;
        while (nbEssai < 3 && !connected) {
            credentialsPrint(username, password);
            if (users.containsKey(username)){
                User user = users.get(username);
                if (user.getPassword().equals(password)) {
                    String input = MFAChoice();
                    try {
                        int choice = StringToInt(input);
                        switch (choice) {
                            case 1:
                                strategy = new PhoneCall();
                                break;
                            case 2:
                                strategy = new AppNotification();
                                break;
                            case 3:
                                strategy = new CodeSentByEmail();
                                break;
                            case 4:
                                strategy = new CodeSentBySMS();
                                break;
                            case 5:
                                strategy = new AuthentificatorApp();
                                break;
                            default:
                                nbEssai++;
                                if (nbEssai < 3) {
                                    System.out.println("Please enter a valid choice");
                                }
                                break;
                        }

                        mfalogin = new MFALogin(strategy);
                        connected =  mfalogin.twoStepVerif(user);
                        if (!connected) {
                            System.out.println("Two Step Verification failed ");
                            nbEssai++;
                        }
                    }
                    catch (NumberFormatException ex) {
                        nbEssai++;
                        if (nbEssai < 3) {
                            System.out.println("Please enter a number between 1 and 5");
                        }
                    }
                }
                else {
                    System.out.println("Wrong Credentials. Retry in a short time");
                    nbEssai++;
                    break;
                }
            }
            else {
                System.out.println("Wrong Credentials. Retry in a short time");
                nbEssai++;
                break;
            }
            if (!connected && nbEssai <3) {
                System.out.println("There are "+(3-nbEssai)+" attempts left\n");
            }
        }

        if (connected) {
            System.out.println("Login successful");
        }
        else {
            System.out.println("Login failed");
        }
        return connected;
    }

    private String MFAChoice() {
        System.out.println("Which method do you prefer to verify that it's you : ");
        System.out.println("- Receiving a phone call (type 1)" );
        System.out.println("- Obtain a notification on an app (type 2)" );
        System.out.println("- Send a code by email (type 3)" );
        System.out.println("- Send a code by text message (type 4)" );
        System.out.println("- Check your authenticator app (type 5)" );
        Scanner sc = new Scanner(System.in);

        return sc.nextLine();
    }

    private int StringToInt(String input) throws NumberFormatException {
        return Integer.parseInt(input);
    }

    private void credentialsPrint(String username, String password) {
        System.out.println("Username: "+username);
        StringBuilder hiddenPassword= new StringBuilder();
        hiddenPassword.append("*".repeat(password.length()));
        System.out.println("Password: "+hiddenPassword);
    }
}
