package src.main.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import src.main.controllers.OrderController;
import src.main.controllers.SiteController;
import src.main.controllers.TransportController;
import src.main.entities.Item;
import src.main.entities.Order;
import src.main.entities.Site;
import src.main.enums.OrderStatus;

public class OrderUI {
    private Scanner scanner;
    private OrderController orderController;
    private SiteController siteController;
    private TransportController transportController;

    // Constructor
    public OrderUI(OrderController orderController, SiteController siteController, TransportController transportController) {
        this.scanner = new Scanner(System.in);
        this.orderController = orderController;
        this.siteController = siteController;
        this.transportController = transportController;
    }
    
    // Methods
    public void start() {
        boolean exit = false;

        while (!exit) {
            displayMenu();
            int choice = getIntInput("Select an option: ");

            switch (choice) {
                case 1:
                    createNewOrder();
                    break;
                case 2:
                    viewOrder();
                    break;
                case 3:
                    viewAllOrders();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 5:
                    viewOrdersByDate();
                    break;
                case 6:
                    viewOrdersByStatus();
                    break;
                case 7:
                    updateOrderStatus();
                    break;
                case 8:
                    assignTransportToOrder();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Order Management ===");
        System.out.println("1. Create New Order");
        System.out.println("2. View Order");
        System.out.println("3. View All Orders");
        System.out.println("4. Cancel Order");
        System.out.println("5. View Orders by Date");
        System.out.println("6. View Orders by Status");
        System.out.println("7. Update Order Status");
        System.out.println("8. Add Order to Transport");
        System.out.println("0. Return");
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Clean buffer
        return input;
    }

    public void createNewOrder() {
        System.out.println("\n=== Create New Order ===");

        // Get date
        String dateStr = getStringInput("Enter order date (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("Cannot select a date in the past.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        // Select site
        List<Site> sites = siteController.getAllSites();
        if (sites.isEmpty()) {
            System.out.println("No sites in the system. Cannot create an order.");
            return;
        }
        System.out.println("Select site:");
        for (int i = 0; i < sites.size(); i++) {
            System.out.println((i + 1) + ". " + sites.get(i).getName());
        }

        int siteChoice = getIntInput("Your choice: ") - 1;
        if (siteChoice < 0 || siteChoice >= sites.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Site selectedSite = sites.get(siteChoice);

        // Enter items
        List<Item> items = new java.util.ArrayList<>();
        boolean addMore = true;
        int itemId = 1;

        while (addMore) {
            System.out.println("\n--- Item #" + itemId + " ---");
            String name = getStringInput("Item name: ");
            int quantity = getIntInput("Quantity: ");
            double weight = getDoubleInput("Weight per unit (kg): ");

            items.add(new Item(itemId++, name, quantity, weight));

            String more = getStringInput("Add another item? (yes/no): ");
            addMore = more.equalsIgnoreCase("yes");
        }

        Order newOrder = orderController.createOrder(date, selectedSite.getId(), items);
        if (newOrder != null) {
            System.out.println("Order created successfully! ID: " + newOrder.getId());
        } else {
            System.out.println("Error creating order.");
        }
    }

    private void viewOrder() {
        int id = getIntInput("Enter order ID: ");
        Order order = orderController.getOrderById(id);
        if (order != null) {
            System.out.println(order);
        } else {
            System.out.println("Order not found.");
        }
    }

    private void viewAllOrders() {
        List<Order> orders = orderController.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders in the system.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
                System.out.println("---------------------");
            }
        }
    }

    private void cancelOrder() {
        int id = getIntInput("Enter order ID to cancel: ");
        boolean success = orderController.cancelOrder(id);
        if (success) {
            System.out.println("Order cancelled successfully.");
        } else {
            System.out.println("Cannot cancel the order.");
        }
    }

    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // Clean buffer
        return input;
    }

    public void updateOrderStatus() {
        int id = getIntInput("Enter order ID: ");
        Order order = orderController.getOrderById(id);
        
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        
        System.out.println("Current status: " + order.getStatus());
        System.out.println("Select new status:");
        
        for (int i = 0; i < OrderStatus.values().length; i++) {
            System.out.println((i + 1) + ". " + OrderStatus.values()[i]);
        }
        
        int statusChoice = getIntInput("Your choice: ");
        if (statusChoice < 1 || statusChoice > OrderStatus.values().length) {
            System.out.println("Invalid selection.");
            return;
        }
        
        OrderStatus newStatus = OrderStatus.values()[statusChoice - 1];
        boolean updated = orderController.updateOrderStatus(id, newStatus);
        
        if (updated) {
            System.out.println("Status updated successfully.");
        } else {
            System.out.println("Error updating status.");
        }
    }
    
    public void assignTransportToOrder() {
        int orderId = getIntInput("Enter order ID: ");
        Order order = orderController.getOrderById(orderId);
        
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            System.out.println("Cannot assign a " + order.getStatus() + " order to transport.");
            return;
        }
        
        int transportId = getIntInput("Enter transport ID: ");
        boolean success = orderController.assignOrderToTransport(orderId, transportId);
        
        if (success) {
            System.out.println("Order assigned to transport successfully.");
        } else {
            System.out.println("Error assigning order to transport.");
        }
    }

