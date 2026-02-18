import assets.*;
import Utils.*;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Event.*;
import Thread.*;
import java.util.Vector;

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
    private static  volatile Map<Int,String> pianoQueue = new Map<>();
    private static volatile Map<Int,String> buffetQueue = new Map<>();

    public static void main(String[] args){
        Main app = new Main();
        app.Init();
        //app.test();

        Thread inputThread = new Thread(new InputRunnable(commandQueue,running));
        Thread outputThread = new Thread(new WriterRunnable(messageQueue,buffet));
        Vector<Thread> staffThreadList = new Vector<>();
        Vector<Thread> clientThreadList = new Vector<>();
        outputThread.start();
        inputThread.start();
        //app.testMessage();

        app.staffThreadInit(staffThreadList);

        for(Thread t : staffThreadList) {
            t.start();
        }

        while (running) {
            try {
                IEvent e = commandQueue.take();
                if (e instanceof QuitEvent) {
                    running = false;
                    outputThread.interrupt();
                    for(Thread t : staffThreadList) {
                        t.interrupt();
                    }
                }
                else if (e instanceof InputEvent){
                    switch(e.getInfo()) {
                        case "f":
                            execTime.speedDown();
                            messageQueue.put(new MessageEvent(Categorie.LOG,"Speed Down"));
                            break;
                        case "h":
                            execTime.speedUp();
                            messageQueue.put(new MessageEvent(Categorie.LOG,"Speed Up"));
                            break;
                        case "t":
                            messageQueue.put(new MessageEvent(Categorie.LOG,"Current Speed : "+execTime.getSpeed()));
                        case "g":
                            messageQueue.put(new MessageEvent(Categorie.LOG,"Adding a new client"));
                            break;
                        default:
                            break;
                    }
                    //messageQueue.put(new MessageEvent(Categorie.LOG,"input key : "+e.getInfo()));
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("The end of the simulation");
    }

    public void Init(){
        System.out.println("Here the control for this simulation :");
        System.out.println("-f : Speed Down");
        System.out.println("-g : Adding a new client");
        System.out.println("-h : Speed Up");
        System.out.println("-t : Show current speed");
        System.out.println("-q or quit or exit : exit the simulation");
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

    public void testMessage() {
        try {
            messageQueue.put( new MessageEvent(Categorie.WANT,"piano","Client-0"));
            messageQueue.put( new MessageEvent(Categorie.PLAY,"CLient-1",(int)execTime.getExecutionTime()));
            messageQueue.put( new MessageEvent(Categorie.TAKE,"coffee,cake","Client-2",(int)execTime.getExecutionTime()));
            messageQueue.put( new MessageEvent(Categorie.BUFFET,buffet));
            messageQueue.put( new MessageEvent(Categorie.LISTEN,"Client-3",(int)execTime.getExecutionTime()));
            messageQueue.put( new MessageEvent(Categorie.END,"drink","Client-4"));
            messageQueue.put( new MessageEvent(Categorie.END,"eat","Client-4"));
            messageQueue.put( new MessageEvent(Categorie.END,"piano","Client-4"));
            messageQueue.put( new MessageEvent(Categorie.END,"music","Client-4"));
            messageQueue.put( new MessageEvent(Categorie.STAFF,"tea","Staff-0",3));
            messageQueue.put( new MessageEvent(Categorie.KITCHEN,"Staff-1"));
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}