package bradford.Integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InitIntegration {
    private List<Double> fonc = new ArrayList<>();
    private int subDivision;
    private double[] range = new double[2];
    private Scanner scanner = new Scanner(System.in);

    public List<Double> initFonc() {
        System.out.println("THE FUNCTION:");

        System.out.println("Choose the value of each coefficient starting from the unit one and type end to finish your function:");
        String input = scanner.nextLine();
        double coef = 0.0;
        while (!input.equals("end")) {
            try {
                coef = Double.parseDouble(input);

                fonc.add(coef);

                input = scanner.nextLine();
            }
            catch (NumberFormatException e) {
                if (!input.equals("end")) {
                    System.out.println("Invalid input. Please try again.");
                    input = scanner.nextLine();
                }
            }
        }
        return fonc;
    }

    public double[] initRange() {
        System.out.println("THE RANGE:");
        System.out.println("Choose the range of the function:");
        String input;
        int n = 0;

        while (n < 2){
            if (n == 0) System.out.print("a=");
            else if (n == 1) System.out.print("b=");
            input = scanner.nextLine();
            try {

                range[n] = Double.parseDouble(input);
                n++;
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        return range;
    }

    public int initSubDivision() {
        System.out.println("THE SUBDIVISION:");
        System.out.println("Choose the number of sub division that will be used in this integration:");

        Boolean finish = false;
        String input = scanner.nextLine();

        while (!finish) {
            try {
                subDivision = Integer.parseInt(input);
                finish = true;
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }

        return subDivision;
    }
    public void showFonc(List<Double> func){
        StringBuilder function = new StringBuilder();
        function.append("f(x) = ");
        int power = 0;

        for (double coef : func) {
            if (power == 0) {
                function.append(coef);
            }
            else {
                if (coef !=0) {
                    if (coef >0) {
                        function.append(" +");
                    }
                    else if (coef < 0) {
                        function.append(" ");
                    }

                    function.append(coef).append("*x");
                    if (power != 1) {
                        function.append("^").append(power);
                    }
                }

            }

            power++;

        }
        function.append("\n");
        System.out.println(function.toString());
    }
}
