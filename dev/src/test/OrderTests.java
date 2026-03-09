package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.entities.*;
import src.main.enums.*;
import src.main.services.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTests {

    private OrderService orderService;
    private SiteService siteService;
    private TransportService transportService;
    private IncidentService incidentService;
    private TruckService truckService;
    private DriverService driverService;

    @BeforeEach
    public void setUp() {
        siteService = new SiteService();
        truckService = new TruckService();
        driverService = new DriverService();
        transportService = new TransportService(truckService, driverService, siteService);
        incidentService = new IncidentService(transportService);
        orderService = new OrderService(siteService, transportService, incidentService);

        siteService.addSite("SRC", "Source", "Addr", "050", "Contact", ShippingZone.NORTH);
        truckService.addTruck("T1", "X", 3000, 12000, LicenseType.C);  // עכשיו זה עובד כראוי עם הפרמטרים הנכונים
        truckService.addTruck("T2", "X", 1000, 3000, LicenseType.C);  // עכשיו זה עובד כראוי עם הפרמטרים הנכונים
        truckService.addTruck("T3", "X", 2000, 10000, LicenseType.C);  // עכשיו זה עובד כראוי עם הפרמטרים הנכונים
        driverService.addDriver("D1", "John Doe", "050-1234567", LicenseType.C);
        driverService.addDriver("D2", "gali niv", "050-1234577", LicenseType.C);
        driverService.addDriver("D3", "gal la", "050-1234577", LicenseType.C);
        siteService.addSite("SRC", "Source Site", "123 Source St.", "050-1111111", "Contact", ShippingZone.NORTH);
    }

    @Test
    public void testCreateAndGetOrder() {
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(1, "Box", 2, 10)));
        assertNotNull(order);
        assertEquals(1, orderService.getAllOrders().size());
    }

    @Test
    public void testGetOrderById() {
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(1, "Item", 1, 5)));
        Order found = orderService.getOrderById(order.getId());
        assertNotNull(found);
        assertEquals(order.getId(), found.getId());
    }

    @Test
    public void testUpdateOrderStatus() {
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(1, "Item", 1, 5)));
        boolean updated = orderService.updateOrderStatus(order.getId(), OrderStatus.IN_PROGRESS);
        assertTrue(updated);
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
    }


    @Test
    public void testAssignTransportToOrder_Success() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(2), "T1", "D1", "SRC", List.of("SRC"));

        // בדיקת אם התחבורה לא null
        assertNotNull(t);

        // יצירת הזמנה עם פריט
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(1, "I", 1, 1000)));

        // שיוך תחבורה להזמנה
        boolean ok = orderService.assignTransportToOrder(order.getId(), t.getId());

        // בדיקות
        assertTrue(ok);
        assertEquals(t, order.getTransport());
    }


    @Test
    public void testAssignTransportToOrder_Overweight() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(2), "T2", "D2", "SRC", List.of("SRC"));
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(2, "Heavy", 1, 10000)));
        boolean ok = orderService.assignTransportToOrder(order.getId(), t.getId());
        assertFalse(ok);
    }

    @Test
    public void testRemoveOrderFromTruck() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now(), "T1", "D1", "SRC", List.of("SRC"));
        t.setTruck(new Truck("T1", "X", 3000, 12000, LicenseType.C));
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(3, "I", 1, 1000)));
        orderService.assignTransportToOrder(order.getId(), t.getId());
        boolean removed = orderService.removeOrderFromTruck(order.getId());
        assertTrue(removed);
        assertNull(order.getTransport());
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    public void testCancelOrder_Success() {
//        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now(), "T3", "D3", "SRC", List.of("SRC"));
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(1, "Item", 1, 1000)));
//        orderService.assignTransportToOrder(order.getId(), t.getId());
        boolean canceled = orderService.cancelOrder(order.getId());
        assertTrue(canceled);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    public void testRemoveItemsFromOrder() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now(), "T1", "D1", "SRC", List.of("SRC"));
        t.setTruck(new Truck("T1", "X", 3000, 12000, LicenseType.C));
        Item item1 = new Item(10, "Box", 2, 5); // total 10kg
        Item item2 = new Item(11, "Chair", 1, 20); // total 20kg
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(item1, item2));
        orderService.assignTransportToOrder(order.getId(), t.getId());

        boolean removed = orderService.removeItems(order.getId(), t.getId(), List.of(item2));
        assertTrue(removed);
        assertEquals(1, order.getItems().size());
        assertFalse(order.getItems().contains(item2));
        assertTrue(order.getItems().contains(item1));
    }
    @Test
    public void testGetOrdersByStatus() {
        Order o1 = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(1, "Item1", 1, 5)));
        Order o2 = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(2, "Item2", 1, 10)));

        orderService.updateOrderStatus(o1.getId(), OrderStatus.DONE);
        orderService.updateOrderStatus(o2.getId(), OrderStatus.IN_PROGRESS);

        List<Order> doneOrders = orderService.getOrdersByStatus(OrderStatus.DONE);
        List<Order> inProgressOrders = orderService.getOrdersByStatus(OrderStatus.IN_PROGRESS);

        assertEquals(1, doneOrders.size());
        assertEquals(o1.getId(), doneOrders.get(0).getId());

        assertEquals(1, inProgressOrders.size());
        assertEquals(o2.getId(), inProgressOrders.get(0).getId());
    }

    @Test
    public void testGetOrdersByDate() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        orderService.createOrder(today, "SRC", List.of(new Item(3, "TodayItem", 1, 2)));
        orderService.createOrder(tomorrow, "SRC", List.of(new Item(4, "FutureItem", 1, 2)));

        List<Order> todayOrders = orderService.getOrdersByDate(today);
        List<Order> futureOrders = orderService.getOrdersByDate(tomorrow);

        assertEquals(1, todayOrders.size());
        assertEquals("TodayItem", todayOrders.get(0).getItems().get(0).getName());

        assertEquals(1, futureOrders.size());
        assertEquals("FutureItem", futureOrders.get(0).getItems().get(0).getName());
    }
    @Test
    public void testGetOrdersInTransport() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now(), "T1", "D1", "SRC", List.of("SRC"));
        t.setTruck(new Truck("T1", "X", 3000, 15000, LicenseType.C));

        Order o1 = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(5, "Box1", 1, 1000)));
        Order o2 = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(6, "Box2", 1, 1500)));

        orderService.assignTransportToOrder(o1.getId(), t.getId());
        orderService.assignTransportToOrder(o2.getId(), t.getId());

        List<Order> result = orderService.getOrdersInTransport(t.getId());
        assertEquals(2, result.size());
        assertTrue(result.contains(o1));
        assertTrue(result.contains(o2));
    }
    @Test
    public void testAssignOrder_ExactCapacity() {
        // Truck: empty 3000, max 12000 → cargo capacity = 9000. Order weight = 9000. Should succeed.
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("SRC"));
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(20, "MaxLoad", 1, 9000)));
        boolean ok = orderService.assignTransportToOrder(order.getId(), t.getId());
        assertTrue(ok);
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
    }

    @Test
    public void testAssignOrder_ZeroWeightItem() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("SRC"));
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(21, "Envelope", 1, 0)));
        boolean ok = orderService.assignTransportToOrder(order.getId(), t.getId());
        assertTrue(ok);
        assertEquals(0.0, t.getCurrentWeight());
    }

    @Test
    public void testAssignOrder_AlreadyAssigned() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("SRC"));
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(22, "Box", 1, 100)));
        orderService.assignTransportToOrder(order.getId(), t.getId());
        boolean second = orderService.assignTransportToOrder(order.getId(), t.getId());
        assertFalse(second); // already assigned, should not double-assign
    }

    @Test
    public void testAssignOrder_CancelledOrderCannotBeAssigned() {
        Transport t = transportService.createTransport(LocalDate.now(), LocalTime.now().plusHours(1), "T1", "D1", "SRC", List.of("SRC"));
        Order order = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(23, "Box", 1, 100)));
        orderService.cancelOrder(order.getId());
        boolean ok = orderService.assignTransportToOrder(order.getId(), t.getId());
        assertFalse(ok);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    public void testCancelOrder_WhenNotAllowed() {
        Order o = orderService.createOrder(LocalDate.now(), "SRC", List.of(new Item(7, "Uncancelable", 1, 500)));
        orderService.updateOrderStatus(o.getId(), OrderStatus.DONE);

        boolean result = orderService.cancelOrder(o.getId());
        assertFalse(result);
        assertEquals(OrderStatus.DONE, o.getStatus());
    }


}
