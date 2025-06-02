package employeeDev.src.dtos;

public class SiteDTO {
    private String name;
    private String address;
    private String contactPhone;
    private String contactName;
    private String shippingZone;

    public SiteDTO(String name, String address, String contactPhone, String contactName, String shippingZone) {
        this.name = name;
        this.address = address;
        this.contactPhone = contactPhone;
        this.contactName = contactName;
        this.shippingZone = shippingZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }   

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getShippingZone() {
        return shippingZone;
    }

    public void setShippingZone(String shippingZone) {
        this.shippingZone = shippingZone;
    }

}
