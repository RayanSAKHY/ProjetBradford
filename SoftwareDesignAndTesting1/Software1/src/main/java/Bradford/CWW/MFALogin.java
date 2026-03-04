package Bradford.CWW;

import Bradford.CWW.MFA.*;
import Bradford.CWW.asssets.User;

import java.util.Scanner;

public class MFALogin {
    private IMFAStrategy strategy;

    public boolean twoStepVerif(User user) {
        boolean result = false;
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
                    strategy = new Email();
                    break;
                case 4:
                    strategy = new TextMessage();
                    break;
                case 5:
                    strategy = new AuthentificatorApp();
                    break;
                default:
                    break;
            }

            if (strategy != null) {
                result = strategy.TwoStepVerif(user);
            }
        }
        catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result;
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
}
