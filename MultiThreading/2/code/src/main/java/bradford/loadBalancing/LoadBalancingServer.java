/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bradford.loadBalancing;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class LoadBalancingServer {
    private static boolean running = true;
    private static int nbTask=0;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        try (var socket = new Socket(args[0], 59898)) {
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
            out.println("SERVER");
            while (running) {
                if (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.equals("end")) {
                        running = false;
                        out.println("LOG:"+socket+"-Server closed");
                        System.out.println("Server stopped");
                        break;
                    }
                    else if (line.equals("upd")) {
                        out.println("UPD:"+nbTask);
                    }
                    else if (line.startsWith("REQ")) {
                        nbTask++;
                        String[] request  = line.split(":");
                        String requestId = request[1];
                        String data = request[2];
                        try {
                            long repet = Long.parseLong(data);

                            System.out.println("Starting the calculi of fibonacci for "+repet);
                            out.println("LOG:"+socket+"-Calculing fibonacci("+repet+")");
                            long result = fibonnaci(repet);

                            out.println("LOG:"+socket+"-fibonacci("+repet+") = "+result);
                            System.out.println("Fibonacci("+repet+") = "+result);
                            out.println("RES:"+requestId+":"+result);
                        }
                        catch (NumberFormatException e) {
                            System.out.println("Message received = "+data);
                            out.println("LOG:"+socket+"-Message received = "+data);
                            String response ="";
                            switch (data) {
                                case "I lost":
                                    response = "I lost the game";
                                    break;
                                case "Hello":
                                    response = "how can I helpp you";
                                    break;
                                case "How are you":
                                    response = "fine I finished it after several hour";
                                    break;
                                default:
                                    response = data;
                                    break;
                            }

                            out.println("LOG:"+socket+"-Response = "+response);
                            out.println("MES:"+requestId+":"+response);
                            System.out.println("Message sent: "+response);
                        }
                        System.out.println("\n");
                    }
                }
            }
        }
    }

    private static long fibonnaci(long n) {
        long[] cache = new long[2];
        cache[0] = 1;
        cache[1] = 1;
        if (n < 2) {
            return 1;
        }
        for (int i = 2; i <=n; i++) {
            long temp = cache[0] + cache[1];
            cache[0] = cache[1];
            cache[1] = temp;
        }

        return cache[1];
    }
}
