package DomainLayer.Supplier;


import java.time.LocalDate;

public class SupplierNeedsPickup extends Supplier {
    public String address;

    public SupplierNeedsPickup(String name, String supplierId, String bankAccount, String address) {
        super(name, supplierId, bankAccount);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getClosestSupplyDate(LocalDate orderDate){
        return orderDate.plusDays(3);
    }

    @Override
    public String toString() {
        return "Supplier Needs Pickup:" + "\n" + super.toString() + "  Address: " + address + "\n";
    }
}
