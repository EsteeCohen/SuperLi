package Tests.DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import src.DomainLayer.*;
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

    // Part 3: Update supplier information
    @Test
    public void testUpdateSupplierName() {
        SystemController controller = SystemController.getInstance();

        // Add a supplier
        String supplierId = "1234";
        controller.addSupplierWithDelivery("SupplierA", supplierId, "BankA", "1,2,3", List.of("John,123456789"));

        // Update name field
        boolean result = controller.updateSupplierField(supplierId, "name", "UpdatedSupplierA");

        // Verify
        assertTrue(result);
        String updatedSupplier = controller.getSupplierById(supplierId);
        assertNotNull(updatedSupplier);
        assertTrue(updatedSupplier.contains("UpdatedSupplierA"));
    }

    @Test
    public void testUpdateSupplierID() {
        SystemController controller = SystemController.getInstance();

        // Add a supplier
        String oldSupplierId = "5678";
        controller.addSupplierWithDelivery("SupplierB", oldSupplierId, "BankB", "4,5", List.of("Jane,987654321"));

        // Update supplierID
        String newSupplierId = "91011";
        boolean result = controller.updateSupplierField(oldSupplierId, "supplierid", newSupplierId);

        // Verify
        assertTrue(result);
        assertNull(controller.getSupplierById(oldSupplierId));
        String updatedSupplier = controller.getSupplierById(newSupplierId);
        assertNotNull(updatedSupplier);
        assertTrue(updatedSupplier.contains("SupplierB"));
    }

    @Test
    public void testUpdateSupplierBankAccount() {
        SystemController controller = SystemController.getInstance();

        // Add a supplier
        String supplierId = "1122";
        controller.addSupplierWithDelivery("SupplierC", supplierId, "BankC", "2,3,4", List.of("Alice,123123123"));

        // Update bank account field
        boolean result = controller.updateSupplierField(supplierId, "bankaccount", "UpdatedBankC");

        // Verify
        assertTrue(result);
        String updatedSupplier = controller.getSupplierById(supplierId);
        assertNotNull(updatedSupplier);
        assertTrue(updatedSupplier.contains("UpdatedBankC"));
    }

    @Test
    public void testUpdateSupplierContactPersons() {
        SystemController controller = SystemController.getInstance();

        // Add a supplier
        String supplierId = "3344";
        controller.addSupplierWithDelivery("SupplierD", supplierId, "BankD", "5,6,7", List.of("Bob,456789123"));

        // Add new contact person
        boolean result = controller.updateSupplierField(supplierId, "contactpersons", "NewName,111222333");

        // Verify
        assertTrue(result);
        String updatedSupplier = controller.getSupplierById(supplierId);
        assertNotNull(updatedSupplier);
        assertTrue(updatedSupplier.contains("NewName"));
        assertTrue(updatedSupplier.contains("111222333"));
    }

    @Test
    public void testUpdateSupplierDeliveryDays() {
        SystemController controller = SystemController.getInstance();

        // Add a supplier with delivery days
        String supplierId = "4455";
        controller.addSupplierWithDelivery("SupplierE", supplierId, "BankE", "1,2,3", List.of("Tim,321654987"));

        // Update delivery days
        boolean result = controller.updateSupplierField(supplierId, "deliverydays", "4,5,6");

        // Verify
        assertTrue(result);
        String updatedSupplier = controller.getSupplierById(supplierId);
        assertNotNull(updatedSupplier);
        assertTrue(updatedSupplier.contains("4"));
        assertTrue(updatedSupplier.contains("5"));
        assertTrue(updatedSupplier.contains("6"));
    }

    @Test
    public void testUpdateInvalidField() {
        SystemController controller = SystemController.getInstance();

        // Add a supplier
        String supplierId = "5566";
        controller.addSupplierWithDelivery("SupplierF", supplierId, "BankF", "2,3,4", List.of("Sara,654987321"));

        // Attempt to update an invalid field
        boolean result = controller.updateSupplierField(supplierId, "invalidField", "invalidValue");

        // Verify
        assertFalse(result);
        String supplier = controller.getSupplierById(supplierId);
        assertNotNull(supplier);
        assertFalse(supplier.contains("invalidValue"));
    }

    @Test
    public void testUpdateNonExistentSupplier() {
        SystemController controller = SystemController.getInstance();

        // Attempt to update a supplier that does not exist
        boolean result = controller.updateSupplierField("9999", "name", "NonExistentSupplier");

        // Verify
        assertFalse(result);
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
        //ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        boolean result = service.addProduct("p1", "SUP005", "PROD001", 10, 100.0, 2);
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
        service.addProduct("p1", "SUP006", "PROD002", 10, 100.0, 2);

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
        //ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        boolean added = service.addProduct("p1", "SUP007", "PROD003", 10, 100.0, 1);
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
        service.addProduct("p1", "SUP008", "PROD004", 10, 100.0, 1);
        service.addProduct("p1", "SUP008", "PROD005", 20, 200.0, 1);

        // Create an agreement
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);

        // Create discounts map for products - Map<ProductIndex, Map<Quantity, DiscountPercent>>
        Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();

        // For first product (index 0)
        Map<Integer, Integer> product1Discounts = new HashMap<>();
        product1Discounts.put(5, 10);  // 10% discount when buying 5+ units
        product1Discounts.put(10, 20); // 20% discount when buying 10+ units
        productDiscounts.put(0, product1Discounts);

        // For second product (index 1)
        Map<Integer, Integer> product2Discounts = new HashMap<>();
        product2Discounts.put(3, 5);   // 5% discount when buying 3+ units
        product2Discounts.put(7, 15);  // 15% discount when buying 7+ units
        productDiscounts.put(1, product2Discounts);

        boolean result = service.createAgreement("SUP008", 0, 0, validFrom, validTo, productDiscounts);
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
        service.addProduct("Product X", "SUP009", "PROD006", 10, 100.0, 1);
        service.addProduct("Product Y", "SUP009", "PROD007", 20, 200.0, 1);

        // Create initial discounts
        Map<Integer, Map<Integer, Integer>> initialDiscounts = new HashMap<>();
        Map<Integer, Integer> product1Discounts = new HashMap<>();
        product1Discounts.put(5, 10);
        initialDiscounts.put(0, product1Discounts);

        Map<Integer, Integer> product2Discounts = new HashMap<>();
        product2Discounts.put(3, 5);
        initialDiscounts.put(1, product2Discounts);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        service.createAgreement("SUP009", 0, 0, validFrom, validTo, initialDiscounts);

        // Step 2: Update payment method
        boolean methodUpdated = service.updatePaymentMethods("SUP009", 0, 1);
        assertTrue(methodUpdated, "Failed to update payment method");

        // Step 3: Update payment timing
        boolean timingUpdated = service.updatePaymentTiming("SUP009", 0, 1);
        assertTrue(timingUpdated, "Failed to update payment timing");

        // Step 4: Update validFrom date
        LocalDate newValidFrom = validFrom.minusDays(3);
        boolean fromUpdated = service.updateValidFrom("SUP009", 0, newValidFrom);
        assertTrue(fromUpdated, "Failed to update validFrom");

        // Step 5: Update validTo date
        LocalDate newValidTo = validTo.plusMonths(3);
        boolean toUpdated = service.updateValidTo("SUP009", 0, newValidTo);
        assertTrue(toUpdated, "Failed to update validTo");

        // Step 6: Update products - keep only the first one with new discounts
        Map<Integer, Map<Integer, Integer>> updatedDiscounts = new HashMap<>();
        Map<Integer, Integer> updatedProduct1Discounts = new HashMap<>();
        updatedProduct1Discounts.put(3, 15);  // 15% discount at 3+ units now
        updatedDiscounts.put(0, updatedProduct1Discounts);

        boolean productsUpdated = service.updateAgreementProducts("SUP009", 0, updatedDiscounts);
        assertTrue(productsUpdated, "Failed to update agreement products");

        // Step 7: Retrieve and verify the updated agreement
        List<String> agreements = service.getAgreementsBySupplier("SUP009");
        assertFalse(agreements.isEmpty(), "No agreements found");

        String agreement = agreements.get(0);
        assertTrue(agreement.contains(service.getPaymentMethods().get(1)), "Payment method not updated correctly");
        assertTrue(agreement.contains(service.getPaymentTimings().get(1)), "Payment timing not updated correctly");
    }

    // Test 10: Create order
    @Test
    public void testCreateOrder() {
        // Add supplier, products, and agreement first
        service.addSupplierWithDelivery("Order Supplier", "SUP010", "123456789", "1,3,5", List.of("Ofek,0548318083"));
        service.addProduct("Order Product", "SUP010", "PROD008", 10, 100.0, 1);

        // Create discounts for the agreement
        Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();
        Map<Integer, Integer> discounts = new HashMap<>();
        discounts.put(5, 10);  // 10% discount for 5+ units
        productDiscounts.put(0, discounts);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        service.createAgreement("SUP010", 0, 0, validFrom, validTo, productDiscounts);

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
        service.addProduct("Status Product", "SUP011", "PROD009", 10, 100.0, 1);

        // Create discounts for the agreement
        Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();
        Map<Integer, Integer> discounts = new HashMap<>();
        discounts.put(5, 10);
        productDiscounts.put(0, discounts);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        service.createAgreement("SUP011", 0, 0, validFrom, validTo, productDiscounts);

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
        List<String> ordersWithOldStatus = service.getOrdersByStatus(1);
        assertTrue(ordersWithOldStatus.isEmpty(), "There should be no orders with status 1");

        List<String> ordersWithNewStatus = service.getOrdersByStatus(2);
        assertFalse(ordersWithNewStatus.isEmpty(), "There should be orders with status 2");
    }

    // Part 12: Test total price
    @Test
    public void testGetTotalPrice_validSupplierAndAgreement() {
        // Arrange
        SystemController systemController = SystemController.getInstance();
        systemController.addSupplierWithDelivery("Supplier A", "123", "123-456", "1,2,3", List.of("John Doe,123456789"));

        systemController.addProductWithDiscounts(
                "Product A", "123", "1001", 10, 5.0, 1
        );
        systemController.addProductWithDiscounts(
                "Product B", "123", "1002", 5, 10.0, 1
        );

        Map<Integer, Map<Integer, Integer>> agreementProducts = new HashMap<>();
        Map<Integer, Integer> discounts = new HashMap<>();
        discounts.put(10, 5);  // Quantity-based discount
        agreementProducts.put(0, discounts);

        systemController.createAgreement(
                "123",
                0, // Payment method index
                0, // Payment timing index
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                agreementProducts
        );

        Map<String, Integer> items = new HashMap<>();
        items.put("1001", 3);
        items.put("1002", 2);

        // Act
        double totalPrice = systemController.getTotalPrice("123", items, 0);

        // Assert
        assertEquals(35.0, totalPrice);
    }

    @Test
    public void testGetTotalPrice_nonExistentSupplier() {
        // Arrange
        SystemController systemController = SystemController.getInstance();
        Map<String, Integer> items = new HashMap<>();
        items.put("1001", 2);

        // Act
        double totalPrice = systemController.getTotalPrice("999", items, 0);

        // Assert
        assertEquals(-1, totalPrice);
    }

    @Test
    public void testGetTotalPrice_invalidAgreementIndex() {
        // Arrange
        SystemController systemController = SystemController.getInstance();
        systemController.addSupplierWithDelivery("Supplier A", "123", "123-456", "1,2,3", List.of("John Doe,123456789"));

        // Act
        double totalPrice = systemController.getTotalPrice("123", Map.of("1001", 2), -1);

        // Assert
        assertEquals(-1, totalPrice);
    }

    @Test
    public void testGetTotalPrice_emptyItemsList() {
        // Arrange
        SystemController systemController = SystemController.getInstance();
        systemController.addSupplierWithDelivery("Supplier A", "123", "123-456", "1,2,3", List.of("John Doe,123456789"));

        Map<Integer, Map<Integer, Integer>> agreementProducts = new HashMap<>();
        systemController.createAgreement(
                "123",
                0,
                0,
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                agreementProducts
        );

        // Act
        double totalPrice = systemController.getTotalPrice("123", new HashMap<>(), 0);

        // Assert
        assertEquals(0, totalPrice);
    }

    @Test
    public void testGetTotalPrice_nonexistentProductInItems() {
        // Arrange
        SystemController systemController = SystemController.getInstance();
        systemController.addSupplierWithDelivery("Supplier A", "123", "123-456", "1,2,3", List.of("John Doe,123456789"));

        systemController.addProductWithDiscounts(
                "Product A", "123", "1001", 10, 5.0, 1
        );

        Map<Integer, Map<Integer, Integer>> agreementProducts = new HashMap<>();
        systemController.createAgreement(
                "123",
                0,
                0,
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                agreementProducts
        );

        Map<String, Integer> items = new HashMap<>();
        items.put("9999", 2);

        // Act
        double totalPrice = systemController.getTotalPrice("123", items, 0);

        // Assert
        assertEquals(0, totalPrice);
    }
}

