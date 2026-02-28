package Bradford.CWW;

import Bradford.CWW.MFA.*;
import Bradford.CWW.asssets.User;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {

    public static boolean login(String username, String password,Map<String,User> users) {
        IMFAStrategy strategy = null;
        boolean connected = false;
        int nbEssai = 0;
        while (nbEssai < 3 && !connected) {
            if (users.containsKey(username)){
                User user = users.get(username);
                if (user.getPassword().equals(password)) {
                    System.out.println("Which method do you prefer to verify that it's you : ");
                    System.out.println("- Receiving a phone call (type 1)" );
                    System.out.println("- Obtain a notification on an app (type 2)" );
                    System.out.println("- Send a code by email (type 3)" );
                    System.out.println("- Send a code by text message (type 4)" );
                    System.out.println("- Check your authenticator app (type 5)" );
                    Scanner sc = new Scanner(System.in);

                    String input =sc.nextLine();
                    try {
                        int choice = Integer.parseInt(input);
                        switch (choice) {
                            case 1:
                                strategy = new PhoneCall();
                                break;
                            case 2:
                                strategy = new AppNotification();
                                break;
                            case 3:
                                strategy = new Email();
                                break;
                            case 4:
                                strategy = new TextMessage();
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

                        if (strategy != null) {
                            connected = strategy.TwoStepVerif(user);
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

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Map<String,User> users = new HashMap<>();
        users.put("",new User());
        users.put("test",new User("test","azerty","hjl@gfd","333444555"));
        System.out.println(login("","",users));
//        System.out.println(login("test","",users));
//        System.out.println(login("tEst","",users));
//
//        login("test","",users);
    }
}
