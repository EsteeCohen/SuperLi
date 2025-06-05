// package transportDev.src.test;


// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import transportDev.src.main.entities.Driver;
// import transportDev.src.main.entities.Truck;
// import transportDev.src.main.enums.LicenseType;
// import transportDev.src.main.services.DriverService;

// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// public class DriverTests {
//     private DriverService driverService;
//     @BeforeEach
//     public void setUp() {
//         driverService = new DriverService();
//     }

//     @Test
//     public void testAddDriver_Valid() {
//         Driver driver = driverService.addDriver("1", "Alice", "050-123", LicenseType.C1);
//         assertNotNull(driver);
//         assertEquals("Alice", driver.getName());
//     }

//     @Test
//     public void testAddDriver_DuplicateId() {
//         driverService.addDriver("1", "Alice", "050-123", LicenseType.C1);
//         assertThrows(IllegalArgumentException.class, () ->
//                 driverService.addDriver("1", "Bob", "050-456", LicenseType.C));
//     }

//     @Test
//     public void testAddDriver_NullFields() {
//         assertThrows(IllegalArgumentException.class, () ->
//                 driverService.addDriver(null, "Alice", "050", LicenseType.C));
//         assertThrows(IllegalArgumentException.class, () ->
//                 driverService.addDriver("2", null, "050", LicenseType.C));
//     }

//     @Test
//     public void testGetDriverById_Exists() {
//         driverService.addDriver("1", "Alice", "050", LicenseType.C1);
//         assertNotNull(driverService.getDriverById("1"));
//     }

//     @Test
//     public void testGetDriverById_NotFound() {
//         assertNull(driverService.getDriverById("999"));
//     }

//     @Test
//     public void testGetAllDrivers() {
//         driverService.addDriver("1", "A", "050", LicenseType.C1);
//         driverService.addDriver("2", "B", "051", LicenseType.C);
//         List<Driver> all = driverService.getAllDrivers();
//         assertEquals(2, all.size());
//     }

//     @Test
//     public void testGetDriversByLicenseType() {
//         driverService.addDriver("1", "A", "050", LicenseType.C1);
//         driverService.addDriver("2", "B", "051", LicenseType.C1);
//         driverService.addDriver("3", "C", "052", LicenseType.C);
//         List<Driver> result = driverService.getDriversByLicenseType(LicenseType.C1);
//         assertEquals(2, result.size());
//     }

//     @Test
//     public void testUpdateDriver() {
//         driverService.addDriver("1", "Alice", "050", LicenseType.C1);
//         driverService.updateDriver("1", "Alicia", "052", LicenseType.C);
//         Driver d = driverService.getDriverById("1");
//         assertEquals("Alicia", d.getName());
//         assertEquals("052", d.getPhone());
//         assertEquals(LicenseType.C, d.getLicenseType());
//     }

//     @Test
//     public void testUpdateDriver_NotFound() {
//         assertThrows(IllegalArgumentException.class, () ->
//                 driverService.updateDriver("999", "Name", "Phone", LicenseType.C));
//     }

//     @Test
//     public void testDeleteDriver_Exists() {
//         driverService.addDriver("1", "A", "050", LicenseType.C);
//         boolean deleted = driverService.deleteDriver("1");
//         assertTrue(deleted);
//         assertNull(driverService.getDriverById("1"));
//     }

//     @Test
//     public void testDeleteDriver_NotFound() {
//         assertFalse(driverService.deleteDriver("999"));
//     }

//     @Test
//     public void testClearAllDrivers() {
//         driverService.addDriver("1", "A", "050", LicenseType.C);
//         driverService.clearAllDrivers();
//         assertEquals(0, driverService.getAllDrivers().size());
//     }

// }
