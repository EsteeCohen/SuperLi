package src.main.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import src.main.entities.*;
import src.main.enums.*;

public class SystemInitializer {
    private DriverService driverService;
    private TruckService truckService;
    private SiteService siteService;
    private TransportService transportService;
    private OrderService orderService;
    private ScheduleService scheduleService;
    private IncidentService incidentService;

    // Constructor
    public SystemInitializer(DriverService driverService, TruckService truckService, SiteService siteService, TransportService transportService, OrderService orderService, ScheduleService scheduleService, IncidentService incidentService) {
        this.driverService = driverService;
        this.truckService = truckService;
        this.siteService = siteService;
        this.transportService = transportService;
        this.orderService = orderService;
        this.scheduleService = scheduleService;
        this.incidentService = incidentService;
    }

    // אתחול המערכת
    public void initializeSystem() {
        initializeDrivers();
        initializeTrucks();
        initializeSites();
        initializeTransports();
        initializeOrders();
        initializeSchedules();
        initializeIncidents();
    }

    // אתחול נהגים
    private void initializeDrivers() {
        // נתוני נהגים מהוראות ההפעלה
        driverService.addDriver("123456789", "ישראל ישראלי", "050-1234567", LicenseType.C);
        driverService.addDriver("234567890", "משה כהן", "052-2345678", LicenseType.CE);
        driverService.addDriver("345678901", "דוד לוי", "054-3456789", LicenseType.C1);
    }

    // אתחול משאיות
    private void initializeTrucks() {
        // נתוני משאיות מהוראות ההפעלה
        truckService.addTruck("12-345-67", "וולוו FH16", 9000, 25000, LicenseType.CE);
        truckService.addTruck("23-456-78", "מרצדס אקטרוס", 8500, 18000, LicenseType.C);
        truckService.addTruck("34-567-89", "איווקו דיילי", 3500, 7500, LicenseType.C1);
    }

