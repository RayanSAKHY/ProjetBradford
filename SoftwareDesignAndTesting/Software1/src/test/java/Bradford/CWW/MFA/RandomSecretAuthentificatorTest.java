package Bradford.CWW.MFA;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomSecretAuthentificatorTest {

    @Test
    public void testTwoStepVerifShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider = () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code + "\n").getBytes());


            RandomSecretAuthentificatorApp app = new RandomSecretAuthentificatorApp(new Scanner(in),new DefaultSecretGenerator(),timeProvider,codeGenerator,new ZxingPngQrGenerator(),true);

            boolean result = app.verifyCode(secret, code);

            assertTrue(result);
        } catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifWithQrGenerationExceptionShouldReturnFalse() {
        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider = () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code + "\n").getBytes());


            RandomSecretAuthentificatorApp app = new RandomSecretAuthentificatorApp(new Scanner(in),new DefaultSecretGenerator(),timeProvider,codeGenerator,new ZxingPngQrGenerator(),true) {
                @Override
                public void generateQrCode(String secret,String label,String path) throws QrGenerationException {
                    throw new QrGenerationException("", new Exception());
                }
            };

            boolean result = app.TwoStepVerif();

            assertFalse(result);
        } catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void testTwoStepVerifWithIOExceptionShouldReturnFalse() {
        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider = () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code + "\n").getBytes());


            RandomSecretAuthentificatorApp app = new RandomSecretAuthentificatorApp(new Scanner(in),new DefaultSecretGenerator(),timeProvider,codeGenerator,new ZxingPngQrGenerator(),true) {
                @Override
                public void generateQrCode(String secret,String label,String path) throws IOException {
                    throw new IOException();
                }
            };

            boolean result = app.TwoStepVerif();

            assertFalse(result);
        } catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }

    }
}
