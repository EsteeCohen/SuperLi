package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.AvailabilityFacade;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.ShiftFacade;
import employeeDev.src.domainLayer.SiteFacade;
import employeeDev.src.serviceLayer.Interfaces.ITransportScheduleService;
import transportDev.src.main.entities.Site;

public class Factory {
    public EmployeeService employeeService;
    public ShiftService shiftService;
    public AssigningService assigningService;
    public RoleService roleService;

    public Factory() {
        // Initialize the facades
        ITransportScheduleService transportScheduleService = null; // Assuming this is initialized elsewhere any we can inject it later
        RoleFacade roleFacade = new RoleFacade();
        SiteFacade siteFacade = new SiteFacade();
        EmployeeFacade employeeFacade = new EmployeeFacade(roleFacade, siteFacade);
        ShiftFacade shiftFacade = new ShiftFacade(transportScheduleService, employeeFacade, siteFacade, roleFacade);
        AvailabilityFacade availabilityFacade = new AvailabilityFacade();
        
        // Initialize the services with the facades
        this.employeeService = new EmployeeService(employeeFacade);
        this.shiftService = new ShiftService(shiftFacade, availabilityFacade, employeeFacade, siteFacade);
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
