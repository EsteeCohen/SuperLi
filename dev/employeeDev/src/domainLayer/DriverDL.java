package employeeDev.src.domainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;
;

public class DriverDL extends EmployeeDL {
    private List<LicenseType> licenseTypes;

    public DriverDL(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, Site site, List<LicenseType> licenseTypes, RoleDL driverRole) {
        super(id, password, fullName, wordStartingDate, wage, wageTypeChar, yearlySickDays, yearlyDaysOff, site);
        this.licenseTypes = licenseTypes;
        this.addRole(driverRole);
    }

    public DriverDL(String id, String password, String fullName, LocalDate wordStartingDate, int wage, char wageTypeChar, int yearlySickDays, int yearlyDaysOff, Site site, RoleDL driverRole){
        super(id, password, fullName, wordStartingDate, wage, wageTypeChar, yearlySickDays, yearlyDaysOff, site);
        this.licenseTypes = new ArrayList<>();
        this.addRole(driverRole);
    }

    public List<LicenseType> getLicenseTypes() {
        return licenseTypes;
    }

    public void setLicenseTypes(List<LicenseType> licenseTypes) {
        this.licenseTypes = licenseTypes;
    }

    public boolean isLicensed(LicenseType licenseType) {
        return licenseTypes.contains(licenseType);
    }

    public void addLicenseType(LicenseType licenseType) {
        if(!licenseTypes.contains(licenseType)) {
            licenseTypes.add(licenseType);
        }
    }

    public void removeLicenseType(LicenseType licenseType) {
        if(licenseTypes.contains(licenseType)) {
            licenseTypes.remove(licenseType);
        }
        throw new IllegalArgumentException("License type not found: " + licenseType);
    }

    @Override
    public boolean isDriver() {
        return true;
    }

    
}
