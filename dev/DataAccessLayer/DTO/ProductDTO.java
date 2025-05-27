package DataAccessLayer.DTO;

import DomainLayer.Supplier.Enums.Units;
import DomainLayer.Supplier.Product;

public class ProductDTO {
    private String name;
    private String supplierId;
    private String catalogNumber;
    private int quantityPerPackage;
    private double price;
    private String unit;

    public ProductDTO(String name, String supplierId, String catalogNumber, int quantityPerPackage, double price, String unit) {
        this.name = name;
        this.supplierId = supplierId;
        this.catalogNumber = catalogNumber;
        this.quantityPerPackage = quantityPerPackage;
        this.price = price;
        this.unit = unit;
    }

    public Product toDomain() {
        return new Product(name, supplierId, catalogNumber, quantityPerPackage, price, Units.valueOf(unit));
    }

    public ProductDTO(Product p) {
        this.name = p.getProductName();
        this.supplierId = p.getSupplierId();
        this.catalogNumber = p.getCatalogNumber();
        this.quantityPerPackage = p.getQuantityPerPackage();
        this.price = p.getPrice();
        this.unit = p.getUnits().toString();
    }

    // Getters

    public String getName() {
        return name;
    }


    public String getSupplierId() {
        return supplierId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public int getQuantityPerPackage() {
        return quantityPerPackage;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }


}
