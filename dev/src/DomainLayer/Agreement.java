package src.DomainLayer;

import src.DomainLayer.Enums.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Agreement {
    private String supplierId;
    private int agreementId;
    private PaymentMethod paymentMethod;
    private PaymentTiming paymentTiming;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Map<String, Map<Integer, Integer>> productDiscounts; // <catalogNumber, <quantity, discount>>


    public Agreement(String supplierId,int agreementId, PaymentMethod paymentMethod, PaymentTiming paymentTiming, LocalDate validFrom,
                     LocalDate validTo) {
        this.supplierId = supplierId;
        this.agreementId = agreementId;
        this.productDiscounts = new HashMap<>();
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

    public Map<String, Map<Integer, Integer>> getProductCatalog() {
        return productDiscounts;
    }

    public void setProductCatalog(Map<String, Map<Integer, Integer>> productCatalog) {
        this.productDiscounts = productCatalog;
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

    // Add getter/setter for the new field
    public Map<String, Map<Integer, Integer>> getProductDiscounts() {
        return productDiscounts;
    }

    public void setProductDiscounts(Map<String, Map<Integer, Integer>> productDiscounts) {
        this.productDiscounts = productDiscounts;
    }

    // Add a method to set discounts for a specific product
    public void setDiscountForProduct(String catalogNumber, Map<Integer, Integer> discounts) {
        productDiscounts.put(catalogNumber, discounts);
    }

    // Add a method to get discounts for a specific product
    public Map<Integer, Integer> getDiscountForProduct(String catalogNumber) {
        return productDiscounts.getOrDefault(catalogNumber, new HashMap<>());
    }

    // Add a method to calculate price with discount
    public double calculatePriceWithDiscount(Product product, int quantity) {
        if (quantity <= 0) return 0;

        Map<Integer, Integer> discounts = getDiscountForProduct(product.getCatalogNumber());
        double discount = 0.0;

        for (Map.Entry<Integer, Integer> entry : discounts.entrySet()) {
            if (quantity >= entry.getKey()) {
                discount = Math.max(discount, entry.getValue());
            }
        }

        return product.getPrice() * quantity * (1 - discount / 100.0);
    }

    public double calculateTotalPrice(Map<String, Integer> items, Map<String, Product> products) {
        double totalPrice = 0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String catalogNumber = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = products.get(catalogNumber);
            if (product == null) continue;

            totalPrice += calculatePriceWithDiscount(product, quantity);
        }
        return totalPrice;
    }


    @Override
    public String toString() {
        StringBuilder productsInfo = new StringBuilder();
        for (Map.Entry<String, Map<Integer, Integer>> entry : productDiscounts.entrySet()) {
            String catalogNumber = entry.getKey();
            Map<Integer, Integer> discounts = entry.getValue();
            productsInfo.append("    - Product Catalog Number: ").append(catalogNumber).append("\n");
            if (discounts.isEmpty()) {
                productsInfo.append("        No discounts defined\n");
            } else {
                for (Map.Entry<Integer, Integer> discountEntry : discounts.entrySet()) {
                    productsInfo.append(String.format("        Quantity: %d → Discount: %d%%\n",
                            discountEntry.getKey(), discountEntry.getValue()));
                }
            }
        }

        return String.format(
                "Agreement:\n" +
                        "  Supplier ID: %s\n" +
                        "  Agreement ID: %d\n" +
                        "  Payment Method: %s\n" +
                        "  Payment Timing: %s\n" +
                        "  Valid From: %s\n" +
                        "  Valid To: %s\n" +
                        "  Product Discounts:\n%s",
                supplierId,
                agreementId,
                paymentMethod,
                paymentTiming,
                validFrom,
                validTo,
                productsInfo.toString().isEmpty() ? "    None\n" : productsInfo.toString()
        );
    }


}
