package assets;
public class Client {
    private int queue = 0; //0 = not in a queue ,  >0 = the place in the queue

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }
}