    // אתחול אתרים
    private void initializeSites() {
        // נתוני אתרים מהוראות ההפעלה
        siteService.addSite("SITE001", "מחסן מרכזי", "התעשייה 10, חיפה", "04-8765432", "רונן כהן", ShippingZone.NORTH);
        siteService.addSite("SITE002", "סופרמרקט גדול", "הרצל 50, תל אביב", "03-6543210", "מיכל לוי", ShippingZone.CENTER);
        siteService.addSite("SITE003", "חנות כלבו", "יפו 20, ירושלים", "02-5432109", "שרה אברהם", ShippingZone.JERUSALEM);
        siteService.addSite("SITE004", "מרכז לוגיסטי", "הנגב 5, באר שבע", "08-6789012", "עמית אלון", ShippingZone.SOUTH);
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
        
//        // קבלת רפרנסים לישויות
//        Truck truck1 = truckService.getTruckByRegNumber("12-345-67");
//        Truck truck2 = truckService.getTruckByRegNumber("23-456-78");
//        Truck truck3 = truckService.getTruckByRegNumber("34-567-89");
//
//        Driver driver1 = driverService.getDriverById("234567890");
//        Driver driver2 = driverService.getDriverById("123456789");
//        Driver driver3 = driverService.getDriverById("345678901");
//
//        Site sourceSite = siteService.getSiteById("SITE001");
//        Site destinationSite1 = siteService.getSiteById("SITE002");
//        Site destinationSite2 = siteService.getSiteById("SITE003");
//        Site destinationSite3 = siteService.getSiteById("SITE004");
//
//        // יצירת רשימות יעדים
//        List<Site> destinations1 = new ArrayList<>();
//        destinations1.add(destinationSite1);
//
//        List<Site> destinations2 = new ArrayList<>();
//        destinations2.add(destinationSite2);
//
//        List<Site> destinations3 = new ArrayList<>();
//        destinations3.add(destinationSite3);
//
//        // יצירת הובלות
//        Transport transport1 = transportService.createTransport(date1, time1, "12-345-67", "234567890", "SITE001", List.of("SITE002"));
//
//        transport1.setStatus(TransportStatus.PLANNING);
//
//        Transport transport2 = new Transport( date2, time2, truck2, driver2, sourceSite, destinations2);
//        transport2.setStatus(TransportStatus.PLANNING);
//
//        Transport transport3 = new Transport( date3, time3, truck3, driver3, sourceSite, destinations3);
//        transport3.setStatus(TransportStatus.PLANNING);
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
        Item item1 = new Item(122, "מקרר", 5, 300);
        Item item2 = new Item(123, "טלוויזיה", 10, 25);
        Item item3 = new Item(124, "מכונת כביסה", 8, 150);
        
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

    // אתחול לוחות זמנים
    private void initializeSchedules() {
        // קבלת נהגים מהשירות
        Driver driver1 = driverService.getDriverById("234567890");
        Driver driver2 = driverService.getDriverById("123456789");
        Driver driver3 = driverService.getDriverById("345678901");
        
        // קבלת הובלות מהשירות
        Transport transport1 = transportService.getTransportById(1);
        Transport transport2 = transportService.getTransportById(2);
        Transport transport3 = transportService.getTransportById(3);
        
        // יצירת לוחות זמנים
        DriverSchedule schedule1 = new DriverSchedule("SCH001", driver1, transport1.getDate());
        schedule1.setStatus(ScheduleStatus.CONFIRMED);
        
        DriverSchedule schedule2 = new DriverSchedule("SCH002", driver2, transport2.getDate());
        schedule2.setStatus(ScheduleStatus.CONFIRMED);
        
        DriverSchedule schedule3 = new DriverSchedule("SCH003", driver3, transport3.getDate());
        schedule3.setStatus(ScheduleStatus.CONFIRMED);
        
        // יצירת רשומות בלוח זמנים
        LocalTime loadingStart1 = transport1.getTime().minusHours(1);
        LocalTime loadingEnd1 = transport1.getTime();
        LocalTime transportEnd1 = transport1.getTime().plusHours(4);
        
        LocalTime loadingStart2 = transport2.getTime().minusHours(1);
        LocalTime loadingEnd2 = transport2.getTime();
        LocalTime transportEnd2 = transport2.getTime().plusHours(3);
        
        LocalTime loadingStart3 = transport3.getTime().minusHours(1);
        LocalTime loadingEnd3 = transport3.getTime();
        LocalTime transportEnd3 = transport3.getTime().plusHours(5);
        
        // הוספת רשומות ללוחות הזמנים
        // הוספת פעילות העמסה
        schedule1.addEntry(new ScheduleEntry(
            "ENT001", loadingStart1, loadingEnd1, transport1, EntryType.LOADING, "העמסת סחורה במחסן המרכזי"
        ));
        
        // הוספת פעילות הובלה
        schedule1.addEntry(new ScheduleEntry(
            "ENT002", loadingEnd1, transportEnd1, transport1, EntryType.TRANSPORT, "הובלה למרכז"
        ));
        
        // חזרה על כך לשאר הנהגים
        schedule2.addEntry(new ScheduleEntry(
            "ENT003", loadingStart2, loadingEnd2, transport2, EntryType.LOADING, "העמסת סחורה במחסן המרכזי"
        ));
        
        schedule2.addEntry(new ScheduleEntry(
            "ENT004", loadingEnd2, transportEnd2, transport2, EntryType.TRANSPORT, "הובלה לירושלים"
        ));
        
        schedule3.addEntry(new ScheduleEntry(
            "ENT005", loadingStart3, loadingEnd3, transport3, EntryType.LOADING, "העמסת סחורה במחסן המרכזי"
        ));
        
        schedule3.addEntry(new ScheduleEntry(
            "ENT006", loadingEnd3, transportEnd3, transport3, EntryType.TRANSPORT, "הובלה לדרום"
        ));
        
        // הוספת הפסקה לנהג הראשון
        schedule1.addEntry(new ScheduleEntry(
            "ENT007", transportEnd1, transportEnd1.plusMinutes(30), null, EntryType.BREAK, "הפסקת צהריים"
        ));
        
        // הוספה למאגר
        scheduleService.addSchedule(schedule1);
        scheduleService.addSchedule(schedule2);
        scheduleService.addSchedule(schedule3);
    }

    // אתחול תקלות
    private void initializeIncidents() {
        // נוסיף תקלה אחת לדוגמה
        Transport transport2 = transportService.getTransportById(1);
        
        // יצירת תקלה
        Incident incident = new Incident(
            "INC001",
            IncidentType.ROAD_CLOSURE, 
            "חסימה בכביש 1 לירושלים עקב תאונת דרכים",
            transport2
        );
        incident.setStatus(IncidentStatus.REPORTED);
        
        // יצירת פתרון
        IncidentResolution resolution = new IncidentResolution(
            "RES001", 
            LocalDate.of(2025, 4, 20).atTime(16, 0),
            "שינוי מסלול דרך כביש 443"
        );
        
        // הוספת פתרון לתקלה
        incident.setResolution(resolution);
        
        // עדכון סטטוס
        incident.setStatus(IncidentStatus.IN_PROGRESS);

    }
}
