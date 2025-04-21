package src.main.ui;

import java.util.List;
import java.util.Scanner;

import src.main.controllers.DriverController;
import src.main.controllers.TruckController;
import src.main.controllers.UserController;
import src.main.entities.Truck;
import src.main.entities.Driver;
import src.main.entities.User;

public class FleetUI {
    private Scanner scanner;
    private TruckController truckController;
    private DriverController driverController;
    private UserController userController;
    private String sessionId;
    
    //בנאי לממשק ניהול צי רכב
    public FleetUI(TruckController truckController, DriverController driverController, UserController userController) {
        this.scanner = new Scanner(System.in);
        this.truckController = truckController;
        this.driverController = driverController;
        this.userController = userController;
    }
    
    //התחלת ממשק ניהול צי רכב
    public void start() {
//        this.sessionId = sessionId;
        
        boolean exit = false;
        
        while (!exit) {
            displayMainMenu();
            int choice = getIntInput("בחר אפשרות: ");
            
            switch (choice) {
                case 1:
                    if (canManageTrucks()) {
                        truckMenu();
                    } else {
                        showAccessDenied("לניהול משאיות");
                    }
                    break;
                case 2:
                    if (canManageDrivers()) {
                        driverMenu();
                    } else {
                        showAccessDenied("לניהול נהגים");
                    }
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("אפשרות לא תקינה, נסה שנית");
            }
        }
    }
    
    //הצגת תפריט ראשי של ניהול צי רכב
    private void displayMainMenu() {
        System.out.println("\n=== ניהול צי רכב ===");
        System.out.println("1. ניהול משאיות");
        System.out.println("2. ניהול נהגים");
        System.out.println("0. חזרה לתפריט הראשי");
    }
    
    //הצגת תפריט ניהול משאיות
    private void truckMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== ניהול משאיות ===");
            System.out.println("1. הוספת משאית חדשה");
            System.out.println("2. צפייה במשאית");
            System.out.println("3. צפייה בכל המשאיות");
            System.out.println("4. חיפוש משאיות לפי סוג רישיון");
            System.out.println("5. חיפוש משאיות לפי קיבולת");
            System.out.println("0. חזרה לתפריט הקודם");
            
            int choice = getIntInput("בחר אפשרות: ");
            
