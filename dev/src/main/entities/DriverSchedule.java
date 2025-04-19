package src.main.entities;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import src.main.enums.ScheduleStatus;
import src.main.enums.EntryType;


public class DriverSchedule {
    private String id;
    private Driver driver;
    private LocalDate date;
    private List<ScheduleEntry> entries;
    private ScheduleStatus status;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // בנאי ללוח זמנים חדש
    public DriverSchedule(String id, Driver driver, LocalDate date) {
        this.id = id;
        this.driver = driver;
        this.date = date;
        this.entries = new ArrayList<>();
        this.status = ScheduleStatus.DRAFT;
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public Driver getDriver() {
        return driver;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public List<ScheduleEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    
    public ScheduleStatus getStatus() {
        return status;
    }
    
    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }
    
    //הוספת רשומה ללוח הזמנים
    public void addEntry(ScheduleEntry entry) {
        if (entry != null) {
            this.entries.add(entry);
        }
    }
    
    //הסרת רשומה מלוח הזמנים לפי מזהה
    public boolean removeEntry(String entryId) {
        return this.entries.removeIf(entry -> entry.getId().equals(entryId));
    }
    
    //קבלת רשומה לפי מזהה
    public ScheduleEntry getEntryById(String entryId) {
        return this.entries.stream()
                .filter(entry -> entry.getId().equals(entryId))
                .findFirst()
                .orElse(null);
    }
    
    //בדיקה האם לוח הזמנים תקין (אין חפיפות בין פעילויות)
    public boolean isValid() {
        // בדיקת תקינות כל רשומה
        for (ScheduleEntry entry : entries) {
            if (!entry.isValid()) {
                return false;
            }
        }
        
        // בדיקת חפיפות בין רשומות
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                ScheduleEntry entry1 = entries.get(i);
                ScheduleEntry entry2 = entries.get(j);
                
                if (entry1.overlapsWith(entry2)) {
                    return false; // יש חפיפה
                }
            }
        }
        
        return true;
    }
    
    //חישוב סך שעות העבודה בלוח הזמנים (ללא הפסקות)
    public double calculateTotalWorkingHours() {
        return entries.stream()
                .filter(entry -> entry.getType() != EntryType.BREAK)
                .mapToDouble(ScheduleEntry::getDurationHours)
                .sum();
    }
    
    //קבלת מספר הרשומות בלוח הזמנים
    public int getEntriesCount() {
        return entries.size();
    }
    
    //קבלת תאריך לוח הזמנים כמחרוזת
    public String getFormattedDate() {
        return date.format(DATE_FORMATTER);
    }
    
    //מיון רשומות לפי זמן התחלה
    public void sortEntriesByStartTime() {
        entries.sort((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()));
    }
    
    //קבלת מספר ההובלות בלוח הזמנים
    public int getTransportsCount() {
        return (int) entries.stream()
                .filter(entry -> entry.getType() == EntryType.TRANSPORT)
                .count();
    }
    
    //בדיקה האם יש חפיפה בין פעילות חדשה לבין הפעילויות הקיימות
    public boolean hasOverlapWith(ScheduleEntry newEntry) {
        return entries.stream().anyMatch(entry -> entry.overlapsWith(newEntry));
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("לוח זמנים #").append(id).append("\n");
        sb.append("נהג: ").append(driver.getName()).append(" (").append(driver.getId()).append(")\n");
        sb.append("תאריך: ").append(getFormattedDate()).append("\n");
        sb.append("סטטוס: ").append(status).append("\n");
        
        if (entries.isEmpty()) {
            sb.append("אין פעילויות מתוכננות");
        } else {
            sb.append("פעילויות:\n");
            // מיון לפי זמן התחלה לפני ההדפסה
            List<ScheduleEntry> sortedEntries = new ArrayList<>(entries);
            sortedEntries.sort((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()));
            
            for (int i = 0; i < sortedEntries.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(sortedEntries.get(i).toString()).append("\n");
            }
        }
        
        return sb.toString();
    }
}
