package IO;

public interface InputSource {
    public String readLine() throws InterruptedException;

    public void close();
}