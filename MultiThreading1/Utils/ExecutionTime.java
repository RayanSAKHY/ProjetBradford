package Utils;

public class ExecutionTime {
    private int speed = 1;
    private RandomNumberGen gen;

    public ExecutionTime(RandomNumberGen gen, int speed) {
        this.gen = gen;
        this.speed = speed;
    }

    public void speedUp() {
        speed++;
    }

    public void speedDown() {
        speed--;
    }

    public int getSpeed() {
        return speed;
    }

    public double getExecutionTime() {
        return gen.getRandomNumber(5000)/speed;
    }
}