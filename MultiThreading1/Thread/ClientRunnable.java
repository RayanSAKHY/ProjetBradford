package Thread;

import assets.*;
import Utils.*;
import Event.*;
import java.util.concurrent.BlockingQueue;

public class ClientRunnable implements Runnable{
    private Buffet buffet;
    private Client client;
    private Set<Int> pianoQueue;
    private Set<Int> buffetQueue;
    private boolean volatile running = true;
    private ExecutionTime execTime;
    private RandomNumberGen genAlea;
    private BlockingQueue<MessageEvent> messageQueue;

    public ClientRunnable(Buffet buffet,Client client, ExecutionTime execTime,RandomNumberGen genAlea,
                          BlockingQueue<MessageEvent> messageQueue) {
        this.buffet = buffet;
        this.client = client;
        this.execTime = execTime;
        this.genAlea = genAlea;
        this.messageQueue = messageQueue;
    }
}