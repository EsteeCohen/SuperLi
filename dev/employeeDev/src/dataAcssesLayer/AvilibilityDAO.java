package employeeDev.src.dataAcssesLayer;

import employeeDev.src.dtos.AvilibilityDTO;
import employeeDev.src.dtos.EmployeeDTO;
import employeeDev.src.dtos.ShiftDTO;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AvilibilityDAO {
    private final String dbPath = "jdbc:sqlite:" + DBConstants.DB_PATH; 
    private final String TABLE_NAME = DBConstants.AVILIBILITY_TABLE;

    private final EmployeeDAO employeeDAO;
    private final ShiftDAO shiftDAO;

    public AvilibilityDAO() {
        employeeDAO = new EmployeeDAO();
        shiftDAO = new ShiftDAO();
    }

    public void saveAvailability(AvilibilityDTO availability) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "INSERT OR REPLACE INTO " +  TABLE_NAME  + " (shift_startingTime, employee_id, IsAvailable, shift_siteName) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, availability.getShift().getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, availability.getEmployee().getId());
            stmt.setInt(3, availability.isAvailable() ? 1 : 0);
            stmt.setString(4, availability.getShift().getSite().getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving availability for employee " + availability.getEmployee().getId() + " for shift starting at " + availability.getShift().getStartTime().toString() + ": " + e.getMessage());
            throw new RuntimeException("Error saving availability for employee " + availability.getEmployee().getId() + " for shift starting at " + availability.getShift().getStartTime().toString(), e);
        }
    }

    public List<AvilibilityDTO> getAvailabilitiesForShift(ShiftDTO shift) {
        List<AvilibilityDTO> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT employee_id, IsAvailable FROM " + TABLE_NAME  +
                         " WHERE shift_startingTime = ? AND shift_siteName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, shift.getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, shift.getSite().getName());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String empId = rs.getString("employee_id");
                boolean isAvailable = rs.getInt("IsAvailable") == 1;

                EmployeeDTO emp = employeeDAO.getEmployeeById(empId);
                if (emp == null) {
                    throw new SQLException("Employee with ID " + empId + " not found while pulling avilibilities for a shift at " + shift.getStartTime().toString() + ".");
                }
                results.add(new AvilibilityDTO(shift, emp, isAvailable));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving availabilities for shift starting at " + shift.getStartTime().toString() + ": " + e.getMessage());
            throw new RuntimeException("Error retrieving availabilities for shift starting at " + shift.getStartTime().toString(), e);
        }
        return results;
    }

    public AvilibilityDTO getAvailabilityForEmployeeAndShift(EmployeeDTO employee, ShiftDTO shift) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT IsAvailable FROM " + TABLE_NAME +
                         " WHERE shift_startingTime = ? AND employee_id = ? AND shift_siteName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, shift.getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, employee.getId());
            stmt.setString(3, shift.getSite().getName());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean isAvailable = rs.getInt("IsAvailable") == 1;
                return new AvilibilityDTO(shift, employee, isAvailable);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving availability for employee " + employee.getId() + " for shift starting at " + shift.getStartTime().toString() + ": " + e.getMessage());
            throw new RuntimeException("Error retrieving availability for employee " + employee.getId() + " for shift starting at " + shift.getStartTime().toString(), e);
        }
        return null;
    }

    public List<AvilibilityDTO> getAllAvailabilities() {
        List<AvilibilityDTO> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDateTime start = Instant.ofEpochSecond(rs.getLong("shift_startingTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
                String empId = rs.getString("employee_id");
                boolean isAvailable = rs.getInt("IsAvailable") == 1;
                String siteName = rs.getString("shift_siteName");
                
                ShiftDTO shift = shiftDAO.getShift(start, siteName);
                if (shift == null) {
                    throw new SQLException("Shift starting at " + start.toString() + " not found while pulling all avilibilities.");
                }
                EmployeeDTO emp = employeeDAO.getEmployeeById(empId);
                if (emp == null) {
                    throw new SQLException("Employee with ID " + empId + " not found while pulling all avilibilities.");
                }
                
                results.add(new AvilibilityDTO(shift, emp, isAvailable));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all availabilities: " + e.getMessage());
            throw new RuntimeException("Error retrieving all availabilities", e);
        }
        return results;
    }
}
