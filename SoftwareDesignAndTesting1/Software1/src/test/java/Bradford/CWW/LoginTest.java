package Bradford.CWW;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testCredentialsPrint() {
        String username = "username";
        String password = "password";

        String result = """
                Username: username
                Password: ********
                """;

        Login login = new Login();

        assertEquals(result,login.credentialsPrint(username,password));
    }

    @Test
    public void wrongPasswordShouldReturnFalse() {
        String username = "";
        String password = "password";

        Login login = new Login();

        assertFalse(login.login(username,password));
    }

    @Test
    public void wrongUsernameShouldReturnFalse() {
        String username = "username";
        String password = "";
        Login login = new Login();

        assertFalse(login.login(username,password));
    }

    @Test
    public void LoginPhoneCallSuccessfulShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("1\nbhsfkgvbkbk\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertTrue(result);
    }

    @Test
    public void LoginEmailSuccessfulShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("2\nbhsfkgvbkbk\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertTrue(result);
    }

    @Test
    public void LoginSMSSuccessfulShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("3\nbhsfkgvbkbk\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertTrue(result);
    }

    @Test
    public void LoginAuthentificatorSuccessfulShouldReturnFalse() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("4\nbhsfkgvbkbk\nguodj\nguofsvb\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertFalse(result);
    }

    @Test
    public void testWrongChoiceShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("5\n1\ngfhskjbfg\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertTrue(result);
    }

    @Test
    public void testWrongChoiceShouldReturnFalse() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("5\n6\n7\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertFalse(result);
    }

    @Test
    public void testInvalidChoiceShouldReturnFalse() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("hf\ngfdkh\ngfhskjbfg\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertFalse(result);
    }

    @Test
    public void testInvalidChoiceShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("hf\ngfdkh\n1\niguhfdi\n".getBytes());

        Login login = new Login(input);

        boolean result = login.login(username,password);
        assertTrue(result);
    }
}
