package src.DomainLayer;

import src.DomainLayer.Enums.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// DATA ACCESS CLASSES (For storing data in memory)

/**
 * Data repository for suppliers
 */
class SupplierRepository {
    private static SupplierRepository instance;
    private Map<Integer, Supplier> suppliers; // Map of supplierId to Supplier
    private int nextSupplierId = 1000;

    private SupplierRepository() {
        suppliers = new HashMap<>();
    }

    public static synchronized SupplierRepository getInstance() {
        if (instance == null) {
            instance = new SupplierRepository();
        }
        return instance;
    }

    public int generateSupplierId() {
        return nextSupplierId++;
    }

    public void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getSupplierId(), supplier);
    }

    public Supplier getSupplier(int supplierId) {
        return suppliers.get(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public boolean removeSupplier(int supplierId) {
        return suppliers.remove(supplierId) != null;
    }

    public List<Supplier> findSuppliersByName(String name) {
        List<Supplier> result = new ArrayList<>();
        for (Supplier supplier : suppliers.values()) {
            if (supplier.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(supplier);
            }
        }
        return result;
    }

    public void clear() {
        suppliers.clear();
    }
}

/**
 * Data repository for orders
 */
class OrderRepository {
    private static OrderRepository instance;
    private Map<Integer, Order> orders; // Map of orderId to Order
    private Map<Integer, List<Order>> orderHistories; // Map of orderId to OrderHistory list
    private int nextOrderId = 1000;

    private OrderRepository() {
        orders = new HashMap<>();
        orderHistories = new HashMap<>();
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public int generateOrderId() {
        return nextOrderId++;
    }

    public void addOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> getOrdersByCompany(int supplierId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getSupplierId() == supplierId) {
                result.add(order);
            }
        }
        return result;
    }


    public STATUS getOrderStatus(int orderId) {
        List<Order> histories = getOrder(orderId);
        if (histories.isEmpty()) {
            return null;
        }
        // Return status of most recent history entry
        return histories.get(histories.size() - 1).getStatus();
    }

    public void updateOrderStatus(int orderId, STATUS newStatus) {
        Order history = new Order(orderId, newStatus, LocalDate.now());
        addOrderHistory(history);
    }

    public void clear() {
        orders.clear();
        orderHistories.clear();
    }
}

// DOMAIN SERVICES (Business logic)

/**
 * Service for managing suppliers
 */
class SupplierService {
    private static SupplierService instance;
    private SupplierRepository supplierRepository;

    private SupplierService() {
        supplierRepository = SupplierRepository.getInstance();
    }

    public static synchronized SupplierService getInstance() {
        if (instance == null) {
            instance = new SupplierService();
        }
        return instance;
    }

    public Supplier createSupplier(String name, int bankAccount) {
        int supplierId = supplierRepository.generateSupplierId();
        Supplier supplier = new Supplier(name, supplierId, bankAccount);
        supplierRepository.addSupplier(supplier);
        return supplier;
    }

    public Supplier getSupplier(int supplierId) {
        return supplierRepository.getSupplier(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.getAllSuppliers();
    }

    public boolean removeSupplier(int supplierId) {
        return supplierRepository.removeSupplier(supplierId);
    }

    public List<Supplier> findSuppliersByName(String name) {
        return supplierRepository.findSuppliersByName(name);
    }

    public void addContactPersonToSupplier(int supplierId, String name, String email, int phoneNumber) {
        Supplier supplier = supplierRepository.getSupplier(supplierId);
        if (supplier != null) {
            ContactPerson contactPerson = new ContactPerson(name, email, phoneNumber);
            supplier.addContactPerson(contactPerson);
        }
    }

    public void addAgreementToSupplier(int supplierId, Agreement agreement) {
        Supplier supplier = supplierRepository.getSupplier(supplierId);
        if (supplier != null) {
            supplier.addAgreement(agreement);
        }
    }

    public List<Product> getSupplierProducts(int supplierId) {
        Supplier supplier = supplierRepository.getSupplier(supplierId);
        if (supplier != null) {
            return supplier.getAvailableProducts();
        }
        return new ArrayList<>();
    }

    public List<Agreement> getValidAgreements(int supplierId) {
        Supplier supplier = supplierRepository.getSupplier(supplierId);
        if (supplier != null) {
            return supplier.getValidAgreements();
        }
        return new ArrayList<>();
    }
}

/**
 * Service for managing orders
 */
class OrderService {
    private static OrderService instance;
    private OrderRepository orderRepository;
    private SupplierRepository supplierRepository;

    private OrderService() {
        orderRepository = OrderRepository.getInstance();
        supplierRepository = SupplierRepository.getInstance();
    }

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public Order createOrder(int supplierId, LocalDate deliveryDate, int[] items) {
        // Verify supplier exists
        Supplier supplier = supplierRepository.getSupplier(supplierId);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found with ID: " + supplierId);
        }

