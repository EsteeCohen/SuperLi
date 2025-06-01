package unitTests;

import domainLayer.*;
import domainLayer.Enums.ShiftType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShiftFacadeTest {

    private ShiftFacade shiftFacade;
    private EmployeeDL employee;
    private RoleDL role;
    private ShiftDL shift;

    @BeforeEach
    void setUp() {
        shiftFacade = new ShiftFacade();
        employee = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        role = new RoleDL("Manager");
        shift = new ShiftDL(LocalDate.of(2025, 4, 26), ShiftType.MORNING);
    }

    @Test
    void testAddShift() {
        shiftFacade.addShift(shift);
        ShiftDL retrievedShift = shiftFacade.getShiftByDateAndType(shift.getDate(), shift.getShiftType().toString());

        assertNotNull(retrievedShift);
        assertEquals(shift.getDate(), retrievedShift.getDate());
        assertEquals(shift.getShiftType(), retrievedShift.getShiftType());
    }

    @Test
    void testGetAllShifts() {
        shiftFacade.addShift(shift);
        ShiftDL anotherShift = new ShiftDL(LocalDate.of(2025, 4, 27), ShiftType.EVENING);
        shiftFacade.addShift(anotherShift);

        List<ShiftDL> allShifts = shiftFacade.getAllShifts();

        assertEquals(2, allShifts.size());
        assertTrue(allShifts.contains(shift));
        assertTrue(allShifts.contains(anotherShift));
    }

    @Test
    void testAssignToShift() {
        shiftFacade.addShift(shift);
        shiftFacade.assignToShift(employee, shift.getDate(), shift.getShiftType().toString(), role);

        assertTrue(shift.getEmployeesAssignment().get(role).contains(employee));
    }

    @Test
    void testAssignToNonexistentShift() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shiftFacade.assignToShift(employee, LocalDate.of(2025, 4, 27), "MORNING", role);
        });

        assertEquals("Shift not found for the given date and type", exception.getMessage());
    }

    @Test
    void testUnassignFromNonexistentShift() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shiftFacade.unassignToShift(employee, LocalDate.of(2025, 4, 27), "MORNING", role);
        });

        assertEquals("Shift not found for the given date and type", exception.getMessage());
    }

    @Test
    void testSetRequirements() {
        shiftFacade.setRequirements(DayOfWeek.MONDAY, ShiftType.MORNING, role, 3);

        int quantity = WeeklyShiftRequirements.getInstance()
                .getRequirements(DayOfWeek.MONDAY, ShiftType.MORNING)
                .get(role);

        assertEquals(3, quantity);
    }
}
