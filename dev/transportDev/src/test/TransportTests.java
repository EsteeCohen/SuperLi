// package transportDev.src.test;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import transportDev.src.main.entities.*;
// import transportDev.src.main.enums.*;
// import transportDev.src.main.services.*;

// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// public class TransportTests {
//     private TransportService transportService;
//     private TruckService truckService;
//     private DriverService driverService;
//     private SiteService siteService;
//     private OrderService orderService;

//     @BeforeEach
//     public void setUp() {
//         truckService = new TruckService();
//         driverService = new DriverService();
//         siteService = new SiteService();
//         transportService = new TransportService(truckService, driverService, siteService);
//         orderService = new OrderService(siteService, transportService, new IncidentService(transportService));
//         transportService.setOrderService(orderService);

//         truckService.addTruck("T1", "TruckModel1", 5000, 15000, LicenseType.C);
//         truckService.addTruck("T2", "TruckModel1", 5000, 16000, LicenseType.C);
//         driverService.addDriver("D1", "John Doe", "050-1234567", LicenseType.C);
//         driverService.addDriver("D2", "Jane Smith", "050-7654321", LicenseType.C);
//         siteService.addSite("SRC", "Source Site", "123 Source St.", "050-1111111", "Contact", ShippingZone.NORTH);
//         siteService.addSite("DST", "Destination Site", "456 Destination St.", "050-2222222", "Contact", ShippingZone.SOUTH);
//     }

//     @Test
//     public void testCreateTransport() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusMinutes(10), "T1", "D1", "SRC", List.of("DST"));
//         assertNotNull(transport);
//         assertEquals("T1", transport.getTruck().getRegNumber());
//         assertEquals("D1", transport.getDriver().getId());
//         assertEquals(1, transport.getDestinations().size());
//         assertEquals("DST", transport.getDestinations().get(0).getId());
//     }

//     @Test
//     public void testCreateTransport_InvalidTruck() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "InvalidTruck", "D1", "SRC", List.of("DST"));
//         assertNull(transport);
//     }

//     @Test
//     public void testGetTransportById() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         Transport found = transportService.getTransportById(transport.getId());
//         assertNotNull(found);
//         assertEquals(transport.getId(), found.getId());
//     }

//     @Test
//     public void testGetTransportsByDate() {
//         LocalDate today = LocalDate.now();
//         transportService.createTransport(today, LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         List<Transport> transports = transportService.getTransportsByDate(today);
//         assertEquals(1, transports.size());
//     }

//     @Test
//     public void testUpdateTransportStatus() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         boolean updated = transportService.updateTransportStatus(transport.getId(), TransportStatus.ACTIVE);
//         assertTrue(updated);
//         assertEquals(TransportStatus.ACTIVE, transport.getStatus());
//     }

//     @Test
//     public void testChangeTruck() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         boolean changed = transportService.changeTruck(transport.getId(), "T2");
//         assertTrue(changed);
//         assertEquals("T2", transport.getTruck().getRegNumber());
//     }

//     @Test
//     public void testChangeTruck_InvalidTransport() {
//         boolean changed = transportService.changeTruck(9999, "T2"); // Invalid transport ID
//         assertFalse(changed);
//     }

//     @Test
//     public void testAddDestination() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         boolean added = transportService.addDestination(transport.getId(), "DST");
//         assertTrue(added);
//         assertEquals(1, transport.getDestinations().size());
//         assertEquals("DST", transport.getDestinations().get(0).getId());
//     }

//     @Test
//     public void testRemoveDestination() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         boolean removed = transportService.removeDestination(transport.getId(), "DST");
//         assertTrue(removed);
//         assertTrue(transport.getDestinations().isEmpty());
//     }

//     @Test
//     public void testCancelTransport() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         boolean canceled = transportService.cancelTransport(transport.getId());
//         assertTrue(canceled);
//         assertEquals(TransportStatus.CANCELLED, transport.getStatus());
//     }

//     @Test
//     public void testCancelTransport_WhenNotCancelable() {
//         Transport transport = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("DST"));
//         transportService.updateTransportStatus(transport.getId(), TransportStatus.ACTIVE);
//         boolean canceled = transportService.cancelTransport(transport.getId());
//         assertFalse(canceled);
//         assertEquals(TransportStatus.ACTIVE, transport.getStatus());
//     }
// }

