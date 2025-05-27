package DataAccessLayer.interfacesDAO;

import DataAccessLayer.DTO.ContactPersonDTO;
import java.util.List;

/**
 * DAO interface for ContactPerson, working with DTOs.
 */
public interface ContactPersonDAO {
    void create(ContactPersonDTO contact);
    List<ContactPersonDTO> readAllBySupplier(String supplierId);
    void deleteBySupplier(String supplierId);
}
