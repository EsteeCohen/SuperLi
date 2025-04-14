package src.ServiceLayer;

import src.DomainLayer.Order;
import src.DomainLayer.OrderController;
import src.DomainLayer.SupplierController;
import src.DomainLayer.Supplier;
import src.DomainLayer.Product;
import src.DomainLayer.Enums.STATUS;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OrderService {
    private static OrderService instance;
    private OrderController orderController;
    private SupplierController supplierController;

    private OrderService() {
        orderController = OrderController.getInstance();
        supplierController = SupplierController.getInstance();
    }

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public Order createOrder(String supplierId, LocalDate supplyDate, Map<Integer, Integer> items) {
        // Verify supplier exists
        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found with ID: " + supplierId);
        }

        // Create new order
        return orderController.createOrder(supplierId, supplyDate, items);
    }

    public Order getOrderById(int orderId) {
        return orderController.getOrderById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderController.getAllOrders();
    }

    public List<Order> getOrdersBySupplier(String supplierId) {
        return orderController.getOrdersBySupplier(supplierId);
    }

    public boolean updateOrderStatus(int orderId, STATUS newStatus) {
        return orderController.updateOrderStatus(orderId, newStatus);
    }

    public boolean addItemToOrder(int orderId, int catalogNumber, int quantity) {
        return orderController.addItemToOrder(orderId, catalogNumber, quantity);
    }

    public boolean removeOrder(int orderId) {
        return orderController.removeOrder(orderId);
    }

    public double calculateOrderTotal(int orderId) {
        Order order = orderController.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        // Using the getTotalPrice method from Order class
        return order.getTotalPrice();
    }
}