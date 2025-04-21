package src.main.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import src.main.entities.DriverSchedule;
import src.main.entities.ScheduleEntry;
import src.main.enums.EntryType;
import src.main.enums.ScheduleStatus;
import src.main.entities.Driver;
import src.main.entities.Transport;

public class ScheduleService {
    private List<DriverSchedule> schedules;
    private DriverService driverService;
    private TransportService transportService;
    
    /**
     * בנאי לשירות ניהול לוחות זמנים
     */
    public ScheduleService(DriverService driverService, TransportService transportService) {
        this.schedules = new ArrayList<>();
        this.driverService = driverService;
        this.transportService = transportService;
    }
    
    /**
     * יצירת לוח זמנים חדש לנהג
     */
    public boolean createSchedule(String driverId, LocalDate date) {
        Driver driver = driverService.getDriverById(driverId);
        
        if (driver == null) {
            return false;
        }
        
        // בדיקה שאין כבר לוח זמנים לנהג בתאריך זה
        if (getDriverScheduleByDate(driverId, date) != null) {
            return false;
        }
        
        // יצירת מזהה ללוח זמנים
        String scheduleId = "SCH" + System.currentTimeMillis();
        
        // יצירת לוח זמנים חדש
        DriverSchedule schedule = new DriverSchedule(scheduleId, driver, date);
        schedule.setStatus(ScheduleStatus.DRAFT);
        
        // הוספה למאגר
        schedules.add(schedule);
        
        return true;
    }
    
    /**
     * הוספת לוח זמנים ישירות למאגר
     */
    public void addSchedule(DriverSchedule schedule) {
        if (schedule != null) {
            schedules.add(schedule);
        }
    }
    
    /**
     * הוספת רשומה ללוח זמנים
     */
    public boolean addScheduleEntry(String scheduleId, LocalTime startTime, LocalTime endTime, 
                                  int transportId, EntryType type, String description) {
        DriverSchedule schedule = getScheduleById(scheduleId);
        
        if (schedule == null) {
            return false;
        }
        
        // רק לוח זמנים במצב טיוטה או מאושר ניתן לעדכן
        if (schedule.getStatus() != ScheduleStatus.DRAFT && 
            schedule.getStatus() != ScheduleStatus.CONFIRMED) {
            return false;
        }
        
        // בדיקת תקינות זמנים
        if (startTime == null || endTime == null || endTime.isBefore(startTime) || 
            startTime.equals(endTime)) {
            return false;
        }
        
        // קבלת ההובלה במידה והסוג הוא הובלה
        Transport transport = null;
        if (type == EntryType.TRANSPORT || type == EntryType.LOADING || type == EntryType.UNLOADING) {
            transport = transportService.getTransportById(transportId);
            
            if (transport == null) {
                return false;
            }
            
            // בדיקה שההובלה באותו תאריך כמו לוח הזמנים
            if (!transport.getDate().equals(schedule.getDate())) {
                return false;
            }
            
            // בדיקה שהנהג בהובלה תואם לנהג בלוח הזמנים
            if (!transport.getDriver().getId().equals(schedule.getDriver().getId())) {
                return false;
            }
        }
        
        // יצירת מזהה לרשומה
        String entryId = "ENT" + System.currentTimeMillis();
        
        // יצירת רשומה חדשה
        ScheduleEntry entry = new ScheduleEntry(entryId, startTime, endTime, transport, type, description);
        
        // בדיקת תקינות רשומה
        if (!entry.isValid()) {
            return false;
        }
        
        // הוספת הרשומה ללוח הזמנים
        schedule.addEntry(entry);
        
        // בדיקת תקינות לוח הזמנים לאחר ההוספה
        if (!schedule.isValid()) {
            // אם יש בעיה, נסיר את הרשומה שהוספנו
            schedule.removeEntry(entryId);
            return false;
        }
        
        return true;
    }
    
    /**
     * הסרת רשומה מלוח זמנים
     */
    public boolean removeScheduleEntry(String scheduleId, String entryId) {
        DriverSchedule schedule = getScheduleById(scheduleId);
        
        if (schedule == null) {
            return false;
        }
        
        // רק לוח זמנים במצב טיוטה או מאושר ניתן לעדכן
        if (schedule.getStatus() != ScheduleStatus.DRAFT && 
            schedule.getStatus() != ScheduleStatus.CONFIRMED) {
            return false;
        }
        
        return schedule.removeEntry(entryId);
    }
    
