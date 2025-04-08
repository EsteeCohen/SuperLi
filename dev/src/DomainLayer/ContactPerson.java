package src.DomainLayer;


public class ContactPerson {
    private String contactName;
    private int phoneNumber;

    public ContactPerson(String contactName, int phoneNumber) {
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
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
