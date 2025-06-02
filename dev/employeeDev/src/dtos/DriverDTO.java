package employeeDev.src.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DriverDTO extends EmployeeDTO {
    private List<String> licenseTypes;

    public DriverDTO(String id, String password, String fullName, LocalDate workStartingDate, int wage,
                     String wageType, int yearlySickDays, int yearlyDaysOff, List<RoleDTO> roles, SiteDTO site,
                     List<String> licenseTypes) {
        super(id, password, fullName, workStartingDate, wage, wageType, yearlySickDays, yearlyDaysOff, roles, site);
        this.licenseTypes = licenseTypes != null ? licenseTypes : new ArrayList<>();
    }

    public List<String> getLicenseTypes() {
        return licenseTypes;
    }

    public void setLicenseTypes(List<String> licenseTypes) {
        this.licenseTypes = licenseTypes != null ? licenseTypes : new ArrayList<>();
    }
}
