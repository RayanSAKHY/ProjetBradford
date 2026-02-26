import assets.*;
import Utils.*;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Event.*;
import Thread.*;
import java.util.Vector;
import java.util.Map;

public class Main {
    private int nbClient;
    private int nbStaff;
    private static volatile Buffet buffet;

    private static RandomNumberGen gen = new RandomNumberGen(13253);
    private static ExecutionTime execTime = new ExecutionTime(gen,1);
    private static BlockingQueue<IEvent> commandQueue = new LinkedBlockingQueue<>();
    private static BlockingQueue<MessageEvent> messageQueue = new LinkedBlockingQueue<>();
    private static volatile boolean running = true; /*i have found information about that
    in this site: https://www.datacamp.com/doc/java/volatile */

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
        app.clientThreadInit(clientThreadList);

        for(Thread t : staffThreadList) {
            t.start();
        }
        for(Thread t : clientThreadList) {
            t.start();
        }

        while (running) {
            try {
                IEvent e = commandQueue.take();
                if (e instanceof QuitEvent) {
                    outputThread.interrupt();
                    for(Thread t : staffThreadList) {
                        t.interrupt();
                        t.join();
                        //System.out.println("fin du thread staff");
                    }
                    for(Thread t : clientThreadList) {
                        t.interrupt();
                        t.join();
                        //System.out.println("fin des client thread");
                    }
                    inputThread.interrupt();
                    inputThread.join();
                    //if (inputThread.isAlive()) System.out.println("fin des input thread");

                    //System.out.println("fin des thread");
                    running = false;
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
                            break;
                        case "b":
                            messageQueue.put(new MessageEvent(Categorie.BUFFET,buffet));
                            break;
                        case "g":
                            int capa = app.addClient(clientThreadList);
                            clientThreadList.get(capa).start();
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
        System.out.println("-b : Show current buffet");
        System.out.println("-q or quit or exit : exit the simulation");
        Scanner sc = new Scanner(System.in);
        System.out.print("How many clients do you want ? (minimum 5 clients) ");
        nbClient = sc.nextInt();
        if (nbClient <5) {
            nbClient = 5;
        }
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

    public void clientThreadInit(Vector<Thread> list) {
        for (int i = 0; i<nbClient;i++) {
            Client client = new Client();
            String name = "Client-"+i;

            list.add(new Thread(new ClientRunnable(buffet,
                    client,execTime, gen,
                    messageQueue),name));
        }
    }

    public int addClient(Vector<Thread> list) {
        int capa = list.size();
        Client client = new Client();
        String name = "Client-"+capa;

        list.add(new Thread(new ClientRunnable(buffet,
                client,execTime, gen,
                messageQueue),name));
        return capa+1;
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