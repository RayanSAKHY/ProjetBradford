package workers;

import Event.*;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;
import java.io.InputStream;
import IO.*;

public class InputRunnable implements Runnable {
    private BlockingQueue<IEvent> queue;
    private volatile boolean running = true;
    private InputSource inputSource;

    public InputRunnable(BlockingQueue<IEvent> queue,InputSource inputSource) {
        this.queue = queue;
        this.inputSource = inputSource;
    }

    @Override
    public void run() {
        try{
            while (running) {
                String line = inputSource.readLine();
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
            inputSource.close();
        }
    }

}