    private void changeTruckByChoice(int transportId) {
        String truckId = getStringInput("Enter new truck license number: ");
        transportController.changeTruck(transportId, truckId);
    }

    private void removeDestinationByChoice(int transportId) {
        // Get destinations from transport
        List<Site> destinations = transportController.getTransportById(transportId).getDestinations();
        
        if (destinations.isEmpty()) {
            System.out.println("This transport has no destinations to remove.");
            return;
        }
        
        System.out.println("Select destination to remove:");
        for (int i = 0; i < destinations.size(); i++) {
            System.out.println((i + 1) + ". " + destinations.get(i).getName());
        }
        
        int choice = getIntInput("Your choice: ") - 1;
        if (choice < 0 || choice >= destinations.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        transportController.removeDestination(transportId, destinations.get(choice).getId());
    }

    public void viewOrdersByDate() {
        String dateStr = getStringInput("Enter date (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }
        
        List<Order> orders = orderController.getOrdersByDate(date);
        
        if (orders.isEmpty()) {
            System.out.println("No orders on this date.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
                System.out.println("---------------------");
            }
        }
    }

    public void viewOrdersByStatus() {
        System.out.println("Select status:");
        for (int i = 0; i < OrderStatus.values().length; i++) {
            System.out.println((i + 1) + ". " + OrderStatus.values()[i]);
        }
        
        int statusChoice = getIntInput("Your choice: ");
        if (statusChoice < 1 || statusChoice > OrderStatus.values().length) {
            System.out.println("Invalid selection.");
            return;
        }
        
        OrderStatus status = OrderStatus.values()[statusChoice - 1];
        List<Order> orders = orderController.getOrdersByStatus(status);
        
        if (orders.isEmpty()) {
            System.out.println("No orders with status " + status);
        } else {
            System.out.println("Orders with status " + status + ":");
            for (Order order : orders) {
                System.out.println(order);
                System.out.println("---------------------");
            }
        }
    }

    public void removeItemsInOrder(int orderId, int transportId) {
        Order order = orderController.getOrderById(orderId);
        
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        
        List<Item> items = order.getItems();
        if (items.isEmpty()) {
            System.out.println("This order has no items.");
            return;
        }
        
        System.out.println("Select items to remove from transport (enter numbers separated by commas):");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " (Quantity: " + item.getQuantity() + ")");
        }
        
        String input = getStringInput("Your selections: ");
        String[] selections = input.split(",");
        List<Integer> selectedItems = new ArrayList<>();
        
        for (String selection : selections) {
            try {
                int index = Integer.parseInt(selection.trim()) - 1;
                if (index >= 0 && index < items.size()) {
                    selectedItems.add(items.get(index).getId());
                }
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        }
        
        if (selectedItems.isEmpty()) {
            System.out.println("No valid items selected.");
            return;
        }
        
        boolean success = orderController.removeItemsFromTransport(orderId, transportId, selectedItems);
        
        if (success) {
            System.out.println("Items removed from transport successfully.");
        } else {
            System.out.println("Error removing items from transport.");
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
