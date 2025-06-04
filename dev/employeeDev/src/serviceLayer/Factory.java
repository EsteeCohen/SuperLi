package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.AvailabilityFacade;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.ShiftFacade;
import employeeDev.src.domainLayer.SiteFacade;
import employeeDev.src.serviceLayer.Interfaces.DriverInfoInterface;
import employeeDev.src.serviceLayer.Interfaces.ITransportScheduleService;
import employeeDev.src.serviceLayer.Interfaces.SiteInfoInterface;
import employeeDev.src.serviceLayer.Interfaces.UserManagmentInteface;



public class Factory {
    public EmployeeService employeeService;
    public ShiftService shiftService;
    public AssigningService assigningService;
    public RoleService roleService;
    public SiteService siteService;

    public Factory() {
        // Initialize the facades
        RoleFacade roleFacade = new RoleFacade();
        SiteFacade siteFacade = new SiteFacade();
        EmployeeFacade employeeFacade = new EmployeeFacade(roleFacade, siteFacade);
        ShiftFacade shiftFacade = new ShiftFacade(employeeFacade, siteFacade, roleFacade);
        AvailabilityFacade availabilityFacade = new AvailabilityFacade(shiftFacade, employeeFacade, siteFacade);

        // load the data from the database
        roleFacade.loadRolesFromDB();
        siteFacade.loadSitesFromDB();
        employeeFacade.loadEmployeesFromDB();
        shiftFacade.loadShiftsFromDB();
        availabilityFacade.LoadAvailabilitiesFromDB();
        
        // Initialize the services with the facades
        this.employeeService = new EmployeeService(employeeFacade);
        this.shiftService = new ShiftService(shiftFacade, availabilityFacade, employeeFacade, roleFacade);
        this.assigningService = new AssigningService(shiftFacade, employeeFacade, roleFacade);
        this.roleService = new RoleService(roleFacade);
        this.siteService = new SiteService(siteFacade);

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

    public SiteService getSiteService() {
        return siteService;
    }

    public DriverInfoInterface getDriverInfoService() {
        return shiftService;
    }

    public SiteInfoInterface getSiteInfoService() {
        return siteService;
    }

    public UserManagmentInteface getUserManagmentInteface(){
        return employeeService;
    }

    public void setTransportScheduleService(ITransportScheduleService transportInterface) {
        assigningService.setTransportScheduleService(transportInterface);
    }
    
}
