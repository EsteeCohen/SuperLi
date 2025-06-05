package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.ShiftDAO;
import employeeDev.src.domainLayer.Enums.ShiftType;
import employeeDev.src.dtos.ShiftDTO;
import employeeDev.src.mappers.EmployeeMapper;
import employeeDev.src.mappers.RoleMapper;
import employeeDev.src.mappers.ShiftMapper;
import employeeDev.src.serviceLayer.Interfaces.ITransportScheduleService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;

public class ShiftFacade {
    private final Map<String, ShiftDL> shifts;
    private final EmployeeFacade employeeFacade;
    private final RoleFacade roleFacade;
    private final SiteFacade siteFacade;

    public ShiftFacade(EmployeeFacade employeeFacade, SiteFacade siteFacade, RoleFacade roleFacade) {
        this.shifts = new HashMap<>();
        this.employeeFacade = employeeFacade;
        this.siteFacade = siteFacade;
        this.roleFacade = roleFacade;
    }

    public List<ShiftDL> getAllShiftsFromSite(Site site) {
        if (site == null) {
            throw new IllegalArgumentException("Site cannot be null");
        }
        List<ShiftDL> shiftList = new ArrayList<>();
        for (ShiftDL shift : shifts.values()) {
            if (shift.getSite() != null && shift.getSite().equals(site)) {
                shiftList.add(shift);
            }
        }
        return shiftList;
    }

