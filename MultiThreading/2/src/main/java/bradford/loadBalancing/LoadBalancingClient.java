package bradford.loadBalancing;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;



public class LoadBalancingClient {
    private static boolean running = true;
    private static String clientId;
    private static final AtomicInteger requestCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }

        Socket socket = new Socket(args[0],59898);
        try (socket) {
            Scanner input = new Scanner(System.in);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("CLIENT");

            System.out.println("Which Id do you want to have ?");
            clientId = input.nextLine();


            System.out.println("You can simulate user input with the console");
            System.out.println("Command: ");
            System.out.println("- Type a number to calculate Fibonacci of this value");
            System.out.println("- Type a message to maybe obtain a secret response");
            System.out.println("- Type \"end\" to stop the load balancing");

            new Thread(() -> {
               while (running && in.hasNextLine()) {
                   String line = in.nextLine();

                   if  (line.equals("end")) {
                       out.println("LOG:"+clientId+"-Server closing");
                       System.out.println("Simulation finishing");
                       running = false;
                       try {
                           socket.close();
                           in.close();
                           System.exit(0);
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }

                       break;
                   }
                   String response;
                   String[] parts = line.split(":");
                   response = switch (parts[0]) {
                       case "MES" -> "Message received: " + parts[1];
                       case "RES" -> "Result for Fibonnaci: " + parts[1];
                       default -> parts[1];
                   };
                   System.out.println(response);
                   out.println("LOG:" + clientId + "-Message received: " + line);
               }
            }).start();

            while (running) {
                if (input.hasNextLine()) {
                    String requestId = String.valueOf(requestCounter.incrementAndGet());
                    String line = input.nextLine();
                    if (line.isEmpty()) {
                        line = "EMPTY";
                    }

                    out.println("REQ:"+requestId+":"+line);
                    out.println("LOG:"+clientId+"-Message sent: "+line);
                }
            }
        }
    }
}
