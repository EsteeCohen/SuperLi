package employeeDev.src.unitTests;

import employeeDev.src.domainLayer.SiteFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SiteFacadeTest {
    private static final String TEST_SITE_1 = "SiteA";
    private static final String TEST_SITE_2 = "SiteB";
    private static final String TEST_SITE_3 = "SiteC";
    private static final String TEST_SITE_4 = "SiteD";

    private SiteFacade siteFacade;
    private Site site1;
    private Site site2;

    @BeforeEach
    void setUp() {
        siteFacade = new SiteFacade();
        site1 = new Site(TEST_SITE_1, "AddressA", "PhoneA", "EmailA", ShippingZone.CENTER);
        site2 = new Site(TEST_SITE_2, "AddressB", "PhoneB", "EmailB", ShippingZone.NORTH);
        siteFacade.addSite(site1);
        siteFacade.addSite(site2);
    }

    @AfterEach
    void cleanUp() {
        employeeDev.src.dataAcssesLayer.SiteDAO dao = new employeeDev.src.dataAcssesLayer.SiteDAO();
        dao.deleteSite(TEST_SITE_1);
        dao.deleteSite(TEST_SITE_2);
        dao.deleteSite(TEST_SITE_3);
        dao.deleteSite(TEST_SITE_4);
    }

    @Test
    void testAddSite() {
        Site site3 = new Site(TEST_SITE_3, "AddressC", "PhoneC", "EmailC", transportDev.src.main.enums.ShippingZone.SOUTH);
        siteFacade.addSite(site3);
        assertTrue(siteFacade.getSites().contains(site3));
    }

    @Test
    void testAddDuplicateSite() {
        int initialSize = siteFacade.getSites().size();
        siteFacade.addSite(site1);
        assertEquals(initialSize, siteFacade.getSites().size());
    }

    @Test
    void testRemoveSite() {
        siteFacade.removeSite(site1);
        assertFalse(siteFacade.getSites().contains(site1));
    }

    @Test
    void testGetSiteByName() {
        Site found = siteFacade.getSiteByName(TEST_SITE_1);
        assertEquals(site1, found);
    }

    @Test
    void testGetSiteByNameNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> siteFacade.getSiteByName("NotExist"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testAddSiteWithStringParams() {
        siteFacade.addSite(TEST_SITE_4, "AddressD", "PhoneD", "EmailD", "SOUTH");
        assertNotNull(siteFacade.getSiteByName(TEST_SITE_4));
    }

    @Test
    void testGetSiteByZone() {
        List<Site> centerSites = siteFacade.getSiteByZone("CENTER");
        assertTrue(centerSites.contains(site1));
        assertFalse(centerSites.contains(site2));
    }

    @Test
    void testDeleteSite() {
        assertTrue(siteFacade.deleteSite(TEST_SITE_1));
        assertThrows(IllegalArgumentException.class, () -> siteFacade.getSiteByName(TEST_SITE_1));
    }
}
