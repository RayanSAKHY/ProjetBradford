package Utils;

import java.util.Random;

public class RandomNumberGen {
    Random gen;
    public RandomNumberGen(long seed) {
        gen = new Random(seed);
    }

    public int getRandomNumber() {
        return gen.nextInt();
    }

    public int getRandomNumber(int max) {
        return gen.nextInt(max);
    }

    public double getProba() {
        return gen.nextDouble();
    }
}