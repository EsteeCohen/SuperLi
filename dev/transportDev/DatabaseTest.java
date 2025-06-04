import java.sql.*;

public class DatabaseTest {
    
    private static final String DB_PATH = "../db.db";
    
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        // Test database connection
        testConnection();
        
        // Show database table structure
        showDatabaseStructure();
        
        // Check employee data
        checkEmployeeData();
        
        // Check transport data
        checkTransportData();
    }
    
    private static void testConnection() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            if (conn != null) {
                System.out.println("Database connection successful!");
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database location: " + DB_PATH);
                System.out.println("SQLite version: " + meta.getDriverVersion());
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
    
    private static void showDatabaseStructure() {
        System.out.println("\nDatabase structure:");
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", null);
            
            System.out.println("Existing tables:");
            while (tables.next()) {
                String tableName = tables.getString(3);
                System.out.println("  - " + tableName);
            }
        } catch (SQLException e) {
            System.out.println("Error reading database structure: " + e.getMessage());
        }
    }
    
    private static void checkEmployeeData() {
        System.out.println("\nChecking employee data:");
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            // Check employees table
            String query = "SELECT COUNT(*) as count FROM Employees";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.println("Number of employees in system: " + count);
                }
            }
            
            // Show employee examples
            query = "SELECT id, fullName, phoneNumber FROM Employees LIMIT 3";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("Sample employees:");
                while (rs.next()) {
                    System.out.printf("  - ID: %s, Name: %s, Phone: %s%n", 
                        rs.getString("id"), 
                        rs.getString("fullName"), 
                        rs.getString("phoneNumber"));
                }
            }
            
            // Check sites table
            query = "SELECT COUNT(*) as count FROM Sites";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.println("Number of sites in system: " + count);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading employee data: " + e.getMessage());
        }
    }
    
    private static void checkTransportData() {
        System.out.println("\nChecking transport tables:");
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            String[] transportTables = {
                "Transports", "Trucks", "Drivers", "Orders", "Items"
            };
            
            for (String tableName : transportTables) {
                try {
                    String query = "SELECT COUNT(*) as count FROM " + tableName;
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {
                        if (rs.next()) {
                            int count = rs.getInt("count");
                            System.out.println(tableName + ": " + count + " records");
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Table " + tableName + " does not exist or is empty");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error checking transport data: " + e.getMessage());
        }
    }
} 