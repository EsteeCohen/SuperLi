package employeeDev.src.unitTests;

import employeeDev.src.domainLayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transportDev.src.main.entities.Site;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftFacadeTest {

    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private RoleFacade roleFacade;
    private ShiftFacade shiftFacade;
    private Site site;
    private RoleDL role;
    private EmployeeDL employee;
    private ShiftDL shift;

    @BeforeEach
    void setUp() {
        employeeFacade = mock(EmployeeFacade.class);
        siteFacade = mock(SiteFacade.class);
        roleFacade = mock(RoleFacade.class);
        shiftFacade = new ShiftFacade(employeeFacade, siteFacade, roleFacade);

        site = mock(Site.class);
        when(site.getName()).thenReturn("TestSite");
        role = mock(RoleDL.class);
        when(role.getName()).thenReturn("TestRole");
        employee = mock(EmployeeDL.class);
        when(employee.getId()).thenReturn("emp1");

        shift = mock(ShiftDL.class);
        when(shift.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 8, 0));
        when(shift.getEndTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 16, 0));
        when(shift.getSite()).thenReturn(site);
        when(shift.meetTheRequirements()).thenReturn(true);
        when(shift.isEmployeeAssigned(any())).thenReturn(false);
    }

    @Test
    void testAddAndGetShift() {
        ShiftDL newShift = mock(ShiftDL.class);
        when(newShift.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 6, 8, 0));
        when(newShift.getSite()).thenReturn(site);
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
        ShiftDL shift1 = mock(ShiftDL.class);
        when(shift1.getSite()).thenReturn(site);
        ShiftDL shift2 = mock(ShiftDL.class);
        when(shift2.getSite()).thenReturn(site);
        shiftFacade.addShift(shift1);
        shiftFacade.addShift(shift2);
        List<ShiftDL> shifts = shiftFacade.getAllShiftsFromSite(site);
        assertEquals(2, shifts.size());
    }

    @Test
    void testGetAllShiftsInRange() {
        ShiftDL shift1 = mock(ShiftDL.class);
        when(shift1.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 8, 0));
        when(shift1.getSite()).thenReturn(site);
        shiftFacade.addShift(shift1);
        List<ShiftDL> shifts = shiftFacade.getAllShiftsInRange(LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 6), site);
        assertTrue(shifts.contains(shift1));
    }

    @Test
    void testGetWeeklyShifts() {
        ShiftDL shift1 = mock(ShiftDL.class);
        when(shift1.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 8, 0));
        when(shift1.getSite()).thenReturn(site);
        shiftFacade.addShift(shift1);
        List<ShiftDL> shifts = shiftFacade.getWeeklyShifts(LocalDate.of(2024, 6, 3), site);
        assertTrue(shifts.contains(shift1));
    }

    @Test
    void testCheckIfThereAreShiftsThatAreNotFullyAssigned() {
        ShiftDL shift1 = mock(ShiftDL.class);
        when(shift1.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 8, 0));
        when(shift1.getSite()).thenReturn(site);
        when(shift1.meetTheRequirements()).thenReturn(false);
        shiftFacade.addShift(shift1);
        boolean result = shiftFacade.checkIfThereAreShiftsThatAreNotFullyAssigned(site, LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 6));
        assertTrue(result);
    }

    @Test
    void testGetAllUnassignedEmployeesForShift() {
        ShiftDL shift1 = mock(ShiftDL.class);
        when(shift1.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 8, 0));
        when(shift1.getSite()).thenReturn(site);
        when(shift1.isEmployeeAssigned(any())).thenReturn(false);
        shiftFacade.addShift(shift1);

        List<EmployeeDL> allEmployees = Arrays.asList(employee);
        when(employeeFacade.getAllEmployees()).thenReturn(allEmployees);

        List<EmployeeDL> unassigned = shiftFacade.getAllUnassignedEmployeesForShift(LocalDateTime.of(2024, 6, 5, 8, 0), site);
        assertTrue(unassigned.contains(employee));
    }

    @Test
    void testGetShiftAtTime() {
        ShiftDL shift1 = mock(ShiftDL.class);
        when(shift1.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 8, 0));
        when(shift1.getEndTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 16, 0));
        when(shift1.getSite()).thenReturn(site);
        shiftFacade.addShift(shift1);

        ShiftDL found = shiftFacade.getShiftAtTime(LocalDateTime.of(2024, 6, 5, 10, 0), site);
        assertEquals(shift1, found);
    }

    @Test
    void testGetAllShiftsWithMissingAssigns() {
        ShiftDL shift1 = mock(ShiftDL.class);
        when(shift1.getStartTime()).thenReturn(LocalDateTime.of(2024, 6, 5, 8, 0));
        when(shift1.getSite()).thenReturn(site);
        when(shift1.meetTheRequirements()).thenReturn(false);
        shiftFacade.addShift(shift1);

        List<ShiftDL> missing = shiftFacade.getAllShiftsWithMissingAssigns(site, LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 6));
        assertTrue(missing.contains(shift1));
    }
}