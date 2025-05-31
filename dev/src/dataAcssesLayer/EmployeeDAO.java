package dataAcssesLayer;

import dtos.EmployeeDTO;
import dtos.RoleDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeDAO {
    private final String employeeTableName = DBConstants.EMPLOYEE_TABLE;
    private final String DBPath = DBConstants.DB_PATH;
    private final String roleTableName = DBConstants.ROLE_TABLE;

    public EmployeeDTO getEmployeeById(String id) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + employeeTableName + " WHERE id = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String password = rs.getString("password");
                        String fullName = rs.getString("fullName");
                        LocalDate workStartingDate = LocalDate.parse(rs.getString("workStartingDate"));
                        int wage = rs.getInt("wage");
                        String wageType = rs.getString("wageType");
                        int yearlySickDays = rs.getInt("yearlySickDays");
                        int yearlyDaysOff = rs.getInt("yearlyDaysOff");
                        List<RoleDTO> roles = getRolesForEmployee(id);
                        return new EmployeeDTO(id, password, fullName, workStartingDate, wage,
                                wageType, yearlySickDays, yearlyDaysOff, roles);
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
            String query = "SELECT * FROM " + roleTableName + " WHERE employeeId = ?";
            try (PreparedStatement statement = roleConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String roleName = rs.getString("roleName");
                        roles.add(new RoleDTO(roleName));
                    }
                }
            }
        }
        return roles;
    }

    public List<EmployeeDTO> getAllEmployees() throws SQLException{
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + employeeTableName;
            try (PreparedStatement statement = employeeConn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                List<EmployeeDTO> employees = new java.util.ArrayList<>();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String password = rs.getString("password");
                    String fullName = rs.getString("fullName");
                    LocalDate workStartingDate = LocalDate.parse(rs.getString("workStartingDate"));
                    int wage = rs.getInt("wage");
                    String wageType = rs.getString("wageType");
                    int yearlySickDays = rs.getInt("yearlySickDays");
                    int yearlyDaysOff = rs.getInt("yearlyDaysOff");
                    List<RoleDTO> roles = getRolesForEmployee(id);
                    employees.add(new EmployeeDTO(id, password, fullName, workStartingDate, wage,
                            wageType, yearlySickDays, yearlyDaysOff, roles));
                }
                return employees;
            }
        }
    }

    public void instertEmployee(EmployeeDTO employee) throws SQLException{
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "INSERT INTO " + employeeTableName + " (id, password, fullName, workStartingDate, wage, wageType, yearlySickDays, yearlyDaysOff) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employee.getId());
                statement.setString(2, employee.getPassword());
                statement.setString(3, employee.getFullName());
                statement.setString(4, employee.getWorkStartingDate().toString());
                statement.setInt(5, employee.getWage());
                statement.setString(6, employee.getWageType());
                statement.setInt(7, employee.getYearlySickDays());
                statement.setInt(8, employee.getYearlyDaysOff());
                statement.executeUpdate();
            }
        }
    }

    public void updateEmployee(EmployeeDTO employee) throws SQLException{
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "UPDATE " + employeeTableName + " SET password = ?, fullName = ?, workStartingDate = ?, wage = ?, wageType = ?, yearlySickDays = ?, yearlyDaysOff = ? WHERE id = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employee.getPassword());
                statement.setString(2, employee.getFullName());
                statement.setString(3, employee.getWorkStartingDate().toString());
                statement.setInt(4, employee.getWage());
                statement.setString(5, employee.getWageType());
                statement.setInt(6, employee.getYearlySickDays());
                statement.setInt(7, employee.getYearlyDaysOff());
                statement.setString(8, employee.getId());
                statement.executeUpdate();
            }
        }
    }

    public EmployeeDTO deleteEmployee(String id) throws SQLException{
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
        return employee;
    }

    public void assignRoleToEmployee(String employeeId, String roleId) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "INSERT INTO EmployeeRoles (employeeId, roleId) VALUES (?, ?)";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                statement.setString(2, roleId);
                statement.executeUpdate();
            }
        }
    }

    public void removeRoleFromEmployee(String employeeId, String roleId) throws SQLException {
        try (Connection employeeConn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "DELETE FROM EmployeeRoles WHERE employeeId = ? AND roleId = ?";
            try (PreparedStatement statement = employeeConn.prepareStatement(query)) {
                statement.setString(1, employeeId);
                statement.setString(2, roleId);
                statement.executeUpdate();
            }
        }
    }
}
