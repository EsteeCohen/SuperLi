package employeeDev.src.mappers;

import employeeDev.src.dtos.SiteDTO;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

public class SiteMapper {

    public static SiteDTO toDTO(Site site) {
        SiteDTO siteDTO = new SiteDTO(site.getName(),
                site.getAddress(),
                site.getContactPhone(),
                site.getContactName(),
                site.getShippingZone().toString());
        return siteDTO;
    }

    public static Site fromDTO(SiteDTO siteDTO) {
        Site site = new Site("id", // Assuming "id" is a placeholder, you might want to change this
                siteDTO.getName(),
                siteDTO.getAddress(),
                siteDTO.getContactPhone(),
                siteDTO.getContactName(),
                ShippingZone.getByHebrewName(siteDTO.getShippingZone()));
        return site;
    }
}
