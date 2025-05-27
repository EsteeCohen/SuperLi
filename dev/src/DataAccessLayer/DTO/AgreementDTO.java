package DataAccessLayer.DTO;

import DomainLayer.Supplier.Agreement;
import DomainLayer.Supplier.Enums.PaymentMethod;
import DomainLayer.Supplier.Enums.PaymentTiming;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AgreementDTO {
    private String supplierId;
    private int agreementId;
    private String paymentMethod;
    private String paymentTiming;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Map<String, Map<Integer, Integer>> productDiscounts; // <catalogNumber, <quantityThreshold, discount>>

    /**
     * Full constructor for DTO
     */
    public AgreementDTO(String supplierId,
                        int agreementId,
                        String paymentMethod,
                        String paymentTiming,
                        LocalDate validFrom,
                        LocalDate validTo,
                        Map<String, Map<Integer, Integer>> productDiscounts) {
        this.supplierId = supplierId;
        this.agreementId = agreementId;
        this.paymentMethod = paymentMethod;
        this.paymentTiming = paymentTiming;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.productDiscounts = productDiscounts;
    }

    /**
     * Build DTO from domain object
     */
    public AgreementDTO(Agreement ag) {
        this.supplierId = ag.getSupplierId();
        this.agreementId = ag.getAgreementId();
        this.paymentMethod = ag.getPaymentMethod().toString();
        this.paymentTiming = ag.getPaymentTiming().toString();
        this.validFrom = ag.getValidFrom();
        this.validTo = ag.getValidTo();
        // deep copy of discounts map
        this.productDiscounts = new HashMap<>(ag.getProductDiscounts());
    }

    /**
     * Convert DTO back to domain object
     */
    public Agreement toDomain() {
        Agreement ag = new Agreement(
                supplierId,
                agreementId,
                PaymentMethod.valueOf(paymentMethod),
                PaymentTiming.valueOf(paymentTiming),
                validFrom,
                validTo
        );
        ag.setProductDiscounts(productDiscounts);
        return ag;
    }

    // Getters
    public String getSupplierId() { return supplierId; }
    public int getAgreementId() { return agreementId; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentTiming() { return paymentTiming; }
    public LocalDate getValidFrom() { return validFrom; }
    public LocalDate getValidTo() { return validTo; }
    public Map<String, Map<Integer, Integer>> getProductDiscounts() { return productDiscounts; }
}
