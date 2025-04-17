package domainLayer;

public class Availability {
    private final Shift shift;
    private final EmployeeSL employee;
    private boolean isAvailable;

    public Availability(Shift shift, EmployeeSL employee, boolean isAvailable) {
        this.shift = shift;
        this.employee = employee;
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean available) {
        isAvailable = available;
    }

    public Shift getShift() {
        return shift;
    }

    public EmployeeSL getEmployee() {
        return employee;
    }
}
