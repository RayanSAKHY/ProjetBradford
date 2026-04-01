package Bradford.CWW.MFA;

import Bradford.CWW.Input.UserInput;
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

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FixedSecretAuthentificator extends AuthentificatorApp implements IMFAStrategy{
    private final User user;
    private final UserInput userInput;

    public FixedSecretAuthentificator(UserInput userInput, User user, SecretGenerator secretGenerator, TimeProvider timeProvider,
                                      CodeGenerator codeGenerator, QrGenerator qrGenerator, boolean testMode) {
        this.user = user;
        this.userInput = userInput;
        super.secretGenerator = secretGenerator;
        super.timeProvider = timeProvider;
        super.codeGenerator = codeGenerator;
        super.qrGenerator = qrGenerator;
        super.testMode = testMode;

        String folder = "qrCode/users";
        File dir = new File(folder);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public FixedSecretAuthentificator(UserInput userInput,User user,boolean testMode) {
        this(userInput,user,new DefaultSecretGenerator(),new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),testMode);
    }

    @Override
    public boolean TwoStepVerif() {

        if (user == null) {
            System.out.println("User is null");
            return false;
        }

        if (!user.getSecret().isEmpty()) {
            String input = userInput.askInput("Do you want to use the saved secret (Y or N) ?");

            switch (input) {
                case "Y":
                    break;
                case "N":
                    try {
                        assignSecret(user,"qrCode/users/QrCode"+user.getUsername()+".png","QrCode" + user.getUsername());
                    }
                    catch (QrGenerationException | IOException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                    break;
                default:
                    userInput.showMessage("Invalid input");
                    break;
            }
        } else {
            try {
                assignSecret(user,"qrCode/users/QrCode"+user.getUsername()+".png","QrCode" + user.getUsername());
            }
            catch (QrGenerationException | IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        String finalSecret = user.getSecret();

        String code = userInput.askInput("Please enter the secret code on your authentification app ");

        return verifyCode(finalSecret, code);
    }

    private void assignSecret(User user,String path,String label) throws IOException,QrGenerationException {
        String secret = secretGenerator.generate();

        generateQrCode(secret,label,path);
        user.setSecret(secret);
    }
}
