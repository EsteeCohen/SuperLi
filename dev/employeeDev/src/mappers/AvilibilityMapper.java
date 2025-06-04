package employeeDev.src.mappers;

import employeeDev.src.domainLayer.AvailabilityDL;
import employeeDev.src.domainLayer.DriverDL;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.ShiftDL;
import employeeDev.src.domainLayer.ShiftFacade;
import employeeDev.src.domainLayer.SiteFacade;
import employeeDev.src.dtos.AvilibilityDTO;
import employeeDev.src.dtos.EmployeeDTO;
import employeeDev.src.dtos.ShiftDTO;


public class AvilibilityMapper {

    public static AvilibilityDTO toDTO(AvailabilityDL availabilityDL) {
        ShiftDTO shiftDTO = ShiftMapper.toDTO(availabilityDL.getShift());
        EmployeeDTO employeeDTO;
        if (availabilityDL.getEmployee().isDriver()) {
            DriverDL driver = (DriverDL) availabilityDL.getEmployee();
            employeeDTO = EmployeeMapper.toDriverDTO(driver);
        } else {
            employeeDTO = EmployeeMapper.toDTO(availabilityDL.getEmployee());
        }
        return new AvilibilityDTO(shiftDTO, employeeDTO, availabilityDL.isAvailable());
    }

    public static AvailabilityDL fromDTO(AvilibilityDTO availabilityDTO, ShiftFacade shiftFacade, SiteFacade siteFacade, EmployeeFacade employeeFacade) {
        ShiftDL shiftDL = shiftFacade.getShiftByStartTimeAndSite(availabilityDTO.getShift().getStartTime(),
                        siteFacade.getSiteByName(availabilityDTO.getShift().getSite().getName()));
        EmployeeDL employeeDL = employeeFacade.getEmployee(availabilityDTO.getEmployee().getId());
        return new AvailabilityDL(
                employeeDL,
                shiftDL,
                availabilityDTO.isAvailable()
        );
    }

}
