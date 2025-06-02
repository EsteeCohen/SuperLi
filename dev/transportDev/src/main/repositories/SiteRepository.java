package transportDev.src.main.repositories;

import java.util.List;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

public interface SiteRepository {
    Site add(String id, String name, String address, String contactPhone, String contactName, ShippingZone zone);
    Site findById(String id);
    List<Site> findAll();
    List<Site> findByZone(ShippingZone zone);
    List<Site> findInSameZone(String siteId);
    boolean updateSite(Site site);
    boolean deleteSite(String id);
} 