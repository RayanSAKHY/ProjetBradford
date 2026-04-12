package Bradford.CWW;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Bradford.CWW.assets.UserData;
import Bradford.CWW.assets.UserDataSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class UserInterfaceTest {

    @BeforeEach
    public void setUp() {
        UserDataSingleton.resetInstance();
    }

    @Test
    public void testInvalidInput() {
        InputStream input = new ByteArrayInputStream("dfighk\nfsdhgkds\n".getBytes());
        UserInterface app = new UserInterface(input,true);
        app.UseApp();
    }

    @Test
    public void testValidCreation1() {
        InputStream input = new ByteArrayInputStream("Y\ndjhsfg\nsgfhkj\nN\n3\n".getBytes());
        UserInterface app = new UserInterface(input,true);
        app.UseApp();
    }

    @Test
    public void testValidCreation2() {
        InputStream input = new ByteArrayInputStream("Y\ndjhsfg\nsgfhkj\ng\n3\n".getBytes());
        UserInterface app = new UserInterface(input,true);
        app.UseApp();
    }

    @Test
    public void testValidCreation3() {
        InputStream input = new ByteArrayInputStream("Y\ndjhsfg\nsgfhkj\nY\ndjhdsfsfg\nsgfsfhkj\nN\n3\n".getBytes());
        UserInterface app = new UserInterface(input,true);
        app.UseApp();
    }

    @Test
    public void testValidNothing() {
        InputStream input = new ByteArrayInputStream("N\n3\n".getBytes());
        UserInterface app = new UserInterface(input,true);
        app.UseApp();
    }

    @Test
    public void testConstruction() {
        UserInterface app = new UserInterface();
    }

    @Test
    public void testUseDemonstration() {
        InputStream input = new ByteArrayInputStream("N\n2\n1\ngfd\n2\nfhsgb\n3\ngfdf\n".getBytes());
        UserInterface app = new UserInterface(input,true);
        app.UseApp();
    }

    @Test
    public void testCreateAndUse() {
        InputStream input = new ByteArrayInputStream("Y\nghf\nfds\nN\n1\n1\nghf\nfds\n3\n".getBytes());
        UserInterface app = new UserInterface(input,true);
        app.UseApp();

        UserData users = UserDataSingleton.getInstance();
        assertTrue(users.usernameExists("ghf"));
    }

}
