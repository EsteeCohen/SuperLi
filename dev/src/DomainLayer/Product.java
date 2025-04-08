package src.DomainLayer;


import src.DomainLayer.Enums.*;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String ProductName;
    private int supplierId;
    private int catalogNumber;
    private int quantityPerPackage;
    private Map<Integer, Double> discountPerPackage; // <amount, discount>
    private double price;   // price per package
    private Units units;

    public Product(int supplierId, int catalogNumber, int quantityPerPackage,
                   Map<Integer, Double> discountPerPackage, double price) {
        this.supplierId = supplierId;
        this.catalogNumber = catalogNumber;
        this.quantityPerPackage = quantityPerPackage;
        this.discountPerPackage = discountPerPackage;
        this.price = price;
        if(this.discountPerPackage == null)
            this.discountPerPackage = new HashMap<>();
    }

    // Getters and setters
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public int getQuantityPerPackage() {
        return quantityPerPackage;
    }

    public void setQuantityPerPackage(int quantityPerPackage) {
        this.quantityPerPackage = quantityPerPackage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscountPerPackage(Map<Integer, Double> discountPerPackage) {
        this.discountPerPackage = discountPerPackage;
    }
    public Map<Integer, Double> getDiscountPerPackage() {
        return discountPerPackage;
    }

    public void addDiscountPerPackage(int amount, double discount) {
        discountPerPackage.put(amount, discount);
    }

    /**
     * Calculate price with discount based on quantity
     * @param quantity the quantity ordered
     * @return the final price considering quantity discounts
     */
    public double calculatePriceWithDiscount(int quantity) {
        if (quantity <= 0) return price;

        double discount = 0.0;

        for (Map.Entry<Integer, Double> entry : discountPerPackage.entrySet()) {
            if (quantity >= entry.getKey()) {
                discount = Math.max(discount, entry.getValue());
            }
        }

        return price * quantity * (1 - discount);
    }


    @Override
    public String toString() {
        return "Product{" +
                "supplierId=" + supplierId +
                ", catalogNumber=" + catalogNumber +
                ", quantityPerPackage=" + quantityPerPackage +
                ", units=" + units +
                ", discountPerPackage=" +  discountPerPackage.toString() +
                ", price=" + price +
                '}';
    }
}
