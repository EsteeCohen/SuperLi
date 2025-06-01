package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.entities.Site;
import src.main.enums.ShippingZone;
import src.main.services.SiteService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class SiteTests {
    private SiteService siteService;

    @BeforeEach
    public void setUp() {
        siteService = new SiteService();
    }

    @Test
    public void testAddSite_Valid() {
        Site s = siteService.addSite("S1", "Source", "Tel Aviv", "050", "Contact", ShippingZone.CENTER);
        assertNotNull(s);
        assertEquals("Source", s.getName());
    }

    @Test
    public void testAddSite_DuplicateId() {
        siteService.addSite("S1", "Source", "TLV", "050", "Contact", ShippingZone.CENTER);
        Site s2 = siteService.addSite("S1", "Other", "TLV", "051", "Someone", ShippingZone.NORTH);
        assertNull(s2);
    }

    @Test
    public void testAddSite_InvalidInput() {
        assertNull(siteService.addSite(null, "Name", "Addr", "Phone", "Contact", ShippingZone.NORTH));
        assertNull(siteService.addSite("ID", "", "Addr", "Phone", "Contact", ShippingZone.NORTH));
        assertNull(siteService.addSite("ID", "Name", "Addr", "Phone", "Contact", null));
    }

    @Test
    public void testGetSiteById() {
        siteService.addSite("S1", "Site", "Address", "050", "Person", ShippingZone.SOUTH);
        Site result = siteService.getSiteById("S1");
        assertNotNull(result);
        assertEquals("Site", result.getName());
    }

    @Test
    public void testGetSiteByName() {
        siteService.addSite("S1", "Warehouse", "Address", "050", "Contact", ShippingZone.SOUTH);
        assertNotNull(siteService.getSiteByName("Warehouse"));
        assertNull(siteService.getSiteByName("DoesNotExist"));
    }

    @Test
    public void testGetSitesByZone() {
        siteService.addSite("S1", "A", "Addr", "050", "C1", ShippingZone.NORTH);
        siteService.addSite("S2", "B", "Addr", "051", "C2", ShippingZone.CENTER);
        List<Site> northSites = siteService.getSitesByZone(ShippingZone.NORTH);
        assertEquals(1, northSites.size());
    }

    @Test
    public void testSearchSitesByName() {
        siteService.addSite("S1", "Big Warehouse", "Addr", "050", "Contact", ShippingZone.SOUTH);
        List<Site> results = siteService.searchSitesByName("warehouse");
        assertEquals(1, results.size());
    }

    @Test
    public void testSearchSitesByAddress() {
        siteService.addSite("S1", "Site", "Herzliya", "050", "Contact", ShippingZone.CENTER);
        List<Site> results = siteService.searchSitesByAddress("herz");
        assertEquals(1, results.size());
    }

    @Test
    public void testUpdateSite() {
        siteService.addSite("S1", "A", "Addr", "050", "C", ShippingZone.CENTER);
        boolean success = siteService.updateSite("S1", "NewName", "NewAddr", "052", "NewContact", ShippingZone.SOUTH);
        Site s = siteService.getSiteById("S1");
        assertTrue(success);
        assertEquals("NewName", s.getName());
        assertEquals(ShippingZone.SOUTH, s.getShippingZone());
    }

    @Test
    public void testUpdateSite_NotFound() {
        assertFalse(siteService.updateSite("Nope", "X", "X", "X", "X", ShippingZone.CENTER));
    }

    @Test
    public void testDeleteSite() {
        siteService.addSite("S1", "Name", "Addr", "050", "C", ShippingZone.CENTER);
        assertTrue(siteService.deleteSite("S1"));
        assertNull(siteService.getSiteById("S1"));
    }

    @Test
    public void testDeleteSite_NotFound() {
        assertFalse(siteService.deleteSite("FakeId"));
    }

    @Test
    public void testClearAllSites() {
        siteService.addSite("S1", "A", "Addr", "050", "C", ShippingZone.CENTER);
        siteService.clearAllSites();
        assertEquals(0, siteService.getAllSites().size());
    }
}
