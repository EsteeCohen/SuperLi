package employeeDev.src.unitTests;

import employeeDev.src.domainLayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeFacadeTest {

    private RoleFacade roleFacade;
    private SiteFacade siteFacade;
    private EmployeeFacade employeeFacade;
    private Site site;
    private RoleDL role;
    private EmployeeDL employee;
    private DriverDL driver;

    @BeforeEach
    void setUp() {
        roleFacade = mock(RoleFacade.class);
        siteFacade = mock(SiteFacade.class);
        employeeFacade = new EmployeeFacade(roleFacade, siteFacade);

        site = mock(Site.class);
        role = mock(RoleDL.class);

        employee = mock(EmployeeDL.class);
        when(employee.getId()).thenReturn("1");
        when(employee.getSite()).thenReturn(site);
        when(employee.hasRole(any())).thenReturn(true);
        when(employee.isDriver()).thenReturn(false);

        driver = mock(DriverDL.class);
        when(driver.getId()).thenReturn("2");
        when(driver.getSite()).thenReturn(site);
        when(driver.isDriver()).thenReturn(true);
        when(driver.isAvailableToDrive()).thenReturn(true);
        when(driver.isLicensed(any())).thenReturn(true);
    }

    @Test
    void testAddAndGetEmployee() {
        EmployeeDL e = mock(EmployeeDL.class);
        when(e.getId()).thenReturn("3");
        employeeFacade.addEmployee(e);
        assertEquals(e, employeeFacade.getEmployee("3"));
    }

    @Test
    void testLoginSuccess() {
        when(employee.IsPassword("pass")).thenReturn(true);
        employeeFacade.addEmployee(employee);
        assertEquals(employee, employeeFacade.login("1", "pass"));
    }

    @Test
    void testLoginFail() {
        when(employee.IsPassword("wrong")).thenReturn(false);
        employeeFacade.addEmployee(employee);
        assertNull(employeeFacade.login("1", "wrong"));
        assertNull(employeeFacade.login("notfound", "pass"));
    }

    @Test
    void testRegisterEmployee() {
        when(siteFacade.getSiteByName("site")).thenReturn(site);
        boolean result = employeeFacade.registerEmployee("Full Name", "pass", "4", 100, 'H', 10, 10, "site", "123");
        assertTrue(result);
        assertNotNull(employeeFacade.getEmployee("4"));
    }

    @Test
    void testRegisterEmployeeDuplicate() {
        when(siteFacade.getSiteByName("site")).thenReturn(site);
        employeeFacade.registerEmployee("Full Name", "pass", "5", 100, 'H', 10, 10, "site", "123");
        boolean result = employeeFacade.registerEmployee("Full Name", "pass", "5", 100, 'H', 10, 10, "site", "123");
        assertFalse(result);
    }

    @Test
    void testAssignRoleToEmployee() {
        employeeFacade.addEmployee(employee);
        when(roleFacade.getRoleByName("Manager")).thenReturn(role);
        when(employee.getRoles()).thenReturn(new ArrayList<>());
        assertTrue(employeeFacade.assignRoleToEmployee("1", "Manager"));
    }

    @Test
    void testAssignRoleToEmployeeNotFound() {
        assertFalse(employeeFacade.assignRoleToEmployee("notfound", "Manager"));
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
        employeeFacade.addEmployee(driver);
        List<DriverDL> drivers = employeeFacade.getDrivers();
        assertTrue(drivers.contains(driver));
    }

    @Test
    void testUpdateEmployeeAttributes() {
        employeeFacade.addEmployee(employee);
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("phone", "123");
        employeeFacade.updateEmployeeAttributes("1", attrs);
        verify(employee, times(1)).updateAttributes(attrs);
        verify(employee, times(1)).updateInDB();
    }

    @Test
    void testCanDriverDrive() {
        employeeFacade.addEmployee(driver);
        assertTrue(employeeFacade.canDriverDrive("2", LicenseType.C1));
    }

    @Test
    void testSetAvailableToDrive() {
        employeeFacade.addEmployee(driver);
        employeeFacade.setAvailableToDrive("2", false);
        verify(driver, times(1)).setAvailableToDrive(false);
        verify(driver, times(1)).updateInDB();
    }

    @Test
    void testUnassignRoleFromEmployee() {
        employeeFacade.addEmployee(employee);
        when(employee.isDriver()).thenReturn(false);
        when(roleFacade.getRoleByName("Manager")).thenReturn(role);
        when(employee.removeRole("Manager")).thenReturn(true);
        assertTrue(employeeFacade.unassignRoleFromEmployee("1", "Manager"));
    }

    @Test
    void testUnassignRoleFromEmployeeDriverRole() {
        employeeFacade.addEmployee(driver);
        when(driver.isDriver()).thenReturn(true);
        assertFalse(employeeFacade.unassignRoleFromEmployee("2", "Driver"));
    }
}