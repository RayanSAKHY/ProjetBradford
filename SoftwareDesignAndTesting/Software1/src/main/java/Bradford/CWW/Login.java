package Bradford.CWW;

import Bradford.CWW.MFA.*;
import Bradford.CWW.assets.User;

import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;
import java.util.Scanner;

public class Login {
    private final Map<String, User> users;
    private final Scanner scanner;
    private final boolean testMode;


    public Login(Scanner scanner,boolean testMode, Map<String, User> users) {
        this.scanner = scanner;
        this.users = users;
        this.testMode = testMode;
        addingTestUsers();
    }

    public Login(Scanner scanner, boolean testMode) {
        this(scanner,testMode,new HashMap<>());
    }
    public Login(boolean testMode) {
        this(new Scanner(System.in),testMode, new HashMap<>());
    }

    public void addingTestUsers() {
        users.put("",new User());
        users.put("test",new User("test","azerty"));
        users.put("I lost",new User("I lost","the game"));
    }

    public boolean login(String username, String password) {
        IMFAStrategy strategy = null;
        boolean connected = false;
        int nbEssai = 0;
        while (nbEssai < 3 && !connected) {
            System.out.println(credentialsPrint(username, password));
            if (users.containsKey(username)){
                User user = users.get(username);
                if (user.getPassword().equals(password)) {
                    strategy = null;
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
                                strategy = new RandomSecretAuthentificatorApp(scanner,testMode);
                                break;
                            case 5:
                                strategy = new FixedSecretAuthentificator(scanner,user,testMode);
                                break;
                            default:
                                nbEssai++;
                                if (nbEssai < 3) {
                                    System.out.println("Please enter a valid choice");
                                }
                                break;
                        }

                        MFALogin mfalogin = new MFALogin(strategy);
                        connected =  mfalogin.twoStepVerif();
                        if (!connected && strategy != null) {
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
                    break;
                }
            }
            else {
                System.out.println("Wrong Credentials. Retry in a short time");
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
        System.out.println("- Scan a QRCode in an Authentificator app (type 4)" );
        System.out.println("- Check the code in your Authentificator app (type 5)" );

        return scanner.nextLine();
    }

    public String credentialsPrint(String username, String password) {
        StringBuilder output = new StringBuilder();
        output.append("Username: ").append(username);
        output.append("\n");
        output.append("Password: ").append("*".repeat(password.length()));
        return output.toString();
    }

    public Scanner getScanner() {
        return scanner;
    }
}
