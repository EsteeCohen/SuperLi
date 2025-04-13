package src;

import src.DomainLayer.Agreement;
import src.DomainLayer.Product;
import src.DomainLayer.ProductController;
import src.DomainLayer.Enums.DaysOfTheWeek;
import src.DomainLayer.Enums.PaymentMethod;
import src.DomainLayer.Enums.PaymentTiming;
import src.DomainLayer.Enums.STATUS;
import src.ServiceLayer.OrderService;
import src.ServiceLayer.SupplierService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemInitializer {
    private SupplierService supplierService;
    private OrderService orderService;
    private ProductController productController;
    private boolean isInitialized = false;

    public SystemInitializer() {
        supplierService = SupplierService.getInstance();
        orderService = OrderService.getInstance();
        productController = ProductController.getInstance();
    }

    /**
     * Initialize the system with sample data
     * @return true if initialization was successful
     */
    public boolean initializeSystem() {
        if (isInitialized) {
            return false; // Already initialized
        }

        try {
            // Create suppliers
            createSampleSuppliers();

            // Create agreements
            createSampleAgreements();

            // Create products
            createSampleProducts();

            // Create orders
            createSampleOrders();

            isInitialized = true;
            return true;
        } catch (Exception e) {
            System.err.println("Error initializing system: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if the system has been initialized
     * @return true if system is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Create sample suppliers
     */
    private void createSampleSuppliers() {
        // Create a supplier that needs pickup
        supplierService.createSupplierNeedsPickup("Office Supplies Ltd", "123 Business St", 1001, 12345678);

        // Create a supplier with delivery
        List<DaysOfTheWeek> deliveryDays1 = new ArrayList<>();
        deliveryDays1.add(DaysOfTheWeek.MONDAY);
        deliveryDays1.add(DaysOfTheWeek.WEDNESDAY);
        deliveryDays1.add(DaysOfTheWeek.FRIDAY);
        supplierService.createSupplierWithDelivery("Fast Delivery Inc", 1002, 87654321, deliveryDays1);

        // Create another supplier with delivery
        List<DaysOfTheWeek> deliveryDays2 = new ArrayList<>();
        deliveryDays2.add(DaysOfTheWeek.TUESDAY);
        deliveryDays2.add(DaysOfTheWeek.THURSDAY);
        supplierService.createSupplierWithDelivery("Premium Products Co", 1003, 56781234, deliveryDays2);

        // Add contact persons
        supplierService.addContactPersonToSupplier(1001, "John Smith", 555123456);
        supplierService.addContactPersonToSupplier(1002, "Sara Johnson", 555789012);
        supplierService.addContactPersonToSupplier(1003, "Michael Brown", 555345678);
        supplierService.addContactPersonToSupplier(1003, "Jessica Lee", 555901234);
    }

    /**
     * Create sample agreements
     */
    private void createSampleAgreements() {
        // Create agreement for supplier 1001
        Agreement agreement1 = new Agreement(
                1001,
                PaymentMethod.CREDIT,
                PaymentTiming.IMMEDIATE,
                LocalDate.now().minusDays(30),
                LocalDate.now().plusDays(335)
        );
        supplierService.addAgreementToSupplier(1001, agreement1);

        // Create agreement for supplier 1002
        Agreement agreement2 = new Agreement(
                1002,
                PaymentMethod.BANK_TRANSFER,
                PaymentTiming.MONTHLY,
                LocalDate.now().minusDays(60),
                LocalDate.now().plusDays(305)
        );
        supplierService.addAgreementToSupplier(1002, agreement2);

        // Create agreement for supplier 1003
        Agreement agreement3 = new Agreement(
                1003,
                PaymentMethod.CHECK,
                PaymentTiming.END_OF_MONTH,
                LocalDate.now().minusDays(15),
                LocalDate.now().plusDays(350)
        );
        supplierService.addAgreementToSupplier(1003, agreement3);
    }

    /**
     * Create sample products
     */
    private void createSampleProducts() {
        // Create products for supplier 1001
        Product product1 = productController.createProduct(1001, 101, 1, 10.99);
        productController.addProductDiscount(1001, 101, 10, 0.05);
        productController.addProductDiscount(1001, 101, 50, 0.10);

        Product product2 = productController.createProduct(1001, 102, 5, 24.99);
        productController.addProductDiscount(1001, 102, 5, 0.07);
        productController.addProductDiscount(1001, 102, 20, 0.15);

        // Create products for supplier 1002
        Product product3 = productController.createProduct(1002, 201, 1, 15.50);
        productController.addProductDiscount(1002, 201, 20, 0.08);

        Product product4 = productController.createProduct(1002, 202, 10, 35.75);
        productController.addProductDiscount(1002, 202, 5, 0.05);
        productController.addProductDiscount(1002, 202, 15, 0.12);

        // Create products for supplier 1003
        Product product5 = productController.createProduct(1003, 301, 1, 45.99);
        productController.addProductDiscount(1003, 301, 3, 0.10);

        Product product6 = productController.createProduct(1003, 302, 2, 89.99);
        productController.addProductDiscount(1003, 302, 2, 0.05);
        productController.addProductDiscount(1003, 302, 5, 0.15);
    }

    /**
     * Create sample orders
     */
    private void createSampleOrders() {
        // Create order for supplier 1001
        Map<Integer, Integer> items1 = new HashMap<>();
        items1.put(101, 15); // 15 units of product 101
        items1.put(102, 7);  // 7 units of product 102
        orderService.createOrder(1001, LocalDate.now().plusDays(7), items1);

        // Create order for supplier 1002
        Map<Integer, Integer> items2 = new HashMap<>();
        items2.put(201, 30); // 30 units of product 201
        orderService.createOrder(1002, LocalDate.now().plusDays(3), items2);

        // Create another order for supplier 1002
        Map<Integer, Integer> items3 = new HashMap<>();
        items3.put(201, 5);  // 5 units of product 201
        items3.put(202, 8);  // 8 units of product 202
        orderService.createOrder(1002, LocalDate.now().plusDays(5), items3);

        // Create order for supplier 1003
        Map<Integer, Integer> items4 = new HashMap<>();
        items4.put(301, 4);  // 4 units of product 301
        items4.put(302, 3);  // 3 units of product 302
        orderService.createOrder(1003, LocalDate.now().plusDays(10), items4);
    }

    /**
     * Clear all system data
     */
    public void resetSystem() {
        // Clear all products
        productController.clearProducts();

        // Other controllers might need similar clear methods
        isInitialized = false;
    }
}