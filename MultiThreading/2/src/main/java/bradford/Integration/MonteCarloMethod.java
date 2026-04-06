package bradford.Integration;


import java.util.List;
import java.util.Random;

public class MonteCarloMethod extends IntegrationMethod {

    public MonteCarloMethod(List<Double> fonc) {
        super(fonc);
    }

    @Override
    public double integrate(List<Double> range, int nbPoints) {
        Random random = new Random();
        double sum = 0;
        double a = range.get(0);
        double b = range.get(1);

        for (int i = 0; i< nbPoints; i++) {
            double rand = random.nextDouble();
            double x = rand*a + (1 - rand)*b;
            sum += f(x);
        }

        return (b-a)*sum/nbPoints;
    }
}
