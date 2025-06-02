package serviceLayer.Interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ITransportScheduleService {

    boolean areThereArivelesBetween(LocalDateTime startDate, LocalDateTime endDate, Site site);

    int getNumberOfDeliveriesInDate(LocalDate date, Site site);

}
