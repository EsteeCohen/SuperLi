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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void insertShift(ShiftDTO shift) {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO " + shiftTableName + " (shiftType, startingTime, endTime, site_name) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, shift.getShiftType());
            stmt.setLong(2, shift.getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setLong(3, shift.getEndTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(4, shift.getSite().getName());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ShiftDTO getShift(LocalDateTime startingTime, String siteName) {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM " + shiftTableName + " WHERE startingTime = ? AND site_name = ?")) {
            stmt.setLong(1, startingTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, siteName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDateTime endTime = Instant.ofEpochSecond(rs.getLong("endTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
                String shiftType = rs.getString("shiftType");

                // need to fetch requirements and assignments
                SiteDTO site = siteDAO.getSite(siteName);
                if (site == null) {
                    throw new SQLException("Site not found in DB");
                }
                Map<RoleDTO, Integer> requirements = getRequirementsForShift(startingTime, site.getName());
                if (requirements == null) {
                    throw new SQLException("Requirements not found for shift at " + startingTime.toString() + " at site " + site.getName());
                }
                Map<RoleDTO, List<EmployeeDTO>> assignments = getAssignmentsForShift(startingTime, site.getName());
                if (assignments == null) {
                    throw new SQLException("Assignments not found for shift at " + startingTime.toString() + " at site " + site.getName());
                }
                return new ShiftDTO(shiftType, startingTime, endTime, requirements, assignments, site);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteShift(String shiftType, LocalDateTime startingTime, String siteName) {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM " + shiftTableName + " WHERE startingTime = ? AND site_name = ?")) {
            stmt.setString(1, startingTime.toString());
            stmt.setString(2, siteName);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ShiftDTO> getAllShifts() {
        List<ShiftDTO> shifts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DBPath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + shiftTableName)) {

            while (rs.next()) {
                String type = rs.getString("shiftType");
                LocalDateTime start = Instant.ofEpochSecond(rs.getLong("startingTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime end = Instant.ofEpochSecond(rs.getLong("endTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                // Fetch requirements and assignments for the shift
                SiteDTO site = siteDAO.getSite(rs.getString("siteName"));
                if (site == null) {
                    throw new SQLException("Site not found in DB");
                }
                Map<RoleDTO, Integer> requirements = getRequirementsForShift(start, site.getName());
                if (requirements == null) {
                    throw new SQLException("Requirements not found for shift at " + start.toString() + " at site " + site.getName());
                }
                Map<RoleDTO, List<EmployeeDTO>> assignments = getAssignmentsForShift(start, site.getName());
                if (assignments == null) {
                    throw new SQLException("Assignments not found for shift at " + start.toString() + " at site " + site.getName());
                }

                ShiftDTO shift = new ShiftDTO(type, start, end, requirements, assignments, null);
                shifts.add(shift);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return shifts;
    }

    public Map<RoleDTO, Integer> getRequirementsForShift(LocalDateTime startingTime, String siteName) {
        Map<RoleDTO, Integer> requirements = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT role_roleName, quantity FROM " + requirementsTableName +
                           " WHERE shift_startingTime = ? AND shift_siteName = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, startingTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, siteName);
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

    public Map<RoleDTO, List<EmployeeDTO>> getAssignmentsForShift(LocalDateTime startingTime, String siteName) {
        HashMap<RoleDTO, List<EmployeeDTO>> assignments = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DBPath)) {
            String query = "SELECT employee_id, role_roleName FROM " + assignmentsTableName +
                           " WHERE shift_start_time = ? AND shift_siteName = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, startingTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, siteName);
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

    public void insertRequirementForShift(ShiftDTO shift, RoleDTO role, int quantity) {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO " + requirementsTableName + " (shift_startingTime, role_roleName, quantity) VALUES (?, ?, ?)")) {
            stmt.setLong(1, shift.getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, role.getName());
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRequirementForShift(LocalDateTime startingTime, String siteName, RoleDTO role) {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM " + requirementsTableName + " WHERE shift_startingTime = ? AND shift_siteName = ? AND role_roleName = ?")) {
            stmt.setLong(1, startingTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, siteName);
            stmt.setString(3, role.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAssignmentForShift(ShiftDTO shift, RoleDTO role, EmployeeDTO employee) {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO " + assignmentsTableName + " (shift_start_time, shift_siteName, role_roleName, employee_id) VALUES (?, ?, ?, ?)")) {
            stmt.setLong(1, shift.getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, shift.getSite().getName());
            stmt.setString(3, role.getName());
            stmt.setString(4, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAssignmentForShift(LocalDateTime startingTime, String siteName, RoleDTO role, EmployeeDTO employee) {
        try (Connection conn = DriverManager.getConnection(DBPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM " + assignmentsTableName + " WHERE shift_start_time = ? AND shift_siteName = ? AND role_roleName = ? AND employee_id = ?")) {
            stmt.setLong(1, startingTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            stmt.setString(2, siteName);
            stmt.setString(3, role.getName());
            stmt.setString(4, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
