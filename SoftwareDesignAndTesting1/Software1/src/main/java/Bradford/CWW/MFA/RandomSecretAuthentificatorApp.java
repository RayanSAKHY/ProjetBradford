package Bradford.CWW.MFA;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Scanner;

public class RandomSecretAuthentificatorApp extends AuthentificatorApp implements IMFAStrategy {
    private final TimeProvider timeProvider;
    private final CodeGenerator codeGenerator;
    private final Scanner scanner;
    private final QrGenerator qrGenerator;
    private final SecretGenerator secretGenerator;

    public RandomSecretAuthentificatorApp() {
        this(new Scanner(System.in),new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    public RandomSecretAuthentificatorApp(Scanner scanner,TimeProvider timeProvider,CodeGenerator codeGenerator, QrGenerator qrGenerator,SecretGenerator secretGenerator) {
        this.scanner = scanner;
        this.timeProvider = timeProvider;
        this.codeGenerator = codeGenerator;
        this.qrGenerator = qrGenerator;
        this.secretGenerator = secretGenerator;
    }

    public RandomSecretAuthentificatorApp(Scanner scanner){
        this(scanner,new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    @Override
    public boolean TwoStepVerif() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        try {
            generateQrCode(secret);
        }
        catch (QrGenerationException | IOException ex) {
            ex.printStackTrace();
            return false;
        }


        System.out.println("Use the QRCode in your authentificator app and type the code you obtain");
        String code = scanner.nextLine();

        return verifyCode(secret,code);
    }
}
