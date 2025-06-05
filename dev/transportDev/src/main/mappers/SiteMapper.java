package transportDev.src.main.mappers;

import transportDev.src.main.dtos.SiteDTO;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

public class SiteMapper {

    public static SiteDTO toDTO(Site site) {
        if (site == null) {
            return null;
        }

        return new SiteDTO(
                site.getName(),
                site.getAddress(),
                site.getContactPhone(),
                site.getContactName(),
                site.getShippingZone().name()
        );
    }

    public static Site fromDTO(SiteDTO siteDTO) {
        if (siteDTO == null) {
            return null;
        }

        ShippingZone shippingZone = ShippingZone.getByName(siteDTO.getShippingZone());
        
        return new Site(
                siteDTO.getName(),
                siteDTO.getAddress(),
                siteDTO.getPhoneNumber(),
                siteDTO.getContactPerson(),
                shippingZone
        );
    }
} 