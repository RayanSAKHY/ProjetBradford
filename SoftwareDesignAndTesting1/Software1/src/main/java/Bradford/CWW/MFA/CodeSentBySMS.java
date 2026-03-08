package Bradford.CWW.MFA;

import java.util.Scanner;

public class CodeSentBySMS implements IMFAStrategy {
    private Scanner scanner;

    public CodeSentBySMS(Scanner scanner) {
        this.scanner = scanner;
    }

    public CodeSentBySMS() {
        this(new Scanner(System.in));
    }

    @Override
    public boolean TwoStepVerif() {
        System.out.println("Please enter your phone number to receive a code by SMS: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("send a code to "+phoneNumber);
        return true;
    }
}
