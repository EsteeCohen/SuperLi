package src.main.services;

import java.util.ArrayList;
import java.util.List;
import src.main.entities.Site;
import src.main.enums.ShippingZone;

public class SiteService {
    // :)
    private List<Site> sites;
    
    //בנאי ליצירת שירות אתרים חדש
    public SiteService() {
        this.sites = new ArrayList<>();
    }
    
    //יוצר אתר חדש במערכת
    public Site addSite(String id, String name, String address, String contactPhone, String contactName, ShippingZone shippingZone) {
        // אימות קלטים
        if (id == null || id.trim().isEmpty() || 
            name == null || name.trim().isEmpty() || 
            address == null || address.trim().isEmpty() || 
            contactPhone == null || contactPhone.trim().isEmpty() || 
            contactName == null || contactName.trim().isEmpty() || 
            shippingZone == null) {
            return null;
        }
        
        // בדיקה אם אתר עם מזהה זה כבר קיים
        if (getSiteById(id) != null) {
            return null;
        }
        
        Site site = new Site(id, name, address, contactPhone, contactName, shippingZone);
        sites.add(site);
        return site;
    }
    
    //מחזיר את כל האתרים במערכת
    public List<Site> getAllSites() {
        return new ArrayList<>(sites);
    }
    
    //מחזיר אתר לפי מזהה
    public Site getSiteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        for (Site site : sites) {
            if (site.getId().equals(id)) {
                return site;
            }
        }
        
        return null;
    }
    
    //מחזיר אתר לפי שם
    public Site getSiteByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        
        for (Site site : sites) {
            if (site.getName().equals(name)) {
                return site;
            }
        }
        
        return null;
    }
    
    //מחזיר אתרים לפי אזור שילוח
    public List<Site> getSitesByZone(ShippingZone zone) {
        if (zone == null) {
            return new ArrayList<>();
        }
        
        List<Site> result = new ArrayList<>();
        for (Site site : sites) {
            if (site.getShippingZone() == zone) {
                result.add(site);
            }
        }
        
        return result;
    }

    //מחפש אתרים לפי שם
    public List<Site> searchSitesByName(String nameQuery) {
        if (nameQuery == null || nameQuery.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Site> result = new ArrayList<>();
        for (Site site : sites) {
            if (site.getName().toLowerCase().contains(nameQuery.toLowerCase())) {
                result.add(site);
            }
        }
        
        return result;
    }

    //מחפש אתרים לפי כתובת
    public List<Site> searchSitesByAddress(String addressQuery) {
        if (addressQuery == null || addressQuery.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Site> result = new ArrayList<>();
        for (Site site : sites) {
            if (site.getAddress().toLowerCase().contains(addressQuery.toLowerCase())) {
                result.add(site);
            }
        }
        
        return result;
    }
    
    //מעדכן פרטי אתר קיים
    public boolean updateSite(String id, String name, String address, String contactPhone, String contactName, ShippingZone shippingZone) {
        Site site = getSiteById(id);
        
        if (site == null) {
            return false;
        }
        
        if (name != null && !name.trim().isEmpty()) {
            site.setName(name);
        }
        
        if (address != null && !address.trim().isEmpty()) {
            site.setAddress(address);
        }
        
        if (contactPhone != null && !contactPhone.trim().isEmpty()) {
            site.setContactPhone(contactPhone);
        }
        
        if (contactName != null && !contactName.trim().isEmpty()) {
            site.setContactName(contactName);
        }
        
        if (shippingZone != null) {
            site.setShippingZone(shippingZone);
        }
        
        return true;
    }
    
    //מוחק אתר מהמערכת
    public boolean deleteSite(String id) {
        Site site = getSiteById(id);
        
        if (site == null) {
            return false;
        }
        
        return sites.remove(site);
    }
    
    //מוחק את כל האתרים מהמערכת - משמש לאתחול או לבדיקות
    public void clearAllSites() {
        sites.clear();
    }
}
