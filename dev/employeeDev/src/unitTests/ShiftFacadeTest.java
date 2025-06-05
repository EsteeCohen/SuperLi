package employeeDev.src.unitTests;

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
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShiftFacadeTest {
    private static final String TEST_EMPLOYEE_ID = "emp1";
    private static final String TEST_SITE = "TestSite";
    private static final String TEST_ROLE = "TestRole";

    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private RoleFacade roleFacade;
    private ShiftFacade shiftFacade;
    private Site site;
    private RoleDL role;
    private EmployeeDL employee;

    @BeforeEach
    void setUp() {
        employeeDev.src.dataAcssesLayer.SiteDAO siteDAO = new employeeDev.src.dataAcssesLayer.SiteDAO();
        if (siteDAO.getSite(TEST_SITE) == null) {
            siteDAO.addSite(new employeeDev.src.dtos.SiteDTO(TEST_SITE, "Test Address", "123456789", "Contact Name", "CENTER"));
        }
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteEmployee(TEST_EMPLOYEE_ID);
        roleFacade = new RoleFacade();
        siteFacade = new SiteFacade();
        site = new Site(TEST_SITE, "Test Address", "123456789", "Contact Name", ShippingZone.CENTER);
        siteFacade.addSite(site);
        employeeFacade = new EmployeeFacade(roleFacade, siteFacade);
        shiftFacade = new ShiftFacade(employeeFacade, siteFacade, roleFacade);
        role = new RoleDL(TEST_ROLE);
        employee = new EmployeeDL(TEST_EMPLOYEE_ID, "password", "Test Name", LocalDate.of(2024, 1, 1), 100, 'H', 10, 10, site, "050-0000000");
    }

    @AfterEach
    void cleanUp() {
        new employeeDev.src.dataAcssesLayer.EmployeeDAO().deleteEmployee(TEST_EMPLOYEE_ID);
        new employeeDev.src.dataAcssesLayer.SiteDAO().deleteSite(TEST_SITE);
    }

    @Test
    void testAddAndGetShift() {
        ShiftDL newShift = new ShiftDL(LocalDateTime.of(2024, 6, 6, 8, 0), LocalDateTime.of(2024, 6, 6, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shiftFacade.addShift(newShift);
        ShiftDL found = shiftFacade.getShiftByStartTimeAndSite(LocalDateTime.of(2024, 6, 6, 8, 0), site);
        assertEquals(newShift, found);
    }

    @Test
    void testAssignToShiftThrowsIfNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
            shiftFacade.assignToShift(employee, LocalDateTime.of(2024, 6, 7, 8, 0), site, role)
        );
    }

    @Test
    void testUnassignToShiftThrowsIfNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
            shiftFacade.unassignToShift(employee, LocalDateTime.of(2024, 6, 7, 8, 0), site, role)
        );
    }

    @Test
    void testGetAllShiftsFromSite() {
        ShiftDL shift1 = new ShiftDL(LocalDateTime.of(2024, 6, 5, 8, 0), LocalDateTime.of(2024, 6, 5, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        ShiftDL shift2 = new ShiftDL(LocalDateTime.of(2024, 6, 6, 8, 0), LocalDateTime.of(2024, 6, 6, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shiftFacade.addShift(shift1);
        shiftFacade.addShift(shift2);
        List<ShiftDL> shifts = shiftFacade.getAllShiftsFromSite(site);
        assertEquals(2, shifts.size());
    }

    @Test
    void testGetAllShiftsInRange() {
        ShiftDL shift1 = new ShiftDL(LocalDateTime.of(2024, 6, 5, 8, 0), LocalDateTime.of(2024, 6, 5, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shiftFacade.addShift(shift1);
        List<ShiftDL> shifts = shiftFacade.getAllShiftsInRange(LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 6), site);
        assertTrue(shifts.contains(shift1));
    }

    @Test
    void testGetWeeklyShifts() {
        ShiftDL shift1 = new ShiftDL(LocalDateTime.of(2024, 6, 5, 8, 0), LocalDateTime.of(2024, 6, 5, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shiftFacade.addShift(shift1);
        List<ShiftDL> shifts = shiftFacade.getWeeklyShifts(LocalDate.of(2024, 6, 3), site);
        assertTrue(shifts.contains(shift1));
    }

    @Test
    void testCheckIfThereAreShiftsThatAreNotFullyAssigned() {
        ShiftDL shift1 = new ShiftDL(LocalDateTime.of(2024, 6, 5, 8, 0), LocalDateTime.of(2024, 6, 5, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shift1.setIntoRequirements(role, 1);
        shiftFacade.addShift(shift1);
        boolean result = shiftFacade.checkIfThereAreShiftsThatAreNotFullyAssigned(site, LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 6));
        assertTrue(result);
    }

    @Test
    void testGetAllUnassignedEmployeesForShift() {
        ShiftDL shift1 = new ShiftDL(LocalDateTime.of(2024, 6, 5, 8, 0), LocalDateTime.of(2024, 6, 5, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shiftFacade.addShift(shift1);
        employeeFacade.addEmployee(employee);
        List<EmployeeDL> unassigned = shiftFacade.getAllUnassignedEmployeesForShift(LocalDateTime.of(2024, 6, 5, 8, 0), site);
        assertTrue(unassigned.contains(employee));
    }

    @Test
    void testGetShiftAtTime() {
        ShiftDL shift1 = new ShiftDL(LocalDateTime.of(2024, 6, 5, 8, 0), LocalDateTime.of(2024, 6, 5, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shiftFacade.addShift(shift1);
        ShiftDL found = shiftFacade.getShiftAtTime(LocalDateTime.of(2024, 6, 5, 10, 0), site);
        assertEquals(shift1, found);
    }

    @Test
    void testGetAllShiftsWithMissingAssigns() {
        ShiftDL shift1 = new ShiftDL(LocalDateTime.of(2024, 6, 5, 8, 0), LocalDateTime.of(2024, 6, 5, 16, 0), employeeDev.src.domainLayer.Enums.ShiftType.MORNING, site);
        shift1.setIntoRequirements(role, 1);
        shiftFacade.addShift(shift1);
        List<ShiftDL> missing = shiftFacade.getAllShiftsWithMissingAssigns(site, LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 6));
        assertTrue(missing.contains(shift1));
    }
}