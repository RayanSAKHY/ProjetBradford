package Thread;

import assets.*;
import Utils.*;
import Event.*;
import java.util.concurrent.BlockingQueue;

public class StaffRunnable  implements Runnable {
    private Buffet buffet;
    private Staff staff;
    private volatile boolean running = true;
    private ExecutionTime execTime;
    private RandomNumberGen genAlea;
    private BlockingQueue<MessageEvent> queue;

    public StaffRunnable(Buffet buffet,Staff staff,ExecutionTime execTime,
                         RandomNumberGen genAlea,BlockingQueue<MessageEvent> queue) {
        this.buffet = buffet;
        this.staff = staff;
        this.execTime = execTime;
        this.genAlea = genAlea;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String threadName = Thread.currentThread().getName();
                int time = (int) execTime.getExecutionTime();

                int nbProduct = genAlea.getRandomNumber(3);
                if (nbProduct == 0) {
                    nbProduct = 1;
                }
                Thread.sleep(time);

                synchronized(buffet) {
                    staff.addToBuffet(buffet,nbProduct);
                }
                queue.put(new MessageEvent(Categorie.STAFF,staff.toString(),
                        threadName,nbProduct) );
                queue.put( new MessageEvent(Categorie.BUFFET,buffet));
                queue.put( new MessageEvent(Categorie.KITCHEN,threadName));
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                running = false;
            }

        }
    }

}