package src.ServiceLayer;

import src.DomainLayer.Enums.*;

public class SupplierSystem {
    private static SupplierSystem instance;
    private SupplierService supplierService;
    private OrderService orderService;
    private ProductService productService;

    private SupplierSystem() {
        this.supplierService = SupplierService.getInstance();
        this.orderService = OrderService.getInstance();
        this.productService = ProductService.getInstance();

    }

    public static synchronized SupplierSystem getInstance() {
        if (instance == null) {
            instance = new SupplierSystem();
        }
        return instance;
    }

    public boolean addSupplierWithDelivery(String name,String id,String bankAccount, String deliveryDays) {
        return supplierService.addSupplierWithDelivery(name, id, bankAccount, deliveryDays);

    }

    public boolean addSupplierNeedsPickup(String name, String id, String bankAccount, String address) {
        return supplierService.addSupplierNeedsPickup(name, id, bankAccount, address);
    }


    public void updateSupplier(Supplier supplier) {
        supplierService.updateSupplier(supplier.getSupplierId(), supplier);
    }

    public Supplier findSupplier(String id) {
        try {
            int supplierId = Integer.parseInt(id);
            return supplierService.getSupplier(supplierId);
        } catch (NumberFormatException e) {
            return null; // Invalid ID format
        }
    }

    public boolean removeSupplier(String id) {
        try {
            int supplierId = Integer.parseInt(id);
            return supplierService.removeSupplier(supplierId);
        } catch (NumberFormatException e) {
            return false; // Invalid ID format
        }
    }

    /*public void initialize() {
        // Initialize the system with sample data
        SystemInitializer initializer = new SystemInitializer();
        initializer.initializeSystem();
    }*/

    public boolean addProduct(String supplierId, int catalogNumber, int quantityPerPackage,String discount, double price) {
        // Create product in the system
        return productService.createProduct(
                supplierId,
                catalogNumber,
                quantityPerPackage,
                discount,
                price
        );
    }





    // Methods for supplier management
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }


    // Methods for product management
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    public boolean addProductWithDiscounts(int supplierId, int catalogNumber, int quantityPerPackage,
                                           Map<Integer, Double> discounts, double price) {
        // Check if supplier exists
        if (supplierService.getSupplier(supplierId) == null) {
            return false;
        }

        // Create product in the system
        return productService.createProduct(
                supplierId,
                catalogNumber,
                quantityPerPackage,
                discounts,
                price
        );
    }

    public boolean updateProductPrice(int supplierId, int catalogNumber, double price) {
        return productService.updateProductPrice(supplierId, catalogNumber, price);
    }

    public boolean updateProductQuantity(int supplierId, int catalogNumber, int quantityPerPackage) {
        return productService.updateQuantityPerPackage(supplierId, catalogNumber, quantityPerPackage);
    }

    public boolean addDiscountToProduct(int supplierId, int catalogNumber, int amount, double discount) {
        return productService.addDiscountPerPackage(supplierId, catalogNumber, amount, discount);
    }

    public Product findProduct(String id) {
        try {
            int productId = Integer.parseInt(id);
            return productService.findProductByCatalogNumber(productId);
        } catch (NumberFormatException e) {
            return null; // Invalid ID format
        }
    }

    public boolean removeProduct(String id) {
        try {
            int productId = Integer.parseInt(id);
            Product product = productService.findProductByCatalogNumber(productId);
            if (product != null) {
                return productService.removeProduct(product.getSupplierId(), productId);
            }
            return false;
        } catch (NumberFormatException e) {
            return false; // Invalid ID format
        }
    }

    // Data persistence methods
    public void saveData(String suppliersFile, String productsFile) throws IOException {
        try (ObjectOutputStream suppliersOut = new ObjectOutputStream(new FileOutputStream(suppliersFile));
             ObjectOutputStream productsOut = new ObjectOutputStream(new FileOutputStream(productsFile))) {

            // Save suppliers
            suppliersOut.writeObject(getAllSuppliers());

            // Save products
            productsOut.writeObject(getAllProducts());
        }
    }

    public void loadData(String suppliersFile, String productsFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream suppliersIn = new ObjectInputStream(new FileInputStream(suppliersFile));
             ObjectInputStream productsIn = new ObjectInputStream(new FileInputStream(productsFile))) {

            // Load suppliers
            List<Supplier> suppliers = (List<Supplier>) suppliersIn.readObject();
            for (Supplier supplier : suppliers) {
                // Since we don't have direct access to the supplier type, we need to handle differently
                if (supplier instanceof DeliverySupplier) {
                    DeliverySupplier deliverySupplier = (DeliverySupplier) supplier;
                    addSupplierWithDelivery(
                            deliverySupplier.getName(),
                            String.valueOf(deliverySupplier.getSupplierId()),
                            deliverySupplier.getBankAccount(),
                            deliverySupplier.getDeliveryDays()
                    );
                } else if (supplier instanceof PickupSupplier) {
                    PickupSupplier pickupSupplier = (PickupSupplier) supplier;
                    addSupplierNeedsPickup(
                            pickupSupplier.getName(),
                            String.valueOf(pickupSupplier.getSupplierId()),
                            pickupSupplier.getBankAccount(),
                            pickupSupplier.getAddress()
                    );
                }
            }

            // Load products
            List<Product> products = (List<Product>) productsIn.readObject();
            for (Product product : products) {
                addProductWithDiscounts(
                        product.getSupplierId(),
                        product.getCatalogNumber(),
                        product.getQuantityPerPackage(),
                        product.getDiscountPerPackage(),
                        product.getPrice()
                );
            }
        }