    /**
     * עדכון סטטוס לוח זמנים
     */
    public boolean updateScheduleStatus(String scheduleId, ScheduleStatus status) {
        DriverSchedule schedule = getScheduleById(scheduleId);
        
        if (schedule == null) {
            return false;
        }
        
        // בדיקת תקינות מעברי סטטוס
        ScheduleStatus currentStatus = schedule.getStatus();
        
        if (currentStatus == ScheduleStatus.CANCELLED) {
            return false; // אין לעדכן לוח זמנים שבוטל
        }
        
        if (currentStatus == ScheduleStatus.COMPLETED && status != ScheduleStatus.COMPLETED) {
            return false; // אין לעדכן לוח זמנים שהושלם
        }
        
        // בדיקה שהלוח תקין לפני אישור
        if (status == ScheduleStatus.CONFIRMED && !schedule.isValid()) {
            return false; // לא ניתן לאשר לוח לא תקין
        }
        
        schedule.setStatus(status);
        return true;
    }
    
    /**
     * קבלת לוח זמנים לפי מזהה
     */
    public DriverSchedule getScheduleById(String scheduleId) {
        return schedules.stream()
                .filter(s -> s.getId().equals(scheduleId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * קבלת לוח זמנים של נהג לפי תאריך
     */
    public DriverSchedule getDriverScheduleByDate(String driverId, LocalDate date) {
        return schedules.stream()
                .filter(s -> s.getDriver().getId().equals(driverId) && s.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * קבלת לוחות זמנים לפי סטטוס
     */
    public List<DriverSchedule> getSchedulesByStatus(ScheduleStatus status) {
        return schedules.stream()
                .filter(s -> s.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * קבלת כל לוחות הזמנים
     */
    public List<DriverSchedule> getAllSchedules() {
        return new ArrayList<>(schedules);
    }
    
    /**
     * קבלת כל לוחות הזמנים של נהג מסוים
     */
    public List<DriverSchedule> getDriverSchedules(String driverId) {
        return schedules.stream()
                .filter(s -> s.getDriver().getId().equals(driverId))
                .collect(Collectors.toList());
    }
    
    /**
     * קבלת לוחות זמנים לפי טווח תאריכים
     */
    public List<DriverSchedule> getSchedulesByDateRange(LocalDate fromDate, LocalDate toDate) {
        return schedules.stream()
                .filter(s -> !s.getDate().isBefore(fromDate) && !s.getDate().isAfter(toDate))
                .collect(Collectors.toList());
    }
    
    /**
     * בדיקה האם נהג זמין בתאריך ושעה מסוימים
     */
    public boolean isDriverAvailable(String driverId, LocalDate date, LocalTime fromTime, LocalTime toTime) {
        DriverSchedule schedule = getDriverScheduleByDate(driverId, date);
        
        // אם אין לוח זמנים, הנהג זמין
        if (schedule == null) {
            return true;
        }
        
        // בדיקה אם יש חפיפה עם פעילות קיימת
        for (ScheduleEntry entry : schedule.getEntries()) {
            if (fromTime.isBefore(entry.getEndTime()) && toTime.isAfter(entry.getStartTime())) {
                return false; // יש חפיפה
            }
        }
        
        return true;
    }
    
    /**
     * חישוב שעות עבודה יומיות של נהג
     */
    public double calculateDriverDailyHours(String driverId, LocalDate date) {
        DriverSchedule schedule = getDriverScheduleByDate(driverId, date);
        
        if (schedule == null) {
            return 0;
        }
        
        double totalHours = 0;
        
        for (ScheduleEntry entry : schedule.getEntries()) {
            // נחשב רק פעילויות שהן לא הפסקה
            if (entry.getType() != EntryType.BREAK) {
                double entryHours = 
                    (entry.getEndTime().toSecondOfDay() - entry.getStartTime().toSecondOfDay()) / 3600.0;
                totalHours += entryHours;
            }
        }
        
        return totalHours;
    }
}
