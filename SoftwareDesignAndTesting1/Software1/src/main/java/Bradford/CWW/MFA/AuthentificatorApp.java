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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class AuthentificatorApp implements IMFAStrategy {
    private final TimeProvider timeProvider;
    private final CodeGenerator codeGenerator;
    private final Scanner scanner;
    private final QrGenerator qrGenerator;
    private final SecretGenerator secretGenerator;

    public AuthentificatorApp() {
        this(new SystemTimeProvider(), new DefaultCodeGenerator(),new Scanner(System.in),new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    public AuthentificatorApp(TimeProvider timeProvider,CodeGenerator codeGenerator,Scanner scanner, QrGenerator qrGenerator,SecretGenerator secretGenerator) {
        this.timeProvider = timeProvider;
        this.codeGenerator = codeGenerator;
        this.qrGenerator = qrGenerator;
        this.secretGenerator = secretGenerator;
        this.scanner = scanner;
    }

    public AuthentificatorApp(TimeProvider timeProvider,CodeGenerator codeGenerator,Scanner scanner) {
        this(timeProvider,codeGenerator,scanner,new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    public AuthentificatorApp(Scanner scanner) {
        this(new SystemTimeProvider(),new DefaultCodeGenerator(),scanner);
    }
    public AuthentificatorApp(TimeProvider timeProvider, CodeGenerator codeGenerator) {
        this(timeProvider,codeGenerator,new Scanner(System.in),new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    @Override
    public boolean TwoStepVerif() {
        String secret = secretGenerator.generate();
        // secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"
        try {
            byte[] imageData = generateQRCodeData(secret);
            generateImage(imageData);
            SwingUtilities.invokeLater(() -> {
                        GUIQrCode guiQrCode= new GUIQrCode("src/main/java/Bradford/CWW/MFA/QRCode.png");
                        guiQrCode.printQrCode();

                    }
            );
        }
        catch (QrGenerationException | IOException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
            return false;
        }

        System.out.println("Use the QRCode in your authentificator app and type the code you obtain");
        String code = scanner.nextLine();
        return verifyCode(secret, code);
    }

    private byte[] generateQRCodeData(String secret) throws dev.samstevens.totp.exceptions.QrGenerationException {
        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret(secret)
                .issuer("AppName")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(30)
                .build();
        return qrGenerator.generate(data);

    }

    private void generateImage(byte[] imageData) throws IOException {
        // Source - https://stackoverflow.com/q/15477152
        // Posted by Arvind Sridharan, modified by community. See post 'Timeline' for change history
        // Retrieved 2026-03-06, License - CC BY-SA 3.0
            if (imageData == null) {
                throw new IOException("Image data is null");
            }
            if (imageData.length == 0) {
                throw new IOException("Image data is empty");
            }
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/Bradford/CWW/MFA/QRCode.png");
            fileOutputStream.write(imageData);
            fileOutputStream.close();
            System.out.println("QR Code generated: QRCode.png");
    }

    public boolean verifyCode(String secret, String code) {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        verifier.setAllowedTimePeriodDiscrepancy(1);

        // secret = the shared secret for the user
        // code = the code submitted by the user
        code = code.trim(); //to remove the space
        return verifier.isValidCode(secret, code);
    }
}

