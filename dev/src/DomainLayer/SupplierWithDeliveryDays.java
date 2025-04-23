package src.DomainLayer;

import src.DomainLayer.Enums.DaysOfTheWeek;
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

}
