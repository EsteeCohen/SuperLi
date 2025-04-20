package domainLayer;

public class AvailabilityDL {
    private final ShiftDL shift;
    private final EmployeeDL employee;
    private boolean isAvailable;

    public AvailabilityDL(ShiftDL shift, EmployeeDL employee) {
        this.shift = shift;
        this.employee = employee;
        this.isAvailable = false; // Default to unavailable
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean available) {
        isAvailable = available;
    }

    public ShiftDL getShift() {
        return shift;
    }

    public EmployeeDL getEmployee() {
        return employee;
    }
}
