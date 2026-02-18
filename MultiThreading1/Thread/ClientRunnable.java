package Thread;

import assets.*;
import Utils.*;
import Event.*;
import java.util.concurrent.BlockingQueue;
import java.util.Map;

public class ClientRunnable implements Runnable{
    private Buffet buffet;
    private Client client;
    private static  volatile Map<Int,String> pianoQueue;
    BlockingQueue<String> buffetQueue = new LinkedBlockingQueue<>();
    private boolean volatile running = true;
    private ExecutionTime execTime;
    private RandomNumberGen genAlea;
    private BlockingQueue<MessageEvent> messageQueue;
    private static int volatile nbPiano = 2;

    public ClientRunnable(Buffet buffet,Client client, ExecutionTime execTime,RandomNumberGen genAlea,
                          BlockingQueue<MessageEvent> messageQueue,
                          Map<Int,String> pianoQueue,Map<Int,String> buffetQueue) {
        this.buffet = buffet;
        this.client = client;
        this.execTime = execTime;
        this.genAlea = genAlea;
        this.messageQueue = messageQueue;
        this.pianoQueue = pianoQueue;
        this.buffetQueue = buffetQueue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String threadName = Thread.currentThread().getName();
                double nbAlea = genAlea.getProba();
                if (nbAlea >0.5) {
                    consume(threadName);
                }
            }
            catch (InteruptedException ex) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    private void consume(String name) throws InterruptedException{
        double nbAlea = genAlea.getProba();
        String content = client.consume(nbAlea);
        messageQueue.put( new MessageEvent(Categorie.WANT,content,name));

        if(buffet.tryUse()) {
            try {
                takeFromBuffet(content);
                messageQueue.put( new MessageEvent(Categorie.TAKE,content,name,
                        (int)execTime.getExecutionTime()));
            } finally {
                buffet.release();
            }
        } else {
            if (!buffetQueue.isEmpty()) {

                String nextClient = buffetQueue.poll();
                messageQueue.put(new MessageEvent(
                        Categorie.END,
                        "Next: " + nextClient,
                        nextClient
                ));
            }
        }
    }

    private void entertain(String name) throws InterruptedException{

    }

    private String entertain(double nbAlea) {
        String output = "";
        if (nbAlea > 0.5) {
            output = "piano";
        }
        else {
            output = "music";
        }
        return output;
    }

    private String consume(double nbAlea) throws InterruptedException{
        String output = "";
        if (nbAlea > 0.5) {
            if (nbAlea > 0.75) {
                output = "coffee";
            }
            else {
                output = "tea";
            }
        }
        else {
            if ( nbAlea > 0.33) {
                output = "cake";
            }
            else if (nbAlea > 0.165) {
                output = "cake,coffee";
            }
            else {
                output = "cake,tea";
            }
        }

        return output;
    }

    private void takeFromBuffet(String content) {
        String[] mots = content.split(",");
        for (String mot:mots){
            int quantity = genAlea.getRandomNumber(5);
            boolean verif;
            switch (mot){
                case "coffee":
                    do {
                        verif = buffet.takeProduct(COFFEE,quantity)
                    }
                    while (!verif);
                    break;
                case "tea":
                    do {
                        verif = buffet.takeProduct(TEA,quantity)
                    }
                    while (!verif);
                    break;
                case "cake":
                    do {
                        verif = buffet.takeProduct(CAKE,quantity)
                    }
                    while (!verif);
                    break;
                default:
                    break;
            }
        }
    }
}