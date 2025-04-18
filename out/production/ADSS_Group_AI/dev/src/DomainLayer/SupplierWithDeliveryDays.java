package src.DomainLayer;

import src.DomainLayer.Enums.DaysOfTheWeek;
import java.util.ArrayList;
import java.util.List;

public class SupplierWithDeliveryDays extends  Supplier{
    private List<DaysOfTheWeek> deliveryDays;

    public SupplierWithDeliveryDays(String name, String supplierId, String bankAccount, List<DaysOfTheWeek> deliveryDays) {
        super(name, supplierId, bankAccount);
        deliveryDays = deliveryDays;
        if(this.deliveryDays == null)
            this.deliveryDays = new ArrayList<DaysOfTheWeek>();
    }

    public List<DaysOfTheWeek> getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(List<DaysOfTheWeek> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public void addDeliveryDay(DaysOfTheWeek day) {
        if (!this.deliveryDays.contains(day)) {
            this.deliveryDays.add(day);
        }
    }

    public boolean removeDeliveryDay(DaysOfTheWeek day) {
        return this.deliveryDays.remove(day);
    }

    @Override
    public String toString() {
        return super.toString() + "  Delivery Days: " + deliveryDays + "\n";
    }

}
