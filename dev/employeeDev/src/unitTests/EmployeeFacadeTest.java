package employeeDev.src.unitTests;

import employeeDev.src.domainLayer.DriverDL;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.SiteFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;
import transportDev.src.main.enums.ShippingZone;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeFacadeTest {
    private static final String TEST_EMPLOYEE_ID_1 = "11111";
    private static final String TEST_DRIVER_ID = "22222";
    private static final String TEST_EMPLOYEE_ID_2 = "44444";
    private static final String TEST_EMPLOYEE_ID_3 = "55555";
    private static final String TEST_SITE = "site";
    private static final String TEST_ROLE_MANAGER = "Manager";
    private static final String TEST_ROLE_DRIVER = "Driver";

    private RoleFacade roleFacade;
    private SiteFacade siteFacade;
    private EmployeeFacade employeeFacade;
    private Site site;
    private RoleDL roleDriver;
    private EmployeeDL employee;
    private DriverDL driver;

    @BeforeEach
    void setUp() {
        Site testSite = new Site(TEST_SITE, "address", "contact", "phone", ShippingZone.CENTER);
        employeeDev.src.dataAcssesLayer.SiteDAO siteDAO = new employeeDev.src.dataAcssesLayer.SiteDAO();
        if (siteDAO.getSite(TEST_SITE) == null) {
            siteDAO.addSite(new employeeDev.src.dtos.SiteDTO(TEST_SITE, "address", "contact", "phone", "CENTER"));
        }
        employeeDev.src.dataAcssesLayer.EmployeeDAO dao = new employeeDev.src.dataAcssesLayer.EmployeeDAO();
        dao.deleteDriver(TEST_DRIVER_ID);
        dao.deleteEmployee(TEST_DRIVER_ID);
        dao.deleteEmployee(TEST_EMPLOYEE_ID_1);
        dao.deleteEmployee(TEST_EMPLOYEE_ID_2);
        dao.deleteEmployee(TEST_EMPLOYEE_ID_3);

        roleFacade = new RoleFacade();
        siteFacade = new SiteFacade();
        siteFacade.addSite(testSite);
        employeeFacade = new EmployeeFacade(roleFacade, siteFacade);
        site = testSite;
        roleDriver = new RoleDL(TEST_ROLE_DRIVER);
        employee = new EmployeeDL(TEST_EMPLOYEE_ID_1, "pass", "Test Employee", LocalDate.now(), 100, 'H', 10, 10, site, "0500000000");
        driver = new DriverDL(TEST_DRIVER_ID, "pass", "test driver", LocalDate.now(), 100, 'H', 10, 10, site, "0500000000", LicenseType.C1, roleDriver, true);
    }

    @AfterEach
    void cleanUp() {
        employeeDev.src.dataAcssesLayer.EmployeeDAO dao = new employeeDev.src.dataAcssesLayer.EmployeeDAO();
        dao.deleteDriver(TEST_DRIVER_ID);
        dao.deleteEmployee(TEST_DRIVER_ID);
        dao.deleteEmployee(TEST_EMPLOYEE_ID_1);
        dao.deleteEmployee(TEST_EMPLOYEE_ID_2);
        dao.deleteEmployee(TEST_EMPLOYEE_ID_3);
        new employeeDev.src.dataAcssesLayer.SiteDAO().deleteSite(TEST_SITE);
    }

    @Test
    void testAddAndGetEmployee() {
        employeeFacade.addEmployee(employee);
        assertEquals(employee, employeeFacade.getEmployee(TEST_EMPLOYEE_ID_1));
    }

    @Test
    void testLoginSuccess() {
        employeeFacade.addEmployee(employee);
        assertEquals(employee, employeeFacade.login(TEST_EMPLOYEE_ID_1, "pass"));
    }

    @Test
    void testLoginFail() {
        employeeFacade.addEmployee(employee);
        assertNull(employeeFacade.login(TEST_EMPLOYEE_ID_1, "wrong"));
        assertNull(employeeFacade.login("notfound", "pass"));
    }

    @Test
    void testRegisterEmployee() {
        boolean result = employeeFacade.registerEmployee("Full Name", "pass", TEST_EMPLOYEE_ID_2, 100, 'H', 10, 10, TEST_SITE, "123");
        assertTrue(result);
        assertNotNull(employeeFacade.getEmployee(TEST_EMPLOYEE_ID_2));
    }

    @Test
    void testRegisterEmployeeDuplicate() {
        employeeFacade.registerEmployee("Full Name", "pass", TEST_EMPLOYEE_ID_3, 100, 'H', 10, 10, TEST_SITE, "123");
        boolean result = employeeFacade.registerEmployee("Full Name", "pass", TEST_EMPLOYEE_ID_3, 100, 'H', 10, 10, TEST_SITE, "123");
        assertFalse(result);
    }

    @Test
    void testAssignRoleToEmployee() {
        employeeFacade.addEmployee(employee);
        roleFacade.add(TEST_ROLE_MANAGER);
        assertTrue(employeeFacade.assignRoleToEmployee(TEST_EMPLOYEE_ID_1, TEST_ROLE_MANAGER));
        assertTrue(employee.getRoles().stream().anyMatch(r -> r.getName().equals(TEST_ROLE_MANAGER)));
    }

    @Test
    void testAssignRoleToEmployeeNotFound() {
        assertFalse(employeeFacade.assignRoleToEmployee("notfound", TEST_ROLE_MANAGER));
    }

    @Test
    void testGetAllEmployees() {
        employeeFacade.addEmployee(employee);
        assertTrue(employeeFacade.getAllEmployees().contains(employee));
    }

    @Test
    void testGetEmployeesBySite() {
        employeeFacade.addEmployee(employee);
        assertTrue(employeeFacade.getEmployeesBySite(site).contains(employee));
    }

    @Test
    void testGetDrivers() {
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteDriver(TEST_DRIVER_ID);
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteEmployee(TEST_DRIVER_ID);
        employeeFacade.addEmployee(driver);
        List<DriverDL> drivers = employeeFacade.getDrivers();
        assertTrue(drivers.stream().anyMatch(d -> d.getId().equals(TEST_DRIVER_ID)));
    }

    @Test
    void testUpdateEmployeeAttributes() {
        employeeFacade.addEmployee(employee);
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("fullName", "Updated Name");
        employeeFacade.updateEmployeeAttributes(TEST_EMPLOYEE_ID_1, attrs);
        EmployeeDL updated = employeeFacade.getEmployee(TEST_EMPLOYEE_ID_1);
        assertEquals("Updated Name", updated.getFullName());
    }

    @Test
    void testCanDriverDrive() {
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteDriver(TEST_DRIVER_ID);
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteEmployee(TEST_DRIVER_ID);
        employeeFacade.addEmployee(driver);
        assertTrue(employeeFacade.canDriverDrive(TEST_DRIVER_ID, LicenseType.C1));
    }

    @Test
    void testSetAvailableToDrive() {
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteDriver(TEST_DRIVER_ID);
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteEmployee(TEST_DRIVER_ID);
        employeeFacade.addEmployee(driver);
        employeeFacade.setAvailableToDrive(TEST_DRIVER_ID, false);
        DriverDL updated = (DriverDL) employeeFacade.getEmployee(TEST_DRIVER_ID);
        assertFalse(updated.isAvailableToDrive());
    }

    @Test
    void testUnassignRoleFromEmployee() {
        employeeFacade.addEmployee(employee);
        roleFacade.add(TEST_ROLE_MANAGER);
        employeeFacade.assignRoleToEmployee(TEST_EMPLOYEE_ID_1, TEST_ROLE_MANAGER);
        assertTrue(employeeFacade.unassignRoleFromEmployee(TEST_EMPLOYEE_ID_1, TEST_ROLE_MANAGER));
        assertFalse(employee.getRoles().stream().anyMatch(r -> r.getName().equals(TEST_ROLE_MANAGER)));
    }

    @Test
    void testUnassignRoleFromEmployeeDriverRole() {
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteDriver(TEST_DRIVER_ID);
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteEmployee(TEST_DRIVER_ID);
        employeeFacade.addEmployee(driver);
        assertFalse(employeeFacade.unassignRoleFromEmployee(TEST_DRIVER_ID, TEST_ROLE_DRIVER));
    }
}