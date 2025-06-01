package dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDTO {
    private String id;
    private String password;
    private String fullName;
    private LocalDate workStartingDate;
    private int wage;
    private String wageType;
    private int yearlySickDays;
    private int yearlyDaysOff;
    private List<RoleDTO> roles;

    public EmployeeDTO(String id, String password, String fullName, LocalDate workStartingDate, int wage,
            String wageType, int yearlySickDays, int yearlyDaysOff, List<RoleDTO> roles) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.workStartingDate = workStartingDate;
        this.wage = wage;
        this.wageType = wageType;
        this.yearlySickDays = yearlySickDays;
        this.yearlyDaysOff = yearlyDaysOff;
        this.roles = roles != null ? roles : new ArrayList<>();
    }

    public EmployeeDTO(String employeeId) {
        //TODO Auto-generated constructor stub
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getWorkStartingDate() {
        return workStartingDate;
    }

    public void setWorkStartingDate(LocalDate workStartingDate) {
        this.workStartingDate = workStartingDate;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public String getWageType() {
        return wageType;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setWageType(String wageType) {
        this.wageType = wageType;
    }

    public int getYearlySickDays() {
        return yearlySickDays;
    }

    public void setYearlySickDays(int yearlySickDays) {
        this.yearlySickDays = yearlySickDays;
    }

    public int getYearlyDaysOff() {
        return yearlyDaysOff;
    }

    public void setYearlyDaysOff(int yearlyDaysOff) {
        this.yearlyDaysOff = yearlyDaysOff;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles != null ? roles : new ArrayList<>();
    }
}
