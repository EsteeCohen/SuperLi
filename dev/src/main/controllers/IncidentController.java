package src.main.controllers;

import java.util.List;

import src.main.entities.Incident;
import src.main.enums.IncidentStatus;
import src.main.enums.IncidentType;
import src.main.services.IncidentService;

// המחלקה IncidentController אחראית על ניהול התקלות במערכת
// היא מספקת ממשק בין הלקוח לשירות ניהול התקלות
public class IncidentController {
    private IncidentService incidentService;
    
    // constructor
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }
    
    // דיווח על תקלה
    public boolean reportIncident(String transportId, String type, String description) {
        try {
            IncidentType incidentType = IncidentType.valueOf(type);
            return incidentService.reportIncident(transportId, incidentType, description);
        } catch (IllegalArgumentException e) {
            return false; // סוג תקלה לא תקין
        }
    }
    
    // עדכון סטטוס תקלה
    public boolean updateIncidentStatus(String incidentId, String status) {
        try {
            IncidentStatus incidentStatus = IncidentStatus.valueOf(status);
            return incidentService.updateIncidentStatus(incidentId, incidentStatus);
        } catch (IllegalArgumentException e) {
            return false; // סטטוס לא תקין
        }
    }
    
    // הוספת פתרון לתקלה
    public boolean addResolution(String incidentId, String description, String resolutionType) {
        return incidentService.addResolution(incidentId, description, resolutionType);
    }
    
    // מחזירה את כל התקלות הפעילות עבור הובלה
    public List<Incident> getActiveIncidents() {
        return incidentService.getActiveIncidents();
    }
    
    // קבלת תקלות לפי הובלה
    public List<Incident> getIncidentsByTransport(String transportId) {
        return incidentService.getIncidentsByTransport(transportId);
    }
    
    // קבלת תקלות לפי סוג
    public List<Incident> getIncidentsByType(String type) {
        try {
            IncidentType incidentType = IncidentType.valueOf(type);
            return incidentService.getIncidentsByType(incidentType);
        } catch (IllegalArgumentException e) {
            return null; // סוג תקלה לא תקין
        }
    }
    
    // קבלת תקלות לפי סטטוס
    public List<Incident> getIncidentsByStatus(String status) {
        try {
            IncidentStatus incidentStatus = IncidentStatus.valueOf(status);
            return incidentService.getIncidentsByStatus(incidentStatus);
        } catch (IllegalArgumentException e) {
            return null; // סטטוס לא תקין
        }
    }
    
    // קבלת תקלה לפי מזהה
    public Incident getIncidentById(String incidentId) {
        return incidentService.getIncidentById(incidentId);
    }
    
    // קבלת כל התקלות
    public List<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }
    
    // קבלת כל התקלות לפי הובלה
    public boolean hasActiveIncidents(String transportId) {
        return incidentService.hasActiveIncidents(transportId);
    }
}
