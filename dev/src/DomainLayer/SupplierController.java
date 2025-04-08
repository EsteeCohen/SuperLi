package src.DomainLayer;

import src.DomainLayer.Enums.DaysOfTheWeek;
import src.DomainLayer.Enums.PaymentMethod;
import src.DomainLayer.Enums.PaymentTiming;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierController {
    private static SupplierController instance;
    private Map<Integer, Supplier> suppliers;

    private SupplierController() {
        this.suppliers = new HashMap<>();
    }

    public static SupplierController getInstance() {
        if (instance == null) {
            instance = new SupplierController();
        }
        return instance;
    }

    /**
     * Add a new supplier
     * @param supplierId ID of the supplier
     * @param supplier Supplier object
     * @return true if added, false if already exists
     */
    public boolean addSupplier(int supplierId, Supplier supplier) {
        if (suppliers.containsKey(supplierId)) {
            return false;
        }
        suppliers.put(supplierId, supplier);
        return true;
    }

    /**
     * Get supplier by ID
     * @param supplierId ID of the supplier
     * @return Supplier object if exists, null otherwise
     */
    public Supplier getSupplierById(int supplierId) {
        return suppliers.get(supplierId);
    }

    /**
     * Update existing supplier
     * @param supplierId ID of the supplier
     * @param updatedSupplier New supplier data
     * @return true if updated successfully, false if not found
     */
    public boolean updateSupplier(int supplierId, Supplier updatedSupplier) {
        if (!suppliers.containsKey(supplierId)) {
            return false;
        }
        suppliers.put(supplierId, updatedSupplier);
        return true;
    }

    /**
     * Remove supplier by ID
     * @param supplierId ID of the supplier to remove
     * @return true if removed, false if not found
     */
    public boolean removeSupplier(int supplierId) {
        return suppliers.remove(supplierId) != null;
    }

    /**
     * Get all suppliers
     * @return List of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }
}