package BusinessLayer;
import java.time.LocalDate;

//holds an info of a discount
//just a subclass that allows us to calc the sell price of a product
class Discount {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double precentage;

    Discount(LocalDate startDate, LocalDate endDate, double precentage)
    {
        this.startDate=startDate;
        this.endDate=endDate;
        this.precentage=precentage;
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
        return precentage;
    }

}
