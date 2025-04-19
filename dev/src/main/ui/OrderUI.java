package src.main.ui;

import java.util.Scanner;

import src.main.controllers.OrderController;

public class OrderUI {
    private OrderController orderController;
    private Scanner scanner;
    
    // Constructor
    public OrderUI(OrderController orderController) {
        this.orderController = orderController;
        this.scanner = new Scanner(System.in);
    }
    
    // Methods
    public void showOrderMenu() {
        // Implementation here
    }
    public void createNewOrder() {
        // Implementation here
    }
    public void displayAllOrders() {
        // Implementation here
    }
    public void displayOrderDetails(String id) {
        // Implementation here
    }
    public void updateOrderStatus() {
        // Implementation here
    }
    public void assignTransportToOrder() {
        // Implementation here
    }
    public void cancelOrder() {
        // Implementation here
    }
    public void viewOrdersByDate() {
        // Implementation here
    }
    public void viewOrdersByStatus() {
        // Implementation here
    }
}
