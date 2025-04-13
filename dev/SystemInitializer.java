public class SystemInitializer {
    private DriverService driverService;
    private TruckService truckService;
    private SiteService siteService;
    private TransportService transportService;
    private OrderService orderService;

    // Constructor
    public SystemInitializer(DriverService driverService, TruckService truckService, SiteService siteService, TransportService transportService, OrderService orderService){
        this.driverService = driverService;
        this.truckService = truckService;
        this.siteService = siteService;
        this.transportService = transportService;
        this.orderService = orderService;

    }

    // Methods
    public void initializeSystem()  // Creates sample data for testing
}
