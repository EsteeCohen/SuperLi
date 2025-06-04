package employeeDev.src.serviceLayer.Interfaces;

import java.util.List;
import transportDev.src.main.entities.Site;

public interface SiteInfoInterface {

    void addSite(String name, String address, String contactPhone, String contactName, String zone);

    void deleteSite(String siteName);

    Site getSiteByName(String siteName);

    List<Site> getAllSites();

    

}
