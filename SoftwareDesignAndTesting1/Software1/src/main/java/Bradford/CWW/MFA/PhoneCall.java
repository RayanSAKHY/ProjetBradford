package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;

import java.util.Scanner;

public class PhoneCall implements IMFAStrategy {

    @Override
    public boolean TwoStepVerif(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your phone number to receive a call: ");
        String phoneNumber = sc.nextLine();
        System.out.println("call to "+phoneNumber);
        return true;
    }
}
