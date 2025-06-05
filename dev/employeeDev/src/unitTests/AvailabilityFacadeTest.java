package employeeDev.src.unitTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import employeeDev.src.domainLayer.AvailabilityDL;
import employeeDev.src.domainLayer.AvailabilityFacade;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.ShiftDL;
import employeeDev.src.domainLayer.ShiftFacade;
import employeeDev.src.domainLayer.SiteFacade;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilityFacadeTest {

    private ShiftFacade shiftFacade;
    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private AvailabilityFacade availabilityFacade;

    private ShiftDL shift;
    private EmployeeDL employee;
    private RoleDL role;
    private AvailabilityDL availability;

    @BeforeEach
    void setUp() {
        shiftFacade = mock(ShiftFacade.class);
        employeeFacade = mock(EmployeeFacade.class);
        siteFacade = mock(SiteFacade.class);
        availabilityFacade = new AvailabilityFacade(shiftFacade, employeeFacade, siteFacade);

        shift = mock(ShiftDL.class);
        employee = mock(EmployeeDL.class);
        role = mock(RoleDL.class);

        availability = mock(AvailabilityDL.class);
        when(availability.getShift()).thenReturn(shift);
        when(availability.getEmployee()).thenReturn(employee);
        when(availability.isAvailable()).thenReturn(true);
        when(employee.hasRole(role)).thenReturn(true);
    }

    @Test
    void testAddAvailability() {
        availabilityFacade.addAvailability(availability);
        assertEquals(1, availabilityFacade.getAllAvailabilities().size());
        verify(availability, times(1)).presistIntoDB();
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
        when(shift.getStartTime()).thenReturn(now.minusHours(1));
        when(shift.getEndTime()).thenReturn(now.plusHours(1));
        availabilityFacade.addAvailability(availability);

        List<AvailabilityDL> result = availabilityFacade.getAllAvailabilities(now.minusHours(2), now.plusHours(2));
        assertTrue(result.contains(availability));
    }
}