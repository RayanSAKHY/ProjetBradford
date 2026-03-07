package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;
//Source : https://github.com/samdjstevens/java-totp
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class AuthentificatorApp implements IMFAStrategy {
    private TimeProvider timeProvider;
    private CodeGenerator codeGenerator;

    public AuthentificatorApp() {
        this(new SystemTimeProvider(), new DefaultCodeGenerator());
    }
    public AuthentificatorApp(TimeProvider timeProvider, CodeGenerator codeGenerator) {
        this.timeProvider = timeProvider;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public boolean TwoStepVerif(User user) {
        Scanner scanner = new Scanner(System.in);
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();
        // secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"
        byte[] imageData = generateQRCodeData(secret);
        generateImage(imageData);
        System.out.println("Use the QRCode in your authentificator app and type the code you obtain");
        String code = scanner.nextLine();

        return verifyCode(secret, code);
    }

    private byte[] generateQRCodeData(String secret) {
        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret(secret)
                .issuer("AppName")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(30)
                .build();
        try {
            QrGenerator generator = new ZxingPngQrGenerator();
            return generator.generate(data);
        }
        catch (dev.samstevens.totp.exceptions.QrGenerationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void generateImage(byte[] imageData) {
        // Source - https://stackoverflow.com/q/15477152
        // Posted by Arvind Sridharan, modified by community. See post 'Timeline' for change history
        // Retrieved 2026-03-06, License - CC BY-SA 3.0

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/Bradford/CWW/QRCode.png");
            fileOutputStream.write(imageData);
            fileOutputStream.close();
            System.out.println("QR Code generated: QRCode.png");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean verifyCode(String secret, String code) {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        verifier.setAllowedTimePeriodDiscrepancy(1);

        // secret = the shared secret for the user
        // code = the code submitted by the user
        code = code.trim(); //pour retirer les espaces
        return verifier.isValidCode(secret, code);
    }
}

