package employeeDev.src.serviceLayer;

import java.util.Map;

import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;

public class EmployeeService {
    private EmployeeFacade employeeFacade;

    public EmployeeService(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    public EmployeeSL login(String id, String password) {
        EmployeeDL edl = employeeFacade.login(id, password);
        if (edl != null) {
            return new EmployeeSL(edl);
        }
        return null; // or throw an exception
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

    public boolean assignRoleToEmployee(String employeeId, String roleName) {
        return employeeFacade.assignRoleToEmployee(employeeId, roleName);
    }

    public void registerAdmin(){
        registerEmployee("Bombardino Crocodilo", "3", "admin", 9999, "Hourly", 1000, 1000);
        assignRoleToEmployee("admin", "HrManager");
    }

    public void updateEmployeeAttributes(String employeeId, Map<String, Object> attributes){
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        employee.updateAttributes(attributes);
    }
}
