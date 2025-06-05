package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.DriverDL;
import transportDev.src.main.enums.LicenseType;

public class DriverSL extends EmployeeSL {
    
    private LicenseType licenseType;
    private boolean isAvailableToDrive;

    public DriverSL(DriverDL driverDL) {
        super(driverDL);
        this.licenseType = driverDL.getLicenseType();
        this.isAvailableToDrive = driverDL.isAvailableToDrive();
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public boolean isLicensed(LicenseType licenseType) {
        return licenseType.equals(this.licenseType);
    }

    public boolean isAvailableToDrive() {
        return isAvailableToDrive;
    }

    public boolean canDrive(LicenseType licenseType) {
        return isLicensed(licenseType) && isAvailableToDrive();
    }

    public void setAvailableToDrive(boolean isAvailable) {
        this.isAvailableToDrive = isAvailable;
    }
}
