import assets.*;
import Utils.*;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Event.*;
import Thread.*;

public class Main {
    private int nbClient;
    private int nbStaff;
    private static Buffet buffet;

    private static RandomNumberGen gen = new RandomNumberGen(13253);
    private static ExecutionTime execTime = new ExecutionTime(gen,1);
    private static BlockingQueue<IEvent> commandQueue = new LinkedBlockingQueue<>();
    private static BlockingQueue<MessageEvent> messageQueue = new LinkedBlockingQueue<>();
    private static volatile boolean running = true; /*i have found information about that
    in this site: https://www.datacamp.com/doc/java/volatile */

    public static void main(String[] args){
        Main app = new Main();
        app.Init();
        app.test();

        Thread inputThread = new Thread(new InputRunnable(commandQueue,running));
        Thread outputThread = new Thread(new WriterRunnable(messageQueue,buffet));
        outputThread.start();
        inputThread.start();


        while (running) {
            try {
                IEvent e = commandQueue.take();
                if (e instanceof QuitEvent) {
                    running = false;
                    outputThread.interrupt();
                }
                else if (e instanceof InputEvent){
                    switch(e.getInfo()) {
                        case "f":
                            execTime.speedDown();
                            System.out.println("Speed Down");
                            break;
                        case "h":
                            execTime.speedUp();
                            System.out.println("Speed Up");
                            break;
                        case "g":
                            System.out.println("Adding a new client");
                            break;
                        default:
                            break;
                    }
                    System.out.println("InputEvent Recu : " + e.getInfo());
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Programme terminé");
    }

    public void Init(){
        Scanner sc = new Scanner(System.in);
        System.out.print("How many clients do you want ? ");
        nbClient = sc.nextInt();
        System.out.print("How many staffs do you want ? ");
        nbStaff = sc.nextInt();
        System.out.print("How many tea do you want initially ? ");
        int nbTea = sc.nextInt();
        System.out.print("How many cakes do you want initially ? ");
        int nbCake = sc.nextInt();
        System.out.print("How many coffee do you want initially ? ");
        int nbCoffee = sc.nextInt();
        buffet = new Buffet(nbTea,nbCoffee,nbCake);
    }

    public void test(){

        Staff staff1 = new Staff(Product.TEA);
        Staff staff2 = new Staff(Product.CAKE);
        Staff staff3 = new Staff(Product.COFFEE);
        Client cliet1 = new Client();

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