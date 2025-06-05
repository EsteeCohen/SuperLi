package employeeDev.src.dataAcssesLayer;

import employeeDev.src.dtos.DriverDTO;
import employeeDev.src.dtos.EmployeeDTO;
import employeeDev.src.dtos.RoleDTO;
import employeeDev.src.dtos.SiteDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private final String DBPath = "jdbc:sqlite:" + DBConstants.DB_PATH;
    private final String employeeTableName = DBConstants.EMPLOYEE_TABLE;
    private final String employeeRoleTableName = DBConstants.EMPLOYEE_ROLES_TABLE;
    private final String DriversTable = DBConstants.DRIVER_TABLE;
    private final SiteDAO siteDAO;

    public EmployeeDAO() {
        this.siteDAO = new SiteDAO();
    }

    public EmployeeDTO getEmployeeById(String id) {
        try (Connection employeeConn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT * FROM " + employeeTableName + " WHERE id = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String password = rs.getString("password");
                        String fullName = rs.getString("fullName");
                        LocalDate workStartingDate = Instant.ofEpochSecond(rs.getInt("workStartingDate"))
                                .atZone(ZoneId.systemDefault()).toLocalDate();
                        int wage = rs.getInt("wage");
                        String wageType = rs.getString("wageType");
                        int yearlySickDays = rs.getInt("yearlySickDays");
                        int yearlyDaysOff = rs.getInt("yearlyDaysOff");
                        String phoneNumber = rs.getString("phoneNumber");
                        List<RoleDTO> roles = getRolesForEmployee(id);
                        SiteDTO site = siteDAO.getSite(rs.getString("site_name"));
                        if (site == null) {
                            throw new SQLException("Site with name " + rs.getString("site_name") + " not found.");
                        }
                        // Check if the employee is a driver
                        String licenseType = getDriversLicenseType(id);
                        if (licenseType != null) {
                            boolean availableToDrive = getDriverAvailability(id);
                            return new DriverDTO(id, password, fullName, workStartingDate, wage,
                                    wageType, yearlySickDays, yearlyDaysOff, roles, site, phoneNumber, licenseType, availableToDrive);
                        }
                        // If not a driver, return as a regular employee
                        return new EmployeeDTO(id, password, fullName, workStartingDate, wage,
                                wageType, yearlySickDays, yearlyDaysOff, roles, site, phoneNumber);
                    } else {
                        return null;
                    }
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Error retrieving employee with ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Error retrieving employee with ID " + id, e);
        }
    }

    private List<RoleDTO> getRolesForEmployee(String employeeId) {
        List<RoleDTO> roles = new java.util.ArrayList<>();
        try (Connection roleConn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT * FROM " + employeeRoleTableName + " WHERE employee_id = ?";
            try (PreparedStatement statement = roleConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String roleName = rs.getString("role_roleName");
                        roles.add(new RoleDTO(roleName));
                    }
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Error retrieving roles for employee with ID " + employeeId + ": " + e.getMessage());
            throw new RuntimeException("Error retrieving roles for employee with ID " + employeeId, e);
        }
        return roles;
    }

    public List<EmployeeDTO> getAllEmployees() {
        try (Connection employeeConn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT * FROM " + employeeTableName;
            try (PreparedStatement statement = employeeConn.prepareStatement(query);
                    ResultSet rs = statement.executeQuery()) {
                List<EmployeeDTO> employees = new ArrayList<>();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String password = rs.getString("password");
                    String fullName = rs.getString("fullName");
                    LocalDate workStartingDate = Instant.ofEpochSecond(rs.getInt("workStartingDate"))
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    int wage = rs.getInt("wage");
                    String wageType = rs.getString("wageType");
                    int yearlySickDays = rs.getInt("yearlySickDays");
                    int yearlyDaysOff = rs.getInt("yearlyDaysOff");
                    String phoneNumber = rs.getString("phoneNumber");
                    List<RoleDTO> roles = getRolesForEmployee(id);
                    SiteDTO site = siteDAO.getSite(rs.getString("site_name"));
                        if (site == null) {
                            throw new SQLException("Site with name " + rs.getString("site_name") + " not found.");
                        }
                    // Check if the employee is a driver
                    String licenseType = getDriversLicenseType(id);
                    if (licenseType != null) {
                        boolean availableToDrive = getDriverAvailability(id);
                        employees.add(new DriverDTO(id, password, fullName, workStartingDate, wage, wageType,
                                yearlySickDays, yearlyDaysOff, roles, site, phoneNumber, licenseType, availableToDrive));
                        continue;
                    }
                    // If not a driver, return as a regular employee
                    employees.add(new EmployeeDTO(id, password, fullName, workStartingDate, wage, wageType,
                            yearlySickDays, yearlyDaysOff, roles, site, phoneNumber));
                }
                return employees;
            }
        }
        catch (SQLException e) {
            System.err.println("Error retrieving all employees: " + e.getMessage());
            throw new RuntimeException("Error retrieving all employees", e);
        }
    }

    private boolean getDriverAvailability(String id) {
        try (Connection driverConn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT availableToDrive FROM " + DriversTable + " WHERE employee_id = ?";
            try (PreparedStatement statement = driverConn.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("availableToDrive") == 1;
                    }
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Error retrieving driver availability for ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Error retrieving driver availability for ID " + id, e);
        }
        return false;
    }

    private String getDriversLicenseType(String id) {
        try (Connection driverConn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT licenseType FROM " + DriversTable + " WHERE employee_id = ?";
            try (PreparedStatement statement = driverConn.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("licenseType");
                    }
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Driver license type for ID " + id + " not found.");
        }
        return null;
    }

    public void instertEmployee(EmployeeDTO employee) {
        try (Connection employeeConn = DriverManager.getConnection(DBPath)) {
            String query = "INSERT INTO " + employeeTableName
                    + " (id, password, fullName, workStartingDate, wage, wageType, yearlySickDays, yearlyDaysOff, site_name, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employee.getId());
                statement.setString(2, employee.getPassword());
                statement.setString(3, employee.getFullName());
                statement.setLong(4, employee.getWorkStartingDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
                statement.setInt(5, employee.getWage());
                statement.setString(6, employee.getWageType());
                statement.setInt(7, employee.getYearlySickDays());
                statement.setInt(8, employee.getYearlyDaysOff());
                statement.setString(9, employee.getSite().getName());
                statement.setString(10, employee.getPhoneNumber());
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error inserting employee with ID " + employee.getId() + ": " + e.getMessage());
            throw new RuntimeException("Error inserting employee with ID " + employee.getId(), e);
        }
    }

    public void instertDriver(DriverDTO driver) {
        instertEmployee(driver);
        try (Connection driverConn = DriverManager.getConnection(DBPath)) {
            String query = "INSERT INTO " + DriversTable + " (employee_id, licenseType, availableToDrive) VALUES (?, ?, ?)";
            try (PreparedStatement statement = driverConn.prepareStatement(query)) {
                statement.setString(1, driver.getId());
                statement.setString(2, driver.getLicenseType());
                statement.setInt(3, driver.isAvailableToDrive() ? 1 : 0);
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error inserting driver with ID " + driver.getId() + ": " + e.getMessage());
            throw new RuntimeException("Error inserting driver with ID " + driver.getId(), e);
        }
    }

    public void updateEmployee(EmployeeDTO employee) {
        try (Connection employeeConn = DriverManager.getConnection(DBPath)) {
            String query = "UPDATE " + employeeTableName
                    + " SET password = ?, fullName = ?, workStartingDate = ?, wage = ?, wageType = ?, yearlySickDays = ?, yearlyDaysOff = ?, site_name = ?, phoneNumber = ? WHERE id = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employee.getPassword());
                statement.setString(2, employee.getFullName());
                statement.setLong(3,
                        employee.getWorkStartingDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
                statement.setInt(4, employee.getWage());
                statement.setString(5, employee.getWageType());
                statement.setInt(6, employee.getYearlySickDays());
                statement.setInt(7, employee.getYearlyDaysOff());
                statement.setString(8, employee.getSite().getName());
                statement.setString(9, employee.getPhoneNumber());
                statement.setString(10, employee.getId());
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error updating employee with ID " + employee.getId() + ": " + e.getMessage());
            throw new RuntimeException("Error updating employee with ID " + employee.getId(), e);
        }
    }

    public void updateDriver(DriverDTO driver) {
        updateEmployee(driver);
        try (Connection driverConn = DriverManager.getConnection(DBPath)) {
            String updateQuery = "UPDATE " + DriversTable + " SET licenseType = ?, availableToDrive = ? WHERE employee_id = ?";
            try (PreparedStatement insertStatement = driverConn.prepareStatement(updateQuery)) {
                insertStatement.setString(1, driver.getLicenseType());
                insertStatement.setInt(2, driver.isAvailableToDrive() ? 1 : 0);
                insertStatement.setString(3, driver.getId());
                insertStatement.executeUpdate();
                
            }
        }
        catch (SQLException e) {
            System.err.println("Error updating driver with ID " + driver.getId() + ": " + e.getMessage());
            throw new RuntimeException("Error updating driver with ID " + driver.getId(), e);
        }
    }

    public void deleteEmployee(String id) {
        EmployeeDTO employee = getEmployeeById(id);
        if (employee != null) {
            try (Connection employeeConn = DriverManager.getConnection(DBPath)) {
                String query = "DELETE FROM " + employeeTableName + " WHERE id = ?";
                try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                    statement.setString(1, id);
                    statement.executeUpdate();
                }
            }
            catch (SQLException e) {
                System.err.println("Error deleting employee with ID " + id + ": " + e.getMessage());
                throw new RuntimeException("Error deleting employee with ID " + id, e);
            }
        }
        
    }

    public void deleteDriver(String id) {
        try (Connection driverConn = DriverManager.getConnection(DBPath)) {
            String deleteDriverQuery = "DELETE FROM " + DriversTable + " WHERE employee_id = ?";
            try (PreparedStatement deleteStatement = driverConn.prepareStatement(deleteDriverQuery)) {
                deleteStatement.setString(1, id);
                deleteStatement.executeUpdate();
            }
            String deleteEmployeeQuery = "DELETE FROM " + employeeTableName + " WHERE id = ?";
            try (PreparedStatement deleteEmployeeStatement = driverConn.prepareStatement(deleteEmployeeQuery)) {
                deleteEmployeeStatement.setString(1, id);
                deleteEmployeeStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error deleting driver with ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Error deleting driver with ID " + id, e);
        }

    }

    public void assignRoleToEmployee(String employeeId, String roleName) {
        try (Connection employeeConn = DriverManager.getConnection(DBPath)) {
            String query = "INSERT OR REPLACE INTO " + employeeRoleTableName + " (employee_id, role_roleName) VALUES (?, ?)";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                statement.setString(2, roleName);
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error assigning role " + roleName + " to employee with ID " + employeeId + ": " + e.getMessage());
            throw new RuntimeException("Error assigning role " + roleName + " to employee with ID " + employeeId, e);
        }
    }

    public void removeRoleFromEmployee(String employeeId, String roleName) {
        try (Connection employeeConn = DriverManager.getConnection(DBPath)) {
            String query = "DELETE FROM " + employeeRoleTableName + " WHERE employee_id = ? AND role_roleName = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                statement.setString(2, roleName);
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error removing role " + roleName + " from employee with ID " + employeeId + ": " + e.getMessage());
            throw new RuntimeException("Error removing role " + roleName + " from employee with ID " + employeeId, e);
        }
    }
}
