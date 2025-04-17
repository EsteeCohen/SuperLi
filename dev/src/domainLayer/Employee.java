package domainLayer;

import domainLayer.Enums.WageType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private final String id;
    private final String password;
    private final  String fullName;
    private final LocalDate wordStartingDate;
    private final int wage;
    private final WageType wageType;
    private final List<Role> roles;
    private final int yearlySickDays;
    private final int yearlyDaysOff;

    public Employee(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.wordStartingDate = wordStartingDate;
        this.wage = wage;
        this.wageType = WageType.fromChar(wageTypeChar);
        this.roles = new ArrayList<Role>();
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public String getId() {
        return id;
    }
}
