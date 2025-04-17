package src.presentationLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.serviceLayer.EmployeeSL;
import src.serviceLayer.RoleSL;

public class EmployeePL {
    private final String id;
    private final String password;
    private final String fullName;
    private final LocalDate wordStartingDate;
    private final int wage;
    private final WageType wageType;
    private final List<RolePL> roles;
    private final int yearlySickDays;
    private final int yearlyDaysOff;

    public EmployeePL(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.wordStartingDate = wordStartingDate;
        this.wage = wage;
        this.wageType = WageType.fromChar(wageTypeChar);
        this.roles = new ArrayList<>();
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
    }

    public EmployeePL(EmployeeSL employeeSL) {
        this.id = employeeSL.getId();
        this.password = employeeSL.getPassword();
        this.fullName = employeeSL.getFullName();
        this.wordStartingDate = employeeSL.getWordStartingDate();
        this.wage = employeeSL.getWage();
        this.wageType = WageType.valueOf(employeeSL.getWageType().name());
        this.roles = new ArrayList<>();
        for (RoleSL roleSL : employeeSL.getRoles()) {
            this.roles.add(new RolePL(roleSL));
        }
        this.yearlySickDays = employeeSL.getYearlySickDays();
        this.yearlyDaysOff = employeeSL.getYearlyDaysOff();
    }

    public List<String> getRole() {
        return roles.stream()
                .map(RolePL::getName)
                .toList();
    }

    public String getID() {
        return id;
    }
}
