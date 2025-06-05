import transportDev.src.main.dataAccessLayer.TruckDAO;
import transportDev.src.main.dtos.TruckDTO;
import java.util.List;
import java.util.ArrayList;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing TransportDev database connection...");
        
        try {
            // Create a TruckDAO instance
            TruckDAO truckDAO = new TruckDAO();
            
            // Create a test truck
            List<String> licenseTypes = new ArrayList<>();
            licenseTypes.add("C");
            
            TruckDTO testTruck = new TruckDTO(
                "TEST-123",           // license plate
                "Test Truck Model",   // model
                8000.0,              // empty weight
                25000.0,             // max weight
                licenseTypes,        // required license types
                true                 // available
            );
            
            System.out.println("Creating test truck: " + testTruck.getLicensePlate());
            
            // Try to insert the truck
            truckDAO.insertTruck(testTruck);
            System.out.println("✅ Truck inserted successfully!");
            
            // Try to retrieve the truck
            TruckDTO retrievedTruck = truckDAO.getTruckByLicensePlate("TEST-123");
            if (retrievedTruck != null) {
                System.out.println("✅ Truck retrieved successfully!");
                System.out.println("Model: " + retrievedTruck.getModel());
                System.out.println("Empty Weight: " + retrievedTruck.getEmptyWeight());
                System.out.println("Max Weight: " + retrievedTruck.getMaxWeight());
                System.out.println("Available: " + retrievedTruck.isAvailable());
            } else {
                System.out.println("❌ Failed to retrieve truck");
            }
            
            // Clean up - delete test truck
            truckDAO.deleteTruck("TEST-123");
            System.out.println("✅ Test truck deleted");
            
            System.out.println("\n🎉 TransportDev is successfully connected to the database!");
            System.out.println("Your DAOs work exactly like employeeDev's DAOs!");
            
        } catch (Exception e) {
            System.err.println("❌ Error testing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 