        // Create new order
        int orderId = orderRepository.generateOrderId();
        Order order = new Order(orderId, supplierId, LocalDate.now(), deliveryDate, items, STATUS.IN_PROCESS);
        orderRepository.addOrder(order);
        return order;
    }

    public Order getOrder(int orderId) {
        return orderRepository.getOrder(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public List<Order> getOrdersBySupplier(int supplierId) {
        return orderRepository.getOrdersByCompany(supplierId);
    }

    public STATUS getOrderStatus(int orderId) {
        return orderRepository.getOrderStatus(orderId);
    }

    public void updateOrderStatus(int orderId, STATUS newStatus) {
        orderRepository.updateOrderStatus(orderId, newStatus);
    }

    public double calculateOrderTotal(int orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        Supplier supplier = supplierRepository.getSupplier(order.getSupplierId());
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found for order: " + orderId);
        }

        double total = 0.0;
        int[] items = order.getItems();
        Map<Integer, Integer> productQuantities = new HashMap<>();

        // Count quantities for each product
        for (int productId : items) {
            productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + 1);
        }

        // Calculate total with quantity discounts
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            // Find product in supplier's catalog
            for (Product product : supplier.getAvailableProducts()) {
                if (product.getProductId() == productId) {
                    total += product.calculatePriceWithDiscount(quantity);
                    break;
                }
            }
        }

        return total;
    }
}

/**
 * Service for generating reports
 */
class ReportService {
    private static ReportService instance;
    private SupplierRepository supplierRepository;
    private OrderRepository orderRepository;

    private ReportService() {
        supplierRepository = SupplierRepository.getInstance();
        orderRepository = OrderRepository.getInstance();
    }

    public static synchronized ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    public List<Map<String, Object>> generateSupplierReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        for (Supplier supplier : supplierRepository.getAllSuppliers()) {
            Map<String, Object> supplierData = new HashMap<>();
            supplierData.put("supplierId", supplier.getSupplierId());
            supplierData.put("name", supplier.getName());
            supplierData.put("bankAccount", supplier.getBankAccount());
            supplierData.put("contactCount", supplier.getContactPersons().size());
            supplierData.put("agreementCount", supplier.getAgreements().size());
            supplierData.put("validAgreementCount", supplier.getValidAgreements().size());

            List<Order> supplierOrders = orderRepository.getOrdersByCompany(supplier.getSupplierId());
            supplierData.put("orderCount", supplierOrders.size());

            report.add(supplierData);
        }
        return report;
    }

    public List<Map<String, Object>> generateProductReport() {
        Map<Integer, Map<String, Object>> productData = new HashMap<>();

        // Collect all products from all suppliers
        for (Supplier supplier : supplierRepository.getAllSuppliers()) {
            for (Product product : supplier.getAvailableProducts()) {
                if (!productData.containsKey(product.getProductId())) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("productId", product.getProductId());
                    data.put("catalogNumber", product.getCatalogNumber());
                    data.put("price", product.getPrice());
                    data.put("suppliers", new ArrayList<Integer>());
                    productData.put(product.getProductId(), data);
                }

                @SuppressWarnings("unchecked")
                List<Integer> suppliers = (List<Integer>) productData.get(product.getProductId()).get("suppliers");
                if (!suppliers.contains(supplier.getSupplierId())) {
                    suppliers.add(supplier.getSupplierId());
                }
            }
        }

        return new ArrayList<>(productData.values());
    }

    public List<Map<String, Object>> generateOrderReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        for (Order order : orderRepository.getAllOrders()) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getOrderId());
            orderData.put("supplierId", order.getSupplierId());
            orderData.put("date", order.getDate());
            orderData.put("deliveryDate", order.getDeliveryDate());
            orderData.put("itemCount", order.getItems().length);
            orderData.put("status", orderRepository.getOrderStatus(order.getOrderId()));

            Supplier supplier = supplierRepository.getSupplier(order.getsupplierId());
            if (supplier != null) {
                orderData.put("supplierName", supplier.getName());
            }

            report.add(orderData);
        }
        return report;
    }
}

// System initialization class
class SystemInitializer {
    private SupplierService supplierService;
    private OrderService orderService;

    public SystemInitializer() {
        supplierService = SupplierService.getInstance();
        orderService = OrderService.getInstance();
    }

