import assets.*;
import Utils.*;
import java.io.IOException;

public class Main {
    private int nbClient;
    private int nbStaff;
    private static Buffet buffet;
    private static RandomNumberGen gen;
    private ExecutionTime execTime;
    private IOCommunication log = new IOCommunication();

    public static void main(String[] args) throws IOException {
        Main app = new Main();
        Main.gen = new RandomNumberGen(13253);
        app.run();
    }

    public void run() throws IOException{

        execTime = new ExecutionTime(gen,1);

        nbClient = log.Ask("How many customer do you want ?");
        System.out.println("Number of customer :" + nbClient);

        nbStaff = log.Ask("How many staff do you want ?");
        System.out.println("Number of customer :" + nbStaff);

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