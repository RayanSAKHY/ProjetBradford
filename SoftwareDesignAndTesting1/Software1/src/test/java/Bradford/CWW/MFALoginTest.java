package Bradford.CWW;

import Bradford.CWW.MFA.RandomSecretAuthentificatorApp;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MFALoginTest {

    @Test
    public void InvalidStrategyShouldReturnFalse() {
        MFALogin test = new MFALogin(null);
        assertFalse(test.twoStepVerif());
    }

    @Test
    public void ValidStrategyShouldReturnFalse() {
        String wrongCode = "123456";

        InputStream input = System.in;

        try {
            System.setIn(new ByteArrayInputStream(wrongCode.getBytes()));
            RandomSecretAuthentificatorApp app = new RandomSecretAuthentificatorApp();
            app.setTestMode(true);
            MFALogin test = new MFALogin(app);
            assertFalse(test.twoStepVerif());
        }
        finally {
            System.setIn(input);
        }

    }

    @Test
    public void ValidStrategyShouldReturnTrue() {
        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";

        try {
            TimeProvider timeProvider = () -> 10000;
            CodeGenerator codeGenerator = new DefaultCodeGenerator();
            String correctCode = codeGenerator.generate(secret, timeProvider.getTime()/60) + "\n";
            SecretGenerator secretGenerator = new SecretGenerator() {
                @Override
                public String generate() {
                    return "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";
                }
            };

            InputStream input = new ByteArrayInputStream(correctCode.getBytes());

            RandomSecretAuthentificatorApp app = new RandomSecretAuthentificatorApp(new Scanner(input),secretGenerator,timeProvider,codeGenerator,new ZxingPngQrGenerator(),true);
            app.setTestMode(true);
            MFALogin test = new MFALogin(app);
            assertTrue(test.twoStepVerif());

        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }
}
