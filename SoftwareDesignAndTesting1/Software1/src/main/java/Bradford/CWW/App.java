package Bradford.CWW;

import Bradford.CWW.assets.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean run = true;

        Map<String, User> users = new HashMap<>();
        System.out.print("Do you wish to create some user (Y or N) ? ");
        String input = scanner.nextLine();
        switch(input) {
            case "Y":
                while (run) {
                    System.out.print("Enter user name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter user password: ");
                    String password = scanner.nextLine();
                    users.put(name,new User(name,password));
                    System.out.print("Do you still want to create more users (Y or N) ? ");
                    input = scanner.nextLine();
                    if (input.equals("N")) {
                        run = false;
                    }
                    else if (!input.equals("Y")) {
                        System.out.println("Invalid input you cannot create more users");
                        run = false;
                    }
                }
                break;
            case "N":
                break;
            default:
                System.out.println("Next time type something valid");
                break;
        }
        run = true;
        Login login = new Login(users);
        while(run){
            System.out.print("Do you want to try connecting with your own users (type 1) or using the demonstration users (type 2) ? ");
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Enter the username :  ");
                    String name = scanner.nextLine();
                    System.out.println("Enter the password :  ");
                    String password = scanner.nextLine();
                    login.login(name,password);
                    break;
                case "2":
                    run = false;
                    System.out.println("Try typing 5");
                    login.login("test","azerty");
                    System.out.println("Try typing 5 again");
                    login.login("test","azerty");
                    System.out.println("Try anything");
                    login.login("test","Azerty");
                    System.out.println("Try typing '");
                    login.login("I lost","the game");
                    break;
                default:
                    run = false;
                    System.out.println("Invalid input the application will end");
            }


        }
        login.close();


    }
}
