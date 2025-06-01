package unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domainLayer.AvailabilityDL;
import domainLayer.AvailabilityFacade;
import domainLayer.EmployeeDL;
import domainLayer.RoleDL;
import domainLayer.ShiftDL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityFacadeTest {

    private AvailabilityFacade availabilityFacade;
    private EmployeeDL employee;
    private ShiftDL shift;
    private RoleDL role;
    private AvailabilityDL availability;

    @BeforeEach
    void setUp() {
        availabilityFacade = new AvailabilityFacade();

        // Create real objects
        employee = new EmployeeDL("E001", "John Doe", LocalDate.of(2020, 1, 1), 5000, 'H', 10, 15);
        shift = new ShiftDL(LocalDate.of(2025, 4, 26), "Morning");
        role = new RoleDL("Manager");
        availability = new AvailabilityDL(employee, shift, true);
    }

    @Test
    void testAddAvailability() {
        availabilityFacade.addAvailability(availability);
        assertEquals(1, availabilityFacade.getAvailabilitiesForShift(shift).size());
    }

    @Test
    void testGetAvailabilitiesForShift() {
        availabilityFacade.addAvailability(availability);

        List<EmployeeDL> availableEmployees = availabilityFacade.getAvailabilitiesForShift(shift);
        assertEquals(1, availableEmployees.size());
        assertTrue(availableEmployees.contains(employee));
    }

    @Test
    void testGetAvailabilitiesForShiftWithRole() {
        employee.addRole(role); // Assign the role to the employee
        availabilityFacade.addAvailability(availability);

        List<EmployeeDL> availableEmployees = availabilityFacade.getAvailabilitiesForShift(shift, role);
        assertEquals(1, availableEmployees.size());
        assertTrue(availableEmployees.contains(employee));
    }

    @Test
    void testGetAvailabilityForEmployee() {
        availabilityFacade.addAvailability(availability);

        AvailabilityDL result = availabilityFacade.getAvailabilityForEmployee(employee, shift);
        assertNotNull(result);
        assertEquals(availability, result);
    }

    @Test
    void testGetAvailabilityForEmployeeNotFound() {
        AvailabilityDL result = availabilityFacade.getAvailabilityForEmployee(employee, shift);
        assertNull(result);
    }
}