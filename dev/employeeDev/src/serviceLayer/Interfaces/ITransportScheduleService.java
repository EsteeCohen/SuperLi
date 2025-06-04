package employeeDev.src.serviceLayer.Interfaces;

import java.time.LocalDateTime;
import transportDev.src.main.entities.Site;

public interface ITransportScheduleService {

    boolean areThereArivelesBetween(LocalDateTime startDate, LocalDateTime endDate, Site site);
}
