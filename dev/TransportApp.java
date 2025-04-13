public class TransportApp {
    public static void main(String[] args) {
        // Initialize services
        DriverService driverService = new DriverService();
        TruckService truckService = new TruckService();
        SiteService siteService = new SiteService();
        TransportService transportService = new TransportService(truckService, driverService, siteService);
        OrderService orderService = new OrderService(siteService, transportService);

        // Initialize controllers
        DriverController driverController = new DriverController(driverService);
        TruckController truckController = new TruckController(truckService);
        SiteController siteController = new SiteController(siteService);
        TransportController transportController = new TransportController(transportService);
        OrderController orderController = new OrderController(orderService);

        // Initialize UI components
        TransportUI transportUI = new TransportUI(transportController);
        OrderUI orderUI = new OrderUI(orderController);
        FleetUI fleetUI = new FleetUI(truckController, driverController);
        SiteUI siteUI = new SiteUI(siteController);
        MainUI mainUI = new MainUI(transportUI, orderUI, fleetUI, siteUI);

        // Initialize system with sample data
        SystemInitializer initializer = new SystemInitializer(driverService, truckService, siteService, transportService, orderService);
        initializer.initializeSystem();

        // Start application
        mainUI.start();
    }
}
