package serviceLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import serviceLayer.Interfaces.DriverAvailabilityInfoDTO;
import serviceLayer.Interfaces.IDriverAvailabilityService;
import domainLayer.EmployeeDL;
import domainLayer.EmployeeFacade;
import domainLayer.RoleDL;
import domainLayer.RoleFacade;
import domainLayer.ShiftDL;
import domainLayer.ShiftFacade;
import serviceLayer.Interfaces.DriverAvailabilityInfoDTO.TimeSlot;
import domainLayer.AvailabilityFacade;
import domainLayer.AvailabilityDL;

public class DriverAvailabilityService implements IDriverAvailabilityService {
    private final EmployeeFacade employeeFacade;
    private final RoleFacade roleFacade;
    private final AvailabilityFacade availabilityFacade;

    // Constructor to inject facades
    public DriverAvailabilityService(EmployeeFacade employeeFacade, RoleFacade roleFacade, AvailabilityFacade availabilityFacade) {
        this.employeeFacade = employeeFacade;
        this.roleFacade = roleFacade;
        this.availabilityFacade = availabilityFacade;
    }

    @Override
    public List<DriverAvailabilityInfoDTO> getDriverAvailabilityConstraints(LocalDateTime startDate, LocalDateTime endDate, String requiredLicenseType) {
        List<DriverAvailabilityInfoDTO> driverInfoList = new ArrayList<>();
        RoleDL driverRole = roleFacade.getRoleByName("Driver");
        if (driverRole == null) {
            System.err.println("DriverAvailabilityService: 'Driver' role not found. Cannot retrieve driver constraints.");
            return new ArrayList<>();
        }
        for (EmployeeDL employee : employeeFacade.getAllEmployees()) {
            if (!employee.hasRole(driverRole)) continue;
            List<TimeSlot> availableSlots = new ArrayList<>();
            for (AvailabilityDL availability : availabilityFacade.getAllAvailabilities(startDate, endDate)) {
                if (availability.getEmployee().equals(employee) && availability.isAvailable()) {
                    availableSlots.add(new TimeSlot(
                        availability.getShift().getStartTime(),
                        availability.getShift().getEndTime()
                    ));
                }
            }
            DriverAvailabilityInfoDTO driverInfo = new DriverAvailabilityInfoDTO(
                employee.getId(),
                employee.getFullName(),
                null, // driverLicenseType (add if you have it)
                availableSlots
            );
            driverInfoList.add(driverInfo);
        }
        return driverInfoList;
    }
}
