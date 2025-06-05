package transportDev.src.main.mappers;

import transportDev.src.main.dtos.TruckDTO;
import transportDev.src.main.entities.Truck;
import transportDev.src.main.enums.LicenseType;
import java.util.ArrayList;
import java.util.List;

public class TruckMapper {

    public static TruckDTO toDTO(Truck truck) {
        if (truck == null) {
            return null;
        }

        List<String> requiredLicenseTypes = new ArrayList<>();
        requiredLicenseTypes.add(truck.getLicenseType().getTypeInString());

        return new TruckDTO(
                truck.getRegNumber(),
                truck.getModel(),
                truck.getEmptyWeight(),
                truck.getMaxWeight(),
                requiredLicenseTypes,
                truck.isAvailable()
        );
    }

    public static Truck fromDTO(TruckDTO truckDTO) {
        if (truckDTO == null) {
            return null;
        }

        // Get the primary license type from the list
        LicenseType licenseType = null;
        if (truckDTO.getRequiredLicenseTypes() != null && !truckDTO.getRequiredLicenseTypes().isEmpty()) {
            licenseType = LicenseType.fromString(truckDTO.getRequiredLicenseTypes().get(0));
        }

        Truck truck = new Truck(
                truckDTO.getLicensePlate(),
                truckDTO.getModel(),
                truckDTO.getEmptyWeight(),
                truckDTO.getMaxWeight(),
                licenseType
        );

        // Set availability
        if (truckDTO.isAvailable()) {
            truck.available();
        } else {
            truck.unavailable();
        }

        return truck;
    }
} 