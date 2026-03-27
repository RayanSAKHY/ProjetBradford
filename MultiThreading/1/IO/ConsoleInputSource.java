package IO;

import java.util.Scanner;

public class ConsoleInputSource implements InputSource {
    private Scanner sc = new Scanner(System.in);

    @Override
    public String readLine() throws InterruptedException{
        return sc.nextLine();
    }

    @Override
    public void close(){
        sc.close();
    }
}