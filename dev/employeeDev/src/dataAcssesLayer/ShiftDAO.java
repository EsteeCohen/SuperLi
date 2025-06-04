package employeeDev.src.dataAcssesLayer;

import employeeDev.src.dtos.EmployeeDTO;
import employeeDev.src.dtos.RoleDTO;
import employeeDev.src.dtos.ShiftDTO;
import employeeDev.src.dtos.SiteDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ShiftDAO {
    private final String DBPath = DBConstants.DB_PATH;
    private final String shiftTableName = DBConstants.EMPLOYEE_TABLE;
    private final String requirementsTableName = DBConstants.SHIFT_REQ_TABLE;
    private final String assignmentsTableName = DBConstants.ASSIGNMENT_TABLE;

    private final EmployeeDAO employeeDAO;
    private final SiteDAO siteDAO;

    public ShiftDAO() {
        this.employeeDAO = new EmployeeDAO();
        this.siteDAO = new SiteDAO();
    }

    public void insertShift(ShiftDTO shift) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO " + shiftTableName + " (shiftType, startingTime, endTime, site_name) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, shift.getShiftType());
            stmt.setLong(2, shift.getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setLong(3, shift.getEndTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(4, shift.getSite().getName());
            stmt.executeUpdate();
        }
    }

    public ShiftDTO getShift(LocalDateTime startingTime) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM " + shiftTableName + " WHERE startingTime = ?")) {
            stmt.setString(1, startingTime.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDateTime endTime = Instant.ofEpochSecond(rs.getLong("endTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
                String shiftType = rs.getString("shiftType");

                // need to fetch requirements and assignments
                Dictionary<RoleDTO, Integer> requirements = getRequirementsForShift(startingTime);
                HashMap<RoleDTO, List<EmployeeDTO>> assignments = getAssignmentsForShift(startingTime);
                SiteDTO site = siteDAO.getSite(rs.getString("siteName"));
                if (site == null) {
                    throw new SQLException("Site not found in DB");
                }
                return new ShiftDTO(shiftType, startingTime, endTime, requirements, assignments, site);
            }
        }
        return null;
    }

    public void deleteShift(String shiftType, LocalDateTime startingTime) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM " + shiftTableName + " WHERE startingTime = ?")) {
            stmt.setString(1, startingTime.toString());
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
                LocalDateTime start = Instant.ofEpochSecond(rs.getLong("startingTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime end = Instant.ofEpochSecond(rs.getLong("endTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                // Fetch requirements and assignments for the shift
                Dictionary<RoleDTO, Integer> requirements = getRequirementsForShift(start);
                HashMap<RoleDTO, List<EmployeeDTO>> assignments = getAssignmentsForShift(start);
                SiteDTO site = siteDAO.getSite(rs.getString("siteName"));
                if (site == null) {
                    throw new SQLException("Site not found in DB");
                }
                ShiftDTO shift = new ShiftDTO(type, start, end, requirements, assignments, null);
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public Dictionary<RoleDTO, Integer> getRequirementsForShift(LocalDateTime startingTime) {
        Dictionary<RoleDTO, Integer> requirements = new Hashtable<>();
        try (Connection conn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT role_roleName, quantity FROM " + requirementsTableName +
                           " WHERE shift_startingTime = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, startingTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roleName = rs.getString("role_roleName");
                int count = rs.getInt("quantity");
                RoleDTO role = new RoleDTO(roleName);
                requirements.put(role, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requirements;
    }

    public HashMap<RoleDTO, List<EmployeeDTO>> getAssignmentsForShift(LocalDateTime startingTime) {
        HashMap<RoleDTO, List<EmployeeDTO>> assignments = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT employee_id, role_roleName FROM " + assignmentsTableName +
                           " WHERE shift_start_time = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, startingTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roleName = rs.getString("role_roleName");
                String employeeId = rs.getString("employee_id");
                RoleDTO role = new RoleDTO(roleName);
                EmployeeDTO employee = employeeDAO.getEmployeeById(employeeId);
                if (employee == null) {
                    throw new SQLException("Employee with ID " + employeeId + " not found while getting shift assigments.");
                }
                assignments.computeIfAbsent(role, k -> new ArrayList<>()).add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }
}
