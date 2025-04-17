package src.serviceLayer;

public class ShiftSL {
    private final LocalDate date;
    private final ShiftType shiftType;
    private final Map<RoleSL, List<EmployeeSL>> employeesAssignment;
}
