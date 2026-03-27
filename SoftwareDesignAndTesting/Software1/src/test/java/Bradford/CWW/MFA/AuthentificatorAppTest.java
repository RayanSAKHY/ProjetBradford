package Bradford.CWW.MFA;

import Bradford.CWW.assets.User;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AuthentificatorAppTest {

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
    public void testGenerateImageShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";

        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            SecretGenerator secretGenerator = new SecretGenerator() {
                @Override
                public String generate() {
                    return "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";
                }
            };

            AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator,new ZxingPngQrGenerator(),secretGenerator);
            app.setTestMode(true);

            app.generateQrCode(secret,"test","/");
            assertTrue(app.verifyCode(secret,code));
        }
        catch (CodeGenerationException |IOException | QrGenerationException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testImageDataNullShouldThrowsException() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();

        QrGenerator qrGenerator = new ZxingPngQrGenerator() {

            @Override
            public byte[] generate(QrData data) {
                return null;
            }
        };

        AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator, qrGenerator, new DefaultSecretGenerator());

        assertThrows(IOException.class, () -> {
            app.generateQrCode("BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV","QrCode","");
        });

    }

    @Test
    public void testMinimalImageDataShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            QrGenerator qrGenerator = new ZxingPngQrGenerator() {
                @Override
                public byte[] generate(QrData data) {
                    return new byte[1];
                }
            };

            AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator, qrGenerator, new DefaultSecretGenerator());
            app.setTestMode(true);
            app.generateQrCode(secret,"test","/");
            app.verifyCode(secret,code);
        }
        catch (CodeGenerationException | QrGenerationException | IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testImageDataEmptyShouldThrowsException() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();

        QrGenerator qrGenerator = new ZxingPngQrGenerator() {

            @Override
            public byte[] generate(QrData data) {
                return new byte[0];
            }
        };

        AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator, qrGenerator, new DefaultSecretGenerator());

        assertThrows(IOException.class, () -> {
            app.generateQrCode("BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV","QrCode","");
        });

    }

    @Test
    public void testExceptionGeneratingQRCodeShouldThrowsException() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();

        QrGenerator qrGenerator = new ZxingPngQrGenerator() {

            @Override
            public byte[] generate(QrData data) throws QrGenerationException {
                throw new QrGenerationException("QRCode cannot be generated",new Exception());
            }
        };

        AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator, qrGenerator, new DefaultSecretGenerator());

        assertThrows(QrGenerationException.class, () -> {
            app.generateQrCode("BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV","QrCode","");
        });

    }

    @Test
    public void testNullPathShouldThrowsIllegalArgumentException() {

        AuthentificatorApp app = new AuthentificatorApp();

        assertThrows(IllegalArgumentException.class, () -> {
            app.generateQrCode("BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV","QrCode",null);
        });
    }
}
