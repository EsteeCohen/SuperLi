package employeeDev.src.serviceLayer.Interfaces;

import java.time.LocalDateTime;
import java.util.List;

public class DriverAvailabilityInfoDTO {
    private String driverId;
    private String driverName;
    private String driverLicenseType;
    private List<TimeSlot> availableTimeSlots;

    public DriverAvailabilityInfoDTO(String driverId, String driverName, String driverLicenseType, List<TimeSlot> availableTimeSlots) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverLicenseType = driverLicenseType;
        this.availableTimeSlots = availableTimeSlots;
    }

    public String getDriverId() { return driverId; }
    public String getDriverName() { return driverName; }
    public String getDriverLicenseType() { return driverLicenseType; }
    public List<TimeSlot> getAvailableTimeSlots() { return availableTimeSlots; }

    public static class TimeSlot {
        private LocalDateTime start;
        private LocalDateTime end;

        public TimeSlot(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }

        public LocalDateTime getStart() { return start; }
        public LocalDateTime getEnd() { return end; }
    }
}
