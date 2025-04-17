package domainLayer;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityFacade {
    private final List<AvailabilityDL> availabilities;

    public AvailabilityFacade() {
        this.availabilities = new ArrayList<>();
    }

    public void addAvailability(AvailabilityDL availability) {
        this.availabilities.add(availability);
    }

    public List<EmployeeDL> getAvailabilitiesForShift(ShiftDL shift) {
        List<EmployeeDL> availableEmployees = new ArrayList<>();
        for (AvailabilityDL availability : availabilities) {
            if (availability.getShift().equals(shift) && availability.isAvailable()) {
                availableEmployees.add(availability.getEmployee());
            }
        }
        return availableEmployees;
    }

    public List<EmployeeDL> getAvailabilitiesForShift(ShiftDL shift, RoleDL role) {
        List<EmployeeDL> availableEmployees = new ArrayList<>();
        for (AvailabilityDL availability : availabilities) {
            if (availability.getShift().equals(shift) && availability.isAvailable() && availability.getEmployee().hasRole(role)) {
                availableEmployees.add(availability.getEmployee());
            }
        }
        return availableEmployees;
    }

    public boolean  getAvailabilitiesForEmployee(EmployeeDL employee, ShiftDL shift) {
        for (AvailabilityDL availability : availabilities) {
            if (availability.getEmployee().equals(employee) && availability.getShift().equals(shift)) {
                return availability.isAvailable();
            }
        }
        return false;
    }


}
