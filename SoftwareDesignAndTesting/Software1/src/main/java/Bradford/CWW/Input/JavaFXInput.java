package Bradford.CWW.Input;

import java.awt.*;

public class JavaFXInput implements UserInput {
    private TextArea textArea;
    private TextField textField;

    public JavaFXInput(TextArea textArea, TextField textField) {
        this.textArea = textArea;
        this.textField = textField;
    }

    @Override
    public String askInput(String message) {
        textArea.append(message+"\n");
        return textField.getText();
    }

    @Override
    public void showMessage(String message) {
        textArea.append(message+"\n");
    }

    @Override
    public void close() {
    }
}
