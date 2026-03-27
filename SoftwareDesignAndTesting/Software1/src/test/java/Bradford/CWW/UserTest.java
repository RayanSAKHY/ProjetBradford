package Bradford.CWW;

import Bradford.CWW.assets.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void testUsername() {
        User user = new User();

        user.setUsername("jvgds");

        assertEquals("jvgds", user.getUsername());

    }

    @Test
    public void testPassword() {
        User user = new User();

        user.setPassword("jvgds");
        assertEquals("jvgds", user.getPassword());
    }
}
