// package transportDev.src.main.services;

// import java.util.ArrayList;
// import java.util.List;
// import transportDev.src.main.entities.*;
// import transportDev.src.main.enums.LicenseType;

// public class DriverService {
//     // :)
//     private List<Driver> drivers;

//     //------------- constructor -------------
//     public DriverService() {
//         this.drivers = new ArrayList<>();
//     }
//     //------------- methods -------------
//     public Driver addDriver(String id, String name, String phone, LicenseType licenseType){
//         if (id == null || name == null || phone == null || licenseType == null) {
//             throw new IllegalArgumentException("All fields are required");
//         }
//         if(getDriverById(id) != null){
//             throw new IllegalArgumentException("Driver with this id already exists");
//         }
//         Driver driver = new Driver(id, name, phone, licenseType);
//         drivers.add(driver);
//         return driver;
//     }

//     // ####
//     public Driver getDriverById(String id) {
//         for (Driver driver : drivers) {
//             if (driver.getId().equals(id)) {
//                 return driver;
//             }
//         }
//         return null;
//     }

//     // ####
//     public List<Driver> getAllDrivers() {
//         return new ArrayList<>(drivers);
//     }

//     // ####
//     public List<Driver> getDriversByLicenseType(LicenseType licenseType) {
//         if (licenseType == null) {
//             throw new IllegalArgumentException("License type is required");
//         }
//         List<Driver> filteredDrivers = new ArrayList<>();
//         for (Driver driver : drivers) {
//             if (driver.getLicenseType() == licenseType) {
//                 filteredDrivers.add(driver);
//             }
//         }
//         return filteredDrivers;
//     }
    

//     public List<Driver> getEligibleDriversForTruck(Truck truck) {
//         if (truck == null) {
//             throw new IllegalArgumentException("Truck is required");
//         }
//         List<Driver> eligibleDrivers = new ArrayList<>();
//         for (Driver driver : drivers) {
//             if (driver.canDrive(truck)) {
//                 eligibleDrivers.add(driver);
//             }
//         }
//         return eligibleDrivers;
//     }

//     public void updateDriver(String id, String name, String phone, LicenseType licenseType) {
//         Driver driver = getDriverById(id);
//         if (driver == null) {
//             throw new IllegalArgumentException("Driver with this id does not exist");
//         }
//         if (name != null) {
//             driver.setName(name);
//         }
//         if (phone != null) {
//             driver.setPhone(phone);
//         }
//         if (licenseType != null) {
//             driver.setLicenseType(licenseType);
//         }
//     }


//     public boolean deleteDriver(String id) {
//         Driver driver = getDriverById(id);
//         if (driver == null) {
//             return false;
//         }
//         drivers.remove(driver);
//         return true;
//     }

//     public void clearAllDrivers() {
//         drivers.clear();
//     }

//     // ####
//     public List<Driver> getAvailableDriversWithLicense(LicenseType requiredLicense, TransportService transportService) {
//         return drivers.stream()
//             .filter(driver -> driver.getLicenseType() == requiredLicense)
//             .filter(driver -> transportService.isDriverAvailable(driver.getId()))
//             .collect(java.util.stream.Collectors.toList());
//     }

// }
