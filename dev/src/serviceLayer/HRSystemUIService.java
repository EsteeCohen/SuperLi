package src.serviceLayer;

import java.util.List;

public class HRSystemUIService {
    private static HRSystemUIService instance = null;
    private HRSystemUIService() {
        // Private constructor to prevent instantiation
    }

    public static HRSystemUIService getInstance() {
        if (instance == null) {
            instance = new HRSystemUIService();
        }
        return instance;
    }

    public void registerEmployee(String name, String id, double salary, String wageType, int yearlySickDays,
            int yearlyDaysOff) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerEmployee'");
    }


    public void setAvailability(String employeeId, String selectedTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAvailability'");
    }

    public void printEmployeeDetails(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'printEmployeeDetails'");
    }

}
