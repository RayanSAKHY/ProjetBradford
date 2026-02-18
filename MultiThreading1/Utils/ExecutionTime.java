package Utils;

public class ExecutionTime {
    private double speed = 1;
    private RandomNumberGen gen;

    public ExecutionTime(RandomNumberGen gen, int speed) {
        this.gen = gen;
        this.speed = speed;
    }

    public void speedUp() {
        speed*=2;
    }

    public void speedDown() {
        speed/=2;
    }

    public double getSpeed() {
        return speed;
    }

    public double getExecutionTime() {
        return gen.getRandomNumber(15000)/speed;
    }
}