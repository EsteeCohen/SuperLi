package transportDev.src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transportDev.src.main.entities.Truck;
import transportDev.src.main.enums.LicenseType;
import transportDev.src.main.services.TruckService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TruckTests {
    private TruckService truckService;


    @BeforeEach
    public void setUp() {
        truckService = new TruckService();
    }

    @Test
    public void testAddTruck_Valid() {
        Truck t = truckService.addTruck("123", "Volvo", 8000, 12000, LicenseType.C1);
        assertNotNull(t);
        assertEquals("123", t.getRegNumber());
    }

    @Test
    public void testAddTruck_Duplicate() {
        truckService.addTruck("123", "Volvo", 8000, 12000, LicenseType.C1);
        Truck t2 = truckService.addTruck("123", "MAN", 7000, 10000, LicenseType.C);
        assertNull(t2);
    }

    @Test
    public void testGetTruckByRegNumber() {
        truckService.addTruck("123", "Volvo", 8000, 12000, LicenseType.C1);
        Truck t = truckService.getTruckByRegNumber("123");
        assertNotNull(t);
        assertEquals("Volvo", t.getModel());
    }

    @Test
    public void testGetAllTrucks() {
        truckService.addTruck("1", "A", 8000, 12000, LicenseType.C);
        truckService.addTruck("2", "B", 6000, 9000, LicenseType.C1);
        List<Truck> list = truckService.getAllTrucks();
        assertEquals(2, list.size());
    }

    @Test
    public void testGetTrucksByLicenseType() {
        truckService.addTruck("1", "A", 8000, 12000, LicenseType.C);
        truckService.addTruck("2", "B", 6000, 9000, LicenseType.C1);
        List<Truck> list = truckService.getTrucksByLicenseType(LicenseType.C);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetTrucksWithCapacityForWeight() {
        truckService.addTruck("1", "A", 5000, 16000, LicenseType.C);
        truckService.addTruck("2", "B", 6000, 9000, LicenseType.C1);
        List<Truck> list = truckService.getTrucksWithCapacityForWeight(9500);
        assertEquals(1, list.size());
        assertEquals("1", list.get(0).getRegNumber());
    }

    @Test
    public void testDeleteTruck() {
        truckService.addTruck("123", "A", 8000, 12000, LicenseType.C);
        assertTrue(truckService.deleteTruck("123"));
        assertNull(truckService.getTruckByRegNumber("123"));
    }

    @Test
    public void testDeleteTruck_NotFound() {
        assertFalse(truckService.deleteTruck("999"));
    }

    @Test
    public void testClearAllTrucks() {
        truckService.addTruck("123", "X", 5000, 8000, LicenseType.C);
        truckService.clearAllTrucks();
        assertTrue(truckService.getAllTrucks().isEmpty());
    }
}
