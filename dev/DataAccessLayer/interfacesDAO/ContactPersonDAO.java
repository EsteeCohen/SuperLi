package DataAccessLayer.interfacesDAO;


import DomainLayer.Supplier.ContactPerson;
import java.util.List;

public interface ContactPersonDAO {
    void create(ContactPerson contact, String supplierId);
    List<ContactPerson> readAllBySupplier(String supplierId);
    void deleteBySupplier(String supplierId);
}
