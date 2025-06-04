package employeeDev.src.dtos;

import java.time.LocalDate;
import java.util.List;

public class DriverDTO extends EmployeeDTO {
    private String licenseType;
    private boolean availableToDrive;

    public DriverDTO(String id, String password, String fullName, LocalDate workStartingDate, int wage,
                     String wageType, int yearlySickDays, int yearlyDaysOff, List<RoleDTO> roles, SiteDTO site, String phoneNumber,
                     String licenseType, boolean availableToDrive) {
        super(id, password, fullName, workStartingDate, wage, wageType, yearlySickDays, yearlyDaysOff, roles, site, phoneNumber);
        this.licenseType = licenseType;
        this.availableToDrive = availableToDrive;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public boolean isAvailableToDrive() {
        return availableToDrive;
    }

    public void setAvailableToDrive(boolean availableToDrive) {
        this.availableToDrive = availableToDrive;
    }

    @Override
    public boolean isDriver() {
        return true;
    }
}
