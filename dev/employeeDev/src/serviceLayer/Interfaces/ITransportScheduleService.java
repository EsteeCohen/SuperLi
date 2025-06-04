package employeeDev.src.serviceLayer.Interfaces;

import java.time.LocalDateTime;
import transportDev.src.main.entities.Site;

public interface ITransportScheduleService {
    /**
     * Returns a list of transport schedules that are planned to arrive within the specified time range.
     *
     * @param startDate the start of the time range
     * @param endDate the end of the time range
     * @return a list of upcoming deliveries scheduled between startDate and endDate
     */
    boolean areThereArivelesBetween(LocalDateTime startDate, LocalDateTime endDate, Site site);
}
