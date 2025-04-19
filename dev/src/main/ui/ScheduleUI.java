package src.main.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import src.main.controllers.DriverController;
import src.main.controllers.ScheduleController;
import src.main.controllers.TransportController;
import src.main.entities.DriverSchedule;
import src.main.entities.ScheduleEntry;
import src.main.enums.EntryType;
import src.main.enums.ScheduleStatus;
import src.main.entities.Driver;
import src.main.entities.Transport;

public class ScheduleUI {
    private Scanner scanner;
    private ScheduleController scheduleController;
    private DriverController driverController;
    private TransportController transportController;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * בנאי לממשק ניהול לוחות זמנים
     */
    public ScheduleUI(ScheduleController scheduleController, DriverController driverController, TransportController transportController) {
        this.scanner = new Scanner(System.in);
        this.scheduleController = scheduleController;
        this.driverController = driverController;
        this.transportController = transportController;
    }

    /**
     * התחלת ממשק ניהול לוחות זמנים
     */
    public void start() {
        boolean exit = false;
        
        while (!exit) {
            displayMenu();
            int choice = getIntInput("בחר אפשרות: ");
            
            switch (choice) {
                case 1:
                    createNewSchedule();
                    break;
                case 2:
                    viewDriverSchedule();
                    break;
                case 3:
                    addEntryToSchedule();
                    break;
                case 4:
                    removeEntryFromSchedule();
                    break;
                case 5:
                    updateScheduleStatus();
                    break;
                case 6:
                    viewAllSchedules();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("אפשרות לא תקינה, נסה שנית");
            }
        }
    }

    /**
     * הצגת תפריט ניהול לוחות זמנים
     */
    private void displayMenu() {
        System.out.println("\n=== ניהול לוחות זמנים ===");
        System.out.println("1. יצירת לוח זמנים חדש");
        System.out.println("2. צפייה בלוח זמנים של נהג");
        System.out.println("3. הוספת פעילות ללוח זמנים");
        System.out.println("4. הסרת פעילות מלוח זמנים");
        System.out.println("5. עדכון סטטוס לוח זמנים");
        System.out.println("6. צפייה בכל לוחות הזמנים");
        System.out.println("0. חזרה לתפריט הראשי");
    }

    /**
     * יצירת לוח זמנים חדש
     */
    private void createNewSchedule() {
        System.out.println("\n=== יצירת לוח זמנים חדש ===");
        
        // הצגת רשימת נהגים
        List<Driver> drivers = driverController.getAllDrivers();
        if (drivers.isEmpty()) {
            System.out.println("אין נהגים במערכת. אנא הוסף נהגים תחילה.");
            return;
        }
        
        System.out.println("בחר נהג:");
        for (int i = 0; i < drivers.size(); i++) {
            System.out.println((i + 1) + ". " + drivers.get(i).getName() + " (ת\"ז: " + drivers.get(i).getId() + ")");
        }
        
        int driverIndex = getIntInput("בחירתך: ") - 1;
        if (driverIndex < 0 || driverIndex >= drivers.size()) {
            System.out.println("בחירה לא תקינה.");
            return;
        }
        
        String driverId = drivers.get(driverIndex).getId();
        
        // קבלת תאריך
        LocalDate date = getDateInput("הזן תאריך (YYYY-MM-DD): ");
        if (date == null) {
            return;
        }
        
        // בדיקה אם כבר קיים לוח זמנים לנהג זה בתאריך זה
        DriverSchedule existingSchedule = scheduleController.getDriverScheduleByDate(driverId, date);
        if (existingSchedule != null) {
            System.out.println("כבר קיים לוח זמנים לנהג זה בתאריך זה.");
            return;
        }
        
        boolean success = scheduleController.createSchedule(driverId, date);
        
        if (success) {
            System.out.println("לוח הזמנים נוצר בהצלחה!");
        } else {
            System.out.println("שגיאה ביצירת לוח הזמנים.");
        }
    }

    /**
     * צפייה בלוח זמנים של נהג
     */
    private void viewDriverSchedule() {
        System.out.println("\n=== צפייה בלוח זמנים של נהג ===");
        
        // הצגת רשימת נהגים
        List<Driver> drivers = driverController.getAllDrivers();
        if (drivers.isEmpty()) {
            System.out.println("אין נהגים במערכת.");
            return;
        }
        
        System.out.println("בחר נהג:");
        for (int i = 0; i < drivers.size(); i++) {
            System.out.println((i + 1) + ". " + drivers.get(i).getName() + " (ת\"ז: " + drivers.get(i).getId() + ")");
        }
        
        int driverIndex = getIntInput("בחירתך: ") - 1;
        if (driverIndex < 0 || driverIndex >= drivers.size()) {
            System.out.println("בחירה לא תקינה.");
            return;
        }
        
        String driverId = drivers.get(driverIndex).getId();
        
        // קבלת תאריך
        LocalDate date = getDateInput("הזן תאריך (YYYY-MM-DD): ");
        if (date == null) {
            return;
        }
        
        DriverSchedule schedule = scheduleController.getDriverScheduleByDate(driverId, date);
        
        if (schedule == null) {
            System.out.println("לא נמצא לוח זמנים לנהג זה בתאריך המבוקש.");
            return;
        }
        
        displayScheduleDetails(schedule);
    }

