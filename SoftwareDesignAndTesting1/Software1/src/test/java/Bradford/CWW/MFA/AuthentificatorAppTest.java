package Bradford.CWW.MFA;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AuthentificatorAppTest {

    @Test
    public void testVerifyCodeShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 30; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code+"\n").getBytes());


            AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator,new Scanner(in));

            boolean result = app.verifyCode(secret, code);

            assertTrue(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testVerifyCodeShouldReturnFalse() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";
        String code = "111111";
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator);

        boolean result = app.verifyCode(secret, code);

        assertFalse(result);
    }

    @Test
    public void testImageDataNullShouldReturnFalse() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();

        QrGenerator qrGenerator = new ZxingPngQrGenerator() {

            @Override
            public byte[] generate(QrData data) {
                return null;
            }
        };

        AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator, new Scanner(System.in), qrGenerator, new DefaultSecretGenerator());

        assertFalse(app.TwoStepVerif());

    }

    @Test
    public void testImageDataEmptyShouldReturnFalse() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();

        QrGenerator qrGenerator = new ZxingPngQrGenerator() {

            @Override
            public byte[] generate(QrData data) {
                return new byte[0];
            }
        };

        AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator, new Scanner(System.in), qrGenerator, new DefaultSecretGenerator());

        assertFalse(app.TwoStepVerif());

    }

    @Test
    public void testExceptionGeneratingQRCodeShouldReturnFalse() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();

        QrGenerator qrGenerator = new ZxingPngQrGenerator() {

            @Override
            public byte[] generate(QrData data) throws QrGenerationException {
                throw new QrGenerationException("QRCode cannot be generated",new Exception());
            }
        };

        AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator, new Scanner(System.in), qrGenerator, new DefaultSecretGenerator());

        assertFalse(app.TwoStepVerif());

    }
}
