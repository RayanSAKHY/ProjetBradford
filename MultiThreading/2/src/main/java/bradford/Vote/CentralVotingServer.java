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
    private static boolean running = true;

    /**
     * Runs the server. When a client connects, the server spawns a new thread to do
     * the servicing and immediately returns to listening. The application limits
     * the number of threads via a thread pool (otherwise millions of clients could
     * cause the server to run out of resources by allocating too many threads).
     */
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898)) {
            System.out.println("The voting aggregator server is running...");
            System.out.println("Type \"end\" after all the voting center sent their result (you need at least 1 result)\n");

            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (running) {
                    String line = scanner.nextLine();
                    if (line.equals("end")) {
                        if (finalVote.isEmpty()) {
                            System.out.println("No voting centers sent their result");
                        } else {
                            running = false;
                            System.out.println("Voting finished");
                            Agregator agregator = new Agregator(finalVote);
                            agregator.getWinner();
                            agregator.getProbaResult();
                            agregator.getResult();
                        }
                    }
                }
            }).start();

            var pool = Executors.newFixedThreadPool(20);
            while (running) {
                pool.execute(new VotingCenter(listener.accept()));
            }
        }
    }

    private static class Agregator{
        private final Map<String,Map<String,Integer>> mediumResult;
        private Map<String,Integer> finalResult = new HashMap<>();
        private int partyNumber=0;

        Agregator(Map<String,Map<String,Integer>> result) {
            this.mediumResult=result;
            finalResult=agregate();
            partyNumber=result.size();
        }

        public void getResult() {
            StringBuilder result = new StringBuilder();
            result.append("Result:\n");
            for (String party : finalResult.keySet()) {
                result.append(party).append(": ").append(finalResult.get(party)).append("\n");
            }
            System.out.println(result);
        }

        public void getWinner() {
            int maximum=0;
            String winner="";
            for (String party : finalResult.keySet()) {
                if (finalResult.get(party)>maximum) {
                    maximum=finalResult.get(party);
                    winner =  party;
                }
            }

            System.out.println("Winner: "+winner+" with "+maximum+" votes");
        }

        public void getProbaResult() {
            StringBuilder result = new StringBuilder();
            result.append("Result:\n");
            for (String party : finalResult.keySet()) {
                result.append(party).append(": ").append(finalResult.get(party)/(100.0*partyNumber)).append("%\n");
            }
            System.out.println(result);
        }

        private Map<String,Integer> agregate(){
            for (Map<String,Integer> result : mediumResult.values()) {

                for (String party : result.keySet()) {
                    if(finalResult.containsKey(party)){
                        finalResult.put(party,finalResult.get(party)+result.get(party));
                    }
                    else {
                        finalResult.put(party,result.get(party));
                    }
                }
            }
            return finalResult;
        }
    }
    private static class VotingCenter implements Runnable {
        private Socket socket;

        VotingCenter(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //System.out.println("Connected: " + socket);
            try {
                var in = new Scanner(socket.getInputStream());

                String centerId = "unknown";
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.startsWith("Center:")) {
                        centerId = line.split(":")[1];
                        System.out.println("Receiving data from center " + centerId+"\n");
                        Map<String, Integer> centerResult = new ConcurrentHashMap<>();
                        finalVote.putIfAbsent(centerId, centerResult);
                    }
                    else if (line.equals("END")) {
                        break;
                    }
                    else {
                        if (finalVote.containsKey(centerId)) {
                            Map<String, Integer> centerResult = finalVote.get(centerId);
                            String[] part = line.split(":");
                            String party = part[0];
                            String vote = part[1];
                            int votesCount = Integer.parseInt(vote.split(" ")[0]);
                            centerResult.put(party,votesCount);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                //System.out.println("Closed: " + socket);
            }
        }
    }
}