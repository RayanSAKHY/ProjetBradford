package Thread;

import Event.*;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;
import java.io.InputStream;

public class InputRunnable implements Runnable {
    private BlockingQueue<IEvent> queue;
    private volatile boolean running;
    private InputStream in;

    public InputRunnable(BlockingQueue<IEvent> queue,boolean running,InputStream in) {
        this.queue = queue;
        this.running = running;
        this.in = in;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(in);
        while (running) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                running = false;
                queue.offer(new QuitEvent());
            } else {
                queue.offer(new InputEvent(line));
            }
        }
        scanner.close();
    }

}