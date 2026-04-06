package bradford.Integration;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class IntegrationServer {
    private static final List<IntegrationResult> results = new ArrayList<>();

    public static void main(String[] args){
        try (var listener = new ServerSocket(59898)) {
            System.out.println("Listening on port 59898");

            List<Double> fonc;
            List<Double> range;
            int subDivision;
            if (args.length >0 && args[0].equals("test")) {
                fonc = new ArrayList<>() {
                    {
                        add(1.0);
                        add(2.0);
                        add(3.0);
                        add(4.0);
                    }
                };
                range = new ArrayList<>(2) {
                    {
                        add(1.0);
                        add(10.0);
                    }
                };
                subDivision = 1000;
            }
            else {
                InitIntegration init = new InitIntegration();
                fonc = init.initFonc();
                init.showFonc(fonc);
                range = init.initRange();
                subDivision = init.initSubDivision();
            }


            System.out.println("Waiting for client");

            final Object lock = new Object();
            boolean[] start = {false};
            IntegrationManager client1 = new IntegrationManager(listener.accept(),lock,start);
            IntegrationManager client2 = new IntegrationManager(listener.accept(),lock,start);

            client1.giveCommand(fonc,range,subDivision,"monteCarlo");
            client2.giveCommand(fonc,range,subDivision,"trapezoide");

            Thread thread1 = new Thread(client1);
            Thread thread2 = new Thread(client2);

            thread1.start();
            thread2.start();

            Thread.sleep(100);
            synchronized (lock) {
                start[0] = true;
                lock.notifyAll();
            }

            System.out.println("Client added succesfully");

            thread1.join();
            thread2.join();

            System.out.println("\nRanked by time");
            results.sort(Comparator.comparing(IntegrationResult::getDuration));
            System.out.println(results.get(0).toString());
        }
        catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    private static class IntegrationManager implements Runnable {
        private final Socket socket;
        private final Object lock;
        private final boolean[] start;

        public IntegrationManager(Socket socket,Object lock,boolean[] start) {
            this.socket = socket;
            this.lock = lock;
            this.start = start;
        }

        public void giveCommand(List<Double> fonc,List<Double> range,int subDisivsion,String method) throws Exception {
            var out = new PrintWriter(socket.getOutputStream(), true);

            StringBuilder message = new StringBuilder();
            int n = fonc.size();
            for (Double aDouble : fonc) {
                message.append(aDouble);
                message.append(":");
            }
            out.println(message.toString());
            message  = new StringBuilder();
            for (double aDouble : range) {
                message.append(aDouble);
                message.append(":");
            }
            out.println(message.toString());
            out.println(subDisivsion);
            out.println(method);
            out.println("END");
        }


        @Override
        public void run() {
            try {
                var out = new PrintWriter(socket.getOutputStream(),true);
                var in = new Scanner(socket.getInputStream());

                synchronized (lock) {
                    while (!start[0]) {
                        lock.wait();
                    }
                }
                out.println("START");

                System.out.println("Starting the calculi");
                String methodName = in.nextLine();
                String resultString = in.nextLine();
                String timeString = in.nextLine();
                double result;
                long duration;
                try {
                    result = Double.parseDouble(resultString);
                    duration = Long.parseLong(timeString);
                }
                catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                    return;
                }


                synchronized (results) {
                    results.add(new IntegrationResult(methodName,result,duration));
                }

                //System.out.println(methodName+":\nResult: "+resultString+"\nTime: "+timeString);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

