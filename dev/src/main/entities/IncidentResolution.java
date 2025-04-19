package src.main.entities;

import java.time.LocalDateTime;

public class IncidentResolution {
    private String id;
    private LocalDateTime resolutionTime;
    private String description;
    private String resolutionType;
    
    //בנאי לפתרון תקלה
    public IncidentResolution(String id, LocalDateTime resolutionTime, String description, String resolutionType) {
        this.id = id;
        this.resolutionTime = resolutionTime;
        this.description = description;
        this.resolutionType = resolutionType;
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public LocalDateTime getResolutionTime() {
        return resolutionTime;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getResolutionType() {
        return resolutionType;
    }
    
    public void setResolutionType(String resolutionType) {
        this.resolutionType = resolutionType;
    }
    
    //קבלת זמן הפתרון כמחרוזת
    public String getFormattedResolutionTime() {
        return resolutionTime.toString().replace("T", " ");
    }
    
    @Override
    public String toString() {
        return "פתרון (" + resolutionType + "): " + description + " - זמן: " + getFormattedResolutionTime();
    }
}
