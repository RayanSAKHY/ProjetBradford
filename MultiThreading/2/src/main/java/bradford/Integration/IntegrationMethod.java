package bradford.Integration;

import java.util.List;

public abstract class IntegrationMethod {
    private final List<Double> fonc;

    public IntegrationMethod(List<Double> fonc) {
        this.fonc = fonc;
    }

    public abstract double integrate(List<Double> range,int nbPoints);

    public double f(double x) {
        double result = 0;
        int numCoef = fonc.size();
        for (int i=numCoef-1;i>=0;i--) {
            result = result * x + fonc.get(i);
        }
        return result;
    }
}
