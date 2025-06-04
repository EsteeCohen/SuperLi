package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.DriverDL;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

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

    public boolean registerEmployee(String fullName, String password, String id, int wage, String wageType, int yearlySickDays, int yearlyDaysOff, String siteName, String phoneNumber) {
        return employeeFacade.registerEmployee(fullName, password, id, wage, Character.toUpperCase(wageType.charAt(0)), yearlySickDays, yearlyDaysOff, siteName, phoneNumber);
    }
    
    public List<DriverSL> getAllDrivers() {
        return employeeFacade.getAllEmployees().stream()
                .filter(employee -> employee instanceof DriverDL)
                .map(employee -> (DriverDL) employee)
                .filter(driver -> driver.getLicenseTypes() != null && !driver.getLicenseTypes().isEmpty())
                .map(DriverSL::new)
                .collect(java.util.stream.Collectors.toList());
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

    public void registerAdmin(String adminID, Site defaultSite) {
        // TO-DO: Replace with actual site retrieval logic        
        registerEmployee("Bombardino Crocodilo", "3", adminID, 9999, "Hourly", 1000, 1000, defaultSite.getName(), "1234567890");
        assignRoleToEmployee("admin", "HrManager");
    }

    public void updateEmployeeAttributes(String employeeId, Map<String, Object> attributes){
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        employee.updateAttributes(attributes);
    }

    public boolean employeeHasRole(String employeeId, String roleName) {
        var employee = getEmployeeById(employeeId);
        if (employee == null) return false;
        return employee.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public Map<String, EmployeeSL> getAllEmployees() {
        return employeeFacade.getAllEmployees().stream()
                .map(EmployeeSL::new)
                .collect(java.util.stream.Collectors.toMap(EmployeeSL::getId, e -> e));
    }
}

