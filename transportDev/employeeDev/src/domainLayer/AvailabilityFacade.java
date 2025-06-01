package domainLayer;

import java.time.LocalDateTime;
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

    public AvailabilityDL getAvailabilityForEmployee(EmployeeDL employee, ShiftDL shift) {
        for (AvailabilityDL availability : availabilities) {
            if (availability.getEmployee().equals(employee) && availability.getShift().equals(shift)) {
                return availability;
            }
        }
        return null; // No availability found for the employee and shift
    }

    public List<AvailabilityDL> getAllAvailabilities() {
        return new ArrayList<>(availabilities);
    }

    public List<AvailabilityDL> getAllAvailabilities(LocalDateTime start, LocalDateTime end) {
        List<AvailabilityDL> result = new ArrayList<>();
        for (AvailabilityDL availability : availabilities) {
            LocalDateTime shiftStart = availability.getShift().getStartTime();
            LocalDateTime shiftEnd = availability.getShift().getEndTime();
            if ((shiftStart.isEqual(start) || shiftStart.isAfter(start)) &&
                (shiftEnd.isEqual(end) || shiftEnd.isBefore(end))) {
                result.add(availability);
            }
        }
        return result;
    }
}
