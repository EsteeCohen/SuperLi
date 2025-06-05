package employeeDev.src.unitTests;

import employeeDev.src.domainLayer.AvailabilityDL;
import employeeDev.src.domainLayer.AvailabilityFacade;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.ShiftDL;
import employeeDev.src.domainLayer.ShiftFacade;
import employeeDev.src.domainLayer.SiteFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityFacadeTest {
    private static final String TEST_EMPLOYEE_ID_1 = "12345";
    private static final String TEST_EMPLOYEE_ID_2 = "67890";
    private static final String TEST_SITE_1 = "TestSite";
    private static final String TEST_SITE_2 = "TestSite2";
    private static final String TEST_ROLE = "TestRole";

    private ShiftFacade shiftFacade;
    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private AvailabilityFacade availabilityFacade;
    private RoleFacade roleFacade;
    private ShiftDL shift;
    private EmployeeDL employee;
    private RoleDL role;
    private AvailabilityDL availability;

    @BeforeEach
    void setUp() {
        roleFacade = new RoleFacade();
        siteFacade = new SiteFacade();
        employeeFacade = new EmployeeFacade(roleFacade, siteFacade);
        shiftFacade = new ShiftFacade(employeeFacade, siteFacade, roleFacade);
        availabilityFacade = new AvailabilityFacade(shiftFacade, employeeFacade, siteFacade);

        transportDev.src.main.entities.Site testSite = new transportDev.src.main.entities.Site(TEST_SITE_1, "Test Address", "123456789", "Contact Name", transportDev.src.main.enums.ShippingZone.CENTER);
        shift = new ShiftDL(LocalDateTime.now(), LocalDateTime.now().plusHours(8), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, testSite);
        employee = new EmployeeDL(TEST_EMPLOYEE_ID_1, "pass", "Test Employee", LocalDateTime.now().toLocalDate(), 100, 'H', 10, 10, testSite, "0500000000");
        role = new RoleDL(TEST_ROLE);
        employee.addRole(role);
        availability = new AvailabilityDL(employee, shift, true);
    }

    @Test
    void testAddAvailability() {
        availabilityFacade.addAvailability(availability);
        assertEquals(1, availabilityFacade.getAllAvailabilities().size());
    }

    @Test
    void testGetAvailabilitiesForShift() {
        availabilityFacade.addAvailability(availability);
        List<EmployeeDL> result = availabilityFacade.getAvailabilitiesForShift(shift);
        assertTrue(result.contains(employee));
    }

    @Test
    void testGetAvailabilitiesForShiftWithRole() {
        availabilityFacade.addAvailability(availability);
        List<EmployeeDL> result = availabilityFacade.getAvailabilitiesForShift(shift, role);
        assertTrue(result.contains(employee));
    }

    @Test
    void testGetAvailabilityForEmployee() {
        availabilityFacade.addAvailability(availability);
        AvailabilityDL found = availabilityFacade.getAvailabilityForEmployee(employee, shift);
        assertEquals(availability, found);
    }

    @Test
    void testGetAllAvailabilities() {
        availabilityFacade.addAvailability(availability);
        List<AvailabilityDL> all = availabilityFacade.getAllAvailabilities();
        assertEquals(1, all.size());
    }

    @Test
    void testGetAllAvailabilitiesWithTimeRange() {
        LocalDateTime now = LocalDateTime.now();
        transportDev.src.main.entities.Site testSite = new transportDev.src.main.entities.Site(TEST_SITE_2, "Test Address2", "987654321", "Contact Name2", transportDev.src.main.enums.ShippingZone.CENTER);
        ShiftDL shift2 = new ShiftDL(now.minusHours(1), now.plusHours(1), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, testSite);
        EmployeeDL employee2 = new EmployeeDL(TEST_EMPLOYEE_ID_2, "pass2", "Test Employee2", now.toLocalDate(), 100, 'H', 10, 10, testSite, "0500000001");
        AvailabilityDL availability2 = new AvailabilityDL(employee2, shift2, true);
        availabilityFacade.addAvailability(availability2);
        List<AvailabilityDL> result = availabilityFacade.getAllAvailabilities(now.minusHours(2), now.plusHours(2));
        assertTrue(result.contains(availability2));
    }

    @AfterEach
    void cleanUp() {
        employeeDev.src.dataAcssesLayer.EmployeeDAO dao = new employeeDev.src.dataAcssesLayer.EmployeeDAO();
        dao.deleteEmployee(TEST_EMPLOYEE_ID_1);
        dao.deleteEmployee(TEST_EMPLOYEE_ID_2);
        employeeDev.src.dataAcssesLayer.SiteDAO siteDAO = new employeeDev.src.dataAcssesLayer.SiteDAO();
        siteDAO.deleteSite(TEST_SITE_1);
        siteDAO.deleteSite(TEST_SITE_2);
        // Clean up all test availabilities for these employees
        employeeDev.src.dataAcssesLayer.AvilibilityDAO availabilityDAO = new employeeDev.src.dataAcssesLayer.AvilibilityDAO();
        availabilityDAO.deleteAvailability(TEST_EMPLOYEE_ID_1);
        availabilityDAO.deleteAvailability(TEST_EMPLOYEE_ID_2);
    }
}