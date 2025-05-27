/*package DomainLayer;

import DataAccessLayer.DatabaseConnection;
import DomainLayer.Supplier.*;
import DomainLayer.Supplier.Enums.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SystemControllerTests {

    private SystemController systemController;
    private static long testCounter = 0;

    @BeforeAll
    static void setUpClass() {
        try {
            DatabaseConnection.setTestMode(true);
            DatabaseConnection.getValidConnection();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            fail("Failed to establish database connection: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        testCounter++;
        try {
            // Ensure connection is valid before each test
            DatabaseConnection.getValidConnection();
            systemController = SystemController.getInstance();
        } catch (SQLException e) {
            fail("Failed to get valid database connection: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            cleanupTestData();
        } catch (Exception e) {
            System.err.println("Warning during tearDown: " + e.getMessage());
            // Reset connection if cleanup failed
            DatabaseConnection.resetConnection();
        }
    }

    private void cleanupTestData() {
        try (Connection conn = DatabaseConnection.getValidConnection();
             Statement stmt = conn.createStatement()) {

            // Disable foreign key constraints temporarily
            stmt.executeUpdate("PRAGMA foreign_keys = OFF");

            stmt.executeUpdate("DELETE FROM OrderItems");
            stmt.executeUpdate("DELETE FROM Orders");
            stmt.executeUpdate("DELETE FROM AgreementProductDiscounts");
            stmt.executeUpdate("DELETE FROM Agreements");
            stmt.executeUpdate("DELETE FROM Products");
            stmt.executeUpdate("DELETE FROM ContactPersons");
            stmt.executeUpdate("DELETE FROM SupplierDeliveryDays");
            stmt.executeUpdate("DELETE FROM SupplierPickupDetails");
            stmt.executeUpdate("DELETE FROM Suppliers");

            // Re-enable foreign key constraints
            stmt.executeUpdate("PRAGMA foreign_keys = ON");

        } catch (SQLException e) {
            System.err.println("Warning during cleanup: " + e.getMessage());
            throw new RuntimeException("Cleanup failed", e);
        }
    }

    @AfterAll
    static void tearDownClass() {
        try (Connection conn = DatabaseConnection.getValidConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("PRAGMA foreign_keys = OFF");
            stmt.executeUpdate("DELETE FROM OrderItems");
            stmt.executeUpdate("DELETE FROM Orders");
            stmt.executeUpdate("DELETE FROM AgreementProductDiscounts");
            stmt.executeUpdate("DELETE FROM Agreements");
            stmt.executeUpdate("DELETE FROM Products");
            stmt.executeUpdate("DELETE FROM ContactPersons");
            stmt.executeUpdate("DELETE FROM SupplierDeliveryDays");
            stmt.executeUpdate("DELETE FROM SupplierPickupDetails");
            stmt.executeUpdate("DELETE FROM Suppliers");
            stmt.executeUpdate("PRAGMA foreign_keys = ON");

        } catch (SQLException e) {
            System.err.println("Final cleanup failed: " + e.getMessage());
        } finally {
            DatabaseConnection.setTestMode(false);
            DatabaseConnection.closeConnection();
        }
    }

    private String getUniqueId(String prefix) {
        return "TEST_" + prefix + "_" + testCounter + "_" + System.currentTimeMillis();
    }

    private String getUniqueCatalogNumber(String prefix) {
        return prefix + "_" + testCounter + "_" + (System.currentTimeMillis() % 100000);
    }


    // ===================== SUPPLIER MANAGEMENT TESTS =====================
    @Test
    void testAddSupplierWithDelivery_Success() {
        // Arrange
        String name = "DeliverySupplier";
        String id = getUniqueId("DS");
        String bankAccount = "123-456-789";
        String deliveryDays = "2,4,6"; // MONDAY, WEDNESDAY, FRIDAY
        List<String> contacts = Arrays.asList("John Doe,123456789", "Jane Smith,987654321");

        // Act
        boolean result = systemController.addSupplierWithDelivery(name, id, bankAccount, deliveryDays, contacts);

        // Assert
        assertTrue(result, "Should successfully add supplier with delivery");
        assertTrue(systemController.checkIfSupplierExists(id), "Supplier should exist after adding");

        String supplier = systemController.getSupplierById(id);
        assertNotNull(supplier, "Should be able to retrieve supplier");
        assertTrue(supplier.contains(name), "Supplier should contain correct name");
        assertTrue(supplier.contains(id), "Supplier should contain correct ID");
        assertTrue(supplier.contains(bankAccount), "Supplier should contain correct bank account");
    }

    @Test
    void testAddSupplierWithDelivery_DuplicateId() {
        // Arrange
        String id = getUniqueId("DS");
        String deliveryDays = "1";
        List<String> contacts = List.of("Contact,123");

        systemController.addSupplierWithDelivery("First", id, "123", deliveryDays, contacts);

        // Act
        boolean result = systemController.addSupplierWithDelivery("Second", id, "456", deliveryDays, contacts);

        // Assert
        assertFalse(result, "Should not allow duplicate supplier ID");
    }

    @Test
    void testAddSupplierWithDelivery_InvalidDeliveryDays() {
        // Act
        boolean result = systemController.addSupplierWithDelivery("TestSupplier", getUniqueId("DS"), "123", "0,8,9", Arrays.asList("Contact,123"));

        // Assert
        assertFalse(result, "Should reject invalid delivery days");
    }

    @Test
    void testAddSupplierWithDelivery_EmptyDeliveryDays() {
        // Act
        boolean result = systemController.addSupplierWithDelivery("TestSupplier", getUniqueId("DS"), "123", "", Arrays.asList("Contact,123"));

        // Assert
        assertTrue(result, "Should succeed with empty delivery days");
    }

    @Test
    void testAddSupplierNeedsPickup_Success() {
        // Arrange
        String name = "PickupSupplier";
        String id = getUniqueId("PS");
        String bankAccount = "987-654-321";
        String address = "123 Main St, City";
        List<String> contacts = List.of("Bob Johnson,555-1234");

        // Act
        boolean result = systemController.addSupplierNeedsPickup(name, id, bankAccount, address, contacts);

        // Assert
        assertTrue(result, "Should successfully add pickup supplier");
        assertTrue(systemController.checkIfSupplierExists(id), "Supplier should exist");

        String pickupSuppliers = systemController.getSuppliersRequiringPickupAsString();
        assertTrue(pickupSuppliers.contains(id), "Should appear in pickup suppliers list");
        assertTrue(pickupSuppliers.contains(address), "Should contain correct address");
    }

    @Test
    void testAddSupplierNeedsPickup_DuplicateId() {
        // Arrange
        String id = getUniqueId("PS");
        systemController.addSupplierNeedsPickup("First", id, "123", "Address1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.addSupplierNeedsPickup("Second", id, "456", "Address2", Arrays.asList("Contact2,456"));

        // Assert
        assertFalse(result, "Should not allow duplicate supplier ID");
    }

    @Test
    void testUpdateSupplierField_Name_Success() {
        // Arrange
        String id = getUniqueId("US");
        systemController.addSupplierWithDelivery("OldName", id, "123", "1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.updateSupplierField(id, "name", "NewName");

        // Assert
        assertTrue(result, "Should successfully update supplier name");
        String supplier = systemController.getSupplierById(id);
        assertTrue(supplier.contains("NewName"), "Should contain new name");
        assertFalse(supplier.contains("OldName"), "Should not contain old name");
    }

    @Test
    void testUpdateSupplierField_BankAccount_Success() {
        // Arrange
        String id = "US002";
        systemController.addSupplierWithDelivery("TestSupplier", id, "123-456", "1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.updateSupplierField(id, "bankaccount", "789-101112");

        // Assert
        assertTrue(result);
        String supplier = systemController.getSupplierById(id);
        assertTrue(supplier.contains("789-101112"));
    }

    @Test
    void testUpdateSupplierField_DeliveryDays_Success() {
        // Arrange
        String id = "US003";
        systemController.addSupplierWithDelivery("TestSupplier", id, "123", "1,2", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.updateSupplierField(id, "deliverydays", "3,4,5");

        // Assert
        assertTrue(result);
        String supplier = systemController.getSupplierById(id);
        assertTrue(supplier.contains("TUESDAY"));   // 3
        assertTrue(supplier.contains("WEDNESDAY")); // 4
        assertTrue(supplier.contains("THURSDAY"));  // 5
    }

    @Test
    void testUpdateSupplierField_DeliveryDays_OnPickupSupplier() {
        // Arrange
        String id = "US004";
        systemController.addSupplierNeedsPickup("PickupSupplier", id, "123", "Address", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.updateSupplierField(id, "deliverydays", "1,2,3");

        // Assert
        assertFalse(result); // Should fail for pickup supplier
    }

    @Test
    void testUpdateSupplierField_ContactPersons_Success() {
        // Arrange
        String id = "US005";
        systemController.addSupplierWithDelivery("TestSupplier", id, "123", "1", Arrays.asList("OldContact,123"));

        // Act
        boolean result = systemController.updateSupplierField(id, "contactpersons", "NewContact,456");

        // Assert
        assertTrue(result);
        String supplier = systemController.getSupplierById(id);
        assertTrue(supplier.contains("NewContact"));
        assertTrue(supplier.contains("456"));
    }

    @Test
    void testUpdateSupplierField_ContactPersons_InvalidFormat() {
        // Arrange
        String id = getUniqueId("US");
        systemController.addSupplierWithDelivery("TestSupplier", id, "123", "2", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.updateSupplierField(id, "contactpersons", "InvalidFormatNoComma");

        // Assert
        assertFalse(result);

        // Act
        result = systemController.updateSupplierField(id, "contactpersons", ",");

        // Assert
        assertFalse(result);

        // Act
        result = systemController.updateSupplierField(id, "contactpersons", ",123456");

        // Assert
        assertFalse(result);
    }
    @Test
    void testUpdateSupplierField_InvalidField() {
        // Arrange
        String id = "US007";
        systemController.addSupplierWithDelivery("TestSupplier", id, "123", "1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.updateSupplierField(id, "invalidfield", "value");

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateSupplierField_NonExistentSupplier() {
        // Act
        boolean result = systemController.updateSupplierField("NONEXISTENT", "name", "NewName");

        // Assert
        assertFalse(result);
    }

    @Test
    void testRemoveSupplier_Success() {
        // Arrange
        String id = getUniqueId("RS");
        systemController.addSupplierWithDelivery("ToRemove", id, "123", "2", Arrays.asList("Contact,123"));
        assertTrue(systemController.checkIfSupplierExists(id));

        // Act
        boolean result = systemController.removeSupplier(id);

        // Assert
        assertTrue(result);
        assertFalse(systemController.checkIfSupplierExists(id));
        assertNull(systemController.getSupplierById(id));
    }

    @Test
    void testRemoveSupplier_NonExistent() {
        // Act
        boolean result = systemController.removeSupplier("NONEXISTENT");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetAllSuppliers() {
        // Arrange
        String id1 = "GAS001";
        String id2 = "GAS002";
        systemController.addSupplierWithDelivery("Supplier1", id1, "123", "1", Arrays.asList("Contact1,123"));
        systemController.addSupplierNeedsPickup("Supplier2", id2, "456", "Address", Arrays.asList("Contact2,456"));

        // Act
        List<String> suppliers = systemController.getAllSuppliers();

        // Assert
        assertNotNull(suppliers);
        assertTrue(suppliers.size() >= 2);
        boolean found1 = suppliers.stream().anyMatch(s -> s.contains(id1));
        boolean found2 = suppliers.stream().anyMatch(s -> s.contains(id2));
        assertTrue(found1);
        assertTrue(found2);
    }

    // ===================== PRODUCT MANAGEMENT TESTS =====================

    @Test
    void testAddProductWithDiscounts_Success() {
        // Arrange
        String supplierId = getUniqueId("APD");
        String catalogNumber = getUniqueCatalogNumber("PROD");
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.addProductWithDiscounts("TestProduct", supplierId, catalogNumber, 10, 50.0, 1);

        // Assert
        assertTrue(result);
        List<String> products = systemController.getAllProducts();
        boolean found = products.stream().anyMatch(p -> p.contains(catalogNumber) && p.contains(supplierId));
        assertTrue(found);
    }

    @Test
    void testAddProductWithDiscounts_NonExistentSupplier() {
        // Act
        boolean result = systemController.addProductWithDiscounts("TestProduct", "NONEXISTENT", "PROD002", 10, 50.0, 1);

        // Assert
        assertFalse(result);
    }

    @Test
    void testAddProductWithDiscounts_InvalidUnit() {
        // Arrange
        String supplierId = "APD002";
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.addProductWithDiscounts("TestProduct", supplierId, "PROD003", 10, 50.0, 999);

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateProductField_Name_Success() {
        // Arrange
        String supplierId = "UPF001";
        String catalogNumber = "PROD004";
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("OldName", supplierId, catalogNumber, 10, 50.0, 1);

        // Act
        boolean result = systemController.updateProductField(supplierId, catalogNumber, "name", "NewName");

        // Assert
        assertTrue(result);
        String product = systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber);
        assertTrue(product.contains("NewName"));
    }

    @Test
    void testUpdateProductField_Price_Success() {
        // Arrange
        String supplierId = "UPF002";
        String catalogNumber = "PROD005";
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("TestProduct", supplierId, catalogNumber, 10, 50.0, 1);

        // Act
        boolean result = systemController.updateProductField(supplierId, catalogNumber, "price", "75.0");

        // Assert
        assertTrue(result);
        String product = systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber);
        assertTrue(product.contains("75.00"));
    }

    @Test
    void testUpdateProductField_InvalidPrice() {
        // Arrange
        String supplierId = getUniqueId("UPF");
        String catalogNumber = getUniqueCatalogNumber("PROD");
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("TestProduct", supplierId, catalogNumber, 10, 50.0, 1);

        // Act
        boolean result = systemController.updateProductField(supplierId, catalogNumber, "price", "invalid");

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateProductField_NonExistentSupplier() {
        // Act
        boolean result = systemController.updateProductField("NONEXISTENT", "PROD007", "name", "NewName");

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateProductField_NonExistentProduct() {
        // Arrange
        String supplierId = "UPF004";
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.updateProductField(supplierId, "NONEXISTENT", "name", "NewName");

        // Assert
        assertFalse(result);
    }

    @Test
    void testRemoveProduct_Success() {
        // Arrange
        String supplierId = "RP001";
        String catalogNumber = "PROD008";
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("ToRemove", supplierId, catalogNumber, 10, 50.0, 1);

        // Verify product exists
        assertNotNull(systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber));

        // Act
        boolean result = systemController.removeProduct(supplierId, catalogNumber);

        // Assert
        assertTrue(result);
        assertNull(systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber));
    }

    @Test
    void testRemoveProduct_NonExistentSupplier() {
        // Act
        boolean result = systemController.removeProduct("NONEXISTENT", "PROD009");

        // Assert
        assertFalse(result);
    }

    @Test
    void testRemoveProduct_NonExistentProduct() {
        // Arrange
        String supplierId = "RP002";
        systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.removeProduct(supplierId, "NONEXISTENT");

        // Assert
        assertFalse(result);
    }

    // ===================== AGREEMENT MANAGEMENT TESTS =====================

    @Test
    void testCreateAgreement_Success() {
        // Arrange
        String supplierId = getUniqueId("CA");
        String catalogNumber1 = getUniqueCatalogNumber("PROD");
        String catalogNumber2 = getUniqueCatalogNumber("PROD");

        systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("Product1", supplierId, catalogNumber1, 10, 50.0, 1);
        systemController.addProductWithDiscounts("Product2", supplierId, catalogNumber2, 20, 100.0, 1);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);

        Map<Integer, Map<Integer, Integer>> indexProducts = new HashMap<>();
        Map<Integer, Integer> product1Discounts = new HashMap<>();
        product1Discounts.put(5, 10); // 10% discount for 5+ units
        indexProducts.put(0, product1Discounts);

        // Act
        boolean result = systemController.createAgreement(supplierId, 0, 0, validFrom, validTo, indexProducts);

        // Assert
        assertTrue(result);
        List<String> agreements = systemController.getAgreementsBySupplierAsStrings(supplierId);
        assertFalse(agreements.isEmpty());
    }

    @Test
    void testCreateAgreement_NonExistentSupplier() {
        // Act
        boolean result = systemController.createAgreement("NONEXISTENT", 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

        // Assert
        assertFalse(result);
    }

    @Test
    void testCreateAgreement_InvalidPaymentMethod() {
        // Arrange
        String supplierId = "CA002";
        systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

        // Act
        boolean result = systemController.createAgreement(supplierId, 999, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateAgreementProducts_Success() {
        // Arrange
        String supplierId = "UAP001";
        systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("Product1", supplierId, "PROD012", 10, 50.0, 1);

        // Create agreement first
        Map<Integer, Map<Integer, Integer>> initialProducts = new HashMap<>();
        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), initialProducts);

        // Update agreement products
        Map<Integer, Map<Integer, Integer>> updatedProducts = new HashMap<>();
        Map<Integer, Integer> discounts = new HashMap<>();
        discounts.put(3, 15); // 15% discount for 3+ units
        updatedProducts.put(0, discounts);

        // Act
        boolean result = systemController.updateAgreementProducts(supplierId, 0, updatedProducts);

        // Assert
        assertTrue(result);
    }

    @Test
    void testUpdatePaymentMethods_Success() {
        // Arrange
        String supplierId = "UPM001";
        systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

        // Act
        boolean result = systemController.updatePaymentMethods(supplierId, 0, 1);

        // Assert
        assertTrue(result);
    }

    @Test
    void testUpdatePaymentTiming_Success() {
        // Arrange
        String supplierId = "UPT001";
        systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

        // Act
        boolean result = systemController.updatePaymentTiming(supplierId, 0, 1);

        // Assert
        assertTrue(result);
    }

    @Test
    void testUpdateValidFrom_Success() {
        // Arrange
        String supplierId = "UVF001";
        systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        LocalDate originalDate = LocalDate.now();
        systemController.createAgreement(supplierId, 0, 0, originalDate, originalDate.plusDays(30), new HashMap<>());

        LocalDate newValidFrom = originalDate.minusDays(5);

        // Act
        boolean result = systemController.updateValidFrom(supplierId, 0, newValidFrom);

        // Assert
        assertTrue(result);
        LocalDate retrievedDate = systemController.getValidFromOfAgreement(supplierId, 0);
        assertEquals(newValidFrom, retrievedDate);
    }

    @Test
    void testRemoveAgreement_Success() {
        // Arrange
        String supplierId = "RA001";
        systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

        // Verify agreement exists
        List<String> agreementsBefore = systemController.getAgreementsBySupplierAsStrings(supplierId);
        assertFalse(agreementsBefore.isEmpty());

        // Act
        boolean result = systemController.removeAgreement(supplierId, 0);

        // Assert
        assertTrue(result);
        List<String> agreementsAfter = systemController.getAgreementsBySupplierAsStrings(supplierId);
        assertTrue(agreementsAfter.size() < agreementsBefore.size());
    }

    // ===================== ORDER MANAGEMENT TESTS =====================

    @Test
    void testInsertOrder_Success() {
        // Arrange
        String supplierId = getUniqueId("IO");
        String catalogNumber = getUniqueCatalogNumber("PROD");

        systemController.addSupplierWithDelivery("OrderSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("OrderProduct", supplierId, catalogNumber, 10, 50.0, 1);

        Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();
        Map<Integer, Integer> discounts = new HashMap<>();
        discounts.put(5, 10);
        productDiscounts.put(0, discounts);

        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), productDiscounts);

        LocalDate orderDate = LocalDate.now();
        LocalDate supplyDate = orderDate.plusDays(7);
        Map<Integer, Integer> items = new HashMap<>();
        items.put(0, 5); // 5 units of first product

        // Act
        boolean result = systemController.insertOrder(supplierId, orderDate, supplyDate, items, "John Doe", "123456789", 0, 1);

        // Assert
        assertTrue(result);
        List<String> orders = systemController.getAllOrders();
        assertFalse(orders.isEmpty());
    }

    @Test
    void testInsertOrder_NonExistentSupplier() {
        // Act
        boolean result = systemController.insertOrder("NONEXISTENT", LocalDate.now(), LocalDate.now().plusDays(7),
                new HashMap<>(), "Contact", "123", 0, 1);

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateOrderStatus_Success() {
        // Arrange
        String supplierId = "UOS001";
        systemController.addSupplierWithDelivery("OrderSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("OrderProduct", supplierId, "PROD014", 10, 50.0, 1);

        Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();
        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), productDiscounts);

        Map<Integer, Integer> items = new HashMap<>();
        items.put(0, 3);
        systemController.insertOrder(supplierId, LocalDate.now(), LocalDate.now().plusDays(7), items, "Contact", "123", 0, 1);

        // Get the order ID (assuming it's the first order with ID 0)
        int orderId = 0;

        // Act
        boolean result = systemController.updateOrderStatus(orderId, 2);

        // Assert
        assertTrue(result);
        List<String> ordersWithNewStatus = systemController.getOrdersByStatus(2);
        assertFalse(ordersWithNewStatus.isEmpty());
    }

    @Test
    void testUpdateOrderStatus_NonExistentOrder() {
        // Act
        boolean result = systemController.updateOrderStatus(99999, 2);

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateOrderStatus_InvalidStatus() {
        // Arrange
        String supplierId = "UOS002";
        systemController.addSupplierWithDelivery("OrderSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("OrderProduct", supplierId, "PROD015", 10, 50.0, 1);

        Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();
        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), productDiscounts);

        Map<Integer, Integer> items = new HashMap<>();
        items.put(0, 3);
        systemController.insertOrder(supplierId, LocalDate.now(), LocalDate.now().plusDays(7), items, "Contact", "123", 0, 1);

        // Act
        boolean result = systemController.updateOrderStatus(0, 999);

        // Assert
        assertFalse(result);
    }

    // ===================== UTILITY AND HELPER TESTS =====================

    @Test
    void testGetTotalPrice_Success() {
        // Arrange
        String supplierId = "GTP001";
        systemController.addSupplierWithDelivery("PriceSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.addProductWithDiscounts("Product1", supplierId, "PROD016", 10, 50.0, 1);
        systemController.addProductWithDiscounts("Product2", supplierId, "PROD017", 5, 25.0, 1);

        Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();
        Map<Integer, Integer> discounts1 = new HashMap<>();
        discounts1.put(5, 10); // 10% discount for 5+ units
        productDiscounts.put(0, discounts1);

        Map<Integer, Integer> discounts2 = new HashMap<>();
        discounts2.put(3, 5); // 5% discount for 3+ units
        productDiscounts.put(1, discounts2);

        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), productDiscounts);

        Map<String, Integer> items = new HashMap<>();
        items.put("PROD016", 6); // Should get 10% discount
        items.put("PROD017", 4); // Should get 5% discount

        // Act
        double totalPrice = systemController.getTotalPrice(supplierId, items, 0);

        // Expected: (50*6*0.9) + (25*4*0.95) = 270 + 95 = 365
        double expected = (50.0 * 6 * 0.9) + (25.0 * 4 * 0.95);

        // Assert
        assertEquals(expected, totalPrice, 0.01);
    }

    @Test
    void testGetTotalPrice_NonExistentSupplier() {
        // Act
        double result = systemController.getTotalPrice("NONEXISTENT", new HashMap<>(), 0);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void testGetTotalPrice_InvalidAgreementIndex() {
        // Arrange
        String supplierId = "GTP002";
        systemController.addSupplierWithDelivery("PriceSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

        // Act
        double result = systemController.getTotalPrice(supplierId, new HashMap<>(), 999);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void testGetTotalPrice_EmptyItems() {
        // Arrange
        String supplierId = "GTP003";
        systemController.addSupplierWithDelivery("PriceSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
        systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

        // Act
        double result = systemController.getTotalPrice(supplierId, new HashMap<>(), 0);
    }
}*/

