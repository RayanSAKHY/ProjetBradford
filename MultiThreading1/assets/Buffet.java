namespace assets;

public class Buffet {
    private int tea = 0;
    private int coffee = 0;
    private int cake = 0;

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

    public void setTea(int tea) {
        this.tea = tea;
    }

    public void setCoffee(int coffee) {
        this.coffee = coffee;
    }

    public void setCake(int cake) {
        this.cake = cake;
    }
}