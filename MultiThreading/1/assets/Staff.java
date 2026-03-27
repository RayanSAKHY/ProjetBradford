package assets;
public class Staff {
    private Product product;

    public Staff(Product product) {
        this.product = product;
    }

    public Product getProduct(){
        return product;
    }

    public String toString(){
        String s = "";
        switch(product) {
            case Product.TEA:
                s ="tea";
                break;
            case Product.COFFEE:
                s = "coffee";
                break;
            case Product.CAKE:
                s = "cake";
                break;
            default:
                break;
        }
        //System.out.println("test : "+s);
        return s;
    }

    public synchronized void addToBuffet(Buffet buffet,int quantity) {
        switch(product) {
            case Product.TEA :
                buffet.addTea(quantity);
                break;
            case Product.COFFEE:
                buffet.addCoffee(quantity);
                break;
            case Product.CAKE:
                buffet.addCake(quantity);
                break;
            default:
                break;
        }
    }
}