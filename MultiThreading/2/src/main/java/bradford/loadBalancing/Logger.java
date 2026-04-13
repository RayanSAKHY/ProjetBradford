package bradford.loadBalancing;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private FileWriter writer;
    private final StringBuilder sb;


    public Logger(String filename) {
        sb = new StringBuilder();

        try {
            writer = new FileWriter(filename);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String context,String message) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm:ss"));
        sb.append("[").append(timeStamp).append("] ").append(context);
        sb.append(" -> ").append(message).append("\n");
    }

    public void close() {
        try {
            writer.write(sb.toString());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
