package employeeDev.src.mappers;

import employeeDev.src.domainLayer.DriverDL;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.SiteFacade;
import employeeDev.src.dtos.DriverDTO;
import employeeDev.src.dtos.EmployeeDTO;
import employeeDev.src.dtos.RoleDTO;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

public class EmployeeMapper {

    public static EmployeeDL fromDTO(EmployeeDTO employeeDTO, RoleFacade roleFacade, SiteFacade siteFacade) {
        List<RoleDL> roles = new ArrayList<>();
        if (employeeDTO.getRoles() != null) {
            for (RoleDTO roleDTO : employeeDTO.getRoles()) {
                RoleDL role = roleFacade.getRoleByName(roleDTO.getName());
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        Site site = siteFacade.getSiteByName(employeeDTO.getSite().getName());
        if (site == null) {
            throw new IllegalArgumentException("Site with name " + employeeDTO.getSite().getName() + " not found.");
        }
        EmployeeDL employee = new EmployeeDL(employeeDTO.getId(),
                employeeDTO.getPassword(),
                employeeDTO.getFullName(),
                employeeDTO.getWorkStartingDate(),
                employeeDTO.getWage(),
                employeeDTO.getWageType().toUpperCase().charAt(0),
                employeeDTO.getYearlySickDays(),
                employeeDTO.getYearlyDaysOff(),
                site,
                employeeDTO.getPhoneNumber(),
                roles
        );
        return employee;
    }

    public static EmployeeDTO toDTO(EmployeeDL employee) {
        List<RoleDTO> roleDTOs = new ArrayList<>();
        for (RoleDL role : employee.getRoles()) {
            roleDTOs.add(new RoleDTO(role.getName()));
        }
        return new EmployeeDTO(
                employee.getId(),
                employee.getPassword(),
                employee.getFullName(),
                employee.getWorkStartingDate(),
                employee.getWage(),
                employee.getWageType().toString(),
                employee.getYearlySickDays(),
                employee.getYearlyDaysOff(),
                roleDTOs,
                SiteMapper.toDTO(employee.getSite()),
                employee.getPhoneNumber()
        );
    }

    public static DriverDL fromDriverDTO(DriverDTO driverDTO, RoleFacade roleFacade, SiteFacade siteFacade) {
        Site site = siteFacade.getSiteByName(driverDTO.getSite().getName());
        if (site == null) {
            throw new IllegalArgumentException("Site with name " + driverDTO.getSite().getName() + " not found.");
        }
        RoleDL driverRole = roleFacade.getRoleByName("Driver");
        if (driverRole == null) {
            throw new IllegalArgumentException("Role 'Driver' not found.");
        }
        LicenseType licenseType = LicenseType.fromString(driverDTO.getLicenseType());
        DriverDL driver = new DriverDL(
                driverDTO.getId(),
                driverDTO.getPassword(),
                driverDTO.getFullName(),
                driverDTO.getWorkStartingDate(),
                driverDTO.getWage(),
                driverDTO.getWageType().toUpperCase().charAt(0),
                driverDTO.getYearlySickDays(),
                driverDTO.getYearlyDaysOff(),
                site,
                driverDTO.getPhoneNumber(),
                licenseType,
                driverRole,
                driverDTO.isAvailableToDrive()
        );
        return driver;
    }

    public static DriverDTO toDriverDTO(DriverDL driver) {
        String licenseType = driver.getLicenseType().getTypeInString();
        List<RoleDTO> rolesDTOs = new ArrayList<>();
        for (RoleDL role : driver.getRoles()) {
            rolesDTOs.add(RoleMapper.toDTO(role));
        }
        return new DriverDTO(
                driver.getId(),
                driver.getPassword(),
                driver.getFullName(),
                driver.getWorkStartingDate(),
                driver.getWage(),
                driver.getWageType().toString(),
                driver.getYearlySickDays(),
                driver.getYearlyDaysOff(),
                rolesDTOs,
                SiteMapper.toDTO(driver.getSite()),
                driver.getPhoneNumber(),
                licenseType,
                driver.isAvailableToDrive()
        );
    }

}
