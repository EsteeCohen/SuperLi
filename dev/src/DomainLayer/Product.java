package src.DomainLayer;


import src.DomainLayer.Enums.*;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String ProductName;
    private String supplierId;
    private String catalogNumber;
    private int quantityPerPackage;
    //private Map<Integer, Double> discountPerPackage; // <amount, discount>
    private double price;   // price per package
    private Units units;

    public Product(String name, String supplierId, String catalogNumber, int quantityPerPackage, double price, Units units) {
        this.ProductName = name;
        this.supplierId = supplierId;
        this.catalogNumber = catalogNumber;
        this.quantityPerPackage = quantityPerPackage;
        //this.discountPerPackage = discountPerPackage;
        this.price = price;
        //if(this.discountPerPackage == null)
            //this.discountPerPackage = new HashMap<>();
        this.units = units;
    }

    // Getters and setters
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
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


    public void setProductName(String value) {
        this.ProductName = value;
    }

    public void setUnits(Units unit) {
        this.units = unit;
    }

    @Override
    public String toString() {
        return String.format(
                "Product:\n  Name: %s\n  Catalog Number: %s\n  Supplier ID: %s\n  Quantity Per Package: %d %s\n  Price per package: %.2f,",
                ProductName != null ? ProductName : "N/A",
                catalogNumber,
                supplierId,
                quantityPerPackage,
                units,
                price
                //discountPerPackage.isEmpty() ? "None" : discountPerPackage.toString()
        );
    }


}
