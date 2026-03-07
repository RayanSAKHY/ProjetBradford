package Bradford.CWW;

import Bradford.CWW.MFA.AuthentificatorApp;
import Bradford.CWW.asssets.User;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class MFALoginTest {

    @Test
    public void InvalidStrategyShouldReturnFalse() {
        MFALogin test = new MFALogin(null);
        assertFalse(test.twoStepVerif(new User()));
    }

    @Test
    public void ValidStrategyShouldReturnFalse() {
        String wrongCode = "123456";

        InputStream input = System.in;

        try {
            System.setIn(new ByteArrayInputStream(wrongCode.getBytes()));

            MFALogin test = new MFALogin(new AuthentificatorApp());
            User testUser = new User();
            assertFalse(test.twoStepVerif(testUser));
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
            String correctCode = codeGenerator.generate(secret, timeProvider.getTime()/30) + "\n";
            SecretGenerator secretGenerator = new SecretGenerator() {
                @Override
                public String generate() {
                    return "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";
                }
            };

            InputStream input = new ByteArrayInputStream(correctCode.getBytes());

            MFALogin test = new MFALogin(new AuthentificatorApp(timeProvider, codeGenerator,input,new ZxingPngQrGenerator(),secretGenerator));
            User testUser = new User();
            assertTrue(test.twoStepVerif(testUser));

        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }
}
