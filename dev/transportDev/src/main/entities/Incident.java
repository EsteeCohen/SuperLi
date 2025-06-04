package src.main.entities;

import java.time.LocalDateTime;
import src.main.enums.IncidentStatus;
import src.main.enums.IncidentType;

public class Incident {
    private final String id;
    private final LocalDateTime reportTime;
    private final IncidentType type;
    private String description;
    private final Transport affectedTransport;
    private IncidentStatus status;
    private IncidentResolution resolution;
    
    //בנאי לתקלה חדשה
    public Incident(String id, IncidentType type, String description, Transport affectedTransport) {
        this.id = id;
        this.reportTime = LocalDateTime.now();
        this.type = type;
        this.description = description;
        this.affectedTransport = affectedTransport;
        this.status = IncidentStatus.REPORTED;
        this.resolution = null;
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
    
    public IncidentResolution getResolution() {
        return resolution;
    }
    
    public void setResolution(IncidentResolution resolution) {
        if (resolution != null) {
            this.resolution = resolution;
            this.status = IncidentStatus.RESOLVED;
        }
    }
    
    //בדיקה האם יש לתקלה פתרון כלשהו
    public boolean hasResolution() {
        return !(this.resolution ==null);
    }

    public void cancelIncident() {
        this.status = IncidentStatus.CANCELLED;
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
        sb.append("פתרון: ").append(resolution.toString()).append("\n");

        return sb.toString();
    }
} // :)
