package assets;
public class Staff {
    private Product product;

    public Staff(Product product) {
        this.product = product;
    }

    public void addToBuffet(Buffet buffet,int quantity) {
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