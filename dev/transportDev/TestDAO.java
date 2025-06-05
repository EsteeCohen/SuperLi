// import transportDev.src.main.dataAccessLayer.*;
// import transportDev.src.main.dtos.*;
// import java.util.List;
// import java.util.Arrays;

// public class TestDAO {
    
//     public static void main(String[] args) {
//         System.out.println("Testing DAOs we created...");
        
//         // Test TruckDAO
//         testTruckDAO();
        
//         // Test ItemDAO  
//         testItemDAO();
        
//         // Test SiteDAO
//         testSiteDAO();
        
//         System.out.println("All tests completed successfully!");
//     }
    
//     private static void testTruckDAO() {
//         System.out.println("\nTesting TruckDAO:");
        
//         TruckDAO truckDAO = new TruckDAO();
        
//         // Get all trucks
//         List<TruckDTO> trucks = truckDAO.getAllTrucks();
//         System.out.println("Number of trucks in system: " + trucks.size());
        
//         for (TruckDTO truck : trucks) {
//             System.out.printf("  - Truck %s, Model: %s, Empty weight: %.0f kg, Max weight: %.0f kg%n", 
//                 truck.getLicensePlate(), 
//                 truck.getModel(),
//                 truck.getEmptyWeight(),
//                 truck.getMaxWeight());
            
//             // Show required license types
//             System.out.println("    Required licenses: " + String.join(", ", truck.getRequiredLicenseTypes()));
//         }
        
//         // Check available trucks
//         List<TruckDTO> availableTrucks = truckDAO.getAvailableTrucks();
//         System.out.println("Available trucks: " + availableTrucks.size());
//     }
    
//     private static void testItemDAO() {
//         System.out.println("\nTesting ItemDAO:");
        
//         ItemDAO itemDAO = new ItemDAO();
        
//         // Get all items
//         List<ItemDTO> items = itemDAO.getAllItems();
//         System.out.println("Number of items in system: " + items.size());
        
//         for (ItemDTO item : items) {
//             System.out.printf("  - Item %d: %s, Weight: %.1f kg, Quantity: %d%n", 
//                 item.getId(),
//                 item.getName(),
//                 item.getWeight(),
//                 item.getQuantity());
//             System.out.println("    Description: " + item.getDescription());
//             System.out.println("    Total weight: " + item.getTotalWeight() + " kg");
//         }
        
//         // Search items by name
//         List<ItemDTO> milkItems = itemDAO.getItemsByName("Milk");
//         System.out.println("Milk items found: " + milkItems.size());
//     }
    
//     private static void testSiteDAO() {
//         System.out.println("\nTesting SiteDAO:");
        
//         SiteDAO siteDAO = new SiteDAO();
        
//         // Get all sites
//         List<SiteDTO> sites = siteDAO.getAllSites();
//         System.out.println("Number of sites in system: " + sites.size());
        
//         for (SiteDTO site : sites) {
//             System.out.printf("  - Site: %s%n", site.getName());
//             if (site.getAddress() != null) {
//                 System.out.println("    Address: " + site.getAddress());
//             }
//             if (site.getContactPerson() != null) {
//                 System.out.println("    Contact person: " + site.getContactPerson());
//             }
//             if (site.getShippingZone() != null) {
//                 System.out.println("    Shipping zone: " + site.getShippingZone());
//             }
//         }
        
//         // Search sites by shipping zone
//         List<SiteDTO> centerSites = siteDAO.getSitesByShippingZone("Center");
//         System.out.println("Sites in Center zone: " + centerSites.size());
//     }
// } 