package employeeDev.src.domainLayer;

public class AvailabilityDL {
    private final ShiftDL shift;
    private final EmployeeDL employee;
    private boolean isAvailable;

    // Main constructor
    public AvailabilityDL(ShiftDL shift, EmployeeDL employee) {
        this.shift = shift;
        this.employee = employee;
        this.isAvailable = false; // Default to unavailable
    }

    // Constructor with availability parameter
    public AvailabilityDL(EmployeeDL employee, ShiftDL shift, boolean isAvailable) {
        this.shift = shift;
        this.employee = employee;
        this.isAvailable = isAvailable;
    }

    // Getter for availability
    public boolean isAvailable() {
        return isAvailable;
    }

    // Setter for availability
    public void setAvailability(boolean available) {
        isAvailable = available;
    }

    // Getter for shift
    public ShiftDL getShift() {
        return shift;
    }

    // Getter for employee
    public EmployeeDL getEmployee() {
        return employee;
    }

    // Override equals to compare AvailabilityDL objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AvailabilityDL that = (AvailabilityDL) obj;
        return shift.equals(that.shift) && employee.equals(that.employee);
    }

    // Override hashCode to ensure consistency with equals
    @Override
    public int hashCode() {
        int result = shift.hashCode();
        result = 31 * result + employee.hashCode();
        return result;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "AvailabilityDL{" +
                "shift=" + shift +
                ", employee=" + employee +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
