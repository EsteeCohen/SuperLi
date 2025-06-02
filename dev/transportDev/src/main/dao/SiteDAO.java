package transportDev.src.main.dao;

import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;
import java.util.List;

public interface SiteDAO {
    void create(Site site);
    Site read(String id);
    void update(Site site);
    void delete(String id);
    List<Site> getAll();
    List<Site> getByZone(ShippingZone zone);
} 