package employeeDev.src.serviceLayer.Interfaces;

import employeeDev.src.serviceLayer.DriverSL;
import java.time.LocalDateTime;
import java.util.List;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

public interface  DriverInfoInterface {

    List<DriverSL> getAvailableDriversWithLicense(LocalDateTime time, Site site, LicenseType licenseType);

    DriverSL getDriverById(String id);

    List<DriverSL> getAllDrivers();

    List<DriverSL> getDriversByLicenseType(LicenseType licenseType);

    boolean canDriverDrive(String driverID, LicenseType licenseType);

    void setAvailableToDrive(String driverID,boolean isAvailable);
    
}
