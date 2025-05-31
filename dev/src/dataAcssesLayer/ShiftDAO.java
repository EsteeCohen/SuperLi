package dataAcssesLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import dtos.EmployeeDTO;
import dtos.RoleDTO;
import dtos.ShiftDTO;

public class ShiftDAO {
    private final String DBPath = DBConstants.DB_PATH;
    private final String shiftTableName = DBConstants.EMPLOYEE_TABLE;
    private final String requirementsTableName = DBConstants.SHIFT_REQ_TABLE;
    private final String assignmentsTableName = DBConstants.ASSIGNMENT_TABLE;

     public void insertShift(ShiftDTO shift) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO " + shiftTableName + " (shiftType, startTime, endTime) VALUES (?, ?, ?)")) {
            stmt.setString(1, shift.getShiftType());
            stmt.setString(2, shift.getStartTime().toString());
            stmt.setString(3, shift.getEndTime().toString());
            stmt.executeUpdate();
        }
    }

    public ShiftDTO getShift(String shiftType, LocalDateTime startTime) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM " + shiftTableName + " WHERE shiftType = ? AND startTime = ?")) {
            stmt.setString(1, shiftType);
            stmt.setString(2, startTime.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDateTime endTime = LocalDateTime.parse(rs.getString("endTime"));
                // אפשר לטעון את המילונים מאוחר יותר אם יש לך טבלאות נפרדות לדרישות/שיבוצים
                return new ShiftDTO(shiftType, startTime, endTime,
                        new Hashtable<>(), new HashMap<>());
            }
        }
        return null;
    }

    public void deleteShift(String shiftType, LocalDateTime startTime) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM " + shiftTableName + " WHERE shiftType = ? AND startTime = ?")) {
            stmt.setString(1, shiftType);
            stmt.setString(2, startTime.toString());
            stmt.executeUpdate();
        }
    }

    public List<ShiftDTO> getAllShifts() throws SQLException {
        List<ShiftDTO> shifts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DBPath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + shiftTableName)) {

            while (rs.next()) {
                String type = rs.getString("shiftType");
                LocalDateTime start = LocalDateTime.parse(rs.getString("startTime"));
                LocalDateTime end = LocalDateTime.parse(rs.getString("endTime"));
                shifts.add(new ShiftDTO(type, start, end, new Hashtable<>(), new HashMap<>()));
            }
        }
        return shifts;
    }

    public Dictionary<RoleDTO, Integer> getRequirementsForShift(LocalDateTime startTime, String shiftType) {
        Dictionary<RoleDTO, Integer> requirements = new Hashtable<>();
        try (Connection conn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT role_name, required_count FROM " + requirementsTableName +
                           " WHERE shift_start_time = ? AND shift_type = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, startTime.toString());
            stmt.setString(2, shiftType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roleName = rs.getString("role_name");
                int count = rs.getInt("required_count");
                RoleDTO role = new RoleDTO(roleName);
                requirements.put(role, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requirements;
    }

    public HashMap<RoleDTO, List<EmployeeDTO>> getAssignmentsForShift(LocalDateTime startTime, String shiftType) {
        HashMap<RoleDTO, List<EmployeeDTO>> assignments = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT employee_id, role_name FROM " + assignmentsTableName +
                           " WHERE shift_start_time = ? AND shift_type = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, startTime.toString());
            stmt.setString(2, shiftType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roleName = rs.getString("role_name");
                String employeeId = rs.getString("employee_id");
                RoleDTO role = new RoleDTO(roleName);
                EmployeeDTO employee = new EmployeeDTO(employeeId);
                assignments.computeIfAbsent(role, k -> new ArrayList<>()).add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }
}
