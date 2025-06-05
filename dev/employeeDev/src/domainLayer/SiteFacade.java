package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.SiteDAO;
import employeeDev.src.dtos.SiteDTO;
import employeeDev.src.mappers.SiteMapper;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

public class SiteFacade {
    private final List<Site> sites;

    public SiteFacade(){
        this.sites = new ArrayList<>();
    }

    public List<Site> getSites() {
        return sites;
    }

    public void addSite(Site site) {
        if (site != null && !sites.contains(site)) {
            sites.add(site);
            site.presistIntoDB();
        } else {
            System.err.println("Site is null or already exists.");
        }
    }

    public void removeSite(Site site) {
        if (site != null && sites.contains(site)) {
            sites.remove(site);
            SiteDAO siteDAO = new SiteDAO();
            siteDAO.deleteSite(site.getName());
        } else {
            throw new IllegalArgumentException("Site is null or does not exist.");
        }
    }

    public Site getSiteByName(String siteName){
        for (Site site : sites) {
            if (site.getName().equalsIgnoreCase(siteName)) {
                return site;
            }
        }
        throw new IllegalArgumentException("Site with name " + siteName + " not found.");
    }

    // loads all sites from the database and adds them to the sites list in the facade
    public void loadSitesFromDB() {
        SiteDAO siteDAO = new SiteDAO();
        List<SiteDTO> siteDTOs = siteDAO.getAllSites();
        for (SiteDTO siteDTO : siteDTOs) {
            Site site = SiteMapper.fromDTO(siteDTO);
            sites.add(site);
        }
    }

    public void addSite(String name, String address, String phoneNumber, String email, String zone) {
        ShippingZone shippingZone;
        try {
            shippingZone = ShippingZone.valueOf(zone.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid shipping zone: " + zone);
        }
        
        Site site = new Site(name, address, phoneNumber, email, shippingZone);
        addSite(site);
    }

    public List<Site> getSiteByZone(String zone) {
        ShippingZone shippingZone;
        try {
            shippingZone = ShippingZone.valueOf(zone.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid shipping zone: " + zone);
        }
        List<Site> filteredSites = new ArrayList<>();
        for (Site site : sites) {
            if (site.getShippingZone() == shippingZone) {
                filteredSites.add(site);
            }
        }
        return filteredSites;
    }

    public boolean deleteSite(String name) {
        Site siteToDelete = getSiteByName(name);
        if (siteToDelete != null) {
            removeSite(siteToDelete);
        } else {
            throw new IllegalArgumentException("Site with name " + name + " not found.");
        }
        return true;
    }
    
}
