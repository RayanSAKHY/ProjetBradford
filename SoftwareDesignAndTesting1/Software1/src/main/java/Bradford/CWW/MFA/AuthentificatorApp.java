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
import java.io.InputStream;
import java.util.Scanner;

public class AuthentificatorApp implements IMFAStrategy {
    private TimeProvider timeProvider;
    private CodeGenerator codeGenerator;
    private InputStream in;
    private Scanner scanner;
    private QrGenerator qrGenerator;
    private SecretGenerator secretGenerator;

    public AuthentificatorApp() {
        this(new SystemTimeProvider(), new DefaultCodeGenerator(),System.in,new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    public AuthentificatorApp(TimeProvider timeProvider,CodeGenerator codeGenerator,InputStream in, QrGenerator qrGenerator,SecretGenerator secretGenerator) {
        this.timeProvider = timeProvider;
        this.codeGenerator = codeGenerator;
        this.in = in;
        this.qrGenerator = qrGenerator;
        this.secretGenerator = secretGenerator;
        scanner = new Scanner(in);
    }

    public AuthentificatorApp(TimeProvider timeProvider,CodeGenerator codeGenerator,InputStream in) {
        this(timeProvider,codeGenerator,in,new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    public AuthentificatorApp(TimeProvider timeProvider, CodeGenerator codeGenerator) {
        this(timeProvider,codeGenerator,System.in,new ZxingPngQrGenerator(),new DefaultSecretGenerator());
    }

    @Override
    public boolean TwoStepVerif(User user) {
        String secret = secretGenerator.generate();
        // secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"
        try {
            byte[] imageData = generateQRCodeData(secret);
            generateImage(imageData);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        System.out.println("Use the QRCode in your authentificator app and type the code you obtain");
        String code = scanner.nextLine();
        System.out.println(code);

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
            return qrGenerator.generate(data);
        }
        catch (dev.samstevens.totp.exceptions.QrGenerationException ex) {
            ex.printStackTrace();
            return new byte[0];
        }
    }

    private void generateImage(byte[] imageData) throws IOException {
        // Source - https://stackoverflow.com/q/15477152
        // Posted by Arvind Sridharan, modified by community. See post 'Timeline' for change history
        // Retrieved 2026-03-06, License - CC BY-SA 3.0
            if (imageData == null) {
                throw new IOException("Image data is null");
            }
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/Bradford/CWW/QRCode.png");
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

