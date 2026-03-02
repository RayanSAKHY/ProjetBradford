package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import java.awt.*;
import assets.*;
import Event.*;
import utils.*;
import IO.*;
import Thread.*;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainFrame extends JFrame {

    private static int nbClients;
    private static int nbStaff;
    private static volatile Buffet buffet;

    public MainFrame(int nbClients,int nbStaff,Buffet buffet) {
        this.nbClients = nbClients;
        this.nbStaff = nbStaff;
        this.buffet = buffet;

        InitWindow();

        setVisible(true);
    }

    private void InitWindow() {
        setTitle("Betty's Café - Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(250, 600));

        JTextArea output = new JTextArea(20,40);
        output.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(output);

        JButton speedUpButton = new JButton("Speed Up");
        JButton speedDownButton = new JButton("Speed Down");
        JButton addClientButton = new JButton("Add Client");
        rightPanel.add(speedUpButton);
        rightPanel.add(speedDownButton);
        rightPanel.add(addClientButton);

        rightPanel.add(new JLabel("Speed: "));
        JTextField speedField = new JTextField(10);
        rightPanel.add(speedField);

        rightPanel.add(new JLabel("Buffet: "));
        JTextField buffetField = new JTextField(10);
        rightPanel.add(buffetField);

        rightPanel.add(new JLabel("Staff: "));
        JTextField nbStaffField = new JTextField(10);
        rightPanel.add(nbStaffField);

        rightPanel.add(new JLabel("Clients: "));
        JTextField nbClientField = new JTextField(10);
        rightPanel.add(nbClientField);

        JSplitPane splitPanel = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                outputScroll,
                rightPanel
        );
        splitPanel.setDividerLocation(500);

        getContentPane().add(splitPanel, BorderLayout.CENTER);

        setSize(800,600);
        setLocationRelativeTo(null);
    }

    /*public void staffThreadInit(Vector<Thread> list) {
        Product[] listProduct= {Product.TEA,Product.COFFEE,Product.CAKE};

        for (int i = 0; i<nbStaff;i++) {
            Staff staff = new Staff(listProduct[i%3]);
            String name = "Staff-"+staff.toString()+i/3;

            list.add(new Thread(new StaffRunnable(buffet,
                    staff,execTime, gen,
                    messageQueue),name));
        }
    }

    public void clientThreadInit(Vector<Thread> list) {
        for (int i = 0; i < nbClient; i++) {
            Client client = new Client();
            String name = "Client-" + i;

            list.add(new Thread(new ClientRunnable(buffet,
                    client, execTime, gen,
                    messageQueue), name));
        }
    }*/
}


