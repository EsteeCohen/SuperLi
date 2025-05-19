package DomainLayer;

import java.time.LocalDate;

/*
    a class used to simulate time
    used because testing properties related to time are problematic
 */
public class TimeController
{
    private static LocalDate date=LocalDate.now();
    private static OrderController orderController = OrderController.getInstance();


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
    public static void NextDay()
    {
        date = date.plusDays(1);
        System.out.println("The date is now: "+date);
        orderController.confirmOrderArrival(getDate());
        //להוסיף בדיקה עבור כל ההזמנות הפתוחות הגיעו

    }
}
