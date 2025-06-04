package employeeDev.src.serviceLayer.Interfaces;

import employeeDev.src.domainLayer.DriverDL;
import java.time.LocalDateTime;
import java.util.List;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

public interface  DriverInfoInterface {

    List<DriverDL> getAvailableDriversWithLicense(LocalDateTime time, Site site, LicenseType licenseType);

    DriverDL getDriverById(String id);

    List<DriverDL> getAllDrivers();

    List<DriverDL> getDriversByLicenseType(LicenseType licenseType);
    
}
