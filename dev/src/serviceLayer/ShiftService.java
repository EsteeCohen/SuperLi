package src.serviceLayer;

import domainLayer.ShiftFacade;
import java.util.List;


public class ShiftService {
    private ShiftFacade shiftFacade;

    public void assignToShift(String employeeId, int shiftNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignToShift'");
    }

    // checks if there are any shifts where not enough employees are available
    public boolean checkForProblematicShifts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkShiftsWithMissingWorkers'");
    }

    public List<String> getWorkTimes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorkTimes'");
    }

}
