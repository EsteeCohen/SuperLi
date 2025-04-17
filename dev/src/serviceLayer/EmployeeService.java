package src.serviceLayer;

import domainLayer.EmployeeDL;
import domainLayer.EmployeeFacade;
import java.time.LocalDate;

public class EmployeeService {
    private EmployeeFacade employeeFacade;

    public EmployeeSL login(String id, String password) {
        EmployeeDL edl = employeeFacade.login(id, password);
        if (edl != null) {
            return new EmployeeSL(edl);
        } else {
            return null; // or throw an exception
        }
    }

    public boolean registerEmployee(String fullName, String password, String id, int wage, String wageType, int yearlySickDays, int yearlyDaysOff) {
        return employeeFacade.registerEmployee(fullName, password, id, wage, Character.toUpperCase(wageType.charAt(0)), yearlySickDays, yearlyDaysOff);
    }

    public EmployeeSL getEmployeeById(String id) {
        EmployeeDL edl = employeeFacade.getEmployee(id);
        if (edl != null) {
            return new EmployeeSL(edl);
        } else {
            return null; // or throw an exception
        }
    }

    public void setAvailabilityOfEmployee(String employeeId, LocalDate date, String ShiftType, boolean available) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAvailabilityOfEmployee'");
    }

    public boolean assignRoleToEmployee(String employeeId, String roleName) {
        return employeeFacade.assignRoleToEmployee(employeeId, roleName);
    }
}
