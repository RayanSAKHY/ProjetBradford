package Thread;

import Event.*;
import java.util.concurrent.BlockingQueue;
import assets.Buffet;

public class WriterRunnable implements Runnable {
    private BlockingQueue<MessageEvent> queue;
    private volatile boolean running = true;
    private Buffet buffet;

    public WriterRunnable(BlockingQueue<MessageEvent> queue,Buffet buffet) {
        this.queue = queue;
        this.buffet = buffet;
    }

    @Override
    public void run(){
        while (running) {
            try {
                MessageEvent current = queue.take();
                System.out.println(current.write());
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }
}
