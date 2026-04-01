package Bradford.CWW.MFA;

import Bradford.CWW.Input.ConsoleInput;
import Bradford.CWW.assets.User;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FixedSecretAuthentificatorTest {

    @Test
    public void testTwoStepVerifSecretExistsShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream(("Y\n"+code+"\n").getBytes());


            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User("","","BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"),new DefaultSecretGenerator(),timeProvider,codeGenerator,new ZxingPngQrGenerator(),true);

            boolean result = app.TwoStepVerif();

            assertTrue(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifSecretToCreateShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code+"\n").getBytes());

            SecretGenerator secretGenerator = new SecretGenerator() {
                @Override
                public String generate() {
                    return "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";
                }
            };
            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User(),secretGenerator,timeProvider,codeGenerator,new ZxingPngQrGenerator(),true);

            boolean result = app.TwoStepVerif();

            assertTrue(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifSecretReCreateShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream(("N\n"+code+"\n").getBytes());

            SecretGenerator secretGenerator = new SecretGenerator() {
                @Override
                public String generate() {
                    return secret;
                }
            };
            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User("","",secret),secretGenerator,timeProvider,codeGenerator,new ZxingPngQrGenerator(),true);

            boolean result = app.TwoStepVerif();

            assertTrue(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifSecretExistInvalidShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream(("u\n"+code+"\n").getBytes());


            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User("","","BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"),new DefaultSecretGenerator(),timeProvider,codeGenerator,new ZxingPngQrGenerator(),true);

            boolean result = app.TwoStepVerif();

            assertTrue(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifUserNullShouldReturnFalse() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code+"\n").getBytes());


            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),null,new DefaultSecretGenerator(),timeProvider,codeGenerator,new ZxingPngQrGenerator(),true);

            boolean result = app.TwoStepVerif();

            assertFalse(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifQrGenerationExceptionShouldReturnFalse() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code+"\n").getBytes());
            QrGenerator qrGenerator = new ZxingPngQrGenerator() {
                @Override
                public byte[] generate(QrData data) throws QrGenerationException {
                    throw new QrGenerationException("test",new Exception());
                }
            };

            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User(),new DefaultSecretGenerator(),timeProvider,codeGenerator,qrGenerator,true);

            boolean result = app.TwoStepVerif();

            assertFalse(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifIOExceptionShouldReturnFalse() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream((code+"\n").getBytes());
            QrGenerator qrGenerator = new ZxingPngQrGenerator() {
                @Override
                public byte[] generate(QrData data) {
                    return null;
                }
            };

            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User(),new DefaultSecretGenerator(),timeProvider,codeGenerator,qrGenerator,true);

            boolean result = app.TwoStepVerif();

            assertFalse(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifSecretReCreateIOExceptionShouldReturnFalse() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream(("N\n"+code+"\n").getBytes());

            SecretGenerator secretGenerator = new SecretGenerator() {
                @Override
                public String generate() {
                    return secret;
                }
            };

            QrGenerator qrGenerator = new ZxingPngQrGenerator() {
                @Override
                public byte[] generate(QrData data){
                    return null;
                }
            };
            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User("","",secret),secretGenerator,timeProvider,codeGenerator,qrGenerator,true);

            boolean result = app.TwoStepVerif();

            assertFalse(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTwoStepVerifSecretReCreateQrGenerationExceptionShouldReturnFalse() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long counter = timeProvider.getTime() / 60; //found in this link https://github.com/samdjstevens/java-totp/blob/master/totp/src/main/java/dev/samstevens/totp/code/CodeGenerator.java
            String code = codeGenerator.generate(secret, counter);
            InputStream in = new ByteArrayInputStream(("N\n"+code+"\n").getBytes());

            SecretGenerator secretGenerator = new SecretGenerator() {
                @Override
                public String generate() {
                    return secret;
                }
            };

            QrGenerator qrGenerator = new ZxingPngQrGenerator() {
                @Override
                public byte[] generate(QrData data) throws QrGenerationException {
                    throw new QrGenerationException("test",new Exception());
                }
            };
            FixedSecretAuthentificator app = new FixedSecretAuthentificator(new ConsoleInput(new Scanner(in)),new User("","",secret),secretGenerator,timeProvider,codeGenerator,qrGenerator,true);

            boolean result = app.TwoStepVerif();

            assertFalse(result);
        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }
}
