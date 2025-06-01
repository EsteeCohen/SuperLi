package employeeDev.src.serviceLayer;

import java.time.LocalDateTime;

public class TransportScheduleSL {

    private final LocalDateTime arrivalTime;
    private final String requiredLicense;
    private final String assignedDriverId;

    public TransportScheduleSL(LocalDateTime arrivalTime, String requiredLicense, String assignedDriverId) {
        this.arrivalTime = arrivalTime;
        this.requiredLicense = requiredLicense;
        this.assignedDriverId = assignedDriverId;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public String getRequiredLicense() {
        return requiredLicense;
    }

    public String getAssignedDriverId() {
        return assignedDriverId;
    }
}