package DomainLayer;

import DomainLayer.Supplier.*;
import DomainLayer.Supplier.Enums.*;
import DomainLayer.Supplier.Repositories.SupplierRepository;
import DomainLayer.Supplier.Repositories.OrderRepository;
import DomainLayer.OrderController;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.MockedConstruction;

import java.time.LocalDate;
import java.util.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SystemControllerTests_Updated {

    private SystemController systemController;
    private MockedStatic<OrderController> orderControllerMock;
    private static long testCounter = 0;

    @BeforeEach
    void setUp() {
        testCounter++;

        // Reset singleton instances before each test
        resetSingletonInstance(SystemController.class, "instance");
        resetSingletonInstance(OrderController.class, "instance");

        // Setup static mocks
        orderControllerMock = mockStatic(OrderController.class);

        // Mock OrderController instance
        OrderController mockOrderControllerInstance = mock(OrderController.class);
        orderControllerMock.when(OrderController::getInstance).thenReturn(mockOrderControllerInstance);

        // Mock repository constructors to avoid database connections
        try (MockedConstruction<SupplierRepository> supplierRepoMock = mockConstruction(SupplierRepository.class);
             MockedConstruction<OrderRepository> orderRepoMock = mockConstruction(OrderRepository.class)) {

            systemController = SystemController.getInstance();
        }
    }

    @AfterEach
    void tearDown() {
        // Close all static mocks
        if (orderControllerMock != null) {
            orderControllerMock.close();
        }
    }

    private void resetSingletonInstance(Class<?> clazz, String fieldName) {
        try {
            Field instanceField = clazz.getDeclaredField(fieldName);
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //Ignore if field doesn't exist or can't be accessed
        }
    }

    private String getUniqueId(String prefix) {
        return "TEST_" + prefix + "_" + testCounter + "_" + System.currentTimeMillis();
    }

    private String getUniqueCatalogNumber(String prefix) {
        return prefix + "_" + testCounter + "_" + (System.currentTimeMillis() % 100000);
    }

    // ===================== SUPPLIER MANAGEMENT TESTS =====================

    @Test
    void testAddSupplierWithDelivery_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String name = "DeliverySupplier";
            String id = getUniqueId("DS");
            String bankAccount = "123-456-789";
            String deliveryDays = "2,4,6"; // MONDAY, WEDNESDAY, FRIDAY
            List<String> contacts = Arrays.asList("John Doe,123456789", "Jane Smith,987654321");

            // Act
            boolean result = systemController.addSupplierWithDelivery(name, id, bankAccount, deliveryDays, contacts);

            // Assert
            assertTrue(result, "Should successfully add supplier with delivery");
            assertTrue(systemController.checkIfSupplierExists(id), "Supplier should exist after adding");

            String supplier = systemController.getSupplierById(id);
            assertNotNull(supplier, "Should be able to retrieve supplier");
            assertTrue(supplier.contains(name), "Supplier should contain correct name");
            assertTrue(supplier.contains(id), "Supplier should contain correct ID");
            assertTrue(supplier.contains(bankAccount), "Supplier should contain correct bank account");
        }
    }

    @Test
    void testAddSupplierWithDelivery_DuplicateId() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("DS");
            String deliveryDays = "1";
            List<String> contacts = List.of("Contact,123");

            systemController.addSupplierWithDelivery("First", id, "123", deliveryDays, contacts);

            // Act
            boolean result = systemController.addSupplierWithDelivery("Second", id, "456", deliveryDays, contacts);

            // Assert
            assertFalse(result, "Should not allow duplicate supplier ID");
        }
    }

    @Test
    void testAddSupplierWithDelivery_InvalidDeliveryDays() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.addSupplierWithDelivery("TestSupplier", getUniqueId("DS"), "123", "0,8,9", Arrays.asList("Contact,123"));

            // Assert
            assertFalse(result, "Should reject invalid delivery days");
        }
    }

    @Test
    void testAddSupplierWithDelivery_EmptyDeliveryDays() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.addSupplierWithDelivery("TestSupplier", getUniqueId("DS"), "123", "", Arrays.asList("Contact,123"));

            // Assert
            assertTrue(result, "Should succeed with empty delivery days");
        }
    }

    @Test
    void testAddSupplierNeedsPickup_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String name = "PickupSupplier";
            String id = getUniqueId("PS");
            String bankAccount = "987-654-321";
            String address = "123 Main St, City";
            List<String> contacts = List.of("Bob Johnson,555-1234");

            // Act
            boolean result = systemController.addSupplierNeedsPickup(name, id, bankAccount, address, contacts);

            // Assert
            assertTrue(result, "Should successfully add pickup supplier");
            assertTrue(systemController.checkIfSupplierExists(id), "Supplier should exist");

            String pickupSuppliers = systemController.getSuppliersRequiringPickupAsString();
            assertTrue(pickupSuppliers.contains(id), "Should appear in pickup suppliers list");
            assertTrue(pickupSuppliers.contains(address), "Should contain correct address");
        }
    }

    @Test
    void testAddSupplierNeedsPickup_DuplicateId() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("PS");
            systemController.addSupplierNeedsPickup("First", id, "123", "Address1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.addSupplierNeedsPickup("Second", id, "456", "Address2", Arrays.asList("Contact2,456"));

            // Assert
            assertFalse(result, "Should not allow duplicate supplier ID");
        }
    }

    @Test
    void testUpdateSupplierField_Name_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("US");
            systemController.addSupplierWithDelivery("OldName", id, "123", "1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.updateSupplierField(id, "name", "NewName");

            // Assert
            assertTrue(result, "Should successfully update supplier name");
            String supplier = systemController.getSupplierById(id);
            assertTrue(supplier.contains("NewName"), "Should contain new name");
            assertFalse(supplier.contains("OldName"), "Should not contain old name");
        }
    }

    @Test
    void testUpdateSupplierField_BankAccount_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("US");
            systemController.addSupplierWithDelivery("TestSupplier", id, "123-456", "1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.updateSupplierField(id, "bankaccount", "789-101112");

            // Assert
            assertTrue(result);
            String supplier = systemController.getSupplierById(id);
            assertTrue(supplier.contains("789-101112"));
        }
    }

    @Test
    void testUpdateSupplierField_DeliveryDays_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("US");
            systemController.addSupplierWithDelivery("TestSupplier", id, "123", "1,2", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.updateSupplierField(id, "deliverydays", "3,4,5");

            // Assert
            assertTrue(result);
            String supplier = systemController.getSupplierById(id);
            assertTrue(supplier.contains("TUESDAY"));   // 3
            assertTrue(supplier.contains("WEDNESDAY")); // 4
            assertTrue(supplier.contains("THURSDAY"));  // 5
        }
    }

    @Test
    void testUpdateSupplierField_DeliveryDays_OnPickupSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("US");
            systemController.addSupplierNeedsPickup("PickupSupplier", id, "123", "Address", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.updateSupplierField(id, "deliverydays", "1,2,3");

            // Assert
            assertFalse(result); // Should fail for pickup supplier
        }
    }

    @Test
    void testUpdateSupplierField_ContactPersons_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("US");
            systemController.addSupplierWithDelivery("TestSupplier", id, "123", "1", Arrays.asList("OldContact,123"));

            // Act
            boolean result = systemController.updateSupplierField(id, "contactpersons", "NewContact,456");

            // Assert
            assertTrue(result);
            String supplier = systemController.getSupplierById(id);
            assertTrue(supplier.contains("NewContact"));
            assertTrue(supplier.contains("456"));
        }
    }

    @Test
    void testUpdateSupplierField_ContactPersons_InvalidFormat() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("US");
            systemController.addSupplierWithDelivery("TestSupplier", id, "123", "2", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.updateSupplierField(id, "contactpersons", "InvalidFormatNoComma");

            // Assert
            assertFalse(result);

            // Act
            result = systemController.updateSupplierField(id, "contactpersons", ",");

            // Assert
            assertFalse(result);

            // Act
            result = systemController.updateSupplierField(id, "contactpersons", ",123456");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testUpdateSupplierField_InvalidField() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("US");
            systemController.addSupplierWithDelivery("TestSupplier", id, "123", "1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.updateSupplierField(id, "invalidfield", "value");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testUpdateSupplierField_NonExistentSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.updateSupplierField("NONEXISTENT", "name", "NewName");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testRemoveSupplier_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id = getUniqueId("RS");
            systemController.addSupplierWithDelivery("ToRemove", id, "123", "2", Arrays.asList("Contact,123"));
            assertTrue(systemController.checkIfSupplierExists(id));

            // Act
            boolean result = systemController.removeSupplier(id);

            // Assert
            assertTrue(result);
            assertFalse(systemController.checkIfSupplierExists(id));
            assertNull(systemController.getSupplierById(id));
        }
    }

    @Test
    void testRemoveSupplier_NonExistent() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.removeSupplier("NONEXISTENT");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testGetAllSuppliers() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String id1 = getUniqueId("GAS1");
            String id2 = getUniqueId("GAS2");
            systemController.addSupplierWithDelivery("Supplier1", id1, "123", "1", Arrays.asList("Contact1,123"));
            systemController.addSupplierNeedsPickup("Supplier2", id2, "456", "Address", Arrays.asList("Contact2,456"));

            // Act
            List<String> suppliers = systemController.getAllSuppliers();

            // Assert
            assertNotNull(suppliers);
            assertTrue(suppliers.size() >= 2);
            boolean found1 = suppliers.stream().anyMatch(s -> s.contains(id1));
            boolean found2 = suppliers.stream().anyMatch(s -> s.contains(id2));
            assertTrue(found1);
            assertTrue(found2);
        }
    }

    // ===================== PRODUCT MANAGEMENT TESTS =====================

    @Test
    void testAddProductWithDiscounts_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("APD");
            String catalogNumber = getUniqueCatalogNumber("PROD");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.addProductWithDiscounts("TestProduct", supplierId, catalogNumber, 10, 50.0, 1);

            // Assert
            assertTrue(result);
            List<String> products = systemController.getAllProducts();
            boolean found = products.stream().anyMatch(p -> p.contains(catalogNumber) && p.contains(supplierId));
            assertTrue(found);
        }
    }

    @Test
    void testAddProductWithDiscounts_NonExistentSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.addProductWithDiscounts("TestProduct", "NONEXISTENT", "PROD002", 10, 50.0, 1);

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testAddProductWithDiscounts_InvalidUnit() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("APD");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.addProductWithDiscounts("TestProduct", supplierId, "PROD003", 10, 50.0, 999);

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testUpdateProductField_Name_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("UPF");
            String catalogNumber = getUniqueCatalogNumber("PROD");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
            systemController.addProductWithDiscounts("OldName", supplierId, catalogNumber, 10, 50.0, 1);

            // Act
            boolean result = systemController.updateProductField(supplierId, catalogNumber, "name", "NewName");

            // Assert
            assertTrue(result);
            String product = systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber);
            assertTrue(product.contains("NewName"));
        }
    }

    @Test
    void testUpdateProductField_Price_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("UPF");
            String catalogNumber = getUniqueCatalogNumber("PROD");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
            systemController.addProductWithDiscounts("TestProduct", supplierId, catalogNumber, 10, 50.0, 1);

            // Act
            boolean result = systemController.updateProductField(supplierId, catalogNumber, "price", "75.0");

            // Assert
            assertTrue(result);
            String product = systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber);
            assertTrue(product.contains("75.00"));
        }
    }

    @Test
    void testUpdateProductField_InvalidPrice() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("UPF");
            String catalogNumber = getUniqueCatalogNumber("PROD");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));
            systemController.addProductWithDiscounts("TestProduct", supplierId, catalogNumber, 10, 50.0, 1);

            // Act
            boolean result = systemController.updateProductField(supplierId, catalogNumber, "price", "invalid");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testUpdateProductField_NonExistentSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.updateProductField("NONEXISTENT", "PROD007", "name", "NewName");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testUpdateProductField_NonExistentProduct() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("UPF");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.updateProductField(supplierId, "NONEXISTENT", "name", "NewName");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testRemoveProduct_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("RP");
            String catalogNumber = getUniqueCatalogNumber("PROD");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
            systemController.addProductWithDiscounts("ToRemove", supplierId, catalogNumber, 10, 50.0, 1);

            // Verify product exists
            assertNotNull(systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber));

            // Act
            boolean result = systemController.removeProduct(supplierId, catalogNumber);

            // Assert
            assertTrue(result);
            assertNull(systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber));
        }
    }

    @Test
    void testRemoveProduct_NonExistentSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.removeProduct("NONEXISTENT", "PROD009");

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testRemoveProduct_NonExistentProduct() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("RP");
            systemController.addSupplierWithDelivery("ProductSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.removeProduct(supplierId, "NONEXISTENT");

            // Assert
            assertFalse(result);
        }
    }

    // ===================== AGREEMENT MANAGEMENT TESTS =====================

    @Test
    void testCreateAgreement_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("CA");
            String catalogNumber1 = getUniqueCatalogNumber("PROD");
            String catalogNumber2 = getUniqueCatalogNumber("PROD");

            systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));
            systemController.addProductWithDiscounts("Product1", supplierId, catalogNumber1, 10, 50.0, 1);
            systemController.addProductWithDiscounts("Product2", supplierId, catalogNumber2, 20, 100.0, 1);

            LocalDate validFrom = LocalDate.now();
            LocalDate validTo = validFrom.plusMonths(6);

            Map<Integer, Map<Integer, Integer>> indexProducts = new HashMap<>();
            Map<Integer, Integer> product1Discounts = new HashMap<>();
            product1Discounts.put(5, 10); // 10% discount for 5+ units
            indexProducts.put(0, product1Discounts);

            // Act
            boolean result = systemController.createAgreement(supplierId, 0, 0, validFrom, validTo, indexProducts);

            // Assert
            assertTrue(result);
            List<String> agreements = systemController.getAgreementsBySupplierAsStrings(supplierId);
            assertFalse(agreements.isEmpty());
        }
    }

    @Test
    void testCreateAgreement_NonExistentSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.createAgreement("NONEXISTENT", 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

            // Assert
            assertFalse(result);
        }
    }

    @Test
    void testCreateAgreement_InvalidPaymentMethod() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("CA");
            systemController.addSupplierWithDelivery("AgreementSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

            // Act
            boolean result = systemController.createAgreement(supplierId, 999, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

            // Assert
            assertFalse(result);
        }
    }

    // ===================== ORDER MANAGEMENT TESTS =====================

    @Test
    void testInsertOrder_Success() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("IO");
            String catalogNumber = getUniqueCatalogNumber("PROD");

            systemController.addSupplierWithDelivery("OrderSupplier", supplierId, "123", "2", Arrays.asList("Contact,123"));
            systemController.addProductWithDiscounts("OrderProduct", supplierId, catalogNumber, 10, 50.0, 1);

            Map<Integer, Map<Integer, Integer>> productDiscounts = new HashMap<>();
            Map<Integer, Integer> discounts = new HashMap<>();
            discounts.put(5, 10);
            productDiscounts.put(0, discounts);

            systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), productDiscounts);

            LocalDate orderDate = LocalDate.now();
            LocalDate supplyDate = orderDate.plusDays(7);
            Map<Integer, Integer> items = new HashMap<>();
            items.put(0, 5); // 5 units of first product

            // Act
            boolean result = systemController.insertOrder(supplierId, orderDate, supplyDate, items, "John Doe", "123456789", 0, 1);

            // Assert
            assertTrue(result);
            List<String> orders = systemController.getAllOrders();
            assertFalse(orders.isEmpty());
        }
    }

    @Test
    void testInsertOrder_NonExistentSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            boolean result = systemController.insertOrder("NONEXISTENT", LocalDate.now(), LocalDate.now().plusDays(7),
                    new HashMap<>(), "Contact", "123", 0, 1);

            // Assert
            assertFalse(result);
        }
    }

    // ===================== UTILITY AND HELPER TESTS =====================

    @Test
    void testGetTotalPrice_NonExistentSupplier() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Act
            double result = systemController.getTotalPrice("NONEXISTENT", new HashMap<>(), 0);

            // Assert
            assertEquals(-1, result);
        }
    }

    @Test
    void testGetTotalPrice_InvalidAgreementIndex() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("GTP");
            systemController.addSupplierWithDelivery("PriceSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));

            // Act
            double result = systemController.getTotalPrice(supplierId, new HashMap<>(), 999);

            // Assert
            assertEquals(-1, result);
        }
    }

    @Test
    void testGetTotalPrice_EmptyItems() {
        try (MockedConstruction<SupplierRepository> mockConstruction = mockConstruction(SupplierRepository.class)) {
            // Arrange
            String supplierId = getUniqueId("GTP");
            systemController.addSupplierWithDelivery("PriceSupplier", supplierId, "123", "1", Arrays.asList("Contact,123"));
            systemController.createAgreement(supplierId, 0, 0, LocalDate.now(), LocalDate.now().plusDays(30), new HashMap<>());

            // Act
            double result = systemController.getTotalPrice(supplierId, new HashMap<>(), 0);

            // Assert - Empty items should return 0, not -1
            assertEquals(0.0, result, 0.01);
        }
    }
}