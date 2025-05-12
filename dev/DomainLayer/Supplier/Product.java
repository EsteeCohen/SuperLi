package DomainLayer.Supplier;

import DomainLayer.Supplier.Enums.*;

import java.time.LocalDate;  // נוסיף את זה
import java.util.HashMap;
import java.util.Map;

public class Product {
    private String ProductName;
    private String supplierId;
    private String catalogNumber;
    private int quantityPerPackage;
    private double price;   // price per package
    private Units units;
    private LocalDate expirationDate;

    // קונסטרקטור מעודכן
    public Product(String name, String supplierId, String catalogNumber, int quantityPerPackage, double price, Units units, LocalDate expirationDate) {
        this.ProductName = name;
        this.supplierId = supplierId;
        this.catalogNumber = catalogNumber;
        this.quantityPerPackage = quantityPerPackage;
        this.price = price;
        this.units = units;
        this.expirationDate = expirationDate;
    }

    public Product(String name, String supplierId, String catalogNumber, int quantityPerPackage, double price, Units units) {
        this(name, supplierId, catalogNumber, quantityPerPackage, price, units, null);
    }

    // Getters and Setters
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getProductName() {
        return ProductName;
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

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return String.format(
                "Product:\n  Name: %s\n  Catalog Number: %s\n  Supplier ID: %s\n  Quantity Per Package: %d %s\n  Price per package: %.2f\n  Expiration Date: %s",
                ProductName != null ? ProductName : "N/A",
                catalogNumber,
                supplierId,
                quantityPerPackage,
                units,
                price,
                expirationDate != null ? expirationDate.toString() : "N/A"
        );
    }
}
