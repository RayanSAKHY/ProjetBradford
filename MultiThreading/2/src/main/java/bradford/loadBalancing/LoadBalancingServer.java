package bradford.loadBalancing;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class LoadBalancingServer {
    private static ServerSocket listener;
    private static Logger logger;
    private static boolean running = true;
    private static final Map<PrintWriter,Integer> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        try {
            listener = new ServerSocket(59898);
            System.out.println("The load balancing server is running...");
            System.out.println("Command: ");
            System.out.println("- Type a number to calculate Fibonacci of this value");
            System.out.println("- Type a message to maybe obtain a secret response");
            System.out.println("- Type \"end\" to stop the load balancing");


            logger = new Logger("log.txt");
            var pool = Executors.newFixedThreadPool(25);
            pool.execute(new Updater());
            pool.execute(new OrderSender());

            try {
                while (running) {
                    pool.execute(new Listener(listener.accept()));
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
        finally {
            logger.close();
        }
    }

    private static class Updater implements Runnable {

        @Override
        public void run() {
            while (running) {
                try {
                    for (PrintWriter out : clients.keySet()) {
                        out.println("upd");
                    }
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
         }
    }

    private static class OrderSender implements Runnable {
        private Scanner in = new Scanner(System.in);

        @Override
        public void run() {
            while (running && in.hasNextLine()) {
                String line = in.nextLine();
                if (line.equals("end")) {
                    running = false;
                    for (PrintWriter out : clients.keySet()) {
                        out.println("end");
                    }

                    try {
                        listener.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                Map.Entry<PrintWriter, Integer> bestClient;
                synchronized (clients) {
                    bestClient = null;
                    for (Map.Entry<PrintWriter, Integer> client : clients.entrySet()) {
                        if (bestClient == null || client.getValue() < bestClient.getValue()) {
                            bestClient = client;
                        }
                    }
                }

                if (bestClient != null) {
                    bestClient.getKey().println(line);
                }

            }
        }
    }

    private static class Listener implements Runnable {
        private PrintWriter out;
        private Scanner in;

        Listener(Socket socket) {
            try {
                out = new PrintWriter(socket.getOutputStream(),true);
                in = new Scanner(socket.getInputStream());

                synchronized (clients) {
                    clients.put(out,0);
                }

            }
            catch (IOException e) {
                System.err.println("Could not open connection to socket");
            }

        }

        @Override
        public void run() {
            while (running) {
                if (in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] parts = line.split(":");
                    switch (parts[0]) {
                        case "UPD":
                            int nbTask = Integer.parseInt(parts[1]);
                            synchronized (clients) {
                                clients.replace(out, nbTask);
                            }
                            break;
                        case "RES":
                            long res = Long.parseLong(parts[1]);
                            System.out.println("Result for fibonacci: "+res);
                            break;
                        case "LOG":
                            String[] info = parts[1].split("-");
                            logger.log(info[0],info[1]);
                            break;
                        case "MES":
                            System.out.println(parts[1]);
                            break;
                        default:
                            System.out.println("Unknown command");
                            break;
                    }
                }
            }
        }
    }
}
