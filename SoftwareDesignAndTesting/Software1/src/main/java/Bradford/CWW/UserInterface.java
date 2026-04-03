package Bradford.CWW;

import Bradford.CWW.Input.ConsoleInput;
import Bradford.CWW.Input.UserInput;
import Bradford.CWW.assets.UserData;
import Bradford.CWW.assets.UserDataSingleton;

import java.io.InputStream;
import java.util.Scanner;

public class UserInterface {
    public UserInput userInput;
    public boolean testMode;
    public InputStream input;
    private final UserData users = UserDataSingleton.getInstance();

    public UserInterface(InputStream input,boolean testMode) {
        this.userInput = new ConsoleInput(new Scanner(input));
        this.testMode = testMode;
    }

    public UserInterface() {
        this(System.in,false);
    }

    public void UseApp(){
        boolean run = true;

        String input = userInput.askInput("Do you wish to create a user (Y or N) ? ");
        switch(input) {
            case "Y":
                while (run) {
                    userInput.showMessage("Each username must be unique or the last user with identical username will be deleted");
                    String name = userInput.askInput("Enter user's name: ");
                    String password = userInput.askInput("Enter user's password: ");
                    users.addUser(name,password);
                    input = userInput.askInput("Do you still want to create more users (Y or N) ? ");
                    if (input.equals("N")) {
                        run = false;
                    }
                    else if (!input.equals("Y")) {
                        userInput.showMessage("Invalid input you cannot create more users");
                        run = false;
                    }
                }
                break;
            case "N":
                break;
            default:
                userInput.showMessage("Next time type something valid");
                break;
        }
        run = true;
        Login login = new Login(userInput,testMode);
        while(run){
            input = userInput.askInput("Do you want to try connecting with your own users (type 1), using the demonstration users (type 2) or qui the application (type 3) ? ");
            switch (input) {
                case "1":
                    String name = userInput.askInput("Enter the username :  ");
                    String password = userInput.askInput("Enter the password :  ");
                    login.loginConsole(name,password);
                    break;
                case "2":
                    run = false;
                    userInput.showMessage("Try typing 5");
                    login.loginConsole("test","azerty");
                    userInput.showMessage("Try typing 5 again");
                    login.loginConsole("test","azerty");
                    userInput.showMessage("It will not work");
                    login.loginConsole("test","Azerty");
                    userInput.showMessage("Try typing 4");
                    login.loginConsole("I lost","the game");
                    break;
                case "3":
                    run = false;
                    break;
                default:
                    run = false;
                    userInput.showMessage("Invalid input the application will end");
            }
        }
        close();
    }

    public void close() {
        if (input != System.in) {
            userInput.close();
        }
    }
}
