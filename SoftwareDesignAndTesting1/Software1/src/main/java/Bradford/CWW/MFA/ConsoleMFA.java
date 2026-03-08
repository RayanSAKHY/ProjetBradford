package Bradford.CWW.MFA;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ConsoleMFA implements IMFAStrategy {

    private Random random;
    private int nbChar;
    private Scanner scanner;

    public ConsoleMFA(Scanner scanner) {
        this.scanner = scanner;
    }

    public ConsoleMFA() {
        this(new Scanner(System.in));
    }
    @Override
    public boolean TwoStepVerif() {
        try {
            init();
        }
        catch (NumberFormatException e) {
            System.out.println("Wrong number format");
            return false;
        }
        String code = generateCode();
        System.out.println("Code generated : " +code);
        System.out.println("Please rewrite the code in the console to prove that it is you");
        String typedCode = scanner.nextLine();

        return verifyCode(code, typedCode);
    }

    public void init() throws NumberFormatException {
        System.out.println("How many character do you want the code to be (minimum 5) ?");
        nbChar = Integer.parseInt(scanner.nextLine());
        if (nbChar < 1) {
            nbChar = 5;
        }
        System.out.println("Which seed do you want to use for the number generator?");
        long seed = Long.parseLong(scanner.nextLine());
        random = new Random(seed);
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < nbChar; i++) {
            int value = random.nextInt(60)+65;
            code.append((char)value);
        }
        return code.toString();
    }

    public boolean verifyCode(String code,String typedCode) {
        return  code.equals(typedCode);
    }
}
