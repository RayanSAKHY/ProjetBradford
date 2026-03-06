package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;
import java.util.Scanner;

public class CodeSentBySMS implements IMFAStrategy {
    @Override
    public boolean TwoStepVerif(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your phone number to receive a code by SMS: ");
        String phoneNumber = sc.nextLine();
        System.out.println("send a code to "+phoneNumber);
        return true;
    }
}
