package employeeDev.src.mappers;

import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.ShiftDL;
import employeeDev.src.domainLayer.SiteFacade;
import employeeDev.src.dtos.EmployeeDTO;
import employeeDev.src.dtos.RoleDTO;
import employeeDev.src.dtos.ShiftDTO;
import employeeDev.src.dtos.SiteDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;

public class ShiftMapper {

    public static ShiftDL fromDTO(ShiftDTO shiftDTO, SiteFacade siteFacade, RoleFacade roleFacade, EmployeeFacade employeeFacade) {
        Site site = siteFacade.getSiteByName(shiftDTO.getSite().getName());
        if (site == null) {
            throw new IllegalArgumentException("Site not found during loading of shifts: " + shiftDTO.getSite().getName());
        }
        Map<RoleDL, Integer> requirements = convertFromDTORequirements(shiftDTO.getRequirements(), roleFacade);
        Map<RoleDL, List<EmployeeDL>> assignments = convertFromDTOAssignments(shiftDTO.getEmployeesAssignment(), roleFacade, employeeFacade);
        ShiftDL shift = new ShiftDL(
            shiftDTO.getStartTime(),
            shiftDTO.getEndTime(),
            shiftDTO.getShiftType(),
            site,
            requirements,
            assignments
        );
        return shift;
    }

    private static Map<RoleDL, Integer> convertFromDTORequirements(Map<RoleDTO, Integer> requirementsDTO, RoleFacade roleFacade) {
        Map<RoleDL, Integer> requirements = new HashMap<>();
        for (Map.Entry<RoleDTO, Integer> entry : requirementsDTO.entrySet()) {
            RoleDL role = roleFacade.getRoleByName(entry.getKey().getName());
            if (role == null) {
                throw new IllegalArgumentException("Role not found during loading of shift req: " + entry.getKey().getName());
            }
            requirements.put(role, entry.getValue());
        }
        return requirements;
    }

    private static Map<RoleDL, List<EmployeeDL>> convertFromDTOAssignments(Map<RoleDTO, List<EmployeeDTO>> assignmentsDTO, RoleFacade roleFacade, EmployeeFacade employeeFacade) {
        Map<RoleDL, List<EmployeeDL>> assignments = new HashMap<>();
        for (Map.Entry<RoleDTO, List<EmployeeDTO>> entry : assignmentsDTO.entrySet()) {
            RoleDL role = roleFacade.getRoleByName(entry.getKey().getName());
            if (role == null) {
                throw new IllegalArgumentException("Role not found during loading of shift assignments: " + entry.getKey().getName());
            }
            List<EmployeeDL> employees = new ArrayList<>();
            for (EmployeeDTO employeeDTO : entry.getValue()) {
                EmployeeDL employee = employeeFacade.getEmployee(employeeDTO.getId());
                if (employee != null) {
                    employees.add(employee);
                }
                else {
                    throw new IllegalArgumentException("Employee not found during loading of shift assignments: " + employeeDTO.getId());
                }
            }
            assignments.put(role, employees);
        }
        return assignments;
    }

    public static ShiftDTO toDTO(ShiftDL shift) {
        Map<RoleDTO, Integer> requirementsDTO = convertToDTORequirements(shift.getRequirements());
        Map<RoleDTO, List<EmployeeDTO>> assignmentsDTO = convertToDTOAssignments(shift.getEmployeesAssignment());
        SiteDTO siteDTO = SiteMapper.toDTO(shift.getSite());
        ShiftDTO shiftDTO = new ShiftDTO(
            shift.getShiftType().toString(),
            shift.getStartTime(),
            shift.getEndTime(),
            requirementsDTO,
            assignmentsDTO,
            siteDTO
        );
        return shiftDTO;
    }

    private static Map<RoleDTO, Integer> convertToDTORequirements(Map<RoleDL, Integer> requirements) {
        Map<RoleDTO, Integer> requirementsDTO = new HashMap<>();
        for (Map.Entry<RoleDL, Integer> entry : requirements.entrySet()) {
            RoleDTO roleDTO = RoleMapper.toDTO(entry.getKey());
            requirementsDTO.put(roleDTO, entry.getValue());
        }
        return requirementsDTO;
    }

    private static Map<RoleDTO, List<EmployeeDTO>> convertToDTOAssignments(Map<RoleDL, List<EmployeeDL>> assignments) {
        Map<RoleDTO, List<EmployeeDTO>> assignmentsDTO = new HashMap<>();
        for (Map.Entry<RoleDL, List<EmployeeDL>> entry : assignments.entrySet()) {
            RoleDTO roleDTO = RoleMapper.toDTO(entry.getKey());
            List<EmployeeDTO> employeeDTOs = new ArrayList<>();
            for (EmployeeDL employee : entry.getValue()) {
                employeeDTOs.add(EmployeeMapper.toDTO(employee));
            }
            assignmentsDTO.put(roleDTO, employeeDTOs);
        }
        return assignmentsDTO;
    }
}
