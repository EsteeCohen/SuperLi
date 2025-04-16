package src.DomainLayer;

import src.DomainLayer.Enums.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Agreement {
    private String supplierId;
    private int agreementId;
    private List<Product> productCatalog;
    private PaymentMethod paymentMethod;
    private PaymentTiming paymentTiming;
    private LocalDate validFrom;
    private LocalDate validTo;

    public Agreement(String supplierId,int agreementId, PaymentMethod paymentMethod, PaymentTiming paymentTiming, LocalDate validFrom,
                     LocalDate validTo) {
        this.supplierId = supplierId;
        this.agreementId = agreementId;
        this.productCatalog = new ArrayList<>();
        this.paymentMethod = paymentMethod;
        this.paymentTiming = paymentTiming;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    // Getters and setters
    public String getSupplierId() {
        return supplierId;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public List<Product> getProductCatalog() {
        return productCatalog;
    }

    public void setProductCatalog(List<Product> productCatalog) {
        this.productCatalog = productCatalog;
    }

    public void addProduct(Product product) {
        this.productCatalog.add(product);
    }

    public boolean removeProduct(Product product) {
        return this.productCatalog.remove(product);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentTiming getPaymentTiming() {
        return paymentTiming;
    }

    public void setPaymentTiming(PaymentTiming paymentTiming) {
        this.paymentTiming = paymentTiming;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    /**
     * Check if the agreement is currently valid
     * @return true if agreement is valid as of current date
     */
    public boolean isValid() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(validFrom) && !today.isAfter(validTo);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "supplierId=" + supplierId +
                ", productCatalog=" + productCatalog +
                ", paymentMethod=" + paymentMethod +
                ", paymentTiming=" + paymentTiming +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                '}';
    }
}