    public void initializeSystem() {
        // Create suppliers
        Supplier supplier1 = supplierService.createSupplier("Tnuva Dairy", 123456789);
        Supplier supplier2 = supplierService.createSupplier("Strauss Group", 987654321);
        Supplier supplier3 = supplierService.createSupplier("Osem-Nestle", 456789123);

        // Add contact persons
        supplierService.addContactPersonToSupplier(supplier1.getSupplierId(), "Moshe Cohen", "moshe@tnuva.com", 541234567);
        supplierService.addContactPersonToSupplier(supplier1.getSupplierId(), "Sarah Levi", "sarah@tnuva.com", 541234568);
        supplierService.addContactPersonToSupplier(supplier2.getSupplierId(), "David Miller", "david@strauss.com", 541234569);
        supplierService.addContactPersonToSupplier(supplier3.getSupplierId(), "Rachel Green", "rachel@osem.com", 541234570);

        // Create agreements
        // Tnuva agreement
        Agreement agreement1 = new Agreement(
                supplier1.getSupplierId(),
                PaymentMethod.CASH,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusYears(1),
                false // Not delivered by company
        );
        agreement1.addDeliveryDay(DaysOfTheWeek.SUNDAY);
        agreement1.addDeliveryDay(DaysOfTheWeek.WEDNESDAY);

        // Add products to Tnuva agreement
        Product milk = new Product(supplier1.getSupplierId(), 10001, 1, 10, 0.05, 5.90);
        Product cheese = new Product(supplier1.getSupplierId(), 10002, 2, 5, 0.03, 12.50);
        Product yogurt = new Product(supplier1.getSupplierId(), 10003, 3, 20, 0.1, 4.50);

        agreement1.addProduct(milk);
        agreement1.addProduct(cheese);
        agreement1.addProduct(yogurt);

        supplierService.addAgreementToSupplier(supplier1.getSupplierId(), agreement1);

        // Strauss agreement
        Agreement agreement2 = new Agreement(supplier2.getSupplierId(), PaymentTerms.CASH_ON_DELIVERY,
                now, now.plusMonths(6), true);
        agreement2.addDeliveryDay(DaysOfTheWeek.MONDAY);
        agreement2.addDeliveryDay(DaysOfTheWeek.THURSDAY);

        // Add products to Strauss agreement
        Product chocolate = new Product(supplier2.getSupplierId(), 2001, 2001, 8.90);
        chocolate.addQuantityDiscount(20, 0.06);  // 6% discount for 20+ units

        Product coffee = new Product(supplier2.getSupplierId(), 2002, 2002, 24.90);
        coffee.addQuantityDiscount(5, 0.05);   // 5% discount for 5+ units
        coffee.addQuantityDiscount(10, 0.12);  // 12% discount for 10+ units

        agreement2.addProduct(chocolate);
        agreement2.addProduct(coffee);

        supplierService.addAgreement(supplier2.getSupplierId(), agreement2);

        // Osem agreement
        Agreement agreement3 = new Agreement(supplier3.getSupplierId(), PaymentTerms.CASH_ON_DELIVERY,
                now, now.plusMonths(9), false);
        agreement3.addDeliveryDay(DaysOfTheWeek.TUESDAY);
        agreement3.addDeliveryDay(DaysOfTheWeek.FRIDAY);

        // Add products to Osem agreement
        Product pasta = new Product(supplier3.getSupplierId(), 3001, 3001, 6.50);
        pasta.addQuantityDiscount(10, 0.04);  // 4% discount for 10+ units
        pasta.addQuantityDiscount(30, 0.10);  // 10% discount for 30+ units

        Product snacks = new Product(supplier3.getSupplierId(), 3002, 3002, 9.90);
        snacks.addQuantityDiscount(15, 0.05);  // 5% discount for 15+ units

        Product canned = new Product(supplier3.getSupplierId(), 3003, 3003, 11.20);
        canned.addQuantityDiscount(12, 0.07);  // 7% discount for 12+ units

        agreement3.addProduct(pasta);
        agreement3.addProduct(snacks);
        agreement3.addProduct(canned);

        supplierService.addAgreement(supplier3.getSupplierId(), agreement3);

        // Create some orders
        Map<Integer, Integer> orderItems1 = new HashMap<>();
        orderItems1.put(milk.getProductId(), 30);  // 30 units of milk
        orderItems1.put(cheese.getProductId(), 15); // 15 units of cheese
        Order order1 = orderService.createOrder(supplier1.getSupplierId(), orderItems1, now.plusDays(3));

        Map<Integer, Integer> orderItems2 = new HashMap<>();
        orderItems2.put(chocolate.getProductId(), 25);  // 25 units of chocolate
        orderItems2.put(coffee.getProductId(), 8);      // 8 units of coffee
        Order order2 = orderService.createOrder(supplier2.getSupplierId(), orderItems2, now.plusDays(2));

        Map<Integer, Integer> orderItems3 = new HashMap<>();
        orderItems3.put(pasta.getProductId(), 40);  // 40 units of pasta
        orderItems3.put(snacks.getProductId(), 20); // 20 units of snacks
        orderItems3.put(canned.getProductId(), 15); // 15 units of canned goods
        Order order3 = orderService.createOrder(supplier3.getSupplierId(), orderItems3, now.plusDays(4));

        // Update order statuses
        orderService.updateOrderStatus(order1.getOrderId(), STATUS.IN_PROCESS);
        orderService.updateOrderStatus(order2.getOrderId(), STATUS.DONE);
    }
}