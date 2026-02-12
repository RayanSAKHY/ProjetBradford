namespace assets;

public class Staff {
    private Product product;

    public Staff(Product product) {
        this.product = product;
    }

    public synchronized addToBuffet(Buffet buffet,Product product,int quantity) {
        switch(product) {
            case Product.TEA :
                buffet.addTea(quantiity);
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