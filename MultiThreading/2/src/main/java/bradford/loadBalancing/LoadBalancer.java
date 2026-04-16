package bradford.loadBalancing;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class LoadBalancer {
    private static ServerSocket listener;
    private static Logger logger;
    private static boolean running = true;
    private static final Map<PrintWriter,Integer> servers = new ConcurrentHashMap<>();
    private static final Map<String,PrintWriter> requestToClient = new ConcurrentHashMap<>();
    private static final Map<PrintWriter,Scanner> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        try {
            listener = new ServerSocket(59898);
            System.out.println("The load balancer is running...");


            logger = new Logger("log.txt");
            var pool = Executors.newFixedThreadPool(25);
            pool.execute(new Updater());

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
            System.exit(0);
        }
    }

    private static class Updater implements Runnable {

        @Override
        public void run() {
            while (running) {
                try {
                    for (PrintWriter out : servers.keySet()) {
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


    private static class Listener implements Runnable {
        private PrintWriter out;
        private Scanner in;

        Listener(Socket socket) {
            try {
                out = new PrintWriter(socket.getOutputStream(),true);
                in = new Scanner(socket.getInputStream());

                String type = in.nextLine();
                if (type.equals("SERVER")) {
                    synchronized (servers) {
                        servers.put(out, 0);
                    }
                }
                else if (type.equals("CLIENT")) {
                    synchronized (clients) {
                        clients.put(out,in);
                    }
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
                    String[] parts = line.split(":",3);
                    String requestId;
                    PrintWriter client;
                    switch (parts[0]) {
                        case "UPD":
                            int nbTask = Integer.parseInt(parts[1]);
                            synchronized (servers) {
                                servers.replace(out, nbTask);
                            }
                            break;
                        case "RES":
                            requestId = parts[1];
                            String data = parts[2];


                            logger.log("load balancer: "+requestId, "Result Received: " + data);
                            client = requestToClient.get(requestId);

                            long res = Long.parseLong(data);

                            if (client != null) {

                                client.println("RES:"+res);

                                requestToClient.remove(requestId);
                            }
                            break;
                        case "REQ":

                            String request = parts[2];
                            requestId = parts[1];

                            requestToClient.put(requestId,out);

                            if (request.equals("end")) {
                                running = false;
                                logger.log("load balancer: "+requestId, "Simulation ending");
                                for (PrintWriter out : servers.keySet()) {
                                    out.println("end");
                                }

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
                            else if (request.equals("EMPTY")) {
                                logger.log("load balancer: "+requestId,"Empty request");
                                break;
                            }

                            Map.Entry<PrintWriter, Integer> bestServer = null;

                            synchronized (servers) {
                                for (Map.Entry<PrintWriter, Integer> server : servers.entrySet()) {
                                    if (bestServer == null || server.getValue() < bestServer.getValue()) {
                                        bestServer = server;
                                    }
                                }
                            }

                            if (bestServer != null) {
                                bestServer.getKey().println("REQ:"+requestId+":" + request);
                            }


                            break;
                        case "LOG":
                            String[] info = parts[1].split("-");
                            logger.log(info[0],info[1]);
                            break;
                        case "MES":
                            requestId = parts[1];

                            logger.log("load Balancer :"+ requestId,"Message sent: "+parts[2]);
                            client = requestToClient.get(requestId);

                            if (client != null) {

                                client.println("MES:"+parts[2]);

                                requestToClient.remove(requestId);
                            }
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
