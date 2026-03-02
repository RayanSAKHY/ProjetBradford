package workers;

import Event.*;
import java.util.concurrent.BlockingQueue;
import IO.*;

public class WriterRunnable implements Runnable {
    private BlockingQueue<MessageEvent> queue;
    private volatile boolean running = true;
    private OutputDest output;

    public WriterRunnable(BlockingQueue<MessageEvent> queue,OutputDest output) {
        this.queue = queue;
        this.output = output;
    }

    @Override
    public void run(){
        while (running) {
            try {
                MessageEvent current = queue.take();
                output.println(current.write());
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }
}
