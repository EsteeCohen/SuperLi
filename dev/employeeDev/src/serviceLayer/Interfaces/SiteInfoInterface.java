package employeeDev.src.serviceLayer.Interfaces;

import java.util.List;
import transportDev.src.main.entities.Site;

public interface SiteInfoInterface {

    Site addSite(String name, String address, String contactPhone, String contactName, String zone);

    boolean deleteSite(String siteName);

    Site getSiteByName(String siteName);

    List<Site> getAllSites();

    List<Site> getSitesByZone(String zone);

}
