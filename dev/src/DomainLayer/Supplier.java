package src.DomainLayer;

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

    public boolean removeAgreement(Agreement agreement) {
        return this.agreements.remove(agreement);
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

    public void removeProductFromCatalog(String catalogNumber) {
        this.productCatalog.remove(catalogNumber);
    }

    public Product getProductFromCatalog(String catalogNumber) {
        return this.productCatalog.get(catalogNumber);
    }

    public void updateProductInCatalog(String catalogNumber, Product product) {
        this.productCatalog.put(catalogNumber, product);
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
}
