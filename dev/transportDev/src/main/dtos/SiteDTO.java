package transportDev.src.main.dtos;

public class SiteDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private String contactPerson;
    private String shippingZone;

    public SiteDTO(String name, String address, String phoneNumber, String contactPerson, String shippingZone) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.contactPerson = contactPerson;
        this.shippingZone = shippingZone;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getShippingZone() {
        return shippingZone;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setShippingZone(String shippingZone) {
        this.shippingZone = shippingZone;
    }
} 