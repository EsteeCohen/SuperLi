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
import java.util.List;

public class EmployeeDAO {

    private final String DBPath = DBConstants.DB_PATH;
    private final String employeeTableName = DBConstants.EMPLOYEE_TABLE;
    private final String roleTableName = DBConstants.ROLE_TABLE;
    private final String DriversTable = DBConstants.DRIVER_TABLE;
    private final SiteDAO siteDAO;

    public EmployeeDAO() {
        this.siteDAO = new SiteDAO();
    }

    public EmployeeDTO getEmployeeById(String id) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + employeeTableName + " WHERE id = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String password = rs.getString("password");
                        String fullName = rs.getString("fullName");
                        LocalDate workStartingDate = Instant.ofEpochSecond(rs.getInt("workStatingDate"))
                                .atZone(ZoneId.systemDefault()).toLocalDate();
                        int wage = rs.getInt("wage");
                        String wageType = rs.getString("wageType");
                        int yearlySickDays = rs.getInt("yearlySickDays");
                        int yearlyDaysOff = rs.getInt("yearlyDaysOff");
                        String phoneNumber = rs.getString("phoneNumber");
                        List<RoleDTO> roles = getRolesForEmployee(id);
                        SiteDTO site = siteDAO.getSite(rs.getString("siteName"));
                        if (site == null) {
                            throw new SQLException("Site with name " + rs.getString("siteName") + " not found.");
                        }
                        // Check if the employee is a driver
                        List<String> licenseTypes = getLicenseTypesOfDriver(id);
                        if (licenseTypes != null) {
                            return new DriverDTO(id, password, fullName, workStartingDate, wage,
                                    wageType, yearlySickDays, yearlyDaysOff, roles, site, phoneNumber, licenseTypes);
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
    }

    private List<RoleDTO> getRolesForEmployee(String employeeId) throws SQLException {
        List<RoleDTO> roles = new java.util.ArrayList<>();
        try (Connection roleConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + roleTableName + " WHERE employee_id = ?";
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
        return roles;
    }

    public List<EmployeeDTO> getAllEmployees() throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + employeeTableName;
            try (PreparedStatement statement = employeeConn.prepareStatement(query);
                    ResultSet rs = statement.executeQuery()) {
                List<EmployeeDTO> employees = new java.util.ArrayList<>();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String password = rs.getString("password");
                    String fullName = rs.getString("fullName");
                    LocalDate workStartingDate = Instant.ofEpochSecond(rs.getInt("workStatingDate"))
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    int wage = rs.getInt("wage");
                    String wageType = rs.getString("wageType");
                    int yearlySickDays = rs.getInt("yearlySickDays");
                    int yearlyDaysOff = rs.getInt("yearlyDaysOff");
                    String phoneNumber = rs.getString("phoneNumber");
                    List<RoleDTO> roles = getRolesForEmployee(id);
                    SiteDTO site = siteDAO.getSite(rs.getString("siteName"));
                        if (site == null) {
                            throw new SQLException("Site with name " + rs.getString("siteName") + " not found.");
                        }
                    // Check if the employee is a driver
                    List<String> licenseTypes = getLicenseTypesOfDriver(id);
                    if (licenseTypes != null) {
                        employees.add(new DriverDTO(id, password, fullName, workStartingDate, wage, wageType,
                                yearlySickDays, yearlyDaysOff, roles, site, phoneNumber, licenseTypes));
                        continue;
                    }
                    // If not a driver, return as a regular employee
                    employees.add(new EmployeeDTO(id, password, fullName, workStartingDate, wage, wageType,
                            yearlySickDays, yearlyDaysOff, roles, site, phoneNumber));
                }
                return employees;
            }
        }
    }

    public void instertEmployee(EmployeeDTO employee) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "INSERT INTO " + employeeTableName
                    + " (id, password, fullName, workStartingDate, wage, wageType, yearlySickDays, yearlyDaysOff, site_name, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employee.getId());
                statement.setString(2, employee.getPassword());
                statement.setString(3, employee.getFullName());
                statement.setLong(4,
                        employee.getWorkStartingDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
                statement.setInt(5, employee.getWage());
                statement.setString(6, employee.getWageType());
                statement.setInt(7, employee.getYearlySickDays());
                statement.setInt(8, employee.getYearlyDaysOff());
                statement.setString(9, employee.getSite().getName());
                statement.setString(10, employee.getPhoneNumber());
                statement.executeUpdate();
            }
        }
    }

    public void instertDriver(DriverDTO driver) throws SQLException {
        instertEmployee(driver);
        try (Connection driverConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "INSERT INTO " + DriversTable + " (employee_id, licenseType) VALUES (?, ?)";
            for (String licenseType : driver.getLicenseTypes()) {
                try (PreparedStatement statement = driverConn.prepareStatement(query)) {
                    statement.setString(1, driver.getId());
                    statement.setString(2, licenseType);
                    statement.executeUpdate();
                }
            }
        }
    }

    public void updateEmployee(EmployeeDTO employee) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
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
    }

    public void updateDriver(DriverDTO driver) throws SQLException {
        updateEmployee(driver);
        try (Connection driverConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String deleteQuery = "DELETE FROM " + DriversTable + " WHERE employee_id = ?";
            try (PreparedStatement deleteStatement = driverConn.prepareStatement(deleteQuery)) {
                deleteStatement.setString(1, driver.getId());
                deleteStatement.executeUpdate();
            }
            String insertQuery = "INSERT INTO " + DriversTable + " (employee_id, licenseType) VALUES (?, ?)";
            for (String licenseType : driver.getLicenseTypes()) {
                try (PreparedStatement insertStatement = driverConn.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, driver.getId());
                    insertStatement.setString(2, licenseType);
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    public void deleteEmployee(String id) throws SQLException {
        EmployeeDTO employee = getEmployeeById(id);
        if (employee != null) {
            try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
                String query = "DELETE FROM " + employeeTableName + " WHERE id = ?";
                try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                    statement.setString(1, id);
                    statement.executeUpdate();
                }
            }
        }
    }

    public void deleteDriver(String id) throws SQLException {
        try (Connection driverConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
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

    }

    public void assignRoleToEmployee(String employeeId, String roleName) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "INSERT INTO " + roleTableName + " (employee_id, role_roleName) VALUES (?, ?)";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                statement.setString(2, roleName);
                statement.executeUpdate();
            }
        }
    }

    public void removeRoleFromEmployee(String employeeId, String roleName) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "DELETE FROM " + roleTableName + " WHERE employee_id = ? AND role_roleName = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                statement.setString(2, roleName);
                statement.executeUpdate();
            }
        }
    }

    public List<String> getLicenseTypesOfDriver(String driverId) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT licenseType FROM " + DriversTable + " WHERE employee_id = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, driverId);
                try (ResultSet rs = statement.executeQuery()) {
                    List<String> licenseTypes = new java.util.ArrayList<>();
                    while (rs.next()) {
                        licenseTypes.add(rs.getString("licenseType"));
                    }
                    return licenseTypes.isEmpty() ? null : licenseTypes;
                }
            }
        }
    }
}
