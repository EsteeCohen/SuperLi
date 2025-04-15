package BusinessLayer;
import java.time.LocalDate;

class Discount {
    private final LocalDate _startDate;
    private final LocalDate _endDate;
    private final double _precentage;

    Discount(LocalDate startDate, LocalDate endDate, double precentage)
    {
        _startDate=startDate;
        _endDate=endDate;
        _precentage=precentage;
    }

    LocalDate getStartDate()
    {
        return _startDate;
    }
    LocalDate getEndDate()
    {
        return _endDate;
    }
    double getPrecentage()
    {
        return _precentage;
    }

}
