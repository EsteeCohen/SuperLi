package src.DomainLayer;

import src.DomainLayer.Enums.Units;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {
    private static ProductController instance;
    private Map<String, Map<Integer, Product>> products; //<supplier ID, <catalogNumber, product>>

    public static ProductController getInstance() {
        if (instance == null)
            instance = new ProductController();
        return instance;
    }

    private ProductController() {
        products = new HashMap<>();
    }

    /**
     * Create a new product
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @param quantityPerPackage Quantity per package
     * @param price Price per package
     * @return The created Product object
     */
    public Product createProduct(String supplierId, int catalogNumber, int quantityPerPackage, double price) {
        Product product = new Product(supplierId, catalogNumber, quantityPerPackage, new HashMap<>(), price);

        // Initialize supplier map if it doesn't exist
        if (!products.containsKey(supplierId)) {
            products.put(supplierId, new HashMap<>());
        }

        // Add product to the map
        products.get(supplierId).put(catalogNumber, product);

        return product;
    }

    /**
     * Get product by supplier ID and catalog number
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @return Product object if found, null otherwise
     */
    public Product getProduct(String supplierId, int catalogNumber) {
        if (products.containsKey(supplierId)) {
            Map<Integer, Product> supplierProducts = products.get(supplierId);
            return supplierProducts.get(catalogNumber);
        }
        return null;
    }

    /**
     * Get all products for a specific supplier
     * @param supplierId ID of the supplier
     * @return List of products for the supplier
     */
    public List<Product> getProductsBySupplier(String supplierId) {
        List<Product> supplierProducts = new ArrayList<>();

        if (products.containsKey(supplierId)) {
            supplierProducts.addAll(products.get(supplierId).values());
        }

        return supplierProducts;
    }

    /**
     * Update product price
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @param newPrice New price for the product
     * @return true if update successful, false if product not found
     */
    public boolean updateProductPrice(String supplierId, int catalogNumber, double newPrice) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product != null) {
            product.setPrice(newPrice);
            return true;
        }
        return false;
    }

    /**
     * Add or update discount for a product
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @param amount Amount threshold for discount
     * @param discount Discount percentage (0.0 to 1.0)
     * @return true if discount added successfully, false if product not found
     */
    public boolean addProductDiscount(String supplierId, int catalogNumber, int amount, double discount) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product != null) {
            product.addDiscountPerPackage(amount, discount);
            return true;
        }
        return false;
    }

    /**
     * Update quantity per package for a product
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @param newQuantity New quantity per package
     * @return true if update successful, false if product not found
     */
    public boolean updateQuantityPerPackage(String supplierId, int catalogNumber, int newQuantity) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product != null) {
            product.setQuantityPerPackage(newQuantity);
            return true;
        }
        return false;
    }

    /**
     * Calculate final price for a product considering discounts
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @param quantity Quantity to calculate price for
     * @return Calculated price or -1 if product not found
     */
    public double calculateProductPrice(String supplierId, int catalogNumber, int quantity) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product != null) {
            return product.calculatePriceWithDiscount(quantity);
        }
        return -1;
    }

    /**
     * Remove a product
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product to remove
     * @return true if removal successful, false if product not found
     */
    public boolean removeProduct(int supplierId, int catalogNumber) {
        if (products.containsKey(supplierId)) {
            Map<Integer, Product> supplierProducts = products.get(supplierId);
            if (supplierProducts.containsKey(catalogNumber)) {
                supplierProducts.remove(catalogNumber);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all products
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();

        for (Map<Integer, Product> supplierProducts : products.values()) {
            allProducts.addAll(supplierProducts.values());
        }

        return allProducts;
    }

    /**
     * Check if a product exists
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @return true if product exists, false otherwise
     */
    public boolean productExists(int supplierId, int catalogNumber) {
        return products.containsKey(supplierId) &&
                products.get(supplierId).containsKey(catalogNumber);
    }

    /**
     * Clear all products (useful for testing)
     */
    public void clearProducts() {
        products.clear();
    }
}