package assets;

public class Client {

    public synchronized int getQueue() {
        return queue;
    }

    public synchronized void setQueue(int queue) {
        this.queue = queue;
    }


}