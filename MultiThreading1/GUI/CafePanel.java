package GUI;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class CafePanel extends JPanel {

    private final int xSize = 800;
    private final int ySize = 500;

    private final static Color BGCOLOR = Color.white;

    public CafePanel() {
        setPreferredSize(new Dimension(xSize,ySize));
        setOpaque(true);
        setBackground(BGCOLOR);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}