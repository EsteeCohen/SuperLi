package src.main.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.main.entities.*;
import src.main.enums.*;

public class SystemInitializer {
    private DriverService driverService;
    private TruckService truckService;
    private SiteService siteService;
    private TransportService transportService;
    private OrderService orderService;   
    private IncidentService incidentService;
    private UserService userService; 

    // Constructor
    public SystemInitializer(DriverService driverService, TruckService truckService, SiteService siteService, TransportService transportService, OrderService orderService,  IncidentService incidentService, UserService userService) {
        this.driverService = driverService;
        this.truckService = truckService;
        this.siteService = siteService;
        this.transportService = transportService;
        this.orderService = orderService;
        this.incidentService = incidentService;
        this.userService = userService;
    }

    // אתחול המערכת
    public void initializeSystem() {
        System.out.println("Welcome to the Transport Management System");
        System.out.println();
        
        // Ask about demo data
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to start with demo data?");
        System.out.println("1. Yes - Load with sample data");
        System.out.println("2. No - Start with empty system");
        System.out.print("Your choice (1/2): ");
        
        boolean useDemoData = false;
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            useDemoData = (choice == 1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Starting without demo data.");
        }
        
        if (useDemoData) {
            System.out.println("Initializing system with demo data...");
            initializeWithDemoData();
            System.out.println("Demo data loaded successfully!");
        } else {
            System.out.println("Initializing empty system...");
            initializeBasicUsers(); // Always need at least one admin user
            System.out.println("Empty system initialized!");
        }
        
        System.out.println();
    }
    
    private void initializeWithDemoData() {
        initializeDrivers();
        initializeTrucks();
        initializeSites();
        initializeTransports();
        initializeOrders();
        initializeIncidents();
        initializeUsers(); 
    }
    
    private void initializeBasicUsers() {
        // Create at least one admin user so someone can log in
        userService.addUser("U001", "admin", "admin123", "System Administrator", UserRole.SYSTEM_ADMIN);
        userService.addUser("U002", "manager", "manager123", "Transport Manager", UserRole.TRANSPORT_MANAGER);
    }

    // אתחול משתמשים
    private void initializeUsers() {
        userService.addUser("U001", "admin", "admin123", "System Administrator", UserRole.SYSTEM_ADMIN);
        userService.addUser("U002", "manager", "manager123", "Transport Manager", UserRole.TRANSPORT_MANAGER);
        userService.addUser("U003", "driver1", "driver123", "Driver Number 1", UserRole.DRIVER);
        userService.addUser("U004", "viewer", "viewer123", "Viewer", UserRole.VIEWER);
    }

    // אתחול נהגים
    private void initializeDrivers() {
        // נתוני נהגים מהוראות ההפעלה
        driverService.addDriver("123456789", "Israel Israeli", "050-1234567", LicenseType.C);
        driverService.addDriver("234567890", "Moshe Cohen", "052-2345678", LicenseType.CE);
        driverService.addDriver("345678901", "David Levy", "054-3456789", LicenseType.C1);
    }

    // אתחול משאיות
    private void initializeTrucks() {
        // נתוני משאיות מהוראות ההפעלה
        truckService.addTruck("12-345-67", "Volvo FH16", 9000, 25000, LicenseType.CE);
        truckService.addTruck("23-456-78", "Mercedes Actros", 8500, 18000, LicenseType.C);
        truckService.addTruck("34-567-89", "Iveco Daily", 3500, 7500, LicenseType.C1);
    }

    // אתחול אתרים
    private void initializeSites() {
        // נתוני אתרים מהוראות ההפעלה
        siteService.addSite("SITE001", "Central Warehouse", "Industry St. 10, Haifa", "04-8765432", "Ronen Cohen", ShippingZone.NORTH);
        siteService.addSite("SITE002", "Large Supermarket", "Herzl 50, Tel Aviv", "03-6543210", "Michal Levy", ShippingZone.CENTER);
        siteService.addSite("SITE003", "Department Store", "Jaffa 20, Jerusalem", "02-5432109", "Sarah Abraham", ShippingZone.JERUSALEM);
        siteService.addSite("SITE004", "Logistics Center", "Negev 5, Beer Sheva", "08-6789012", "Amit Alon", ShippingZone.SOUTH);
    }

    // אתחול הובלות
    private void initializeTransports() {
        // תאריכים לדוגמה להובלות
        LocalDate date1 = LocalDate.of(2026, 4, 20);
        LocalDate date2 = LocalDate.of(2026, 4, 21);
        LocalDate date3 = LocalDate.of(2026, 4, 22);
        
        // שעות לדוגמה להובלות
        LocalTime time1 = LocalTime.of(8, 0);
        LocalTime time2 = LocalTime.of(9, 30);
        LocalTime time3 = LocalTime.of(10, 0);
        

        // יצירת הובלות
        Transport transport1 = transportService.createTransport(
                date1, time1, "12-345-67", "234567890", "SITE001", List.of("SITE002"));

        Transport transport2 = transportService.createTransport(
                date2, time2, "23-456-78", "123456789", "SITE001", List.of("SITE003"));

        Transport transport3 = transportService.createTransport(
                date3, time3, "34-567-89", "345678901", "SITE001", List.of("SITE004"));

    }

    // אתחול הזמנות
    private void initializeOrders() {
        // דוגמה להזמנות בסיסיות המקושרות להובלות
        // הנחה: מחלקת Order כבר מוגדרת
        Transport transport1 = transportService.getTransportById(1);
        Transport transport2 = transportService.getTransportById(2);
        Transport transport3 = transportService.getTransportById(3);
        
        Site site1 = siteService.getSiteById("SITE002");
        Site site2 = siteService.getSiteById("SITE003");
        Site site3 = siteService.getSiteById("SITE004");
        
        // יצירת פריטים לדוגמה
        Item item1 = new Item(122, "Refrigerator", 5, 300);
        Item item2 = new Item(123, "Television", 10, 25);
        Item item3 = new Item(124, "Washing Machine", 8, 150);
        
        // יצירת הזמנות
        List<Item> items1 = new ArrayList<>();
        items1.add(item1);
        items1.add(item2);
        Order order1 = new Order(transport1.getDate(), site1, items1);
        orderService.assignTransportToOrder(order1.getId(), transport1.getId());

        List<Item> items2 = new ArrayList<>();
        items2.add(item2);
        items2.add(item3);
        Order order2 = new Order(transport2.getDate(), site2, items2);
        orderService.assignTransportToOrder(order2.getId(), transport2.getId());

        List<Item> items3 = new ArrayList<>();
        items3.add(item1);
        items3.add(item3);
        Order order3 = new Order(transport3.getDate(), site3, items3);
        orderService.assignTransportToOrder(order3.getId(), transport3.getId());

    }

    // אתחול תקלות
    private void initializeIncidents() {
        // נוסיף תקלה אחת לדוגמה
        Transport transport2 = transportService.getTransportById(1);
        
        // יצירת תקלה
        Incident incident = new Incident(
            "INC001",
            IncidentType.OTHER, 
            "Road blockage on Highway 1 to Jerusalem due to traffic accident",
            transport2
        );
        incident.setStatus(IncidentStatus.REPORTED);
        
        // יצירת פתרון
        IncidentResolution resolution = new IncidentResolution(
            "RES001", 
            LocalDate.of(2025, 4, 20).atTime(16, 0),
            "Route change via Highway 443"
        );
        
        // הוספת פתרון לתקלה
        incident.setResolution(resolution);
        
        // עדכון סטטוס
        incident.setStatus(IncidentStatus.IN_PROGRESS);

    }
}
