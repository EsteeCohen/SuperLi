package transportDev.src.main.dao.impl;

import transportDev.src.main.dao.SiteDAO;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteDAOImpl implements SiteDAO {
    private Map<String, Site> sites = new HashMap<>();
    
    @Override
    public void create(Site site) {
        sites.put(site.getId(), site);
    }
    
    @Override
    public Site read(String id) {
        return sites.get(id);
    }
    
    @Override
    public void update(Site site) {
        sites.put(site.getId(), site);
    }
    
    @Override
    public void delete(String id) {
        sites.remove(id);
    }
    
    @Override
    public List<Site> getAll() {
        return new ArrayList<>(sites.values());
    }
    
    @Override
    public List<Site> getByZone(ShippingZone zone) {
        return sites.values().stream()
            .filter(site -> site.getShippingZone() == zone)
            .collect(java.util.stream.Collectors.toList());
    }
} 