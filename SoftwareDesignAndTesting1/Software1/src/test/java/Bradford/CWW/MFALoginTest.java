package Bradford.CWW;

import Bradford.CWW.MFA.AuthentificatorApp;
import Bradford.CWW.asssets.User;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
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
            String correctCode = codeGenerator.generate(secret, timeProvider.getTime()) + "\n";

            InputStream input = System.in;

            try {
                System.setIn(new ByteArrayInputStream(correctCode.getBytes()));

                MFALogin test = new MFALogin(new AuthentificatorApp());
                User testUser = new User();
                assertTrue(test.twoStepVerif(testUser));
            }
            finally {
                System.setIn(input);
            }

        }
        catch (dev.samstevens.totp.exceptions.CodeGenerationException ex) {
            ex.printStackTrace();
        }
    }
}
