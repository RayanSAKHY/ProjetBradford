package Thread;

import Event.*;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;
import java.io.InputStream;

public class InputRunnable implements Runnable {
    private BlockingQueue<IEvent> queue;
    private volatile boolean running = true;
    private InputStream in;

    public InputRunnable(BlockingQueue<IEvent> queue,InputStream in) {
        this.queue = queue;
        this.in = in;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(in);
        try{
            while (running) {
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                    running = false;
                    queue.put(new QuitEvent());
                } else {
                    queue.put(new InputEvent(line));
                }
            }
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            running = false;
        }
        finally {
            scanner.close();
        }
    }

}