package employeeDev.src.domainLayer;


import employeeDev.src.dataAcssesLayer.EmployeeDAO;
import employeeDev.src.dtos.DriverDTO;
import employeeDev.src.dtos.EmployeeDTO;
import employeeDev.src.mappers.EmployeeMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

public class EmployeeFacade {
    private final Map<String, EmployeeDL> employees;
    private final RoleFacade roleFacade;
    private final SiteFacade siteFacade;

    public EmployeeFacade(RoleFacade roleFacade, SiteFacade siteFacade) {
        this.roleFacade = roleFacade;
        this.siteFacade = siteFacade;
        this.employees = new HashMap<>();
    }

    public void addEmployee(EmployeeDL e) {
        if (e != null) {
            employees.put(e.getId(), e);
            e.insertIntoDB();
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

    public boolean registerEmployee(String fullName, String password, String id, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, String siteName, String phoneNumber) {
        if (employees.get(id) != null) {
            return false; // Employee with this ID already exists
        }
        EmployeeDL newEmployee = new EmployeeDL(id, password, fullName, LocalDate.now(), wage, wageTypeChar, yearlySickDays, yearlyDaysOff, siteFacade.getSiteByName(siteName), phoneNumber);
        addEmployee(newEmployee);
        return true;
    }

    public boolean registerDriver(String fullName, String password, String id, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, String siteName, String phoneNumber, LicenseType licenseType) {
        if (employees.get(id) != null) {
            return false; // Employee with this ID already exists
        }
        DriverDL newDriver = new DriverDL(id, password, fullName, LocalDate.now(), wage, wageTypeChar, yearlySickDays, yearlyDaysOff, siteFacade.getSiteByName(siteName), phoneNumber, licenseType, roleFacade.getRoleByName("Driver"), true);
        addEmployee(newDriver);
        return true;
    }

    public boolean assignRoleToEmployee(String employeeId, String roleName) {
        EmployeeDL employee = employees.get(employeeId);
        if (employee != null) {
            RoleDL role = roleFacade.getRoleByName(roleName);
            if (role != null) {
                employee.getRoles().add(role);
                EmployeeDAO employeeDAO = new EmployeeDAO();
                employeeDAO.assignRoleToEmployee(employeeId, roleName);
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

    // Load employees from the database
    public void loadEmployeesFromDB() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<EmployeeDTO> employeeList = employeeDAO.getAllEmployees();
        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.isDriver()) {
               employees.put(employeeDTO.getId(), EmployeeMapper.fromDriverDTO((DriverDTO)employeeDTO, roleFacade, siteFacade));
            } else {
               employees.put(employeeDTO.getId(), EmployeeMapper.fromDTO(employeeDTO, roleFacade, siteFacade));
            }
        }
    }

    public void updateEmployeeAttributes(String employeeId, Map<String, Object> attributes) {
        EmployeeDL employee = employees.get(employeeId);
        if (employee != null) {
            employee.updateAttributes(attributes);
            employee.updateInDB();
        }
    }

    public boolean canDriverDrive(String driverID, LicenseType licenseType) {
        DriverDL driver = (DriverDL) employees.get(driverID);
        if (driver == null) {
            return false; // Driver not found
        }
        return driver.isAvailableToDrive() && driver.isLicensed(licenseType);
    }

    public void setAvailableToDrive(String driverID,boolean isAvailable) {
        DriverDL driver = (DriverDL) employees.get(driverID);
        if (driver == null) {
            return; // Driver not found
        }
        driver.setAvailableToDrive(isAvailable);
        driver.updateInDB();
    }

    public List<RoleDL> getAllRoles() {
        return roleFacade.getAllRoles();
    }

    public boolean unassignRoleFromEmployee(String employeeId, String roleName) {
        EmployeeDL employee = employees.get(employeeId);
        if (employee.isDriver() && roleName.equals("Driver")) {
            return false;
        }
        if (employee != null) {
            RoleDL role = roleFacade.getRoleByName(roleName);
            if (role != null && employee.removeRole(roleName)) {
                EmployeeDAO employeeDAO = new EmployeeDAO();
                employeeDAO.removeRoleFromEmployee(employeeId, roleName);
                return true;
            }
        }
        return false;
    }
}
