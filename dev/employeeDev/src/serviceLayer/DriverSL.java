package employeeDev.src.serviceLayer;

import java.util.List;

import employeeDev.src.domainLayer.DriverDL;
import transportDev.src.main.enums.LicenseType;

public class DriverSL {
    private String id;
    private String fullName;
    private List<LicenseType> licenseTypes;

    public DriverSL(String id, String fullName, List<LicenseType> licenseTypes) {
        this.id = id;
        this.fullName = fullName;
        this.licenseTypes = licenseTypes;
    }

    public DriverSL(DriverDL driverDL) {
        this.id = driverDL.getId();
        this.fullName = driverDL.getFullName();
        this.licenseTypes = driverDL.getLicenseTypes();
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public List<LicenseType> getLicenseTypes() {
        return licenseTypes;
    }

    public boolean isLicensed(LicenseType licenseType) {
        return licenseTypes != null && licenseTypes.contains(licenseType);
    }
}
