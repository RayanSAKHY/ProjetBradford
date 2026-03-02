import java.util.Iterator;
import javax.swing.*;
import GUI.*;
import assets.Buffet;

public class MainGUI {

    private int nbClient;
    private int nbStaff;
    private static volatile Buffet buffet;

    public static void main(String[]  args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InitFrame initFrame = new InitFrame();
                initFrame.setVisible(true);
            }
        });
    }
}
