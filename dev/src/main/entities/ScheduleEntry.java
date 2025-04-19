package src.main.entities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import src.main.enums.EntryType;

public class ScheduleEntry {
    private String id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Transport transport; // יכול להיות null אם זו פעילות מסוג אחר
    private EntryType type;
    private String description;
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    //בנאי לרשומה בלוח זמנים
    public ScheduleEntry(String id, LocalTime startTime, LocalTime endTime, Transport transport, EntryType type, String description) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.transport = transport;
        this.type = type;
        this.description = description;
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public Transport getTransport() {
        return transport;
    }
    
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
    
    public EntryType getType() {
        return type;
    }
    
    public void setType(EntryType type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    // חישוב משך הזמן של הרשומה בשעות
    public double getDurationHours() {
        if (startTime == null || endTime == null) {
            return 0;
        }
        
        int startSeconds = startTime.toSecondOfDay();
        int endSeconds = endTime.toSecondOfDay();
        
        if (endSeconds < startSeconds) {
            // אם זמן הסיום הוא לפני זמן ההתחלה (למשל, יום הסתיים), נניח שזמן הסיום הוא למחרת
            endSeconds += 24 * 60 * 60; // הוספת 24 שעות בשניות
        }
        
        return (endSeconds - startSeconds) / 3600.0; // המרה לשעות
    }
    
    //בדיקה האם הרשומה תקפה (שעת סיום אחרי שעת התחלה)
    public boolean isValid() {
        return startTime != null && endTime != null && 
               !endTime.isBefore(startTime) &&
               (type != EntryType.TRANSPORT || transport != null);
    }
    
    //בדיקה האם יש חפיפה עם רשומה אחרת
    public boolean overlapsWith(ScheduleEntry other) {
        if (other == null || this.startTime == null || this.endTime == null || 
            other.startTime == null || other.endTime == null) {
            return false;
        }
        
        return (this.startTime.isBefore(other.endTime) && 
                other.startTime.isBefore(this.endTime));
    }
    
    //קבלת זמני ההתחלה והסיום כמחרוזות מפורמטות
    public String getFormattedStartTime() {
        return startTime != null ? startTime.format(TIME_FORMATTER) : "";
    }
    
    public String getFormattedEndTime() {
        return endTime != null ? endTime.format(TIME_FORMATTER) : "";
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFormattedStartTime()).append(" - ").append(getFormattedEndTime());
        sb.append(" (").append(type).append(")");
        
        if (description != null && !description.isEmpty()) {
            sb.append(": ").append(description);
        }
        
        if (transport != null) {
            sb.append(" - הובלה #").append(transport.getId());
        }
        
        return sb.toString();
    }
}
