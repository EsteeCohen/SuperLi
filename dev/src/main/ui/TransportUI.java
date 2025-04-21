package src.main.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.main.controllers.TransportController;
import src.main.entities.Site;
import src.main.entities.Transport;
import src.main.enums.ShippingZone;
import src.main.enums.TransportStatus;

public class TransportUI {
    private Scanner scanner;
    private TransportController transportController;
    
    // Constructor
    public TransportUI(TransportController transportController) {
        this.scanner = new Scanner(System.in);
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
                    createTransport();
                    break;
                case 2:
                    viewTransport();
                    break;
                case 3:
                    viewAllTransports();
                    break;
                case 4:
                    viewTransportsByDate();
                    break;
                case 5:
                    viewTransportsByStatus();
                    break;
                case 6:
                    viewTransportsByZone();
                    break;
                case 7:
                    updateTransportsStatus();
                    break;
                case 8:
                    cancelTransport();
                    break;
                case 9:
                    changeDriver();
                    break;
                case 10:
                    changeTruck();
                    break;
                case 11:
                    addDestination();
                    break;
                case 12:
                    removeDestination();
                    break;
                case 0:
                    System.out.println("חוזר לתפריט הראשי...");
                    exit = true;
                    break;
                default:
                    System.out.println("⚠ בחירה לא תקינה, נסה שוב.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n===== ניהול הובלות =====");
        System.out.println("1. יצירת הובלה חדשה");
        System.out.println("2. צפייה בהובלה");
        System.out.println("3. צפייה בכל ההובלות");
        System.out.println("4. צפייה לפי תאריך");
        System.out.println("5. צפייה לפי סטטוס");
        System.out.println("6. צפייה לפי אזור שילוח");
        System.out.println("7. עדכון סטטוס");
        System.out.println("8. ביטול הובלה");
        System.out.println("9. שינוי נהג");
        System.out.println("10. שינוי משאית");
        System.out.println("11. הוספת יעד");
        System.out.println("12. הסרת יעד");
        System.out.println("0. חזרה");
    }

    // Input helpers
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("⚠ אנא הזן מספר תקין.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Clean buffer
        return input;
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void createTransport() {
        // 1. תאריך
        String dateStr = getStringInput("הזן תאריך (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("⚠ לא ניתן לבחור תאריך מהעבר.");
                return;
            }
        } catch (Exception e) {
            System.out.println("⚠ פורמט תאריך שגוי.");
            return;
        }

        // 2. שעה
        String timeStr = getStringInput("הזן שעה (HH:mm): ");
        LocalTime time;
        try {
            time = LocalTime.parse(timeStr);
        } catch (Exception e) {
            System.out.println("⚠ פורמט שעה שגוי.");
            return;
        }

        // 3. משאית
        String truckId = getStringInput("הזן מספר רישוי של משאית: ");

        // 4. נהג
        String driverId = getStringInput("הזן מזהה נהג: ");

        // 5. אתר מקור
        String sourceSiteId = getStringInput("הזן מזהה אתר מקור: ");

        // 6. יעדים
        List<String> destinationIds = new ArrayList<>();
        boolean more = true;
        while (more) {
            String destId = getStringInput("הזן מזהה אתר יעד: ");
            destinationIds.add(destId);

            String answer = getStringInput("להוסיף יעד נוסף? (כן/לא): ");
            more = answer.equalsIgnoreCase("כן");
        }

        // יצירת ההובלה
        Transport transport = transportController.createTransport(date, time, truckId, driverId, sourceSiteId, destinationIds);
        if (transport != null) {
            System.out.println(" ההובלה נוצרה בהצלחה.");
        }
        else {
            System.out.println(" שגיאה ביצירת ההובלה.");
        }
    }

    private void viewTransport() {
        int id = getIntInput("הזן מזהה הובלה: ");
        Transport t = transportController.getTransportById(id);
        if (t != null) {
            System.out.println(t);
        } else {
            System.out.println(" הובלה לא נמצאה.");
        }
    }

    private void viewAllTransports() {
        List<Transport> transports = transportController.getAllTransports();
        if (transports.isEmpty()) {
            System.out.println(" אין הובלות במערכת.");
        } else {
            System.out.println("\n--- רשימת כל ההובלות ---");
            for (Transport t : transports) {
                System.out.println(t);
                System.out.println("------------------------");
            }
        }
    }

    private void viewTransportsByDate() {
        String dateStr = getStringInput("הזן תאריך (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
            List<Transport> transports = transportController.getTransportsByDate(date);
            if (transports.isEmpty()) {
                System.out.println(" אין הובלות בתאריך זה.");
            } else {
                System.out.println(" הובלות בתאריך " + date + ":");
                for (Transport t : transports) {
                    System.out.println(t);
                    System.out.println("---------------------");
                }
            }
        } catch (Exception e) {
            System.out.println(" פורמט תאריך שגוי.");
        }
    }

    private void viewTransportsByStatus() {
        System.out.println("בחר סטטוס:");
        System.out.println("1. PLANNING");
        System.out.println("2. ACTIVE");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");

        int choice = getIntInput("בחירתך: ");
        TransportStatus status;
        switch (choice) {
            case 1: status = TransportStatus.PLANNING; break;
            case 2: status = TransportStatus.ACTIVE; break;
            case 3: status = TransportStatus.COMPLETED; break;
            case 4: status = TransportStatus.CANCELLED; break;
            default:
                System.out.println(" בחירה לא תקינה.");
                return;
        }

        List<Transport> transports = transportController.getTransportsByStatus(status);
        if (transports.isEmpty()) {
            System.out.println(" אין הובלות בסטטוס זה.");
        } else {
            transports.forEach(t -> {
                System.out.println(t);
                System.out.println("----------------");
            });
        }
    }

