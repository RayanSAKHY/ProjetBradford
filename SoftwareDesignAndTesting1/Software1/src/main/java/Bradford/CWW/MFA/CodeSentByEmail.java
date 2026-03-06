package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;

import java.util.Scanner;
//Source : https://stackoverflow.com/questions/884943/how-do-i-send-an-e-mail-in-java

public class CodeSentByEmail implements IMFAStrategy {
    @Override
    public boolean TwoStepVerif(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your email to receive a code by email: ");
        String email = sc.nextLine();
        System.out.println("send a code to "+email);
        return true;
    }
}
