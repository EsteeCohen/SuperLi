package employeeDev.src.domainLayer;

import employeeDev.src.domainLayer.Enums.WageType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeDL {
    private final String id;
    private String password;
    private String fullName;
    private final LocalDate wordStartingDate;
    private int wage;
    private WageType wageType;
    private final List<RoleDL> roles;
    private int yearlySickDays;
    private int yearlyDaysOff;

    // Main constructor
    public EmployeeDL(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
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

    // Simplified constructor
    public EmployeeDL(String id, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        this(id, null, fullName, wordStartingDate, wage, wageTypeChar, yearlySickDays, yearlyDaysOff);
    }

    // Add a role to the employee
    public void addRole(RoleDL role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    // Check if the employee has a specific role
    public boolean hasRole(RoleDL role) {
        return roles.contains(role);
    }

    // Getters
    public String getId() {
        return id;
    }

    public boolean IsPassword(String password) {
        return this.password != null && this.password.equals(password);
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

    public void updateAttributes(Map<String, Object> attributes) {
        for (String key : attributes.keySet()) {
            switch (key) {
                case "password":
                    this.password = (String) attributes.get(key);
                    break;
                case "fullName":
                    this.fullName = (String) attributes.get(key);
                    break;
                case "wage":
                    this.wage = (int) attributes.get(key);
                    break;
                case "wageType":
                    this.wageType = WageType.fromChar(((String) attributes.get(key)).charAt(0));
                    break;
                case "yearlySickDays":
                    this.yearlySickDays = (int) attributes.get(key);
                    break;
                case "yearlyDaysOff":
                    this.yearlyDaysOff = (int) attributes.get(key);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        }
    }
}
