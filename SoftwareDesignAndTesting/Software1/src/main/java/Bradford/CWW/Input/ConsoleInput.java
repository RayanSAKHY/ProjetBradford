package Bradford.CWW.Input;

import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleInput implements UserInput {
    private Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String askInput(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    @Override
    public void askInputAsync(String message, Consumer<String> queue) {
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void close() {
        scanner.close();
    }
}
