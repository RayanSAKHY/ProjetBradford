package Thread;

import Event.*;
import java.util.concurrent.BlockingQueue;
import assets.Buffet;
import java.io.OutputStream;
import java.io.PrintWriter;

public class WriterRunnable implements Runnable {
    private BlockingQueue<MessageEvent> queue;
    private volatile boolean running = true;
    private Buffet buffet;
    private PrintWriter output;

    public WriterRunnable(BlockingQueue<MessageEvent> queue,Buffet buffet,OutputStream output) {
        this.queue = queue;
        this.buffet = buffet;
        this.output = new PrintWriter(output,true);
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
