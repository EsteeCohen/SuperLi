package serviceLayer.Interfaces;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import serviceLayer.TransportScheduleSL;

public interface ITransportScheduleService {
    /**
     * Returns a list of transport schedules that are planned to arrive within the specified time range.
     *
     * @param startDate the start of the time range
     * @param endDate the end of the time range
     * @return a list of upcoming deliveries scheduled between startDate and endDate
     */
    List<TransportScheduleSL> getUUpcomingDeliveries(LocalDateTime startDate, LocalDateTime endDate);
}
