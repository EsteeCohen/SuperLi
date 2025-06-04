package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.SiteDAO;
import employeeDev.src.dtos.SiteDTO;
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
        } else {
            throw new IllegalArgumentException("Site is null or already exists.");
        }
    }

    public void removeSite(Site site) {
        if (site != null && sites.contains(site)) {
            sites.remove(site);
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
            Site site = new Site("id" ,siteDTO.getName(), siteDTO.getAddress(), siteDTO.getContactPhone(), siteDTO.getContactName(), ShippingZone.getByHebrewName(siteDTO.getShippingZone()));
            addSite(site);
        }
    }
    
}
