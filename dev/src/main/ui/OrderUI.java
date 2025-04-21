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
            int choice = getIntInput("בחר אפשרות: ");

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
                    System.out.println("אפשרות לא תקינה, נסה שנית");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== ניהול הזמנות ===");
        System.out.println("1. יצירת הזמנה חדשה");
        System.out.println("2. צפייה בהזמנה");
        System.out.println("3. צפייה בכל ההזמנות");
        System.out.println("4. ביטול הזמנה");
        System.out.println("5. צפייה בהזמנות לפי תאריך");
        System.out.println("6. צפייה בהזמנות לפי סטטוס");
        System.out.println("7. עדכון סטטוס של הזמנה");
        System.out.println("8. הוספת הזמנה להובלה");
        System.out.println("0. חזרה");
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("אנא הזן מספר תקין.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // ניקוי ה-buffer
        return input;
    }

    public void createNewOrder() {
        System.out.println("\n=== יצירת הזמנה חדשה ===");

        // קבלת תאריך
        String dateStr = getStringInput("הזן תאריך הזמנה (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("לא ניתן לבחור תאריך מהעבר.");
                return;
            }
        } catch (Exception e) {
            System.out.println("פורמט תאריך שגוי.");
            return;
        }

        // בחירת אתר
        List<Site> sites = siteController.getAllSites();
        if (sites.isEmpty()) {
            System.out.println("אין אתרים במערכת. לא ניתן ליצור הזמנה.");
            return;
        }
        System.out.println("בחר אתר:");
        for (int i = 0; i < sites.size(); i++) {
            System.out.println((i + 1) + ". " + sites.get(i).getName());
        }

        int siteChoice = getIntInput("בחירתך: ") - 1;
        if (siteChoice < 0 || siteChoice >= sites.size()) {
            System.out.println("בחירה לא תקינה.");
            return;
        }

        Site selectedSite = sites.get(siteChoice);

        // הזנת פריטים
        List<Item> items = new java.util.ArrayList<>();
        boolean addMore = true;
        int itemId = 1;

        while (addMore) {
            System.out.println("\n--- פריט #" + itemId + " ---");
            String name = getStringInput("שם הפריט: ");
            int quantity = getIntInput("כמות: ");
            double weight = getDoubleInput("משקל ליחידה (בק\"ג): ");

            items.add(new Item(itemId++, name, quantity, weight));

            String more = getStringInput("להוסיף פריט נוסף? (כן/לא): ");
            addMore = more.equalsIgnoreCase("כן");
        }

        Order newOrder = orderController.createOrder(date, selectedSite.getId(), items);
        if (newOrder != null) {
            System.out.println("הזמנה נוצרה בהצלחה! מזהה: " + newOrder.getId());
        } else {
            System.out.println("שגיאה ביצירת ההזמנה.");
        }
    }

    private void viewOrder() {
        int id = getIntInput("הכנס מזהה הזמנה: ");
        Order order = orderController.getOrderById(id);
        if (order != null) {
            System.out.println(order);
        } else {
            System.out.println("ההזמנה לא נמצאה.");
        }
    }

    private void viewAllOrders() {
        List<Order> orders = orderController.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("אין הזמנות במערכת.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
                System.out.println("---------------------");
            }
        }
    }

    private void cancelOrder() {
        int id = getIntInput("הכנס מזהה הזמנה לביטול: ");
        boolean success = orderController.cancelOrder(id);
        if (success) {
            System.out.println("ההזמנה בוטלה בהצלחה.");
        } else {
            System.out.println("לא ניתן לבטל את ההזמנה.");
        }
    }

    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("אנא הזן מספר תקין.");
            System.out.print(prompt);
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // ניקוי ה-buffer
        return input;
    }

    public void updateOrderStatus() {
        int id = getIntInput("הכנס מזהה הזמנה: ");
        Order order = orderController.getOrderById(id);

        if (order == null) {
            System.out.println("הזמנה לא נמצאה.");
            return;
        }

        System.out.println("סטטוס נוכחי: " + order.getStatus());
        System.out.println("בחר סטטוס חדש:");
        System.out.println("1. CREATED");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");

        int choice = getIntInput("בחירתך: ");
        OrderStatus newStatus;
        switch (choice) {
            case 1: newStatus = OrderStatus.CREATED; break;
            case 2: newStatus = OrderStatus.IN_PROGRESS; break;
            case 3: newStatus = OrderStatus.DONE; break;
            case 4: newStatus = OrderStatus.CANCELLED; break;
            default:
                System.out.println("בחירה לא תקינה.");
                return;
        }

        boolean success = orderController.updateOrderStatus(id, newStatus);
        System.out.println(success ? "הסטטוס עודכן בהצלחה." : "שגיאה בעדכון הסטטוס.");
    }

    public void assignTransportToOrder() {
        int orderId = getIntInput("הכנס מזהה הזמנה: ");
        int transportId = getIntInput("הכנס מזהה הובלה לשיוך: ");

        boolean success = orderController.assignTransportToOrder(orderId, transportId);

        while(!success) {
            System.out.println("בעקבות המשקל לא ניתן לשייך את ההזמנה להובלה. בחר פתרון רצוי: ");
            System.out.println("1. הורדת פריטים מההזמנה");
            System.out.println("2. החלפת משאית להובלה");
            System.out.println("3. הורדת יעד מההובלה");

            int choice = getIntInput("בחירתך: ");
            switch (choice) {
                case 1:
                    removeItemsInOrder(orderId,transportId);
                    break;
                case 2:
                    changeTruckByChoice(transportId);
                    break;
                case 3:
                    removeDestinationByChoice(transportId);
                    break;
                default:
                    System.out.println("בחירה לא תקינה.");
                    return;
            }
            success = orderController.assignTransportToOrder(orderId, transportId);
        }

        System.out.println("ההובלה שויכה להזמנה בהצלחה.");
    }

    private void changeTruckByChoice(int transportId) {
        String truckId = getStringInput("הזן מספר רישוי של משאית חלופית: ");
        boolean changed = transportController.changeTruck(transportId, truckId);
        System.out.println(changed ? "המשאית הוחלפה בהצלחה." : "שגיאה בהחלפת המשאית.");
    }

    private void removeDestinationByChoice(int transportId) {
        List<Site> destinations = transportController.getTransportById(transportId).getDestinations();
        if (destinations.isEmpty()) {
            System.out.println("אין יעדים בהובלה.");
            return;
        }

        System.out.println("יעדים בהובלה:");
        for (int i = 0; i < destinations.size(); i++) {
            System.out.println((i + 1) + ". " + destinations.get(i).getName());
        }

        int index = getIntInput("בחר יעד להסרה: ") - 1;
        if (index < 0 || index >= destinations.size()) {
            System.out.println("בחירה לא תקינה.");
            return;
        }

        String siteId = destinations.get(index).getId();
        boolean removed = transportController.removeDestination(transportId, siteId);
        System.out.println(removed ? "היעד הוסר." : "שגיאה בהסרת היעד.");
    }

    public void viewOrdersByDate() {
        String input = getStringInput("הכנס תאריך (yyyy-MM-dd): ");
        try {
            LocalDate date = LocalDate.parse(input);
            List<Order> orders = orderController.getOrdersByDate(date);
            if (orders.isEmpty()) {
                System.out.println("אין הזמנות בתאריך זה.");
            } else {
                orders.forEach(order -> {
                    System.out.println(order);
                    System.out.println("----------------");
                });
            }
        } catch (Exception e) {
            System.out.println("פורמט תאריך שגוי.");
        }
    }

    public void viewOrdersByStatus() {
        System.out.println("בחר סטטוס:");
        System.out.println("1. CREATED");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");

        int choice = getIntInput("בחירתך: ");
        OrderStatus status;
        switch (choice) {
            case 1: status = OrderStatus.CREATED; break;
            case 2: status = OrderStatus.IN_PROGRESS; break;
            case 3: status = OrderStatus.DONE; break;
            case 4: status = OrderStatus.CANCELLED; break;
            default:
                System.out.println("בחירה לא תקינה.");
                return;
        }

        List<Order> orders = orderController.getOrdersByStatus(status);
        if (orders.isEmpty()) {
            System.out.println("אין הזמנות בסטטוס זה.");
        } else {
            orders.forEach(order -> {
                System.out.println(order);
                System.out.println("----------------");
            });
        }
    }


    public void removeItemsInOrder(int orderId, int transportId) {
        Order order = orderController.getOrderById(orderId);
        if (order == null) {
            System.out.println("ההזמנה לא נמצאה.");
            return;
        }

        List<Item> items = new ArrayList<>(order.getItems());
        if (items.isEmpty()) {
            System.out.println("אין פריטים בהזמנה.");
            return;
        }

        List<Item> itemsToRemove = new ArrayList<>();

        boolean done = false;
        while (!done && !items.isEmpty()) {
            System.out.println("\nפריטים בהזמנה:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i));
            }

            int index = getIntInput("בחר מספר פריט להסרה: ") - 1;
            if (index < 0 || index >= items.size()) {
                System.out.println("בחירה לא תקינה.");
                continue;
            }

            Item selected = items.get(index);
            itemsToRemove.add(selected);
            items.remove(index);

            String more = getStringInput("להסיר פריט נוסף? (כן/לא): ");
            done = !more.equalsIgnoreCase("כן");
        }

        if (itemsToRemove.isEmpty()) {
            System.out.println("לא נבחרו פריטים להסרה.");
            return;
        }

        boolean removed = orderController.removeItems(orderId, transportId, itemsToRemove);
        System.out.println(removed ? "הפריטים הוסרו מההזמנה." : "שגיאה בהסרת הפריטים.");
    }


    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
