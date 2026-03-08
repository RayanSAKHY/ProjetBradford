package Bradford.CWW.MFA;

import java.util.Scanner;

public class PhoneCall implements IMFAStrategy {

    private Scanner scanner;

    public PhoneCall(Scanner scanner) {
        this.scanner = scanner;
    }

    public PhoneCall()
    {
        this(new Scanner(System.in));
    }

    @Override
    public boolean TwoStepVerif() {
        System.out.println("Please enter your phone number to receive a call: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("call to "+phoneNumber);
        return true;
    }
}
