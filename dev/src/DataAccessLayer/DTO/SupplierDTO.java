/*package DataAccessLayer.DTO;

import DomainLayer.Supplier.Enums.DaysOfTheWeek;
import DomainLayer.Supplier.Supplier;
import DomainLayer.Supplier.SupplierWithDeliveryDays;
import DomainLayer.Supplier.SupplierNeedsPickup;

import java.util.ArrayList;
import java.util.List;

public class SupplierDTO {
    private String name;
    private String supplierId;
    private String bankAccount;
    private String type;              // "delivery_days" or "needs_pickup"
    private List<String> deliveryDays;
    private String pickupAddress;


    public SupplierDTO(String name,
                       String supplierId,
                       String bankAccount,
                       String type,
                       List<String> deliveryDays,
                       String pickupAddress) {
        this.name = name;
        this.supplierId = supplierId;
        this.bankAccount = bankAccount;
        this.type = type;
        this.deliveryDays = deliveryDays;
        this.pickupAddress = pickupAddress;
    }

    /**
     * Build DTO from domain object

    public SupplierDTO(Supplier supplier) {
        this.name = supplier.getName();
        this.supplierId = supplier.getSupplierId();
        this.bankAccount = supplier.getBankAccount();
        if (supplier instanceof SupplierWithDeliveryDays) {
            this.type = "delivery_days";
            this.pickupAddress = null;
            this.deliveryDays = new ArrayList<>();
            for (DaysOfTheWeek d : ((SupplierWithDeliveryDays) supplier).getDeliveryDays()) {
                deliveryDays.add(d.toString());
            }
        } else if (supplier instanceof SupplierNeedsPickup) {
            this.type = "needs_pickup";
            this.deliveryDays = null;
            this.pickupAddress = ((SupplierNeedsPickup) supplier).getAddress();
        }
    }

     // Convert DTO back to domain object

    public Supplier toDomain() {
        if ("delivery_days".equals(type)) {
            List<DaysOfTheWeek> days = new ArrayList<>();
            if (deliveryDays != null) {
                for (String s : deliveryDays) {
                    days.add(DaysOfTheWeek.valueOf(s));
                }
            }
            return new SupplierWithDeliveryDays(name, supplierId, bankAccount, days);
        } else {
            return new SupplierNeedsPickup(name, supplierId, bankAccount, pickupAddress);
        }
    }

    // Getters
    public String getName() { return name; }
    public String getSupplierId() { return supplierId; }
    public String getBankAccount() { return bankAccount; }
    public String getType() { return type; }
    public List<String> getDeliveryDays() { return deliveryDays; }
    public String getPickupAddress() { return pickupAddress; }
}*/
// File: DataAccessLayer/DTO/SupplierDTO.java
package DataAccessLayer.DTO;

import DomainLayer.Supplier.Supplier;

/**
 * Base DTO for all suppliers: holds the common fields.
 */
public abstract class SupplierDTO {
    private final String name;
    private final String supplierId;
    private final String bankAccount;
    private final String type;

    protected SupplierDTO(String name, String supplierId, String bankAccount, String type) {
        this.name = name;
        this.supplierId = supplierId;
        this.bankAccount = bankAccount;
        this.type = type;
    }

    /** Convert this DTO back to the correct domain object. */
    public abstract Supplier toDomain();

    /** Factory: build the right DTO subclass from a domain Supplier. */
    public static SupplierDTO fromDomain(Supplier supplier) {
        switch (supplier.getSupplierType()) {
            case "delivery_days":
                return new DeliverySupplierDTO((DomainLayer.Supplier.SupplierWithDeliveryDays) supplier);
            case "needs_pickup":
                return new PickupSupplierDTO((DomainLayer.Supplier.SupplierNeedsPickup) supplier);
            default:
                throw new IllegalArgumentException("Unknown supplier type: " + supplier.getSupplierType());
        }
    }

    // common getters
    public String getName()        { return name; }
    public String getSupplierId()  { return supplierId; }
    public String getBankAccount() { return bankAccount; }
    public String getType()        { return type; }
}
