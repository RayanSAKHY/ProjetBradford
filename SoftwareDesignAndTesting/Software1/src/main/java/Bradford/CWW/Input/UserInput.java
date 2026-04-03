package Bradford.CWW.Input;

import java.util.function.Consumer;

public interface UserInput {
    String askInput(String message);
    void askInputAsync(String message, Consumer<String> queue);
    void showMessage(String message);
    void close();
}
