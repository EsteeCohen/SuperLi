package DomainLayer;

//class mainly used to hold sales data to allow creating a report
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

    /**
     * calculates the actual price per product that were paid
     * @return hw much did the customer pay per product
     */
    double getTotalPrice()
    {
        return basePrice*(1-discoundtPrecentage/100);
    }

    /**
     * caculates the total revenue from this sale
     * @return totalprice*quantity
     */
    double getTotalRevenue()
    {
        return getTotalPrice()*quantity;
    }
    void addQuantity(int quantity)
    {
        this.quantity+=quantity;
    }
}