    /**
     * הוספת פעילות ללוח זמנים
     */
    private void addEntryToSchedule() {
        System.out.println("\n=== הוספת פעילות ללוח זמנים ===");
        
        String scheduleId = getStringInput("הזן מזהה לוח זמנים: ");
        
        // בדיקה אם לוח הזמנים קיים
        DriverSchedule schedule = scheduleController.getScheduleById(scheduleId);
        if (schedule == null) {
            System.out.println("לוח זמנים לא נמצא.");
            return;
        }
        
        // בדיקת סטטוס לוח הזמנים
        if (schedule.getStatus() != ScheduleStatus.DRAFT && schedule.getStatus() != ScheduleStatus.CONFIRMED) {
            System.out.println("ניתן להוסיף פעילויות רק ללוח זמנים בסטטוס טיוטה או מאושר.");
            return;
        }
        
        // קבלת סוג הפעילות
        System.out.println("בחר סוג פעילות:");
        System.out.println("1. הובלה");
        System.out.println("2. הפסקה");
        System.out.println("3. תחזוקה");
        System.out.println("4. העמסה");
        System.out.println("5. פריקה");
        
        int typeChoice = getIntInput("בחירתך: ");
        EntryType entryType;
        
        switch (typeChoice) {
            case 1:
                entryType = EntryType.TRANSPORT;
                break;
            case 2:
                entryType = EntryType.BREAK;
                break;
            case 3:
                entryType = EntryType.MAINTENANCE;
                break;
            case 4:
                entryType = EntryType.LOADING;
                break;
            case 5:
                entryType = EntryType.UNLOADING;
                break;
            default:
                System.out.println("בחירה לא תקינה.");
                return;
        }
        
        // קבלת זמני התחלה וסיום
        LocalTime startTime = getTimeInput("שעת התחלה (HH:MM): ");
        if (startTime == null) {
            return;
        }
        
        LocalTime endTime = getTimeInput("שעת סיום (HH:MM): ");
        if (endTime == null) {
            return;
        }
        
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            System.out.println("שעת הסיום חייבת להיות מאוחרת משעת ההתחלה.");
            return;
        }
        
        String transportId = null;
        if (entryType == EntryType.TRANSPORT || entryType == EntryType.LOADING || entryType == EntryType.UNLOADING) {
            // הצגת רשימת הובלות רלוונטיות
            List<Transport> transports = transportController.getTransportsByDate(schedule.getDate());
            
            if (transports.isEmpty()) {
                System.out.println("אין הובלות בתאריך זה.");
                return;
            }
            
            System.out.println("בחר הובלה:");
            for (int i = 0; i < transports.size(); i++) {
                Transport transport = transports.get(i);
                if (transport.getDriver().getId().equals(schedule.getDriver().getId())) {
                    System.out.println((i + 1) + ". הובלה " + transport.getId() + " - " + 
                                      transport.getSourceSite().getName() + " אל " + 
                                      transport.getDestinations().get(0).getName() + ", שעה: " + 
                                      transport.getTime());
                }
            }
            
            int transportIndex = getIntInput("בחירתך: ") - 1;
            if (transportIndex < 0 || transportIndex >= transports.size()) {
                System.out.println("בחירה לא תקינה.");
                return;
            }
            
            transportId = transports.get(transportIndex).getId();
        }
        
        String description = getStringInput("הזן תיאור הפעילות: ");
        
        boolean success = scheduleController.addScheduleEntry(scheduleId, startTime, endTime, transportId, entryType.toString(), description);
        
