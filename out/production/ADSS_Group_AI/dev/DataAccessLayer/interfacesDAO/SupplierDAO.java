package DataAccessLayer.interfacesDAO;


import DomainLayer.Supplier.Supplier;
import java.util.List;

public interface SupplierDAO {
    void create(Supplier supplier);
    Supplier read(String supplierId);
    List<Supplier> readAll();
    void update(Supplier supplier);
    void delete(String supplierId);
}
