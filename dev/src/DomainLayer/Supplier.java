package src.DomainLayer;

import java.util.ArrayList;
import java.util.List;

public abstract class Supplier {
    private String name;
    private String supplierId;
    private String bankAccount;
    private List<Agreement> agreements;
    private List<ContactPerson> contactPersons;

    public Supplier(String name, String supplierId, String bankAccount) {
        this.name = name;
        this.supplierId = supplierId;
        this.bankAccount = bankAccount;
        this.agreements = new ArrayList<>();
        this.contactPersons = new ArrayList<>();
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

    public boolean removeContactPerson(ContactPerson contactPerson) {
        return this.contactPersons.remove(contactPerson);
    }

    /**
     * Find all valid agreements for this supplier
     * @return list of currently valid agreements
     */
    public List<Agreement> getValidAgreements() {
        List<Agreement> validAgreements = new ArrayList<>();
        for (Agreement agreement : agreements) {
            if (agreement.isValid()) {
                validAgreements.add(agreement);
            }
        }
        return validAgreements;
    }

    /**
     * Get all products available from this supplier through valid agreements
     * @return list of available products
     */
    public List<Product> getAvailableProducts() {
        List<Product> products = new ArrayList<>();
        for (Agreement agreement : getValidAgreements()) {
            products.addAll(agreement.getProductCatalog());
        }
        return products;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "name: " + name + '\'' +
                ", supplierId: " + supplierId +
                ", bankAccount: " + bankAccount +
                ", agreements: " + agreements.toString() +
                ", contactPersons: " + contactPersons.toString() +
                '}';
    }
}
