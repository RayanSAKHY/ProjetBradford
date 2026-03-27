package IO;

import java.util.concurrent.BlockingQueue;

public class GUIInputSource implements InputSource {

    private BlockingQueue<String> queue;

    public GUIInputSource(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public String readLine() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void close(){
        queue.clear();
    }
}