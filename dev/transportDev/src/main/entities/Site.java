package transportDev.src.main.entities;

import employeeDev.src.dataAcssesLayer.SiteDAO;
import employeeDev.src.mappers.SiteMapper;
import transportDev.src.main.enums.ShippingZone;

public class Site {
    private String name;
    private String address;
    private String contactPhone;
    private String contactName;
    private ShippingZone shippingZone;

    //----------------- Constructors -------------------
    public Site(String name, String address, String contactPhone, String contactName, ShippingZone shippingZone) {
        this.name = name;
        this.address = address;
        this.contactPhone = contactPhone;
        this.contactName = contactName;
        this.shippingZone = shippingZone;
    }
    //----------------- Getters and Setters -------------------
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
                "name='" + name + '\'' +
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
        return name.equals(((Site) o).name); //name is unique

    }
    public void presistIntoDB() {
        SiteDAO siteDAO = new SiteDAO();
        siteDAO.addSite(SiteMapper.toDTO(this));
    }
}
