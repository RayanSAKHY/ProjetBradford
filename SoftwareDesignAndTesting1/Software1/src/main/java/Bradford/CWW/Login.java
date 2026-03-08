package Bradford.CWW;

import Bradford.CWW.MFA.*;
import Bradford.CWW.asssets.User;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;
import java.util.Scanner;

public class Login {
    private MFALogin mfalogin;
    private Map<String, User> users = new HashMap<>();
    private Scanner scanner;
    private InputStream in;

    public Login(InputStream in) {
        users.put("",new User());
        users.put("test",new User("test","azerty"));
        this.in = in;
        scanner = new Scanner(in);
    }

    public Login() {
        this(System.in);
    }

    public boolean login(String username, String password) {
        IMFAStrategy strategy = null;
        boolean connected = false;
        int nbEssai = 0;
        while (nbEssai <= 3 && !connected) {
            System.out.println(credentialsPrint(username, password));
            if (users.containsKey(username)){
                User user = users.get(username);
                if (user.getPassword().equals(password)) {
                    String input = MFAChoice();
                    try {
                        int choice = Integer.parseInt(input);
                        switch (choice) {
                            case 1:
                                strategy = new PhoneCall(scanner);
                                break;
                            case 2:
                                strategy = new CodeSentByEmail(scanner);
                                break;
                            case 3:
                                strategy = new CodeSentBySMS(scanner);
                                break;
                            case 4:
                                strategy = new AuthentificatorApp(scanner);
                                break;
                            case 5:
                                strategy = new ConsoleMFA(scanner);
                                break;
                            default:
                                nbEssai++;
                                if (nbEssai < 3) {
                                    System.out.println("Please enter a valid choice");
                                }
                                break;
                        }

                        mfalogin = new MFALogin(strategy);
                        connected =  mfalogin.twoStepVerif();
                        if (!connected) {
                            System.out.println("Two Step Verification failed ");
                            nbEssai++;
                        }
                    }
                    catch (NumberFormatException ex) {
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
        System.out.println("- Send a code by email (type 2)" );
        System.out.println("- Send a code by text message (type 3)" );
        System.out.println("- Check your authenticator app (type 4)" );
        System.out.println("- Check your ability to write in the console (type 5)" );

        return scanner.nextLine();
    }

    public String credentialsPrint(String username, String password) {
        StringBuilder output = new StringBuilder();
        output.append("Username: ").append(username);
        output.append("\n");
        StringBuilder hiddenPassword= new StringBuilder();
        hiddenPassword.append("*".repeat(password.length()));
        output.append("Password: ").append(hiddenPassword);
        output.append("\n");
        return output.toString();
    }
}
