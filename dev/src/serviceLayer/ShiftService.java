package serviceLayer;

import domainLayer.AvailabilityDL;
import domainLayer.ShiftDL;
import domainLayer.ShiftFacade;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ShiftService {
    private ShiftFacade shiftFacade;
    private AvailabilityDL availabilityFacade;

    // checks if there are any shifts where not enough employees are available
    public boolean checkForProblematicShifts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkShiftsWithMissingWorkers'");
    }

    public List<ShiftSL> getAllShift() {
        List<ShiftDL> allDlShifts  = shiftFacade.getAllShifts();
        List<ShiftSL> allSlShifts = new ArrayList<>();
        for (ShiftDL dlShift : allDlShifts) {
            allSlShifts.add(new ShiftSL(dlShift));
        }
        return allSlShifts;
    }

    public void setAvailabilityOfEmployeetoShift(String employeeId, LocalDate date, String ShiftType, boolean available) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAvailabilityOfEmployee'");
    }

}
