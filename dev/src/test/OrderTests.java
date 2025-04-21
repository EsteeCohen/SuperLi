package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.entities.*;
import src.main.enums.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTests {

    private Site site;
    private List<Item> items;

    @BeforeEach
    public void setup() {
        site = new Site("S1", "מחסן ראשי", "תל אביב", "0501111111", "נועם", ShippingZone.CENTER);
        items = new ArrayList<>();
        items.add(new Item(1, "שק קמח", 2, 5));  // 10 ק"ג
        items.add(new Item(2, "בקבוק שמן", 1, 3)); // 3 ק"ג
    }

    @Test
    public void testValidOrderCreation() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(2, order.getItems().size());
    }

    @Test
    public void testSetDateInPastThrowsException() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        assertThrows(IllegalArgumentException.class, () -> order.setDate(LocalDate.now().minusDays(1)));
    }

    @Test
    public void testTotalOrderWeightCalculation() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        assertEquals(13.0, order.OrderWeight());
    }

    @Test
    public void testSetStatusWorks() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        order.setStatus(OrderStatus.DONE);
        assertEquals(OrderStatus.DONE, order.getStatus());
    }

    @Test
    public void testSetTransportAffectsTotalWeight() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        Driver driver = new Driver("D1", "יוסי", "0501234567", LicenseType.C);
        Truck truck = new Truck("123-45-678", "MAN", 5000, 10000, LicenseType.C);
        Transport transport = new Transport(LocalDate.now().plusDays(1),
                java.time.LocalTime.now().plusHours(1),
                truck, driver, site, List.of(site));
        order.setTransport(transport);
        transport.setCurrentWeight(order.OrderWeight());
        assertEquals(5000 + 13.0, transport.getTotalWeight());
    }

    @Test
    public void testCanBeCancelledWhenCreated() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        assertTrue(order.canBeCancelled());
    }

    @Test
    public void testCannotCancelAfterDone() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        order.setStatus(OrderStatus.DONE);
        assertFalse(order.canBeCancelled());
    }

    @Test
    public void testSetAndRemoveItems() {
        Order order = new Order(LocalDate.now().plusDays(1), site, new ArrayList<>(items));
        List<Item> newList = new ArrayList<>();
        newList.add(new Item(3, "חלב", 1, 1));
        order.setItems(newList);
        assertEquals(1, order.getItems().size());
        assertEquals("חלב", order.getItems().getFirst().getName());
    }

    @Test
    public void testAssignTransportAndCheckWeight() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items); // 13 ק"ג
        assertEquals(13.0, order.OrderWeight());

        Truck truck = new Truck("123-00-111", "Iveco", 5000, 10000, LicenseType.C);
        Driver driver = new Driver("D9", "ליאור", "0522222222", LicenseType.C);
        Transport transport = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, site, List.of(site));
        transport.setCurrentWeight(order.OrderWeight());

        order.setTransport(transport);

        assertEquals(13.0 + 5000, transport.getTotalWeight());
    }

    @Test
    public void testTransportAffectsCanBeCancelled() {
        Order order = new Order(LocalDate.now().plusDays(1), site, items);
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2),
                new Truck("444", "MAN", 5000, 8000, LicenseType.C),
                new Driver("D5", "נועם", "0500000000", LicenseType.C),
                site, List.of(site));
        order.setTransport(t);
        order.setStatus(OrderStatus.IN_PROGRESS);

        assertFalse(order.canBeCancelled());
    }
    @Test
    public void testRemoveItemsFromOrderAndUpdateTransportWeight() {
        // יצירת הזמנה עם פריטים
        List<Item> orderItems = new ArrayList<>();
        Item item1 = new Item(1, "שמן", 2, 4); // 8 ק"ג
        Item item2 = new Item(2, "קמח", 1, 5); // 5 ק"ג
        orderItems.add(item1);
        orderItems.add(item2);

        Order order = new Order(LocalDate.now().plusDays(1), site, new ArrayList<>(orderItems));
        double originalOrderWeight = order.OrderWeight(); // 13

        // שיוך הובלה
        Truck truck = new Truck("111-22-333", "DAF", 5000, 10000, LicenseType.C);
        Driver driver = new Driver("D5", "שירה", "0521231234", LicenseType.C);
        Transport transport = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2),
                truck, driver, site, List.of(site));

        transport.setCurrentWeight(originalOrderWeight);
        order.setTransport(transport);

        // הורדת פריט אחד
        List<Item> newItems = new ArrayList<>();
        newItems.add(item2); // רק 5 ק"ג נשאר

        order.setItems(newItems);

        // עדכון משקל בהובלה בהתאם
        double newWeight = newItems.stream().mapToDouble(Item::getTotalWeight).sum();
        transport.setCurrentWeight(newWeight);

        // בדיקות
        assertEquals(5.0, order.OrderWeight());
        assertEquals(5.0, transport.getCurrentWeight());
        assertEquals(5000 + 5.0, transport.getTotalWeight());
    }


}