    public void assignToShift(EmployeeDL employee, LocalDateTime startTime, Site site, RoleDL role) {
        ShiftDL shift = shifts.get(getShiftKey(startTime, site.getName()));
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and site");
        }
        shift.assignEmployee(role, employee);
        ShiftDAO shiftDAO = new ShiftDAO();
        shiftDAO.insertAssignmentForShift(ShiftMapper.toDTO(shift), RoleMapper.toDTO(role), EmployeeMapper.toDTO(employee));
    }

    public void unassignToShift(EmployeeDL employee, LocalDateTime startTime, Site site,  RoleDL role) {
        ShiftDL shift = shifts.get(getShiftKey(startTime, site.getName()));
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and site");
        }
        shift.unassignEmployee(role, employee);
        ShiftDAO shiftDAO = new ShiftDAO();
        shiftDAO.deleteAssignmentForShift(startTime, shift.getSite().getName(), RoleMapper.toDTO(role), EmployeeMapper.toDTO(employee));
    }

    public ShiftDL getShiftByStartTimeAndSite(LocalDateTime startTime, Site site) {
        return shifts.get(getShiftKey(startTime, site.getName()));
    }

    public void addShift(ShiftDL shift) {
        shifts.put(getShiftKey(shift.getStartTime(), shift.getSite().getName()), shift);
        ShiftDAO shiftDAO = new ShiftDAO();
        shiftDAO.insertShift(ShiftMapper.toDTO(shift));
    }

    public void setWeeklyRequirements(DayOfWeek day, ShiftType shift, RoleDL role, int quantity) {
        WeeklyShiftRequirements.getInstance().setRequirements(day, shift, role, quantity);
    }

    public void setRequirementForShift(ShiftDL shift, RoleDL role, int quantity) {
        if (shift == null || role == null) {
            throw new IllegalArgumentException("Shift and role cannot be null");
        }
        shift.setIntoRequirements(role, quantity);
        shift.presistIntoRequirements();
    }

    public boolean checkIfThereAreShiftsThatAreNotFullyAssigned(Site site, LocalDate startDate, LocalDate endDate) {
        if (site == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Site and dates cannot be null");
        }
        List<ShiftDL> shiftList = getAllShiftsInRange(startDate, endDate, site);
        for (ShiftDL shift : shiftList) {
            if (!shift.meetTheRequirements()) {
                return true; // Found a shift that does not meet the requirements
            }
        }
        return false; // All shifts have at least one assigned employee
    }

    public List<ShiftDL> getAllShiftsInRange(LocalDate startDate, LocalDate endDate, Site site) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        List<ShiftDL> shiftList = new ArrayList<>();
        for (ShiftDL shift : shifts.values()) {
            LocalDate shiftDate = shift.getStartTime().toLocalDate();
            if (!shiftDate.isBefore(startDate) && !shiftDate.isAfter(endDate) && shift.getSite().equals(site)) {
                shiftList.add(shift);
            }
        }
        return shiftList;
    }

    public List<ShiftDL> getWeeklyShifts(LocalDate startDay, Site site) {
        List<ShiftDL> weeklyShifts = new ArrayList<>();
        LocalDate endDay = startDay.plusDays(6);

        for (ShiftDL shift : shifts.values()) {
            LocalDate shiftDate = shift.getStartTime().toLocalDate();
            if (!shiftDate.isBefore(startDay) && !shiftDate.isAfter(endDay) && shift.getSite().equals(site)) {
                weeklyShifts.add(shift);
            }
        }

        return weeklyShifts;
    }

    // Helper method to generate the key for the shifts map
    private String getShiftKey(LocalDateTime startTime, String siteName) {
        return startTime.toString() + siteName;
    }

    // Get the shift at a specific time
    public ShiftDL getShiftAtTime(LocalDateTime time, Site site) {
        for (ShiftDL shift : shifts.values()) {
            if ((shift.getStartTime().isEqual(time) || (time.isAfter(shift.getStartTime()) && time.isBefore(shift.getEndTime()))) && shift.getSite().equals(site)) {
                return shift;
            }
        }
        return null; // No shift found at the specified time
    }

    // makes sure that every shift where a delivery is expected to arrive has at least one warehouseman required
    public void intergrateShiftToDeliveries(Site site, RoleDL warehousemanRole, ITransportScheduleService transportInterface) {
        for (ShiftDL shift : shifts.values()) {
            if (transportInterface.areThereArrivalsAtTheShift(shift.getStartTime(), shift.getEndTime(), site) && shift.getRequiredEmployeeCount(warehousemanRole) < 1) {
                setRequirementForShift(shift, warehousemanRole, 1);
            }
        }
    }

    public List<EmployeeDL> getAllUnassignedEmployeesForShift(LocalDateTime startTime, Site site) {
        List<EmployeeDL> unassigned = new ArrayList<>();
        ShiftDL targetShift = null;
        for (ShiftDL shift : shifts.values()) {
            if (shift.getStartTime().isEqual(startTime) && shift.getSite().equals(site)) {
                targetShift = shift;
                break;
            }
        }
        if (targetShift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and type");
        }
        for (EmployeeDL employee : employeeFacade.getAllEmployees()) {
            if (!targetShift.isEmployeeAssigned(employee)) {
                unassigned.add(employee);
            }
        }
        return unassigned;
    }

    // Load shifts from the database and add them to the internal map
    public void loadShiftsFromDB() {
        ShiftDAO shiftDAO = new ShiftDAO();
        List<ShiftDTO> shiftList = shiftDAO.getAllShifts();
        for (ShiftDTO shiftDTO : shiftList) {
            ShiftDL shift = ShiftMapper.fromDTO(shiftDTO, siteFacade, roleFacade, employeeFacade);
            if (shift != null) {
                shifts.put(getShiftKey(shift.getStartTime(), shift.getSite().getName()), shift);
            }
        }
    }

    public List<ShiftDL> getAllShiftsWithMissingAssigns(Site site, LocalDate startDate, LocalDate endDate) {
        if (site == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Site and dates cannot be null");
        }
        List<ShiftDL> shiftList = getAllShiftsInRange(startDate, endDate, site);
        List<ShiftDL> shiftsWithMissingAssigns = new ArrayList<>();
        for (ShiftDL shift : shiftList) {
            if (!shift.meetTheRequirements()) {
                shiftsWithMissingAssigns.add(shift);
            }
        }
        return shiftsWithMissingAssigns;
    }

}
