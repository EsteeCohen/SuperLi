package transportDev.src.main;

import employeeDev.src.serviceLayer.Factory;
import employeeDev.src.serviceLayer.Interfaces.DriverInfoInterface;
import employeeDev.src.serviceLayer.Interfaces.ITransportScheduleService;
import employeeDev.src.serviceLayer.Interfaces.SiteInfoInterface;
import transportDev.src.main.controllers.*;
import transportDev.src.main.services.*;
import transportDev.src.main.ui.*;

public class TransportApp {

    // Services for the Transport Management System
    private DriverInfoInterface driverService;
    private TruckService truckService;
    private SiteInfoInterface siteService;
    private TransportService transportService;
    private IncidentService incidentService;
    private OrderService orderService;
    private UserService userService;
    private ITransportScheduleService transportScheduleService;

    // facade controller for the Transport Management System
    private FacadeController facadeController;

    // system initializer for demo data
    private SystemInitializer initializer;

    // UI for the Transport Management System
    private LoginUI loginUI;
    private TransportUI transportUI;
    private OrderUI orderUI;
    private FleetUI fleetUI;
    private SiteUI siteUI;
    private UserManagementUI userManagementUI;
    private ReportsUI reportsUI;
    private MainUI mainUI;

    public TransportApp(Factory employeeFactory) {
        // Initialize services
        driverService = employeeFactory.getDriverInfoService();
        truckService = new TruckService();
        siteService = employeeFactory.getSiteInfoService();
        transportService = new TransportService(truckService, driverService, siteService);
        incidentService = new IncidentService(transportService);
        orderService = new OrderService(siteService, transportService, incidentService);
        transportService.setOrderService(orderService);
        userService = new UserService();
        transportScheduleService = new TransportScheduleService(transportService);


        // Initialize facade controller
        facadeController = new FacadeController(
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
        initializer = new SystemInitializer(driverService,truckService, 
                                                             siteService, transportService, 
                                                             orderService, incidentService, userService);
        // initializer.initializeSystem();
        
        // Initialize UI components
        loginUI = new LoginUI(facadeController);
        transportUI = new TransportUI(facadeController);
        orderUI = new OrderUI(facadeController);
        fleetUI = new FleetUI(facadeController);
        siteUI = new SiteUI(facadeController);
        userManagementUI = new UserManagementUI(facadeController, "admin");
        reportsUI = new ReportsUI(facadeController);
        
        mainUI = new MainUI(facadeController, loginUI, transportUI, orderUI, 
                                  fleetUI, siteUI, userManagementUI, reportsUI);
    }
    
    public ITransportScheduleService getITransportScheduleService() {
        return transportScheduleService;
    }

    public void startSystem(){
        mainUI.start();
    }
    
}
