package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.entities.*;
import src.main.enums.*;
import src.main.services.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IncidentTests {

    private IncidentService incidentService;
    private TransportService transportService;
    private TruckService truckService;
    private DriverService driverService;
    private SiteService siteService;
    private Transport transport;

    @BeforeEach
    public void setUp() {
        truckService = new TruckService();
        driverService = new DriverService();
        siteService = new SiteService();
        transportService = new TransportService(truckService, driverService, siteService);
        incidentService = new IncidentService(transportService);

        truckService.addTruck("T1", "Model", 2000, 10000, LicenseType.C);
        driverService.addDriver("D1", "Driver", "050-0000000", LicenseType.C);
        siteService.addSite("S1", "Source", "Addr", "050", "Contact", ShippingZone.NORTH);
        siteService.addSite("S2", "Dest", "Addr", "050", "Contact", ShippingZone.NORTH);

        transport = transportService.createTransport(
                LocalDate.now(), LocalTime.now().plusHours(1),
                "T1", "D1", "S1", List.of("S2"));
    }

    @Test
    public void testReportIncident() {
        boolean ok = incidentService.reportIncident(transport.getId(), IncidentType.TRUCK_BREAKDOWN, "Flat tyre");
        assertTrue(ok);
        assertEquals(1, incidentService.getAllIncidents().size());
        assertEquals(IncidentStatus.REPORTED, incidentService.getAllIncidents().get(0).getStatus());
    }

    @Test
    public void testResolvedIncidentCannotBeReopened() {
        incidentService.reportIncident(transport.getId(), IncidentType.OTHER, "Test");
        Incident incident = incidentService.getAllIncidents().get(0);
        String id = incident.getId();

        incidentService.setResolution(id, "Fixed");
        assertEquals(IncidentStatus.RESOLVED, incident.getStatus());

        // Attempt to change status after resolution — must be rejected
        boolean updated = incidentService.updateIncidentStatus(id, IncidentStatus.IN_PROGRESS);
        assertFalse(updated);
        assertEquals(IncidentStatus.RESOLVED, incident.getStatus());
    }

    @Test
    public void testCancelledIncidentCannotBeUpdated() {
        incidentService.reportIncident(transport.getId(), IncidentType.OTHER, "Test");
        Incident incident = incidentService.getAllIncidents().get(0);
        String id = incident.getId();

        incident.cancelIncident();
        assertEquals(IncidentStatus.CANCELLED, incident.getStatus());

        boolean updated = incidentService.updateIncidentStatus(id, IncidentStatus.REPORTED);
        assertFalse(updated);
        assertEquals(IncidentStatus.CANCELLED, incident.getStatus());
    }

    @Test
    public void testCancelledIncidentCannotGetResolution() {
        incidentService.reportIncident(transport.getId(), IncidentType.OTHER, "Test");
        Incident incident = incidentService.getAllIncidents().get(0);
        incident.cancelIncident();

        boolean resolved = incidentService.setResolution(incident.getId(), "Too late");
        assertFalse(resolved);
        assertFalse(incident.hasResolution());
    }

    @Test
    public void testSetResolutionMovesToResolved() {
        incidentService.reportIncident(transport.getId(), IncidentType.DRIVER_UNAVAILABLE, "Sick");
        Incident incident = incidentService.getAllIncidents().get(0);

        boolean ok = incidentService.setResolution(incident.getId(), "Replacement driver assigned");
        assertTrue(ok);
        assertTrue(incident.hasResolution());
        assertEquals(IncidentStatus.RESOLVED, incident.getStatus());
    }

    @Test
    public void testToStringOnUnresolvedIncidentDoesNotThrow() {
        incidentService.reportIncident(transport.getId(), IncidentType.OTHER, "Open incident");
        Incident incident = incidentService.getAllIncidents().get(0);
        assertDoesNotThrow(incident::toString);
        assertTrue(incident.toString().contains("אין פתרון עדיין"));
    }

    @Test
    public void testGetActiveIncidents() {
        incidentService.reportIncident(transport.getId(), IncidentType.OTHER, "Open");
        Incident incident = incidentService.getAllIncidents().get(0);
        incidentService.setResolution(incident.getId(), "Done");

        incidentService.reportIncident(transport.getId(), IncidentType.TRUCK_BREAKDOWN, "Still open");

        List<Incident> active = incidentService.getActiveIncidents();
        assertEquals(1, active.size());
        assertEquals(IncidentStatus.RESOLVED, incident.getStatus());
    }
}
