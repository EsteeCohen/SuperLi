package src.main.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import src.main.entities.DriverSchedule;
import src.main.enums.EntryType;
import src.main.enums.ScheduleStatus;
import src.main.services.ScheduleService;


// אחראי על ניהול לוחות הזמנים של נהגים
public class ScheduleController {
    private ScheduleService scheduleService;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    // קונסטרקטור
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    // יצירת לוח זמנים חדש לנהג
    public boolean createSchedule(String driverId, LocalDate date) {
        return scheduleService.createSchedule(driverId, date);
    }
    
    // יצירת לוח זמנים חדש לנהג (עם תאריך כמחרוזת)
    public boolean createSchedule(String driverId, String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            return createSchedule(driverId, date);
        } catch (DateTimeParseException e) {
            return false; // פורמט תאריך לא תקין
        }
    }
    
    // הוספת רשומה ללוח זמנים
    public boolean addScheduleEntry(String scheduleId, LocalTime startTime, LocalTime endTime, 
                                  int transportId, String entryTypeStr, String description) {
        try {
            EntryType entryType = EntryType.valueOf(entryTypeStr);
            return scheduleService.addScheduleEntry(scheduleId, startTime, endTime, transportId, entryType, description);
        } catch (IllegalArgumentException e) {
            return false; // סוג רשומה לא תקין
        }
    }
    
    // הוספת רשומה ללוח זמנים (עם שעות כמחרוזות)
    public boolean addScheduleEntry(String scheduleId, String startTimeStr, String endTimeStr, 
                                  int transportId, String entryTypeStr, String description) {
        try {
            LocalTime startTime = LocalTime.parse(startTimeStr, timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeStr, timeFormatter);
            return addScheduleEntry(scheduleId, startTime, endTime, transportId, entryTypeStr, description);
        } catch (DateTimeParseException e) {
            return false; // פורמט זמן לא תקין
        }
    }
    
    // הסרת רשומה מלוח זמנים
    public boolean removeScheduleEntry(String scheduleId, String entryId) {
        return scheduleService.removeScheduleEntry(scheduleId, entryId);
    }
    
    // עדכון סטטוס לוח זמנים
    public boolean updateScheduleStatus(String scheduleId, String statusStr) {
        try {
            ScheduleStatus status = ScheduleStatus.valueOf(statusStr);
            return scheduleService.updateScheduleStatus(scheduleId, status);
        } catch (IllegalArgumentException e) {
            return false; // סטטוס לא תקין
        }
    }
    
    // קבלת לוח זמנים לפי מזהה
    public DriverSchedule getScheduleById(String scheduleId) {
        return scheduleService.getScheduleById(scheduleId);
    }
    
    // קבלת לוח זמנים של נהג לפי תאריך
    public DriverSchedule getDriverScheduleByDate(String driverId, LocalDate date) {
        return scheduleService.getDriverScheduleByDate(driverId, date);
    }
    
    // קבלת לוח זמנים של נהג לפי תאריך (עם תאריך כמחרוזת)
    public DriverSchedule getDriverScheduleByDate(String driverId, String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            return getDriverScheduleByDate(driverId, date);
        } catch (DateTimeParseException e) {
            return null; // פורמט תאריך לא תקין
        }
    }
    
    // קבלת לוח זמנים לפי סטטוס
    public List<DriverSchedule> getSchedulesByStatus(String statusStr) {
        try {
            ScheduleStatus status = ScheduleStatus.valueOf(statusStr);
            return scheduleService.getSchedulesByStatus(status);
        } catch (IllegalArgumentException e) {
            return null; // סטטוס לא תקין
        }
    }
    
    // קבלת כל לוחות הזמנים
    public List<DriverSchedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
    
    // קבלת לוחות זמנים של נהג לפי מזהה
    public List<DriverSchedule> getDriverSchedules(String driverId) {
        return scheduleService.getDriverSchedules(driverId);
    }
    
    // קבלת לוחות זמנים של נהג לפי תאריך
    public List<DriverSchedule> getSchedulesByDateRange(String fromDateStr, String toDateStr) {
        try {
            LocalDate fromDate = LocalDate.parse(fromDateStr, dateFormatter);
            LocalDate toDate = LocalDate.parse(toDateStr, dateFormatter);
            return scheduleService.getSchedulesByDateRange(fromDate, toDate);
        } catch (DateTimeParseException e) {
            return null; // פורמט תאריך לא תקין
        }
    }
    
    // בדיקה אם נהג זמין
    public boolean isDriverAvailable(String driverId, String dateStr, String fromTimeStr, String toTimeStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            LocalTime fromTime = LocalTime.parse(fromTimeStr, timeFormatter);
            LocalTime toTime = LocalTime.parse(toTimeStr, timeFormatter);
            return scheduleService.isDriverAvailable(driverId, date, fromTime, toTime);
        } catch (DateTimeParseException e) {
            return false; // פורמט תאריך או זמן לא תקין
        }
    }
    
    // חישוב שעות עבודה יומיות של נהג
    public double calculateDriverDailyHours(String driverId, String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            return scheduleService.calculateDriverDailyHours(driverId, date);
        } catch (DateTimeParseException e) {
            return -1; // פורמט תאריך לא תקין
        }
    }
}
