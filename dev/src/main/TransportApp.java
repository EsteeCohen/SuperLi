
package src.main;

import src.main.services.*;
import src.main.controllers.*;
import src.main.ui.*;

public class TransportApp {
    public static void main(String[] args) {
        // Initialize services
        DriverService driverService = new DriverService();
        TruckService truckService = new TruckService();
        SiteService siteService = new SiteService();
        TransportService transportService = new TransportService(truckService, driverService, siteService);
        IncidentService incidentService = new IncidentService(transportService);
        OrderService orderService = new OrderService(siteService, transportService,incidentService);
        transportService.setOrderService(orderService);
        UserService userService = new UserService();
        ScheduleService scheduleService = new ScheduleService(driverService,transportService);

        // Initialize controllers
        DriverController driverController = new DriverController(driverService);
        TruckController truckController = new TruckController(truckService);
        SiteController siteController = new SiteController(siteService);
        TransportController transportController = new TransportController(transportService);
        OrderController orderController = new OrderController(orderService);
        UserController userController = new UserController(userService);
        IncidentController incidentController = new IncidentController(incidentService);
        ScheduleController scheduleController = new ScheduleController(scheduleService);

        // Initialize UI components
        TransportUI transportUI = new TransportUI(transportController);
        OrderUI orderUI = new OrderUI(orderController,siteController, transportController);
        FleetUI fleetUI = new FleetUI(truckController, driverController,userController);
        SiteUI siteUI = new SiteUI(siteController);
//        IncidentUI incidentUI = new IncidentUI();
        LoginUI loginUI = new LoginUI(userController);
        ScheduleUI scheduleUI = new ScheduleUI(scheduleController, driverController, transportController);
        String sessionId = loginUI.processLogin();
        UserManagementUI userManagementUI = new UserManagementUI(userController, sessionId);
        MainUI mainUI = new MainUI(userController,transportController,orderController,truckController,driverController,siteController,scheduleController,incidentController,loginUI,transportUI,orderUI,fleetUI,siteUI, scheduleUI, userManagementUI);

        // Initialize system with sample data
        SystemInitializer initializer = new SystemInitializer(driverService, truckService, siteService, transportService, orderService,scheduleService,incidentService, userService);
        initializer.initializeSystem();

        // Start application
        mainUI.start();
    }
}
