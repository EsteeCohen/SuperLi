package serviceLayer;

import domainLayer.EmployeeDL;
import domainLayer.RoleDL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSL {
    private final String id;
    private final String fullName;
    private final LocalDate wordStartingDate;
    private final int wage;
    private final WageTypeSL wageType;
    private final List<RoleSL> roles;
    private final int yearlySickDays;
    private final int yearlyDaysOff;

    public EmployeeSL(String id, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        this.id = id;
        this.fullName = fullName;
        this.wordStartingDate = wordStartingDate;
        this.wage = wage;
        this.wageType = WageTypeSL.fromChar(wageTypeChar);
        this.roles = new ArrayList<>();
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
    }

    public EmployeeSL(EmployeeDL edl){
        this.id = edl.getId();
        this.fullName = edl.getFullName();
        this.wordStartingDate = edl.getWordStartingDate();
        this.wage = edl.getWage();
        this.wageType = WageTypeSL.fromChar(edl.getWageType().getChar());
        this.roles = new ArrayList<>();
        for (RoleDL role : edl.getRoles()) {
            this.roles.add(new RoleSL(role));
        }
        this.yearlySickDays = edl.getYearlySickDays();
        this.yearlyDaysOff = edl.getYearlyDaysOff();
    }

    // Getters for the fields (optional, if needed)
    public String getId() {
        return id;
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
