package employeeDev.src.presentationLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.entities.Site;
import employeeDev.src.serviceLayer.EmployeeSL;
import employeeDev.src.serviceLayer.RoleSL;

public class EmployeePL {
    private final String id;
    private final String fullName;
    private final LocalDate wordStartingDate;
    private final int wage;
    private final WageTypePL wageType;
    private final List<RolePL> roles;
    private final int yearlySickDays;
    private final int yearlyDaysOff;
    private final Site site;

    public EmployeePL(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, Site site) {
        this.id = id;
        this.fullName = fullName;
        this.wordStartingDate = wordStartingDate;
        this.wage = wage;
        this.wageType = WageTypePL.fromChar(wageTypeChar);
        this.roles = new ArrayList<>();
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
        this.site = site; 
    }
    public EmployeePL(EmployeeSL employeeSL) {
        this.id = employeeSL.getId();
        this.fullName = employeeSL.getFullName();
        this.wordStartingDate = employeeSL.getWordStartingDate();
        this.wage = employeeSL.getWage();
        this.wageType = WageTypePL.valueOf(employeeSL.getWageType().name());
        this.roles = new ArrayList<>();
        for (RoleSL roleSL : employeeSL.getRoles()) {
            this.roles.add(new RolePL(roleSL));
        }
        this.yearlySickDays = employeeSL.getYearlySickDays();
        this.yearlyDaysOff = employeeSL.getYearlyDaysOff();
        this.site = employeeSL.getSite();
    }
    

    public List<String> getRoles() {
        return roles.stream()
                .map(RolePL::getName)
                .toList();
    }

    public String getID() {
        return id;
    }


    @Override
    public String toString() {
        return "----------Employee Details:----------\n" + 
                "Id: " + id + '\n' +
                "Full Name: " + fullName + '\n' +
                "Starting Date: " + wordStartingDate + "\n" +
                "Wage: " + wage + "\n" +
                "Wage Type: " + wageType + "\n" +
                "Roles: " + roles + "\n" +
                "Yearly Sick Days: " + yearlySickDays + "\n" +
                "Yearly Days Off: " + yearlyDaysOff + "\n" +
                "Site: " + site.getName() + " at " + site.getAddress() + "\n" +
                "-------------------------------------\n";
    }

    public String getFullName() {
        return fullName;
    }
}
