package Thread;

import Event.*;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;

public class InputRunnable implements Runnable {
    private BlockingQueue<IEvent> queue;
    private volatile boolean running;

    public InputRunnable(BlockingQueue<IEvent> queue,boolean running) {
        this.queue = queue;
        this.running = running;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                running = false;
                queue.offer(new QuitEvent());
            } else {
                queue.offer(new InputEvent(line));
            }
        }
        scanner.close();
    }

}