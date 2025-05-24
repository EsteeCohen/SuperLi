package DataAccessLayer.interfacesDAO;


import DataAccessLayer.DTO.SupplierDTO;
import java.util.List;

public interface SupplierDAO {
    void create(SupplierDTO supplier);
    SupplierDTO read(String supplierId);
    List<SupplierDTO> readAll();
    void update(SupplierDTO supplier);
    void delete(String supplierId);
    void updateName(String supplierId, String name);
    void updateBankAccount(String supplierId, String bankAccount);
    void updateSupplierId(String oldId, String newId);
    void updateDeliveryDays(String supplierId, List<String> days);
    void updatePickupAddress(String supplierId, String address);

    /** new: read suppliers by supplier_type (e.g. "PICKUP") */
    List<SupplierDTO> readByType(String supplierType);

}
