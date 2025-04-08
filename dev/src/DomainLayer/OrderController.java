package src.DomainLayer;

import src.DomainLayer.Enums.STATUS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {
    public static OrderController instance;
    private Map<Integer, Order> orders;
    private int nextOrderId;

    public static OrderController getInstance() {
        if (instance == null)
            instance = new OrderController();
        return instance;
    }

    private OrderController() {
        this.orders = new HashMap<>();
        this.nextOrderId = 1; // Start with order ID 1
    }

    public Order createOrder(int supplierId, LocalDate supplyDate, Map<Integer, Integer> items) {
        Order order = new Order(nextOrderId, supplierId, LocalDate.now(), supplyDate, items, STATUS.IN_PROCESS);
        orders.put(nextOrderId, order);
        nextOrderId++;
        return order;
    }

    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }

    public List<Order> getOrdersBySupplier(int supplierId) {
        List<Order> supplierOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getSupplierId() == supplierId) {
                supplierOrders.add(order);
            }
        }
        return supplierOrders;
    }

    public boolean updateOrderStatus(int orderId, STATUS status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            return true;
        }
        return false;
    }

    public boolean addItemToOrder(int orderId, int catalogNumber, int quantity) {
        Order order = orders.get(orderId);
        if (order != null) {
            Map<Integer, Integer> items = order.getItems();
            items.put(catalogNumber, items.getOrDefault(catalogNumber, 0) + quantity);
            return true;
        }
        return false;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public boolean removeOrder(int orderId) {
        return orders.remove(orderId) != null;
    }
}