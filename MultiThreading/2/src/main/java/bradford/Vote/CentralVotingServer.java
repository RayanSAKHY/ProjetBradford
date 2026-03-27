package bradford.Vote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class CentralVotingServer {
    private static Map<String,Map<String,Integer>> finalVote = new ConcurrentHashMap<>();

    /**
     * Runs the server. When a client connects, the server spawns a new thread to do
     * the servicing and immediately returns to listening. The application limits
     * the number of threads via a thread pool (otherwise millions of clients could
     * cause the server to run out of resources by allocating too many threads).
     */
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898)) {
            System.out.println("The capitalization server is running...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Capitalizer(listener.accept()));
            }
        }
    }

    private static class Capitalizer implements Runnable {
        private Socket socket;

        Capitalizer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {
                var in = new Scanner(socket.getInputStream());
                var out = new PrintWriter(socket.getOutputStream(), true);

                String centerId = "unknown";
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.startsWith("Center:")) {
                        centerId = line.split(":")[1];
                        System.out.println("Receiving data from center " + centerId);
                        Map<String, Integer> centerResult = new ConcurrentHashMap<>();
                        finalVote.putIfAbsent(centerId, centerResult);
                    }
                    else if (line.equals("END")) {
                        break;
                    }
                    else {
                        Map<String, Integer> centerResult = finalVote.get(centerId);
                        String party = line.split(":")[0];
                        String vote = line.split(":")[1];
                        int votesCount = Integer.parseInt(vote.split(" ")[0]);
                        centerResult.put(party,votesCount);
                    }
                }

                for(String id : finalVote.keySet()) {
                    System.out.println(id + " -> " + finalVote.get(id));
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }
    }
}