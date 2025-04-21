package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.entities.*;
import src.main.enums.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransportTests {

    private Driver driver;
    private Truck truck;
    private Site sourceSite;
    private Site destination1;
    private Site destination2;

    @BeforeEach
    public void setup() {
        driver = new Driver("123", "Dana", "0522215232", LicenseType.C);
        truck = new Truck("444-55-666", "Volvo", 5000, 10000, LicenseType.C);
        sourceSite = new Site("S1", "מחסן ראשי", "תל אביב", "0501234567", "רוני", ShippingZone.CENTER);
        destination1 = new Site("S2", "לקוח א", "חולון", "0501111111", "יוסי", ShippingZone.CENTER);
        destination2 = new Site("S3", "לקוח ב", "בת ים", "0502222222", "אנה", ShippingZone.CENTER);
    }

    @Test
    public void testValidTransportCreation() {
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        assertEquals(TransportStatus.PLANNING, t.getStatus());
    }

    @Test
    public void testInvalidPastDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transport(LocalDate.now().minusDays(1), LocalTime.now(), truck, driver, sourceSite, List.of(destination1)));
    }

    @Test
    public void testInvalidPastTimeThrowsException() {
        LocalDate today = LocalDate.now();
        LocalTime validTime = LocalTime.now().plusHours(2);
        Transport t = new Transport(today, validTime, truck, driver, sourceSite, List.of(destination1));
        LocalTime pastTime = LocalTime.now().minusHours(1);
        assertThrows(IllegalArgumentException.class, () -> t.setTime(pastTime));
    }


    @Test
    public void testSetTruckToNullThrowsException() {
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        assertThrows(IllegalArgumentException.class, () -> t.setTruck(null));
    }

    @Test
    public void testSetDriverToNullThrowsException() {
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        assertThrows(IllegalArgumentException.class, () -> t.setDriver(null));
    }

    @Test
    public void testAddInvalidZoneDestinationFails() {
        Site badZoneSite = new Site("S9", "ירושלים", "ירושלים", "0500000000", "משה", ShippingZone.JERUSALEM);
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        assertFalse(t.validateSameRegion(List.of(destination1, badZoneSite)));
    }

    @Test
    public void testSetStatusUpdatesCorrectly() {
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        t.setStatus(TransportStatus.ACTIVE);
        assertEquals(TransportStatus.ACTIVE, t.getStatus());
    }

    @Test
    public void testValidWeightCheck() {
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        t.setCurrentWeight(1000);
        assertEquals(1000, t.getCurrentWeight());
        assertTrue(t.isWeightValid(t.getCurrentWeight()));
    }

    @Test
    public void testCannotCancelAfterInProgress() {
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        t.setStatus(TransportStatus.ACTIVE);
        assertFalse(t.canBeCancelled());
    }

    @Test
    public void testTotalWeightCalculation() {
        Transport t = new Transport(LocalDate.now().plusDays(1), LocalTime.now().plusHours(1),
                truck, driver, sourceSite, List.of(destination1));
        t.setCurrentWeight(2000);
        assertEquals(7000, t.getTotalWeight()); // 5000 + 2000
    }
}
