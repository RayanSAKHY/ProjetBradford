import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import GUI.*;
import Event.*;
import Utils.*;
import assets.*;
import Thread.*;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainGUI {

    private int nbClient;
    private int nbStaff;
    private static volatile Buffet buffet;

    private static RandomNumberGen gen = new RandomNumberGen(13253);
    private static ExecutionTime execTime = new ExecutionTime(gen,1);
    private static BlockingQueue<IEvent> commandQueue = new LinkedBlockingQueue<>();
    private static BlockingQueue<MessageEvent> messageQueue = new LinkedBlockingQueue<>();
    private static volatile boolean running = true; /*i have found information about that
    in this site: https://www.datacamp.com/doc/java/volatile */

    public static void nap(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //
            //  Print out the name of the tread that caused this.
            //
            System.err.println("Thread " + Thread.currentThread().getName()
                    + " throwed exception " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        final CafePanel cafe = new CafePanel();
        final JFrame win = new JFrame("Betty's Café");
        win.setSize(800,500);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton speedUpButton = new JButton("Speed Up");
                JButton speedDownButton = new JButton("Speed Down");
                rightPanel.add(speedUpButton);
                rightPanel.add(speedDownButton);

                JTextArea output = new JTextArea();
                output.setEditable(false);
                JScrollPane outputScroll = new JScrollPane(output);

                JTextField input = new JTextField();
                rightPanel.add(input);


                Vector<Thread> staffThreadList = new Vector<>();
                Vector<Thread> clientThreadList = new Vector<>();

//
//                killButton.addActionListener(
//                        new ActionListener() {
//                            public void actionPerformed(ActionEvent e) {
//                                Iterator<Ball> iterator = ballList.iterator();
//
//                                while (iterator.hasNext()) {
//                                    Ball temp = iterator.next();
//
//                                    if (temp.isAlive()) {
//                                        temp.interrupt();
//                                        ballsPanel.removeBall(temp);
//                                        iterator.remove();
//                                        break;
//                                    }
//                                }
//
//                            }
//                        }
//                );
//
//                pauseButton.addActionListener(
//                        new ActionListener() {
//                            public void actionPerformed(ActionEvent e) {
//                                for (Ball temp : ballList) {
//                                    temp.pauseBall();
//                                }
//                            }
//                        }
//                );
//
//                restartButton.addActionListener(
//                        new ActionListener() {
//                            public void actionPerformed(ActionEvent e) {
//                                for (Ball temp : ballList) {
//                                    temp.resumeBall();
//                                }
//                            }
//                        }
//                );
                JSplitPane splitPanel = new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        outputScroll,
                        rightPanel
                );
                splitPanel.setDividerLocation(500);

                win.getContentPane().add(splitPanel);

                win.setVisible(true);
            }
        });
    }
}