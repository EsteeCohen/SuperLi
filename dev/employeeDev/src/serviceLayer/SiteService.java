package employeeDev.src.serviceLayer;

import java.util.List;

import employeeDev.src.domainLayer.SiteFacade;
import transportDev.src.main.entities.Site;

public class SiteService {
    private SiteFacade siteFacade;
    public SiteService(SiteFacade siteFacade) {
        this.siteFacade = siteFacade;
    }

    public List<Site> getAllSites() {
        return siteFacade.getSites();
    }
    public Site getSiteByName(String name) {
        return siteFacade.getSiteByName(name);
    }
    public void addSite(Site site) {
        siteFacade.addSite(site);
    }
    public void addSite(String id,String name, String address, String phoneNumber, String email, String zone) {
        siteFacade.addSite(id,name, address, phoneNumber, email, zone);
    }
    public void deleteSite(String name) {
        siteFacade.deleteSite(name);
    }
    public Site getSiteByZone(String zone) {
        return siteFacade.getSiteByZone(zone);
    }
}
