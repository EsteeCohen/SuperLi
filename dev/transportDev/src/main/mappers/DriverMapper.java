package transportDev.src.main.mappers;

import employeeDev.src.serviceLayer.DriverSL;
import transportDev.src.main.dtos.DriverDTO;
import transportDev.src.main.enums.LicenseType;
import java.util.ArrayList;
import java.util.List;

public class DriverMapper {

    public static DriverDTO toDTO(DriverSL driver) {
        if (driver == null) {
            return null;
        }

        List<String> licenseTypes = new ArrayList<>();
        // Convert from DriverSL's license type to string list
        if (driver.getLicenseType() != null) {
            licenseTypes.add(driver.getLicenseType().getTypeInString());
        }

        return new DriverDTO(
                driver.getId(),
                driver.getFullName(),
                driver.getPhoneNumber(),
                licenseTypes,
                driver.isAvailableToDrive()
        );
    }

    public static DriverSL fromDTO(DriverDTO driverDTO) {
        // Note: This method should typically not be used directly 
        // since DriverSL objects should be created through the employeeDev facade
        // This is here for completeness but should be used with caution
        if (driverDTO == null) {
            return null;
        }

        // This would require creating a DriverSL instance, which should be done
        // through the proper employeeDev facade/service layer
        throw new UnsupportedOperationException(
            "Creating DriverSL from DTO should be done through employeeDev facade. " +
            "Use DriverInfoInterface.getDriverById() instead."
        );
    }
} 