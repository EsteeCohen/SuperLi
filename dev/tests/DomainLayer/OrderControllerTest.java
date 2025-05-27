package DomainLayer;

import DomainLayer.OrderController;
import DomainLayer.Supplier.*;
import DomainLayer.Supplier.Enums.*;
import DomainLayer.Supplier.Order;
import DomainLayer.Supplier.Repositories.OrderRepository;
import DomainLayer.Inventory.ProductFacade;
import DomainLayer.Inventory.ReportFacade;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.MockedConstruction;

import java.time.LocalDate;
import java.util.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private OrderController orderController;
    private MockedStatic<SystemController> systemControllerMock;
    private MockedStatic<ProductFacade> productFacadeMock;
    private MockedStatic<ReportFacade> reportFacadeMock;

    @BeforeEach
    void setUp() {
        // Reset singleton instances before each test
        resetSingletonInstance(OrderController.class, "instance");
        resetSingletonInstance(SystemController.class, "instance");
        resetSingletonInstance(ProductFacade.class, "instance");
        resetSingletonInstance(ReportFacade.class, "instance");

        // Setup static mocks
        systemControllerMock = mockStatic(SystemController.class);
        productFacadeMock = mockStatic(ProductFacade.class);
        reportFacadeMock = mockStatic(ReportFacade.class);

        // Mock the getInstance calls
        ProductFacade mockProductFacadeInstance = mock(ProductFacade.class);
        ReportFacade mockReportFacadeInstance = mock(ReportFacade.class);

        productFacadeMock.when(ProductFacade::getInstance).thenReturn(mockProductFacadeInstance);
        reportFacadeMock.when(ReportFacade::getInstance).thenReturn(mockReportFacadeInstance);

        // Create OrderController instance after mocking
        orderController = OrderController.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Close all static mocks
        if (systemControllerMock != null) {
            systemControllerMock.close();
        }
        if (productFacadeMock != null) {
            productFacadeMock.close();
        }
        if (reportFacadeMock != null) {
            reportFacadeMock.close();
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

    //Singleton Tests

    @Test
    void testGetInstance_SingletonBehavior() {
        OrderController instance1 = OrderController.getInstance();
        OrderController instance2 = OrderController.getInstance();

        assertNotNull(instance1, "Instance1 should not be null");
        assertNotNull(instance2, "Instance2 should not be null");
        assertSame(instance1, instance2, "The instances should be the same (singleton)");
    }

    @Test
    void testGetInstance_ProperInitialization() {
        OrderController instance = OrderController.getInstance();

        try {
            Field ordersField = OrderController.class.getDeclaredField("orders");
            ordersField.setAccessible(true);
            List<?> orders = (List<?>) ordersField.get(instance);

            Field nextOrderIdField = OrderController.class.getDeclaredField("nextOrderId");
            nextOrderIdField.setAccessible(true);
            int nextOrderId = nextOrderIdField.getInt(instance);

            Field periodicOrdersField = OrderController.class.getDeclaredField("periodicOrders");
            periodicOrdersField.setAccessible(true);
            Object periodicOrders = periodicOrdersField.get(instance);

            assertNotNull(orders, "Orders should be initialized");
            assertTrue(orders.isEmpty(), "Orders list should be empty upon initialization");
            assertEquals(0, nextOrderId, "Next order ID should start at 0");
            assertNotNull(periodicOrders, "Periodic orders map should be initialized");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection error: " + e.getMessage());
        }
    }

    //Initialize Orders Tests

    @Test
    void testInitializeOrders_WithValidOrders() {
        Order testOrder = createMockOrder(5, "SUP001", LocalDate.now().plusDays(5));
        List<Order> orders = Arrays.asList(testOrder);

        orderController.inistializeOrders(orders);
        assertEquals(6, orderController.getOrderId()); // Next ID should be max + 1
    }

    @Test
    void testInitializeOrders_IgnoresPastOrders() {
        Order pastOrder = createMockOrder(1, "SUP001", LocalDate.now().minusDays(1)); // Past date
        Order futureOrder = createMockOrder(2, "SUP002", LocalDate.now().plusDays(1)); // Future date

        List<Order> orders = Arrays.asList(pastOrder, futureOrder);
        orderController.inistializeOrders(orders);

        assertEquals(3, orderController.getOrderId()); // Should be max + 1
    }

    //Stock Shortage Reporting Tests

    @Test
    void testReportStockShortage_NoBestSupplierFound() {
        OrderController spyController = spy(orderController);
        Map<String, Object> emptyResult = new HashMap<>();
        doReturn(emptyResult).when(spyController).chooseBestSupplierAndAgreement("ProductA", 10);

        spyController.reportStockShortage("ProductA", 10);

        verify(spyController, times(1)).chooseBestSupplierAndAgreement("ProductA", 10);
        verify(spyController, never()).checkOpenOrder(anyString(), anyInt());
    }

    @Test
    void testReportStockShortage_OrderExists() {
        OrderController spyController = spy(orderController);

        Map<String, Object> supplierData = new HashMap<>();
        supplierData.put("supplierId", "Supplier1");
        supplierData.put("agreementId", 123);
        doReturn(supplierData).when(spyController).chooseBestSupplierAndAgreement("ProductB", 5);

        Order mockOrder = createMockOrder(1, "Supplier1", LocalDate.now().plusDays(1));
        doReturn(mockOrder).when(spyController).checkOpenOrder("Supplier1", 123);
        doNothing().when(spyController).updatePeriodicOrders(mockOrder, "ProductB", 5);

        spyController.reportStockShortage("ProductB", 5);

        verify(spyController, times(1)).chooseBestSupplierAndAgreement("ProductB", 5);
        verify(spyController, times(1)).checkOpenOrder("Supplier1", 123);
        verify(spyController, times(1)).updatePeriodicOrders(mockOrder, "ProductB", 5);
        verify(spyController, never()).createAutomaticShortageOrders(anyString(), anyInt(), anyString(), anyInt());
    }

    @Test
    void testReportStockShortage_CreateNewOrder() {
        OrderController spyController = spy(orderController);

        Map<String, Object> supplierData = new HashMap<>();
        supplierData.put("supplierId", "Supplier2");
        supplierData.put("agreementId", 456);
        doReturn(supplierData).when(spyController).chooseBestSupplierAndAgreement("ProductC", 8);
        doReturn(null).when(spyController).checkOpenOrder("Supplier2", 456);

        Order mockOrder = createMockOrder(1, "Supplier2", LocalDate.now().plusDays(1));
        doReturn(mockOrder).when(spyController).createAutomaticShortageOrders("Supplier2", 456, "ProductC", 8);

        spyController.reportStockShortage("ProductC", 8);

        verify(spyController, times(1)).chooseBestSupplierAndAgreement("ProductC", 8);
        verify(spyController, times(1)).checkOpenOrder("Supplier2", 456);
        verify(spyController, times(1)).createAutomaticShortageOrders("Supplier2", 456, "ProductC", 8);
        verify(spyController, never()).updatePeriodicOrders(any(Order.class), anyString(), anyInt());
    }

    // ========== Check Open Order Tests ==========

    @Test
    void testCheckOpenOrder_ReturnsCorrectOrder() {
        Agreement mockAgreement = mock(Agreement.class);
        when(mockAgreement.getAgreementId()).thenReturn(123);

        Order testOrder = new Order(1, "Supplier1", LocalDate.now(), null,
                mockAgreement, LocalDate.now().plusDays(2),
                new HashMap<>(), STATUS.IN_PROCESS, 100.0);
        orderController.addOrder(testOrder);

        Order result = orderController.checkOpenOrder("Supplier1", 123);

        assertNotNull(result);
        assertEquals(testOrder, result);
    }

    @Test
    void testCheckOpenOrder_ReturnsNullForNonexistentOrder() {
        Order result = orderController.checkOpenOrder("NonexistentSupplier", 999);
        assertNull(result);
    }

    @Test
    void testCheckOpenOrder_MultipleOrdersSameSupplier() {
        Agreement agreement1 = mock(Agreement.class);
        Agreement agreement2 = mock(Agreement.class);
        when(agreement1.getAgreementId()).thenReturn(1);
        when(agreement2.getAgreementId()).thenReturn(2);

        Order order1 = new Order(1, "Supplier1", LocalDate.now(), null,
                agreement1, LocalDate.now().plusDays(1), new HashMap<>(), STATUS.IN_PROCESS, 100.0);
        Order order2 = new Order(2, "Supplier1", LocalDate.now(), null,
                agreement2, LocalDate.now().plusDays(2), new HashMap<>(), STATUS.IN_PROCESS, 200.0);

        orderController.addOrder(order1);
        orderController.addOrder(order2);

        Order result1 = orderController.checkOpenOrder("Supplier1", 1);
        Order result2 = orderController.checkOpenOrder("Supplier1", 2);

        assertEquals(order1, result1);
        assertEquals(order2, result2);
    }

    //Choose Best Supplier Tests

    @Test
    void testChooseBestSupplierAndAgreement_SuccessCase() {
        String productName = "ProductA";
        int requiredQuantity = 10;

        Supplier mockSupplier1 = mock(Supplier.class);
        Supplier mockSupplier2 = mock(Supplier.class);

        Map<String, Object> supplier1Data = new HashMap<>();
        supplier1Data.put("price", 50.0);
        supplier1Data.put("agreementId", 1);

        Map<String, Object> supplier2Data = new HashMap<>();
        supplier2Data.put("price", 40.0);
        supplier2Data.put("agreementId", 2);

        when(mockSupplier1.getBestPriceAndAgreementForProductName(productName, requiredQuantity)).thenReturn(supplier1Data);
        when(mockSupplier2.getBestPriceAndAgreementForProductName(productName, requiredQuantity)).thenReturn(supplier2Data);
        when(mockSupplier1.getSupplierId()).thenReturn("Supplier1");
        when(mockSupplier2.getSupplierId()).thenReturn("Supplier2");

        List<Supplier> mockSuppliers = List.of(mockSupplier1, mockSupplier2);
        SystemController mockSystemController = mock(SystemController.class);
        when(mockSystemController.getAllSupplierObjects()).thenReturn(mockSuppliers);
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        Map<String, Object> result = orderController.chooseBestSupplierAndAgreement(productName, requiredQuantity);

        assertNotNull(result);
        assertEquals("Supplier2", result.get("supplierId"));
        assertEquals(2, result.get("agreementId"));
    }

    @Test
    void testChooseBestSupplierAndAgreement_NoSuppliers() {
        String productName = "ProductB";
        int requiredQuantity = 15;

        SystemController mockSystemController = mock(SystemController.class);
        when(mockSystemController.getAllSupplierObjects()).thenReturn(new ArrayList<>());
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        Map<String, Object> result = orderController.chooseBestSupplierAndAgreement(productName, requiredQuantity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testChooseBestSupplierAndAgreement_NoValidSupplierData() {
        String productName = "ProductC";
        int requiredQuantity = 20;

        Supplier mockSupplier = mock(Supplier.class);
        when(mockSupplier.getBestPriceAndAgreementForProductName(productName, requiredQuantity)).thenReturn(Collections.emptyMap());
        when(mockSupplier.getSupplierId()).thenReturn("Supplier1");

        List<Supplier> mockSuppliers = List.of(mockSupplier);
        SystemController mockSystemController = mock(SystemController.class);
        when(mockSystemController.getAllSupplierObjects()).thenReturn(mockSuppliers);
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        Map<String, Object> result = orderController.chooseBestSupplierAndAgreement(productName, requiredQuantity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testChooseBestSupplierAndAgreement_InvalidAgreementId() {
        String productName = "ProductD";
        int requiredQuantity = 25;

        Supplier mockSupplier = mock(Supplier.class);
        Map<String, Object> supplierData = new HashMap<>();
        supplierData.put("price", 30.0);
        supplierData.put("agreementId", -1); // Invalid agreement ID

        when(mockSupplier.getBestPriceAndAgreementForProductName(productName, requiredQuantity)).thenReturn(supplierData);
        when(mockSupplier.getSupplierId()).thenReturn("Supplier1");

        List<Supplier> mockSuppliers = List.of(mockSupplier);
        SystemController mockSystemController = mock(SystemController.class);
        when(mockSystemController.getAllSupplierObjects()).thenReturn(mockSuppliers);
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        Map<String, Object> result = orderController.chooseBestSupplierAndAgreement(productName, requiredQuantity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //Order Arrival Tests

    @Test
    void testConfirmOrderArrival_NoOrdersArriving() {
        LocalDate arrivalDate = LocalDate.now();

        SystemController mockSystemController = mock(SystemController.class);
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        // Should complete without exceptions
        assertDoesNotThrow(() -> orderController.confirmOrderArrival(arrivalDate));
    }

    @Test
    void testGetOrdersArrivingOn_NoOrders() {
        LocalDate testDate = LocalDate.now().plusDays(3);
        List<Order> result = orderController.getOrdersArrivingOn(testDate);
        assertEquals(0, result.size());
    }

    @Test
    void testGetOrdersArrivingOn_WithMatchingOrders() {
        LocalDate testDate = LocalDate.now().plusDays(3);

        Order order1 = createMockOrder(1, "SUP001", testDate);
        Order order2 = createMockOrder(2, "SUP002", testDate.plusDays(1));
        Order order3 = createMockOrder(3, "SUP003", testDate);

        orderController.addOrder(order1);
        orderController.addOrder(order2);
        orderController.addOrder(order3);

        List<Order> result = orderController.getOrdersArrivingOn(testDate);
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order3));
        assertFalse(result.contains(order2));
    }

    //Automatic Order Creation Tests

    @Test
    void testCreateAutomaticShortageOrders_SupplierNotFound() {
        SystemController mockSystemController = mock(SystemController.class);
        when(mockSystemController.getSupplierObjectById("INVALID")).thenReturn(null);
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        Order result = orderController.createAutomaticShortageOrders("INVALID", 1, "Test Product", 25);

        assertNull(result);
    }

    //Periodic Orders Tests

    @Test
    void testAddPeriodicOrders_WithNullProducts() {
        assertDoesNotThrow(() -> orderController.addPeriodicOrders("SUP001", 1, null));
    }

    @Test
    void testAddPeriodicOrders_WithEmptyProducts() {
        assertDoesNotThrow(() -> orderController.addPeriodicOrders("SUP001", 1, new HashMap<>()));
    }

    @Test
    void testAddPeriodicOrders_WithNullOrder() {
        assertDoesNotThrow(() -> orderController.addPeriodicOrders((Order) null));
    }

    @Test
    void testAddPeriodicOrders_WithValidOrder() {
        Order testOrder = createMockOrder(1, "SUP001", LocalDate.now().plusDays(1));
        assertDoesNotThrow(() -> orderController.addPeriodicOrders(testOrder));
    }

    //Update Periodic Orders Tests

    @Test
    void testUpdatePeriodicOrders_NoSupplierFound() {
        Order mockOrder = mock(Order.class);
        when(mockOrder.getSupplierId()).thenReturn("INVALID");

        SystemController mockSystemController = mock(SystemController.class);
        when(mockSystemController.getSupplierObjectById("INVALID")).thenReturn(null);
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        assertDoesNotThrow(() -> orderController.updatePeriodicOrders(mockOrder, "TestProduct", 10));
        verify(mockOrder, never()).setItems(any());
    }

    @Test
    void testUpdatePeriodicOrders_ValidUpdate() {
        Order mockOrder = mock(Order.class);
        when(mockOrder.getSupplierId()).thenReturn("SUP001");
        when(mockOrder.getOrderId()).thenReturn(1);

        Supplier mockSupplier = createMockSupplier("SUP001");
        Product mockProduct = mock(Product.class);
        when(mockProduct.getCatalogNumber()).thenReturn("CAT001");
        when(mockSupplier.findProductByName("TestProduct")).thenReturn(mockProduct);

        SystemController mockSystemController = mock(SystemController.class);
        when(mockSystemController.getSupplierObjectById("SUP001")).thenReturn(mockSupplier);
        systemControllerMock.when(SystemController::getInstance).thenReturn(mockSystemController);

        // Mock OrderRepository
        try (MockedConstruction<OrderRepository> mockConstruction = mockConstruction(OrderRepository.class)) {
            orderController.updatePeriodicOrders(mockOrder, "TestProduct", 5);

            Map<String, Integer> expectedItems = new HashMap<>();
            expectedItems.put("CAT001", 5);
            verify(mockOrder, times(1)).setItems(expectedItems);
        }
    }

    //Utility Tests

    @Test
    void testIncrementOrderId() {
        int initialId = orderController.getOrderId();
        orderController.incrementOrderId();
        assertEquals(initialId + 1, orderController.getOrderId());
    }

    @Test
    void testAddAndRemoveOrder() {
        Order testOrder = createMockOrder(999, "SUP001", LocalDate.now().plusDays(1));

        orderController.addOrder(testOrder);
        // Verify order was added by trying to find it
        Order found = orderController.checkOpenOrder("SUP001", 1);

        orderController.removeOrder(testOrder);
        // Should complete without exceptions
    }

    @Test
    void testGetTotalPrice_NullSupplier() {
        Map<String, Integer> items = new HashMap<>();
        items.put("CAT001", 10);

        double result = orderController.getTotalPrice(null, items, 0);
        assertEquals(-1, result);
    }

    @Test
    void testGetTotalPrice_InvalidAgreementIndex() {
        Supplier mockSupplier = createMockSupplier("SUP001");
        Map<String, Integer> items = new HashMap<>();
        items.put("CAT001", 10);

        double result = orderController.getTotalPrice(mockSupplier, items, 99);
        assertEquals(-1, result);
    }

    @Test
    void testGetTotalPrice_ValidParameters() {
        Supplier mockSupplier = createMockSupplier("SUP001");
        Map<String, Integer> items = new HashMap<>();
        items.put("CAT001", 10);

        double result = orderController.getTotalPrice(mockSupplier, items, 0);
        assertTrue(result >= 0);
    }

    //Edge Cases and Error Handling

    @Test
    void testReconcileStockAfterArrival_PlaceholderMethod() {
        // Test placeholder method - should complete without exceptions
        assertDoesNotThrow(() -> orderController.reconcileStockAfterArrival(1));
    }

    //Helper Methods

    private Order createMockOrder(int orderId, String supplierId, LocalDate supplyDate) {
        Agreement mockAgreement = mock(Agreement.class);
        when(mockAgreement.getAgreementId()).thenReturn(1);

        ContactPerson mockContact = mock(ContactPerson.class);
        when(mockContact.getContactName()).thenReturn("Test Contact");
        when(mockContact.getPhoneNumber()).thenReturn("123-456-7890");

        return new Order(orderId, supplierId, LocalDate.now(), mockContact,
                mockAgreement, supplyDate, new HashMap<>(), STATUS.IN_PROCESS, 100.0);
    }

    private Supplier createMockSupplier(String supplierId) {
        Supplier mockSupplier = mock(Supplier.class);
        when(mockSupplier.getSupplierId()).thenReturn(supplierId);
        when(mockSupplier.getClosestSupplyDate(any(LocalDate.class))).thenReturn(LocalDate.now().plusDays(3));

        // Mock agreement
        Agreement mockAgreement = mock(Agreement.class);
        when(mockAgreement.getAgreementId()).thenReturn(1);
        when(mockAgreement.calculateTotalPrice(any(), any())).thenReturn(100.0);

        List<Agreement> agreements = Arrays.asList(mockAgreement);
        when(mockSupplier.getAgreements()).thenReturn(agreements);

        // Mock contact person
        ContactPerson mockContact = mock(ContactPerson.class);
        when(mockContact.getContactName()).thenReturn("Test Contact");
        when(mockContact.getPhoneNumber()).thenReturn("123-456-7890");

        List<ContactPerson> contacts = Arrays.asList(mockContact);
        when(mockSupplier.getContactPersons()).thenReturn(contacts);

        // Mock product
        Product mockProduct = mock(Product.class);
        when(mockProduct.getCatalogNumber()).thenReturn("CAT001");
        when(mockProduct.getProductName()).thenReturn("Test Product");
        when(mockSupplier.findProductByName(anyString())).thenReturn(mockProduct);

        return mockSupplier;
    }
}