package DomainLayer;

import DomainLayer.Inventory.ProductFacade;

import java.time.DayOfWeek;
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
        for(int i=0;i<days;i++)
            NextDay();
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
        if(date.getDayOfWeek()== DayOfWeek.MONDAY || date.getDayOfWeek()== DayOfWeek.THURSDAY)
        {//reset the latest sales for all the products
            System.out.println("ATTENTION! ITS "+date.getDayOfWeek());
            ProductFacade.getInstance().restartSale();
        }

    }
}
