package DomainLayer.Inventory;

//class mainly used to hold sales data to allow creating a report
class Sale {
    private int quantity;
    private final double basePrice;
    private final double discountPercentage;
    Sale(int quantity, double basePrice, double discountPercentage)
    {
        this.quantity=quantity;
        this.basePrice=basePrice;
        this.discountPercentage = discountPercentage;
    }

    int getQuantity() {
        return quantity;
    }

    double getBasePrice() {
        return basePrice;
    }

    double getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * calculates the actual price per product that were paid
     * @return hw much did the customer pay per product
     */
    double getTotalPrice()
    {
        return basePrice*(1- discountPercentage /100);
    }

    /**
     * calculates the total revenue from this sale
     * @return totalPrice*quantity
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
