package employeeDev.src.domainLayer;

import java.sql.Driver;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import transportDev.src.main.entities.Site;

import java.util.HashMap;
import java.util.List;

public class EmployeeFacade {
    private Map<String, EmployeeDL> employees;
    private RoleFacade roleFacade;

    public EmployeeFacade(RoleFacade roleFacade) {
        this.roleFacade = roleFacade;
        this.employees = new HashMap<String, EmployeeDL>();
    }

    public void addEmployee(EmployeeDL e) {
        if (e != null) {
            employees.put(e.getId(), e);
        }
    }

    public EmployeeDL getEmployee(String id) {
        return employees.get(id);
    }

    public boolean employeeHasRole(String id, String roleName) {
        EmployeeDL employee = employees.get(id);
        if (employee != null) {
            return employee.hasRole(roleFacade.getRoleByName(roleName));
        }
        return false;
    }

    public EmployeeDL login(String id, String password) {
        EmployeeDL employee = employees.get(id);
        if (employee != null && employee.IsPassword(password)) {
            return employee;
        }
        // wrong password or id
        return null;
    }

    public boolean registerEmployee(String fullName, String password, String id, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, Site site) {
        if (employees.get(id) != null) {
            return false; // Employee with this ID already exists
        }
        EmployeeDL newEmployee = new EmployeeDL(id, password, fullName, LocalDate.now(), wage, wageTypeChar, yearlySickDays, yearlyDaysOff, site);
        employees.put(id, newEmployee);
        return true;
    }

    public boolean assignRoleToEmployee(String employeeId, String roleName) {
        EmployeeDL employee = employees.get(employeeId);
        if (employee != null) {
            RoleDL role = roleFacade.getRoleByName(roleName);
            if (role != null) {
                employee.getRoles().add(role);
                return true;
            }
        }
        return false; // Employee or role not found
    }

    public List<EmployeeDL> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public List<EmployeeDL> getEmployeesBySite(Site site) {
        List<EmployeeDL> employeesAtSite = new ArrayList<>();
        for (EmployeeDL employee : employees.values()) {
            if (employee.getSite().equals(site)) {
                employeesAtSite.add(employee);
            }
        }
        return employeesAtSite;
    }

    public List<DriverDL> getDrivers() {
        List<DriverDL> drivers = new ArrayList<>();
        for (EmployeeDL employee : employees.values()) {
            if (employee.isDriver()) {
                drivers.add((DriverDL) employee);
            }
        }
        return drivers;
    }
}
