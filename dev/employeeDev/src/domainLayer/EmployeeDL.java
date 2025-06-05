package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.EmployeeDAO;
import employeeDev.src.domainLayer.Enums.WageType;
import employeeDev.src.mappers.EmployeeMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;

public class EmployeeDL {
    private final String id;
    private String password;
    private String fullName;
    private final LocalDate workStartingDate;
    private int wage;
    private WageType wageType;
    private final List<RoleDL> roles;
    private int yearlySickDays;
    private int yearlyDaysOff;
    private Site site;
    private String phoneNumber;

    // Main constructor
    public EmployeeDL(String id, String password, String fullName, LocalDate workStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, Site site, String phoneNumber) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.workStartingDate = workStartingDate;
        this.wage = wage;
        this.wageType = WageType.fromChar(wageTypeChar);
        this.roles = new ArrayList<>();
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
        this.site = site;
        this.phoneNumber = phoneNumber;
        
    }

    // Constructor for creating an employee with roles
    public EmployeeDL(String id, String password, String fullName, LocalDate workStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, Site site, String phoneNumber, List<RoleDL> roles) {
        this(id, password, fullName, workStartingDate, wage, wageTypeChar, yearlySickDays, yearlyDaysOff, site, phoneNumber);
        if (roles != null) {
            this.roles.addAll(roles);
        }
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

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getWorkStartingDate() {
        return workStartingDate;
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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public boolean isDriver(){
        return false; // This method should be overridden in DriverDL or other subclasses
    }

    public void insertIntoDB() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.instertEmployee(EmployeeMapper.toDTO(this));
    }

    public void updateInDB() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.updateEmployee(EmployeeMapper.toDTO(this));
    }

    public boolean removeRole(String roleName) {
        for (RoleDL role : roles) {
            if (role.getName().equals(roleName)) {
                roles.remove(role);
                EmployeeDAO employeeDAO = new EmployeeDAO();
                employeeDAO.removeRoleFromEmployee(id, roleName);
                return true;
            }
        }
        return false; // Role not found
    }
}