    private void viewTransportsByZone() {
        System.out.println("בחר אזור שילוח:");
        System.out.println("1. NORTH");
        System.out.println("2. CENTER");
        System.out.println("3. SOUTH");
        System.out.println("4. JERUSALEM");

        int choice = getIntInput("בחירתך: ");
        ShippingZone zone;
        switch (choice) {
            case 1: zone = ShippingZone.NORTH; break;
            case 2: zone = ShippingZone.CENTER; break;
            case 3: zone = ShippingZone.SOUTH; break;
            case 4: zone = ShippingZone.JERUSALEM; break;
            default:
                System.out.println("בחירה לא תקינה.");
                return;
        }

        List<Transport> transports = transportController.getTransportsByZone(zone);
        if (transports.isEmpty()) {
            System.out.println("אין הובלות באזור זה.");
        } else {
            transports.forEach(t -> {
                System.out.println(t);
                System.out.println("----------------");
            });
        }
    }

    private void updateTransportsStatus() {
        int id = getIntInput("הזן מזהה הובלה: ");
        Transport transport = transportController.getTransportById(id);
        if (transport == null) {
            System.out.println(" הובלה לא נמצאה.");
            return;
        }

        System.out.println("סטטוס נוכחי: " + transport.getStatus());
        System.out.println("בחר סטטוס חדש:");
        System.out.println("1. PLANNING");
        System.out.println("2. ACTIVE");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");

        int choice = getIntInput("בחירתך: ");
        TransportStatus newStatus;
        switch (choice) {
            case 1: newStatus = TransportStatus.PLANNING; break;
            case 2: newStatus = TransportStatus.ACTIVE; break;
            case 3: newStatus = TransportStatus.COMPLETED; break;
            case 4: newStatus = TransportStatus.CANCELLED; break;
            default:
                System.out.println(" בחירה לא תקינה.");
                return;
        }

        boolean updated = transportController.updateTransportStatus(id, newStatus);
        System.out.println(updated ? " סטטוס עודכן בהצלחה." : " שגיאה בעדכון הסטטוס.");
    }

    private void changeDriver() {
        int transportId = getIntInput("הזן מזהה הובלה: ");
        Transport transport = transportController.getTransportById(transportId);
        if (transport == null) {
            System.out.println(" הובלה לא נמצאה.");
            return;
        }

        String driverId = getStringInput("הזן מזהה נהג חדש: ");

        boolean success = transportController.changeDriver(transportId, driverId);
        System.out.println(success ? " הנהג עודכן בהצלחה." : " לא ניתן לעדכן את הנהג (אין רישיון מתאים או שהנהג אינו פנוי).");
    }

    private void changeTruck() {
        int transportId = getIntInput("הזן מזהה הובלה: ");
        Transport transport = transportController.getTransportById(transportId);
        if (transport == null) {
            System.out.println(" הובלה לא נמצאה.");
            return;
        }

        String truckId = getStringInput("הזן מספר רישוי של משאית חדשה: ");

        boolean success = transportController.changeTruck(transportId, truckId);
        System.out.println(success ? " המשאית עודכנה בהצלחה." : " לא ניתן לעדכן את המשאית (ייתכן שאינה קיימת או שאינה פנויה).");
    }

    private void addDestination() {
        int transportId = getIntInput("הזן מזהה הובלה: ");
        Transport transport = transportController.getTransportById(transportId);
        if (transport == null) {
            System.out.println(" הובלה לא נמצאה.");
            return;
        }

        String siteId = getStringInput("הזן מזהה אתר יעד חדש שאותו תרצה להוסיף: ");

        boolean success = transportController.addDestination(transportId, siteId);
        System.out.println(success ? " יעד נוסף להובלה בהצלחה." : " לא ניתן להוסיף את היעד (אולי אזור שונה או כבר קיים).");
    }

    private void removeDestination() {
        int transportId = getIntInput("הזן מזהה הובלה: ");
        Transport transport = transportController.getTransportById(transportId);
        if (transport == null) {
            System.out.println(" הובלה לא נמצאה.");
            return;
        }

        List<Site> destinations = transport.getDestinations();
        if (destinations.isEmpty()) {
            System.out.println(" אין יעדים בהובלה זו.");
            return;
        }

        System.out.println("יעדים בהובלה:");
        for (int i = 0; i < destinations.size(); i++) {
            System.out.println((i + 1) + ". " + destinations.get(i).getName() + " (מזהה: " + destinations.get(i).getId() + ")");
        }

        int index = getIntInput("בחר יעד להסרה: ") - 1;
        if (index < 0 || index >= destinations.size()) {
            System.out.println(" בחירה לא תקינה.");
            return;
        }

        String siteId = destinations.get(index).getId();
        boolean removed = transportController.removeDestination(transportId, siteId);
        System.out.println(removed ? " היעד הוסר בהצלחה." : " שגיאה בהסרת היעד.");
    }

    private void cancelTransport() {
        int transportId = getIntInput("הזן מזהה הובלה לביטול: ");
        Transport transport = transportController.getTransportById(transportId);
        if (transport == null) {
            System.out.println(" הובלה לא נמצאה.");
            return;
        }

        if (!transport.canBeCancelled()) {
            System.out.println(" לא ניתן לבטל הובלה שאינה במצב PLANNING.");
            return;
        }

        boolean cancelled = transportController.cancelTransport(transportId);
        System.out.println(cancelled ? " ההובלה בוטלה בהצלחה." : " שגיאה בביטול ההובלה.");
    }

}
