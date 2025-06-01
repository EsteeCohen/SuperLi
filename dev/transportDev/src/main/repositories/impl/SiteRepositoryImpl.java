package src.main.repositories.impl;

import src.main.repositories.SiteRepository;
import src.main.dao.SiteDAO;
import src.main.entities.Site;
import src.main.enums.ShippingZone;
import java.util.List;

public class SiteRepositoryImpl implements SiteRepository {
    private SiteDAO siteDAO;
    
    public SiteRepositoryImpl(SiteDAO siteDAO) {
        this.siteDAO = siteDAO;
    }
    
    @Override
    public Site add(String id, String name, String address, String contactPhone, String contactName, ShippingZone zone) {
        if (siteDAO.read(id) != null) {
            return null; // Site already exists
        }
        Site site = new Site(id, name, address, contactPhone, contactName, zone);
        siteDAO.create(site);
        return site;
    }
    
    @Override
    public Site findById(String id) {
        return siteDAO.read(id);
    }
    
    @Override
    public List<Site> findAll() {
        return siteDAO.getAll();
    }
    
    @Override
    public List<Site> findByZone(ShippingZone zone) {
        return siteDAO.getByZone(zone);
    }
    
    @Override
    public List<Site> findInSameZone(String siteId) {
        Site site = siteDAO.read(siteId);
        if (site == null) return null;
        
        return siteDAO.getByZone(site.getShippingZone());
    }
    
    @Override
    public boolean updateSite(Site site) {
        if (siteDAO.read(site.getId()) == null) return false;
        siteDAO.update(site);
        return true;
    }
    
    @Override
    public boolean deleteSite(String id) {
        if (siteDAO.read(id) == null) return false;
        siteDAO.delete(id);
        return true;
    }
} 