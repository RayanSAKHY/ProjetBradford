package Bradford.CWW;

import Bradford.CWW.Input.ConsoleInput;
import Bradford.CWW.Input.UserInput;
import Bradford.CWW.MFA.*;
import Bradford.CWW.assets.*;
import Bradford.CWW.assets.UserDataSingleton;

import java.lang.StringBuilder;
import java.util.Scanner;
import java.util.function.Consumer;

public class Login {
    private final UserData users;
    private final UserInput userInput;
    private final boolean testMode;


    public Login(UserInput userInput,boolean testMode) {
        this.userInput = userInput;
        this.testMode = testMode;
        this.users = UserDataSingleton.getInstance();
    }

    public Login(boolean testMode) {
        this(new ConsoleInput(new Scanner(System.in)),testMode);
    }


    public boolean loginJavaFX(String username, String password) {
        if (users.usernameExists(username)) {
            User user = users.getUser(username);
            return user.getPassword().equals(password);
        }
        else {
            return false;
        }
    }

    public boolean loginMFAConsole(int choice, User user) {
        IMFAStrategy strategy = null;
        switch (choice) {
            case 1:
                strategy = new PhoneCall(userInput);
                break;
            case 2:
                strategy = new CodeSentByEmail(userInput);
                break;
            case 3:
                strategy = new CodeSentBySMS(userInput);
                break;
            case 4:
                strategy = new RandomSecretAuthentificatorApp(userInput,testMode);
                break;
            case 5:
                strategy = new FixedSecretAuthentificator(userInput,user,testMode);
                break;
            default:
                break;
        }

        if (strategy != null) {
            return strategy.TwoStepVerif();
        }
        return false;
    }

    public void loginMFAJavaFX(int choice, User user, Consumer<Boolean> queue, javafx.scene.image.ImageView imageView) {
        IMFAStrategy strategy = null;
        switch (choice) {
            case 1:
                strategy = new PhoneCall(userInput);
                break;
            case 2:
                strategy = new CodeSentByEmail(userInput);
                break;
            case 3:
                strategy = new CodeSentBySMS(userInput);
                break;
            case 4:
                strategy = new RandomSecretAuthentificatorApp(userInput,true,imageView,testMode);
                break;
            case 5:
                strategy = new FixedSecretAuthentificator(userInput,user,true,imageView,testMode);
                break;
            default:
                break;
        }

        MFALogin mfalogin = new MFALogin(strategy);
        IMFAStrategy finalStrategy = strategy;
        mfalogin.twoStepVerifAsync(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean success) {
                if (success) {
                    queue.accept(true);
                }
                else {
                    if (finalStrategy != null) {
                        userInput.showMessage("Two Step Verification failed ") ;
                        queue.accept(false);
                    }
                }
            }
        });

    }

    public boolean loginConsole(String username, String password) {
        boolean connected = false;
        int nbEssai = 0;
        while (nbEssai < 3 && !connected) {
            userInput.showMessage(credentialsPrint(username, password)) ;
            if (users.usernameExists(username)){
                User user = users.getUser(username);
                if (user.getPassword().equals(password)) {
                    String input = MFAChoice();
                    try {
                        int choice = Integer.parseInt(input);
                        connected = loginMFAConsole(choice, user);

                        if (!connected) {
                            nbEssai++;
                        }
                    }
                    catch (NumberFormatException ex) {
                        nbEssai++;
                        if (nbEssai < 3) {
                            userInput.showMessage("Please enter a number between 1 and 5");
                        }
                    }
                }
                else {
                    userInput.showMessage("Wrong Credentials. Retry in a short time");
                    break;
                }
            }
            else {
                userInput.showMessage("Wrong Credentials. Retry in a short time");
                break;
            }
            if (!connected && nbEssai <3) {
                userInput.showMessage("There are "+(3-nbEssai)+" attempts left\n");
            }
        }

        if (connected) {
            userInput.showMessage("Login successful");
        }
        else {
            userInput.showMessage("Login failed");
        }
        return connected;
    }

    private String MFAChoice() {
        StringBuilder mfa = new StringBuilder();
        mfa.append("Which method do you prefer to verify that it's you : ").append("\n");
        mfa.append("- Receiving a phone call (type 1)").append("\n");
        mfa.append("- Send a code by email (type 2)").append("\n");
        mfa.append("- Send a code by text message (type 3)").append("\n");
        mfa.append("- Scan a QRCode in an Authenticator app (type 4)").append("\n");
        mfa.append("- Check the code in your Authenticator app (type 5)").append("\n");

        return userInput.askInput(mfa.toString());
    }

    public String credentialsPrint(String username, String password) {
        StringBuilder output = new StringBuilder();
        output.append("Username: ").append(username);
        output.append("\n");
        output.append("Password: ").append("*".repeat(password.length()));
        return output.toString();
    }

    public UserInput getUserInput() {
        return userInput;
    }
}
