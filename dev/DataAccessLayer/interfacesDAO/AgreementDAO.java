package DataAccessLayer.interfacesDAO;

import DataAccessLayer.DTO.AgreementDTO;
import java.util.List;

public interface AgreementDAO {
    void create(AgreementDTO agreement);
    AgreementDTO read(int agreementId);
    List<AgreementDTO> readAllBySupplier(String supplierId);
    void update(AgreementDTO agreement);
    void delete(int agreementId);
    void deleteBySupplier(String supplierId);
}
