package DataAccessLayer;

import DataAccessLayer.*;
import DataAccessLayer.interfacesDAO.AgreementDAO;
import DomainLayer.Supplier.Agreement;

import java.sql.SQLException;
import java.util.List;

public class AgreementRepository {
    private final AgreementDAO agreementDAO;

    public AgreementRepository() throws SQLException {
        this.agreementDAO = new AgreementDAOImpl();
    }

    public void addAgreement(Agreement agreement) {
        agreementDAO.create(agreement);
    }

    public Agreement getAgreementById(int agreementId) {
        return agreementDAO.read(agreementId);
    }

    public List<Agreement> getAgreementsBySupplier(String supplierId) {
        return agreementDAO.readAllBySupplier(supplierId);
    }

    public void updateAgreement(Agreement agreement) {
        agreementDAO.update(agreement);
    }

    public void removeAgreement(int agreementId) {
        agreementDAO.delete(agreementId);
    }

    public void removeAgreementsBySupplier(String supplierId) {
        agreementDAO.deleteBySupplier(supplierId);
    }
}
