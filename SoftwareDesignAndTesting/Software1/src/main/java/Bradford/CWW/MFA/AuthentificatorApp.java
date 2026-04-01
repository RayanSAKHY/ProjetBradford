package Bradford.CWW.MFA;


//Source : https://github.com/samdjstevens/java-totp
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import javafx.application.Application;
import javafx.application.Platform;

import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;

public class AuthentificatorApp{
    protected TimeProvider timeProvider;
    protected CodeGenerator codeGenerator;
    protected QrGenerator qrGenerator;
    protected SecretGenerator secretGenerator;
    private GUIQrCode guiQrCode;
    private String Path;
    protected boolean testMode;
    private boolean guiMode;

    private javafx.scene.image.ImageView guiImageView;

    public AuthentificatorApp(boolean guiMode,javafx.scene.image.ImageView guiImageView) {
        this(new SystemTimeProvider(),new DefaultCodeGenerator(),new ZxingPngQrGenerator(),
                new DefaultSecretGenerator(),guiMode,guiImageView);
    }

    public AuthentificatorApp() {
        this(new SystemTimeProvider(), new DefaultCodeGenerator(),new ZxingPngQrGenerator(),new DefaultSecretGenerator(),false,null);
    }

    public void setTestMode(boolean value) {
        this.testMode = value;
    }

    public AuthentificatorApp(TimeProvider timeProvider,CodeGenerator codeGenerator, QrGenerator qrGenerator,
                              SecretGenerator secretGenerator, boolean guiMode, javafx.scene.image.ImageView guiImageView) {
        this.timeProvider = timeProvider;
        this.codeGenerator = codeGenerator;
        this.qrGenerator = qrGenerator;
        this.secretGenerator = secretGenerator;

        this.guiMode = guiMode;
        this.guiImageView = guiImageView;
    }

    public AuthentificatorApp(TimeProvider timeProvider,CodeGenerator codeGenerator) {
        this(timeProvider,codeGenerator,new ZxingPngQrGenerator(),new DefaultSecretGenerator(),false,null);
    }

    public void generateQrCode(String secret,String label,String path) throws QrGenerationException,IOException,IllegalArgumentException {
        Path = path;

        // secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"
        byte [] imageData= generateQRCodeData(secret,label);

        generateImage(imageData,path);


        if (guiMode && guiImageView != null) {
            Platform.runLater(() -> {
                if (guiQrCode == null) {
                    guiQrCode = new GUIQrCode(path, guiImageView);
                } else {
                    guiQrCode.updateQrCode(path);
                }
            });
        } else {
            System.out.println("QrCode generated at: "+path);
        }

    }

    private byte[] generateQRCodeData(String secret,String label) throws QrGenerationException {
        QrData data = new QrData.Builder()
                .label(label)
                .secret(secret)
                .issuer("AppName")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(60)
                .build();
        return qrGenerator.generate(data);

    }

    private void generateImage(byte[] imageData,String path) throws IOException,IllegalArgumentException {
        // Source - https://stackoverflow.com/q/15477152
        // Posted by Arvind Sridharan, modified by community. See post 'Timeline' for change history
        // Retrieved 2026-03-06, License - CC BY-SA 3.0
        if (imageData == null) {
            throw new IOException("Image data is null");
        }
        if (imageData.length == 0) {
            throw new IOException("Image data is empty");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        fileOutputStream.write(imageData);
        fileOutputStream.close();
        System.out.println("QR Code generated ");
    }

    public boolean verifyCode(String secret, String code) {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        end();

        verifier.setTimePeriod(60);
        verifier.setAllowedTimePeriodDiscrepancy(1);

        // secret = the shared secret for the user
        // code = the code submitted by the user
        code = code.trim(); //to remove the space
        return verifier.isValidCode(secret, code);
    }

    public void end() {
        if (guiQrCode != null && !testMode){
            Platform.runLater(() -> {
                this.guiQrCode.end();
            });
        }
    }
}

