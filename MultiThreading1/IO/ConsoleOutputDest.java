package IO;

public class ConsoleOutputDest implements OutputDest{
    @Override
    public void println(String message) {
        System.out.println(message);
    }
}