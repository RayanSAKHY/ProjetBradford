package assets;

import java.util.concurrent.locks.ReentrantLock;

public class Buffet {
    private int tea = 0;
    private int coffee = 0;
    private int cake = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public Buffet(int tea, int coffee, int cake) {
        this.tea = tea;
        this.coffee = coffee;
        this.cake = cake;
    }

    public synchronized boolean isAvailable(int nbTea,int nbCoffee,int nbCake) {
        return cake > nbCake || coffee > nbCoffee || tea > nbTea;
    }

    public boolean tryUse() {
        return lock.tryLock();
    }

    public void release() {
        lock.unlock();
    }

    public synchronized boolean takeProduct(Product p,int quantity) {
        switch(p) {
            case CAKE:
                if(cake > quantity) { cake-=quantity; return true; } else return false;
            case COFFEE:
                if(coffee > quantity) { coffee-=quantity; return true; } else return false;
            case TEA:
                if(tea > quantity) { tea-=quantity; return true; } else return false;
            default:
                return false;
        }
    }

    public synchronized int getTea() {
        return tea;
    }

    public synchronized void addTea(int nbTea) {
        tea += nbTea;
    }

    public synchronized int getCoffee() {
        return coffee;
    }

    public synchronized void addCoffee(int nbCoffee) {
        coffee += nbCoffee;
    }

    public synchronized int getCake() {
        return cake;
    }

    public synchronized void addCake(int nbCake) {
        cake += nbCake;
    }
}