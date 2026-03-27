package Bradford.CWW.MFA;

import java.util.Scanner;

public class PhoneCall implements IMFAStrategy {

    private final Scanner scanner;

    public PhoneCall(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public boolean TwoStepVerif() {
        System.out.println("Please enter your phone number to receive a call: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("call to "+phoneNumber);
        return true;
    }
}
