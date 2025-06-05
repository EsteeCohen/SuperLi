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

    void setAvailableToDrive(String driverID,boolean isAvailable);

    void addDriver(String fullName, String password, String id, int wage, String wageType, int yearlySickDays, int yearlyDaysOff, String siteName, String phoneNumber, LicenseType licenseType);
    
}
