// File: DataAccessLayer/DTO/DeliverySupplierDTO.java
package DataAccessLayer.DTO;

import DomainLayer.Supplier.SupplierWithDeliveryDays;
import DomainLayer.Supplier.Enums.DaysOfTheWeek;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for suppliers that deliver on specific days.
 */
public class DeliverySupplierDTO extends SupplierDTO {
    private final List<String> deliveryDays;

    /** Full constructor (e.g. when reading from DB). */
    public DeliverySupplierDTO(String name,
                               String supplierId,
                               String bankAccount,
                               List<String> deliveryDays) {
        super(name, supplierId, bankAccount, "delivery_days");
        this.deliveryDays = new ArrayList<>(deliveryDays);
    }

    /** Build from domain object. */
    public DeliverySupplierDTO(SupplierWithDeliveryDays supplier) {
        super(supplier.getName(),
                supplier.getSupplierId(),
                supplier.getBankAccount(),
                "delivery_days");
        this.deliveryDays = new ArrayList<>();
        for (DaysOfTheWeek d : supplier.getDeliveryDays()) {
            this.deliveryDays.add(d.toString());
        }
    }

    @Override
    public DomainLayer.Supplier.SupplierWithDeliveryDays toDomain() {
        List<DaysOfTheWeek> days = new ArrayList<>();
        for (String s : deliveryDays) {
            days.add(DaysOfTheWeek.valueOf(s));
        }
        return new SupplierWithDeliveryDays(
                getName(),
                getSupplierId(),
                getBankAccount(),
                days
        );
    }

    public List<String> getDeliveryDays() {
        return new ArrayList<>(deliveryDays);
    }
}
