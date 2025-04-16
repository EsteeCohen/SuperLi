package src.DomainLayer;


public class ContactPerson {
    private String contactName;
    private String phoneNumber;

    public ContactPerson(String contactName, String phoneNumber) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ContactPerson{" +
                "contactName='" + contactName + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
