package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.DriverDL;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.serviceLayer.Interfaces.UserManagmentInteface;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

public class EmployeeService implements UserManagmentInteface {
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
                .filter(employee -> employee.isDriver())
                .map(employee -> (DriverDL) employee)
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
        employeeFacade.updateEmployeeAttributes(employeeId, attributes);
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

    @Override
    public EmployeeSL getUserById(String id) {
        return getEmployeeById(id);
    }

    @Override
    public List<EmployeeSL> getAllUsers() {
        return employeeFacade.getAllEmployees().stream()
                .map(EmployeeSL::new)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean hasRole(String userId, String roleName) {
        EmployeeSL employee = getUserById(userId);
        if (employee == null) {
            return false; // User not found
        }
        return employee.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public String[] getAllRoles() {
        return employeeFacade.getAllRoles().stream()
                .map(role -> role.getName())
                .toArray(String[]::new);
    }

    public void registerEmployeeWithRole(String name, String password, String id, Integer salary, String wageType,
            Integer yearlySickDays, Integer yearlyDaysOff, String siteName, String phone, String role) {
        employeeFacade.registerEmployee(name, password, id, salary.intValue(), wageType.charAt(0), yearlySickDays.intValue(), yearlyDaysOff.intValue(), siteName, phone);
        employeeFacade.assignRoleToEmployee(id, role);
    }

    public void registerDriver(String name, String password, String id, Integer salary, String wageType,
            Integer yearlySickDays, Integer yearlyDaysOff, String siteName, String phone, LicenseType licenseType) {
                employeeFacade.registerDriver(name, password, id, salary, wageType.charAt(0), yearlySickDays, yearlyDaysOff, siteName, phone, licenseType);

    }

    public List<LicenseType> getAllLicensesType() {
        return List.of(LicenseType.values());
    }

    public boolean unassignRoleFromEmployee(String employeeId, String roleName) {
        return employeeFacade.unassignRoleFromEmployee(employeeId, roleName);
    }

    public boolean isEmployeeExists(String employeeId) {
        return employeeFacade.getEmployee(employeeId) != null;
    }
}

