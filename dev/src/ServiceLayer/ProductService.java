package src.ServiceLayer;

import src.DomainLayer.*;
import src.DomainLayer.Enums.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductService {
    private static ProductService instance = null;
    private List<Product> products;

    private ProductService() {
        this.products = new ArrayList<>();
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProduct(String supplierId, int catalogNumber) {
        for (Product product : products) {
            if (product.getSupplierId() == supplierId && product.getCatalogNumber() == catalogNumber) {
                return product;
            }
        }
        return null;
    }

    public boolean createProduct(String supplierId, int catalogNumber, int quantityPerPackage, double price) {
        // Check if product already exists
        if (getProduct(supplierId, catalogNumber) != null) {
            return false;
        }

        Map<Integer, Double> discounts = new HashMap<>();
        Product newProduct = new Product(supplierId, catalogNumber, quantityPerPackage, discounts, price);
        products.add(newProduct);
        return true;
    }

    public boolean createProduct(int supplierId, int catalogNumber, int quantityPerPackage,
                                 Map<Integer, Double> discountPerPackage, double price) {
        // Check if product already exists
        if (getProduct(supplierId, catalogNumber) != null) {
            return false;
        }

        Product newProduct = new Product(supplierId, catalogNumber, quantityPerPackage, discountPerPackage, price);
        products.add(newProduct);
        return true;
    }

    public boolean updateProductPrice(int supplierId, int catalogNumber, double price) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product == null) {
            return false;
        }

        product.setPrice(price);
        return true;
    }

    public boolean updateQuantityPerPackage(int supplierId, int catalogNumber, int quantityPerPackage) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product == null) {
            return false;
        }

        product.setQuantityPerPackage(quantityPerPackage);
        return true;
    }

    public boolean addDiscountPerPackage(int supplierId, int catalogNumber, int amount, double discount) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product == null) {
            return false;
        }

        product.addDiscountPerPackage(amount, discount);
        return true;
    }

    public boolean removeProduct(int supplierId, int catalogNumber) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product == null) {
            return false;
        }

        products.remove(product);
        return true;
    }

    public Product findProductByCatalogNumber(int catalogNumber) {
        for (Product product : products) {
            if (product.getCatalogNumber() == catalogNumber) {
                return product;
            }
        }
        return null;
    }
}