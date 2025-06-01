package employeeDev.src.presentationLayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import employeeDev.src.serviceLayer.AssigningService;
import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.ShiftService;

public class CurrentShiftsTablePresentation extends Form {
    private ShiftService shiftService;
    private EmployeeService employeeService;
    private AssigningService assigningService;
    private Scanner scanner;
    private ArrayList<ShiftPL> shifts;
    public CurrentShiftsTablePresentation(ShiftService shiftService, Scanner scanner, AssigningService assigningService,
            EmployeeService employeeService) {
        super("Weekly Shift Table");
        this.assigningService = assigningService;
        this.employeeService = employeeService;
        this.scanner = scanner;
        shifts = new ArrayList<>(shiftService.getAllShift().stream()
                                             .map(shiftSL -> new ShiftPL(shiftSL))
                                             .toList());
        shifts.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));
    }

    public void showCurrentShiftsAndEmployees() {
        if (shifts.isEmpty()) {
            System.out.println("No shifts available.");
            return;
        }
        for (ShiftPL shift : shifts) {
           if(shift.getStartTime().isBefore(java.time.LocalDateTime.now()) && shift.getEndTime().isAfter(java.time.LocalDateTime.now())) {
                System.out.println("-----------------------------------");
                System.out.println("Shift Number: " + (shifts.indexOf(shift) + 1)); // Display shift number
                System.out.println("Start Time: " + shift.getStartTime());
                System.out.println("End Time: " + shift.getEndTime());
                System.out.println("Shift Type: " + shift.getShiftType());
                System.out.println("Assigned Employees:");
                shift.getEmployeesAssignment().forEach((role, employees) -> {
                    System.out.println("  Role: " + role.getName());
                    employees.forEach(employee -> System.out.println("    - " + employee.getFullName() + " (ID: " + employee.getID() + ")"));
                });
            }
        }
    }

}
