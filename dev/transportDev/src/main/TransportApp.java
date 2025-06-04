package src.main;

import src.main.services.*;
import src.main.controllers.*;
import src.main.ui.*;

public class TransportApp {
    public static void main(String[] args) {
        System.out.println("=== Transport Management System for Supermarket Chain ===");
        System.out.println("Initializing system...");
        
        // Initialize services
        DriverService driverService = new DriverService();
        TruckService truckService = new TruckService();
        SiteService siteService = new SiteService();
        TransportService transportService = new TransportService(truckService, driverService, siteService);
        IncidentService incidentService = new IncidentService(transportService);
        OrderService orderService = new OrderService(siteService, transportService, incidentService);
        transportService.setOrderService(orderService);
        UserService userService = new UserService();
        ITransportScheduleService transportScheduleService = new TransportScheduleService(transportService);


        // Initialize facade controller
        FacadeController facadeController = new FacadeController(
            incidentService,
            driverService,
            userService,
            siteService,
            orderService,
            truckService,
            transportService,
                transportScheduleService
        );

        // Initialize system with option for demo data
        SystemInitializer initializer = new SystemInitializer(driverService, truckService, 
                                                             siteService, transportService, 
                                                             orderService, incidentService, userService);
        initializer.initializeSystem();

        // Initialize UI components
        LoginUI loginUI = new LoginUI(facadeController);
        TransportUI transportUI = new TransportUI(facadeController);
        OrderUI orderUI = new OrderUI(facadeController);
        FleetUI fleetUI = new FleetUI(facadeController);
        SiteUI siteUI = new SiteUI(facadeController);
        UserManagementUI userManagementUI = new UserManagementUI(facadeController, "admin");
        ReportsUI reportsUI = new ReportsUI(facadeController);
        
        MainUI mainUI = new MainUI(facadeController, loginUI, transportUI, orderUI, 
                                  fleetUI, siteUI, userManagementUI, reportsUI);

        System.out.println("System ready. Please log in.");
        System.out.println();

        // Start application
        mainUI.start();
    }
}
