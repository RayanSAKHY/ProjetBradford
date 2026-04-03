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
import java.util.function.Consumer;

public class FixedSecretAuthentificator extends AuthentificatorApp implements IMFAStrategy{
    private final User user;
    private final UserInput userInput;

    public FixedSecretAuthentificator(UserInput userInput, User user, SecretGenerator secretGenerator, TimeProvider timeProvider,
                                      CodeGenerator codeGenerator, QrGenerator qrGenerator, boolean guiMode, javafx.scene.image.ImageView imageView, boolean testMode) {
        super(timeProvider, codeGenerator, qrGenerator, secretGenerator, guiMode, imageView);
        this.user = user;
        this.userInput = userInput;
        super.testMode = testMode;

        String folder = "qrCode/users";
        File dir = new File(folder);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public FixedSecretAuthentificator(UserInput userInput, User user, SecretGenerator secretGenerator, TimeProvider timeProvider,
                                      CodeGenerator codeGenerator, QrGenerator qrGenerator, boolean testMode) {
        this(userInput, user, secretGenerator, timeProvider, codeGenerator, qrGenerator, false, null, testMode);
    }

    public FixedSecretAuthentificator(UserInput userInput, User user, boolean guiMode, javafx.scene.image.ImageView imageView, boolean testMode) {
        this(userInput, user, new DefaultSecretGenerator(), new SystemTimeProvider(), new DefaultCodeGenerator(), new ZxingPngQrGenerator(), guiMode, imageView, testMode);
    }

    public FixedSecretAuthentificator(UserInput userInput,User user,boolean testMode) {
        this(userInput,user,new DefaultSecretGenerator(),new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),false,null,testMode);
    }

    @Override
    public boolean TwoStepVerif() {

        if (user == null) {
            userInput.showMessage("User is null");
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

    @Override
    public void TwoStepVerifAsync(Consumer<String> queue) {

        if (user == null) {
            userInput.showMessage("User is null");
            queue.accept(null);
            return;
        }

        if (!user.getSecret().isEmpty()) {
            userInput.askInputAsync("Do you want to use the saved secret (Y or N) ?", input -> {
                switch (input) {
                    case "Y":
                        askForCodeAsync(queue);
                        break;
                    case "N":
                        try {
                            assignSecret(user,"qrCode/users/QrCode"+user.getUsername()+".png","QrCode" + user.getUsername());
                            askForCodeAsync(queue);
                        }
                        catch (QrGenerationException | IOException ex) {
                            ex.printStackTrace();
                            queue.accept(null);
                        }
                        break;
                    default:
                        askForCodeAsync(queue);
                        break;
                }
            });
        }
        else {
            try {
                assignSecret(user,"qrCode/users/QrCode"+user.getUsername()+".png","QrCode" + user.getUsername());
                askForCodeAsync(queue);
            }
            catch (QrGenerationException | IOException ex) {
                ex.printStackTrace();
                queue.accept(null);
            }
        }
    }

    private void askForCodeAsync(Consumer<String> queue) {
        userInput.askInputAsync("Please enter the secret code on your authentication app", code -> {
            if (verifyCode(user.getSecret(), code)) {
                userInput.showMessage("Verification succesful");
                closeQrCode();
                queue.accept("success");
            }
            else {
                userInput.showMessage("Verification failed");
                closeQrCode();
                queue.accept("failed");
            }
        });
    }

    private void closeQrCode() {
        this.end();
    }
    private void assignSecret(User user,String path,String label) throws IOException,QrGenerationException {
        String secret = secretGenerator.generate();

        generateQrCode(secret,label,path);
        user.setSecret(secret);
    }
}
