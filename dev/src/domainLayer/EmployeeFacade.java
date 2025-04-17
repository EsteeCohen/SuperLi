package domainLayer;

import domainLayer.Enums.WageType;

import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;

public class EmployeeFacade {
    private Dictionary<String, EmployeeDL> employees;
    private RoleFacade roleFacade;

    private EmployeeFacade(RoleFacade roleFacade) {
        this.roleFacade = roleFacade;
        this.employees = new Hashtable<String, EmployeeDL>();
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

    public boolean registerEmployee(String fullName, String password, String id, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff) {
        if (employees.get(id) != null) {
            return false; // Employee with this ID already exists
        }
        EmployeeDL newEmployee = new EmployeeDL(id, password, fullName, LocalDate.now(), wage, wageTypeChar, yearlySickDays, yearlyDaysOff);
        employees.put(id, newEmployee);
        return true;
    }
}
