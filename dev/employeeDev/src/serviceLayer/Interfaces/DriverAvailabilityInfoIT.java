package employeeDev.src.serviceLayer.Interfaces;

import employeeDev.src.domainLayer.DriverDL;
import java.time.LocalDateTime;
import java.util.List;
import transportDev.src.main.entities.Site;

public interface  DriverAvailabilityInfoIT {
    List<DriverDL> getAllAssignDriver(LocalDateTime time, Site site);
}
