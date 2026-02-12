package assets;
public class Client {
    private int queue = 0; //0 = not in a queue ,  >0 = the place in the queue

    public synchronized int getQueue() {
        return queue;
    }

    public synchronized void setQueue(int queue) {
        this.queue = queue;
    }

}