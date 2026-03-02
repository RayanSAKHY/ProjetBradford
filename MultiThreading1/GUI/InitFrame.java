package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import assets.*;

public class InitFrame extends JFrame {

    private JTextField nbClientField;
    private JTextField nbStaffField;
    private JTextField nbTeaField;
    private JTextField nbCakeField;
    private JTextField nbCoffeeField;
    private JButton startButton;

    public InitFrame() {
        setTitle("Betty's Cafe Initialisation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,2));

        panel.add(new JLabel("Number of clients (minimum 5):"));
        nbClientField = new JTextField("5");
        panel.add(nbClientField);

        panel.add(new JLabel("Number of staffs (minimum 3):"));
        nbStaffField = new JTextField("3");
        panel.add(nbStaffField);

        panel.add(new JLabel("Number of teas :"));
        nbTeaField = new JTextField("2");
        panel.add(nbTeaField);

        panel.add(new JLabel("Number of cake :"));
        nbCakeField = new JTextField("2");
        panel.add(nbCakeField);

        panel.add(new JLabel("Number of coffee :"));
        nbCoffeeField = new JTextField("2");
        panel.add(nbCoffeeField);

        startButton = new JButton("Démarrer");
        panel.add(startButton);


        add(panel);

        pack();
        setLocationRelativeTo(null);

        startButton.addActionListener((ActionEvent e) -> {
            int nbClients = Integer.parseInt(nbClientField.getText());
            if (nbClients < 5) {
                nbClients = 5;
            }
            int nbStaff = Integer.parseInt(nbStaffField.getText());
            if (nbStaff < 3) {
                nbStaff = 3;
            }
            int nbTea = Integer.parseInt(nbTeaField.getText());
            int nbCake = Integer.parseInt(nbCakeField.getText());
            int nbCoffee = Integer.parseInt(nbCoffeeField.getText());

            this.dispose();

            Buffet buffet = new Buffet(nbTea, nbCoffee, nbCake);
            MainFrame mainFrame = new MainFrame(nbClients, nbStaff, buffet);
            mainFrame.setVisible(true);

            mainFrame.run();
        });
    }
}