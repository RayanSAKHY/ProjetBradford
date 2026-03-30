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
        private List<Integer> favorabilityMeter = new ArrayList<>();
        private Random rand = new Random();
        int numParty;

        public VotingCenter(String centerId, List<String> parties) {
            this.centerId = centerId;
            this.parties = parties;
            for (String party : parties) {
                mediumVotes.put(party, 0);
            }
            numParty = parties.size();
            createFavorabilityMeter();
        }

        private void createFavorabilityMeter() {
            int sum =0;

            for (int i = 0; i < numParty-1; i++) {
                int bound = 100/(numParty);
                int favorability = rand.nextInt(bound)+ numParty;
                double factor = 0.5;
                while (favorability + sum > 100) {
                    if (bound > 5) {
                        bound = (int) (bound*factor);
                        favorability = rand.nextInt(bound) +numParty;
                    }
                    else {
                        favorability = 0;
                    }
                }
                sum += favorability;
                favorabilityMeter.add(sum);
            }
            favorabilityMeter.add(100);
            System.out.println("Favorability meter: " + favorabilityMeter);
        }

        public String vote() {

            StringBuilder result = new StringBuilder();

            for (int i = 1; i <= 10000; i++) {
                double rate = rand.nextDouble();
                for (int j = 0;j<numParty;j++) {
                    if (rate < favorabilityMeter.get(j)/100.0) {
                        String parti = parties.get(j);
                        mediumVotes.replace(parti, mediumVotes.get(parti) + 1);
                        break;
                    }
                }


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