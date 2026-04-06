package bradford.Integration;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IntegrationClient {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Pass the server IP");
            return;
        }
        try (var socket = new Socket(args[0],59898)) {
            var out = new PrintWriter(socket.getOutputStream(), true);

            var in = new Scanner(socket.getInputStream());

            IntegrationDoer doer = new IntegrationDoer(in,out);

            doer.integrate();
        }
    }

    private static class IntegrationDoer{
        private final Scanner in;
        private final PrintWriter out;

        public IntegrationDoer(Scanner in,PrintWriter out) {
            this.in = in;
            this.out = out;
        }

        public String integrate() {
            StringBuilder output = new StringBuilder();
            String integrationMethod = "unknown";
            try {
                String line;
                String foncString = in.nextLine();
                String rangeString = in.nextLine();
                int subDivision = Integer.parseInt(in.nextLine());
                int nbPoint;
                integrationMethod = in.nextLine();


                if (!in.nextLine().equals("END")) {
                    throw new IOException("Wrong message format");
                }

                List<Double> fonc = new ArrayList<Double>();
                List<Double> range = new ArrayList<>(2);

                for (String f : foncString.split(":")) {
                    fonc.add(Double.parseDouble(f));
                }

                for (String r : rangeString.split(":")) {
                    range.add(Double.parseDouble(r));
                }


                IntegrationMethod integration = null;
                switch (integrationMethod) {
                    case "monteCarlo":
                        integration = new MonteCarloMethod(fonc);
                        nbPoint = subDivision;
                        break;
                    case "trapezoide":
                        integration = new TrapezoideMethod(fonc);
                        nbPoint = subDivision+1;
                        break;
                    default:
                        throw new IOException("Method unknown");
                }

                do {
                    line = in.nextLine();
                } while (!line.equals("START"));
                Instant startTime = Instant.now();

                double result = integration.integrate(range, nbPoint);

                Instant endTime = Instant.now();
                Duration duration = Duration.between(startTime, endTime);

                out.println(integrationMethod);
                out.println(result);
                out.println(duration.toNanos());

                System.out.println(integrationMethod+":\nResult: "+result+"\nTime: "+duration.toNanos());


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return "";
        }

    }
}
