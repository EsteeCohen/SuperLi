package employeeDev.src.serviceLayer.Interfaces;

import java.time.LocalDateTime;
import java.util.List;

public interface IDriverAvailabilityService {
    /**
     * Returns a list of constraints and availability for potential drivers within a given date range.
     * This allows the transport system to make an informed decision about driver assignment.
     * @param startDate The start date and time for the check range.
     * @param endDate The end date and time for the check range.
     * @param requiredLicenseType Optional: required license type to filter drivers.
     * @return A list of DriverAvailabilityInfoDTO containing availability and constraint details for each driver.
     */
    List<DriverAvailabilityInfoDTO> getDriverAvailabilityConstraints(LocalDateTime startDate, LocalDateTime endDate, String requiredLicenseType);
}

