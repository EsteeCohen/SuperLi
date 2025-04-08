package src.ServiceLayer;

public class OrderService {
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

    public Order createOrder(int companyId, LocalDate deliveryDate, int[] items) {
        // Verify supplier exists
        Supplier supplier = supplierRepository.getSupplier(companyId);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found with ID: " + companyId);
        }

        // Create new order
        int orderId = orderRepository.generateOrderId();
        Order order = new Order(orderId, companyId, LocalDate.now(), deliveryDate, items);
        orderRepository.addOrder(order);
        return order;
    }

    public Order getOrder(int orderId) {
        return orderRepository.getOrder(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public List<Order> getOrdersBySupplier(int companyId) {
        return orderRepository.getOrdersByCompany(companyId);
    }

    public List<OrderHistory> getOrderHistory(int orderId) {
        return orderRepository.getOrderHistory(orderId);
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

        Supplier supplier = supplierRepository.getSupplier(order.getCompanyId());
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
