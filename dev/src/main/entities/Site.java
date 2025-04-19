package src.main.entities;

import src.main.enums.ShippingZone;

public class Site {
    private String id;
    private String name;
    private String address;
    private String contactPhone;
    private String contactName;
    private ShippingZone shippingZone;

    //----------------- Constructors -------------------
    public Site(String id, String name, String address, String contactPhone, String contactName, ShippingZone shippingZone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactPhone = contactPhone;
        this.contactName = contactName;
        this.shippingZone = shippingZone;
    }
    //----------------- Getters and Setters -------------------
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public ShippingZone getShippingZone() {
        return shippingZone;
    }
    public void setShippingZone(ShippingZone shippingZone) {
        this.shippingZone = shippingZone;
    }
    //----------------- toString -------------------
    @Override
    public String toString() {
        return "Site{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactName='" + contactName + '\'' +
                ", shippingZone=" + shippingZone +
                '}';
    }
    //----------------- equals -------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site)) return false;

        Site site = (Site) o;

        if (!id.equals(site.id)) return false;
        if (!name.equals(site.name)) return false;
        if (!address.equals(site.address)) return false;
        if (!contactPhone.equals(site.contactPhone)) return false;
        if (!contactName.equals(site.contactName)) return false;
        return shippingZone == site.shippingZone;
    }
}
