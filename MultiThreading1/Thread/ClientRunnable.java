package Thread;

import assets.*;
import Utils.*;
import Event.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ClientRunnable implements Runnable{
    private static volatile Buffet buffet;
    private Client client;
    private volatile boolean running = true;
    private ExecutionTime execTime;
    private RandomNumberGen genAlea;
    private BlockingQueue<MessageEvent> messageQueue;
    private static volatile int nbPiano = 2;
    private final ReentrantLock Lock = new ReentrantLock(true);

    public ClientRunnable(Buffet buffet,Client client, ExecutionTime execTime,RandomNumberGen genAlea,
                          BlockingQueue<MessageEvent> messageQueue) {
        this.buffet = buffet;
        this.client = client;
        this.execTime = execTime;
        this.genAlea = genAlea;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String threadName = Thread.currentThread().getName();
                double nbAlea = genAlea.getProba();
                if (nbAlea >0.5) {
                    consume(threadName);
                    entertain(threadName);
                }
                else {
                    entertain(threadName);
                    consume(threadName);
                }
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    private void consume(String name) throws InterruptedException{
        double nbAlea = genAlea.getProba();
        int[] quantities = new int[2];
        for (int i = 0; i < 2; i++) {
            quantities[i]=genAlea.getRandomNumber(3);
            if (quantities[i] == 0) {
                quantities[i] = 1;
            }
        }
        String content = consume(nbAlea,quantities);
        messageQueue.put( new MessageEvent(Categorie.WANT,content,name));

        boolean hasWaited = false;
        if (!buffet.tryUse()) {
            messageQueue.put(new MessageEvent(Categorie.WAIT,"buffet", name));
            hasWaited = true;
            while(!buffet.tryUse() && running) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                Thread.sleep(1);
            }
        }
        else {
            if (hasWaited) {
                messageQueue.put(new MessageEvent(Categorie.END, name));
                hasWaited = false;
            }
            try {
                int time = (int)execTime.getExecutionTime();
                takeFromBuffet(content,buffet,name);
                messageQueue.put( new MessageEvent(Categorie.TAKE,content,name, time));
                Thread.sleep(time);
                String id = "drink";
                if (content.contains("cake")) {
                    id = "eat";
                }
                messageQueue.put(new MessageEvent(Categorie.END,id,name));
            } finally {
                buffet.release();
            }
        }
    }

    private void entertain(String name) throws InterruptedException{
        double nbAlea = genAlea.getProba();

        String content = entertain(nbAlea);
        int time = (int)execTime.getExecutionTime();
        switch(content) {
            case "piano":
                messageQueue.put( new MessageEvent(Categorie.WANT,content,name));
                boolean hasWaited = false;
                if (!Lock.tryLock() || nbPiano <= 0) {
                    messageQueue.put(new MessageEvent(Categorie.WAIT, "piano", name));
                    hasWaited = true;
                    while(!Lock.tryLock() && running) {
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        Thread.sleep(1);
                    }
                }
                else {
                    if (hasWaited) {
                        messageQueue.put(new MessageEvent(Categorie.END,"wait",name));
                        hasWaited = false;
                    }
                    try {
                        nbPiano--;
                        messageQueue.put( new MessageEvent(Categorie.PLAY,name,time));
                        Thread.sleep(time);
                        messageQueue.put(new MessageEvent(Categorie.END,content,name));
                    } finally {
                        nbPiano++;
                        Lock.unlock();
                    }
                }

                break;
            case "music":
                messageQueue.put( new MessageEvent(Categorie.LISTEN,name,
                        time));
                Thread.sleep(time);
                break;
            default:
                break;
        }

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

    private String consume(double nbAlea,int[] quantities) throws InterruptedException{
        String output = "";
        if (nbAlea > 0.5) {
            if (nbAlea > 0.75) {
                output = quantities[0] +" coffee";
            }
            else {
                output = quantities[0] +" tea";
            }
        }
        else {
            if ( nbAlea > 0.33) {
                output = quantities[0] + " cake";
            }
            else if (nbAlea > 0.165) {
                output = quantities[0] +" cake,"+quantities[1] +" coffee";
            }
            else {
                output = quantities[0] +" cake,"+quantities[1] +" tea";
            }
        }

        return output;
    }

    private void takeFromBuffet(String content,Buffet buffet,String name) throws InterruptedException{
        String[] mots = content.split(",");
        for (String mot:mots){
            boolean verif;
            String product = mot.split(" ")[1];
            int quantity = Integer.parseInt(mot.split(" ")[0]);

                switch (product) {
                    case "coffee":
                        verif = buffet.takeProduct(Product.COFFEE, quantity);
                        if (!verif) messageQueue.put(new MessageEvent(Categorie.WAITBUFFET,name));
                        while (!verif && running && !Thread.currentThread().isInterrupted()) {
                            verif = buffet.takeProduct(Product.COFFEE, quantity);
                            Thread.sleep(1);
                        }
                        break;
                    case "tea":
                        verif = buffet.takeProduct(Product.TEA, quantity);
                        if (!verif) messageQueue.put(new MessageEvent(Categorie.WAITBUFFET,name));
                        while (!verif && running && !Thread.currentThread().isInterrupted()) {
                            verif = buffet.takeProduct(Product.TEA, quantity);
                            Thread.sleep(1);
                        }
                        break;
                    case "cake":
                        verif = buffet.takeProduct(Product.CAKE, quantity);
                        if (!verif) messageQueue.put(new MessageEvent(Categorie.WAITBUFFET,name));
                        while (!verif && running && !Thread.currentThread().isInterrupted()) {
                            verif = buffet.takeProduct(Product.CAKE, quantity);
                            Thread.sleep(1);
                        }
                        break;
                    default:
                        break;
                }
        }
    }
}