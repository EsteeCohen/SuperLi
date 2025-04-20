package Tests.DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import src.ServiceLayer.SupplierSystemService;

import java.time.LocalDate;
import java.util.*;

class SupplierSystemTests {

    private SupplierSystemService service;

    @BeforeEach
    public void setUp() {
        // Get a new instance for each test
        service = SupplierSystemService.getInstance();
    }

    // Test 1: Add supplier with delivery days
    @Test
    public void testAddSupplierWithDelivery() {
        boolean result = service.addSupplierWithDelivery("Test Supplier", "SUP001", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        assertTrue(result);

        List<String> suppliers = service.getAllSuppliers();
        assertFalse(suppliers.isEmpty());
        boolean found = false;
        for (String supplier : suppliers) {
            if (supplier.contains("SUP001")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Supplier not found in the list");
    }

    // Test 2: Add supplier that needs pickup
    @Test
    public void testAddSupplierNeedsPickup() {
        boolean result = service.addSupplierNeedsPickup("Pickup Supplier", "SUP002", "987654321", "Test Address 123", List.of("Ofek,0548318083"));
        assertTrue(result);

        String suppliersRequiringPickup = service.getSuppliersRequiringPickup();
        assertTrue(suppliersRequiringPickup.contains("SUP002"), "Supplier that needs pickup not found");
    }

    // Test 3: Update supplier information
    @Test
    public void testUpdateSupplier() {
        // Add a supplier with initial delivery days
        service.addSupplierWithDelivery("Original Name", "SUP003", "123456789", "1,3,5", List.of("Ofek,0548318083"));

        // Update the name
        boolean nameUpdate = service.updateSupplier("SUP003", "name", "Updated Name");
        assertTrue(nameUpdate, "Failed to update supplier name");

        // Update delivery days
        boolean deliveryDaysUpdate = service.updateSupplier("SUP003", "deliveryDays", "2,4,6"); // Monday, Wednesday, Friday
        assertTrue(deliveryDaysUpdate, "Failed to update delivery days");

        // Verify supplier exists and name updated
        List<String> suppliers = service.getAllSuppliers();
        boolean found = false;
        for (String supplier : suppliers) {
            if (supplier.contains("SUP003") && supplier.contains("Updated Name")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Updated supplier not found");
    }

    // Test 4: Remove supplier
    @Test
    public void testRemoveSupplier() {
        // First, add a supplier
        service.addSupplierWithDelivery("To Remove", "SUP004", "123456789", "1,3,5", List.of("Ofek,0548318083"));

        // Now remove it
        boolean result = service.removeSupplier("SUP004");
        assertTrue(result);

        // Verify it's gone
        List<String> suppliers = service.getAllSuppliers();
        boolean found = false;
        for (String supplier : suppliers) {
            if (supplier.contains("SUP004")) {
                found = true;
                break;
            }
        }
        assertFalse(found, "Supplier should be removed but was found");
    }

    // Test 5: Add product with discounts
    @Test
    public void testAddProduct() {
        // First, add a supplier
        service.addSupplierWithDelivery("Product Supplier", "SUP005", "123456789", "1,3,5", List.of("Ofek,0548318083"));

        // Now add a product
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        boolean result = service.addProduct("p1", "SUP005", "PROD001", 10, discounts, 100.0, 0);
        assertTrue(result);

        // Verify the product was added
        List<String> products = service.getAllProducts();
        boolean found = false;
        for (String product : products) {
            if (product.contains("PROD001") && product.contains("SUP005")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Product not found in the list");
    }

    // Test 6: Update product information
    @Test
    public void testUpdateProduct() {
        // Add supplier and product first
        service.addSupplierWithDelivery("Product Update Supplier", "SUP006", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("p1", "SUP006", "PROD002", 10, discounts, 100.0, 0);

        // Update the product price
        boolean result = service.updateProduct("SUP006", "PROD002", "price", "150.0");
        assertTrue(result);

        // Verify the update
        String product = service.getProductBySupplierAndCatalog("SUP006", "PROD002");
        assertTrue(product.contains("Price per package: 150.00"), "Updated product price not found");
    }

    // Test 7: Remove product
    @Test
    public void testRemoveProduct() {
        service.addSupplierWithDelivery("Product Remove Supplier", "SUP007", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        boolean added = service.addProduct("p1", "SUP007", "PROD003", 10, discounts, 100.0, 0);
        assertTrue(added, "Product was not added successfully");

        String productBeforeRemoval = service.getProductBySupplierAndCatalog("SUP007", "PROD003");
        assertNotNull(productBeforeRemoval, "Product should exist before removal");

        boolean removed = service.removeProduct("SUP007", "PROD003");
        assertTrue(removed, "Product removal failed");

        String productAfterRemoval = service.getProductBySupplierAndCatalog("SUP007", "PROD003");
        assertNull(productAfterRemoval, "Product should be removed but was found");
    }


    // Test 8: Create agreement
    @Test
    public void testCreateAgreement() {
        // Add supplier and products first
        service.addSupplierWithDelivery("Agreement Supplier", "SUP008", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("p1", "SUP008", "PROD004", 10, discounts, 100.0, 0);
        service.addProduct("p1", "SUP008", "PROD005", 20, discounts, 200.0, 0);

        // Create an agreement
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        Set<Integer> productIndices = Set.of(0, 1); // assuming these are valid indices
        boolean result = service.createAgreement("SUP008", 0, 0, validFrom, validTo, productIndices);
        assertTrue(result);

        // Verify the agreement was created
        List<String> agreements = service.getAgreementsBySupplier("SUP008");
        assertFalse(agreements.isEmpty(), "No agreements found");
    }

    // Test 9: Update agreement
    @Test
    public void testUpdateAgreement() {
        // Step 1: Add supplier, products, and initial agreement
        service.addSupplierWithDelivery("Agreement Update Supplier", "SUP009", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("p1", "SUP009", "PROD006", 10, discounts, 100.0, 0);
        service.addProduct("p2", "SUP009", "PROD007", 20, discounts, 200.0, 0);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        Set<Integer> productIndices = Set.of(0, 1);
        service.createAgreement("SUP009", 0, 0, validFrom, validTo, productIndices);

        // Step 2: Update payment method
        boolean methodUpdated = service.updatePaymentMethods("SUP009", 0, 1);
        assertTrue(methodUpdated, "Failed to update payment method");

        // Step 3: Update payment timing
        boolean timingUpdated = service.updatePaymentTiming("SUP009", 0, 2);
        assertTrue(timingUpdated, "Failed to update payment timing");

        // Step 4: Update validFrom date
        LocalDate newValidFrom = validFrom.minusDays(3);
        boolean fromUpdated = service.updateValidFrom("SUP009", 0, newValidFrom);
        assertTrue(fromUpdated, "Failed to update validFrom");

        // Step 5: Update validTo date
        LocalDate newValidTo = validTo.plusMonths(3);
        boolean toUpdated = service.updateValidTo("SUP009", 0, newValidTo);
        assertTrue(toUpdated, "Failed to update validTo");

        // Step 6: Update products - keep only the first one
        List<Integer> newProducts = List.of(0);
        boolean productsUpdated = service.updateAgreementProducts("SUP009", 0, newProducts);
        assertTrue(productsUpdated, "Failed to update agreement products");

        // Step 7: Retrieve and verify the updated agreement
        List<String> agreements = service.getAgreementsBySupplier("SUP009");
        assertFalse(agreements.isEmpty(), "No agreements found");

        String agreement = agreements.get(0);
        assertTrue(agreement.contains(service.getPaymentMethods().get(1)), "Payment method not updated correctly");
        assertTrue(agreement.contains(service.getPaymentTimings().get(2)), "Payment timing not updated correctly");
        assertTrue(agreement.contains("Valid From: " + newValidFrom), "validFrom not updated");
        assertTrue(agreement.contains("Valid To: " + newValidTo), "validTo not updated");

        // Validate products were updated
        assertTrue(agreement.contains("PROD006"), "Expected product not found");
        assertFalse(agreement.contains("PROD007"), "Unexpected product still present after update");
    }


    // Test 10: Create order
    @Test
    public void testCreateOrder() {
        // Add supplier, products, and agreement first
        service.addSupplierWithDelivery("Order Supplier", "SUP010", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("p1","SUP010", "PROD008", 10, discounts, 100.0, 0);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        Set<Integer> productIndices = Set.of(0);
        service.createAgreement("SUP010", 0, 0, validFrom, validTo, productIndices);

        // Create an order
        LocalDate orderDate = LocalDate.now();
        LocalDate supplyDate = orderDate.plusDays(7);
        Map<Integer, Integer> items = new HashMap<>();
        items.put(0, 5); // 5 units of the first product in the agreement

        boolean result = service.insertOrder("SUP010", orderDate, supplyDate, items, "Contact Person", "123456789", 0, 1);
        assertTrue(result);

        // Verify the order was created
        List<String> orders = service.getAllOrders();
        assertFalse(orders.isEmpty(), "No orders found");
    }

    // Test 11: Update order status
    @Test
    public void testUpdateOrderStatus() {
        // First create a supplier, product, agreement, and order
        service.addSupplierWithDelivery("Status Update Supplier", "SUP011", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("p1", "SUP011", "PROD009", 10, discounts, 100.0, 0);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        Set<Integer> productIndices = Set.of(0);
        service.createAgreement("SUP011", 0, 0, validFrom, validTo, productIndices);

        LocalDate orderDate = LocalDate.now();
        LocalDate supplyDate = orderDate.plusDays(7);
        Map<Integer, Integer> items = new HashMap<>();
        items.put(0, 5);
        service.insertOrder("SUP011", orderDate, supplyDate, items, "Contact Person", "123456789", 0, 1);

        // Update order status - need to know the order ID
        // Assuming the first order has ID 0
        boolean result = service.updateOrderStatus(0, 2); // Change to status with index 2
        assertTrue(result);

        // Verify the status update
        List<String> orders = service.getOrdersByStatus(1);
        assertTrue(orders.isEmpty(), "There is products with status 1");

        orders = service.getOrdersByStatus(2);
        assertTrue(!orders.isEmpty(), "There is not products with status 1");
    }

    // Test 12: Order price calculation with discount
    @Test
    public void testOrderPriceCalculationWithDiscount() {
        // Step 1: Add a supplier and product with discount
        service.addSupplierWithDelivery("Discount Test Supplier", "SUP012", "123456789", "1,3,5", List.of("Test,0500000000"));

        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("5,10", "10,20")); // 10% for 5+, 20% for 10+
        service.addProduct("ProductA", "SUP012", "PROD010", 1, discounts, 100.0, 0); // base price = 100

        // Step 2: Create agreement
        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(10);
        Set<Integer> productIndices = Set.of(0);
        boolean agreementCreated = service.createAgreement("SUP012", 0, 0, from, to, productIndices);
        assertTrue(agreementCreated);

        // Step 3: Create order with quantity that matches discount
        Map<Integer, Integer> items = Map.of(0, 10); // quantity = 10 => 20% discount
        LocalDate orderDate = LocalDate.now();
        LocalDate supplyDate = orderDate.plusDays(3);

        boolean orderCreated = service.insertOrder("SUP012", orderDate, supplyDate, items,
                "Discount Tester", "0501234567", 0, 1);
        assertTrue(orderCreated);

        // Step 4: Retrieve the order and check total price
        List<String> orders = service.getAllOrders();
        assertFalse(orders.isEmpty());

        String orderDetails = orders.get(orders.size() - 1);
        assertTrue(orderDetails.contains("Total Price: 800.00")); // 10 * 100 * 0.8 = 800.00
    }

}