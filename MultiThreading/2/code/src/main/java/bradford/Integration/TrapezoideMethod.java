package bradford.Integration;

import java.util.List;

public class TrapezoideMethod extends IntegrationMethod {

    public TrapezoideMethod(List<Double> fonc) {
        super(fonc);
    }

    @Override
    public double integrate(List<Double> range, int nbPoints) {
        double a = range.get(0);
        double b = range.get(1);

        double h = (b-a)/nbPoints;
        double sum = 0;

        for (int i = 1; i <= nbPoints-1; i++) {
            sum += f(a+i*h);
        }

        return (h/2.0) * (f(a) + 2*sum + f(b));
    }
}
