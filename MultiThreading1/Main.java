import assets.*;
import Utils.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Event.*;
import Thread.*;

public class Main {
    private int nbClient;
    private int nbStaff;
    private static Buffet buffet;
    private static RandomNumberGen gen;
    private ExecutionTime execTime;
    private static BlockingQueue<IEvent> queue = new LinkedBlockingQueue<>();
    private static volatile boolean running = true; /*i have found information about that
    in this site: https://www.datacamp.com/doc/java/volatile */

    public static void main(String[] args) {

        Thread inputThread = new Thread(new InputRunnable(queue,running));
        inputThread.start();

        Main.gen = new RandomNumberGen(13253);

        while (running) {
            try {
                IEvent e = queue.take();
                if (e instanceof QuitEvent) {
                    running = false;
                } else {
                    System.out.println("Reçu : " + e);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Programme terminé");

        Main app = new Main();
        app.test();
    }

    public void test(){

        execTime = new ExecutionTime(gen,1);

        Staff staff1 = new Staff(Product.TEA);
        Staff staff2 = new Staff(Product.CAKE);
        Staff staff3 = new Staff(Product.COFFEE);
        Client cliet1 = new Client();
        buffet = new Buffet(2,2,2);

        System.out.println("buffet cake : " + buffet.getCake());
        System.out.println("buffet coffee : " + buffet.getCoffee());
        System.out.println("buffet tea : " + buffet.getTea());

        staff1.addToBuffet(buffet,3);
        staff2.addToBuffet(buffet,3);
        staff3.addToBuffet(buffet,3);

        System.out.println("buffet cake : " + buffet.getCake());
        System.out.println("buffet coffee : " + buffet.getCoffee());
        System.out.println("buffet tea : " + buffet.getTea());
    }
}