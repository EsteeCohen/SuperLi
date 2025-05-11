package DomainLayer;

import java.time.LocalDate;

/*
    a class used to simulate time
    used because testing properties related to time are problematic
 */
public class TimeController
{
    private static TimeController instance;
    private static LocalDate date;

    public static TimeController getInstance()
    {
        if (instance == null)
            instance = new TimeController();
        return instance;
    }

    private TimeController()
    {
        this.date = LocalDate.now();
    }

    /**
     * move the current date a number of days into he future
     * @param days how many days into the future to move
     */
    public static void GoToTheFuture(int days)
    {
        date = date.plusDays(days);
    }

    public static LocalDate getDate()
    {
        return date;
    }
}
