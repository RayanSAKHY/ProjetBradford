package Bradford.CWW.MFA;


import java.util.Date;
import java.util.Scanner;
//Source : https://stackoverflow.com/questions/884943/how-do-i-send-an-e-mail-in-java



public class CodeSentByEmail implements IMFAStrategy {
    private Scanner scanner;

    public CodeSentByEmail(Scanner scanner) {
        this.scanner = scanner;
    }

    public CodeSentByEmail() {
        this(new Scanner(System.in));
    }

    @Override
    public boolean TwoStepVerif() {
        System.out.println("Please enter your email to receive a code by email: ");
        String email = scanner.nextLine();
        System.out.println("sent a code to "+email);
        return true;
    }
}
