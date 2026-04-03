package Bradford.CWW.Input;


import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class JavaFXInput implements UserInput {
    private TextArea textArea;
    private TextField textField;
    private Button button;

    public JavaFXInput(TextArea textArea, TextField textField, Button button) {
        this.textArea = textArea;
        this.textField = textField;
        this.button = button;
    }

    @Override
    public String askInput(String message) {
        return null;
    }

    @Override
    public void askInputAsync(String message, Consumer<String> queue) {
        textArea.setText(message);

        button.setOnAction(event -> {
            String input = textField.getText();
            textField.clear();
            queue.accept(input);
        });

    }

    @Override
    public void showMessage(String message) {
        textArea.setText(message+"\n");
    }

    @Override
    public void close() {
    }
}
