package BusinessLayer;

class Sale {
    private int quantity;
    private final double basePrice;
    private final double discoundtPrecentage;
    Sale(int quantity, double basePrice, double discoundtPrecentage)
    {
        this.quantity=quantity;
        this.basePrice=basePrice;
        this.discoundtPrecentage=discoundtPrecentage;
    }

    int getQuantity() {
        return quantity;
    }

    double getBasePrice() {
        return basePrice;
    }

    double getDiscoundtPrecentage() {
        return discoundtPrecentage;
    }

    double getTotalPrice()
    {
        return basePrice*discoundtPrecentage/100;
    }
    double getTotalRevenue()
    {
        return getTotalPrice()*quantity;
    }
    void addQuantity(int quantity)
    {
        this.quantity+=quantity;
    }
}
