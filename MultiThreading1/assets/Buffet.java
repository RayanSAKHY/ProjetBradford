package assets;
public class Buffet {
    private int tea = 0;
    private int coffee = 0;
    private int cake = 0;

    public Buffet(int tea, int coffee, int cake) {
        this.tea = tea;
        this.coffee = coffee;
        this.cake = cake;
    }

    public int getTea() {
        return tea;
    }

    public void addTea(int nbTea) {
        tea += nbTea;
    }

    public int getCoffee() {
        return coffee;
    }

    public void addCoffee(int nbCoffee) {
        coffee += nbCoffee;
    }

    public int getCake() {
        return cake;
    }

    public void addCake(int nbCake) {
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