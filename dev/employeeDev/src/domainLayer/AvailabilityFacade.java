package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.AvilibilityDAO;
import employeeDev.src.dtos.AvilibilityDTO;
import employeeDev.src.mappers.AvilibilityMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityFacade {
    private final List<AvailabilityDL> availabilities;
    private final ShiftFacade shiftFacade;
    private final EmployeeFacade employeeFacade;
    private final SiteFacade siteFacade;
    

    public AvailabilityFacade(ShiftFacade shiftFacade, EmployeeFacade employeeFacade, SiteFacade siteFacade) {
        this.availabilities = new ArrayList<>();
        this.shiftFacade = shiftFacade;
        this.employeeFacade = employeeFacade;
        this.siteFacade = siteFacade;
    }

    public void addAvailability(AvailabilityDL availability) {
        this.availabilities.add(availability);
        availability.presistIntoDB();
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
        return null;
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

    public void LoadAvailabilitiesFromDB() {
        AvilibilityDAO availabilityDAO = new AvilibilityDAO();
        List<AvilibilityDTO> availabilityDTOs = availabilityDAO.getAllAvailabilities();
        for (AvilibilityDTO dto : availabilityDTOs) {
            AvailabilityDL availability = AvilibilityMapper.fromDTO(dto, shiftFacade, siteFacade, employeeFacade);
            if (availability != null) {
                availabilities.add(availability);
            }
        }
        
    }
}
