package Bradford.CWW;

import Bradford.CWW.Input.ConsoleInput;
import Bradford.CWW.Input.UserInput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testCredentialsPrint() {
        String username = "username";
        String password = "password";

        String result = """
                Username: username
                Password: ********""";

        Login login = new Login(true);

        assertEquals(result,login.credentialsPrint(username,password));
    }

    @Test
    public void wrongPasswordShouldReturnFalse() {
        String username = "";
        String password = "password";

        Login login = new Login(true);

        assertFalse(login.loginConsole(username,password));
    }

    @Test
    public void wrongUsernameShouldReturnFalse() {
        String username = "username";
        String password = "";
        Login login = new Login(true);

        assertFalse(login.loginConsole(username,password));
    }

    @Test
    public void LoginPhoneCallSuccessfulShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("1\nbhsfkgvbkbk\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertTrue(result);
    }

    @Test
    public void LoginEmailSuccessfulShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("2\nbhsfkgvbkbk\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertTrue(result);
    }

    @Test
    public void LoginSMSSuccessfulShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("3\nbhsfkgvbkbk\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertTrue(result);
    }

    @Test
    public void LoginRandomAuthentificatorSuccessfulShouldReturnFalse() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("4\nbhsfkgvbkbk\n4\nguodj\n4\nguofsvb\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertFalse(result);
    }

    @Test
    public void LoginFixedAuthentificatorSuccessfulShouldReturnFalse() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("5\nbhsfkgvbkbk\n5\nguodj\n5\nguofsvb\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertFalse(result);
    }

    @Test
    public void testWrongChoiceShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("6\n1\ngfhskjbfg\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertTrue(result);
    }

    @Test
    public void testWrongChoiceShouldReturnFalse() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("6\n7\n8\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertFalse(result);
    }

    @Test
    public void testInvalidChoiceShouldReturnFalse() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("hf\ngfdkh\ngfhskjbfg\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertFalse(result);
    }

    @Test
    public void testInvalidChoiceShouldReturnTrue() {
        String username = "";
        String password = "";

        InputStream input = new ByteArrayInputStream("hf\ngfdkh\n1\niguhfdi\n".getBytes());

        Login login = new Login(new ConsoleInput(new Scanner(input)),true);

        boolean result = login.loginConsole(username,password);
        assertTrue(result);
    }

    @Test
    public void testGetScanneer() {
        Login login = new Login(true);
        UserInput userInput = login.getUserInput();

        assertNotNull(userInput);
    }

}
