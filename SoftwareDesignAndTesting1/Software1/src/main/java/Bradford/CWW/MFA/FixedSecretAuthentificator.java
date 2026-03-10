package Bradford.CWW.MFA;

import Bradford.CWW.assets.User;
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

public class FixedSecretAuthentificator extends AuthentificatorApp implements IMFAStrategy{
    private final User user;
    private final Scanner scanner;

    public FixedSecretAuthentificator(Scanner scanner, User user, SecretGenerator secretGenerator, TimeProvider timeProvider, CodeGenerator codeGenerator, QrGenerator qrGenerator, boolean testMode) {
        this.user = user;
        this.scanner = scanner;
        super.secretGenerator = secretGenerator;
        super.timeProvider = timeProvider;
        super.codeGenerator = codeGenerator;
        super.qrGenerator = qrGenerator;
        super.testMode = testMode;
    }

    public FixedSecretAuthentificator(Scanner scanner,User user,boolean testMode) {
        this(scanner,user,new DefaultSecretGenerator(),new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),testMode);
    }

    @Override
    public boolean TwoStepVerif() {

        if (user == null) {
            System.out.println("User is null");
            return false;
        }

        if (!user.getSecret().isEmpty()) {
            System.out.println("Do you want to use the saved secret (Y or N) ?");
            String input = scanner.nextLine();

            switch (input) {
                case "Y":
                    break;
                case "N":
                    try {
                        assignSecret(user,"src/main/java/Bradford/CWW/assets/QrCode/QrCode"+user.getUsername()+".png","QrCode" + user.getUsername());
                    }
                    catch (QrGenerationException | IOException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } else {
            try {
                assignSecret(user,"src/main/java/Bradford/CWW/assets/QrCode/QrCode"+user.getUsername()+".png","QrCode" + user.getUsername());
            }
            catch (QrGenerationException | IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        String finalSecret = user.getSecret();

        System.out.println("Please enter the secret code on your authentification app ");
        String code = scanner.nextLine();

        return verifyCode(finalSecret, code);
    }

    private void assignSecret(User user,String path,String label) throws IOException,QrGenerationException {
        String secret = secretGenerator.generate();

        generateQrCode(secret,label,path);
        user.setSecret(secret);
    }
}
