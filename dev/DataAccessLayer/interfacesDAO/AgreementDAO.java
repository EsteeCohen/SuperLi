package DataAccessLayer.interfacesDAO;

import DataAccessLayer.DTO.AgreementDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AgreementDAO {
    void create(AgreementDTO agreement);
    AgreementDTO read(int agreementId);
    List<AgreementDTO> readAllBySupplier(String supplierId);
    void update(AgreementDTO agreement);
    void delete(int agreementId);
    void deleteBySupplier(String supplierId);
    void updatePaymentMethod(int agreementId, String paymentMethod);

    void updatePaymentTiming(int agreementId, String paymentTiming);

    void updateValidFrom(int agreementId, LocalDate validFrom);

    void updateValidTo(int agreementId, LocalDate validTo);

    void updateProductDiscounts(String supplierId, int agreementId, Map<String, Map<Integer,Integer>> discounts);
}
