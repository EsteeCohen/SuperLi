package src.main.controllers;

import java.util.List;

import src.main.services.SiteService;
import src.main.enums.ShippingZone;
import src.main.entities.Site;

//בקר ניהול אתרים
public class SiteController {
    private SiteService siteService;
    
    // constructor
    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }
    
    // הוספת אתר חדש
    public Site addSite(String id, String name, String address, String contactPhone, String contactName, String zoneStr) {
        try {
            ShippingZone zone = ShippingZone.valueOf(zoneStr);
            return siteService.addSite(id, name, address, contactPhone, contactName, zone);
        } catch (IllegalArgumentException e) {
            return null; // אזור שילוח לא תקין
        }
    }
    
    // הסרת אתר
    public boolean deleteSite(String id) {
        return siteService.deleteSite(id);
    }
    
    // קבלת אתר לפי מזהה
    public Site getSiteById(String id) {
        return siteService.getSiteById(id);
    }
    
    // קבלת כל האתרים
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }
    
    // קבלת אתרים לפי אזור
    public List<Site> getSitesByZone(String zoneStr) {
        try {
            ShippingZone zone = ShippingZone.valueOf(zoneStr);
            return siteService.getSitesByZone(zone);
        } catch (IllegalArgumentException e) {
            return null; // אזור שילוח לא תקין
        }
    }
    
    // עדכון אתר קיים
    public boolean updateSite(String id, String name, String address, String contactPhone, String contactName, String zoneStr) {
        Site site = siteService.getSiteById(id);
        
        if (site == null) {
            return false;
        }
        
        try {
            ShippingZone zone = null;
            if (zoneStr != null && !zoneStr.isEmpty()) {
                zone = ShippingZone.valueOf(zoneStr);
            }
            
            // עדכון רק השדות שאינם ריקים/null
            if (name != null && !name.isEmpty()) {
                site.setName(name);
            }
            
            if (address != null && !address.isEmpty()) {
                site.setAddress(address);
            }
            
            if (contactPhone != null && !contactPhone.isEmpty()) {
                site.setContactPhone(contactPhone);
            }
            
            if (contactName != null && !contactName.isEmpty()) {
                site.setContactName(contactName);
            }
            
            if (zone != null) {
                site.setShippingZone(zone);
            }
            
            return true;
        } catch (IllegalArgumentException e) {
            return false; // אזור שילוח לא תקין
        }
    }
    
    // חיפוש אתרים לפי שם
    public List<Site> searchSitesByName(String nameQuery) {
        return siteService.searchSitesByName(nameQuery);
    }
    
    // חיפוש אתרים לפי כתובת
    public List<Site> searchSitesByAddress(String addressQuery) {
        return siteService.searchSitesByAddress(addressQuery);
    }
    
    // בדיקה אם אתר קיים
    public boolean siteExists(String id) {
        return siteService.getSiteById(id) != null;
    }
    
    // קבלת מספר האתרים במערכת
    public int getTotalSitesCount() {
        return siteService.getAllSites().size();
    }
    
    // קבלת מספר האתרים לפי אזור
    public int getSitesCountByZone(String zoneStr) {
        try {
            ShippingZone zone = ShippingZone.valueOf(zoneStr);
            List<Site> sites = siteService.getSitesByZone(zone);
            return sites != null ? sites.size() : 0;
        } catch (IllegalArgumentException e) {
            return 0; // אזור שילוח לא תקין
        }
    }
}
