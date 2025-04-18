package serviceLayer;

import domainLayer.ShiftFacade;
import java.time.LocalDate;
import java.util.List;


public class ShiftService {
    private ShiftFacade shiftFacade;

    public void assignToShift(String employeeId, LocalDate date, String shiftType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignToShift'");
    }

    // checks if there are any shifts where not enough employees are available
    public boolean checkForProblematicShifts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkShiftsWithMissingWorkers'");
    }

    public List<ShiftSL> getAllShift() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorkTimes'");
    }

    public void setAvailabilityOfEmployeetoShift(String employeeId, LocalDate date, String ShiftType, boolean available) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAvailabilityOfEmployee'");
    }

}
