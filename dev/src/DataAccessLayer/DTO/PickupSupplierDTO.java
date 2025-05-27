// File: DataAccessLayer/DTO/PickupSupplierDTO.java
package DataAccessLayer.DTO;

import DomainLayer.Supplier.SupplierNeedsPickup;

/**
 * DTO for suppliers that require pickup at an address.
 */
public class PickupSupplierDTO extends SupplierDTO {
    private final String pickupAddress;

    /** Full constructor (e.g. when reading from DB). */
    public PickupSupplierDTO(String name,
                             String supplierId,
                             String bankAccount,
                             String pickupAddress) {
        super(name, supplierId, bankAccount, "needs_pickup");
        this.pickupAddress = pickupAddress;
    }

    /** Build from domain object. */
    public PickupSupplierDTO(SupplierNeedsPickup supplier) {
        super(supplier.getName(),
                supplier.getSupplierId(),
                supplier.getBankAccount(),
                "needs_pickup");
        this.pickupAddress = supplier.getAddress();
    }

    @Override
    public SupplierNeedsPickup toDomain() {
        return new SupplierNeedsPickup(
                getName(),
                getSupplierId(),
                getBankAccount(),
                pickupAddress
        );
    }

    public String getPickupAddress() {
        return pickupAddress;
    }
}
