package domainLayer;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityFacade {
    private final List<Availability> availabilities;

    public AvailabilityFacade() {
        this.availabilities = new ArrayList<>();
    }

    public void addAvailability(Availability availability) {
        this.availabilities.add(availability);
    }

    public List<EmployeeSL> getAvailabilitiesForShift(Shift shift) {
        List<EmployeeSL> availableEmployees = new ArrayList<>();
        for (Availability availability : availabilities) {
            if (availability.getShift().equals(shift) && availability.isAvailable()) {
                availableEmployees.add(availability.getEmployee());
            }
        }
        return availableEmployees;
    }

    public List<EmployeeSL> getAvailabilitiesForShift(Shift shift, Role role) {
        List<EmployeeSL> availableEmployees = new ArrayList<>();
        for (Availability availability : availabilities) {
            if (availability.getShift().equals(shift) && availability.isAvailable() && availability.getEmployee().hasRole(role)) {
                availableEmployees.add(availability.getEmployee());
            }
        }
        return availableEmployees;
    }

    public boolean  getAvailabilitiesForEmployee(EmployeeSL employee, Shift shift) {
        for (Availability availability : availabilities) {
            if (availability.getEmployee().equals(employee) && availability.getShift().equals(shift)) {
                return availability.isAvailable();
            }
        }
        return false;
    }


}
