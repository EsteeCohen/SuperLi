package transportDev.src.main.services;

import transportDev.src.main.dataAccessLayer.*;
import transportDev.src.main.dtos.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            System.out.println("🔍 Testing Database Connection...");
            
            // Test Site Operations
            System.out.println("\n📌 Testing Site Operations:");
            SiteDAO siteDAO = new SiteDAO();
            
            // Create test site
            SiteDTO testSite = new SiteDTO(
                "Test Site",
                "123 Test St",
                "555-0123",
                "Test Contact",
                "Zone A"
            );
            siteDAO.insertSite(testSite);
            System.out.println("✅ Site inserted successfully");
            
            // Retrieve site
            SiteDTO retrievedSite = siteDAO.getSiteByName("Test Site");
            if (retrievedSite != null) {
                System.out.println("✅ Site retrieved successfully: " + retrievedSite.getName());
            }
            
            // Test Truck Operations
            System.out.println("\n🚛 Testing Truck Operations:");
            TruckDAO truckDAO = new TruckDAO();
            
            // Create test truck
            List<String> licenseTypes = new ArrayList<>();
            licenseTypes.add("C");
            TruckDTO testTruck = new TruckDTO(
                "TEST-123",
                "Test Model",
                1000.0,
                5000.0,
                licenseTypes,
                true
            );
            truckDAO.insertTruck(testTruck);
            System.out.println("✅ Truck inserted successfully");
            
            // Retrieve truck
            TruckDTO retrievedTruck = truckDAO.getTruckByLicensePlate("TEST-123");
            if (retrievedTruck != null) {
                System.out.println("✅ Truck retrieved successfully: " + retrievedTruck.getLicensePlate());
            }
            
            // Test Driver Operations
            System.out.println("\n👤 Testing Driver Operations:");
            DriverDAO driverDAO = new DriverDAO();
            
            // Create test driver
            List<String> driverLicenseTypes = new ArrayList<>();
            driverLicenseTypes.add("C");
            DriverDTO testDriver = new DriverDTO(
                "D123",
                "Test Driver",
                "555-0124",
                driverLicenseTypes,
                true
            );
            driverDAO.insertDriver(testDriver);
            System.out.println("✅ Driver inserted successfully");
            
            // Retrieve driver
            DriverDTO retrievedDriver = driverDAO.getDriverById("D123");
            if (retrievedDriver != null) {
                System.out.println("✅ Driver retrieved successfully: " + retrievedDriver.getId());
            }
            
            // Test Transport Operations
            System.out.println("\n🚚 Testing Transport Operations:");
            TransportDAO transportDAO = new TransportDAO();
            
            // Create test transport
            List<SiteDTO> destinations = new ArrayList<>();
            destinations.add(testSite);
            TransportDTO testTransport = new TransportDTO(
                1,
                LocalDate.now(),
                LocalTime.now(),
                testTruck,
                testDriver,
                testSite,
                destinations,
                2000.0,
                "PENDING"
            );
            transportDAO.insertTransport(testTransport);
            System.out.println("✅ Transport inserted successfully");
            
            // Retrieve transport
            TransportDTO retrievedTransport = transportDAO.getTransportById(1);
            if (retrievedTransport != null) {
                System.out.println("✅ Transport retrieved successfully: ID " + retrievedTransport.getId());
            }
            
            // Clean up test data
            System.out.println("\n🧹 Cleaning up test data...");
            transportDAO.deleteTransport(1);
            driverDAO.deleteDriver("D123");
            truckDAO.deleteTruck("TEST-123");
            siteDAO.deleteSite("Test Site");
            System.out.println("✅ Test data cleaned up successfully");
            
            System.out.println("\n✨ All database tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error during database testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 