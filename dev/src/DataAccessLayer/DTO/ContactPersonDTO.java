package DataAccessLayer.DTO;

import DomainLayer.Supplier.ContactPerson;

/**
 * Data Transfer Object for ContactPerson, including the supplier association.
 */
public class ContactPersonDTO {
    private String supplierId;
    private String contactName;
    private String phoneNumber;

    /**
     * Full constructor
     */
    public ContactPersonDTO(String supplierId, String contactName, String phoneNumber) {
        this.supplierId = supplierId;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }
    /**
     * Build DTO from domain object + supplierId
     */
    public ContactPersonDTO(ContactPerson contact, String supplierId) {
        this.supplierId  = supplierId;
        this.contactName = contact.getContactName();
        this.phoneNumber = contact.getPhoneNumber();
    }

    /**
     * Convert back to domain object
     */
    public ContactPerson toDomain() {
        return new ContactPerson(contactName, phoneNumber);
    }

    // Getters
    public String getSupplierId()  { return supplierId; }
    public String getContactName() { return contactName; }
    public String getPhoneNumber(){ return phoneNumber; }
}
