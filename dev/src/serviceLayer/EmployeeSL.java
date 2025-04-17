package src.serviceLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSL {
    private final String id;
    private final String password;
    private final String fullName;
    private final LocalDate wordStartingDate;
    private final int wage;
    private final WageTypeSL wageType;
    private final List<RoleSL> roles;
    private final int yearlySickDays;
    private final int yearlyDaysOff;

    public EmployeeSL(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.wordStartingDate = wordStartingDate;
        this.wage = wage;
        this.wageType = WageTypeSL.fromChar(wageTypeChar);
        this.roles = new ArrayList<>();
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
    }

    // Getters for the fields (optional, if needed)
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getWordStartingDate() {
        return wordStartingDate;
    }

    public int getWage() {
        return wage;
    }

    public WageTypeSL getWageType() {
        return wageType;
    }

    public List<RoleSL> getRoles() {
        return roles;
    }

    public int getYearlySickDays() {
        return yearlySickDays;
    }

    public int getYearlyDaysOff() {
        return yearlyDaysOff;
    }
}
