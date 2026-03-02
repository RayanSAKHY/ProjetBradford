package IO;

public class GUIOutputDest implements OutputDest{
    private JTextArea output;

    public GUIOutputDest(JTextArea output) {
        this.output = output;
    }

    @Override public void println(String message) {
        SwingUtilities.invokeLater(() ->
                output.append(message + "\n")
        );
    }
}