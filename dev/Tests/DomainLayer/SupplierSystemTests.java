package Tests.DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import src.ServiceLayer.SupplierSystemService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SupplierSystemTests {

    private SupplierSystemService service;

    @BeforeEach
    public void setUp() {
        // Get a new instance for each test
        service = SupplierSystemService.getInstance();
        // Clear existing data (you may want to add a method for this in your system)
        // For now we'll just create new test data for each test
    }

    // Test 1: Add supplier with delivery days
    @Test
    public void testAddSupplierWithDelivery() {
        boolean result = service.addSupplierWithDelivery("Test Supplier", "SUP001", "123456789", "1,3,5");
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
        boolean result = service.addSupplierNeedsPickup("Pickup Supplier", "SUP002", "987654321", "Test Address 123");
        assertTrue(result);

        String suppliersRequiringPickup = service.getSuppliersRequiringPickup();
        assertTrue(suppliersRequiringPickup.contains("SUP002"), "Supplier that needs pickup not found");
    }

    // Test 3: Update supplier information
    @Test
    public void testUpdateSupplier() {
        // First, add a supplier
        service.addSupplierWithDelivery("Original Name", "SUP003", "123456789", "1,3,5");

        // Now update the name
        boolean result = service.updateSupplier("SUP003", "name", "Updated Name");
        assertTrue(result);

        // Verify the update
        List<String> suppliers = service.getAllSuppliers();
        boolean found = false;
        for (String supplier : suppliers) {
            if (supplier.contains("SUP003") && supplier.contains("Updated Name")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Updated supplier name not found");
    }

    // Test 4: Remove supplier
    @Test
    public void testRemoveSupplier() {
        // First, add a supplier
        service.addSupplierWithDelivery("To Remove", "SUP004", "123456789", "1,3,5");

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
        service.addSupplierWithDelivery("Product Supplier", "SUP005", "123456789", "1,3,5");

        // Now add a product
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        boolean result = service.addProduct("SUP005", "PROD001", 10, discounts, 100.0, 0);
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
        service.addSupplierWithDelivery("Product Update Supplier", "SUP006", "123456789", "1,3,5");
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("SUP006", "PROD002", 10, discounts, 100.0, 0);

        // Update the product price
        boolean result = service.updateProduct("SUP006", "PROD002", "price", "150.0");
        assertTrue(result);

        // Verify the update
        String product = service.getProductBySupplierAndCatalog("SUP006", "PROD002");
        assertTrue(product.contains("price: 150.0"), "Updated product price not found");
    }

    // Test 7: Remove product
    @Test
    public void testRemoveProduct() {
        // Add supplier and product first
        service.addSupplierWithDelivery("Product Remove Supplier", "SUP007", "123456789", "1,3,5");
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("SUP007", "PROD003", 10, discounts, 100.0, 0);

        // Remove the product
        boolean result = service.removeProduct("SUP007", "PROD003");
        assertTrue(result);

        // Verify it's gone
        String product = service.getProductBySupplierAndCatalog("SUP007", "PROD003");
        assertNull("Product should be removed but was found", product);
    }

    // Test 8: Create agreement
    @Test
    public void testCreateAgreement() {
        // Add supplier and products first
        service.addSupplierWithDelivery("Agreement Supplier", "SUP008", "123456789", "1,3,5");
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("SUP008", "PROD004", 10, discounts, 100.0, 0);
        service.addProduct("SUP008", "PROD005", 20, discounts, 200.0, 0);

        // Create an agreement
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        List<Integer> productIndices = Arrays.asList(0, 1); // assuming these are valid indices
        boolean result = service.createAgreement("SUP008", 0, 0, validFrom, validTo, productIndices);
        assertTrue(result);

        // Verify the agreement was created
        List<String> agreements = service.getAgreementsBySupplier("SUP008");
        assertFalse(agreements.isEmpty(), "No agreements found");
    }

    // Test 9: Update agreement
    @Test
    public void testUpdateAgreement() {
        // Add supplier, products, and agreement first
        service.addSupplierWithDelivery("Agreement Update Supplier", "SUP009", "123456789", "1,3,5");
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("SUP009", "PROD006", 10, discounts, 100.0, 0);
        service.addProduct("SUP009", "PROD007", 20, discounts, 200.0, 0);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        List<Integer> productIndices = Arrays.asList(0, 1);
        service.createAgreement("SUP009", 0, 0, validFrom, validTo, productIndices);

        // Update payment method
        boolean result = service.updatePaymentMethods("SUP009", 0, 1);
        assertTrue(result);

        // Verify the update
        List<String> agreements = service.getAgreementsBySupplier("SUP009");
        boolean found = false;
        for (String agreement : agreements) {
            // Check if it contains the updated payment method (index 1)
            if (agreement.contains("paymentMethod: " + service.getPaymentMethods().get(1))) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Updated payment method not found in agreement");
    }

    // Test 10: Create order
    @Test
    public void testCreateOrder() {
        // Add supplier, products, and agreement first
        service.addSupplierWithDelivery("Order Supplier", "SUP010", "123456789", "1,3,5");
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("SUP010", "PROD008", 10, discounts, 100.0, 0);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        List<Integer> productIndices = Arrays.asList(0);
        service.createAgreement("SUP010", 0, 0, validFrom, validTo, productIndices);

        // Create an order
        LocalDate orderDate = LocalDate.now();
        LocalDate supplyDate = orderDate.plusDays(7);
        Map<Integer, Integer> items = new HashMap<>();
        items.put(0, 5); // 5 units of the first product in the agreement

        boolean result = service.insertOrder("SUP010", orderDate, supplyDate, items, "Contact Person", "123456789", 0, 0);
        assertTrue(result);

        // Verify the order was created
        List<String> orders = service.getAllOrders();
        assertFalse(orders.isEmpty(), "No orders found");
    }

    // Test 11: Update order status
    @Test
    public void testUpdateOrderStatus() {
        // First create a supplier, product, agreement, and order
        service.addSupplierWithDelivery("Status Update Supplier", "SUP011", "123456789", "1,3,5");
        ArrayList<String> discounts = new ArrayList<>(Arrays.asList("10,5", "20,10"));
        service.addProduct("SUP011", "PROD009", 10, discounts, 100.0, 0);

        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(6);
        List<Integer> productIndices = Arrays.asList(0);
        service.createAgreement("SUP011", 0, 0, validFrom, validTo, productIndices);

        LocalDate orderDate = LocalDate.now();
        LocalDate supplyDate = orderDate.plusDays(7);
        Map<Integer, Integer> items = new HashMap<>();
        items.put(0, 5);
        service.insertOrder("SUP011", orderDate, supplyDate, items, "Contact Person", "123456789", 0, 0);

        // Update order status - need to know the order ID
        // Assuming the first order has ID 0
        boolean result = service.updateOrderStatus(0, 1); // Change to status with index 1
        assertTrue(result);

        // Verify the status update
        List<String> orders = service.getOrdersByStatus(1);
        assertFalse(orders.isEmpty(), "No orders found with updated status");
    }
}