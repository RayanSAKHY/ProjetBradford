package Bradford.CWW.MFA;

import Bradford.CWW.Input.ConsoleInput;
import Bradford.CWW.Input.UserInput;
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
import java.util.Scanner;

public class RandomSecretAuthentificatorApp extends AuthentificatorApp implements IMFAStrategy {
    private final UserInput userInput;

    public RandomSecretAuthentificatorApp() {
        this(new ConsoleInput(new Scanner(System.in)), new DefaultSecretGenerator(),new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),false);
    }

    public RandomSecretAuthentificatorApp(UserInput userInput,SecretGenerator secretGenerator,TimeProvider timeProvider,CodeGenerator codeGenerator,QrGenerator qrGenerator,boolean testMode) {
        this.userInput = userInput;
        super.secretGenerator = secretGenerator;
        super.timeProvider = timeProvider;
        super.codeGenerator = codeGenerator;
        super.qrGenerator = qrGenerator;
        super.testMode = testMode;
    }

    public RandomSecretAuthentificatorApp(UserInput userInput,boolean testMode) {
        this(userInput, new DefaultSecretGenerator(),new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),testMode);
    }


    @Override
    public boolean TwoStepVerif() {

        String secret = secretGenerator.generate();

        try {
            generateQrCode(secret,"QrCode","src/main/java/Bradford/CWW/MFA/QrCode.png");
        }
        catch (QrGenerationException | IOException ex) {
            ex.printStackTrace();
            return false;
        }


        String code = userInput.askInput("Use the QRCode in your authentificator app and type the code you obtain");

        return verifyCode(secret,code);
    }
}
