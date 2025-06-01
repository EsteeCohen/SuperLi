package dataAcssesLayer;

import dtos.AvilibilityDTO;
import dtos.EmployeeDTO;
import dtos.ShiftDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AvilibilityDAO {
    private final String dbPath;

    public AvilibilityDAO(String dbPath) {
        this.dbPath = dbPath;
    }

    public void saveAvailability(AvilibilityDTO availability) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "INSERT OR REPLACE INTO EmployeeAvailability (shift_start_time, shift_type, employee_id, is_available) " +
                         "VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, availability.getShift().getStartTime().toString());
            stmt.setString(2, availability.getShift().getShiftType());
            stmt.setString(3, availability.getEmployee().getId());
            stmt.setInt(4, availability.isAvailable() ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AvilibilityDTO> getAvailabilitiesForShift(ShiftDTO shift) {
        List<AvilibilityDTO> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT employee_id, is_available FROM EmployeeAvailability " +
                         "WHERE shift_start_time = ? AND shift_type = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, shift.getStartTime().toString());
            stmt.setString(2, shift.getShiftType());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String empId = rs.getString("employee_id");
                boolean isAvailable = rs.getInt("is_available") == 1;

                EmployeeDTO emp = new EmployeeDTO(empId);
                results.add(new AvilibilityDTO(shift, emp, isAvailable));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public AvilibilityDTO getAvailabilityForEmployee(EmployeeDTO employee, ShiftDTO shift) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT is_available FROM EmployeeAvailability " +
                         "WHERE shift_start_time = ? AND shift_type = ? AND employee_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, shift.getStartTime().toString());
            stmt.setString(2, shift.getShiftType());
            stmt.setString(3, employee.getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean isAvailable = rs.getInt("is_available") == 1;
                return new AvilibilityDTO(shift, employee, isAvailable);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AvilibilityDTO> getAllAvailabilities() {
        List<AvilibilityDTO> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String sql = "SELECT shift_start_time, shift_type, employee_id, is_available FROM EmployeeAvailability";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDateTime start = LocalDateTime.parse(rs.getString("shift_start_time"));
                String type = rs.getString("shift_type");
                String empId = rs.getString("employee_id");
                boolean isAvailable = rs.getInt("is_available") == 1;

                ShiftDTO shift = new ShiftDTO(type, start, null, null, null); // Complete as needed
                EmployeeDTO emp = new EmployeeDTO(empId);
                results.add(new AvilibilityDTO(shift, emp, isAvailable));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
