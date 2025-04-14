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
    private Map<String, Supplier> suppliers;

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
     * Get supplier by ID
     * @param supplierId ID of the supplier
     * @return Supplier object if exists, null otherwise
     */
    public Supplier getSupplierById(String supplierId) {
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
    public boolean removeSupplier(String supplierId) {
        return suppliers.remove(supplierId) != null;
    }

    /**
     * Get all suppliers
     * @return List of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public boolean addSupplierWithDelivery(String name, String id, String bankAccount, String deliveryDays) {
        if(suppliers.containsKey(id)) {
            return false;
        }
        List<DaysOfTheWeek> days = parseDeliveryDays(deliveryDays);
        SupplierWithDeliveryDays supplier = new SupplierWithDeliveryDays(name, id, bankAccount, days);
        suppliers.put(id, supplier);
        return true;
    }

    public List<DaysOfTheWeek> parseDeliveryDays(String deliveryDays) {
        List<DaysOfTheWeek> days = new ArrayList<>();
        String[] daysArray = deliveryDays.split(",");
        for (String day : daysArray) {
            try {
                days.add(DaysOfTheWeek.values()[Integer.parseInt(day.trim())-1]);
            } catch (IllegalArgumentException e) {
                // Handle invalid day
            }
        }
        return days;
    }

    public boolean addSupplierNeedsPickup(String name, String id, String bankAccount, String address) {
        if(suppliers.containsKey(id)) {
            return false;
        }

        SupplierNeedsPickup supplier = new SupplierNeedsPickup(name, id, bankAccount, address);
        suppliers.put(id, supplier);
        return true;
    }
}