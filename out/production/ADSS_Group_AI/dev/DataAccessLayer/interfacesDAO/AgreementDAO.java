package DataAccessLayer.interfacesDAO;

import DomainLayer.Supplier.Agreement;
import java.util.List;

public interface AgreementDAO {
    void create(Agreement agreement);
    Agreement read(int agreementId);
    List<Agreement> readAllBySupplier(String supplierId);
    void update(Agreement agreement);
    void delete(int agreementId);

    void deleteBySupplier(String supplierId);
}
