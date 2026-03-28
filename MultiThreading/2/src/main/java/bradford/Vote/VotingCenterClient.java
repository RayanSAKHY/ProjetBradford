package bradford.Vote;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class VotingCenterClient{

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Pass the server IP and the center number in separate arguments.");
            return;
        }
        try (var socket = new Socket(args[0], 59898)) {
            var out = new PrintWriter(socket.getOutputStream(), true);

            List<String> parties = new ArrayList<>() {
                {
                    add("A");
                    add("B");
                    add("C");
                    add("D");
                    add("E");
                }
            };
            VotingCenter center = new VotingCenter(args[1],parties);
            String result = center.vote();
            out.println(result);
            out.println("END");

            System.out.println(result);
            System.out.println("Results sent!");
        }
    }

    public static class VotingCenter{
        private String centerId;
        private List<String> parties;
        private Map<String, Integer> mediumVotes = new HashMap<>();

        public VotingCenter(String centerId, List<String> parties) {
            this.centerId = centerId;
            this.parties = parties;
            for (String party : parties) {
                mediumVotes.put(party, 0);
            }
        }

        public String vote() {

            Random rand = new Random();

            StringBuilder result = new StringBuilder();
            int nbParties = parties.size();

            for (int i = 1; i <= 10000; i++) {
                int choice = rand.nextInt(nbParties);
                String parti = parties.get(choice);
                mediumVotes.replace(parti, mediumVotes.get(parti) + 1);
            }

            result.append("Center:").append(centerId);
            for (String party : parties) {
                result.append("\n");
                result.append(party).append(":").append(mediumVotes.get(party)).append(" votes");
            }
            return result.toString();
        }
    }
}