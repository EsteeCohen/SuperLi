package src.DomainLayer;



public class SupplierNeedsPickup extends Supplier {
    public String address;

    public SupplierNeedsPickup(String name, String address, int supplierId, int bankAccount) {
        super(name, supplierId, bankAccount);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String toString() {
        return super.toString() + "\nAddress: " + address + "\n";
    }
}
