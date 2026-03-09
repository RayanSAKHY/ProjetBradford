package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Scanner;

public class FixedSecretAuthentificator extends AuthentificatorApp implements IMFAStrategy{
    private User user;
    private Scanner scanner;
    private final QrGenerator qrGenerator;
    private final SecretGenerator secretGenerator;
    private final TimeProvider timeProvider;
    private final CodeGenerator codeGenerator;

    public FixedSecretAuthentificator(User user) {
        this(new Scanner(System.in),user,new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }
    public FixedSecretAuthentificator(Scanner scanner, User user, TimeProvider timeProvider,CodeGenerator codeGenerator,QrGenerator qrGenerator,SecretGenerator secretGenerator) {
        this.user = user;
        this.scanner = scanner;
        this.qrGenerator = qrGenerator;
        this.secretGenerator = secretGenerator;
        this.timeProvider = timeProvider;
        this.codeGenerator = codeGenerator;
    }

    public FixedSecretAuthentificator(Scanner scanner, User user) {
        this(scanner,user,new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    @Override
    public boolean TwoStepVerif() {

        if (user.getSecret() != null && !user.getSecret().isEmpty()) {
            System.out.println("Do you want to use the saved secret (Y or N) ?");
            String input = scanner.nextLine();

            switch (input) {
                case "Y":
                    break;
                case "N":
                    SecretGenerator secretGenerator = new DefaultSecretGenerator();
                    String secret = secretGenerator.generate();

                    try {
                        generateQrCode(secret);
                    } catch (QrGenerationException | IOException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                    user.setSecret(secret);
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } else {
            SecretGenerator secretGenerator = new DefaultSecretGenerator();
            String secret = secretGenerator.generate();

            try {
                generateQrCode(secret, "QrCode" + user.getUsername());
            } catch (QrGenerationException | IOException ex) {
                ex.printStackTrace();
                return false;
            }

            user.setSecret(secret);
        }

        String finalSecret = user.getSecret();

        System.out.println("Please enter the secret code on your authentification app ");
        String code = scanner.nextLine();

        return verifyCode(finalSecret, code);
    }
}
