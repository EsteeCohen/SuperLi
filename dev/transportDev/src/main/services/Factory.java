// package transportDev.src.main.services;

// import transportDev.src.main.controllers.FacadeController;

// public class Factory {
//     private OrderService orderService;
//     private TruckService truckService;
//     private IncidentService incidentService;
//     private TransportService transportService;
//     private TransportScheduleService transportScheduleService;
//     private FacadeController facadeController;

//     // Constructor
//      public Factory() {
//         // Initialize services
//         DriverService driverService = new DriverService();
//         TruckService truckService = new TruckService();
//         SiteService siteService = new SiteService();
//         TransportService transportService = new TransportService(truckService, driverService, siteService);
//         IncidentService incidentService = new IncidentService(transportService);
//         OrderService orderService = new OrderService(siteService, transportService, incidentService);
//         transportService.setOrderService(orderService);
//         UserService userService = new UserService();
//         ITransportScheduleService transportScheduleService = new TransportScheduleService(transportService);


//         // Initialize facade controller
//         facadeController = new FacadeController(
//             incidentService,
//             driverService,
//             userService,
//             siteService,
//             orderService,
//             truckService,
//             transportService,
//                 transportScheduleService
//         );

//      }

//      public FacadeController getFacadeController(){
//         return facadeController;
//      }

//     public OrderService getOrderService() {
//         return orderService;
//     }

//     public TruckService getTruckService() {
//         return truckService;
//     }

//     public IncidentService getIncidentService() {
//         return incidentService;
//     }

//     public TransportService getTransportService(){
//         return transportService;
//     }

//     public ITransportScheduleService getTransportScheduleInterface(){
//         return transportScheduleService;
//     }
    

// }
