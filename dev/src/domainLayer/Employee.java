package src.domainLayer;

import java.util.Date;
import java.util.List;
import src.domainLayer.Enums.WageType;

public class Employee {
    private String id;
    private String password;
    private String fullName;
    private Date wordStartingDate;
    private int wage;
    private WageType wageType;
    private List<Role> roles;
    private int yearlySickDays;
    private int yearlyDaysOff;

    public Employee(String id, String password, String fullName, Date wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.wordStartingDate = wordStartingDate;
        this.wage = wage;
        this.wageType = WageType.fromChar(wageTypeChar);
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
    }
}
