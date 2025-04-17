package domainLayer;

import domainLayer.Enums.WageType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDL {
    private final String id;
    private final String password;
    private final  String fullName;
    private final LocalDate wordStartingDate;
    private final int wage;
    private final WageType wageType;
    private final List<RoleDL> roles;
    private final int yearlySickDays;
    private final int yearlyDaysOff;

    public EmployeeDL(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.wordStartingDate = wordStartingDate;
        this.wage = wage;
        this.wageType = WageType.fromChar(wageTypeChar);
        this.roles = new ArrayList<RoleDL>();
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
    }

    public boolean hasRole(RoleDL role) {
        return roles.contains(role);
    }

    public String getId() {
        return id;
    }

    public boolean IsPassword(String password) {
        return this.password.equals(password);
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

    public WageType getWageType() {
        return wageType;
    }
    
    public List<RoleDL> getRoles() {
        return roles;
    }

    public int getYearlySickDays() {
        return yearlySickDays;
    }

    public int getYearlyDaysOff() {
        return yearlyDaysOff;
    }
}
