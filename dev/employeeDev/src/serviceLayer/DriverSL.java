package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.DriverDL;
import transportDev.src.main.enums.LicenseType;

public class DriverSL extends EmployeeSL {
    
    private LicenseType licenseType;

    public DriverSL(DriverDL driverDL) {
        super(driverDL);
        this.licenseType = driverDL.getLicenseType();
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public boolean isLicensed(LicenseType licenseType) {
        return licenseType.equals(this.licenseType);
    }
}
