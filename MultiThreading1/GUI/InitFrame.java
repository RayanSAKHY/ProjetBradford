package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InitFrame extends JFrame {

    private JTextField nbClient;
    private JTextField nbStaff;
    private JTextField nbTea;
    private JTextField nbCake;
    private JTextField nbCoffee;
    private JButton startButton;

    public InitFrame() {
        setTitle("Betty's Cafe Initialisation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,2));

        panel.add(new JLabel("Number of clients :"));
        nbClient = new JTextField();
        panel.add(nbClient);

        panel.add(new JLabel("Number of staffs :"));
        nbStaff = new JTextField();
        panel.add(nbStaff);

        panel.add(new JLabel("Number of teas :"));
        nbTea = new JTextField();
        panel.add(nbTea);

        panel.add(new JLabel("Number of cake :"));
        nbCake = new JTextField();
        panel.add(nbCake);

        panel.add(new JLabel("Number of coffee :"));
        nbCoffee = new JTextField();
        panel.add(nbCoffee);

        startButton = new JButton("Démarrer");
        panel.add(startButton);


        add(panel);

        startButton.addActionListener((ActionEvent e) -> {
            int nbClients = Integer.parseInt(nbClientsField.getText());
            int nbStaff = Integer.parseInt(nbStaffField.getText());

            this.dispose();

            SwingUtilities.invokeLater(() -> new MainFrame(nbClients, nbStaff).setVisible(true));
        });
    }
}