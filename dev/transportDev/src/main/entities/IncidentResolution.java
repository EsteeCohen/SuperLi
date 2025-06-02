package transportDev.src.main.entities;

import java.time.LocalDateTime;

public class IncidentResolution {
    private String id;
    private LocalDateTime resolutionTime;
    private String description;

    //בנאי לפתרון תקלה
    public IncidentResolution(String id, LocalDateTime resolutionTime, String description) {
        this.id = id;
        this.resolutionTime = resolutionTime;
        this.description = description;
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
    

    //קבלת זמן הפתרון כמחרוזת
    public String getFormattedResolutionTime() {
        return resolutionTime.toString().replace("T", " ");
    }

    @Override
    public String toString() {
        return "פתרון: " + description + " - זמן: " + getFormattedResolutionTime();
    }

}
