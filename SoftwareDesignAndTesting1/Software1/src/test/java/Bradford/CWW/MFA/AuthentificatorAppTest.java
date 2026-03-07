package Bradford.CWW.MFA;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AuthentificatorAppTest {

    @Test
    public void testVerifyCodeShouldReturnTrue() {

        String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";


        try {
            TimeProvider timeProvider =  () -> 10000;
            DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

            long baseTime = timeProvider.getTime();
            String code = codeGenerator.generate(secret, baseTime);


            AuthentificatorApp app = new AuthentificatorApp(timeProvider, codeGenerator);

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
}
