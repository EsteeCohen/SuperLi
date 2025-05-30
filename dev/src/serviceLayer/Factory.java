package serviceLayer;

import domainLayer.AvailabilityFacade;
import domainLayer.EmployeeFacade;
import domainLayer.RoleFacade;
import domainLayer.ShiftFacade;
import serviceLayer.Interfaces.ITransportScheduleService;

public class Factory {
    public EmployeeService employeeService;
    public ShiftService shiftService;
    public AssigningService assigningService;
    public RoleService roleService;

    public Factory() {
        // Initialize the facades
        ITransportScheduleService transportScheduleService = null; // Assuming this is initialized elsewhere any we can inject it later
        RoleFacade roleFacade = new RoleFacade();
        EmployeeFacade employeeFacade = new EmployeeFacade(roleFacade);
        ShiftFacade shiftFacade = new ShiftFacade(transportScheduleService);
        AvailabilityFacade availabilityFacade = new AvailabilityFacade();
        
        
        // Initialize the services with the facades
        this.employeeService = new EmployeeService(employeeFacade);
        this.shiftService = new ShiftService(shiftFacade, availabilityFacade, employeeFacade);
        this.assigningService = new AssigningService(shiftFacade, employeeFacade, roleFacade);
        this.roleService = new RoleService(roleFacade);
    }

    public ShiftService getShiftService() {
        return shiftService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public AssigningService getAssigningService() {
        return assigningService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    
}
