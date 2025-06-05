package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.SiteFacade;
import employeeDev.src.serviceLayer.Interfaces.SiteInfoInterface;
import java.util.List;
import transportDev.src.main.entities.Site;

public class SiteService implements SiteInfoInterface{
    private SiteFacade siteFacade;
    public SiteService(SiteFacade siteFacade) {
        this.siteFacade = siteFacade;
    }

    @Override
    public List<Site> getAllSites() {
        return siteFacade.getSites();
    }

    @Override
    public Site getSiteByName(String name) {
        return siteFacade.getSiteByName(name);
    }

    public void addSite(Site site) {
        siteFacade.addSite(site);
    }

    @Override
    public Site addSite(String name, String address, String phoneNumber, String email, String zone) {
        siteFacade.addSite(name, address, phoneNumber, email, zone);
        return siteFacade.getSiteByName(name);
    }

    @Override
    public boolean deleteSite(String name) {
        try {
            return siteFacade.deleteSite(name);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public List<Site> getSitesByZone(String zone) {
        return siteFacade.getSiteByZone(zone);
    }
}