            switch (choice) {
                case 1:
                    if (userController.isAuthorized(sessionId, "CREATE", "TRUCK")) {
                        addNewTruck();
                    } else {
                        showAccessDenied("להוספת משאית");
                    }
                    break;
                case 2:
                    viewTruck();
                    break;
                case 3:
                    viewAllTrucks();
                    break;
                case 4:
                    searchTrucksByLicenseType();
                    break;
                case 5:
                    searchTrucksByCapacity();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("אפשרות לא תקינה, נסה שנית");
            }
        }
    }
    
    //הצגת תפריט ניהול נהגים
    private void driverMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== ניהול נהגים ===");
            System.out.println("1. הוספת נהג חדש");
            System.out.println("2. צפייה בנהג");
            System.out.println("3. צפייה בכל הנהגים");
            System.out.println("4. חיפוש נהגים לפי סוג רישיון");
            System.out.println("0. חזרה לתפריט הקודם");
            
            int choice = getIntInput("בחר אפשרות: ");
            
            switch (choice) {
                case 1:
                    if (userController.isAuthorized(sessionId, "CREATE", "DRIVER")) {
                        addNewDriver();
                    } else {
                        showAccessDenied("להוספת נהג");
                    }
                    break;
                case 2:
                    viewDriver();
                    break;
                case 3:
                    viewAllDrivers();
                    break;
                case 4:
                    searchDriversByLicenseType();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("אפשרות לא תקינה, נסה שנית");
            }
        }
    }
    
    //הוספת משאית חדשה
    private void addNewTruck() {
        System.out.println("\n=== הוספת משאית חדשה ===");
        
        String regNumber = getStringInput("הזן מספר רישוי: ");
        String model = getStringInput("הזן דגם: ");
        
        double emptyWeight = getDoubleInput("הזן משקל נטו (בק\"ג): ");
        double maxWeight = getDoubleInput("הזן משקל מקסימלי מותר (בק\"ג): ");
        
// ------------- חסרה המרה למחרוזת!!! ---------------

        System.out.println("בחר סוג רישיון נדרש:");
        System.out.println("1. C1 - רישיון למשאיות עד 12 טון");
        System.out.println("2. C - רישיון למשאיות עד 15 טון");
        System.out.println("3. CE - רישיון לרכב מחובר/מורכב");
        System.out.println("4. C1E - רישיון למשאיות קלות עם גרור");
        
        int licenseChoice = getIntInput("בחירתך: ");
        String licenseType;
        
        switch (licenseChoice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("בחירה לא תקינה. מגדיר כברירת מחדל: C");
                licenseType = "C";
        }
        
        Truck newTruck = truckController.addTruck(regNumber, model, ""+emptyWeight, ""+maxWeight, licenseType);
        
        if (newTruck != null) {
            // הוספת המשאית למערכת הצליחה
            System.out.println("המשאית נוספה בהצלחה!");
        } else {
            System.out.println("שגיאה בהוספת המשאית. ייתכן שמספר הרישוי כבר קיים במערכת.");
        }
    }
    
    //צפייה במשאית ספציפית
    private void viewTruck() {
        System.out.println("\n=== צפייה במשאית ===");
        String regNumber = getStringInput("הזן מספר רישוי: ");
        
        Truck truck = truckController.getTruckByRegNumber(regNumber);
        
        if (truck != null) {
            displayTruckDetails(truck);
        } else {
            System.out.println("משאית לא נמצאה במערכת.");
        }
    }
    
    //צפייה בכל המשאיות
    private void viewAllTrucks() {
        System.out.println("\n=== רשימת כל המשאיות ===");
        
        List<Truck> trucks = truckController.getAllTrucks();
        
        if (trucks.isEmpty()) {
            System.out.println("אין משאיות במערכת.");
            return;
        }
        
        for (Truck truck : trucks) {
            displayTruckDetails(truck);
            System.out.println("--------------------");
        }
    }
    
    //חיפוש משאיות לפי סוג רישיון
    private void searchTrucksByLicenseType() {
        System.out.println("\n=== חיפוש משאיות לפי סוג רישיון ===");
        
        System.out.println("בחר סוג רישיון:");
        System.out.println("1. C1 - רישיון למשאיות עד 12 טון");
        System.out.println("2. C - רישיון למשאיות עד 15 טון");
        System.out.println("3. CE - רישיון לרכב מחובר/מורכב");
        System.out.println("4. C1E - רישיון למשאיות קלות עם גרור");
        
        int licenseChoice = getIntInput("בחירתך: ");
        String licenseType;
        
        switch (licenseChoice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("בחירה לא תקינה. מציג משאיות עם רישיון C כברירת מחדל.");
                licenseType = "C";
        }
        
        List<Truck> trucks = truckController.getTrucksByLicenseType(licenseType);
        
        if (trucks.isEmpty()) {
            System.out.println("אין משאיות עם סוג רישיון זה.");
            return;
        }
        
        System.out.println("משאיות עם רישיון " + licenseType + ":");
        for (Truck truck : trucks) {
            displayTruckDetails(truck);
            System.out.println("--------------------");
        }
    }
    
    //חיפוש משאיות לפי קיבולת
    private void searchTrucksByCapacity() {
        System.out.println("\n=== חיפוש משאיות לפי קיבולת ===");
        
        double minCapacity = getDoubleInput("הזן קיבולת מינימלית (בק\"ג): ");

        // ------------- חסרה המרה למחרוזת!!! ---------------
        
        List<Truck> trucks = truckController.getTrucksWithCapacityForWeight(""+minCapacity);
        
        if (trucks.isEmpty()) {
            System.out.println("אין משאיות עם קיבולת מעל " + minCapacity + " ק\"ג.");
            return;
        }
        
        System.out.println("משאיות עם קיבולת מעל " + minCapacity + " ק\"ג:");
        for (Truck truck : trucks) {
            displayTruckDetails(truck);
            System.out.println("--------------------");
        }
    }
    
    // הוספת נהג חדש
    private void addNewDriver() {
        System.out.println("\n=== הוספת נהג חדש ===");
        
        String id = getStringInput("הזן ת\"ז: ");
        String name = getStringInput("הזן שם: ");
        String phone = getStringInput("הזן מספר טלפון: ");
        
        System.out.println("בחר סוג רישיון:");
        System.out.println("1. C1 - רישיון למשאיות עד 12 טון");
        System.out.println("2. C - רישיון למשאיות עד 15 טון");
        System.out.println("3. CE - רישיון לרכב מחובר/מורכב");
        System.out.println("4. C1E - רישיון למשאיות קלות עם גרור");
        
        int licenseChoice = getIntInput("בחירתך: ");
        String licenseType;
        
        switch (licenseChoice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("בחירה לא תקינה. מגדיר כברירת מחדל: C");
                licenseType = "C";
        }
        
        Driver newDriver = driverController.addDriver(id, name, phone, licenseType);
        
        if (newDriver != null) {
            // הוספת הנהג למערכת הצליחה
            System.out.println("הנהג נוסף בהצלחה!");
        } else {
            System.out.println("שגיאה בהוספת הנהג. ייתכן שת\"ז כבר קיימת במערכת.");
        }
    }
    
    //צפייה בנהג ספציפי
    private void viewDriver() {
        System.out.println("\n=== צפייה בנהג ===");
        String id = getStringInput("הזן ת\"ז: ");
        
        Driver driver = driverController.getDriverById(id);
        
        if (driver != null) {
            displayDriverDetails(driver);
        } else {
            System.out.println("נהג לא נמצא במערכת.");
        }
    }
    
    //צפייה בכל הנהגים
    private void viewAllDrivers() {
        System.out.println("\n=== רשימת כל הנהגים ===");
        
        List<Driver> drivers = driverController.getAllDrivers();
        
        if (drivers.isEmpty()) {
            System.out.println("אין נהגים במערכת.");
            return;
        }
        
        for (Driver driver : drivers) {
            displayDriverDetails(driver);
            System.out.println("--------------------");
        }
    }
    
    //חיפוש נהגים לפי סוג רישיון
    private void searchDriversByLicenseType() {
        System.out.println("\n=== חיפוש נהגים לפי סוג רישיון ===");
        
        System.out.println("בחר סוג רישיון:");
        System.out.println("1. C1 - רישיון למשאיות עד 12 טון");
        System.out.println("2. C - רישיון למשאיות עד 15 טון");
        System.out.println("3. CE - רישיון לרכב מחובר/מורכב");
        System.out.println("4. C1E - רישיון למשאיות קלות עם גרור");
        
        int licenseChoice = getIntInput("בחירתך: ");
        String licenseType;
        
        switch (licenseChoice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("בחירה לא תקינה. מציג נהגים עם רישיון C כברירת מחדל.");
                licenseType = "C";
        }
        
        List<Driver> drivers = driverController.getDriversByLicenseType(licenseType);
        
        if (drivers.isEmpty()) {
            System.out.println("אין נהגים עם סוג רישיון זה.");
            return;
        }
        
        System.out.println("נהגים עם רישיון " + licenseType + ":");
        for (Driver driver : drivers) {
            displayDriverDetails(driver);
            System.out.println("--------------------");
        }
    }
    
    //הצגת פרטי משאית
    private void displayTruckDetails(Truck truck) {
        System.out.println("מספר רישוי: " + truck.getRegNumber());
        System.out.println("דגם: " + truck.getModel());
        System.out.println("משקל נטו: " + truck.getEmptyWeight() + " ק\"ג");
        System.out.println("משקל מקסימלי: " + truck.getMaxWeight() + " ק\"ג");
        System.out.println("סוג רישיון נדרש: " + truck.getLicenseType());
    }
    
    //הצגת פרטי נהג
    private void displayDriverDetails(Driver driver) {
        System.out.println("ת\"ז: " + driver.getId());
        System.out.println("שם: " + driver.getName());
        System.out.println("טלפון: " + driver.getPhone());
        System.out.println("סוג רישיון: " + driver.getLicenseType());
    }
    
    //בדיקת הרשאה לניהול משאיות
    private boolean canManageTrucks() {
        return userController.isAuthorized(sessionId, "VIEW", "TRUCK");
    }
    
    //בדיקת הרשאה לניהול נהגים
    private boolean canManageDrivers() {
        return userController.isAuthorized(sessionId, "VIEW", "DRIVER");
    }
    
    //הצגת הודעת חוסר הרשאה
    private void showAccessDenied(String action) {
        System.out.println("\nאין לך הרשאה " + action + ".");
        System.out.println("אנא פנה למנהל המערכת אם הינך זקוק להרשאה זו.");
        System.out.println("\nלחץ Enter כדי להמשיך");
        scanner.nextLine();
    }
    
    //קבלת קלט מספרי מהמשתמש
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
    
    //קבלת קלט מספר עשרוני מהמשתמש
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
    
    //קבלת קלט טקסט מהמשתמש
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

}
