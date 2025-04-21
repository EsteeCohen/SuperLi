package src.main.ui;

import java.util.List;
import java.util.Scanner;

import src.main.controllers.SiteController;
import src.main.entities.Site;

public class SiteUI {
    private Scanner scanner;
    private SiteController siteController;

    /**
     * בנאי לממשק ניהול אתרים
     */
    public SiteUI(SiteController siteController) {
        this.scanner = new Scanner(System.in);
        this.siteController = siteController;
    }

    /**
     * התחלת ממשק ניהול אתרים
     */
    public void start() {
        boolean exit = false;
        
        while (!exit) {
            displayMenu();
            int choice = getIntInput("בחר אפשרות: ");
            
            switch (choice) {
                case 1:
                    addNewSite();
                    break;
                case 2:
                    viewSite();
                    break;
                case 3:
                    viewAllSites();
                    break;
                case 4:
                    searchSitesByZone();
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
     * הצגת תפריט ניהול אתרים
     */
    private void displayMenu() {
        System.out.println("\n=== ניהול אתרים ===");
        System.out.println("1. הוספת אתר חדש");
        System.out.println("2. צפייה באתר");
        System.out.println("3. צפייה בכל האתרים");
        System.out.println("4. חיפוש אתרים לפי אזור");
        System.out.println("0. חזרה לתפריט הראשי");
    }

    /**
     * הוספת אתר חדש
     */
    private void addNewSite() {
        System.out.println("\n=== הוספת אתר חדש ===");
        
        String id = getStringInput("הזן מזהה אתר: ");
        String name = getStringInput("הזן שם אתר: ");
        String address = getStringInput("הזן כתובת: ");
        String contactPhone = getStringInput("הזן טלפון איש קשר: ");
        String contactName = getStringInput("הזן שם איש קשר: ");
        
        System.out.println("בחר אזור שילוח:");
        System.out.println("1. צפון");
        System.out.println("2. מרכז");
        System.out.println("3. דרום");
        System.out.println("4. ירושלים");
        
        int zoneChoice = getIntInput("בחירתך: ");
        String zone;
        
        switch (zoneChoice) {
            case 1:
                zone = "NORTH";
                break;
            case 2:
                zone = "CENTER";
                break;
            case 3:
                zone = "SOUTH";
                break;
            case 4:
                zone = "JERUSALEM";
                break;
            default:
                System.out.println("בחירה לא תקינה. מגדיר כברירת מחדל: מרכז");
                zone = "CENTER";
        }
        
        Site site = siteController.addSite(id, name, address, contactPhone, contactName, zone);
        
        if (site != null) {
            System.out.println("האתר נוסף בהצלחה!");
        } else {
            System.out.println("שגיאה בהוספת האתר. ייתכן שהמזהה כבר קיים במערכת.");
        }
    }

    /**
     * צפייה באתר ספציפי
     */
    private void viewSite() {
        System.out.println("\n=== צפייה באתר ===");
        String id = getStringInput("הזן מזהה אתר: ");
        
        Site site = siteController.getSiteById(id);
        
        if (site != null) {
            displaySiteDetails(site);
        } else {
            System.out.println("אתר לא נמצא במערכת.");
        }
    }

    /**
     * צפייה בכל האתרים
     */
    private void viewAllSites() {
        System.out.println("\n=== רשימת כל האתרים ===");
        
        List<Site> sites = siteController.getAllSites();
        
        if (sites.isEmpty()) {
            System.out.println("אין אתרים במערכת.");
            return;
        }
        
        for (Site site : sites) {
            displaySiteDetails(site);
            System.out.println("--------------------");
        }
    }

    /**
     * חיפוש אתרים לפי אזור שילוח
     */
    private void searchSitesByZone() {
        System.out.println("\n=== חיפוש אתרים לפי אזור ===");
        System.out.println("בחר אזור שילוח:");
        System.out.println("1. צפון");
        System.out.println("2. מרכז");
        System.out.println("3. דרום");
        System.out.println("4. ירושלים");
        
        int zoneChoice = getIntInput("בחירתך: ");
        String zone;
        
        switch (zoneChoice) {
            case 1:
                zone = "NORTH";
                break;
            case 2:
                zone = "CENTER";
                break;
            case 3:
                zone = "SOUTH";
                break;
            case 4:
                zone = "JERUSALEM";
                break;
            default:
                System.out.println("בחירה לא תקינה. מציג אתרים באזור המרכז כברירת מחדל.");
                zone = "CENTER";
        }
        
        List<Site> sites = siteController.getSitesByZone(zone);
        
        if (sites.isEmpty()) {
            System.out.println("אין אתרים באזור זה.");
            return;
        }
        
        System.out.println("אתרים באזור " + zone + ":");
        for (Site site : sites) {
            displaySiteDetails(site);
            System.out.println("--------------------");
        }
    }

    /**
     * הצגת פרטי אתר
     */
    private void displaySiteDetails(Site site) {
        System.out.println("מזהה: " + site.getId());
        System.out.println("שם: " + site.getName());
        System.out.println("כתובת: " + site.getAddress());
        System.out.println("טלפון איש קשר: " + site.getContactPhone());
        System.out.println("שם איש קשר: " + site.getContactName());
        System.out.println("אזור שילוח: " + site.getShippingZone());
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
