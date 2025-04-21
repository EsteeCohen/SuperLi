package DomainLayer;
import java.time.LocalDate;

//holds an info of a discount
//just a subclass that allows us to calc the sell price of a product
public class Discount {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double percentage;

    public Discount(LocalDate startDate, LocalDate endDate, double precentage)
    {
        this.startDate=startDate;
        this.endDate=endDate;
        this.percentage=precentage;
    }

    LocalDate getStartDate()
    {
        return startDate;
    }
    LocalDate getEndDate()
    {
        return endDate;
    }
    double getPrecentage()
    {
        return percentage;
    }

}