        if (success) {
            System.out.println("הפעילות נוספה בהצלחה ללוח הזמנים!");
        } else {
            System.out.println("שגיאה בהוספת הפעילות. ייתכן שיש חפיפה עם פעילות קיימת.");
        }
    }

    /**
     * הסרת פעילות מלוח זמנים
     */
    private void removeEntryFromSchedule() {
        System.out.println("\n=== הסרת פעילות מלוח זמנים ===");
        
        String scheduleId = getStringInput("הזן מזהה לוח זמנים: ");
        
        // בדיקה אם לוח הזמנים קיים
        DriverSchedule schedule = scheduleController.getScheduleById(scheduleId);
        if (schedule == null) {
            System.out.println("לוח זמנים לא נמצא.");
            return;
        }
        
        // בדיקת סטטוס לוח הזמנים
        if (schedule.getStatus() != ScheduleStatus.DRAFT && schedule.getStatus() != ScheduleStatus.CONFIRMED) {
            System.out.println("ניתן להסיר פעילויות רק מלוח זמנים בסטטוס טיוטה או מאושר.");
            return;
        }
        
        // הצגת רשימת הפעילויות
        List<ScheduleEntry> entries = schedule.getEntries();
        if (entries.isEmpty()) {
            System.out.println("אין פעילויות בלוח הזמנים.");
            return;
        }
        
        System.out.println("בחר פעילות להסרה:");
        for (int i = 0; i < entries.size(); i++) {
            ScheduleEntry entry = entries.get(i);
            System.out.println((i + 1) + ". " + entry.getType() + " - " + 
                              entry.getStartTime() + " עד " + entry.getEndTime() + 
                              " (" + entry.getDescription() + ")");
        }
        
        int entryIndex = getIntInput("בחירתך: ") - 1;
        if (entryIndex < 0 || entryIndex >= entries.size()) {
            System.out.println("בחירה לא תקינה.");
            return;
        }
        
        String entryId = entries.get(entryIndex).getId();
        
        boolean success = scheduleController.removeScheduleEntry(scheduleId, entryId);
        
        if (success) {
            System.out.println("הפעילות הוסרה בהצלחה מלוח הזמנים!");
        } else {
            System.out.println("שגיאה בהסרת הפעילות.");
        }
    }

    /**
     * עדכון סטטוס לוח זמנים
     */
    private void updateScheduleStatus() {
        System.out.println("\n=== עדכון סטטוס לוח זמנים ===");
        
        String scheduleId = getStringInput("הזן מזהה לוח זמנים: ");
        
        // בדיקה אם לוח הזמנים קיים
        DriverSchedule schedule = scheduleController.getScheduleById(scheduleId);
        if (schedule == null) {
            System.out.println("לוח זמנים לא נמצא.");
            return;
        }
        
        System.out.println("סטטוס נוכחי: " + schedule.getStatus());
        System.out.println("בחר סטטוס חדש:");
        System.out.println("1. טיוטה (DRAFT)");
        System.out.println("2. מאושר (CONFIRMED)");
        System.out.println("3. בביצוע (IN_PROGRESS)");
        System.out.println("4. הושלם (COMPLETED)");
        System.out.println("5. בוטל (CANCELLED)");
        
        int statusChoice = getIntInput("בחירתך: ");
        String status;
        
        switch (statusChoice) {
            case 1:
                status = "DRAFT";
                break;
            case 2:
                status = "CONFIRMED";
                break;
            case 3:
                status = "IN_PROGRESS";
                break;
            case 4:
                status = "COMPLETED";
                break;
            case 5:
                status = "CANCELLED";
                break;
            default:
                System.out.println("בחירה לא תקינה.");
                return;
        }
        
        boolean success = scheduleController.updateScheduleStatus(scheduleId, status);
        
        if (success) {
            System.out.println("סטטוס לוח הזמנים עודכן בהצלחה ל-" + status + "!");
        } else {
            System.out.println("שגיאה בעדכון סטטוס לוח הזמנים.");
        }
    }

    /**
     * צפייה בכל לוחות הזמנים
     */
    private void viewAllSchedules() {
        System.out.println("\n=== כל לוחות הזמנים ===");
        
        List<DriverSchedule> schedules = scheduleController.getAllSchedules();
        
        if (schedules.isEmpty()) {
            System.out.println("אין לוחות זמנים במערכת.");
            return;
        }
        
        for (DriverSchedule schedule : schedules) {
            displayScheduleDetails(schedule);
            System.out.println("------------------------");
        }
    }

    /**
     * הצגת פרטי לוח זמנים
     */
    private void displayScheduleDetails(DriverSchedule schedule) {
        System.out.println("מזהה לוח זמנים: " + schedule.getId());
        System.out.println("נהג: " + schedule.getDriver().getName() + " (ת\"ז: " + schedule.getDriver().getId() + ")");
        System.out.println("תאריך: " + schedule.getDate());
        System.out.println("סטטוס: " + schedule.getStatus());
        
        List<ScheduleEntry> entries = schedule.getEntries();
        
        if (entries.isEmpty()) {
            System.out.println("אין פעילויות בלוח הזמנים.");
            return;
        }
        
        System.out.println("פעילויות:");
        for (int i = 0; i < entries.size(); i++) {
            ScheduleEntry entry = entries.get(i);
            System.out.println("  " + (i + 1) + ". " + entry.getType() + " - " + 
                              entry.getStartTime() + " עד " + entry.getEndTime());
            System.out.println("     תיאור: " + entry.getDescription());
            
            if (entry.getTransport() != null) {
                System.out.println("     הובלה: " + entry.getTransport().getId());
            }
        }
    }

    /**
     * קבלת קלט תאריך מהמשתמש
     */
    private LocalDate getDateInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        
        try {
            return LocalDate.parse(input, dateFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("פורמט תאריך לא תקין. השתמש בפורמט YYYY-MM-DD.");
            return null;
        }
    }

    /**
     * קבלת קלט שעה מהמשתמש
     */
    private LocalTime getTimeInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        
        try {
            return LocalTime.parse(input, timeFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("פורמט שעה לא תקין. השתמש בפורמט HH:MM.");
            return null;
        }
    }

    /**
     * קבלת קלט מספרי מהמשתמש
     */
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

    /**
     * קבלת קלט טקסט מהמשתמש
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
