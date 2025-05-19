package DomainLayer.Supplier;

import DomainLayer.Supplier.Enums.DaysOfTheWeek;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplierWithDeliveryDays extends  Supplier{
    private List<DaysOfTheWeek> deliveryDays;

    public SupplierWithDeliveryDays(String name, String supplierId, String bankAccount, List<DaysOfTheWeek> deliveryDays) {
        super(name, supplierId, bankAccount);
        this.deliveryDays = deliveryDays;
        if(this.deliveryDays == null)
            this.deliveryDays = new ArrayList<DaysOfTheWeek>();
    }

    public List<DaysOfTheWeek> getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(List<DaysOfTheWeek> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    @Override
    public String toString() {
        return "Supplier With Fixed Delivery Days:" + "\n" + super.toString() + "  Delivery Days: " + deliveryDays.toString() + "\n";
    }

    @Override
    public LocalDate getClosestSupplyDate(LocalDate today) {
        int todayValue = today.getDayOfWeek().getValue() == 7 ? 1 : today.getDayOfWeek().getValue() + 1;
        int minDays = Integer.MAX_VALUE;
        for (DaysOfTheWeek deliveryDay : deliveryDays) {
            int deliveryValue = deliveryDay.ordinal() + 1;
            int daysUntil = deliveryValue - todayValue;
            if (daysUntil < 0) {
                daysUntil += 7; // next week
            }
            if (daysUntil <= minDays) {
                minDays = daysUntil;
            }
        }
        return today.plusDays(minDays);
    }

}
