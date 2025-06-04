package src.main.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import transportDev.src.main.entities.Incident;
import transportDev.src.main.entities.IncidentResolution;
import transportDev.src.main.entities.Transport;
import transportDev.src.main.enums.IncidentStatus;
import transportDev.src.main.enums.IncidentType;

public class IncidentService {
    // :)
    private List<Incident> incidents;
    private TransportService transportService;

    //בנאי לשירות ניהול תקלות
    public IncidentService(TransportService transportService) {
        this.incidents = new ArrayList<>();
        this.transportService = transportService;
    }

    //דיווח על תקלה חדשה
    public boolean reportIncident(int transportId, IncidentType type, String description) {
        Transport transport = transportService.getTransportById(transportId);
        
        if (transport == null) {
            return false;
        }
        
        // יצירת מזהה תקלה חדש
        String incidentId = "INC" + System.currentTimeMillis();
        
        // יצירת תקלה חדשה
        Incident incident = new Incident(incidentId, type, description, transport);
        incident.setStatus(IncidentStatus.REPORTED);
        
        // הוספת התקלה למאגר
        incidents.add(incident);
        
        return true;
    }

    //עדכון סטטוס תקלה
    public boolean updateIncidentStatus(String incidentId, IncidentStatus status) {
        Incident incident = getIncidentById(incidentId);
        
        if (incident == null) {
            return false;
        }
        
        // בדיקת תקינות מעברי סטטוס
        if (incident.getStatus() == IncidentStatus.CANCELLED || 
            incident.getStatus() == IncidentStatus.RESOLVED) {
            return false; // אי אפשר לעדכן תקלה שכבר נפתרה או בוטלה
        }
        
        incident.setStatus(status);
        return true;
    }

    //הוספת פתרון לתקלה
    public boolean setResolution(String incidentId, String description) {
        Incident incident = getIncidentById(incidentId);
        
        if (incident == null) {
            return false;
        }
        
        // אי אפשר להוסיף פתרון לתקלה שבוטלה
        if (incident.getStatus() == IncidentStatus.CANCELLED) {
            return false;
        }
        
        // יצירת מזהה פתרון חדש
        String resolutionId = "RES" + System.currentTimeMillis();
        
        // יצירת פתרון חדש
        IncidentResolution resolution = new IncidentResolution(
            resolutionId, 
            LocalDateTime.now(), 
            description
        );
        
        // הוספת הפתרון לתקלה
        incident.setResolution(resolution);
        
        // אם התקלה עדיין במצב "דווח", נעדכן אותה למצב "בטיפול"
        if (incident.getStatus() == IncidentStatus.REPORTED) {
            incident.setStatus(IncidentStatus.IN_PROGRESS);
        }
        
        return true;
    }

    //קבלת כל התקלות הפעילות
    public List<Incident> getActiveIncidents() {
        return incidents.stream()
                .filter(i -> i.getStatus() == IncidentStatus.REPORTED || 
                             i.getStatus() == IncidentStatus.IN_PROGRESS)
                .collect(Collectors.toList());
    }

    //קבלת תקלות לפי הובלה
    public List<Incident> getIncidentsByTransport(int transportId) {
        return incidents.stream()
                .filter(i -> i.getAffectedTransport().getId()== transportId)
                .collect(Collectors.toList());
    }

    //קבלת תקלות לפי סוג
    public List<Incident> getIncidentsByType(IncidentType type) {
        return incidents.stream()
                .filter(i -> i.getType() == type)
                .collect(Collectors.toList());
    }

    //קבלת תקלות לפי סטטוס
    public List<Incident> getIncidentsByStatus(IncidentStatus status) {
        return incidents.stream()
                .filter(i -> i.getStatus() == status)
                .collect(Collectors.toList());
    }

    //קבלת תקלה לפי מזהה
    public Incident getIncidentById(String incidentId) {
        return incidents.stream()
                .filter(i -> Objects.equals(i.getId(), incidentId))
                .findFirst()
                .orElse(null);
    }

    //קבלת כל התקלות
    public List<Incident> getAllIncidents() {
        return new ArrayList<>(incidents);
    }

    //בדיקת קיום תקלות פעילות עבור הובלה
    public boolean hasActiveIncidents(int transportId) {
        return incidents.stream()
                .anyMatch(i -> i.getAffectedTransport().getId()== transportId &&
                              (i.getStatus() == IncidentStatus.REPORTED || 
                               i.getStatus() == IncidentStatus.IN_PROGRESS));
    }
}
