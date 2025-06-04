package employeeDev.src.dtos;

import employeeDev.src.domainLayer.ShiftDL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ShiftDTO {
    private String shiftType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Map<RoleDTO, Integer> requirements;
    private Map<RoleDTO, List<EmployeeDTO>> employeesAssignment;
    private SiteDTO site;

    public ShiftDTO(String shiftType, LocalDateTime startTime, LocalDateTime endTime,
                    Map<RoleDTO, Integer> requirements,
                    Map<RoleDTO, List<EmployeeDTO>> employeesAssignment, SiteDTO site) {
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requirements = requirements;
        this.employeesAssignment = employeesAssignment;
        this.site = site;
    }
     
    public ShiftDTO(ShiftDL shift, SiteDTO site, Map<RoleDTO, Integer> requirements,
                    Map<RoleDTO, List<EmployeeDTO>> employeesAssignment) {
        this.shiftType = shift.getShiftType().toString();
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.requirements = requirements;
        this.employeesAssignment = employeesAssignment;
        this.site = site;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Map<RoleDTO, Integer> getRequirements() {
        return requirements;
    }

    public void setRequirements(Map<RoleDTO, Integer> requirements) {
        this.requirements = requirements;
    }

    public Map<RoleDTO, List<EmployeeDTO>> getEmployeesAssignment() {
        return employeesAssignment;
    }

    public void setEmployeesAssignment(Map<RoleDTO, List<EmployeeDTO>> employeesAssignment) {
        this.employeesAssignment = employeesAssignment;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    
}
