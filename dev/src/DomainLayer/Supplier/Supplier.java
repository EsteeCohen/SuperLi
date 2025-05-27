package DomainLayer.Supplier;

import DomainLayer.TimeController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Supplier {
    private String name;
    private String supplierId;
    private String bankAccount;
    private List<Agreement> agreements;
    private List<ContactPerson> contactPersons;
    Map<String, Product> productCatalog; // <catalogNumber, Product>


    public Supplier(String name, String supplierId, String bankAccount) {
        this.name = name;
        this.supplierId = supplierId;
        this.bankAccount = bankAccount;
        this.agreements = new ArrayList<>();
        this.contactPersons = new ArrayList<>();
        this.productCatalog = new HashMap<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public void addAgreement(Agreement agreement) {
        this.agreements.add(agreement);
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public void addContactPerson(ContactPerson contactPerson) {
        this.contactPersons.add(contactPerson);
    }

    public Map<String, Product> getProductCatalog() {
        return productCatalog;
    }

    public boolean addProductToCatalog(Product product) {
        try {
            this.productCatalog.put(product.getCatalogNumber(), product);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Product getProductFromCatalog(String catalogNumber) {
        return this.productCatalog.get(catalogNumber);
    }


    public String getCatalogNumberByIndex(int index) {
        List<String> keys = new ArrayList<>(this.productCatalog.keySet());
        return keys.get(index);
    }

    @Override
    public String toString() {
        StringBuilder contacts = new StringBuilder();
        for (ContactPerson cp : contactPersons) {
            contacts.append(String.format("    - %s\n", cp.toString()));
        }

        StringBuilder ags = new StringBuilder();
        for (Agreement ag : agreements) {
            ags.append(String.format("    - Agreement ID: %d\n", ag.getAgreementId()));
        }

        return String.format(
                "Supplier:\n  Name: %s\n  ID: %s\n  Bank Account: %s\n  Contacts:\n%s  Agreements:\n%s",
                name,
                supplierId,
                bankAccount,
                contacts.toString().isEmpty() ? "    None\n" : contacts.toString(),
                ags.toString().isEmpty() ? "    None\n" : ags.toString()
        );
    }

    public int getAgreementSerialNumber(int agreementId) {
        int serialNumber = 0;
        for(Agreement agreement : agreements) {
            if(agreement.getAgreementId() == agreementId)
                return serialNumber;
            serialNumber++;
        }
        return -1;
    }

    public Product getProductNameByCatalogNumber(String catalogNumber) {
        return productCatalog.get(catalogNumber);
    }

    public Product findProductByName(String productName) {
        for (Product product : productCatalog.values()) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }


    public Map<String, Object> getBestPriceAndAgreementForProductName(String productName, int quantity) {
        double bestPrice = Double.MAX_VALUE;
        int bestAgreementId = -1;
        LocalDate today = TimeController.getDate();

        Product matchedProduct = findProductByName(productName); // שימוש בפונקציה החדשה

        if (matchedProduct == null) {
            return new HashMap<>(); // אין לספק את המוצר הזה
        }

        for (Agreement agreement : agreements) {
            if (agreement.getValidFrom().isAfter(today) || agreement.getValidTo().isBefore(today))
                continue;

            double price = agreement.calculatePriceWithDiscount(matchedProduct, quantity);
            if (price < bestPrice) {
                bestPrice = price;
                bestAgreementId = agreement.getAgreementId();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("price", bestPrice);
        result.put("agreementId", bestAgreementId);

        return result;
    }


    public abstract LocalDate getClosestSupplyDate(LocalDate today);

    public String getSupplierType() {
        if (this instanceof SupplierWithDeliveryDays) {
            return "delivery_days";
        } else if (this instanceof SupplierNeedsPickup) {
            return "needs_pickup";
        } else {
            return null;
        }

    }
}
