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
import Utils.*;
import IO.*;
import workers.*;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainFrame extends JFrame {

    private int nbClients;
    private int nbStaff;
    private volatile Buffet buffet;

    private static RandomNumberGen gen = new RandomNumberGen(13253);
    private static ExecutionTime execTime = new ExecutionTime(gen,1);
    private static BlockingQueue<IEvent> commandQueue = new LinkedBlockingQueue<>();
    private static BlockingQueue<MessageEvent> messageQueue = new LinkedBlockingQueue<>();
    private static volatile boolean running = true; /*i have found information about that
    in this site: https://www.datacamp.com/doc/java/volatile */
    private static InputSource inputSource;
    private static OutputDest outputDest;
    private Vector<Thread> staffThreadList = new Vector<>();
    private Vector<Thread> clientThreadList = new Vector<>();
    private Thread outputThread;

    private JTextArea output;
    private JButton speedUpButton;
    private JButton speedDownButton;
    private JButton addClientButton;
    private JButton buffetButton;
    private JTextField speedField;
    private JTextField nbStaffField;
    private JTextField nbClientField;

    public MainFrame(int nbClients,int nbStaff,Buffet buffet) {
        this.nbClients = nbClients;
        this.nbStaff = nbStaff;
        this.buffet = buffet;

        InitWindow();

        outputDest= new GUIOutputDest(output);
        outputThread = new Thread(new WriterRunnable(messageQueue,outputDest));

        staffThreadInit(staffThreadList);
        clientThreadInit(clientThreadList);




    }

    private void InitWindow() {
        setTitle("Betty's Café - Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(250, 600));

        output = new JTextArea(20,40);
        output.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(output);

        speedUpButton = new JButton("Speed Up");
        speedDownButton = new JButton("Speed Down");
        addClientButton = new JButton("Add Client");
        buffetButton = new JButton("Buffet");
        rightPanel.add(speedUpButton);
        rightPanel.add(speedDownButton);
        rightPanel.add(addClientButton);
        rightPanel.add(buffetButton);

        speedField = new JTextField(10);
        rightPanel.add(speedField);



        nbStaffField = new JTextField(10);
        rightPanel.add(nbStaffField);

        nbClientField = new JTextField(10);
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

    public void staffThreadInit(Vector<Thread> list) {
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
        for (int i = 0; i < nbClients; i++) {
            Client client = new Client();
            String name = "Client-" + i;

            list.add(new Thread(new ClientRunnable(buffet,
                    client, execTime, gen,
                    messageQueue), name));
        }
    }

    private void updateFields() {
        nbStaffField.setText("Number of staff : " + nbStaff);
        nbClientField.setText("Number of clients : " + nbClients);
        speedField.setText("Speed : " + execTime.getSpeed());
    }

    public void run() {
        outputThread.start();

        for(Thread t : clientThreadList) {
            t.start();
        }

        for(Thread t : staffThreadList) {
            t.start();
        }
        speedUpButton.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    execTime.speedUp();
                    updateFields();
                    messageQueue.put(new MessageEvent(Categorie.LOG,"Speed Up"));
                }
                catch (InterruptedException ex) {
                    running = false;
                    Thread.currentThread().interrupt();
                }

            }
        });

        speedDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    execTime.speedDown();
                    updateFields();
                    messageQueue.put(new MessageEvent(Categorie.LOG,"Speed Down"));
                }
                catch (InterruptedException ex) {
                    running = false;
                    Thread.currentThread().interrupt();
                }
            }
        });

        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    int capa = clientThreadList.size();
                    Client client = new Client();
                    String name = "Client-"+capa;
                    nbClients++;
                    Thread thread = new Thread(new ClientRunnable(buffet,
                            client,execTime, gen,
                            messageQueue),name);

                    clientThreadList.add(thread);
                    thread.start();
                    messageQueue.put(new MessageEvent(Categorie.LOG,"Adding a new client"));
                    updateFields();
                }
                catch (InterruptedException ex){
                    running = false;
                    Thread.currentThread().interrupt();
                }
            }
        });

        buffetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    messageQueue.put(new MessageEvent(Categorie.BUFFET,buffet));
                }
                catch (InterruptedException ex) {
                    running = false;
                    Thread.currentThread().interrupt();
                }
            }
        });
        updateFields();
    }
}


