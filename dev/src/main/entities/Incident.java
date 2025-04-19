package src.main.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import src.main.enums.IncidentStatus;
import src.main.enums.IncidentType;

public class Incident {
    private String id;
    private LocalDateTime reportTime;
    private IncidentType type;
    private String description;
    private Transport affectedTransport;
    private IncidentStatus status;
    private List<IncidentResolution> resolutions;
    
    //בנאי לתקלה חדשה
    public Incident(String id, LocalDateTime reportTime, IncidentType type, String description, Transport affectedTransport) {
        this.id = id;
        this.reportTime = reportTime;
        this.type = type;
        this.description = description;
        this.affectedTransport = affectedTransport;
        this.status = IncidentStatus.REPORTED;
        this.resolutions = new ArrayList<>();
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public LocalDateTime getReportTime() {
        return reportTime;
    }
    
    public IncidentType getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Transport getAffectedTransport() {
        return affectedTransport;
    }
    
    public IncidentStatus getStatus() {
        return status;
    }
    
    public void setStatus(IncidentStatus status) {
        this.status = status;
    }
    
    public List<IncidentResolution> getResolutions() {
        return new ArrayList<>(resolutions);
    }
    
    //הוספת פתרון לתקלה
    public void addResolution(IncidentResolution resolution) {
        if (resolution != null) {
            this.resolutions.add(resolution);
        }
    }
    
    //קבלת הפתרון האחרון שהוסף
    public IncidentResolution getLatestResolution() {
        if (this.resolutions.isEmpty()) {
            return null;
        }
        return this.resolutions.get(this.resolutions.size() - 1);
    }
    
    //בדיקה האם יש לתקלה פתרון כלשהו
    public boolean hasResolution() {
        return !this.resolutions.isEmpty();
    }
    
    //קבלת זמן הדיווח על התקלה כמחרוזת
    public String getFormattedReportTime() {
        return reportTime.toString().replace("T", " ");
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("תקלה #").append(id).append("\n");
        sb.append("סוג: ").append(type).append("\n");
        sb.append("זמן דיווח: ").append(getFormattedReportTime()).append("\n");
        sb.append("תיאור: ").append(description).append("\n");
        sb.append("הובלה מושפעת: ").append(affectedTransport.getId()).append("\n");
        sb.append("סטטוס: ").append(status).append("\n");
        
        if (!resolutions.isEmpty()) {
            sb.append("פתרונות:\n");
            for (IncidentResolution resolution : resolutions) {
                sb.append("  - ").append(resolution.toString()).append("\n");
            }
        }
        
        return sb.toString();
    }
}
