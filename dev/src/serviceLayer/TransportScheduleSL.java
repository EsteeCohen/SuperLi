package serviceLayer;

import java.time.LocalDateTime;

public class TransportScheduleSL {
    private String transportId;
    private LocalDateTime arrivalTime;
    private int durationMinutes;
    private String requiredLicense;
    private String assignedDriverId;

    public TransportScheduleSL(String transportId, LocalDateTime arrivalTime, int durationMinutes, String requiredLicense, 
                               String assignedDriverId) {
        this.transportId = transportId;
        this.arrivalTime = arrivalTime;
        this.durationMinutes = durationMinutes;
        this.requiredLicense = requiredLicense;
        this.assignedDriverId = assignedDriverId;
    }

    public String getTransportId() {
        return transportId;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getRequiredLicense() {
        return requiredLicense;
    }

    public String getAssignedDriverId() {
        return assignedDriverId;
    }

    public void setAssignedDriverId(String assignedDriverId) {
        this.assignedDriverId = assignedDriverId;
    }
}
