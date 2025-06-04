package src.main.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import transportDev.src.main.controllers.FacadeController;
import transportDev.src.main.entities.Item;
import transportDev.src.main.entities.Order;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.OrderStatus;

public class OrderUI {
    // :)
    private Scanner scanner;
    private FacadeController facadeController;

    // Constructor
    public OrderUI(FacadeController facadeController) {
        this.scanner = new Scanner(System.in);
        this.facadeController = facadeController;
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
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
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
        List<Site> sites = facadeController.getAllSites();
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
        List<Item> items = new ArrayList<>();
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

        Order newOrder = facadeController.createOrder(date, selectedSite.getId(), items);
        if (newOrder != null) {
            System.out.println("Order created successfully! ID: " + newOrder.getId());
        } else {
            System.out.println("Error creating order.");
        }
    }

    private void viewOrder() {
        int id = getIntInput("Enter order ID: ");
        Order order = facadeController.getOrderById(id);
        if (order != null) {
            System.out.println(order);
        } else {
            System.out.println("Order not found.");
        }
    }

    private void viewAllOrders() {
        List<Order> orders = facadeController.getAllOrders();
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
        boolean success = facadeController.cancelOrder(id);
        if (success) {
            System.out.println("Order cancelled successfully.");
        } else {
            System.out.println("Cannot cancel the order.");
        }
    }

    public void updateOrderStatus() {
        int id = getIntInput("Enter order ID: ");
        Order order = facadeController.getOrderById(id);
        
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        
        System.out.println("\nCurrent status: " + order.getStatus());
        System.out.println("\nAvailable statuses:");
        for (OrderStatus status : OrderStatus.values()) {
            if (status != order.getStatus()) {
                System.out.println("- " + status);
            }
        }
        
        String statusStr = getStringInput("Enter new status: ");
        try {
            OrderStatus newStatus = OrderStatus.valueOf(statusStr.toUpperCase());
            if (newStatus == order.getStatus()) {
                System.out.println("New status is same as current status.");
                return;
            }
            
            if (facadeController.updateOrderStatus(id, newStatus)) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Failed to update status.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status.");
        }
    }

    public void assignTransportToOrder() {
        int orderId = getIntInput("Enter order ID: ");
        Order order = facadeController.getOrderById(orderId);
        
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        
        int transportId = getIntInput("Enter transport ID: ");
        
        if (facadeController.assignTransportToOrder(orderId, transportId)) {
            System.out.println("Order assigned to transport successfully.");
        } else {
            System.out.println("Failed to assign order to transport.");
        }
    }

    public void viewOrdersByDate() {
        String dateStr = getStringInput("Enter date (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
            List<Order> orders = facadeController.getOrdersByDate(date);
            if (orders.isEmpty()) {
                System.out.println("No orders found for this date.");
            } else {
                System.out.println("\n=== Orders for " + date + " ===");
                for (Order order : orders) {
                    System.out.println(order);
                    System.out.println("---------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    public void viewOrdersByStatus() {
        System.out.println("\nAvailable statuses:");
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println("- " + status);
        }
        
        String statusStr = getStringInput("Enter status: ");
        try {
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());
            List<Order> orders = facadeController.getOrdersByStatus(status);
            
            if (orders.isEmpty()) {
                System.out.println("No orders with status: " + status);
            } else {
                System.out.println("\n=== Orders with status: " + status + " ===");
                for (Order order : orders) {
                    System.out.println(order);
                    System.out.println("---------------------");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status.");
        }
    }

    public void removeItemsInOrder(int orderId, int transportId) {
        Order order = facadeController.getOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        List<Item> items = order.getItems();
        if (items.isEmpty()) {
            System.out.println("No items in this order.");
            return;
        }

        System.out.println("\nCurrent items in order:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }

        List<Item> itemsToRemove = new ArrayList<>();
        boolean more = true;

        while (more && !items.isEmpty()) {
            int choice = getIntInput("Select item to remove (1-" + items.size() + "): ");
            if (choice < 1 || choice > items.size()) {
                System.out.println("Invalid selection.");
                continue;
            }

            itemsToRemove.add(items.get(choice - 1));
            String moreStr = getStringInput("Remove another item? (yes/no): ");
            more = moreStr.equalsIgnoreCase("yes");
        }

        if (!itemsToRemove.isEmpty()) {
            if (facadeController.removeItems(orderId, transportId, itemsToRemove)) {
                System.out.println("Items removed successfully.");
            } else {
                System.out.println("Failed to remove items.");
            }
        }
    }
}
