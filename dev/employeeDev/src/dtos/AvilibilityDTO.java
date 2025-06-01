package employeeDev.src.dtos;

public class AvilibilityDTO {
    private ShiftDTO shift;
    private EmployeeDTO employee;
    private boolean isAvailable;

    public AvilibilityDTO(ShiftDTO shift, EmployeeDTO employee, boolean isAvailable) {
        this.shift = shift;
        this.employee = employee;
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public ShiftDTO getShift() {
        return shift;
    }

    public void setShift(ShiftDTO shift) {
        this.shift = shift;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }
}
